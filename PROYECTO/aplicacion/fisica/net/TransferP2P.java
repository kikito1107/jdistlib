package aplicacion.fisica.net;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import aplicacion.fisica.documentos.Documento;

public class TransferP2P
{
	private String ip_origen;

	private int id;

	private static int port = 8866;

	private static boolean serverExecuted = false;

	private static Thread hebra = null;

	public TransferP2P( String ip_orig, int identificador, int puerto )
	{
		ip_origen = ip_orig;
		id = identificador;
	}
	
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

	public synchronized static void pararHebra()
	{
		if (getServerExecuted())
		{

			setServerExecuted(false);
			System.out.println("PARANDO LA HEBRA");
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

	private class ServerThread extends Thread
	{
		int id;

		Documento d;

		public ServerThread( int identificador, Documento doc )
		{
			id = identificador;
			d = doc;
		}

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

				try
				{
					// Iniciamos el servidor RMIRegistry
					LocateRegistry.createRegistry(port);
				}
				catch (Exception e)
				{
					//e.printStackTrace();
					
					//se lanza una excepcion esperada, porque se hace un registro
					//duplicado. no modificar nada.
				}

				// Se publica el objeto AccesoMesa
				TransmisorFicherosP2P tf = new TransferenciaFicheroP2P(
						d);
				
				host = InetAddress.getLocalHost().getHostName();
				
				Naming.rebind("//"+host+":" + port
						+ "/TransferenciaFicheroP2P" + id, tf);

				System.out.println("Servicio RMI activo en " + host
						+ " sobre el puerto " + port);
				System.out
						.println("Ha sido creado el objeto con identificador "
								+ id);

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

			System.out.println("Salimos del bucle!!!!!");

			try
			{
				// desligamos el nombre del objeto anteriormente registrado
				Naming.unbind("//"+host+":" + port
						+ "/TransferenciaFicheroP2P" + id);
				System.out.println("Objeto desligado!!!");
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
