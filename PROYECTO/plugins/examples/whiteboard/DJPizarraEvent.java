package plugins.examples.whiteboard;

import java.util.Vector;

import componentes.gui.visualizador.figuras.Figura;

import Deventos.DJViewerEvent;



/**
 * Clase que implementa los eventos asociados a un lienzo de dibujo
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class DJPizarraEvent extends DJViewerEvent
{
	private static final long serialVersionUID = 1L;

	/**
	 * Identificador de evento de sincronizacion
	 */
	public static final Integer SINCRONIZACION = new Integer(453);

	/**
	 * Identificador de evento de respuesta de sincronizacion
	 */
	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(
			SINCRONIZACION.intValue() + 1);

	/**
	 * Identificador de evento de nueva anotacion
	 */
	public static final Integer NUEVA_ANOTACION = new Integer(SINCRONIZACION
			.intValue() + 2);

	/**
	 * Identificador de evento de limpieza de lienzo
	 */
	public static final Integer LIMPIEZA_LIENZO = new Integer(SINCRONIZACION
			.intValue() + 4);

	/**
	 * Identificador de evento de deshacer
	 */
	public static final Integer DESHACER = new Integer(SINCRONIZACION
			.intValue() + 5);

	/**
	 * Identificador de evento de borrado
	 */
	public static final Integer BORRADO = new Integer(
			SINCRONIZACION.intValue() + 6);

	/**
	 * Identificador de evento de rehacer
	 */
	public static final Integer REHACER = new Integer(
			SINCRONIZACION.intValue() + 7);

	/**
	 * Figura asociada al evento
	 */
	public Figura dibujo = null;;

	/**
	 * Conjunto de figuras del evento
	 */
	public Vector<Figura> dibujos = null;

	/**
	 * Indice de la figura a borrar
	 */
	public Integer aBorrar = null;

	/**
	 * Constructor por defecto
	 */
	public DJPizarraEvent()
	{

	}

	/**
	 * Constructor de copias
	 * @param evento Evento a copiar
	 */
	public DJPizarraEvent( DJPizarraEvent evento )
	{
		this.origen = ( evento.origen == null ) ? null : new Integer(
				evento.origen.intValue());
		this.destino = ( evento.destino == null ) ? null : new Integer(
				evento.destino.intValue());
		this.contador = ( evento.contador == null ) ? null : new Long(
				evento.contador.longValue());
		this.usuario = ( evento.usuario == null ) ? null : new String(
				evento.usuario);
		this.tipo = ( evento.tipo == null ) ? null : new Integer(evento.tipo
				.intValue());
		this.aplicacion = ( evento.aplicacion == null ) ? null : new String(
				evento.aplicacion);
		this.componente = ( evento.componente == null ) ? null : new Integer(
				evento.componente.intValue());
		this.nombreComponente = ( evento.nombreComponente == null ) ? null
				: new String(evento.nombreComponente);
		this.ultimoProcesado = ( evento.ultimoProcesado == null ) ? null
				: new Integer(evento.ultimoProcesado.intValue());
		this.dibujo = ( evento.dibujo == null ) ? null : evento.dibujo;
		this.dibujos = ( evento.dibujos == null ) ? null : evento.dibujos;
		this.aBorrar = ( evento.aBorrar == null ) ? null : evento.aBorrar;
	}

}
