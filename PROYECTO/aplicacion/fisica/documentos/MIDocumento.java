package aplicacion.fisica.documentos;

import java.io.Serializable;

import metainformacion.MIRol;
import metainformacion.MIUsuario;

/**
 * Metainformacion de un documento
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz.
 *
 */
public class MIDocumento implements Serializable
{
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

	/**
	 * Constante que indica que un documento tiene permiso de lectura
	 */
	public final static char PERMISO_LECTURA = 'r';

	/**
	 * Constante que indica que un documento tiene permiso de escritura
	 */
	public final static char PERMISO_ESCRITURA = 'w';
	
	/**
	 * Conjunto de constantes para indicar distintos formatos de fichero
	 * que son soportados por el sistema de forma predefinida.
	 */
	public final static String TIPO_MENSAJE = "msg";
	public final static String TIPO_IMAGEN = "img";
	public final static String TIPO_DOC = "doc";
	public final static String TIPO_BANDEJA_MAIL = "INCOMING";
	public final static String TIPO_PDF = "pdf";
	public final static String TIPO_BANDEJA_TXT = "txt";

	/**
	 * Constructor por defecto
	 */
	public MIDocumento()
	{
		
	}

	/**
	 * Constructor
	 * @param id Identificador del documento en la BD
	 * @param nom Nombre del documento
	 * @param dir Indica si el documento es un directorio
	 * @param perm Permisos del documento
	 * @param usu Propietario del documento
	 * @param ro Rol del propietario
	 * @param pad Identificador de la carpeta en la que se encuentra el documento
	 * @param ruta Ruta del documento en el servidor
	 * @param tip Tipo de documento
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
	 * @return Nombre del documento
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * Accede al identificador del documento
	 * @return Identificador del documento
	 */
	public int getId()
	{
		return id_documento;
	}

	/**
	 * Comprueba si el documento es un directorio
	 * @return True si el documento es un directorio. False en caso contrario
	 */
	public boolean esDirectorio()
	{
		return es_directorio;
	}

	/**
	 * Devuelve los permisos que tiene un documento
	 * @return Permisos del documento
	 */
	public String getPermisos()
	{
		return permisos;
	}

	/**
	 * Devuelve los datos del propietario del documento
	 * @return Objeto de la clase @see MIUsuario con los datos del propietario del documento
	 */
	public MIUsuario getUsuario()
	{
		return usuario;
	}

	/**
	 * Devuelve el rol del propietario del documento
	 * @return Objeto de la clase @see MIRol con los datos del rol del propietario del documento
	 */
	public MIRol getRol()
	{
		return rol;
	}

	/**
	 * Devuelve el identificador del directorio padre de un documento
	 * @return Identificador del directorio padre del documento
	 */
	public int getPadre()
	{
		return padre;
	}

	/**
	 * Devuelve la ruta local del documento
	 * @return Ruta local del documento
	 */
	public String getRutaLocal()
	{
		return ruta_local;
	}

	/**
	 * Obtiene el tipo del documentos
	 * @return Tipo del documento
	 */
	public String getTipo()
	{
		return tipo;
	}

	/**
	 * Cambia el nombre a un documento
	 * @param nombre Nuevo nombre que tendra el documento
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	/**
	 * Pone un identificador a un documento
	 * @param id_documento Nuevo identificador del documento
	 */
	public void setId(int id_documento)
	{
		this.id_documento = id_documento;
	}

	/**
	 * Permite indicar si un documento es un directorio o no
	 * @param es_directorio Asigna si el documento actual debe 
	 * 		  ser considerado un directorio o no
	 */
	public void esDirectorio(boolean es_directorio)
	{
		this.es_directorio = es_directorio;
	}

	/**
	 * Permite poner los permisos a un documento
	 * @param permisos Permisos del documento
	 */
	public void setPermisos(String permisos)
	{
		this.permisos = permisos;
	}

	/**
	 * Permite cambiar el propietario de un documento
	 * @param usuario Usuario que actuara como propietario del documento
	 */
	public void setUsuario(MIUsuario usuario)
	{
		this.usuario = usuario;
	}

	/**
	 * Permite cambiar el rol del propietario de un documento
	 * @param rol Nuevo rol del propietario del documento
	 */
	public void setRol(MIRol rol)
	{
		this.rol = rol;
	}

	/**
	 * Permite cambiar el identificador del directorio padre de un documento
	 * @param padre Identificador nuevo del directorio padre del documento
	 */
	public void setPadre(int padre)
	{
		this.padre = padre;
	}

	/**
	 * Permite cambiar el path de un documento
	 * @param ruta_local Nuevo path del documento
	 */
	public void setRutaLocal(String ruta_local)
	{
		this.ruta_local = ruta_local;
	}

	/**
	 * Permite cambiar el tipo de un documento
	 * @param tip Nuevo tipo del documento
	 */
	public void setTipo(String tip)
	{
		this.tipo = tip;
	}

	/**
	 * Devuelve una representacion en forma de string del documento
	 * @return Cadena con el nombre del documento y su extension
	 */
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
	 * Comprueba si tenemos permisos para acceder a un documento
	 * @param u Usuario que solicita comprobar los permisos de un documento
	 * @param r Rol que desempe–a el usario que solicita el acceso al documento
	 * @param modo Indicacion de si queremos acceder en modo escritura o en modo lectura.
	 * @return True si tenemos acceso al documento en el modo especificado o False si no lo tenemos.
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
	 * Devuelve una variable con el tipo de documento a partir de una extension
	 * @param extension Extension del fichero
	 * @return Tipo de documento.
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
	 * Devuelve el mensaje que contenga el documento, si este es de tipo MSG
	 * @return Mensaje que contiene el documento de tipo MSG. Devuelve null
	 *         si el documento no es de tipo MSG.
	 */
	public String getMensaje()
	{
		return mensaje;
	}

	/**
	 * Pone un nuevo mensaje en el documento actual. Solo sera usado si este
	 * tipo de documento es MSG.
	 * 
	 * @param mensaje Mensaje a introducir en un documento de tipo MSG
	 */
	public void setMensaje(String mensaje)
	{
		this.mensaje = mensaje;
	}
	
	/**
	 * A partir de un nombre de fichero obtiene su extension.
	 * @param nombreFichero Nombre del fichero a cual extraer la extension
	 * @return Extension del fichero.
	 */
	public static String getExtension(String nombreFichero) {
		nombreFichero = nombreFichero.replace('.', ':');

		String[] desc = nombreFichero.split(":");

		String extension = desc[desc.length - 1];

		extension = MIDocumento.getTipoFichero(extension);
		
		return extension;
	}
	
	
}
