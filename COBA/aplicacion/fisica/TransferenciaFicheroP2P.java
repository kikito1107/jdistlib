package aplicacion.fisica;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import aplicacion.fisica.documentos.Documento;

public class TransferenciaFicheroP2P extends UnicastRemoteObject implements InterfazTransferenciaFicheroP2P
{
	private static final long serialVersionUID = 1L;
	
	private Documento d; 
	
	public TransferenciaFicheroP2P(Documento doc) throws RemoteException
	{
		super();
		d = doc;
	}
	
	public Documento getDocumento()
	{	
		return d;
	}
}
