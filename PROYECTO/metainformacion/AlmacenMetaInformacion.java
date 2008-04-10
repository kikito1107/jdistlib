package metainformacion;

import java.util.*;

import util.*;

/**
 * Clase para ayudar al almacenaje y consulta de toda la informacion
 */

public class AlmacenMetaInformacion {
  private Vector<MIAplicacion> aplicaciones = null;
  private LectorBD_MI lector = null;

  public AlmacenMetaInformacion() {
	 start();
  }

  /**
	* Lectura de la metainformacion desde la base de datos
	*/
  private void start() {	 
	 try {
		lector = new LectorBD_MI();
		lector.crearLog();
		aplicaciones = lector.Recuperar();
		lector.cerrarConexion();		
	 }
	 catch (Exception e) {}

  }

  /**
	* @param aplicacion String Nombre de la aplicacion desde la que nos estamos
	* identificando
	* @param usuario String Nombre de usuario del usuario que deseamos identificar
	* @param clave String Clave para el usuario
	* @return MIInformacionConexion El campo infoCompleta.identificacionValida
	* de esta clase contendra valdra true si la identifiación ha sido correcta
	* o false en otro caso. Si ha sido valida contendra toda la informacion
	* necesaria que sera devuelta a la aplicacion para una inicializacion correcta.
	* Ver clase <code>MIInformacionConexion</code>.
	*/
  public MIInformacionConexion identificar(String aplicacion, String usuario,
														 String clave) {
	 String mensajeError = new String("");
	 MIUsuario usr = null;
	 MICompleta infoCompleta = new MICompleta();

	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 if (apl != null) {
		usr = apl.getUsuario(usuario);
		if (usr != null) {
		  if (!usr.getClave().equals(clave)) {
			 mensajeError = new String("Contraseña incorrecta");
		  }
		  if (usr.online()) {
			 mensajeError = new String(
				  "Ya hay un usuario identificado con ese nombre");
		  }
		}
		else {
		  mensajeError = new String("No existe el usuario " + usuario);
		}
	 }
	 else {
		mensajeError = new String("No hay datos referentes a la aplicacion " +
										  aplicacion);
	 }

	 infoCompleta.mensajeError = new String(mensajeError);
	 if (mensajeError.length() == 0) { // No ha habido error
		usr.setOnline(true);
		usr.setRolActual(usr.getRolPorDefecto());

		infoCompleta.identificacionValida = new Boolean(true);
		infoCompleta.esAdministrador = new Boolean(usr.esAdministrador());
		infoCompleta.rol = usr.getRolPorDefecto();
		infoCompleta.rolesPermitidos = obtenerRolesPermitidos(aplicacion, usuario);
		infoCompleta.usuariosConectados = (Vector) apl.getUsuariosConectados();
		Vector componentes = obtenerComponentes(aplicacion);
		int permisoU = -1;
		int permisoR = -1;
		String componente = null;
		for (int i = 0; i < componentes.size(); i++) {
		  componente = (String) componentes.elementAt(i);
		  permisoU = obtenerPermisoComponenteUsuario(aplicacion, usuario,
				componente);
		  permisoR = obtenerPermisoComponenteRol(aplicacion, infoCompleta.rol,
															  componente);
		  if (permisoR == -1) {
			 if (permisoU == -1) {
				permisoU = apl.getPermisoDefault();
			 }
		  }
		  else {
			 if (permisoU == -1) {
				permisoU = permisoR;
			 }
			 else {
				if (permisoU < permisoR) {
				  permisoU = permisoR;
				}
			 }
		  }
		  //System.out.println(componente + " " + permisoU);
		  infoCompleta.componentes.add(new MIComponente(componente, permisoU));
		}
	 }

	 return infoCompleta;
  }

  /**
	* Cambiamos el estado de un usuario a conectado.
	* @param aplicacion String Aplicacion a la que pertenece el usuario
	* @param usuario String Usuario que se ha conectado
	* @return String Devuelve el rol con el que se ha conectado al usuario. Rol
	* por defecto. Si el usuario o aplicacion no existe valdra null.
	*/
  public String usuarioConectado(String aplicacion, String usuario) {
	 String rol = null;
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 if (apl != null) {
		rol = apl.setUsuarioConectado(usuario);
	 }
	 return rol;
  }

