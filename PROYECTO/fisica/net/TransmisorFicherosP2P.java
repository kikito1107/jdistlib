package fisica.net;

import java.rmi.Remote;
import java.rmi.RemoteException;

import fisica.documentos.Documento;

/**
 * Interfaz encargada de la transmision de ficheros entre usuarios
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public interface TransmisorFicherosP2P extends Remote
{
	/**
	 * Devuelve el documento que se desea enviar
	 * 
	 * @return Documento que se desea enviar
	 */
	public Documento getDocumento() throws RemoteException;
}
