package aplicacion.fisica.net;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import aplicacion.fisica.documentos.Documento;

/**
 * Transfiere documentos entre usuarios
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class TransferenciaFicheroP2P extends UnicastRemoteObject implements
		TransmisorFicherosP2P
{
	private static final long serialVersionUID = 1L;

	private Documento d;

	/**
	 * Constructor
	 * 
	 * @param doc
	 *            Documento a enviar
	 * @throws RemoteException
	 */
	public TransferenciaFicheroP2P( Documento doc ) throws RemoteException
	{
		super();
		d = doc;
	}

	/**
	 * Devuelve el documento que se desea enviar
	 * 
	 * @return Documento que se desea enviar
	 */
	public Documento getDocumento()
	{
		return d;
	}
}
