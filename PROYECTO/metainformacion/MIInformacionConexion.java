package metainformacion;

import java.io.Serializable;
import java.util.Vector;

/**
 * Metainformacion de una conexion
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class MIInformacionConexion implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Mensaje de error producido por el sistema
	 */
	public String mensajeError = null;

	/**
	 * Rol asociado a la conexion
	 */
	public String rol = null;

	/**
	 * Indica si es un administrador o no
	 */
	public Boolean esAdministrador = new Boolean(false);

	/**
	 * Vector con la metainformacion de los componentes de una aplicacion
	 */
	public Vector<MIComponente> componentes = new Vector<MIComponente>();

	/**
	 * Indica si una identificacion de un usuario es valida o no
	 */
	public Boolean identificacionValida = new Boolean(false);

	/**
	 * Vector con los roles permitidos
	 */
	@SuppressWarnings( "unchecked" )
	public Vector rolesPermitidos = new Vector();

	/**
	 * Vector con los usuarios conectados
	 */
	@SuppressWarnings( "unchecked" )
	public Vector usuariosConectados = new Vector();

}
