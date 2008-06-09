package fisica.documentos;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import figuras.Figura;

/**
 * Anotacion realizada sobre una pagina determinada de un documento del sistema
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class Anotacion implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Figura contenido;

	private String usuario;

	private String rol;

	private Date fecha = null;

	/**
	 * Constructor
	 * 
	 * @param cont
	 *            Figura a dibujar en la anotacion.
	 * @param usuario
	 *            Usuario que realiza la anotacion
	 * @param rol
	 *            Rol bajo el cual el usuario realizo la anotacion
	 */
	public Anotacion( Figura cont, String usuario, String rol )
	{
		fecha = new Date();
		contenido = cont;
		this.usuario = usuario;
		this.rol = rol;
	}

	/**
	 * Devuelve la figura a dibujar
	 * 
	 * @return Figura a dibujar.
	 */
	public Figura getContenido()
	{
		return contenido;
	}

	/**
	 * Accede al usuario que realiza la anotacion
	 * 
	 * @return String con el nombre del autor
	 */
	public String getUsuario()
	{
		return usuario;
	}

	/**
	 * Accede al rol que desempeñaba el usuario cuando realizo la anotacion
	 * 
	 * @return String con el nombre del rol
	 */
	public String getRol()
	{
		return rol;
	}

	/**
	 * Consulta la fecha y hora en la que fue realizada la anotacion
	 * 
	 * @return Cadena con la representacion de la fecha en el siguiente formato:
	 *         dd/MMMMM/yyyy 'a las' hh:mm
	 */
	public String getFecha()
	{
		SimpleDateFormat formato = new SimpleDateFormat(
				"dd/MMMMM/yyyy 'a las' hh:mm");

		return formato.format(fecha);
	}
}
