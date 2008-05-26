package Deventos;

import java.awt.Color;
import java.util.Vector;

import javax.swing.ImageIcon;

import aplicacion.fisica.documentos.Anotacion;
import aplicacion.fisica.documentos.Pagina;

import figuras.Figura;

/**
 * 
 * @author Carlos Rodriguez Dominguez
 * @date 2-1-2008
 */

/**
 * Clase que implementa los eventos asociados a un lienzo de dibujo
 */
public class DJLienzoEvent extends DJViewerEvent
{
	private static final long serialVersionUID = 1L;

	public static final Integer SINCRONIZACION = new Integer(77);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(
			SINCRONIZACION.intValue() + 1);

	public static final Integer NUEVA_PAGINA = new Integer(SINCRONIZACION
			.intValue() + 2);

	public static final Integer NUEVA_ANOTACION = new Integer(3390);

	public static final Integer LIMPIEZA_LIENZO = new Integer(SINCRONIZACION
			.intValue() + 4);

	public static final Integer DESHACER = new Integer(SINCRONIZACION
			.intValue() + 5);

	public static final Integer BORRADO = new Integer(
			SINCRONIZACION.intValue() + 6);

	public static final Integer REHACER = new Integer(
			SINCRONIZACION.intValue() + 7);

	public static final Integer BORRAR_IMAGEN = new Integer(SINCRONIZACION
			.intValue() + 8);

	public ImageIcon imagen = null;

	public Figura dibujo = null;

	public Color color = null;

	public Anotacion anotacion = null;

	public Pagina pagina = null;

	public Integer numPagina = null;

	public Vector<Color> colores = null;

	public Vector<Figura> dibujos = null;

	public Integer aBorrar = null;

	public Integer idDestino = null;

	public Integer puerto = null;

	public String direccionRMI = null;

	public String path = null;

	public Boolean sincronizarFichero = null;

	public DJLienzoEvent()
	{

	}

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
		this.colores = ( evento.colores == null ) ? null : evento.colores;
		this.dibujos = ( evento.dibujos == null ) ? null : evento.dibujos;
		this.aBorrar = ( evento.aBorrar == null ) ? null : evento.aBorrar;
	}

}
