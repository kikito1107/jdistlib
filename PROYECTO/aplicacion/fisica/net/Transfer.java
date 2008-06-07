package aplicacion.fisica.net;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import aplicacion.fisica.documentos.Documento;

/**
 * Transfiere un fichero entre dos ordenadores vía RMI
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz.
 */
public class Transfer
{
	private String path;

	private String ip_origen;

	private static final int port = 1099;

	private static boolean serverExecuted = false;

	/**
	 * Constructor
	 * 
	 * @param ip_orig
	 *            Direccion IP del ordenador del cual recibiremos o enviaremos
	 *            el fichero
	 * @param fich
	 *            path del fichero a mover
	 */
	public Transfer( String ip_orig, String fich )
	{
		path = fich;
		ip_origen = ip_orig;
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
	 * Inicia el servidor RMI para poder realizar transferencias
	 */
	public static void establecerServidor()
	{
		if (!getServerExecuted())
		{
			Transfer ts = new Transfer("", "");

			ServerThread s = ts.new ServerThread();
			new Thread(s).start();

			setServerExecuted(true);
		}
	}

	/**
	 * Recibe un documento del servidor
	 * 
	 * @param forceText
	 *            Bandera booleana que indica si hay que forzar la conversion a
	 *            texto
	 * @return Objeto de tipo
	 * @see Documento si todo ha ido correctamente y null en caso contrario
	 */
	public Documento receiveDocumento(boolean forceText)
	{
		try
		{
			TransmisorFicheros ar = (TransmisorFicheros) Naming.lookup("//"
					+ ip_origen + "/TransferenciaFichero");

			return ar.getDocument(path, forceText);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Envia un documento al servidor
	 * 
	 * @param d
	 *            Documento a enviar
	 * @return True si todo fue correcto y false en caso contrario
	 */
	public boolean sendDocumento(Documento d)
	{
		try
		{
			TransmisorFicheros ar = (TransmisorFicheros) Naming.lookup("//"
					+ ip_origen + "/TransferenciaFichero");

			return ar.sendDocument(d);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * Recibe los bytes de un fichero desde el servidor
	 * 
	 * @return Vector de bytes con el contenido del fichero solicitado
	 */
	public byte[] receiveFileBytes()
	{
		try
		{
			TransmisorFicheros ar = (TransmisorFicheros) Naming.lookup("//"
					+ ip_origen + "/TransferenciaFichero");

			return ar.getByteFiles(path);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return new byte[0];
		}
	}

	/**
	 * Envia los bytes de un fichero al servidor
	 * 
	 * @param datos
	 *            Vector de bytes con el contenido del fichero
	 * @return True si la transferencia ha sido correcta. False en caso
	 *         contrario
	 */
	public boolean sendFile(byte[] datos)
	{
		try
		{
			TransmisorFicheros ar = (TransmisorFicheros) Naming.lookup("//"
					+ ip_origen + "/TransferenciaFichero");

			return ar.sendByteFile(datos, path);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * Hebra que ejecuta el servidor RMI
	 */
	private class ServerThread extends Thread
	{
		/**
		 * Constructor por defecto
		 */
		public ServerThread()
		{
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
				// Se indica a rmiregistry dónde están las clases.
				// Cambiar el path al sitio en el que esté. Es importante
				// mantener la barra al final.

				String ruta = "./";

				System.setProperty("java.rmi.server.codebase", "file:/" + ruta
						+ "/");

				// Iniciamos el servidor RMIRegistry
				LocateRegistry.createRegistry(port);

				// Se publica el objeto AccesoMesa
				host = InetAddress.getLocalHost().getHostName();

				TransmisorFicheros tf = new TransferenciaFichero();
				Naming.rebind("//" + host + "/TransferenciaFichero", tf);

				System.out.println("Servicio RMI activo en " + host
						+ " sobre el puerto " + port);

			}
			catch (Exception e)
			{
				System.err.println("Error en la ejecucion de RMI: "
						+ e.getMessage());
			}

			while (true)
				try
				{
					Thread.sleep(1000);
				}
				catch (Exception ex)
				{
				}
		}
	}
}
