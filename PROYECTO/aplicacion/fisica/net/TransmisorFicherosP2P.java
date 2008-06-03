package aplicacion.fisica.net;

import java.rmi.Remote;
import java.rmi.RemoteException;

import aplicacion.fisica.documentos.Documento;

/**
 * 
 * @author anab
 *
 */
public interface TransmisorFicherosP2P extends Remote
{
	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public Documento getDocumento() throws RemoteException;
}
