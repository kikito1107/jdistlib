package Deventos;

import java.util.Vector;

/**
 * Eventos distribuidos de arbol
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DJTreeEvent extends DEvent
{
	private static final long serialVersionUID = 8023764190845874051L;

	public static final Integer SINCRONIZACION = new Integer(0);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);

	public static final Integer APERTURA_CIERRE = new Integer(2);

	public static final Integer SELECCION = new Integer(3);

	@SuppressWarnings( "unchecked" )
	public Vector path = null;

	@SuppressWarnings( "unchecked" )
	public Vector estado = null;

	/**
	 * Constructor por defecto
	 */
	public DJTreeEvent()
	{
	}

	/**
	 * Constructor de copias
	 * 
	 * @param evento
	 *            Evento a copiar
	 */
	@SuppressWarnings( "unchecked" )
	public DJTreeEvent( DJTreeEvent evento )
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

		this.path = ( evento.path == null ) ? null : (Vector) evento.path
				.clone();
		this.estado = ( evento.estado == null ) ? null : (Vector) evento.estado
				.clone();

		System.out.println("evento de arbol");

	}

}
