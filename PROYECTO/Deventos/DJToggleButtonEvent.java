package Deventos;

/**
 * Eventos distribuidos de toggle button
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DJToggleButtonEvent extends DEvent
{
	private static final long serialVersionUID = 8168417816387222116L;

	public static final Integer SINCRONIZACION = new Integer(0);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);

	public static final Integer PRESIONADO = new Integer(2);

	public static final Integer SOLTADO = new Integer(3);

	public Boolean presionado = null;

	public Boolean marcado = null;

	/**
	 * Constructor
	 */
	public DJToggleButtonEvent()
	{
	}

}
