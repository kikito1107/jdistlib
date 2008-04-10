package componentes;

import javax.swing.JToggleButton;
import javax.swing.Action;
import javax.swing.Icon;
import java.util.*;
import Deventos.*;
import interfaces.listeners.*;
import Deventos.enlaceJS.*;
import java.awt.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DJToggleButton
	 extends JToggleButton {
  private static final String uiClassID = "DToggleButtonUI";
  private static final long serialVersionUID = 1L;
  
  private Vector<Object> djtogglebuttonlisteners = new Vector<Object>(5);
  private Vector<Object> ljtogglebuttonlisteners = new Vector<Object>(5);
  private Vector<Object> lujtogglebuttonlisteners = new Vector<Object>(5);
  private Integer DID = null;
  private String nombre = null;
  private ColaEventos colaRecepcion = new ColaEventos();
  private ColaEventos colaEnvio = null;
  private Integer ultimoProcesado = new Integer( -1);
  private int nivelPermisos = 10;

  public DJToggleButton() {
	 this.nombre = null;
	 extrasConstructor();
  }

  public DJToggleButton(String p0) {
	 super(p0);
	 this.nombre = null;
	 extrasConstructor();
  }

  public DJToggleButton(String p0, boolean p1) {
	 super(p0, p1);
	 this.nombre = null;
	 extrasConstructor();
  }

  public DJToggleButton(Action p0) {
	 super(p0);
	 this.nombre = null;
	 extrasConstructor();
  }

  public DJToggleButton(Icon p0) {
	 super(p0);
	 this.nombre = null;
	 extrasConstructor();
  }

  public DJToggleButton(Icon p0, boolean p1) {
	 super(p0, p1);
	 this.nombre = null;
	 extrasConstructor();
  }

  public DJToggleButton(String p0, Icon p1) {
	 super(p0, p1);
	 this.nombre = null;
	 extrasConstructor();
  }

  public DJToggleButton(String p0, Icon p1, boolean p2) {
	 super(p0, p1, p2);
	 this.nombre = null;
	 extrasConstructor();
  }

  private void extrasConstructor() {
	 /*addDJToggleButtonListener(new Listener());
	 DID = new Integer(DConector.alta(this));
	 colaEnvio = DConector.getColaEventos();
	 setForeground(Color.GRAY);*/
  }

  public String getUIClassID() {
	 return uiClassID;
  }

  public void addDJToggleButtonListener(DJToggleButtonListener listener) {
	 djtogglebuttonlisteners.add(listener);
  }

  public void addLJToggleButtonListener(LJToggleButtonListener listener) {
	 ljtogglebuttonlisteners.add(listener);
  }
  public void addLUJToggleButtonListener(LJToggleButtonListener listener) {
	 lujtogglebuttonlisteners.add(listener);
  }
  public void removeDJToggleButtonListeners() {
	 djtogglebuttonlisteners.removeAllElements();
  }

  public void removeLJToggleButtonListeners() {
	 ljtogglebuttonlisteners.removeAllElements();
  }
  public void removeLUJToggleButtonListeners() {
	 lujtogglebuttonlisteners.removeAllElements();
  }

  public Vector<Object> getDJToggleButtonListeners() {
	 return djtogglebuttonlisteners;
  }

  public Vector<Object> getLJToggleButtonListeners() {
	 return ljtogglebuttonlisteners;
  }
  public Vector<Object> getLUJToggleButtonListeners() {
	 return lujtogglebuttonlisteners;
  }

  public void activar() {
	 setEnabled(true);
  }

  public void desactivar() {
	 setEnabled(false);
  }

  public void iniciarHebraProcesadora() {
	 Thread t = new Thread(new HebraProcesadora(colaRecepcion, this));
	 t.start();
  }

  public DJToggleButtonEvent obtenerInfoEstado(){
	 DJToggleButtonEvent evento = new DJToggleButtonEvent();
	 evento.presionado = new Boolean(getModel().isPressed());
	 evento.marcado = new Boolean(isSelected());
	 return evento;
  }

  public void procesarEvento(DEvent evento) {
	 if (evento.tipo.intValue() == DJToggleButtonEvent.PRESIONADO.intValue()) {
		if (!getModel().isPressed()) {
		  getModel().setArmed(true);
		  getModel().setPressed(true);
		}
	 }
	 if (evento.tipo.intValue() ==
		  DJToggleButtonEvent.SOLTADO.intValue()) {
		if (getModel().isPressed()) {
		  getModel().setArmed(false);
		  getModel().setPressed(false);
		  setSelected(!isSelected());

		  Vector v = getLJToggleButtonListeners();
		  for (int i = 0; i < v.size(); i++) {
			 ( (LJToggleButtonListener) v.elementAt(i)).cambioEstado(
				  isSelected());
		  }
		  if(evento.usuario.equals(DConector.Dusuario)){
			 v = getLUJToggleButtonListeners();
			 for (int i = 0; i < v.size(); i++) {
				( (LJToggleButtonListener) v.elementAt(i)).cambioEstado(
					 isSelected());
			 }
		  }

		}
	 }
  }

  public void sincronizar() {
	 DJToggleButtonEvent evento = new DJToggleButtonEvent();

	 evento.origen = new Integer(1); // Aplicacion
	 evento.destino = new Integer(0); // Coordinador
	 evento.componente = new Integer(DID.intValue());
	 evento.tipo = new Integer(DJToggleButtonEvent.SINCRONIZACION.intValue());
	 evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());
	 colaEnvio.nuevoEvento(evento);
  }

  public int getNivelPermisos(){
	 return nivelPermisos;
}
  public void setNivelPermisos(int nivel) {
	 nivelPermisos = nivel;
	 if(nivel >= 20){
		setForeground(Color.BLACK);
}
	 else{
		setForeground(Color.GRAY);
}
  }

  public Integer getID() {
	 return DID;
  }

  public String getNombre() {
	 return nombre;
  }
  public ColaEventos obtenerColaRecepcion(){
	 return colaRecepcion;
}
  public ColaEventos obtenerColaEnvio(){
	 return colaEnvio;
}
  public HebraProcesadoraBase crearHebraProcesadora(){
	 return null;
  }

  private class Listener
		implements DJToggleButtonListener {

	 public void presionado(DJToggleButtonEvent evento) {
		enviarEvento(evento, DJToggleButtonEvent.PRESIONADO.intValue());
	 }

	 public void soltado(DJToggleButtonEvent evento) {
		enviarEvento(evento, DJToggleButtonEvent.SOLTADO.intValue());
	 }

	 private void enviarEvento(DJToggleButtonEvent evento, int tipo) {
		if (nivelPermisos >= 20) {
		  try {
			 evento.componente = new Integer(DID.intValue());
			 evento.origen = new Integer(1); // Aplicacion
			 evento.destino = new Integer(0); // Coordinador
			 evento.tipo = new Integer(tipo);
			 evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

			 colaEnvio.nuevoEvento(evento);
		  }
		  catch (Exception e) {
			 e.printStackTrace();
		  }
		}
	 }
  }

  /**
	* Esta hebra se encarga de realizar las operaciones pertinentes con los
	* eventos que se reciben
*/
  class HebraProcesadora
		implements Runnable {

	 ColaEventos cola = null;
	 DJToggleButton togglebutton = null;

	 HebraProcesadora(ColaEventos cola, DJToggleButton togglebutton) {
		this.cola = cola;
		this.togglebutton = togglebutton;
	 }

	 public void run() {
		DJToggleButtonEvent evento = null;
		DJToggleButtonEvent saux = null;
		DJToggleButtonEvent respSincr = null;
		ColaEventos colaAux = new ColaEventos();

		int numEventos = colaRecepcion.tamanio(); // Para evitar quedarnos bloqueados
		int i = 0;

		// Buscamos si se ha recibido una respuesta de sincronizacion
		for (int j = 0; j < numEventos; j++) {
		  saux = (DJToggleButtonEvent) colaRecepcion.extraerEvento();
		  if ( (respSincr == null) &&
				(saux.tipo.intValue() ==
				 DJToggleButtonEvent.RESPUESTA_SINCRONIZACION.intValue())) {
			 respSincr = saux;
		  }
		  else {
			 colaAux.nuevoEvento(saux);
		  }
		}

		setEnabled(true);

		if (respSincr != null) { // Se ha recibido respuesta de sincronizacion
		  ultimoProcesado = new Integer(respSincr.ultimoProcesado.intValue());
		  togglebutton.setSelected(respSincr.marcado.booleanValue());
		  togglebutton.getModel().setPressed(respSincr.presionado.booleanValue());
		  togglebutton.getModel().setArmed(respSincr.presionado.booleanValue());

		}

		// Colocamos en la cola de recepcion los eventos que deben ser
		// procesados (recibidos mientras se realizaba la sincronizacion )
		numEventos = colaAux.tamanio();
		for (int j = 0; j < numEventos; j++) {
		  saux = (DJToggleButtonEvent) colaAux.extraerEvento();
		  if (saux.ultimoProcesado.intValue() > ultimoProcesado.intValue()) {
			 colaRecepcion.nuevoEvento(saux);
		  }
		}
		//System.out.println("Eventos en espera de ejecucion="+cola.tamanio());

		//togglebutton.setEnabled(true);

		while (true) {
		  evento = (DJToggleButtonEvent) cola.extraerEvento();
		  ultimoProcesado = new Integer(evento.contador.intValue());
		  if (nivelPermisos >= 10) {
			 /*System.out.println("HebraProcesadora(" + DID +
									  "): Recibido evento del tipo " + evento.tipo);*/
			 if (evento.tipo.intValue() ==
				  DJToggleButtonEvent.SINCRONIZACION.intValue() &&
				  !evento.usuario.equals(DConector.Dusuario)) {
				DJToggleButtonEvent aux = new DJToggleButtonEvent();
				aux.origen = new Integer(1); // Aplicacion
				aux.destino = new Integer(0); // Coordinador
				aux.componente = new Integer(DID.intValue());
				aux.tipo = new Integer(DJToggleButtonEvent.RESPUESTA_SINCRONIZACION.
											  intValue());
				aux.ultimoProcesado = new Integer(ultimoProcesado.intValue());
				aux.presionado = new Boolean(togglebutton.getModel().isPressed());
				aux.marcado = new Boolean(togglebutton.isSelected());
				colaEnvio.nuevoEvento(aux);
			 }
			 if (evento.tipo.intValue() == DJToggleButtonEvent.PRESIONADO.intValue()) {
				if (!togglebutton.getModel().isPressed()) {
				  togglebutton.getModel().setArmed(true);
				  togglebutton.getModel().setPressed(true);
				}
			 }
			 if (evento.tipo.intValue() ==
				  DJToggleButtonEvent.SOLTADO.intValue()) {
				if (togglebutton.getModel().isPressed()) {
				  togglebutton.getModel().setArmed(false);
				  togglebutton.getModel().setPressed(false);
				  togglebutton.setSelected(!togglebutton.isSelected());

				  Vector<Object> v = getLJToggleButtonListeners();
				  for (i = 0; i < v.size(); i++) {
					 ( (LJToggleButtonListener) v.elementAt(i)).cambioEstado(
						  togglebutton.isSelected());
				  }
				  if(evento.usuario.equals(DConector.Dusuario)){
					 v = getLUJToggleButtonListeners();
					 for (i = 0; i < v.size(); i++) {
						( (LJToggleButtonListener) v.elementAt(i)).cambioEstado(
							 togglebutton.isSelected());
					 }
				  }

				}
			 }
		  }
		}
	 }
  }


}
