package plugins.evento;

import Deventos.DEvent;

/**
 * Evento de registro de un plugin en la plataforma
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class DPluginRegisterEvent extends DEvent
{
	private static final long serialVersionUID = 6349698439393004785L;

	/**
	 * Identificador de tipo de evento de sincronizacion
	 */
	public static final Integer SINCRONIZACION = new Integer(0);

	/**
	 * Identificador de tipo de evento de respuesta de sincronizacion
	 */
	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);

	/**
	 * Nombre del plugin
	 */
	public String nombre = null;

	/**
	 * Version del plugin
	 */
	public Long version;

	/**
	 * Direccion IP
	 */
	public String ip = null;

	/**
	 * Path del fichero jar que contiene los plugins
	 */
	public String jarPath = null;
}
