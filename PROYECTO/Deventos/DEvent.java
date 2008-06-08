package Deventos;

import net.jini.core.entry.Entry;

/**
 * Clase generica de eventos. Implementa la interfaz Entry para poder ser
 * escrito en el JavaSpace.
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DEvent implements Entry
{
	private static final long serialVersionUID = -4485497108474849738L;

	/**
	 * Origen del evento
	 */
	public Integer origen = null;

	/**
	 * Destino del evento
	 */
	public Integer destino = null;

	/**
	 * Numero de secuencia del evento
	 */
	public Long contador = null;

	/**
	 * Nombre de usuario que genera el evento
	 */
	public String usuario = null;

	/**
	 * Nombre del rol
	 */
	public String rol = null;

	/**
	 * Identificador del tipo de evento
	 */
	public Integer tipo = null;

	/**
	 * Nombre de la aplicacion que genera el evento
	 */
	public String aplicacion = null;

	/**
	 * Identificador del componente que genera el evento
	 */
	public Integer componente = null;

	/**
	 * Nombre del componente que genera el evento
	 */
	public String nombreComponente = null;

	/**
	 * Ultimo evento procesado
	 */
	public Integer ultimoProcesado = null;

	/**
	 * Evento adjunto a este evento
	 */
	public DEvent eventoAdjunto = null;

	/**
	 * Constructor
	 */
	public DEvent()
	{
	}

	/**
	 * Constructor de copias
	 * 
	 * @param evento
	 *            Evento de referencia para generar el nuevo evento
	 */
	public DEvent( DEvent evento )
	{
		this.origen = ( this.origen == null ) ? null : new Integer(
				evento.origen.intValue());
		this.destino = ( this.destino == null ) ? null : new Integer(
				evento.destino.intValue());
		this.contador = ( this.contador == null ) ? null : new Long(
				evento.contador.longValue());
		this.usuario = ( this.usuario == null ) ? null : new String(
				evento.usuario);
		this.tipo = ( this.tipo == null ) ? null : new Integer(evento.tipo
				.intValue());
		this.aplicacion = ( this.aplicacion == null ) ? null : new String(
				evento.aplicacion);
		this.componente = ( this.componente == null ) ? null : new Integer(
				evento.componente.intValue());
		this.nombreComponente = ( this.nombreComponente == null ) ? null
				: new String(evento.nombreComponente);
		this.ultimoProcesado = ( this.ultimoProcesado == null ) ? null
				: new Integer(evento.ultimoProcesado.intValue());
	}

	/**
	 * Adjunta un evento a este evento
	 * 
	 * @param evento
	 *            Evento que se desea adjuntar
	 */
	public void aniadirEventoAdjunto(DEvent evento)
	{
		eventoAdjunto = evento;
	}

	/**
	 * Extrae el evento adjunto. Cuando se extrae el evento se actualizan sus
	 * miembros contador, usuario y aplicacion de tal forma que sean iguales a
	 * los del evento en el que estaba adjuntado
	 * 
	 * @return null si no hay evento adjunto o el evento adjunto si existe
	 */
	public DEvent extraerEventoAdjunto()
	{
		if (contador != null)
		{
			eventoAdjunto.contador = new Long(this.contador.longValue());
			eventoAdjunto.usuario = new String(this.usuario);
			eventoAdjunto.aplicacion = new String(this.aplicacion);
		}
		return eventoAdjunto;
	}

	/**
	 * Convierte el evento a una cadena de texto para poder hacer logging
	 */
	@Override
	public String toString()
	{
		String cadena = null;
		cadena = new String("[(tipo=" + tipo + ")(componente=" + componente
				+ ")(origen=" + origen + ")(destino=" + destino
				+ ")(aplicacion=" + aplicacion + ")(usuario=" + usuario
				+ ")(contador=" + contador + ")]");
		return cadena;
	}
}
