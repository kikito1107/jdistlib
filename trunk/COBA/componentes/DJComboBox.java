package componentes;

import javax.swing.*;
import lookandfeel.Dmetal.*;
import java.util.*;
import Deventos.*;
import interfaces.listeners.*;
import componentes.gui.*;
import Deventos.enlaceJS.*;
import java.awt.event.*;
import java.awt.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DJComboBox extends JComboBox {
  private static final String uiClassID = "DComboBoxUI";

  private Vector djcomboboxlisteners = new Vector(5);
  private Vector ljcomboboxlisteners = new Vector(5);
  private Vector lujcomboboxlisteners = new Vector(5);
  private Integer DID = new Integer( -1);
  private String nombre = null;
  private ColaEventos colaRecepcion = new ColaEventos();
  private ColaEventos colaEnvio = null;
  private ListenerMovimientoFrame lmf = null;

  private Integer ultimoProcesado = new Integer(-1);
  private int nivelPermisos = 20;
  DIComboBox padre = null;

  private boolean popupVisible = false;
  private int itemSeleccionado = 0;

    public DJComboBox(DIComboBox padre) {
		super();
		this.padre = padre;
		this.nombre = null;
		extrasConstructor();
	 }

	 public DJComboBox(DIComboBox padre,Object[] p0) {
	super(p0);
	this.padre = padre;
	this.nombre = null;
	extrasConstructor();
	 }

	 public DJComboBox(Vector p0) {
	super(p0);
	this.nombre = null;
	extrasConstructor();
	 }

	 public DJComboBox(ComboBoxModel p0) {
	super(p0);
	this.nombre = null;
	extrasConstructor();
	 }


	 public void setInfoInicial(boolean popupVisible, int itemSeleccionado){
		this.popupVisible = popupVisible;
		this.itemSeleccionado = itemSeleccionado;
}
	 public void ocultarPopup(){
		( (DMetalComboBoxUI) getUI()).mcp.setVisible(false);
	 }

	 public void actualizarEstadoPopup(){
		if(popupVisible && !isPopupVisible()){
		  showPopup();
		  ( (DMetalComboBoxUI) getUI()).setItemLista(itemSeleccionado);
		}
	 }

  public boolean ocultoEnJerarquia(){
boolean oculto = false;
	 if(padre.oculto() || padre.ocultoEnJerarquia()){
		oculto = true;
}
	 return oculto;
}

  private void extrasConstructor(){
	 /*DID = new Integer(DConector.alta(this));
	 colaEnvio = DConector.getColaEventos();*/

}

  public DJComboBoxEvent obtenerInfoEstado(){
	 DJComboBoxEvent evento = new DJComboBoxEvent();
	 evento.abierto = new Boolean(isPopupVisible());
	 evento.itemSeleccionado = new Integer(getSelectedIndex());
	 evento.seleccionLista = new Integer( ( (DMetalComboBoxUI)getUI()).getItemSeleccionado());
	 return evento;
}

  public int obtenerNumComponentesHijos() {
	 return 0;
  }

	 public String getUIClassID() {
		  return uiClassID;
	 }

	 public void activar() {
		setEnabled(true);
	 }

	 public void desactivar() {
		setEnabled(false);
	 }

	 public void iniciarHebraProcesadora(){
		Thread t = new Thread(new HebraProcesadora(colaRecepcion, this));
		t.start();
  //Instalamos el listener que controla si se mueve la ventana
	 }

	 public void procesarEvento(DEvent ev) {
		// ******* SOLUCION PARA MOVER POPUP CUANDO SE MUEVE EL FRAME ******
		if(lmf == null){
		  Container container = padre.getParent();
		 // System.out.print("");
		  while (! (container instanceof JFrame)) {
			 container = container.getParent();
		  }
		  lmf = new ListenerMovimientoFrame();
		  ( (JFrame) container).addComponentListener(lmf);
		}
		// *****************************************************************

		DJComboBoxEvent evento = (DJComboBoxEvent)ev;
		if (evento.tipo.intValue() == DJComboBoxEvent.ABIERTO.intValue()) {
		  if(!ocultoEnJerarquia()){
			 showPopup();
		  }
		  popupVisible = true;
		}
		if (evento.tipo.intValue() == DJComboBoxEvent.CERRADO.intValue()) {
		  ( (DMetalComboBoxUI) getUI()).mcp.setVisible(false);
		  popupVisible = false;
		}
		if (evento.tipo.intValue() == DJComboBoxEvent.SELECCIONADO.intValue()) {
		  setSelectedIndex(evento.itemSeleccionado.intValue());
		  ( (DMetalComboBoxUI) getUI()).mcp.setVisible(false);
		  popupVisible = false;
		  repaint();

		  Vector v = getLJComboBoxListeners();
		  for (int i = 0; i < v.size(); i++) {
			 ( (LJComboBoxListener) v.elementAt(i)).elementoSeleccionado(evento.itemSeleccionado.intValue());
		  }

		  if(evento.usuario.equals(DConector.Dusuario)){
			 v = getLUJComboBoxListeners();
			 for (int i = 0; i < v.size(); i++) {
				( (LJComboBoxListener) v.elementAt(i)).elementoSeleccionado(evento.itemSeleccionado.intValue());
			 }
		  }

		}
		if (evento.tipo.intValue() ==
			 DJComboBoxEvent.CAMBIO_SELECCION_LISTA.intValue()) {
		  if (popupVisible) {
			 ( (DMetalComboBoxUI) getUI()).setItemLista(evento.
				  seleccionLista.intValue());
			 itemSeleccionado = evento.seleccionLista.intValue();
		  }
		}

	 }
	 public void procesarMetaInformacion(DMIEvent evento){

  }

	 public void sincronizar() {
		DJComboBoxEvent evento = new DJComboBoxEvent();

		evento.origen = new Integer(1); // Aplicacion
		evento.destino = new Integer(0); // Coordinador
		evento.componente = new Integer(DID.intValue());
		evento.tipo = new Integer(DJComboBoxEvent.SINCRONIZACION.intValue());
		evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());
		colaEnvio.nuevoEvento(evento);
	 }

	 public int getNivelPermisos(){
		return nivelPermisos;
  }
	 public void setNivelPermisos(int nivel){
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
	 public String getNombre(){
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

	 public void addDJComboBoxListener(DJComboBoxListener listener){
		djcomboboxlisteners.add(listener);
	 }

	 public void addLJComboBoxListener(LJComboBoxListener listener){
		ljcomboboxlisteners.add(listener);
	 }

	 public void addLUJComboBoxListener(LJComboBoxListener listener){
		lujcomboboxlisteners.add(listener);
	 }

  public Vector getDJComboBoxListeners(){
	 return djcomboboxlisteners;
  }
  public Vector getLJComboBoxListeners(){
	 return ljcomboboxlisteners;
  }
  public Vector getLUJComboBoxListeners(){
	 return lujcomboboxlisteners;
  }

  public void removeDJComboBoxListeners(){
	 djcomboboxlisteners.removeAllElements();
  }
  public void removeLJComboBoxListeners(){
	 ljcomboboxlisteners.removeAllElements();
  }
  public void removeLUJComboBoxListeners(){
	 lujcomboboxlisteners.removeAllElements();
  }


  public int getIndiceSeleccionadoLista(){
	 return ((DMetalComboBoxUI)getUI()).getItemSeleccionado();
  }

  /**
	* Seleccion de un elemento por su indice
	* @param i Indice del elemento que queremos seleccionar
	*/
  public void seleccionar(int i){
	 this.setSelectedIndex(i);
  }

  /**
	* Obtener indice del elemento seleccionado
	* @returns Indice del elemento seleccionado
	*/
  public int getIndiceSeleccionado(){
	 return this.getSelectedIndex();
  }

  /**
	* Añadir un elemento a la lista
	* @param cadena Elemento que deseamos añadir
	*/
  public void aniadir(String cadena){
	 this.addItem(cadena);
  }

  /**
	* Añadir un array de elementos a la lista
	* @param cadenas Array de elementos que deseamos añadir
	*/
  public void aniadir(String[] cadenas){
	 for(int i=0; i<cadenas.length; i++)
		this.addItem(cadenas[i]);
  }

  /**
	* Obtener numero de elementos en la lista
	* @returns Numero de elementos en la lista
	*/
  public int getNumeroElementos(){
	 return this.getItemCount();
  }

  /**
	* Obtener elemento seleccionado
	* @returns Elemento seleccionado
	*/
  public String getSeleccionado(){
	 return (String)this.getSelectedItem();
  }

  /**
	* Obtener elemento en el índice elegido
	* @param i Indice del elemento que deseamos obtener
	* @returns Elemento deseado
	*/
  public String getElementoEnIndice(int i){
	 return (String)this.getItemAt(i);
  }

  /**
	* Seleccionar elemento en el indice i
	* @param i Indice del elemento que deseamos seleccionar
	*/
  public void seleccionarIndice(int i){
	 this.setSelectedIndex(i);
  }

  /**
	* Elemento que deseamos seleccionar
	* @param cadena Elemento que deseamos seleccionar
	*/
  public void seleccionar(String cadena){
	 this.setSelectedItem(cadena);
  }

  /**
	* Eliminar todos los elementos
	*
	*/
  public void vaciar(){
	 this.removeAllItems();
  }

  /**
	* Borra el elemento deseado
	* @param cadena Elemento que deseamos borrar
	*/
  public void borrarElemento(String cadena){
	 this.removeItem(cadena);
  }

  /**
	* Eliminar elemento en el índice deseado
	* @param i Indice del elemento que deseamos eliminar
	*/
  public void borrarElementoEnIndice(int i){
	 this.removeItemAt(i);
  }


	 private class ListenerMovimientoFrame
		  extends java.awt.event.ComponentAdapter {

		public void componentMoved(ComponentEvent e) {
		  if(isPopupVisible()){
			 synchronized(this){
				DMetalComboBoxUI ui = (DMetalComboBoxUI)getUI();
				  int index = ui.getItemSeleccionado();
				  showPopup();
				  if (index >= 0) {
				ui.setItemLista(index);
			 }
			 }
		  }
		}
	 }

  private class Listener implements DJComboBoxListener{

	 public void abierto(){
		if(nivelPermisos >= 20){
		  DJComboBoxEvent evento = new DJComboBoxEvent();
		  evento.componente = new Integer(DID.intValue());
		  evento.origen = new Integer(1); // Aplicacion
		  evento.destino = new Integer(0); // Coordinador
		  evento.componente = new Integer(DID.intValue());
		  evento.tipo = new Integer(DJComboBoxEvent.ABIERTO.intValue());
		  evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

		  colaEnvio.nuevoEvento(evento);
		}
}
	 public void cerrado(){
		if(nivelPermisos >= 20){
		  DJComboBoxEvent evento = new DJComboBoxEvent();
		  evento.componente = new Integer(DID.intValue());
		  evento.origen = new Integer(1); // Aplicacion
		  evento.destino = new Integer(0); // Coordinador
		  evento.componente = new Integer(DID.intValue());
		  evento.tipo = new Integer(DJComboBoxEvent.CERRADO.intValue());
		  evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

		  colaEnvio.nuevoEvento(evento);
		}
}
	 public void cambioSeleccionLista(DJComboBoxEvent evento){
		if(nivelPermisos >= 20){
		  evento.componente = new Integer(DID.intValue());
		  evento.origen = new Integer(1); // Aplicacion
		  evento.destino = new Integer(0); // Coordinador
		  evento.componente = new Integer(DID.intValue());
		  evento.tipo = new Integer(DJComboBoxEvent.CAMBIO_SELECCION_LISTA.
											 intValue());
		  evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

		  colaEnvio.nuevoEvento(evento);
		}
	 }

	 public void seleccion(DJComboBoxEvent evento){
		if(nivelPermisos >= 20){
		  evento.componente = new Integer(DID.intValue());
		  evento.origen = new Integer(1); // Aplicacion
		  evento.destino = new Integer(0); // Coordinador
		  evento.componente = new Integer(DID.intValue());
		  evento.tipo = new Integer(DJComboBoxEvent.SELECCIONADO.intValue());
		  evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

		  colaEnvio.nuevoEvento(evento);
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
	  DJComboBox combobox = null;

	  HebraProcesadora(ColaEventos cola, DJComboBox combobox) {
		 this.cola = cola;
		 this.combobox = combobox;
	  }

	  public void run() {
		 DJComboBoxEvent evento = null;
		 DJComboBoxEvent saux = null;
		 DJComboBoxEvent respSincr = null;
		 ColaEventos colaAux = new ColaEventos();

		 int numEventos = colaRecepcion.tamanio(); // Para evitar quedarnos bloqueados
		 int i=0;
		 int posicion=-1;
		 boolean encontradaRespuestaSincronizacion = false;

		 // Buscamos si se ha recibido una respuesta de sincronizacion
		 for(int j=0; j<numEventos; j++){
			saux = (DJComboBoxEvent)colaRecepcion.extraerEvento();
			if((respSincr == null) && (saux.tipo.intValue() == DJComboBoxEvent.RESPUESTA_SINCRONIZACION.intValue())){
			  respSincr = saux;
			}else{
			  colaAux.nuevoEvento(saux);
			}
		 }

		 //addDJComboBoxListener(new Listener());
		 activar();

		 if(respSincr != null){// Se ha recibido respuesta de sincronizacion
			ultimoProcesado = new Integer(respSincr.ultimoProcesado.intValue());
			combobox.setSelectedIndex(respSincr.itemSeleccionado.intValue());
			if(respSincr.abierto.booleanValue()){
			  combobox.showPopup();
			  ((DMetalComboBoxUI)combobox.getUI()).setItemLista(respSincr.seleccionLista.intValue());
			}
		 }

		 // Colocamos en la cola de recepcion los eventos que deben ser
		 // procesados (recibidos mientras se realizaba la sincronizacion )
		 numEventos = colaAux.tamanio();
		 for(int j=0; j<numEventos; j++){
			saux = (DJComboBoxEvent)colaAux.extraerEvento();
			if(saux.ultimoProcesado.intValue() > ultimoProcesado.intValue()){
			  colaRecepcion.nuevoEvento(saux);
			}
		 }

		 // Comenzamos a escuchar eventos del usuario

		 while (true) {
			evento = (DJComboBoxEvent) cola.extraerEvento();
			ultimoProcesado = new Integer(evento.contador.intValue());
			if(nivelPermisos >= 10){
			  /*System.out.println("HebraProcesadora(" + DID +
										"): Recibido evento del tipo " + evento.tipo);*/
			  // Indicamos cual ha sido el último evento procesado
			  ultimoProcesado = new Integer(evento.contador.intValue());

			  if (evento.tipo.intValue() == DJComboBoxEvent.SINCRONIZACION.intValue() &&
					!evento.usuario.equals(DConector.Dusuario)) {
				 DJComboBoxEvent aux = new DJComboBoxEvent();
				 aux.origen = new Integer(1); // Aplicacion
				 aux.destino = new Integer(0); // Coordinador
				 aux.componente = new Integer(DID.intValue());
				 aux.tipo = new Integer(DJComboBoxEvent.RESPUESTA_SINCRONIZACION.
												intValue());
				 aux.ultimoProcesado = new Integer(ultimoProcesado.intValue());
				 aux.abierto = new Boolean(combobox.isPopupVisible());
				 aux.itemSeleccionado = new Integer(combobox.getSelectedIndex());
				 aux.seleccionLista = new Integer( ( (DMetalComboBoxUI) combobox.
																getUI()).getItemSeleccionado());
				 colaEnvio.nuevoEvento(aux);
			  }
			  if (evento.tipo.intValue() == DJComboBoxEvent.ABIERTO.intValue()) {
				 combobox.showPopup();
			  }
			  if (evento.tipo.intValue() == DJComboBoxEvent.CERRADO.intValue()) {
				 ( (DMetalComboBoxUI) combobox.getUI()).mcp.setVisible(false);
			  }
			  if (evento.tipo.intValue() == DJComboBoxEvent.SELECCIONADO.intValue()) {
				 combobox.setSelectedIndex(evento.itemSeleccionado.intValue());
				 ( (DMetalComboBoxUI) combobox.getUI()).mcp.setVisible(false);
				 combobox.repaint();
			  }
			  if (evento.tipo.intValue() ==
					DJComboBoxEvent.CAMBIO_SELECCION_LISTA.intValue()) {
				 if(combobox.isPopupVisible()){
					( (DMetalComboBoxUI) combobox.getUI()).setItemLista(evento.
						 seleccionLista.intValue());
				 }
			  }
			}
		 }
	  }

	}

}
