package aplicacion.fisica.net;

import java.rmi.Remote;
import java.rmi.RemoteException;

import aplicacion.fisica.documentos.Documento;

public interface InterfazTransferenciaFichero extends Remote
{
	public Documento getDocument(String path) throws RemoteException;
	public  byte[]  getByteFiles(String path) throws RemoteException;
	public  boolean  sendByteFile(byte[] bytes, String path) throws RemoteException;
	public  boolean  sendDocument(Documento d) throws RemoteException;
}
