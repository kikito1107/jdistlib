package metainformacion;

import java.io.*;
import java.util.*;

/**
 * <p>Description: Clase para guardar metainformacion sobre una aplicacion.
 * Guardamos informacion sobre: Nombre de la aplicacion, permiso por defecto
 * de los componentes, usuarios definidos, roles definidos y componentes definidos.</p>
 * @author Juan Antonio Ibañez Santorum (looper@telefonica.net)
 */

public class MIAplicacion
implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer permisoDefault = new Integer(20);
	private Vector<MIUsuario> usuarios = new Vector<MIUsuario>();
	private Vector<MIRol> roles = new Vector<MIRol>();
	private Vector<MIComponente> componentes = new Vector<MIComponente>();
	private String nombre = null;
	private int identificador;

	public MIAplicacion() {
	}

	public MIRol getRolByName(String nombreRol) {
		MIRol rol = null;
		
		for (int i = 0; i<roles.size(); ++i){
			if (roles.get(i).getNombreRol().equals(nombreRol))
				rol = roles.get(i);
		}
		
		return rol;
	}
	
	/**
	 * Especificamos el nombre de la aplicacion
	 * @param nombre String Nombre deseado
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getIdentificador()
	{
		return identificador;
	}

	public void setIdentificador(int identificador)
	{
		this.identificador = identificador;
	}

	/**
	 * Obtenemos el nombre de la aplicacion
	 * @return String Nombre de la aplicacion
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Especificamos el permiso por defecto de los componentes.
	 * @param permiso int Permiso por defecto
	 */
	public void setPermisoDefault(int permiso) {
		permisoDefault = new Integer(permiso);
	}

	/**
	 * Obtenemos el permiso por defecto de los componentes.
	 * @return int Permiso por defecto
	 */
	public int getPermisoDefault() {
		return permisoDefault.intValue();
	}

	/**
	 * Definimos un nuevo rol para esta aplicacion
	 * @param rol MIRol Nuevo rol
	 */
	public String nuevoRol(MIRol rol) {
		String mensaje = "";
		if (getRol(rol.getNombreRol()) == null) {
			roles.add(rol);
		}
		else {
			mensaje = "No se ha podido añadir al rol";
		}
		return mensaje;
	}

	/**
	 * Definimos un nuevo usuario para esta aplicacion. Si el rol por defecto es nulo no lo a–ade
	 * @param usuario MIUsuario Nuevo usuario
	 */
	public String nuevoUsuario(MIUsuario usuario) {
		String mensaje = "";
		if (getUsuario(usuario.getNombreUsuario()) == null &&
				getRol(usuario.getRolPorDefecto()) != null) {
			usuarios.add(usuario);
		}
		else {
			mensaje = "No se ha podido añadir al usuario";
		}
		return mensaje;
	}

	public boolean eliminarUsuario(String usuario) {
		boolean encontrado = false;
		int i = 0;
		MIUsuario aux = null;
		while (!encontrado && i < usuarios.size()) {
			aux = (MIUsuario) usuarios.elementAt(i);
			if (aux.getNombreUsuario().equals(usuario)) {
				encontrado = true;
				usuarios.remove(i);
			}
			i++;
		}
		return encontrado;
	}

	public void actualizarMarcaTiempoUsuario(String usuario) {
		MIUsuario aux = getUsuario(usuario);
		if (aux != null) {
			aux.actualizarMarcaTiempo();
		}
	}

	public String eliminarRol(String rol) {
		String mensaje = "";
		boolean rolPorDefecto = false;
		int i = 0;
		MIRol aux = null;
		MIUsuario usr = null;
		boolean encontrado = false;
		for (i = 0; i < usuarios.size(); i++) {
			usr = (MIUsuario) usuarios.elementAt(i);
			if (usr.getRolPorDefecto().equals(rol)) {
				rolPorDefecto = true;
				mensaje = "No se pude eliminar un rol que alguien tiene por defecto";
			}
		}

		if (!rolPorDefecto) {
			while (!encontrado && i < roles.size()) {
				aux = (MIRol) roles.elementAt(i);
				if (aux.getNombreRol().equals(rol)) {
					encontrado = true;
					roles.remove(i);
				}
				i++;
			}
			for (i = 0; i < usuarios.size(); i++) {
				usr = (MIUsuario) usuarios.elementAt(i);
				usr.eliminarRolPermitido(rol);
			}
		}

		return mensaje;
	}

	/**
	 * Pone al usuario especificado el estado de conectado. Si no existe el usuario
	 * no se realiza ninguna accion
	 * @param usuario String Usuario que deseamos especificar como conectado
	 */
	public String setUsuarioConectado(String usuario) {
		String rolPorDefecto = null;
		MIUsuario usr = getUsuario(usuario);
		if (usr != null) {
			usr.setOnline(true);
			usr.setRolActual(usr.getRolPorDefecto());
			rolPorDefecto = usr.getRolActual();
		}
		return rolPorDefecto;
	}

	public String setRolActualUsuario(String usuario, String nuevoRol) {
		String mensaje = new String("");
		MIUsuario usr = getUsuario(usuario);
		MIRol rol = getRol(nuevoRol);
		if (usr != null) {
			if (rol != null) {
				usr.setRolActual(nuevoRol);
			}
			else {
				mensaje = new String("No existe el rol " + nuevoRol);
			}
		}
		else {
			mensaje = new String("No existe el usuario " + usuario);
		}
		return mensaje;
	}

	/**
	 * Pone al usuario especificado el estado de desconectado. Si no existe el usuario
	 * no se realiza ningun accion
	 * @param usuario String Usuario que deseamos especificar como desconectado
	 */
	public void setUsuarioDesconectado(String usuario) {
		MIUsuario usr = getUsuario(usuario);
		if (usr != null) {
			usr.setOnline(false);
			usr.setRolActual(null);
		}
	}

	/**
	 * Definimos un nuevo componente para esta aplicacion
	 * @param componente MIComponente Nuevo componente
	 */
	public void nuevoComponente(MIComponente componente) {
		componentes.add(componente);
	}

	/**
	 * Especificamos un permiso para un cierto componente para un rol. Si el
	 * rol no existe no se realiza accion alguna.
	 * @param rol String Rol sobre el que queremos definir el permiso
	 * @param componente String Componente al que queremos especificar el permiso
	 * @param permiso int Permiso para el componente
	 */
	public void nuevoPermisoRol(String rol, String componente, int permiso) {
		MIRol aux = getRol(rol);
		if (aux != null) {
			aux.nuevoPermisoComponente(componente, permiso);
		}
	}

	/**
	 * Especificamos un permiso para un cierto componente para un usuario.. Si el
	 * usuario no existe no se realiza accion alguna
	 * @param usuario String Usuario sobre el que queremos definir el permiso
	 * @param componente String Componente al que queremos especificar el permiso
	 * @param permiso int Permiso para el componente
	 */
	public void nuevoPermisoUsuario(String usuario, String componente,
			int permiso) {
		MIUsuario aux = getUsuario(usuario);
		if (aux != null) {
			aux.nuevoPermisoComponente(componente, permiso);
		}
	}

	/**
	 * Especificamos un nuevo rol permitido par un cierto usuario. Si el usuario
	 * no existe no se realiza accion alguna
	 * @param usuario String Usuario al que queremos añadir un nuevo rol permitido
	 * @param rol String Nuevo rol que le permitimos al usuario
	 */
	public String nuevoRolPermitidoUsuario(String usuario, String rol) {
		String mensaje = "";
		MIUsuario aux = getUsuario(usuario);
		MIRol rl = getRol(rol);
		if (aux != null) {
			if (rl != null) {
				if (!aux.nuevoRolPermitido(rol)) {
					mensaje = "El usuario ya tiene ese rol permitido";
				}
			}
			else {
				mensaje = "No existe el rol";
			}
		}
		else {
			mensaje = "No se ha podido añadir el rol";
		}
		return mensaje;
	}

	/**
	 * Eliminamos un rol de entre los permitidos al usuario. Si el usuario no existe
	 * no se realiza accion alguna
	 * @param usuario String Usuario al que queremos eliminar un rol permitido
	 * @param rol String Rol que deseamos eliminar
	 */
	public String eliminarRolPermitidoUsuario(String usuario, String rol) {
		String mensaje = "";
		MIUsuario aux = getUsuario(usuario);
		if (aux != null) {
			if (!aux.eliminarRolPermitido(rol)) {
				mensaje = "No se ha podido eliminar el rol";
			}
		}
		else {
			mensaje = "No se ha podido eliminar el rol";
		}
		return mensaje;
	}

	/**
	 * Obtenemos los roles definidos para esta aplicacion
	 * @return Vector Vector de MIRol con los roles definidos
	 */
	public Vector getRoles() {
		return roles;
	}

	/**
	 * Obtenemos una instancia del rol especificado
	 * @param rol String Rol que deseamos buscar
	 * @return MIRol Rol deseado. Si no existe se devuelve null
	 */
	public MIRol getRol(String rol) {
		boolean encontrado = false;
		int i = 0;
		MIRol aux = null;
		MIRol rolEncontrado = null;
		while (!encontrado && i < roles.size()) {
			aux = (MIRol) roles.elementAt(i);
			if (aux.getNombreRol().equals(rol)) {
				encontrado = true;
				rolEncontrado = aux;
			}
			i++;
		}
		return rolEncontrado;
	}

	/**
	 * Obtenemos los usuarios definidos para esta aplicacion
	 * @return Vector Vector de MIUsuario con los usuarios definidos
	 */
	public Vector getUsuarios() {
		return usuarios;
	}

	public Vector getUsuariosNoActualizados() {
		MIUsuario usr = null;
		Vector<MIUsuario> v = new Vector<MIUsuario>();
		long tiempoMargen = 60000;
		long tiempoActual = new Date().getTime();
		for (int i = 0; i < usuarios.size(); i++) {
			usr = (MIUsuario) usuarios.elementAt(i);
			if (usr.online() &&
					( (tiempoActual - usr.getActualizado()) > tiempoMargen)) {
				v.add(usr);
			}
		}

		return v;
	}

	/**
	 * Obtenemos aquellos usuarios que estan bajo un determinado rol en el
	 * momento de la consulta
	 * @param rol String Rol sobre el que deseamos hacer la consulta
	 * @return Vector Vector de String con los nombres de los usuarios que
	 * estan bajo el rol pasado como parametro.
	 */
	public Vector<String> getUsuariosBajoRol(String rol) {
		Vector v = getUsuarios();
		Vector<String> conectados = new Vector<String>();
		MIUsuario usr = null;
		for (int i = 0; i < v.size(); i++) {
			usr = (MIUsuario) v.elementAt(i);
			if (usr.online() && usr.getRolActual().equals(rol)) {
				conectados.add(new String(usr.getNombreUsuario()));
			}
		}

		return conectados;
	}

	/**
	 * Obtiene los usuarios conectados
	 * @return Vector Vector de tamaño 2. En la primera posicion tiene un vector
	 * con los nombres de los usuarios conectados. En la segunda posicion tiene
	 * un vector con los roles con los que estan conectados esos usuarios. Un
	 * posible valor devuelto podria ser:<br><br>
	 * Vector1 -> [LooPer,PeTer]<br>
	 * Vector2 -> [Jefe,Empleado]<br>
	 * Esto nos indicaria que LooPer esta conectado con el rol Jefe y PeTer
	 * con el rol Empleado.
	 */
	@SuppressWarnings("unchecked")
	public Vector getUsuariosConectados() {
		Vector<Vector<String>> v = new Vector<Vector<String>>();
		Vector<String> usu = new Vector<String>();
		Vector<String> rol = new Vector<String>();
		MIUsuario usr = null;
		for (int i = 0; i < usuarios.size(); i++) {
			usr = (MIUsuario) usuarios.elementAt(i);
			if (usr.online()) {
				usu.add(usr.getNombreUsuario());
				rol.add(usr.getRolActual());
			}
		}

		v.add(usu);
		v.add(rol);
		return v;
	}

	@SuppressWarnings("unchecked")
	public Vector<MIUsuario> getUsuariosConectadosInfo() {
		
		Vector<MIUsuario> usu = new Vector<MIUsuario>();
		MIUsuario usr = null;
		for (int i = 0; i < usuarios.size(); i++) {
			usr = (MIUsuario) usuarios.elementAt(i);
			if (usr.online()) {
				usu.add(usr);
			}
		}

		return usu;
	}

	/**
	 * Obtenemos una instancia del usuario especificado
	 * @param usuario String Usuario que deseamos buscar
	 * @return MIUsuario Usuario deseado. Si no existe se devuelve null
	 */
	public MIUsuario getUsuario(String usuario) {
		boolean encontrado = false;
		int i = 0;
		MIUsuario aux = null;
		MIUsuario usuarioEncontrado = null;
		while (!encontrado && i < usuarios.size()) {
			aux = (MIUsuario) usuarios.elementAt(i);
			if (aux.getNombreUsuario().equals(usuario)) {
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
	 * @param usuario String Usuario sobre el que queremos la informacion
	 * @return String Rol actual. Si no existe el usuario valdra null;
	 */
	public String getRolActualUsuario(String usuario) {
		MIUsuario usr = getUsuario(usuario);
		String rol = null;
		if (usr != null) {
			rol = usr.getRolActual();
		}
		return rol;
	}

	/**
	 * Obtenemos los componentes definidos para esta aplicacion
	 * @return Vector Vector de MIComponente con los componentes definidos
	 */
	public Vector getComponentes() {
		return componentes;
	}

	/**
	 * Obtenemos una instancia del componente especificado
	 * @param componente String Componente que deseamos buscar
	 * @return MIComponente Componente deseado. Si no existe se devuelve null
	 */
	public MIComponente getComponente(String componente) {
		boolean encontrado = false;
		int i = 0;
		MIComponente aux = null;
		MIComponente componenteEncontrado = null;
		while (!encontrado && i < componentes.size()) {
			aux = (MIComponente) componentes.elementAt(i);
			if (aux.getNombreComponente().equals(componente)) {
				encontrado = true;
				componenteEncontrado = aux;
			}
			i++;
		}
		return componenteEncontrado;
	}

	public int getPermisoComponenteUsuario(String usuario, String componente) {
		int permiso = -1;
		MIUsuario usr = null;
		usr = getUsuario(usuario);
		if (usr != null) {
			permiso = usr.getPermisoComponente(componente);
			if (permiso == -1) {
				permiso = permisoDefault.intValue();
			}
		}
		return permiso;
	}

	public boolean setPermisoComponenteUsuario(String nombreUsuario,
			String componente,
			int nuevoPermiso) {
		boolean cambiado = false;
		MIUsuario usuario = getUsuario(nombreUsuario);
		MIComponente cmp = getComponente(componente);
		if (usuario != null && cmp != null) {
			usuario.setPermisoComponente(componente, nuevoPermiso);
			cambiado = true;
		}
		return cambiado;
	}

	public int getPermisoComponenteRol(String rol, String componente) {
		int permiso = -1;
		MIRol aux = null;
		aux = getRol(rol);
		if (aux != null) {
			permiso = aux.getPermisoComponente(componente);
			if (permiso == -1) {
				permiso = permisoDefault.intValue();
			}
		}
		return permiso;
	}

	public boolean setPermisoComponenteRol(String nombreRol, String componente,
			int nuevoPermiso) {
		boolean cambiado = false;
		MIRol rol = getRol(nombreRol);
		MIComponente cmp = getComponente(componente);
		if (rol != null && cmp != null) {
			rol.setPermisoComponente(componente, nuevoPermiso);
			cambiado = true;
		}
		return cambiado;
	}

	/**
	 * Obtenemos si un cierto rol existe o no
	 * @param rol String Rol sobre el que queremos buscar si existe
	 * @return boolean True si existe. False en otro caso
	 */
	public boolean existeRol(String rol) {
		return (getRol(rol) != null);
	}

	/**
	 * Obtenemos si un cierto usuario existe.
	 * @param usuario String Usuario sobre el que queremos buscar si existe
	 * @return boolean True si existe. False en otro caso
	 */
	public boolean existeUsuario(String usuario) {
		return (getUsuario(usuario) != null);
	}

	/**
	 * Obtenemos si un cierto componente existe
	 * @param componente String Componente sobre el que queremos buscar si existe
	 * @return boolean True si existe. False en otro caso
	 */
	public boolean existeComponente(String componente) {
		return (getComponente(componente) != null);
	}

	public String toString() {
		int i = 0;
		String cadena = new String("------------- " + nombre +
		" ----------------\n");
		cadena = new String(cadena + "Permiso default: " + permisoDefault + "\n");
		cadena = new String(cadena +
		"................ USUARIOS ...................\n");
		for (i = 0; i < usuarios.size(); i++) {
			cadena = new String(cadena + (MIUsuario) usuarios.elementAt(i));
		}
		cadena = new String(cadena + "................ ROLES ...................\n");
		for (i = 0; i < roles.size(); i++) {
			cadena = new String(cadena + (MIRol) roles.elementAt(i));
		}
		cadena = new String(cadena +
		"................ COMPONENTES ...................\n");
		for (i = 0; i < componentes.size(); i++) {
			cadena = new String(cadena + (MIComponente) componentes.elementAt(i) +
			"\n");
		}

		cadena = new String(cadena + "------------- " + nombre +
		" ----------------\n");

		return cadena;
	}

}
