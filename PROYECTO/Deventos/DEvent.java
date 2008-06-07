package Deventos;

import net.jini.core.entry.Entry;

/**
 * Clase genñrica de eventos. Implementa la interfaz Entry para poder ser
 * escrito en el JavaSpace.
 */

public class DEvent implements Entry
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4485497108474849738L;

	public Integer origen = null; // Origen del evento

	public Integer destino = null; // Destino del evento

	public Long contador = null; // Nñmero de secuencia del evento

	public String usuario = null; // Usuario que genera el evento

	public String rol = null;

	public Integer tipo = null; // Tipo de evento

	public String aplicacion = null; // Aplicacion que genera el evento

	public Integer componente = null; // Componente que genera el evento

	public String nombreComponente = null; // Nombre del componente que genera

	// el evento

	public Integer ultimoProcesado = null; // ñltimo evento procesado por el

	// componente

	// que genera el evento

	public DEvent eventoAdjunto = null; // Evento adjunto a este evento

	public DEvent()
	{
	}

	/**
	 * Constructor que crea un evento identico a otro dado
	 * 
	 * @param evento
	 *            DEvent Evento de referencia para generar el nuevo evento
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
	 *            DEvent Evento que se desea adjuntar
	 */
	public void aniadirEventoAdjunto(DEvent evento)
	{
		eventoAdjunto = evento;
	}

	/**
	 * Extrae el evento ajunto. Cuando se extrae el evento se actualizan sus
	 * miembros contador, usuario y aplicacion de tal forma que sean iguales a
	 * los del evento en el que estaba adjuntado
	 * 
	 * @return DEvent null si no hay evento adjunto o el evento ajunto cuando lo
	 *         hay
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
