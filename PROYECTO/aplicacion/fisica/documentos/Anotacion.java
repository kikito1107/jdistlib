package aplicacion.fisica.documentos;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import figuras.Figura;

public class Anotacion implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Documento documento;

	private Figura contenido;

	private String usuario;

	private String rol;
	
	private Date fecha = null;

	public Anotacion(Figura cont, String usuario, String rol)
	{
		fecha = new Date();
		contenido = cont;
		this.usuario = usuario;
		this.rol = rol;
	}


	public void setContenido(Figura cont)
	{
		
	}


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
