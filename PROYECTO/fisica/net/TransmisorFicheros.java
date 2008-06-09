package fisica.net;

import java.rmi.Remote;
import java.rmi.RemoteException;

import fisica.documentos.Documento;


/**
 * Interfaz encargada de la transmision de ficheros
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public interface TransmisorFicheros extends Remote
{
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
			throws RemoteException;

	/**
	 * Abre un fichero y devuelve los bytes contenidos en este
	 * 
	 * @param path
	 *            Path del fichero
	 * @param Vector
	 *            de bytes con el contenido del fichero
	 */
	public byte[] getByteFiles(String path) throws RemoteException;

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
			throws RemoteException;

	/**
	 * Guarda un documento en el sistema de ficheros junto con sus anotaciones
	 * 
	 * @param d
	 *            Documento a guardar
	 * @return True si se ha guardado correctamente. False en caso contrario
	 */
	public boolean sendDocument(Documento d) throws RemoteException;
}
