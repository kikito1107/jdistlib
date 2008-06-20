package fisica.net;

/**
 * Clase abstracta para la implementacion de protocolos de red
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public abstract class BaseNetwork implements Network
{
	private ConfiguracionConexion configuracion;

	/**
	 * Constructor
	 * 
	 * @param conf
	 *            Configuracion de la conexion
	 */
	protected BaseNetwork( ConfiguracionConexion conf )
	{
		configuracion = conf;
	}

	private ConfiguracionConexion getConfiguracionConexion()
	{
		return configuracion;
	}

	protected int getPuerto()
	{
		return getConfiguracionConexion().getPuerto();
	}
}
