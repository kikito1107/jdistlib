package Deventos;

/**
 * Eventos distribuidos de campo de texto
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DJTextFieldEvent extends DEvent
{
	private static final long serialVersionUID = 7169940901048242521L;

	public static final Integer SINCRONIZACION = new Integer(0);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);

	public static final Integer REPLACE = new Integer(2);

	public static final Integer REMOVE = new Integer(3);

	public static final Integer INSERT = new Integer(4);

	public Integer p1 = null;

	public Integer p2 = null;

	public String contenido = null;
}
