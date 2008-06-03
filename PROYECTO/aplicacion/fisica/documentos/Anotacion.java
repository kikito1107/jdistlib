package aplicacion.fisica.documentos;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import figuras.Figura;

/**
 * Anotacion realizada sobre una pagina determinada de un documento
 * @author anab, carlos
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
	 * @param cont figura a dibujar en la anotacion
	 * @param usuario usuario que realiza la anotacion
	 * @param rol rol bajo el cual el usuario realizo la anotacion
	 */
	public Anotacion(Figura cont, String usuario, String rol)
	{
		fecha = new Date();
		contenido = cont;
		this.usuario = usuario;
		this.rol = rol;
	}


	/**
	 * Establece la figura a dibujar
	 * @param cont
	 */
	public void setContenido(Figura cont)
	{
		
	}

	/**
	 * Devuelve la figura a dibujar
	 * @return
	 */
	public Figura getContenido()
	{
		return contenido;
	}

	/**
	 * Accede al usuario que realiza la anotacion
	 * @return String con el nombre del autor
	 */
	public String getUsuario()
	{
		return usuario;
	}

	/**
	 * Accede al rol que desempe–aba el usuario cuando realizo la anotacion
	 * @return String con el nombre del rol
	 */
	public String getRol()
	{
		return rol;
	}


	/**
	 * Consulta la fecha y hora en la que fue realizada la anotacion
	 * @return cadena con la representaci—n de la fecha en el siguiente formato: dd/MMMMM/yyyy 'a las' hh:mm
	 */
	public String getFecha()
	{
		SimpleDateFormat formato = new SimpleDateFormat("dd/MMMMM/yyyy 'a las' hh:mm");
		
		
		return formato.format(fecha);
	}
}
