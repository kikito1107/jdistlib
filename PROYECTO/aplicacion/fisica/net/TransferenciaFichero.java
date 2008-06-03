package aplicacion.fisica.net;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import aplicacion.fisica.ServidorFicheros;
import aplicacion.fisica.documentos.Documento;
import aplicacion.fisica.documentos.filtros.TXTFilter;

/**
 * Clase encargada de acceder a documentos desde el servidor
 * @author anab
 */
public class TransferenciaFichero extends UnicastRemoteObject implements
		TransmisorFicheros
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @throws RemoteException
	 */
	public TransferenciaFichero() throws RemoteException
	{
		super();
	}
	
	/**
	 * Construye un documento a partir de un fichero del FS
	 * @param path path del fichero
	 * @param force indica si hay que forzar la converion a a documento de texto
	 * @return el documento si todo va ok y null en caso contrario
	 */
	public Documento getDocument(String path, boolean force)
	{
		
		if (force)
			return new TXTFilter().getDocumento(path, "", "");
		
		return Documento.openDocument(path, "", "");
	}

	/**
	 * Abre un fichero y devuelve el contenido de este
	 * @param path path del fichero
	 * @param vector de bytes con el contenido del fichero
	 */
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

	/**
	 * Almacena un documento
	 * @param bytes contenido del documento a almacenar
	 * @param path path donde se quiere almacenar el fichero
	 * @return true si el documento se ha almacenado correctamente y false en caso contrario
	 */
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

	/**
	 * Guarda un documento en el FS junto con sus anotaciones
	 * @param d documento a guardar
	 * @return true si se ha guardado correctamente y false en caso contrario
	 */
	public boolean sendDocument(Documento d) throws RemoteException
	{
		return Documento.saveDocument(d, d.getPath());
	}
}
