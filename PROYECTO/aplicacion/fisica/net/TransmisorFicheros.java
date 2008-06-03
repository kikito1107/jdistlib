package aplicacion.fisica.net;

import java.rmi.Remote;
import java.rmi.RemoteException;

import aplicacion.fisica.documentos.Documento;

/**
 * Interfaz encargada de la transmision de ficheros
 * @author carlos
 */
public interface TransmisorFicheros extends Remote
{
	/**
	 * 
	 * @param path
	 * @param force
	 * @return
	 * @throws RemoteException
	 */
	public Documento getDocument(String path, boolean force) throws RemoteException;

	
	/**
	 * 
	 * @param path
	 * @return
	 * @throws RemoteException
	 */
	public byte[] getByteFiles(String path) throws RemoteException;

	
	/**
	 * 
	 * @param bytes
	 * @param path
	 * @return
	 * @throws RemoteException
	 */
	public boolean sendByteFile(byte[] bytes, String path)
			throws RemoteException;

	/**
	 * 
	 * @param d
	 * @return
	 * @throws RemoteException
	 */
	public boolean sendDocument(Documento d) throws RemoteException;
}
