package Deventos.enlaceJS;

import java.util.Vector;

/**
 * Tokens para la edicion compartida de ficheros
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class TokenFichero extends Token
{
	private static final long serialVersionUID = 6362232361013310234L;

	/**
	 * Nombre del fichero que se esta editando
	 */
	public String Fichero = null;

	/**
	 * Direccion IP (para RMI)
	 */
	public String ip = null;

	/**
	 * Indica si queremos sincronizar o no
	 */
	public Boolean sincronizar = null;

	/**
	 * Usuarios que se encuentran editando el fichero actualmente
	 */
	public Vector<String> editores = null;

	/**
	 * Constructor por defecto
	 */
	public TokenFichero()
	{
		super();
	}
	
	/**
	 * Constructor con parametros
	 * @param aplicacion Nombre de la aplicacion
	 * @param fichero Nombre del fichero que se editara
	 */
	public TokenFichero( String aplicacion, String fichero )
	{
		this.aplicacion = aplicacion;
		this.Fichero = fichero;
	}

	/**
	 * Realiza la baja de un usuario en la edicion de un documento
	 * @param usuario Nombre del usuario
	 */
	public void bajaUsuario(String usuario)
	{
		if (editores != null && editores.size() > 0)
		{
			editores.remove(usuario);
			System.err.println("El usuario " + usuario + " se da de baja");
		}
	}

	/**
	 * Realiza el alta de un usuario en la edicion de un documento
	 * @param usuario Nombre del usuario
	 */
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
