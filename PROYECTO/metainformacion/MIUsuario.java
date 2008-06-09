package metainformacion;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

/**
 * Permite obtener la metainformacion de un usuario para una aplicacion.
 * Guardamos informacion sobre: Nombre, clave, roles que tiene permitidos,
 * restricciones sobre el uso de ciertos componentes (no tiene que haber
 * restriccion sobre todos), rol por defecto y estado del usuario
 * (conectado/desconectado)
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class MIUsuario implements Serializable
{
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

	/**
	 * Constructor
	 * 
	 * @param nombreUsuario
	 *            Nombre del usuario
	 * @param clave
	 *            Clave del usuario
	 */
	public MIUsuario( String nombreUsuario, String clave )
	{
		this.nombreUsuario = nombreUsuario;
		this.clave = clave;
	}

	/**
	 * Obtiene el identificador para el usuario
	 * 
	 * @return Identificador del usuario
	 */
	public int getIdentificador()
	{
		return identificador;
	}

	/**
	 * Asigna el identificador a un usuario
	 * 
	 * @param identificador
	 *            Identificador del usuario
	 */
	public void setIdentificador(int identificador)
	{
		this.identificador = identificador;
	}

	/**
	 * Actualiza la marca de tiempo para el usuario
	 */
	public void actualizarMarcaTiempo()
	{
		this.actualizado = new Date().getTime();
	}

	/**
	 * Devuelve la marca de tiempo para el usuario
	 * 
	 * @return Marca de tiempo para el usuario
	 */
	public long getActualizado()
	{
		return actualizado;
	}

	/**
	 * Obtiene el nombre del usuario
	 * 
	 * @return Nombre del usuario
	 */
	public String getNombreUsuario()
	{
		return nombreUsuario;
	}

	/**
	 * Obtiene la clave del usuario
	 * 
	 * @return Clave del usuario
	 */
	public String getClave()
	{
		return clave;
	}

	/**
	 * Obtiene el rol actual del usuario. Solo tiene sentido si el usuario esta
	 * conectado
	 * 
	 * @return Rol actual del usuario.
	 */
	public String getRolActual()
	{
		return rolActual;
	}

	/**
	 * Especifica el rol actual del usuario
	 * 
	 * @param rol
	 *            Rol que le deseamos especificar
	 */
	public void setRolActual(String rol)
	{
		if (tieneRolPermitido(rol)) rolActual = rol;
	}

	/**
	 * Buscamos si el usuario tiene permitido un determinado rol
	 * 
	 * @param rol
	 *            Nombre del rol sobre el que queremos saber si se tiene o no
	 *            permiso.
	 * @return Si el rol no esta entre los permitidos devolvera False. True en
	 *         caso contrario
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
	 * Especificamos un nuevo rol permitido para el usuario
	 * 
	 * @param rol
	 *            Nuevo rol que se le permite
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
	 *            Componente sobre el que deseamos obtener informacion.
	 * @return Permisos para el componente. Si el componente no esta definido
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
	 *            Componente al que se quiere cambiar el permiso
	 * @param permiso
	 *            Nuevo permiso que deseamos establecer al componente
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
	 *            Componente que deseamos añadir
	 * @param permiso
	 *            Permiso con el que queremos añadir el componente
	 */
	public void nuevoPermisoComponente(String componente, int permiso)
	{
		MIComponente aux = getComponente(componente);
		if (aux == null)
			permisosComponentes.add(new MIComponente(componente, permiso));
		else aux.setPermisoComponente(permiso);
	}

	/**
	 * Asigna el rol por defecto de este usuario
	 * 
	 * @param rol
	 *            Nombre del rol
	 */
	public void setRolPorDefecto(String rol)
	{
		rolPorDefecto = rol;
	}

	/**
	 * Obtiene los componentes para los que se ha especificado algun permiso
	 * 
	 * @return Vector de metainformacion de los componentes para los que se ha
	 *         definido algun permiso
	 */
	@SuppressWarnings( "unchecked" )
	public Vector getPermisosComponentes()
	{
		return permisosComponentes;
	}

	/**
	 * Obtiene los roles permitidos para este usuario
	 * 
	 * @return Vector con los nombres de los roles permitidos a este usuario
	 */
	@SuppressWarnings( "unchecked" )
	public Vector getRolesPermitidos()
	{
		return rolesPermitidos;
	}

	/**
	 * Obtiene el rol por defecto de este usuario
	 * 
	 * @return Nombre del rol por defecto de este usuario
	 */
	public String getRolPorDefecto()
	{
		return rolPorDefecto;
	}

	/**
	 * Cambia el usuario a online o desconectado
	 * 
	 * @param b
	 *            Estado del usuario que deseamos establecer. True para indicar
	 *            que esta online y False para indicar que esta desconectado
	 */
	public void setOnline(boolean b)
	{
		online = b;
		if (b) this.actualizado = new Date().getTime();
	}

	/**
	 * Obtiene el estado del usuario
	 * 
	 * @return True si esta conectado. False en caso contrario.
	 */
	public boolean online()
	{
		return online;
	}

	/**
	 * Permite asignar si el usuario es administrador o no
	 * 
	 * @param b
	 *            Indica si el usuario es administrador o no
	 */
	public void setEsAdministrador(boolean b)
	{
		administrador = b;
	}

	/**
	 * Devuelve si el usuario es o no administrador
	 * 
	 * @return True si el usuario es administrador. False en caso contrario
	 */
	public boolean esAdministrador()
	{
		return administrador;
	}

	// *********** FUNCIONES DE APOYO
	// **********************************************

	/**
	 * Devuelve una instancia del componente con el nombre indicado
	 * 
	 * @param nombreComponente
	 *            Nombre del componente que queremos obtener
	 * @return La instancia del componente buscado. Si no existe un componente
	 *         con ese nombre devuelve null.
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
	 * Devuelve la posicion dentro del vector interno del rol con el nombre
	 * indicado
	 * 
	 * @param rol
	 *            Rol sobre el que deseamos buscar informacion
	 * @return Posicion dentro del vector. Si no existe rol con ese nombre
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
