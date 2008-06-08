package Deventos;

import java.util.Vector;

/**
 * Eventos distribuidos de menu
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DJMenuEvent extends DEvent
{
	private static final long serialVersionUID = -8785660228126158590L;

	public static final Integer SINCRONIZACION = new Integer(0);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);

	public static final Integer CAMBIO_ESTADO = new Integer(2);

	@SuppressWarnings( "unchecked" )
	public Vector path = null;
}
