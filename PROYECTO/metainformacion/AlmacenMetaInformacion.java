package metainformacion;

import java.util.Vector;

/**
 * Clase para ayudar al almacenado y consulta de toda la metainformacion
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class AlmacenMetaInformacion
{
	private Vector<MIAplicacion> aplicaciones = null;

	private LectorBD_MI lector = null;

	/**
	 * Constructor
	 */
	public AlmacenMetaInformacion()
	{
		start();
	}

	/**
	 * Lectura de la metainformacion desde la base de datos
	 */
	private void start()
	{
		try
		{
			lector = new LectorBD_MI();
			lector.crearLog();
			aplicaciones = lector.Recuperar();
			lector.cerrarConexion();
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * @param aplicacion
	 *            Nombre de la aplicacion desde la que nos estamos identificando
	 * @param usuario
	 *            Nombre de usuario del usuario que deseamos identificar
	 * @param clave
	 *            Clave para el usuario
	 * @return El campo infoCompleta.identificacionValida de esta clase
	 *         contendra valdra true si la identifiacion ha sido correcta o
	 *         false en otro caso. Si ha sido valida contendra toda la
	 *         informacion necesaria que sera devuelta a la aplicacion para una
	 *         inicializacion correcta. Ver clase
	 *         <code>MIInformacionConexion</code>.
	 */
	@SuppressWarnings( "unchecked" )
	public MIInformacionConexion identificar(String aplicacion, String usuario,
			String clave)
	{
		String mensajeError = new String("");
		MIUsuario usr = null;
		MICompleta infoCompleta = new MICompleta();

		MIAplicacion apl = obtenerAplicacion(aplicacion);
		if (apl != null)
		{
			usr = apl.getUsuario(usuario);
			if (usr != null)
			{
				if (!usr.getClave().equals(clave))
					mensajeError = new String("Contraseña incorrecta");
				if (usr.online())
					mensajeError = new String(
							"Ya hay un usuario identificado con ese nombre");
			}
			else mensajeError = new String("No existe el usuario " + usuario);
		}
		else mensajeError = new String(
				"No hay datos referentes a la aplicacion " + aplicacion);

		infoCompleta.mensajeError = new String(mensajeError);
		if (mensajeError.length() == 0)
		{ // No ha habido error
			usr.setOnline(true);
			usr.setRolActual(usr.getRolPorDefecto());

			infoCompleta.identificacionValida = new Boolean(true);
			infoCompleta.esAdministrador = new Boolean(usr.esAdministrador());
			infoCompleta.rol = usr.getRolPorDefecto();
			infoCompleta.rolesPermitidos = obtenerRolesPermitidos(aplicacion,
					usuario);
			infoCompleta.usuariosConectados = apl.getUsuariosConectados();
			Vector componentes = obtenerComponentes(aplicacion);
			int permisoU = -1;
			int permisoR = -1;
			String componente = null;
			for (int i = 0; i < componentes.size(); i++)
			{
				componente = (String) componentes.elementAt(i);
				permisoU = obtenerPermisoComponenteUsuario(aplicacion, usuario,
						componente);
				permisoR = obtenerPermisoComponenteRol(aplicacion,
						infoCompleta.rol, componente);
				if (permisoR == -1)
				{
					if (permisoU == -1) permisoU = apl.getPermisoDefault();
				}
				else if (permisoU == -1)
					permisoU = permisoR;
				else if (permisoU < permisoR) permisoU = permisoR;
				// System.out.println(componente + " " + permisoU);
				infoCompleta.componentes.add(new MIComponente(componente,
						permisoU));
			}
		}

		return infoCompleta;
	}

	/**
	 * Cambiamos el estado de un usuario a conectado.
	 * 
	 * @param aplicacion
	 *            Aplicacion a la que pertenece el usuario
	 * @param usuario
	 *            Usuario que se ha conectado
	 * @return Devuelve el rol con el que se ha conectado al usuario. Rol por
	 *         defecto. Si el usuario o aplicacion no existe valdra null.
	 */
	public String usuarioConectado(String aplicacion, String usuario)
	{
		String rol = null;
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		if (apl != null) rol = apl.setUsuarioConectado(usuario);
		return rol;
	}

	/**
	 * Cambiamos el estado de un usuario a desconectado.
	 * 
	 * @param aplicacion
	 *            Aplicacion a la que pertenece el usuario
	 * @param usuario
	 *            Usuario que se ha conectado
	 */
	public void usuarioDesconectado(String aplicacion, String usuario)
	{
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		if (apl != null) apl.setUsuarioDesconectado(usuario);
	}

	/**
	 * Actualiza la marca de tiempo de un usuario
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion a la que esta conectada el usuario
	 * @param usuario
	 *            Nombre del usuario
	 */
	public void actualizarMarcaTiempoUsuario(String aplicacion, String usuario)
	{
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		if (apl != null) apl.actualizarMarcaTiempoUsuario(usuario);
	}

	/**
	 * Cambiamos el rol de uno de los usuarios conectados. Para que el cambio se
	 * haga efectivo debe exister el rol y el usuario debe tener el permiso de
	 * cambiar a ese rol
	 * 
	 * @param aplicacion
	 *            Aplicacion a la que pertenece el usuario
	 * @param usuario
	 *            Usuario que cambia de rol
	 * @param rol
	 *            Nuevo rol del usuario
	 * @return Devuelve el mensaje resultante del cambio de rol. Si la longitud
	 *         es 0 es que no ha habido ningun error. Si es mayor que 0 contiene
	 *         el error producido. Si vale null es porque no existe la
	 *         aplicacion indicada
	 */
	@SuppressWarnings( "unchecked" )
	public MICompleta cambiarRolUsuarioConectado(String aplicacion,
			String usuario, String rol)
	{
		String mensaje = null;
		MICompleta info = new MICompleta();
		MIUsuario usr = null;
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		if (apl != null)
		{
			mensaje = apl.setRolActualUsuario(usuario, rol);

			if (mensaje.length() == 0)
			{ // No ha habido error
				usr = apl.getUsuario(usuario);
				info.identificacionValida = new Boolean(true);
				info.rol = usr.getRolActual();
				info.rolesPermitidos = obtenerRolesPermitidos(aplicacion,
						usuario);
				Vector componentes = obtenerComponentes(aplicacion);
				int permisoU = -1;
				int permisoR = -1;
				String componente = null;
				for (int i = 0; i < componentes.size(); i++)
				{
					componente = (String) componentes.elementAt(i);
					permisoU = obtenerPermisoComponenteUsuario(aplicacion,
							usuario, componente);
					permisoR = obtenerPermisoComponenteRol(aplicacion,
							info.rol, componente);
					if (permisoR == -1)
					{
						if (permisoU == -1) permisoU = apl.getPermisoDefault();
					}
					else if (permisoU == -1)
						permisoU = permisoR;
					else if (permisoU < permisoR) permisoU = permisoR;
					info.componentes
							.add(new MIComponente(componente, permisoU));
				}
			}
		}
		return info;
	}

	/**
	 * Obtiene los usuarios conectados en un cierto momento
	 * 
	 * @param aplicacion
	 *            Aplicacion de la cual queremos la informacion
	 * @return Vector de tamaño 2. En la primera posicion tiene un vector con
	 *         los nombres de los usuarios conectados. En la segunda posicion
	 *         tiene un vector con los roles con los que estan conectados esos
	 *         usuarios. Si no existe la aplicacion devolvera null. Un posible
	 *         valor devuelto podria ser:<br>
	 *         <br>
	 *         Vector1 -> [LooPer,PeTer]<br>
	 *         Vector2 -> [Jefe,Empleado]<br>
	 *         Esto nos indicaria que LooPer esta conectado con el rol Jefe y
	 *         PeTer con el rol Empleado.
	 */
	@SuppressWarnings( "unchecked" )
	public Vector obtenerUsuariosConectados(String aplicacion)
	{
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		Vector v = null;
		if (apl != null) v = apl.getUsuariosConectados();
		return v;
	}

	/**
	 * Obtiene la metainformacion de todos los usuarios conectados
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion a la que se conectan los usuarios
	 * @return Vector con la metainformacion de los usuarios
	 */
	public Vector<MIUsuario> obtenerDatosUsuariosConectados(String aplicacion)
	{
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		Vector<MIUsuario> v = null;
		if (apl != null) v = apl.getUsuariosConectadosInfo();
		return v;
	}

	/**
	 * Obtiene la metainformacion de todos los usuarios, esten conectados o no
	 * en este momento a la aplicacion
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion a la que pertenecen los usuarios
	 * @return Vector con la metainformacion de los usuarios
	 */
	@SuppressWarnings( "unchecked" )
	public Vector<MIUsuario> obtenerDatosUsuarios(String aplicacion)
	{
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		Vector<MIUsuario> v = null;
		if (apl != null) v = apl.getUsuarios();
		return v;
	}

	/**
	 * Obtiene la metainformacion de un rol del sistema
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion
	 * @param name
	 *            Nombre del rol del cual obtener la metainformacion
	 * @return Metainformacion del rol
	 */
	public MIRol obtenerDatosRol(String aplicacion, String name)
	{

		MIAplicacion app = obtenerAplicacion(aplicacion);

		if (app != null)
			return app.getRol(name);
		else return null;
	}

	// ****************************************************************************

	// ********* METODOS PARA MANEJO DE METAINFORMACION **********
	/**
	 * Obtiene el nombre de los usuarios que estan conectados en un rol
	 * determinado
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion
	 * @param rol
	 *            Nombre del rol
	 * @return Vector con los nombres de usuario
	 */
	public Vector<String> obtenerUsuariosBajoRol(String aplicacion, String rol)
	{
		Vector<String> v = new Vector<String>();
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		if (apl != null) v = apl.getUsuariosBajoRol(rol);
		return v; // Vector de String
	}

	/**
	 * Obtiene el nombre de los usuarios de una aplicacion
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion
	 * @return Vector con el nombre de los usuarios
	 */
	@SuppressWarnings( "unchecked" )
	public Vector<String> obtenerUsuarios(String aplicacion)
	{
		Vector v = null;
		Vector<String> usr = new Vector<String>();
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		MIUsuario usuario = null;
		if (apl != null)
		{
			v = apl.getUsuarios();
			if (v != null) for (int i = 0; i < v.size(); i++)
			{
				usuario = (MIUsuario) v.elementAt(i);
				usr.add(usuario.getNombreUsuario());
			}
		}
		return usr; // Vector de String
	}

	/**
	 * Obtiene los nombre de los usuarios cuya metainformacion no esta
	 * actualizada en el sistema
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion
	 * @return Vector con los nombres de usuario
	 */
	@SuppressWarnings( "unchecked" )
	public Vector<String> obtenerUsuariosNoActualizados(String aplicacion)
	{
		Vector v = null;
		Vector<String> usr = new Vector<String>();
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		MIUsuario usuario = null;
		if (apl != null)
		{
			v = apl.getUsuariosNoActualizados();
			if (v != null) for (int i = 0; i < v.size(); i++)
			{
				usuario = (MIUsuario) v.elementAt(i);
				usr.add(usuario.getNombreUsuario());
			}
		}
		return usr; // Vector de String
	}

	/**
	 * Obtiene el nombre de los roles existentes para una aplicacion concreta
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion
	 * @return Vector con el nombre de los roles existentes
	 */
	@SuppressWarnings( "unchecked" )
	public Vector<String> obtenerRoles(String aplicacion)
	{
		Vector v = null;
		Vector<String> rl = new Vector<String>();
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		MIRol rol = null;
		if (apl != null)
		{
			v = apl.getRoles();
			if (v != null) for (int i = 0; i < v.size(); i++)
			{
				rol = (MIRol) v.elementAt(i);
				rl.add(rol.getNombreRol());
			}
		}
		return rl; // Vector de String
	}

	/**
	 * Obtiene el nombre de los componentes existentes para una aplicacion
	 * concreta
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion
	 * @return Vector con el nombre de los componentes existentes
	 */
	@SuppressWarnings( "unchecked" )
	public Vector<String> obtenerComponentes(String aplicacion)
	{
		Vector v = null;
		Vector<String> cmp = new Vector<String>();
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		MIComponente componente = null;
		if (apl != null)
		{
			v = apl.getComponentes();
			if (v != null) for (int i = 0; i < v.size(); i++)
			{
				componente = (MIComponente) v.elementAt(i);
				cmp.add(componente.getNombreComponente());
			}
		}
		return cmp; // Vector de String
	}

	/**
	 * Obtiene los roles permitidos para un cierto usuario
	 * 
	 * @param aplicacion
	 *            Aplicacion a la que pertenece el usuario
	 * @param usuario
	 *            Usuario del cual queremos la informacion
	 * @return Vector de String que contiene los roles permitidos
	 */
	@SuppressWarnings( "unchecked" )
	public Vector<String> obtenerRolesPermitidos(String aplicacion,
			String usuario)
	{
		Vector<String> v = null;
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		MIUsuario usr = null;
		if (apl != null)
		{
			usr = apl.getUsuario(usuario);
			if (usr != null) v = usr.getRolesPermitidos();
		}
		return v; // Vector de String
	}

	/**
	 * Obtiene el nivel de permisos de un usuario para un componente concreto
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion
	 * @param usuario
	 *            Nombre del usuario
	 * @param componente
	 *            Nombre del componente
	 * @return Nivel de permisos
	 */
	public int obtenerPermisoComponenteUsuario(String aplicacion,
			String usuario, String componente)
	{
		int permiso = -1;
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		if (apl != null)
			permiso = apl.getPermisoComponenteUsuario(usuario, componente);
		return permiso;
	}

	/**
	 * Asigna un nivel de permisos para un componente y para un usuario concreto
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion
	 * @param usuario
	 *            Nombre del usuario
	 * @param componente
	 *            Nombre del componente
	 * @param nuevoPermiso
	 *            Nivel de permisos a asignar
	 * @return True si se pudo asignar el nivel de permisos. False en otro caso
	 */
	public boolean setPermisoComponenteUsuario(String aplicacion,
			String usuario, String componente, int nuevoPermiso)
	{
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		boolean cambiado = false;
		if (apl != null)
			cambiado = apl.setPermisoComponenteUsuario(usuario, componente,
					nuevoPermiso);
		return cambiado;
	}

	/**
	 * Obtiene el nivel de permisos para un componente y para un rol
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion
	 * @param rol
	 *            Nombre del rol
	 * @param componente
	 *            Nombre del componente
	 * @return Nivel de permisos para el componente
	 */
	public int obtenerPermisoComponenteRol(String aplicacion, String rol,
			String componente)
	{
		int permiso = -1;
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		if (apl != null)
			permiso = apl.getPermisoComponenteRol(rol, componente);
		return permiso;
	}

	/**
	 * Asigna un nivel de permisos para un componente y para un rol concreto
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion
	 * @param rol
	 *            Nombre del rol
	 * @param componente
	 *            Nombre del componente
	 * @param nuevoPermiso
	 *            Nivel de permisos a asignar
	 * @return True si se pudo asignar el nivel de permisos. False en otro caso
	 */
	public boolean setPermisoComponenteRol(String aplicacion, String rol,
			String componente, int nuevoPermiso)
	{
		boolean cambiado = false;
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		if (apl != null)
			cambiado = apl.setPermisoComponenteRol(rol, componente,
					nuevoPermiso);
		return cambiado;
	}

	/**
	 * Obtiene el nombre del rol que tiene actualmente el usuario
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion
	 * @param usuario
	 *            Nombre de usuario
	 * @return Nombre del rol que tiene el usuario actualmente
	 */
	public String obtenerRolActualUsuario(String aplicacion, String usuario)
	{
		String rol = new String();
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		if (apl != null) rol = apl.getRolActualUsuario(usuario);
		return rol;
	}

	/**
	 * Añadimos un nuevo rol a los permitidos de un usuario
	 * 
	 * @param aplicacion
	 *            Aplicacion a la que pertenece el usuario
	 * @param usuario
	 *            Usuario al cual queremos añadir un nuevo rol permitido
	 * @param rol
	 *            Nuevo rol permitido
	 */
	public String nuevoRolPermitidoUsuario(String aplicacion, String usuario,
			String rol)
	{
		String mensaje = "";
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		if (apl != null) mensaje = apl.nuevoRolPermitidoUsuario(usuario, rol);
		return mensaje;
	}

	/**
	 * Crea un nuevo usuario para una aplicacion
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion
	 * @param usuario
	 *            Nombre de usuario
	 * @param clave
	 *            Clave asociada al nombre de usuario
	 * @param rolDefecto
	 *            Rol por defecto para el usuario
	 * @param administrador
	 *            Indica si el usuario es administrador o no
	 * @return Mensaje producido por el sistema al agregar el usuario
	 */
	public String nuevoUsuario(String aplicacion, String usuario, String clave,
			String rolDefecto, boolean administrador)
	{
		String mensaje = "";
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		MIUsuario usr = new MIUsuario(usuario, clave);
		usr.setRolPorDefecto(rolDefecto);
		usr.setEsAdministrador(administrador);
		if (apl != null)
			mensaje = apl.nuevoUsuario(usr);
		else mensaje = "No se ha podido añadir el usuario";
		return mensaje;
	}

	/**
	 * Crea un nuevo rol para una aplicacion
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion
	 * @param rol
	 *            Nombre del rol
	 * @return Mensaje producido por el sistema al agregar el rol
	 */
	public String nuevoRol(String aplicacion, String rol)
	{
		String mensaje = "";
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		MIRol rl = new MIRol(rol);
		if (apl != null)
			mensaje = apl.nuevoRol(rl);
		else mensaje = "No se ha podido aniadir el rol";
		return mensaje;
	}

	/**
	 * Elimina un rol de los permitidos para un usuario
	 * 
	 * @param aplicacion
	 *            Aplicacion a la que pertenece el usuario
	 * @param usuario
	 *            Usuario al cual queremos eliminar un rol permitido
	 * @param rol
	 *            Rol que deseamos eliminar
	 */
	public String eliminarRolPermitidoUsuario(String aplicacion,
			String usuario, String rol)
	{
		String mensaje = "";
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		if (apl != null)
			mensaje = apl.eliminarRolPermitidoUsuario(usuario, rol);
		return mensaje;
	}

	/**
	 * Elimina un usuario de entre los disponibles para una aplicacion
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion
	 * @param usuario
	 *            Nombre del usuario
	 * @return Mensaje producido por el sistema
	 */
	public String eliminarUsuario(String aplicacion, String usuario)
	{
		String mensaje = "";
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		if (apl != null)
			if (!apl.eliminarUsuario(usuario))
				mensaje = new String("No se ha podido eliminar el usuario");
		return mensaje;
	}

	/**
	 * Elimina un rol de entre los disponibles para una aplicacion
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion
	 * @param rol
	 *            Nombre del rol
	 * @return Mensaje producido por el sistema
	 */
	public String eliminarRol(String aplicacion, String rol)
	{
		String mensaje = null;
		MIAplicacion apl = obtenerAplicacion(aplicacion);
		if (apl != null) mensaje = apl.eliminarRol(rol);
		return mensaje;
	}

	/**
	 * Obtiene las aplicaciones registradas en el sistema
	 * 
	 * @return Vector con los nombre de aplicacion disponibles
	 */
	public Vector<String> obtenerAplicaciones()
	{
		Vector<String> v = new Vector<String>();
		MIAplicacion aux = null;
		for (int i = 0; i < aplicaciones.size(); i++)
		{
			aux = aplicaciones.elementAt(i);
			v.add(aux.getNombre());
		}
		return v;
	}

	/**
	 * Guardamos la metainformacion disponible en memoria al almacen fisico
	 */
	public void salvar()
	{
		lector = new LectorBD_MI();
		lector.actualizarBD(aplicaciones);
		lector.cerrarConexion();
	}

	// ************ FUNCIONES DE APOYO *******************
	/**
	 * Obtiene la metainformaicon disponible de una cierta aplicacion
	 * 
	 * @param nombre
	 *            Nombre de la aplicacion de la que deseamos buscar su
	 *            metainformacion
	 * @return Metainformacion buscada. En caso de no existir una aplicacion con
	 *         ese nombre valdra null.
	 */
	private MIAplicacion obtenerAplicacion(String nombre)
	{
		MIAplicacion aplicacion = null;
		MIAplicacion aux = null;
		for (int i = 0; i < aplicaciones.size(); i++)
		{
			aux = aplicaciones.elementAt(i);
			if (aux.getNombre().equals(nombre)) aplicacion = aux;
		}
		return aplicacion;
	}

	/**
	 * Permite hacer un test del funcionamiento
	 * 
	 * @param args
	 *            Argumentos. Son ignorados
	 */
	public static void main(String[] args)
	{
		AlmacenMetaInformacion smi = new AlmacenMetaInformacion();
		System.out.println(smi.obtenerPermisoComponenteUsuario(
				"AplicacionDePrueba", "Looper", "componente1"));
		System.out.println(smi.obtenerPermisoComponenteUsuario(
				"AplicacionDePrueba", "Looper", "componente2"));
		System.out.println(smi.obtenerPermisoComponenteRol(
				"AplicacionDePrueba", "Jefe", "componente1"));
		System.out.println(smi.obtenerPermisoComponenteRol(
				"AplicacionDePrueba", "Empleado", "componente1"));
	}
}
