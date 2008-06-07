package aplicacion.plugin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JOptionPane;

import Deventos.enlaceJS.DConector;
import aplicacion.plugin.deventos.DPluginRegisterEvent;

import componentes.base.DComponente;
import componentes.base.DComponenteBase;
import componentes.base.HebraProcesadoraBase;
import Deventos.DEvent;

/**
 * Clase abstracta para implementar los plugins de la plataforma
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public abstract class DAbstractPlugin extends DComponenteBase
{
	// categorias de los plugins, para permitir su clasificacion
	public static final int CATEGORIA_MULTIMEDIA = 0;

	public static final int CATEGORIA_COMUNICACION = 1;

	public static final int CATEGORIA_DESARROLLO = 2;

	public static final int CATEGORIA_EDICION = 3;

	public static final int CATEGORIA_OCIO = 4;

	public static final int CATEGORIA_INFORMACION = 5;

	public static final int CATEGORIA_UTILIDADES = 6;

	public static final int CATEGORIA_CIENTIFICO = 7;

	public static final int CATEGORIA_OTROS = 8;

	/**
	 * Nombre del plugin
	 */
	protected String nombre = "";

	/**
	 * Version del plugin. Solo se tiene en cuenta cuando activamos el campo
	 * versioningEnabled
	 */
	protected long version = 0;

	/**
	 * Nombre del fichero .jar que contendra las clases del plugin implementado
	 */
	protected String jarFile = "";

	/**
	 * Indica si queremos activar la gestion de versiones del plugin
	 * implementado
	 */
	protected boolean versioningEnabled = false;

	/**
	 * Indicamos si por defecto se deberia mostrar este plugin
	 */
	protected boolean shouldShow = true;

	/**
	 * Indica la categoria a la que pertenece el plugin
	 */
	protected int categoria = CATEGORIA_OTROS;

	/**
	 * Descripcion del plugin
	 */
	protected String descripcion = "";

	private Integer ultimoProcesado = new Integer(-1);

	/**
	 * Constructor
	 * 
	 * @param nombre
	 *            Nombre del componente
	 * @param conexionDC
	 * @param padre
	 * @throws Exception
	 */
	protected DAbstractPlugin( String nombre, boolean conexionDC,
			DComponenteBase padre ) throws Exception
	{
		super(nombre, conexionDC, padre);
		
		register();
	}

	/**
	 * Obtiene una instancia de la clase que implementa
	 * el plugin
	 * @return Instancia de la clase que implementa al plugin
	 * @throws Exception
	 */
	public abstract DAbstractPlugin getInstance() throws Exception;

	/**
	 * Inicializa los parametros del plugin
	 * @throws Exception
	 */
	public abstract void init() throws Exception;

	/**
	 * Comienza la ejecucion del plugin
	 * @throws Exception
	 */
	public abstract void start() throws Exception;

	/**
	 * Para la ejecucion del plugin
	 * @throws Exception
	 */
	public abstract void stop() throws Exception;

	/**
	 * Envia un evento de registro del plugin en la plataforma. Permite
	 * distribuir los plugins al resto de usuarios.
	 */
	public final void register()
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

	/**
	 * Permite obtener la version que tiene el plugin implementado
	 * 
	 * @return Version del plugin
	 */
	public long getVersion()
	{
		return version;
	}

	/**
	 * Permite obtener el nombre del fichero jar que contiene la implementacion
	 * del plugin
	 * 
	 * @return Nombre del fichero jar que contiene la implementacion del plugin
	 */
	public String getJarFileName()
	{
		return jarFile;
	}

	@Override
	public String toString()
	{
		return nombre;
	}

	/**
	 * Indica la categoria a la que pertenece el plugin
	 * 
	 * @return Categoria a la que pertenece el plugin
	 */
	public int getCategoria()
	{
		return categoria;
	}

	/**
	 * Asigna una categoria al plugin
	 * 
	 * @param categ
	 *            Categoria que se desea asignar al plugin
	 */
	public void setCategoria(int categ)
	{
		categoria = categ;
	}

	/**
	 * Obtiene la descripcion del plugin
	 * 
	 * @return Descripcion del plugin
	 */
	public String getDescripcion()
	{
		return descripcion;
	}

	/**
	 * Obtiene la informacion de estado del plugin
	 * 
	 * @return Evento con la informacion de estado del plugin.
	 */
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

	/**
	 * Indica si debemos mostrar de forma predeterminada el plugin o no.
	 * 
	 * @return True si el plugin debe ser mostrado de forma predeterminada.
	 *         False en caso contrario.
	 */
	public boolean shouldShowIt()
	{
		return shouldShow;
	}

	/**
	 * Asigna si el plugin debe ser mostrado de forma predeterminada o no.
	 * 
	 * @param v
	 *            Variable que indica si debemos o no mostrar el plugin de forma
	 *            predeterminada.
	 */
	public void setShouldShowit(boolean v)
	{
		this.shouldShow = v;
	}
	
	/**
	 * Obtiene el nombre del fichero jar que contiene 
	 * la implementacion del plugin
	 * @return Fichero jar que contiene la implementacion del plugin
	 */
	public String getJarFile()
	{
		return jarFile;
	}

	/**
	 * Envia el fichero jar que contiene al plugin hacia una direccion IP de
	 * destino y a un path concreto
	 * 
	 * @param ipdestino
	 *            IP de destino del fichero jar
	 * @param pathdestino
	 *            Path de destino del fichero jar
	 * @pre En el destino el servidor RMI debe estar iniciado
	 */
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

	/**
	 * Obtiene un fichero jar con un plugin desde la direccion IP de origen y lo
	 * guarda en un path de destino localmente.
	 * 
	 * @param iporigen
	 *            IP desde la que descargaremos el plugin
	 * @param pathorigen
	 *            Path de destino local donde guardaremos el plugin descargado
	 */
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
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Procesa un evento de registro de un plugin
	 * en la plataforma. Si esta habilitada la gestion
	 * de versiones, entonces indica con que version
	 * deseamos quedarnos
	 * @param dp Evento de registro recibido
	 */
	private void procesarEvento(DPluginRegisterEvent dp)
	{		
		int res; // para almacenar el resultado de los confirm dialog's
		if (dp.tipo.intValue() == DPluginRegisterEvent.RESPUESTA_SINCRONIZACION
				.intValue())
			if (( dp.version > getVersion() ) && this.versioningEnabled
					&& !dp.usuario.equals(DConector.Dusuario)
					&& ( getName().equals(dp.nombre) ))
			{

				Object[] options =
				{ "Instalar Nueva Versiñn", "Conservar Versiñn Actual" };

				res = JOptionPane
						.showOptionDialog(null,
								"Hay una nueva version del plug-in "
										+ getName() + " (Version Actual: "
										+ getVersion() + ", Version Nueva: "
										+ dp.version,
								"Nuevo Version Disponible",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[1]);

				if (res == JOptionPane.YES_OPTION)
				{
					// hacer una recepcion del fichero plugin remoto y
					// sobreescribir fichero local
					receiveMe(dp.ip, dp.jarPath);

					// reconstruir los plugins

					Object[] options2 =
					{ "Reiniciar Ahora", "Mñs tarde" };

					res = JOptionPane
							.showOptionDialog(
									null,
									"Debera reiniciar la aplicacion para que la nueva version del plugin: "
											+ getName()
											+ " funcione correctamente. ñCerrarla ahora?",
									"Aviso", JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE, null,
									options2, options2[1]);
					if (res == JOptionPane.YES_OPTION)
					{
						DConector.obtenerDC().salir();
						System.exit(0);
					}

					// podria probarse:
					// aplicacion.gui.PanelPrincipal.removeAll();
					// aplicacion.gui.PanelPrincipal.add(DPluginLoader.getAllPlugins(...))
				}
			}

			else if (( dp.version < getVersion() ) && this.versioningEnabled
					&& !dp.usuario.equals(DConector.Dusuario)
					&& ( getName().equals(dp.nombre) )) // enviar nuestro
														// fichero
				// jar
				sendMe(dp.ip, dp.jarPath);
	}

	/**
	 * Hebra encargada de procesar los eventos
	 */
	private class HebraProcesadora extends HebraProcesadoraBase
	{
		/**
		 * Contructor
		 * @param dc Componente al cual procesar los eventos
		 */
		HebraProcesadora( DComponente dc )
		{
			super(dc);
		}

		/**
		 * Ejecucion de la hebra
		 */
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
				saux = vaux.elementAt(j);
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
						&& ( getName().equals(evento.nombre) ))
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