  /**
	* Cambiamos el estado de un usuario a desconectado.
	* @param aplicacion String Aplicacion a la que pertenece el usuario
	* @param usuario String Usuario que se ha conectado
	*/
  public void usuarioDesconectado(String aplicacion, String usuario) {
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 if (apl != null) {
		apl.setUsuarioDesconectado(usuario);
	 }
  }

  public void actualizarMarcaTiempoUsuario(String aplicacion, String usuario) {
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 if (apl != null) {
		apl.actualizarMarcaTiempoUsuario(usuario);
	 }
  }

  /**
	* Cambiamos el rol de uno de los usuarios conectados. Para que el cambio
	* se haga efectivo debe exister el rol y el usuario debe tener el permiso
	* de cambiar a ese rol
	* @param aplicacion String Aplicacion a la que pertenece el usuario
	* @param usuario String Usuario que cambia de rol
	* @param rol String Nuevo rol del usuario
	* @return String Devuelve el mensaje resultante del cambio de rol. Si la
	* longitud es 0 es que no ha habido ningun error. Si es mayor que 0 contiene
	* el error producido. Si vale null es porque no existe la aplicacion indicada
	*/
  public MICompleta cambiarRolUsuarioConectado(String aplicacion,
															  String usuario,
															  String rol) {
	 String mensaje = null;
	 MICompleta info = new MICompleta();
	 MIUsuario usr = null;
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 if (apl != null) {
		mensaje = apl.setRolActualUsuario(usuario, rol);

		if (mensaje.length() == 0) { // No ha habido error
		  usr = apl.getUsuario(usuario);
		  info.identificacionValida = new Boolean(true);
		  info.rol = usr.getRolActual();
		  info.rolesPermitidos = obtenerRolesPermitidos(aplicacion, usuario);
		  Vector componentes = obtenerComponentes(aplicacion);
		  int permisoU = -1;
		  int permisoR = -1;
		  String componente = null;
		  for (int i = 0; i < componentes.size(); i++) {
			 componente = (String) componentes.elementAt(i);
			 permisoU = obtenerPermisoComponenteUsuario(aplicacion, usuario,
				  componente);
			 permisoR = obtenerPermisoComponenteRol(aplicacion, info.rol,
																 componente);
			 if (permisoR == -1) {
				if (permisoU == -1) {
				  permisoU = apl.getPermisoDefault();
				}
			 }
			 else {
				if (permisoU == -1) {
				  permisoU = permisoR;
				}
				else {
				  if (permisoU < permisoR) {
					 permisoU = permisoR;
				  }
				}
			 }
			 info.componentes.add(new MIComponente(componente, permisoU));
		  }
		}
	 }
	 return info;
  }

  /**
	* Obtiene los usuarios conectados en un cierto momento
	* @param aplicacion String Aplicacion de la cual queremos la informacion
	* @return Vector Vector de tamaño 2. En la primera posicion tiene un vector
	* con los nombres de los usuarios conectados. En la segunda posicion tiene
	* un vector con los roles con los que estan conectados esos usuarios. Si no
	* existe la aplicacion devolvera null. Un posible valor devuelto podria
	* ser:<br><br>
	* Vector1 -> [LooPer,PeTer]<br>
	* Vector2 -> [Jefe,Empleado]<br>
	* Esto nos indicaria que LooPer esta conectado con el rol Jefe y PeTer
	* con el rol Empleado.
	*/
  public Vector obtenerUsuariosConectados(String aplicacion) {
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 Vector v = null;
	 if (apl != null) {
		v = apl.getUsuariosConectados();
	 }
	 return v;
  }

// ****************************************************************************

// ************* METODOS PARA MANEJO DE METAINFORMACION ***********************
  public Vector<String> obtenerUsuariosBajoRol(String aplicacion, String rol) {
	 Vector<String> v = new Vector<String>();
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 if (apl != null) {
		v = apl.getUsuariosBajoRol(rol);
	 }
	 return v; // Vector de String
  }

