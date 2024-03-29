package Deventos;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DJComboBoxEvent
	 extends DEvent {
  public static final Integer SINCRONIZACION = new Integer(0);
  public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);
  public static final Integer ABIERTO = new Integer(2);
  public static final Integer CERRADO = new Integer(3);
  public static final Integer SELECCIONADO = new Integer(4);
  public static final Integer CAMBIO_SELECCION_LISTA = new Integer(5);

  public Boolean abierto = null;
  public Integer itemSeleccionado = null;
  public Integer seleccionLista = null;

  public DJComboBoxEvent() {

  }

  public DJComboBoxEvent(DJComboBoxEvent evento) {
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

	 this.abierto = (evento.abierto == null) ? null :
		  new Boolean(evento.abierto.booleanValue());
	 this.itemSeleccionado = (evento.itemSeleccionado == null) ? null :
		  new Integer(evento.itemSeleccionado.intValue());
	 this.seleccionLista = (evento.seleccionLista == null) ? null :
		  new Integer(evento.seleccionLista.intValue());
  }

}
