package Deventos;

/**
 * Eventos distribuidos de checkbox
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DJCheckBoxEvent extends DEvent
{
	private static final long serialVersionUID = 811553538312735248L;

	public static final Integer SINCRONIZACION = new Integer(0);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);

	public static final Integer PRESIONADO = new Integer(2);

	public static final Integer SOLTADO = new Integer(3);

	public Boolean presionada = null;

	public Boolean marcada = null;

	/**
	 * Constructor
	 */
	public DJCheckBoxEvent()
	{
	}

}
