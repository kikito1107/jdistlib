package Deventos;

/**
 * Eventos distribuidos de raton
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DMouseEvent extends DEvent
{
	private static final long serialVersionUID = 7584830432002351244L;

	public static final Integer CAMBIO_POSICION = new Integer(0);

	public static final Integer PETICION_INFORMACION = new Integer(1);

	public static final Integer OCULTACION = new Integer(2);

	public Integer px = null;

	public Integer py = null;

	public String rol = null;

}
