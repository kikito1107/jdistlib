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

	/**
	 * Construye un documento a partir de un fichero del sistema de ficheros
	 * 
	 * @param path
	 *            Path del fichero
	 * @param force
	 *            Indica si hay que forzar la converion a un documento de texto
	 * @return El documento si todo es correcto o null en caso contrario
	 */
	public Documento getDocument(String path, boolean force)
	{

		if (force) return new TXTFilter().getDocumento(path, "", "");

		return Documento.openDocument(path, "", "");
	}

	/**
	 * Abre un fichero y devuelve los bytes contenidos en este
	 * 
	 * @param path
	 *            Path del fichero
	 * @param Vector
	 *            de bytes con el contenido del fichero
	 */
	public byte[] getByteFiles(String path)
	{
		byte[] bytes = null;
		try
		{
			RandomAccessFile raf = new RandomAccessFile(ServidorFicheros
					.getDirectorioBase()
					+ path, "r");

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

	/**
	 * Almacena los bytes de un documento en un path determinado
	 * 
	 * @param bytes
	 *            Contenido del documento a almacenar
	 * @param path
	 *            Path donde se quiere almacenar el fichero
	 * @return True si el documento se ha almacenado correctamente. False en
	 *         caso contrario
	 */
	public boolean sendByteFile(byte[] bytes, String path)
	{
		try
		{

			RandomAccessFile raf = new RandomAccessFile(ServidorFicheros
					.getDirectorioBase()
					+ path, "rw");
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

	/**
	 * Guarda un documento en el sistema de ficheros junto con sus anotaciones
	 * 
	 * @param d
	 *            Documento a guardar
	 * @return True si se ha guardado correctamente. False en caso contrario
	 */
	public boolean sendDocument(Documento d) throws RemoteException
	{
		return Documento.saveDocument(d, d.getPath());
	}
}
