package aplicacion.plugin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JOptionPane;

import Deventos.DEvent;
import Deventos.enlaceJS.DConector;
import aplicacion.plugin.deventos.DPluginRegisterEvent;

import componentes.base.DComponente;
import componentes.base.DComponenteBase;
import componentes.base.HebraProcesadoraBase;

public abstract class DAbstractPlugin extends DComponenteBase
{
	protected DAbstractPlugin( String nombre, boolean conexionDC,
			DComponenteBase padre ) throws Exception
	{
		super(nombre, conexionDC, padre);

		register();
	}

	protected String nombre;

	protected long version;

	protected String jarFile;

	protected boolean started;

	protected boolean versioningEnabled;

	private Integer ultimoProcesado = new Integer(-1);

	public void register()
	{
		HebraProcesadora th = new HebraProcesadora(this);
		th.iniciarHebra();

		DPluginRegisterEvent evt = obtenerInfoEstado();
		evt.tipo = new Integer(DPluginRegisterEvent.SINCRONIZACION.intValue());

		enviarEvento(evt);
	}

	@Override
	public String getName()
	{
		return nombre;
	}

	public long getVersion()
	{
		return version;
	}

	public String getJarFileName()
	{
		return jarFile;
	}

	public boolean isStarted()
	{
		return started;
	}

	@Override
	public String toString()
	{
		return nombre;
	}

	protected DPluginRegisterEvent obtenerInfoEstado()
	{
		DPluginRegisterEvent dpre = new DPluginRegisterEvent();
		dpre.nombre = getName();
		dpre.version = getVersion();
		try
		{
			dpre.ip = InetAddress.getLocalHost().getHostName();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		dpre.jarPath = getJarFileName();

		return dpre;
	}

	public abstract DAbstractPlugin getInstance() throws Exception;

	public abstract void init() throws Exception;

	public abstract void start() throws Exception;

	public abstract void stop() throws Exception;

	private void sendMe(String ipdestino, String pathdestino)
	{
		aplicacion.fisica.net.Transfer.establecerServidor();
		aplicacion.fisica.net.Transfer t = new aplicacion.fisica.net.Transfer(
				ipdestino, pathdestino);

		byte[] bytes = null;
		try
		{
			RandomAccessFile raf = new RandomAccessFile(getJarFileName(), "r");

			int tamanio = (int) raf.length();

			bytes = new byte[tamanio];

			raf.read(bytes);

			raf.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		t.sendFile(bytes);
	}

	private void receiveMe(String iporigen, String pathorigen)
	{
		aplicacion.fisica.net.Transfer.establecerServidor();
		aplicacion.fisica.net.Transfer t = new aplicacion.fisica.net.Transfer(
				iporigen, pathorigen);

		byte[] bytes = t.receiveFileBytes();

		try
		{
			RandomAccessFile raf = new RandomAccessFile(getJarFileName(), "rw");
			raf.write(bytes);
			raf.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void procesarEvento(DPluginRegisterEvent dp)
	{
		int res; // para almacenar el resultado de los confirm dialog's
		if (dp.tipo == DPluginRegisterEvent.RESPUESTA_SINCRONIZACION)
			if (( dp.version > getVersion() ) && this.versioningEnabled
					&& !dp.usuario.equals(DConector.Dusuario)
					&& ( getName() == dp.nombre ))
			{
				res = JOptionPane.showConfirmDialog(null,
						"Hay una nueva version del plug-in " + getName()
								+ " (Version Actual: " + getVersion()
								+ ", Version Nueva: " + dp.version, "Aviso",
						JOptionPane.YES_NO_OPTION);

				if (res == JOptionPane.YES_OPTION)
				{
					// hacer una recepcion del fichero plugin remoto y
					// sobreescribir fichero local
					receiveMe(dp.ip, dp.jarPath);

					// pedir reiniciar la aplicacion
					res = JOptionPane
							.showConfirmDialog(
									null,
									"Debera reiniciar la aplicacion para que la nueva version del plugin: "
											+ getName()
											+ " funcione correctamente. ÀCerrarla ahora?",
									"Aviso", JOptionPane.YES_NO_OPTION);
					if (res == JOptionPane.YES_OPTION)
					{
						System.exit(0);
					}
				}
			}

			else if (( dp.version < getVersion() ) && this.versioningEnabled
					&& !dp.usuario.equals(DConector.Dusuario)
					&& ( getName() == dp.nombre )) // enviar nuestro fichero
													// jar
				sendMe(dp.ip, dp.jarPath);
	}

	private class HebraProcesadora extends HebraProcesadoraBase
	{
		HebraProcesadora( DComponente dc )
		{
			super(dc);
		}

		@Override
		public void run()
		{
			DPluginRegisterEvent evento = null;
			DPluginRegisterEvent saux = null;
			DPluginRegisterEvent respSincr = null;
			Vector<DPluginRegisterEvent> vaux = new Vector<DPluginRegisterEvent>();

			DEvent[] eventos = obtenerEventosColaRecepcion();
			int numEventos = eventos.length;

			// Buscamos si se ha recibido una respuesta de sincronizacion
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DPluginRegisterEvent) eventos[j];
				if (( respSincr == null )
						&& ( saux.tipo.intValue() == DPluginRegisterEvent.RESPUESTA_SINCRONIZACION
								.intValue() ))
					respSincr = saux;
				else vaux.add(saux);
			}

			if (respSincr != null) procesarEvento(respSincr);

			// Colocamos en la cola de recepcion los eventos que deben ser
			// procesados (recibidos mientras se realizaba la sincronizacion )
			numEventos = vaux.size();
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DPluginRegisterEvent) vaux.elementAt(j);
				if (saux.ultimoProcesado.intValue() > ultimoProcesado
						.intValue()) procesarEvento(saux);
			}

			while (true)
			{
				evento = (DPluginRegisterEvent) leerSiguienteEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());
				if (( evento.tipo.intValue() == DPluginRegisterEvent.SINCRONIZACION
						.intValue() )
						&& !evento.usuario.equals(DConector.Dusuario)
						&& ( getName() == evento.nombre ))
				{
					DPluginRegisterEvent infoEstado = obtenerInfoEstado();
					infoEstado.tipo = new Integer(
							DPluginRegisterEvent.RESPUESTA_SINCRONIZACION
									.intValue());

					enviarEvento(infoEstado);
				}
				else procesarEvento(evento);
			}
		}
	}
}
