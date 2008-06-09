package fisica.net;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import fisica.documentos.Documento;


/**
 * Clase encargada de la transmision de documentos entre usuarios
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class TransferP2P
{
	private String ip_origen;

	private int id;

	private static int port = 8866;

	private static boolean serverExecuted = false;

	private static Thread hebra = null;

	/**
	 * Constructor
	 * 
	 * @param ip_orig
	 *            Direccion IP de origen de la conexion
	 * @param identificador
	 *            Identificador de la conexion a realizar
	 * @param puerto
	 *            Puerto donde se desean realizar las transmisiones
	 */
	public TransferP2P( String ip_orig, int identificador, int puerto )
	{
		ip_origen = ip_orig;
		id = identificador;
	}

	/**
	 * Consulta el puerto donde se encuentra esperando el servidor
	 * 
	 * @return Puerto donde esta en estado "listen" el servidor
	 */
	public static int getPort()
	{
		return port;
	}

	private static synchronized void setServerExecuted(boolean b)
	{
		serverExecuted = b;
	}

	private static synchronized boolean getServerExecuted()
	{
		return serverExecuted;
	}

	/**
	 * Inicia el servidor RMI para la transferencia entre usuarios
	 * 
	 * @param id
	 *            Identificador del objeto registrado
	 * @param doc
	 *            Documento a enviar
	 */
	public synchronized static void establecerServidor(int id, Documento doc)
	{
		if (!getServerExecuted())
		{
			TransferP2P ts = new TransferP2P("", 0, 0);

			ServerThread s = ts.new ServerThread(id, doc);
			hebra = new Thread(s);
			hebra.start();

			setServerExecuted(true);
		}
	}

	/**
	 * Para la ejecucion de la hebra del servidor. Desliga el objeto registrado
	 */
	public synchronized static void pararHebra()
	{
		if (getServerExecuted())
		{

			setServerExecuted(false);

			try
			{
				Thread.sleep(2000L);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * Recepcion del documento desde el servidor RMI
	 * 
	 * @return El documento si todo fue correcto o null en caso contrario
	 */
	public Documento receive()
	{
		try
		{
			TransmisorFicherosP2P ar = (TransmisorFicherosP2P) Naming
					.lookup("//" + ip_origen + ":" + port
							+ "/TransferenciaFicheroP2P" + id);

			return ar.getDocumento();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Hebra encargada de gestionar el servidor RMI
	 */
	private class ServerThread extends Thread
	{
		int id;

		private Documento d;

		/**
		 * Constructor de la hebra
		 * 
		 * @param identificador
		 *            Identificador del objeto registrado
		 * @param doc
		 *            Documento a enviar a traves del servidor
		 */
		public ServerThread( int identificador, Documento doc )
		{
			id = identificador;
			d = doc;
		}

		/**
		 * Ejecucion de la hebra
		 */
		@Override
		public void run()
		{
			String host = null;

			try
			{
				// Se indica a rmiregistry donde estan las clases.
				// Cambiar el path al sitio en el que esta. Es importante
				// mantener la barra al final.

				String ruta = "./";

				System.setProperty("java.rmi.server.codebase", "file:/" + ruta
						+ "/");

				try
				{
					// Iniciamos el servidor RMIRegistry
					LocateRegistry.createRegistry(port);
				}
				catch (Exception e)
				{
					// e.printStackTrace();

					// se lanza una excepcion esperada, porque se hace un
					// registro
					// duplicado. no modificar nada.
				}

				// Se publica el objeto AccesoMesa
				TransmisorFicherosP2P tf = new TransferenciaFicheroP2P(d);

				host = InetAddress.getLocalHost().getHostName();

				Naming.rebind("//" + host + ":" + port
						+ "/TransferenciaFicheroP2P" + id, tf);

				System.out.println("Servicio RMI activo en " + host
						+ " sobre el puerto " + port);
				//System.out
				//		.println("Ha sido creado el objeto con identificador "
				//				+ id);

			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.err.println("Error en la ejecucion de RMI: "
						+ e.getMessage());
			}

			// esperamos hasta que se le indique a la hebra parar: serverExectud
			// == false
			while (getServerExecuted())
				try
				{
					Thread.sleep(1000L);
				}
				catch (Exception ex)
				{
				}

			try
			{
				// desligamos el nombre del objeto anteriormente registrado
				Naming.unbind("//" + host + ":" + port
						+ "/TransferenciaFicheroP2P" + id);
			}
			catch (RemoteException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (MalformedURLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (NotBoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
