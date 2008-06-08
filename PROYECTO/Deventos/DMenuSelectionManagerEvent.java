package Deventos;

import java.util.Vector;

/**
 * Eventos distribuidos para el manejador de seleccion de menus
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DMenuSelectionManagerEvent extends DEvent
{
	private static final long serialVersionUID = 4448368233043907008L;

	public static final Integer SINCRONIZACION = new Integer(0);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);

	public static final Integer CAMBIO_PATH = new Integer(2);

	@SuppressWarnings( "unchecked" )
	public Vector path = null;
}
