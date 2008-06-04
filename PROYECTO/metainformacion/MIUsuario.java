package metainformacion;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

/**
 * <p>
 * Description: Clase que representa un usuario de una aplicacion. Guardamos
 * informacion sobre: Nombre, clave, roles que tiene permitidos, restricciones
 * sobre el uso de ciertos componentes (no tiene que haber restriccion sobre
 * todos), rol por defecto y estado del usuario (conectado/desconectado)
 * </p>
 * 
 * @author Juan Antonio Ibañez Santorum (looper@telefonica.net)
 */
public class MIUsuario implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nombreUsuario = null;

	private String clave = null;

	private Vector<String> rolesPermitidos = new Vector<String>();

	private Vector<MIComponente> permisosComponentes = new Vector<MIComponente>();

	private String rolPorDefecto = null;

	private String rolActual = null;

	private boolean online = false;

	private boolean administrador = false;

	private long actualizado = -1;

	private int identificador = -1;

	public int getIdentificador()
	{
		return identificador;
	}

	public void setIdentificador(int identificador)
	{
		this.identificador = identificador;
	}

	public MIUsuario( String nombreUsuario, String clave )
	{
		this.nombreUsuario = nombreUsuario;
		this.clave = clave;
	}

	public void actualizarMarcaTiempo()
	{
		this.actualizado = new Date().getTime();
	}

	public long getActualizado()
	{
		return actualizado;
	}

	/**
	 * Obtenemos el nombre del usuario
	 * 
	 * @return String Nombre del usuario
	 */
	public String getNombreUsuario()
	{
		return nombreUsuario;
	}

	/**
	 * Obtenemos la clave del usuario
	 * 
	 * @return String Clave del usuario
	 */
	public String getClave()
	{
		return clave;
	}

	/**
	 * Obtiene el rol actual del usuario. Solo tiene sentido si el usuario está
	 * conectado
	 * 
	 * @return String Rol actual del usuario.
	 */
	public String getRolActual()
	{
		return rolActual;
	}

	/**
	 * Especifica el rol actual del usuario
	 * 
	 * @param rol
	 *            String Rol que le deseamos especificar
	 */
	public void setRolActual(String rol)
	{
		if (tieneRolPermitido(rol)) rolActual = rol;
	}

	/**
	 * Buscamos si el usuario tiene permitido un determinado rol
	 * 
	 * @param rol
	 *            String Nombre del rol sobre el que queremos saber si se tiene
	 *            o no permiso.
	 * @return boolean Si el rol no esta entre los permitidos devolvera false.
	 *         True en caso contrario
	 */
	public boolean tieneRolPermitido(String rol)
	{
		String aux = null;
		boolean encontrado = false;
		int i = 0;
		while (!encontrado && ( i < rolesPermitidos.size() ))
		{
			aux = rolesPermitidos.elementAt(i);
			if (aux.equals(rol)) encontrado = true;
			i++;
		}
		return encontrado;
	}

	/**
	 * Especificamos un nuevo rol permitido al usuario
	 * 
	 * @param rol
	 *            String Nuevo rol que se le permite
	 */
	public boolean nuevoRolPermitido(String rol)
	{
		boolean aniadido = false;
		if (!tieneRolPermitido(rol))
		{
			rolesPermitidos.add(rol);
			aniadido = true;
		}
		return aniadido;
	}

	/**
	 * Quitamos el rol de entre los permitidos. Si no existe el rol no se
	 * realiza accion alguna. No se eliminara el rol por defecto como rol
	 * permitido
	 * 
	 * @param rol
	 *            String Rol que deseamos eliminar
	 */
	public boolean eliminarRolPermitido(String rol)
	{
		int pos = getPosicionRol(rol);
		boolean eliminado = false;
		if (( pos != -1 ) && !rol.equals(rolPorDefecto))
		{
			rolesPermitidos.remove(pos);
			eliminado = true;
		}
		return eliminado;
	}

	/**
	 * Obtiene el permiso que tiene el usuario sobre un determinado rol.
	 * 
	 * @param componente
	 *            String Componente sobre el que deseamos obtener informacion.
	 * @return int Permiso del comoponente. Si el componente no está definido
	 *         devuelve -1.
	 */
	public int getPermisoComponente(String componente)
	{
		int permiso = -1;
		MIComponente aux = getComponente(componente);

		if (aux != null) permiso = aux.permisoComponente();
		return permiso;
	}

	/**
	 * Cambia el permiso del componente para el usuario. Si no existe el
	 * componente se añade un nuevo componente con el permiso indicado
	 * 
	 * @param componente
	 *            String Componente al que se quiere cambiar el permiso
	 * @param permiso
	 *            int Nuevo permiso que deseamos establecer al componente
	 */
	public void setPermisoComponente(String componente, int permiso)
	{
		MIComponente aux = getComponente(componente);

		if (aux != null)
			aux.setPermisoComponente(permiso);
		else permisosComponentes.add(new MIComponente(componente, permiso));
	}

	/**
	 * Incluimos informacion sobre el permiso de un cierto componente. Si el
	 * componente ya estuviera definido se cambia su permiso. En caso contrario
	 * se crea la informacion para ese componente
	 * 
	 * @param componente
	 *            String Componente que deseamos añadir
	 * @param permiso
	 *            int Permiso con el que queremos añadir el componente
	 */
	public void nuevoPermisoComponente(String componente, int permiso)
	{
		MIComponente aux = getComponente(componente);
		if (aux == null)
			permisosComponentes.add(new MIComponente(componente, permiso));
		else aux.setPermisoComponente(permiso);
	}

	/**
	 * Especificamos el rol por defecto de este usuario
	 * 
	 * @param rol
	 *            String
	 */
	public void setRolPorDefecto(String rol)
	{
		rolPorDefecto = rol;
	}

	/**
	 * Obtenemos los componentes para los que se ha especificado algun permiso
	 * 
	 * @return Vector Vector de MIComponente con los componentes para los que se
	 *         ha definido algun permiso
	 */
	@SuppressWarnings("unchecked")
	public Vector getPermisosComponentes()
	{
		return permisosComponentes;
	}

	/**
	 * Obtenemos los roles permitidos para este usuario
	 * 
	 * @return Vector Vector de String con los nombres de los roles permitidos a
	 *         este usuario
	 */
	@SuppressWarnings("unchecked")
	public Vector getRolesPermitidos()
	{
		return rolesPermitidos;
	}

	/**
	 * Obtiene el rol por defecto de este usuario
	 * 
	 * @return String Nombre del rol por defecto de este usuario
	 */
	public String getRolPorDefecto()
	{
		return rolPorDefecto;
	}

	/**
	 * Cambia el usuario a online o desconectado
	 * 
	 * @param b
	 *            boolean Estado del usuario que deseamos establecer. True para
	 *            indicar que esta online y False para indicar que esta
	 *            desconectado
	 */
	public void setOnline(boolean b)
	{
		online = b;
		if (b) this.actualizado = new Date().getTime();
	}

	/**
	 * Obtenemos el estado del usuario
	 * 
	 * @return boolean Devolvera True si esta conectado o False en caso
	 *         contrario.
	 */
	public boolean online()
	{
		return online;
	}

	public void setEsAdministrador(boolean b)
	{
		administrador = b;
	}

	public boolean esAdministrador()
	{
		return administrador;
	}

	// *********** FUNCIONES DE APOYO
	// **********************************************

	/**
	 * Nos devuelve una instancia del componente con el nombre indicado
	 * 
	 * @param nombreComponente
	 *            String Nombre del componente que queremos obtener
	 * @return MIComponente La instancia del componente buscado. Si no existe un
	 *         componente con ese nombre devuelve null.
	 */
	private MIComponente getComponente(String nombreComponente)
	{
		MIComponente aux = null;
		MIComponente encontrado = null;
		int i = 0;
		while (( encontrado == null ) && ( i < permisosComponentes.size() ))
		{
			aux = permisosComponentes.elementAt(i);
			if (aux.getNombreComponente().equals(nombreComponente))
				encontrado = aux;
			i++;
		}
		return encontrado;
	}

	/**
	 * Devuelve la posicion dentro del vectoor interno del rol con el nombre
	 * indicado
	 * 
	 * @param rol
	 *            String Rol sobre el que deseamos buscar informacion
	 * @return int Posicion dentro del vector. Si no existe rol con ese nombre
	 *         devuelve -1
	 */
	private int getPosicionRol(String rol)
	{
		int pos = -1;
		int i = 0;
		String aux = null;
		while (( pos == -1 ) && ( i < rolesPermitidos.size() ))
		{
			aux = rolesPermitidos.elementAt(i);
			if (aux.equals(rol)) pos = i;
			i++;
		}
		return pos;
	}

	@Override
	public String toString()
	{
		int i = 0;
		String cadena = new String("***********************\n");
		cadena = new String(cadena + "Nombre usuario: " + nombreUsuario + "\n");
		cadena = new String(cadena + "Clave : " + clave + "\n");
		cadena = new String(cadena + "Rol por defecto : " + rolPorDefecto
				+ "\n");
		cadena = new String(cadena + "Roles permitidos:\n");
		for (i = 0; i < rolesPermitidos.size(); i++)
			cadena = new String(cadena + "  " + rolesPermitidos.elementAt(i)
					+ "\n");
		cadena = new String(cadena + "Permisos componentes:\n");
		for (i = 0; i < permisosComponentes.size(); i++)
		{
			MIComponente aux = permisosComponentes.elementAt(i);
			cadena = new String(cadena + "  " + aux.getNombreComponente()
					+ " -> " + aux.permisoComponente() + "\n");
		}
		cadena = new String(cadena + "***********************\n");

		return cadena;
	}
}
