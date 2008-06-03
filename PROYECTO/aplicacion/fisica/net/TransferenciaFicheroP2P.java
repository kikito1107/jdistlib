package aplicacion.fisica.net;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import aplicacion.fisica.documentos.Documento;

/**
 * Transfiere documentos entre usuarios
 * @author anab
 */
public class TransferenciaFicheroP2P extends UnicastRemoteObject implements
		TransmisorFicherosP2P
{
	private static final long serialVersionUID = 1L;

	private Documento d;

	/**
	 * Constructor
	 * @param doc documento a enviar
	 * @throws RemoteException 
	 */
	public TransferenciaFicheroP2P( Documento doc ) throws RemoteException
	{
		super();
		d = doc;
	}

	/**
	 * Devuelve el documento
	 */
	public Documento getDocumento()
	{
		return d;
	}
}
