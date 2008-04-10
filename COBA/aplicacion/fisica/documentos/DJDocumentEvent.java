package aplicacion.fisica.documentos;

import Deventos.DEvent;

public class DJDocumentEvent extends DEvent 
{
	private static final long serialVersionUID = 1L;
	
	public static final Integer SINCRONIZACION = new Integer(77);
	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(78);
	public static final Integer CARGADO = new Integer(2);
	public static final Integer ANOTACION = new Integer(3);
	
	public Documento contenido = null;
	
	public DJDocumentEvent()
	{
	
	}
	
	public DJDocumentEvent(DJDocumentEvent evento)
	{
		this.origen = (evento.origen == null) ? null :
			  new Integer(evento.origen.intValue());
		 this.destino = (evento.destino == null) ? null :
			  new Integer(evento.destino.intValue());
		 this.contador = (evento.contador == null) ? null :
			  new Long(evento.contador.longValue());
		 this.usuario = (evento.usuario == null) ? null : new String(evento.usuario);
		 this.tipo = (evento.tipo == null) ? null : new Integer(evento.tipo.intValue());
		 this.aplicacion = (evento.aplicacion == null) ? null :
			  new String(evento.aplicacion);
		 this.componente = (evento.componente == null) ? null :
			  new Integer(evento.componente.intValue());
		 this.nombreComponente = (evento.nombreComponente == null) ? null :
			  new String(evento.nombreComponente);
		 this.ultimoProcesado = (evento.ultimoProcesado == null) ? null :
			  new Integer(evento.ultimoProcesado.intValue());
		 this.contenido = (evento.contenido == null) ? null : evento.contenido;
	}
	
}
