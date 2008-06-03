package aplicacion.fisica.documentos;

import java.io.Serializable;

import metainformacion.MIRol;
import metainformacion.MIUsuario;

/**
 * Metainformacion de un documento.
 * @author carlos, anab
 *
 */
public class MIDocumento implements Serializable
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

	/**
	 * 
	 *
	 */
	public MIDocumento()
	{
		
	}

	/**
	 * Constructor
	 * @param id identificador del documento en la BD
	 * @param nom nombre del documento
	 * @param dir indica si el documento es un directorio
	 * @param perm permisos del documento
	 * @param usu propietario del documento
	 * @param ro rol del propietario
	 * @param pad id de la carpeta en al que se encuentra el usuario
	 * @param ruta ruta del fichero en el servidor
	 * @param tip tipo del fichero
	 */
	public MIDocumento( int id, String nom, boolean dir, String perm,
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

	/**
	 * Accede al nombre del documento
	 * @return
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * Accede al id del documento
	 * @return
	 */
	public int getId()
	{
		return id_documento;
	}

	/**
	 * Comprueba si el documento es un directorio
	 * @return
	 */
	public boolean esDirectorio()
	{
		return es_directorio;
	}

	/**
	 * 
	 * @return
	 */
	public String getPermisos()
	{
		return permisos;
	}

	/**
	 * 
	 * @return
	 */
	public MIUsuario getUsuario()
	{
		return usuario;
	}

	/**
	 * 
	 * @return
	 */
	public MIRol getRol()
	{
		return rol;
	}

	/**
	 * 
	 * @return
	 */
	public int getPadre()
	{
		return padre;
	}

	/**
	 * 
	 * @return
	 */
	public String getRutaLocal()
	{
		return ruta_local;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipo()
	{
		return tipo;
	}

	/**
	 * 
	 * @param nombre
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	/**
	 * 
	 * @param id_documento
	 */
	public void setId(int id_documento)
	{
		this.id_documento = id_documento;
	}

	/**
	 * 
	 * @param es_directorio
	 */
	public void esDirectorio(boolean es_directorio)
	{
		this.es_directorio = es_directorio;
	}

	/**
	 * 
	 * @param permisos
	 */
	public void setPermisos(String permisos)
	{
		this.permisos = permisos;
	}

	/**
	 * 
	 * @param usuario
	 */
	public void setUsuario(MIUsuario usuario)
	{
		this.usuario = usuario;
	}

	/**
	 * 
	 * @param rol
	 */
	public void setRol(MIRol rol)
	{
		this.rol = rol;
	}

	/**
	 * 
	 * @param padre
	 */
	public void setPadre(int padre)
	{
		this.padre = padre;
	}

	/**
	 * 
	 * @param ruta_local
	 */
	public void setRutaLocal(String ruta_local)
	{
		this.ruta_local = ruta_local;
	}

	/**
	 * 
	 * @param tip
	 */
	public void setTipo(String tip)
	{
		this.tipo = tip;
	}

	@Override
	public String toString()
	{
		if (this.getTipo() ==null || !this.getTipo().equals(MIDocumento.TIPO_MENSAJE))
			return this.nombre;
		else {
			String nombr = this.getNombre();
			return nombr.replaceAll("."+MIDocumento.TIPO_MENSAJE, "");
		}
			
	}

	/**
	 * 
	 * @param u
	 * @param r
	 * @param modo
	 * @return
	 */
	public boolean comprobarPermisos(String u, String r, char modo)
	{
		
		if (id_documento == 1) return true;

		int index = -1;

		if (modo == MIDocumento.PERMISO_LECTURA)
			index = 0;
		else if (modo == MIDocumento.PERMISO_ESCRITURA)
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
	
	/**
	 * 
	 * @param extension
	 * @return
	 */
	public static String getTipoFichero(String extension)
	{
		String res = "";

		extension = extension.toLowerCase();

		if (extension.equals("gif") || extension.equals("png")
				|| extension.equals("jpg") || extension.equals("jpeg")
				|| extension.equals("tiff") || extension.equals("tif")
				|| extension.equals("bmp"))
			res = new String(MIDocumento.TIPO_IMAGEN);
		else res = extension;

		return res;
	}

	/**
	 * 
	 * @return
	 */
	public String getMensaje()
	{
		return mensaje;
	}

	/**
	 * 
	 * @param mensaje
	 */
	public void setMensaje(String mensaje)
	{
		this.mensaje = mensaje;
	}
	
	
}
