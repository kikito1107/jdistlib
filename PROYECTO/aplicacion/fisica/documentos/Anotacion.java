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

	public Anotacion()
	{
		fecha = new Date();
		
	}

	public void setDocumento(Documento doc)
	{
		documento = doc;
	}

	public void setContenido(Figura cont)
	{
		contenido = cont;
	}

	public Documento getDocumento()
	{
		return documento;
	}

	public Figura getContenido()
	{
		return contenido;
	}

	public String getUsuario()
	{
		return usuario;
	}

	public String getRol()
	{
		return rol;
	}

	public void setUsuario(String usuario)
	{
		this.usuario = usuario;
	}

	public void setRol(String rol)
	{
		this.rol = rol;
	}

	public String getFecha()
	{
		SimpleDateFormat formato = new SimpleDateFormat("dd/MMMMM/yyyy 'a las' hh:mm");
		
		
		return formato.format(fecha);
	}
}
