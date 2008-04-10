package aplicacion.fisica;

import java.rmi.Remote;
import java.rmi.RemoteException;

import aplicacion.fisica.documentos.Documento;

public interface InterfazTransferenciaFichero extends Remote
{
	public Documento getFile(String path) throws RemoteException;
	public  byte[]  getByteFiles(String path) throws RemoteException;
	public  boolean  sendFile(byte[] bytes, String path) throws RemoteException;
}
