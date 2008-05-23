package aplicacion.plugin.deventos;

import Deventos.DEvent;

public class DPluginRegisterEvent extends DEvent
{
	private static final long serialVersionUID = 6349698439393004785L;

	public static final Integer SINCRONIZACION = new Integer(0);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);

	public String nombre = null;

	public Long version;

	public String ip = null;

	public String jarPath = null;
}
