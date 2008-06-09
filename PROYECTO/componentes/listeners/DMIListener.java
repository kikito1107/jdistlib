package componentes.listeners;

import awareness.MICompleta;

/**
 * Interfaz para los listeners de los eventos de metainformacion
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface DMIListener
{
	/**
	 * Accion realizada al conectarse un usuario con un rol
	 * 
	 * @param usuario
	 *            Nombre del usuario que se conecta
	 * @param rol
	 *            Rol del usuario que se conecta
	 */
	public void conexionUsuario(String usuario, String rol);

	/**
	 * Accion realizada al desconectarse un usuario
	 * 
	 * @param usuario
	 *            Usuario que se desconecta
	 */
	public void desconexionUsuario(String usuario);

	/**
	 * Accion que se realiza al cambiar el rol de un usuario
	 * 
	 * @param usuario
	 *            Nombre del usuario
	 * @param rol
	 *            Nombre del nuevo rol del usuario
	 * @param rolAntiguo
	 *            Nombre del antiguo rol del usuario
	 * @param info
	 *            Metainformacion del usuario
	 */
	public void cambioRolUsuario(String usuario, String rol, String rolAntiguo,
			MICompleta info);

	/**
	 * Accion que se realiza al cambiar los permisos de acceso sobre un
	 * componente distribuido para un usuario
	 * 
	 * @param usuario
	 *            Nombre del usuario
	 * @param componente
	 *            Nombre del componente distribuido
	 * @param permiso
	 *            Permisos sobre el componente nuevos
	 */
	public void cambioPermisoComponenteUsuario(String usuario,
			String componente, int permiso);

	/**
	 * Accion que se realiza al cambiar los permisos de acceso sobre un
	 * componente distribuido para un rol
	 * 
	 * @param rol
	 *            Nombre del rol
	 * @param componente
	 *            Nombre del componente distribuido
	 * @param permiso
	 *            Permisos sobre el componente nuevos
	 */
	public void cambioPermisoComponenteRol(String rol, String componente,
			int permiso);

	/**
	 * Accion que se realiza al agregar un rol permitido a un usuario concreto
	 * 
	 * @param usuario
	 *            Nombre del usuario
	 * @param rol
	 *            Nombre del nuevo rol permitido para el usuario
	 */
	public void nuevoRolPermitido(String usuario, String rol);

	/**
	 * Accion que se realiza al eliminar un rol permitido a un usuario concreto
	 * 
	 * @param usuario
	 *            Nombre del usuario
	 * @param rol
	 *            Nombre del rol permitido eliminado para el usuario
	 */
	public void eliminarRolPermitido(String usuario, String rol);

	/**
	 * Accion que se realiza al eliminar un usuario
	 * 
	 * @param usuario
	 *            Nombre del usuario a eliminar
	 */
	public void usuarioEliminado(String usuario);

	/**
	 * Accion que se realiza al eliminar un rol
	 * 
	 * @param usuario
	 *            Nombre del rol a eliminar
	 */
	public void rolEliminado(String rol);
}
