package aplicacion.plugin;

import interfaces.DComponente;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JOptionPane;

import aplicacion.plugin.deventos.DPluginRegisterEvent;

import Deventos.DEvent;
import Deventos.enlaceJS.DConector;

import componentes.DComponenteBase;
import componentes.HebraProcesadoraBase;

public abstract class DAbstractPlugin extends DComponenteBase
{	
	protected DAbstractPlugin(String nombre, boolean conexionDC, DComponenteBase padre)
	{
		super(nombre, conexionDC, padre);
		
		try
		{
			init();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected String nombre;
	protected long version;
	protected String jarFile;
	protected boolean started;
	protected boolean versioningEnabled;
	
	private Integer ultimoProcesado = new Integer(-1);

	public void register()
	{

	}

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
	
	public abstract void init() throws Exception;
	public abstract void start() throws Exception;
	public abstract void stop() throws Exception;
	
	private void sendMe(String ipdestino, String pathdestino)
	{
		aplicacion.fisica.Transfer.establecerServidor();
		aplicacion.fisica.Transfer t = new aplicacion.fisica.Transfer(ipdestino, pathdestino);
		
		byte[] bytes = null; 
		try
		{
			RandomAccessFile raf = new RandomAccessFile(getJarFileName(), "r");
			
			int tamanio = (int)raf.length();
			
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
		aplicacion.fisica.Transfer.establecerServidor();
		aplicacion.fisica.Transfer t = new aplicacion.fisica.Transfer(iporigen, pathorigen);
		
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
		if (dp.tipo == DPluginRegisterEvent.RESPUESTA_SINCRONIZACION)
		{
			if (dp.version > getVersion() && this.versioningEnabled)
				JOptionPane.showMessageDialog(null, "Hay una nueva version del plug-in "+getName()+" (Version Actual: "+getVersion()+", Version Nueva: "+dp.version);
		}
	}
	
	class HebraProcesadora extends HebraProcesadoraBase
	{
		HebraProcesadora( DComponente dc )
		{
			super(dc);
		}

		public void run()
		{
			DPluginRegisterEvent evento = null;
			DPluginRegisterEvent saux = null;
			DPluginRegisterEvent respSincr = null;
			Vector vaux = new Vector();

			DEvent[] eventos = obtenerEventosColaRecepcion();
			int numEventos = eventos.length;
			int i = 0;

			// Buscamos si se ha recibido una respuesta de sincronizacion
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DPluginRegisterEvent) eventos[j];
				if (( respSincr == null ) && ( saux.tipo.intValue() == DPluginRegisterEvent.RESPUESTA_SINCRONIZACION.intValue() ))
				{
					respSincr = saux;
				}
				else
				{
					vaux.add(saux);
				}
			}

			if (respSincr != null)
			{ // Se ha recibido respuesta de sincronizacion
				procesarEvento(respSincr);
			}

			// Colocamos en la cola de recepcion los eventos que deben ser
			// procesados (recibidos mientras se realizaba la sincronizacion )
			numEventos = vaux.size();
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DPluginRegisterEvent) vaux.elementAt(j);
				if (saux.ultimoProcesado.intValue() > ultimoProcesado.intValue())
				{
					procesarEvento(saux);
				}
			}

			while (true)
			{
				evento = (DPluginRegisterEvent) leerSiguienteEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());
				if (evento.tipo.intValue() == DPluginRegisterEvent.SINCRONIZACION.intValue()
					 && !evento.usuario.equals(DConector.Dusuario) && getName() == evento.nombre)
				{
					DPluginRegisterEvent infoEstado = obtenerInfoEstado();
					infoEstado.tipo = new Integer(DPluginRegisterEvent.RESPUESTA_SINCRONIZACION.intValue());
					infoEstado.nombre = getName();
					try
					{
						infoEstado.ip = InetAddress.getLocalHost().getHostName();
					}
					catch (UnknownHostException e)
					{
						e.printStackTrace();
					}
					infoEstado.version = getVersion();
					infoEstado.jarPath = getJarFileName();
					
					enviarEvento(infoEstado);
				}
				else
				{
					procesarEvento(evento);
				}
			}
		}
	}
}
