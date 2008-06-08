package Deventos.enlaceJS;

import java.util.Vector;

public class TokenFichero extends Token
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6362232361013310234L;

	public String Fichero = null;

	public String ip = null;

	public Boolean sincronizar = null;

	public Vector<String> editores = null;

	public TokenFichero( String aplicacion, String fichero )
	{
		this.aplicacion = aplicacion;
		this.Fichero = fichero;

	}

	public TokenFichero()
	{
		super();
	}

	public void bajaUsuario(String usuario)
	{
		if (editores != null && editores.size() > 0)
		{
			editores.remove(usuario);
			System.err.println("El usuario " + usuario + " se da de baja");
		}
	}

	public void nuevoUsuario(String usuario)
	{
		if (editores != null)
		{

			// si el usuario ya estaba registrado no lo volvemos a anadir
			if (editores.indexOf(usuario) != -1) return;

			editores.add(usuario);
			System.err.println("El usuario " + usuario + " se da de alta");
		}
	}
}
