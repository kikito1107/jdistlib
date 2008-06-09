package awareness;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

/**
 * Permite guardar metainformacion sobre una aplicacion. Guardamos informacion
 * sobre: Nombre de la aplicacion, permiso por defecto de los componentes,
 * usuarios definidos, roles definidos y componentes definidos.
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class MIAplicacion implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Integer permisoDefault = new Integer(20);

	private Vector<MIUsuario> usuarios = new Vector<MIUsuario>();

	private Vector<MIRol> roles = new Vector<MIRol>();

	private Vector<MIComponente> componentes = new Vector<MIComponente>();

	private String nombre = null;

	private int identificador;

	/**
	 * Constructor
	 */
	public MIAplicacion()
	{
	}

	/**
	 * Especificamos el nombre de la aplicacion
	 * 
	 * @param nombre
	 *            Nombre deseado
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	/**
	 * Obtiene el identificador de la aplicacion
	 * 
	 * @return Identificador de la aplicacion
	 */
	public int getIdentificador()
	{
		return identificador;
	}

	/**
	 * Asigna el identificador de la aplicacion
	 * 
	 * @param identificador
	 */
	public void setIdentificador(int identificador)
	{
		this.identificador = identificador;
	}

	/**
	 * Obtiene el nombre de la aplicacion
	 * 
	 * @return Nombre de la aplicacion
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * Especifica el permiso por defecto de los componentes.
	 * 
	 * @param permiso
	 *            Permiso por defecto
	 */
	public void setPermisoDefault(int permiso)
	{
		permisoDefault = new Integer(permiso);
	}

	/**
	 * Obtiene el permiso por defecto de los componentes.
	 * 
	 * @return Permiso por defecto para los componentes
	 */
	public int getPermisoDefault()
	{
		return permisoDefault.intValue();
	}

	/**
	 * Definimos un nuevo rol para esta aplicacion
	 * 
	 * @param rol
	 *            Metainformacion del nuevo rol
	 */
	public String nuevoRol(MIRol rol)
	{
		String mensaje = "";
		if (getRol(rol.getNombreRol()) == null)
			roles.add(rol);
		else mensaje = "No se ha podido añadir al rol";
		return mensaje;
	}

	/**
	 * Define un nuevo usuario para esta aplicacion. Si el rol por defecto es
	 * null no lo añade
	 * 
	 * @param usuario
	 *            Metainformacion del nuevo usuario
	 */
	public String nuevoUsuario(MIUsuario usuario)
	{
		String mensaje = "";
		if (( getUsuario(usuario.getNombreUsuario()) == null )
				&& ( getRol(usuario.getRolPorDefecto()) != null ))
			usuarios.add(usuario);
		else mensaje = "No se ha podido añadir al usuario";
		return mensaje;
	}

	/**
	 * Elimina un usuario de la aplicacion
	 * 
	 * @param usuario
	 *            Nombre del usuario
	 * @return True si se elimino con exito. False en caso contrario
	 */
	public boolean eliminarUsuario(String usuario)
	{
		boolean encontrado = false;
		int i = 0;
		MIUsuario aux = null;
		while (!encontrado && ( i < usuarios.size() ))
		{
			aux = usuarios.elementAt(i);
			if (aux.getNombreUsuario().equals(usuario))
			{
				encontrado = true;
				usuarios.remove(i);
			}
			i++;
		}
		return encontrado;
	}

	/**
	 * Actualiza la marca de tiempo para un usuario. Evita que se desconecte
	 * automaticamente pasado un tiempo
	 * 
	 * @param usuario
	 *            Nombre del usuario
	 */
	public void actualizarMarcaTiempoUsuario(String usuario)
	{
		MIUsuario aux = getUsuario(usuario);
		if (aux != null) aux.actualizarMarcaTiempo();
	}

	/**
	 * Elimina un rol de la aplicacion
	 * 
	 * @param rol
	 *            Nombre del rol
	 * @return Mensaje del sistema
	 */
	public String eliminarRol(String rol)
	{
		String mensaje = "";
		boolean rolPorDefecto = false;
		int i = 0;
		MIRol aux = null;
		MIUsuario usr = null;
		boolean encontrado = false;
		for (i = 0; i < usuarios.size(); i++)
		{
			usr = usuarios.elementAt(i);
			if (usr.getRolPorDefecto().equals(rol))
			{
				rolPorDefecto = true;
				mensaje = "No se pude eliminar un rol que alguien tiene por defecto";
			}
		}

		if (!rolPorDefecto)
		{
			while (!encontrado && ( i < roles.size() ))
			{
				aux = roles.elementAt(i);
				if (aux.getNombreRol().equals(rol))
				{
					encontrado = true;
					roles.remove(i);
				}
				i++;
			}
			for (i = 0; i < usuarios.size(); i++)
			{
				usr = usuarios.elementAt(i);
				usr.eliminarRolPermitido(rol);
			}
		}

		return mensaje;
	}

	/**
	 * Pone al usuario especificado el estado de conectado. Si no existe el
	 * usuario no se realiza ninguna accion
	 * 
	 * @param usuario
	 *            Usuario que deseamos especificar como conectado
	 * @return Rol por defecto del usuario
	 */
	public String setUsuarioConectado(String usuario)
	{
		String rolPorDefecto = null;
		MIUsuario usr = getUsuario(usuario);
		if (usr != null)
		{
			usr.setOnline(true);
			usr.setRolActual(usr.getRolPorDefecto());
			rolPorDefecto = usr.getRolActual();
		}
		return rolPorDefecto;
	}

	/**
	 * Asigna un rol a un usuario para esta aplicacion
	 * 
	 * @param usuario
	 *            Nombre del usuario
	 * @param nuevoRol
	 *            Nombre del nuevo rol del usuario
	 * @return Mensaje producido por el sistema
	 */
	public String setRolActualUsuario(String usuario, String nuevoRol)
	{
		String mensaje = new String("");
		MIUsuario usr = getUsuario(usuario);
		MIRol rol = getRol(nuevoRol);
		if (usr != null)
		{
			if (rol != null)
				usr.setRolActual(nuevoRol);
			else mensaje = new String("No existe el rol " + nuevoRol);
		}
		else mensaje = new String("No existe el usuario " + usuario);
		return mensaje;
	}

	/**
	 * Pone al usuario especificado el estado de desconectado. Si no existe el
	 * usuario no se realiza ningun accion
	 * 
	 * @param usuario
	 *            Usuario que deseamos especificar como desconectado
	 */
	public void setUsuarioDesconectado(String usuario)
	{
		MIUsuario usr = getUsuario(usuario);
		if (usr != null)
		{
			usr.setOnline(false);
			usr.setRolActual(null);
		}
	}

	/**
	 * Definimos un nuevo componente para esta aplicacion
	 * 
	 * @param componente
	 *            Nuevo componente
	 */
	public void nuevoComponente(MIComponente componente)
	{
		componentes.add(componente);
	}

	/**
	 * Especificamos un permiso para un cierto componente para un rol. Si el rol
	 * no existe no se realiza accion alguna.
	 * 
	 * @param rol
	 *            Rol sobre el que queremos definir el permiso
	 * @param componente
	 *            Componente al que queremos especificar el permiso
	 * @param permiso
	 *            Permiso para el componente
	 */
	public void nuevoPermisoRol(String rol, String componente, int permiso)
	{
		MIRol aux = getRol(rol);
		if (aux != null) aux.nuevoPermisoComponente(componente, permiso);
	}

	/**
	 * Especificamos un permiso para un cierto componente para un usuario.. Si
	 * el usuario no existe no se realiza accion alguna
	 * 
	 * @param usuario
	 *            Usuario sobre el que queremos definir el permiso
	 * @param componente
	 *            Componente al que queremos especificar el permiso
	 * @param permiso
	 *            Permiso para el componente
	 */
	public void nuevoPermisoUsuario(String usuario, String componente,
			int permiso)
	{
		MIUsuario aux = getUsuario(usuario);
		if (aux != null) aux.nuevoPermisoComponente(componente, permiso);
	}

	/**
	 * Especificamos un nuevo rol permitido par un cierto usuario. Si el usuario
	 * no existe no se realiza accion alguna
	 * 
	 * @param usuario
	 *            Usuario al que queremos añadir un nuevo rol permitido
	 * @param rol
	 *            Nuevo rol que le permitimos al usuario
	 */
	public String nuevoRolPermitidoUsuario(String usuario, String rol)
	{
		String mensaje = "";
		MIUsuario aux = getUsuario(usuario);
		MIRol rl = getRol(rol);
		if (aux != null)
		{
			if (rl != null)
			{
				if (!aux.nuevoRolPermitido(rol))
					mensaje = "El usuario ya tiene ese rol permitido";
			}
			else mensaje = "No existe el rol";
		}
		else mensaje = "No se ha podido añadir el rol";
		return mensaje;
	}

	/**
	 * Eliminamos un rol de entre los permitidos al usuario. Si el usuario no
	 * existe no se realiza accion alguna
	 * 
	 * @param usuario
	 *            Usuario al que queremos eliminar un rol permitido
	 * @param rol
	 *            Rol que deseamos eliminar
	 */
	public String eliminarRolPermitidoUsuario(String usuario, String rol)
	{
		String mensaje = "";
		MIUsuario aux = getUsuario(usuario);
		if (aux != null)
		{
			if (!aux.eliminarRolPermitido(rol))
				mensaje = "No se ha podido eliminar el rol";
		}
		else mensaje = "No se ha podido eliminar el rol";
		return mensaje;
	}

	/**
	 * Obtenemos los roles definidos para esta aplicacion
	 * 
	 * @return Vector con la metainformacion de los roles definidos
	 */
	@SuppressWarnings( "unchecked" )
	public Vector getRoles()
	{
		return roles;
	}

	/**
	 * Obtenemos una instancia de la metainformacion del rol especificado
	 * 
	 * @param rol
	 *            Nombre del rol
	 * @return Metainformacion del rol deseado. Si no existe se devuelve null
	 */
	public MIRol getRol(String rol)
	{
		boolean encontrado = false;
		int i = 0;
		MIRol aux = null;
		MIRol rolEncontrado = null;
		while (!encontrado && ( i < roles.size() ))
		{
			aux = roles.elementAt(i);
			if (aux.getNombreRol().equals(rol))
			{
				encontrado = true;
				rolEncontrado = aux;
			}
			i++;
		}
		return rolEncontrado;
	}

	/**
	 * Obtenemos los usuarios definidos para esta aplicacion
	 * 
	 * @return Vector de metainformacion para los usuarios definidos
	 */
	@SuppressWarnings( "unchecked" )
	public Vector getUsuarios()
	{
		return usuarios;
	}

	/**
	 * Obtiene un vector con la metainformacion de los usuario no actualizados
	 * 
	 * @return Vector con la metainformacion de los usuarios no actualizados
	 */
	@SuppressWarnings( "unchecked" )
	public Vector getUsuariosNoActualizados()
	{
		MIUsuario usr = null;
		Vector<MIUsuario> v = new Vector<MIUsuario>();
		long tiempoMargen = 60000;
		long tiempoActual = new Date().getTime();
		for (int i = 0; i < usuarios.size(); i++)
		{
			usr = usuarios.elementAt(i);
			if (usr.online()
					&& ( ( tiempoActual - usr.getActualizado() ) > tiempoMargen ))
				v.add(usr);
		}

		return v;
	}

	/**
	 * Obtenemos aquellos usuarios que estan bajo un determinado rol en el
	 * momento de la consulta
	 * 
	 * @param rol
	 *            Rol sobre el que deseamos hacer la consulta
	 * @return Vector con los nombres de los usuarios que estan bajo el rol
	 *         pasado como parametro.
	 */
	@SuppressWarnings( "unchecked" )
	public Vector<String> getUsuariosBajoRol(String rol)
	{
		Vector v = getUsuarios();
		Vector<String> conectados = new Vector<String>();
		MIUsuario usr = null;
		for (int i = 0; i < v.size(); i++)
		{
			usr = (MIUsuario) v.elementAt(i);
			if (( usr != null ) && usr.online()
					&& ( usr.getRolActual() != null )
					&& usr.getRolActual().equals(rol))
				conectados.add(new String(usr.getNombreUsuario()));
		}

		return conectados;
	}

	/**
	 * Obtiene los usuarios conectados
	 * 
	 * @return Vector de tamaño 2. En la primera posicion tiene un vector con
	 *         los nombres de los usuarios conectados. En la segunda posicion
	 *         tiene un vector con los roles con los que estan conectados esos
	 *         usuarios. Un posible valor devuelto podria ser:<br>
	 *         <br>
	 *         Vector1 -> [LooPer,PeTer]<br>
	 *         Vector2 -> [Jefe,Empleado]<br>
	 *         Esto nos indicaria que LooPer esta conectado con el rol Jefe y
	 *         PeTer con el rol Empleado.
	 */
	@SuppressWarnings( "unchecked" )
	public Vector getUsuariosConectados()
	{
		Vector<Vector<String>> v = new Vector<Vector<String>>();
		Vector<String> usu = new Vector<String>();
		Vector<String> rol = new Vector<String>();
		MIUsuario usr = null;
		for (int i = 0; i < usuarios.size(); i++)
		{
			usr = usuarios.elementAt(i);
			if (usr.online())
			{
				usu.add(usr.getNombreUsuario());
				rol.add(usr.getRolActual());
			}
		}

		v.add(usu);
		v.add(rol);
		return v;
	}

	@SuppressWarnings( "unchecked" )
	public Vector<MIUsuario> getUsuariosConectadosInfo()
	{

		Vector<MIUsuario> usu = new Vector<MIUsuario>();
		MIUsuario usr = null;
		for (int i = 0; i < usuarios.size(); i++)
		{
			usr = usuarios.elementAt(i);
			if (usr.online()) usu.add(usr);
		}

		return usu;
	}

	/**
	 * Obtenemos una instancia del usuario especificado
	 * 
	 * @param usuario
	 *            Usuario que deseamos buscar
	 * @return Usuario deseado. Si no existe se devuelve null
	 */
	public MIUsuario getUsuario(String usuario)
	{
		boolean encontrado = false;
		int i = 0;
		MIUsuario aux = null;
		MIUsuario usuarioEncontrado = null;
		while (!encontrado && ( i < usuarios.size() ))
		{
			aux = usuarios.elementAt(i);
			if (aux.getNombreUsuario().equals(usuario))
			{
				encontrado = true;
				usuarioEncontrado = aux;
			}
			i++;
		}
		return usuarioEncontrado;
	}

	/**
	 * Obtiene el rol actual del usuario. Solo tiene sentido cuando el usuario
	 * esta conectado
	 * 
	 * @param usuario
	 *            Usuario sobre el que queremos la informacion
	 * @return Rol actual. Si no existe el usuario valdra null;
	 */
	public String getRolActualUsuario(String usuario)
	{
		MIUsuario usr = getUsuario(usuario);
		String rol = null;
		if (usr != null) rol = usr.getRolActual();
		return rol;
	}

	/**
	 * Obtenemos los componentes definidos para esta aplicacion
	 * 
	 * @return Vector de metainformacion de los componentes definidos
	 */
	@SuppressWarnings( "unchecked" )
	public Vector getComponentes()
	{
		return componentes;
	}

	/**
	 * Obtenemos una instancia del componente especificado
	 * 
	 * @param componente
	 *            Componente que deseamos buscar
	 * @return Componente deseado. Si no existe se devuelve null
	 */
	public MIComponente getComponente(String componente)
	{
		boolean encontrado = false;
		int i = 0;
		MIComponente aux = null;
		MIComponente componenteEncontrado = null;
		while (!encontrado && ( i < componentes.size() ))
		{
			aux = componentes.elementAt(i);
			if (aux.getNombreComponente().equals(componente))
			{
				encontrado = true;
				componenteEncontrado = aux;
			}
			i++;
		}
		return componenteEncontrado;
	}

	/**
	 * Obtiene los permisos que tiene un usuario para un componente
	 * 
	 * @param usuario
	 *            Nombre del usuario
	 * @param componente
	 *            Nombre del componente
	 * @return Nivel de permisos o -1 si ocurrio algun error
	 */
	public int getPermisoComponenteUsuario(String usuario, String componente)
	{
		int permiso = -1;
		MIUsuario usr = null;
		usr = getUsuario(usuario);
		if (usr != null)
		{
			permiso = usr.getPermisoComponente(componente);
			if (permiso == -1) permiso = permisoDefault.intValue();
		}
		return permiso;
	}

	/**
	 * Asigna un nivel de permisos para un usuario y un componente
	 * 
	 * @param nombreUsuario
	 *            Nombre del usuario
	 * @param componente
	 *            Nombre del componente
	 * @param nuevoPermiso
	 *            Nuevo nivel de permisos
	 * @return True si el nivel de permisos se asigno con exito. False en caso
	 *         contrario
	 */
	public boolean setPermisoComponenteUsuario(String nombreUsuario,
			String componente, int nuevoPermiso)
	{
		boolean cambiado = false;
		MIUsuario usuario = getUsuario(nombreUsuario);
		MIComponente cmp = getComponente(componente);
		if (( usuario != null ) && ( cmp != null ))
		{
			usuario.setPermisoComponente(componente, nuevoPermiso);
			cambiado = true;
		}
		return cambiado;
	}

	/**
	 * Obtiene el nivel de permisos para un rol y un componente
	 * 
	 * @param rol
	 *            Nombre del rol
	 * @param componente
	 *            Nombre del componente
	 * @return Nivel de permisos o -1 si ocurrio algun error
	 */
	public int getPermisoComponenteRol(String rol, String componente)
	{
		int permiso = -1;
		MIRol aux = null;
		aux = getRol(rol);
		if (aux != null)
		{
			permiso = aux.getPermisoComponente(componente);
			if (permiso == -1) permiso = permisoDefault.intValue();
		}
		return permiso;
	}

	/**
	 * Asigna el nivel de permisos de un rol sobre un componente
	 * 
	 * @param nombreRol
	 *            Nombre del rol
	 * @param componente
	 *            Nombre del componente
	 * @param nuevoPermiso
	 *            Nuevo nivel de permisos
	 * @return True si todo fue correctamente. False en caso contrario
	 */
	public boolean setPermisoComponenteRol(String nombreRol, String componente,
			int nuevoPermiso)
	{
		boolean cambiado = false;
		MIRol rol = getRol(nombreRol);
		MIComponente cmp = getComponente(componente);
		if (( rol != null ) && ( cmp != null ))
		{
			rol.setPermisoComponente(componente, nuevoPermiso);
			cambiado = true;
		}
		return cambiado;
	}

	/**
	 * Obtenemos si un cierto rol existe o no
	 * 
	 * @param rol
	 *            Rol sobre el que queremos buscar si existe
	 * @return True si existe. False en otro caso
	 */
	public boolean existeRol(String rol)
	{
		return ( getRol(rol) != null );
	}

	/**
	 * Obtenemos si un cierto usuario existe.
	 * 
	 * @param usuario
	 *            Usuario sobre el que queremos buscar si existe
	 * @return True si existe. False en otro caso
	 */
	public boolean existeUsuario(String usuario)
	{
		return ( getUsuario(usuario) != null );
	}

	/**
	 * Obtenemos si un cierto componente existe
	 * 
	 * @param componente
	 *            Componente sobre el que queremos buscar si existe
	 * @return True si existe. False en otro caso
	 */
	public boolean existeComponente(String componente)
	{
		return ( getComponente(componente) != null );
	}

	@Override
	public String toString()
	{
		int i = 0;
		String cadena = new String("------------- " + nombre
				+ " ----------------\n");
		cadena = new String(cadena + "Permiso default: " + permisoDefault
				+ "\n");
		cadena = new String(cadena
				+ "................ USUARIOS ...................\n");
		for (i = 0; i < usuarios.size(); i++)
			cadena = new String(cadena + usuarios.elementAt(i));
		cadena = new String(cadena
				+ "................ ROLES ...................\n");
		for (i = 0; i < roles.size(); i++)
			cadena = new String(cadena + roles.elementAt(i));
		cadena = new String(cadena
				+ "................ COMPONENTES ...................\n");
		for (i = 0; i < componentes.size(); i++)
			cadena = new String(cadena + componentes.elementAt(i) + "\n");

		cadena = new String(cadena + "------------- " + nombre
				+ " ----------------\n");

		return cadena;
	}

}
