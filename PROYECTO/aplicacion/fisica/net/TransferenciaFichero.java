package aplicacion.fisica.net;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import aplicacion.fisica.documentos.Documento;

public class TransferenciaFichero extends UnicastRemoteObject implements InterfazTransferenciaFichero
{
	private static final long serialVersionUID = 1L;
	
	public TransferenciaFichero() throws RemoteException
	{
		super();
	}
	
	public Documento getFile(String path)
	{	
		return Documento.openDocument(path, "", "");
	}
	
	public  byte[]  getByteFiles(String path){
		byte[] bytes = null; 
		try
		{
			RandomAccessFile raf = new RandomAccessFile(path, "r");
			
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
		
		return bytes;
	}

	public boolean sendFile(byte[] bytes, String path)
	{
		try
		{
			RandomAccessFile raf = new RandomAccessFile(path, "rw");
			//System.out.println("RECIBIDO FICHERO");
			raf.write(bytes);
			raf.close();
			return true;
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			System.out.println("SE ha producido un error");
			e.printStackTrace();
			return false;
		}
	}
}
