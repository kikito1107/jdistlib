package fisica.net;

/**
 * Clase para guardar los datos de configuracion de un protocolo de red
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class ConfiguracionConexion
{
	public static final int TIPO_CONEXION_TCP = 1;

	public static final int TIPO_CONEXION_UDP = 2;

	public static final int PUERTO_POR_DEFECTO = 8765;

	public static final int TIPO_CONEXION_POR_DEFECTO = TIPO_CONEXION_TCP;

	private int puerto;

	private int tipo_conexion;

	/**
	 * Constructor
	 */
	public ConfiguracionConexion()
	{
		setPuerto(PUERTO_POR_DEFECTO);
		setTipoConexion(TIPO_CONEXION_POR_DEFECTO);
	}

	/**
	 * Asigna el puerto para la conexion
	 * 
	 * @param p
	 *            Puerto para la conexion
	 */
	public void setPuerto(int p)
	{
		puerto = p;
	}

	/**
	 * Obtiene el puerto para la conexion
	 * 
	 * @return Puerto para la conexion
	 */
	public int getPuerto()
	{
		return puerto;
	}

	/**
	 * Asigna el tipo de conexion
	 * 
	 * @param t
	 *            Tipo de conexion
	 */
	public void setTipoConexion(int t)
	{
		tipo_conexion = t;
	}

	/**
	 * Obtiene el tipo de conexion
	 * 
	 * @return Tipo de conexion
	 */
	public int getTipoConexion()
	{
		return tipo_conexion;
	}
}
