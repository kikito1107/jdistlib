package aplicacion.fisica.net;

import java.rmi.Remote;
import java.rmi.RemoteException;

import aplicacion.fisica.documentos.Documento;

public interface TransmisorFicherosP2P extends Remote
{
	public Documento getDocumento() throws RemoteException;
}
