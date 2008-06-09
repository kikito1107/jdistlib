package Deventos;

import java.util.Vector;

import javax.swing.ImageIcon;

import componentes.util.ElementoLista;


/**
 * Eventos distribuidos de lista
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DJListEvent extends DEvent
{
	private static final long serialVersionUID = -2737865599786314171L;

	public static final Integer SINCRONIZACION = new Integer(0);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);

	public static final Integer CAMBIO_POSICION = new Integer(2);

	public static final Integer ANIADIR_ELEMENTO = new Integer(3);

	public static final Integer ANIADIR_ELEMENTOS = new Integer(4);

	public static final Integer ELIMINAR_ELEMENTO = new Integer(5);

	public static final Integer ELIMINAR_ELEMENTO_POSICION = new Integer(6);

	public Integer posicion = null;

	public String elemento = null;

	public ImageIcon imagen = null;

	@SuppressWarnings( "unchecked" )
	public Vector elementos = null;

	public ElementoLista ellista = null;

	public ImageIcon[] imagenes = null;

	public String[] textos = null;

	/**
	 * Constructor
	 */
	public DJListEvent()
	{
	}

	/**
	 * Constructor con parametros
	 * 
	 * @param nuevaPosicion
	 *            Nueva posicion de seleccion en la lista
	 */
	public DJListEvent( int nuevaPosicion )
	{
		posicion = new Integer(nuevaPosicion);
	}
}
