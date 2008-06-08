package Deventos;

/**
 * Eventos distribuidos de boton
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DJButtonEvent extends DEvent
{
	private static final long serialVersionUID = 4070549006834251523L;

	public static final Integer SINCRONIZACION = new Integer(0);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);

	public static final Integer PRESIONADO = new Integer(2);

	public static final Integer SOLTADO = new Integer(3);

	public Boolean pulsado = null;

	/**
	 * Constructor por defecto
	 */
	public DJButtonEvent()
	{
	}

	/**
	 * Constructor de copias
	 * 
	 * @param evento
	 *            Evento a copiar
	 */
	public DJButtonEvent( DJButtonEvent evento )
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

		this.pulsado = ( evento.pulsado == null ) ? null : new Boolean(
				evento.pulsado.booleanValue());

	}
}
