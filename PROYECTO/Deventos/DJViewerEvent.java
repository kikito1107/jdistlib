package Deventos;

import javax.swing.ImageIcon;

/**
 * Eventos distribuidos para el visor
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class DJViewerEvent extends DEvent
{
	private static final long serialVersionUID = 1L;

	/**
	 * Identificador de evento de sincronizacion
	 */
	public static final Integer SINCRONIZACION = new Integer(77);

	/**
	 * Identificador de evento de respuesta de sincronizacion
	 */
	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(78);

	/**
	 * Identificador de evento de carga completa
	 */
	public static final Integer CARGADO = new Integer(2);

	/**
	 * Imagen asociada al evento. No es conveniente usar este campo debido a la
	 * sobrecarga de informacion que se realiza sobre el JavaSpace
	 */
	public ImageIcon contenido = null;

	/**
	 * Constructor por defecto
	 */
	public DJViewerEvent()
	{

	}

	/**
	 * Constructor de copias
	 * 
	 * @param evento
	 *            Evento a copiar
	 */
	public DJViewerEvent( DJViewerEvent evento )
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
		this.contenido = ( evento.contenido == null ) ? null : evento.contenido;
	}
}