  public Vector obtenerUsuarios(String aplicacion) {
	 Vector v = null;
	 Vector<String> usr = new Vector<String>();
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 MIUsuario usuario = null;
	 if (apl != null) {
		v = apl.getUsuarios();
		if (v != null) {
		  for (int i = 0; i < v.size(); i++) {
			 usuario = (MIUsuario) v.elementAt(i);
			 usr.add(usuario.getNombreUsuario());
		  }
		}
	 }
	 return usr; // Vector de String
  }

  public Vector obtenerUsuariosNoActualizados(String aplicacion) {
	 Vector v = null;
	 Vector<String> usr = new Vector<String>();
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 MIUsuario usuario = null;
	 if (apl != null) {
		v = apl.getUsuariosNoActualizados();
		if (v != null) {
		  for (int i = 0; i < v.size(); i++) {
			 usuario = (MIUsuario) v.elementAt(i);
			 usr.add(usuario.getNombreUsuario());
		  }
		}
	 }
	 return usr; // Vector de String
  }

  public Vector<String> obtenerRoles(String aplicacion) {
	 Vector v = null;
	 Vector<String> rl = new Vector<String>();
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 MIRol rol = null;
	 if (apl != null) {
		v = apl.getRoles();
		if (v != null) {
		  for (int i = 0; i < v.size(); i++) {
			 rol = (MIRol) v.elementAt(i);
			 rl.add(rol.getNombreRol());
		  }
		}
	 }
	 return rl; // Vector de String
  }

  public Vector<String> obtenerComponentes(String aplicacion) {
	 Vector v = null;
	 Vector<String> cmp = new Vector<String>();
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 MIComponente componente = null;
	 if (apl != null) {
		v = apl.getComponentes();
		if (v != null) {
		  for (int i = 0; i < v.size(); i++) {
			 componente = (MIComponente) v.elementAt(i);
			 cmp.add(componente.getNombreComponente());
		  }
		}
	 }
	 return cmp; // Vector de String
  }

  /**
	* Obtiene los roles permitidos a un cierto usuario
	* @param aplicacion String Aplicacion a la que pertenece el usuario
	* @param usuario String Usuario del cual queremos la informacion
	* @return Vector Vector de String que contiene los roles permitidos
	*/
  public Vector<String> obtenerRolesPermitidos(String aplicacion, String usuario) {
	 Vector<String> v = null;
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 MIUsuario usr = null;
	 if (apl != null) {
		usr = apl.getUsuario(usuario);
		if (usr != null) {
		  v = usr.getRolesPermitidos();
		}
	 }
	 return v; // Vector de String
  }

  public int obtenerPermisoComponenteUsuario(String aplicacion, String usuario,
															String componente) {
	 int permiso = -1;
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 if (apl != null) {
		permiso = apl.getPermisoComponenteUsuario(usuario, componente);
	 }
	 return permiso;
  }

  public boolean setPermisoComponenteUsuario(String aplicacion, String usuario,
															String componente,
															int nuevoPermiso) {
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 boolean cambiado = false;
	 if (apl != null) {
		cambiado = apl.setPermisoComponenteUsuario(usuario, componente,
																 nuevoPermiso);
	 }
	 return cambiado;
  }

  public int obtenerPermisoComponenteRol(String aplicacion, String rol,
													  String componente) {
	 int permiso = -1;
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 if (apl != null) {
		permiso = apl.getPermisoComponenteRol(rol, componente);
	 }
	 return permiso;
  }

  public boolean setPermisoComponenteRol(String aplicacion, String rol,
													  String componente, int nuevoPermiso) {
	 boolean cambiado = false;
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 if (apl != null) {
		cambiado = apl.setPermisoComponenteRol(rol, componente, nuevoPermiso);
	 }
	 return cambiado;
  }

  public String obtenerRolActualUsuario(String aplicacion, String usuario) {
	 String rol = new String();
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 if (apl != null) {
		rol = apl.getRolActualUsuario(usuario);
	 }
	 return rol;
  }

  /**
	* Añadimos un nuevo rol a los permitidos de un usuario
	* @param aplicacion String Aplicacion a la que pertenece el usuario
	* @param usuario String Usuario al cual queremos añadir un nuevo rol
	* permitido
	* @param rol String Nuevo rol permitido
	*/
  public String nuevoRolPermitidoUsuario(String aplicacion, String usuario,
													  String rol) {
	 String mensaje = "";
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 if (apl != null) {
		mensaje = apl.nuevoRolPermitidoUsuario(usuario, rol);
	 }
	 return mensaje;
  }

