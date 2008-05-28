package aplicacion.plugin.example.pizarra;

import java.util.Vector;

import Deventos.DJViewerEvent;


import figuras.Figura;

/**
 * 
 * @author Carlos Rodriguez Dominguez
 * @date 2-1-2008
 */

/**
 * Clase que implementa los eventos asociados a un lienzo de dibujo
 */
public class DJPizarraEvent extends DJViewerEvent
{
	private static final long serialVersionUID = 1L;

	public static final Integer SINCRONIZACION = new Integer(453);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(
			SINCRONIZACION.intValue() + 1);

	public static final Integer NUEVA_ANOTACION = new Integer(SINCRONIZACION
			.intValue() + 2);

	public static final Integer LIMPIEZA_LIENZO = new Integer(SINCRONIZACION
			.intValue() + 4);

	public static final Integer DESHACER = new Integer(SINCRONIZACION
			.intValue() + 5);

	public static final Integer BORRADO = new Integer(
			SINCRONIZACION.intValue() + 6);

	public static final Integer REHACER = new Integer(
			SINCRONIZACION.intValue() + 7);

	public Figura dibujo = null;;

	public Vector<Figura> dibujos = null;

	public Integer aBorrar = null;

	public DJPizarraEvent()
	{

	}

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
