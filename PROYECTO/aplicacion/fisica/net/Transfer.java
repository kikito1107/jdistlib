package aplicacion.fisica.net;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import aplicacion.fisica.documentos.Documento;

public class Transfer
{
	private String path;
	private String ip_origen;
	
	private static final int port = 1099;
	public static boolean serverExecuted = false;
	
	
	public Transfer(String ip_orig, String fich)
	{
		path = fich;
		ip_origen = ip_orig;
	}
	
	public static void establecerServidor()
	{
		if (!serverExecuted)
		{
			Transfer ts = new Transfer("","");
			
			ServerThread s = ts.new ServerThread();
			new Thread(s).start();
			
			serverExecuted = true;
		}
	}
	
	public Documento receiveDocumento()
	{
		try{
			InterfazTransferenciaFichero ar = (InterfazTransferenciaFichero)Naming.lookup("//" + ip_origen + "/TransferenciaFichero");
		
			return ar.getDocument(path);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	
	public boolean sendDocumento(Documento d)
	{
		try{
			InterfazTransferenciaFichero ar = (InterfazTransferenciaFichero)Naming.lookup("//" + ip_origen + "/TransferenciaFichero");
		
			return ar.sendDocument(d);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
	
	public byte[] receiveFileBytes()
	{
		try{
			InterfazTransferenciaFichero ar = (InterfazTransferenciaFichero)Naming.lookup("//" + ip_origen + "/TransferenciaFichero");
		
			return ar.getByteFiles(path);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public boolean sendFile(byte[] datos)
	{
		try{
			InterfazTransferenciaFichero ar = (InterfazTransferenciaFichero)Naming.lookup("//" + ip_origen + "/TransferenciaFichero");
		
			return ar.sendByteFile(datos, path);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
	
	private class ServerThread extends Thread
	{
		public ServerThread()
		{
		}
		
		public void run()
		{
			try {
				// Se indica a rmiregistry dónde están las clases.
				// Cambiar el path al sitio en el que esté. Es importante
				// mantener la barra al final.

				String ruta = "./";
					
				System.setProperty("java.rmi.server.codebase", "file:/" + ruta + "/");

				// Iniciamos el servidor RMIRegistry
				LocateRegistry.createRegistry( port );

				// Se publica el objeto AccesoMesa
				InterfazTransferenciaFichero tf = new TransferenciaFichero();
				Naming.rebind("//localhost/TransferenciaFichero", tf);

				String host = InetAddress.getLocalHost().getHostName();
				System.out.println("Servicio RMI activo en " + host + " sobre el puerto " + port);

			} 
			catch (Exception e) 
			{
					System.err.println("Error en la ejecucion de RMI: " + e.getMessage());
			}
			
			while(true)
			{
				try{
					Thread.sleep(1000);
				}
				catch(Exception ex)
				{
				}
			}
		}
	}
}
