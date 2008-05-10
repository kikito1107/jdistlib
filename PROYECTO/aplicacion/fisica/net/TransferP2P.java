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
	private int puerto;
	
	public static  int port = 1101;
	public static boolean serverExecuted = false;
	private static Thread hebra = null;
	
	
	public TransferP2P(String ip_orig, int identificador, int puerto)
	{
		ip_origen = ip_orig;
		id = identificador;
	}
	
	public static void establecerServidor(int id, Documento doc)
	{
		if (!serverExecuted)
		{
			TransferP2P ts = new TransferP2P("", 0, 0);
			
			ServerThread s = ts.new ServerThread(id, doc);
			hebra = new Thread(s);
			hebra.start();
			
			serverExecuted = true;
		}
	}
	
	public static void pararHebra() {
		if (serverExecuted) {

			serverExecuted = false;
			System.out.println("PARANDO LA HEBRA");
			try
			{
				Thread.sleep(1000L);
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
		try{
			InterfazTransferenciaFicheroP2P ar = (InterfazTransferenciaFicheroP2P)
								Naming.lookup("//" + ip_origen + ":" + puerto + "/TransferenciaFicheroP2P" + id);
		
			return ar.getDocumento();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	private class ServerThread extends Thread
	{
		int id;
		Documento d;
		
		public ServerThread(int identificador, Documento doc)
		{
			id = identificador;
			d = doc;
		}
		
		public void run()
		{
			try {
				// Se indica a rmiregistry dónde están las clases.
				// Cambiar el path al sitio en el que esté. Es importante
				// mantener la barra al final.

				String ruta = "./";
					
				System.setProperty("java.rmi.server.codebase", "file:/" + ruta + "/");

				boolean registrado;
				
				try {
					LocateRegistry.getRegistry(port);
					registrado = true;
				}
				catch(Exception e) {
					registrado = false;
				}
				
				try {
				// Iniciamos el servidor RMIRegistry
					if (!registrado)
						LocateRegistry.createRegistry( port );
				}
				catch(Exception e){
					port = 1333;
					LocateRegistry.createRegistry( port );
					System.out.println("Registrado RMI en el puerto " + port);
				}

				// Se publica el objeto AccesoMesa
				InterfazTransferenciaFicheroP2P tf = new TransferenciaFicheroP2P(d);
				Naming.rebind("//localhost:" + puerto + "/TransferenciaFicheroP2P"+id, tf);

				String host = InetAddress.getLocalHost().getHostName();
				System.out.println("Servicio RMI activo en " + host + " sobre el puerto " + port);
				System.out.println("Ha sido creado el objeto con identificador " + id );

			} 
			catch (Exception e) 
			{
					System.err.println("Error en la ejecucion de RMI: " + e.getMessage());
			}
			
			// esperamos hasta que se le indique a la hebra parar: serverExectud == false
			while(serverExecuted)
			{
				try{
					Thread.sleep(1000);
				}
				catch(Exception ex)
				{
				}
			}
			
			System.out.println("Salimos del bucle!!!!!");
			
			try
			{
				// desligamos el nombre del objeto anteriormente registrado
				Naming.unbind("//localhost:" + puerto + "/TransferenciaFicheroP2P"+id);
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
