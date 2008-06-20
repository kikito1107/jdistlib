package fisica.audio;

import fisica.net.ConfiguracionConexion;

/**
 * Clase que se encarga de encapsular todas la clases que guardan datos acerca
 * del audio y la conexion usada
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class DatosGenerales
{
	private DatosAudio datos_audio;

	private ConfiguracionConexion configuracion_conexion;

	private ConfiguracionAudio configuracion_audio;

	/**
	 * Constructor
	 */
	public DatosGenerales()
	{
		configuracion_conexion = new ConfiguracionConexion();
		configuracion_audio = new ConfiguracionAudio(this);
		datos_audio = new DatosAudio(this);
	}

	/**
	 * Obtiene los datos para el audio
	 * 
	 * @return Datos del audio
	 */
	public DatosAudio getChatModel()
	{
		return datos_audio;
	}

	/**
	 * Obtiene la configuracion de la conexion
	 * 
	 * @return Configuracion de la conexion
	 */
	public ConfiguracionConexion getConnectionSettings()
	{
		return configuracion_conexion;
	}

	/**
	 * Obtiene la configuracion del audio
	 * 
	 * @return Configuracion del audio
	 */
	public ConfiguracionAudio getAudioSettings()
	{
		return configuracion_audio;
	}
}
