package Deventos;

import java.awt.Color;

import javax.swing.ImageIcon;

import aplicacion.fisica.documentos.Anotacion;
import aplicacion.fisica.documentos.Pagina;
import figuras.Figura;

/**
 * Eventos distribuidos de lienzo
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class DJLienzoEvent extends DJViewerEvent
{
	private static final long serialVersionUID = 1L;

	/**
	 * Identificador de evento de sincronizacion
	 */
	public static final Integer SINCRONIZACION = new Integer(77);

	/**
	 * Identificador de evento de respuesta de sincronizacion
	 */
	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(
			SINCRONIZACION.intValue() + 1);

	/**
	 * Identificador de evento de nueva pagina
	 */
	public static final Integer NUEVA_PAGINA = new Integer(SINCRONIZACION
			.intValue() + 2);

	/**
	 * Identificador de evento de nueva anotacion
	 */
	public static final Integer NUEVA_ANOTACION = new Integer(3390);

	/**
	 * Identificador de evento de limpieza de lienzo
	 */
	public static final Integer LIMPIEZA_LIENZO = new Integer(SINCRONIZACION
			.intValue() + 4);

	/**
	 * Identificador de evento de deshacer una accion
	 */
	public static final Integer DESHACER = new Integer(SINCRONIZACION
			.intValue() + 5);

	/**
	 * Identificador de evento de borrado de elementos
	 */
	public static final Integer BORRADO = new Integer(
			SINCRONIZACION.intValue() + 6);

	/**
	 * Identificador de evento de rehacer una accion
	 */
	public static final Integer REHACER = new Integer(
			SINCRONIZACION.intValue() + 7);

	/**
	 * Identificador de evento de borrado de una imagen o descarga
	 */
	public static final Integer BORRAR_IMAGEN = new Integer(SINCRONIZACION
			.intValue() + 8);

	/**
	 * Imagen asociada al evento
	 */
	public ImageIcon imagen = null;

	/**
	 * Figura correspondiente a una anotacion
	 */
	public Figura dibujo = null;

	/**
	 * Color de la anotacion
	 */
	public Color color = null;

	/**
	 * Anotacion realizada
	 */
	public Anotacion anotacion = null;

	/**
	 * Pagina asociada al evento
	 */
	public Pagina pagina = null;

	/**
	 * Numero de pagina donde se produce el evento
	 */
	public Integer numPagina = null;

	/**
	 * Identificador de la figura a eliminar
	 */
	public Integer aBorrar = null;

	/**
	 * Identificador de destino
	 */
	public Integer idDestino = null;

	/**
	 * Puerto de destino del evento (para RMI)
	 */
	public Integer puerto = null;

	/**
	 * Direccion IP de destino (para RMI)
	 */
	public String direccionRMI = null;

	/**
	 * Path de destino
	 */
	public String path = null;

	/**
	 * Indicar si hay que sincronizar el fichero
	 */
	public Boolean sincronizarFichero = null;

	/**
	 * Constructor por defecto
	 */
	public DJLienzoEvent()
	{

	}

	/**
	 * Constructor de copias
	 * 
	 * @param evento
	 *            Evento a copiar
	 */
	public DJLienzoEvent( DJLienzoEvent evento )
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
		this.imagen = ( evento.imagen == null ) ? null : evento.imagen;
		this.color = ( evento.color == null ) ? null : evento.color;
		this.dibujo = ( evento.dibujo == null ) ? null : evento.dibujo;
		this.aBorrar = ( evento.aBorrar == null ) ? null : evento.aBorrar;
	}

}
