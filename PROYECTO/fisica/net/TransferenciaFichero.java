package fisica.net;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import fisica.ServidorFicheros;
import fisica.documentos.Documento;
import fisica.documentos.filtros.TXTFilter;

/**
 * Metodos para la transferencia de documentos
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez.
 */
public class TransferenciaFichero extends UnicastRemoteObject implements
		TransmisorFicheros
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @throws RemoteException
	 */
	public TransferenciaFichero() throws RemoteException
	{
		super();
	}

	public Documento getDocument(String path, boolean force)
	{

		if (force) return new TXTFilter().getDocumento(path, "", "");

		return Documento.openDocument(path, "", "");
	}

	public byte[] getByteFiles(String path) throws RemoteException
	{
		return getByteFiles(path, false);
	}

	public byte[] getByteFiles(String path, boolean isAbsolutePath)
			throws RemoteException
	{
		String ppath = path;

		if (!isAbsolutePath)
			ppath = ServidorFicheros.getDirectorioBase() + path;

		byte[] bytes = null;
		try
		{
			RandomAccessFile raf = new RandomAccessFile(ppath, "r");

			int tamanio = (int) raf.length();

			bytes = new byte[tamanio];

			raf.read(bytes);

			raf.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}

		return bytes;
	}

	public boolean sendByteFile(byte[] bytes, String path)
			throws RemoteException
	{
		return sendByteFile(bytes, path, false);
	}

	public boolean sendByteFile(byte[] bytes, String path,
			boolean isAbsolutePath) throws RemoteException
	{
		String ppath = path;

		if (!isAbsolutePath)
			ppath = ServidorFicheros.getDirectorioBase() + path;
		
		System.out.println(ppath);
		System.out.println(ServidorFicheros.getDirectorioBase());

		try
		{

			RandomAccessFile raf = new RandomAccessFile(ppath, "rw");
			System.out.println("RECIBIDO FICHERO");
			raf.write(bytes);
			raf.close();
			return true;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (Exception e)
		{
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
