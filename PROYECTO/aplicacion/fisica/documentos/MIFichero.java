package aplicacion.fisica.documentos;

import java.io.Serializable;

import metainformacion.MIRol;
import metainformacion.MIUsuario;

public class MIFichero implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6727437732098052591L;

	private String nombre;

	private int id_documento;

	private boolean es_directorio;

	private String permisos;

	private MIUsuario usuario;

	private MIRol rol;

	private int padre;

	private String ruta_local;

	private String tipo;
	
	private String mensaje = null;

	public final static char PERMISO_LECTURA = 'r';

	public final static char PERMISO_ESCRITURA = 'w';
	
	public final static String TIPO_MENSAJE = "msg";
	
	public final static String TIPO_IMAGEN = "img";
	
	public final static String TIPO_DOC = "doc";
	
	public final static String TIPO_BANDEJA_MAIL = "INCOMING";
	
	public final static String TIPO_PDF = "pdf";
	
	public final static String TIPO_BANDEJA_TXT = "txt";

	public MIFichero()
	{

	}

	public MIFichero( int id, String nom, boolean dir, String perm,
			MIUsuario usu, MIRol ro, int pad, String ruta, String tip )
	{
		nombre = nom;
		id_documento = id;
		es_directorio = dir;
		permisos = perm;
		usuario = usu;
		rol = ro;
		padre = pad;
		ruta_local = ruta;
		tipo = tip;
	}

	public String getNombre()
	{
		return nombre;
	}

	public int getId()
	{
		return id_documento;
	}

	public boolean esDirectorio()
	{
		return es_directorio;
	}

	public String getPermisos()
	{
		return permisos;
	}

	public MIUsuario getUsuario()
	{
		return usuario;
	}

	public MIRol getRol()
	{
		return rol;
	}

	public int getPadre()
	{
		return padre;
	}

	public String getRutaLocal()
	{
		return ruta_local;
	}

	public String getTipo()
	{
		return tipo;
	}

	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	public void setId(int id_documento)
	{
		this.id_documento = id_documento;
	}

	public void esDirectorio(boolean es_directorio)
	{
		this.es_directorio = es_directorio;
	}

	public void setPermisos(String permisos)
	{
		this.permisos = permisos;
	}

	public void setUsuario(MIUsuario usuario)
	{
		this.usuario = usuario;
	}

	public void setRol(MIRol rol)
	{
		this.rol = rol;
	}

	public void setPadre(int padre)
	{
		this.padre = padre;
	}

	public void setRutaLocal(String ruta_local)
	{
		this.ruta_local = ruta_local;
	}

	public void setTipo(String tip)
	{
		this.tipo = tip;
	}

	@Override
	public String toString()
	{
		if (this.getTipo() ==null || !this.getTipo().equals(MIFichero.TIPO_MENSAJE))
			return this.nombre;
		else {
			String nombr = this.getNombre();
			return nombr.replaceAll("."+MIFichero.TIPO_MENSAJE, "");
		}
			
	}

	public boolean comprobarPermisos(String u, String r, char modo)
	{

		int index = -1;

		if (modo == MIFichero.PERMISO_LECTURA)
			index = 0;
		else if (modo == MIFichero.PERMISO_ESCRITURA)
			index = 1;
		else return false;

		if (getUsuario().getNombreUsuario().equals(u))
			index += 0;

		else if (getRol().getNombreRol().equals(r))
			index += 2;
		else index += 4;

		if (getPermisos().charAt(index) == modo)
			return true;
		else return false;
	}
	
	public static String getTipoFichero(String extension)
	{
		String res = "";

		extension = extension.toLowerCase();

		if (extension.equals("gif") || extension.equals("png")
				|| extension.equals("jpg") || extension.equals("jpeg")
				|| extension.equals("tiff") || extension.equals("tif")
				|| extension.equals("bmp"))
			res = new String(MIFichero.TIPO_IMAGEN);
		else res = extension;

		return res;
	}

	public String getMensaje()
	{
		return mensaje;
	}

	public void setMensaje(String mensaje)
	{
		this.mensaje = mensaje;
	}
	
	
}
