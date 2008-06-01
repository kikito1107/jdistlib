package aplicacion.fisica.net;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import aplicacion.fisica.ServidorFicheros;
import aplicacion.fisica.documentos.Documento;
import aplicacion.fisica.documentos.filtros.ImageFilter;
import aplicacion.fisica.documentos.filtros.MSGFilter;
import aplicacion.fisica.documentos.filtros.PDFFilter;
import aplicacion.fisica.documentos.filtros.TXTFilter;

public class TransferenciaFichero extends UnicastRemoteObject implements
		TransmisorFicheros
{
	private static final long serialVersionUID = 1L;

	public TransferenciaFichero() throws RemoteException
	{
		super();
	}

	public Documento getDocument(String path, boolean force)
	{
		
		if (force)
			return new TXTFilter().getDocumento(path, "", "");
		
		Documento.addFilter(new MSGFilter());
		Documento.addFilter(new ImageFilter());
		Documento.addFilter(new PDFFilter());
		Documento.addFilter(new TXTFilter());
		return Documento.openDocument(path, "", "");
	}

	public byte[] getByteFiles(String path)
	{
		byte[] bytes = null;
		try
		{
			RandomAccessFile raf = new RandomAccessFile(ServidorFicheros.getDirectorioBase()+ path, "r");

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

		return bytes;
	}

	public boolean sendByteFile(byte[] bytes, String path)
	{
		try
		{
			
			RandomAccessFile raf = new RandomAccessFile(ServidorFicheros.getDirectorioBase() + path, "rw");
			System.out.println("RECIBIDO FICHERO");
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
			System.out.println("Se ha producido un error");
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendDocument(Documento d) throws RemoteException
	{
		return Documento.saveDocument(d, d.getPath());
	}
}