  public String nuevoUsuario(String aplicacion, String usuario, String clave,
									  String rolDefecto, boolean administrador) {
	 String mensaje = "";
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 MIUsuario usr = new MIUsuario(usuario, clave);
	 usr.setRolPorDefecto(rolDefecto);
	 usr.setEsAdministrador(administrador);
	 if (apl != null) {
		mensaje = apl.nuevoUsuario(usr);
	 }
	 else {
		mensaje = "No se ha podido añadir el usuario";
	 }
	 return mensaje;
  }

  public String nuevoRol(String aplicacion, String rol) {
	 String mensaje = "";
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 MIRol rl = new MIRol(rol);
	 if (apl != null) {
		mensaje = apl.nuevoRol(rl);
	 }
	 else {
		mensaje = "No se ha podido aniadir el rol";
	 }
	 return mensaje;
  }

  /**
	* Eliminadmos un rol del los permitidos a un usuario
	* @param aplicacion String Aplicacion a la que pertenece el usuario
	* @param usuario String Usuario al cual queremos eliminar un rol permitido
	* @param rol String Rol que deseamos eliminar
	*/
  public String eliminarRolPermitidoUsuario(String aplicacion, String usuario,
														  String rol) {
	 String mensaje = "";
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 if (apl != null) {
		mensaje = apl.eliminarRolPermitidoUsuario(usuario, rol);
	 }
	 return mensaje;
  }

  public String eliminarUsuario(String aplicacion, String usuario) {
	 String mensaje = "";
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 if (apl != null) {
		if (!apl.eliminarUsuario(usuario)) {
		  mensaje = new String("No se ha podido eliminar el usuario");
		}
	 }
	 return mensaje;
  }

  public String eliminarRol(String aplicacion, String rol) {
	 String mensaje = null;
	 MIAplicacion apl = obtenerAplicacion(aplicacion);
	 if (apl != null) {
		mensaje = apl.eliminarRol(rol);
	 }
	 return mensaje;
  }

  public Vector<String> obtenerAplicaciones() {
	 Vector<String> v = new Vector<String>();
	 MIAplicacion aux = null;
	 for (int i = 0; i < aplicaciones.size(); i++) {
		aux = (MIAplicacion) aplicaciones.elementAt(i);
		v.add(aux.getNombre());
	 }
	 return v;
  }

  /**
	* Guardamos la metainformacion disponible en memoria a una BD.
	*/
  public void salvar() {
	  
	  lector = new LectorBD_MI();
	  lector.actualizarBD(aplicaciones);
	  lector.cerrarConexion();
  }

// ************ FUNCIONES DE APOYO ********************************************
  /**
	* Obtiene la metainformaicon disponible de una cierta aplicacion
	* @param nombre String Nombre de la aplicacion de la que deseamos buscar
	* su metainformacion
	* @return MIAplicacion Metainformacion buscada. En caso de no existir una
	* aplicación con ese nombre valdra null.
	*/
  private MIAplicacion obtenerAplicacion(String nombre) {
	 MIAplicacion aplicacion = null;
	 MIAplicacion aux = null;
	 for (int i = 0; i < aplicaciones.size(); i++) {
		aux = (MIAplicacion) aplicaciones.elementAt(i);
		if (aux.getNombre().equals(nombre)) {
		  aplicacion = aux;
		}
	 }
	 return aplicacion;
  }

  public static void main(String[] args) {
	 AlmacenMetaInformacion smi = new AlmacenMetaInformacion();
	 System.out.println(smi.obtenerPermisoComponenteUsuario("AplicacionDePrueba",
		  "Looper", "componente1"));
	 System.out.println(smi.obtenerPermisoComponenteUsuario("AplicacionDePrueba",
		  "Looper", "componente2"));
	 System.out.println(smi.obtenerPermisoComponenteRol("AplicacionDePrueba",
		  "Jefe", "componente1"));
	 System.out.println(smi.obtenerPermisoComponenteRol("AplicacionDePrueba",
		  "Empleado", "componente1"));
	 //smi.salvarAFichero("aux3.txt");
  }

}
