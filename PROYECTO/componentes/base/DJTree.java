package componentes.base;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import lookandfeel.Dmetal.DMetalTreeUI;
import Deventos.ColaEventos;
import Deventos.DEvent;
import Deventos.DJTreeEvent;
import Deventos.enlaceJS.DConector;

import componentes.listeners.DJTreeListener;
import componentes.listeners.LJTreeListener;

public class DJTree
	 extends JTree {
  private static final String uiClassID = "DTreeUI";

  private Vector<Object> djtreelisteners = new Vector<Object>(5);
  private Vector<Object> ljtreelisteners = new Vector<Object>(5);
  private Vector<Object> lujtreelisteners = new Vector<Object>(5);
  private Integer DID = new Integer(-1);
  private String nombre = null;
  private ColaEventos colaRecepcion = new ColaEventos();
  private ColaEventos colaEnvio = null;

  private Integer ultimoProcesado = new Integer( -1);
  private int nivelPermisos = 10;

  public DJTree() {
	 this.nombre = null;
	 extrasConstructor();
  }

  public DJTree(Object[] p0) {
	 super(p0);
	 this.nombre = null;
	 extrasConstructor();
  }

  public DJTree(Hashtable<?,?> p0) {
	 super(p0);
	 this.nombre = null;
	 extrasConstructor();
  }

  public DJTree(Vector<?> p0) {
	 super(p0);
	 this.nombre = null;
	 extrasConstructor();
  }

  public DJTree(TreeModel p0) {
	 super(p0);
	 this.nombre = null;
	 extrasConstructor();
  }

  public DJTree(TreeNode p0) {
	 super(p0);
	 this.nombre = null;
	 extrasConstructor();
  }

  public DJTree(TreeNode p0, boolean p1) {
	 super(p0, p1);
	 this.nombre = null;
	 extrasConstructor();
  }

  private void extrasConstructor() {
	 getSelectionModel().setSelectionMode(TreeSelectionModel.
													  SINGLE_TREE_SELECTION);
	 /*addDJTreeListener(new Listener());
	 DID = new Integer(DConector.alta(this));
	 colaEnvio = DConector.getColaEventos();*/
  }

  public String getUIClassID() {
	 return uiClassID;
  }

  public void addDJTreeListener(DJTreeListener listener) {
	 djtreelisteners.add(listener);
  }
  public void addLJTreeListener(LJTreeListener listener) {
	 ljtreelisteners.add(listener);
  }
  public void addLUJTreeListener(LJTreeListener listener) {
	 lujtreelisteners.add(listener);
  }

  public void removeDJTreeListeners() {
	 djtreelisteners.removeAllElements();
  }
  public void removeLJTreeListeners() {
	 ljtreelisteners.removeAllElements();
  }
  public void removeLUJTreeListeners() {
	 lujtreelisteners.removeAllElements();
  }

  public Vector getDJTreeListeners() {
	 return djtreelisteners;
  }
  public Vector getLJTreeListeners() {
	 return ljtreelisteners;
  }
  public Vector getLUJTreeListeners() {
	 return lujtreelisteners;
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

  public DJTreeEvent obtenerInfoEstado(){
	 DJTreeEvent evento = new DJTreeEvent();
	 evento.estado = getEstado();
	 return evento;
  }

  public void procesarEvento(DEvent ev) {
	 DJTreeEvent evento = (DJTreeEvent)ev;
	 if (evento.tipo.intValue() == DJTreeEvent.APERTURA_CIERRE.intValue()) {
		( (DMetalTreeUI) getUI()).procesarEvento(evento);
	 }
	 if (evento.tipo.intValue() == DJTreeEvent.SELECCION.intValue()) {
		( (DMetalTreeUI) getUI()).procesarEvento(evento);

		Vector v = getLJTreeListeners();
		for (int i = 0; i < v.size(); i++) {
		  ( (LJTreeListener) v.elementAt(i)).cambioSeleccion();
		}
		if(evento.usuario.equals(DConector.Dusuario)){
		  v = getLUJTreeListeners();
		  for (int i = 0; i < v.size(); i++) {
			 ( (LJTreeListener) v.elementAt(i)).cambioSeleccion();
		  }
		}

	 }
  }

  public void sincronizar() {
	 DJTreeEvent evento = new DJTreeEvent();
		 evento.origen = new Integer(1); // Aplicacion
		 evento.destino = new Integer(0); // Coordinador
		 evento.componente = new Integer(DID.intValue());
		 evento.tipo = new Integer(DJTreeEvent.SINCRONIZACION.intValue());
		 evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

		 colaEnvio.nuevoEvento(evento);
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

  public Vector getEstado(){
	 return ((DMetalTreeUI)getUI()).getEstado();
  }

  public int getNivelPermisos(){
	 return nivelPermisos;
}
  public void setNivelPermisos(int nivel) {
	 nivelPermisos = nivel;
  }

  private class Listener
		implements DJTreeListener {

	 public void apertura_cierre(DJTreeEvent evento) {
		if (nivelPermisos >= 20) {
		  evento.componente = new Integer(DID.intValue());
		  evento.origen = new Integer(1); // Aplicacion
		  evento.destino = new Integer(0); // Coordinador
		  evento.componente = new Integer(DID.intValue());
		  evento.tipo = new Integer(DJTreeEvent.APERTURA_CIERRE.intValue());
		  evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

		  colaEnvio.nuevoEvento(evento);
		}
	 }

	 public void seleccion(DJTreeEvent evento) {
		if (nivelPermisos >= 20) {
		  evento.componente = new Integer(DID.intValue());
		  evento.origen = new Integer(1); // Aplicacion
		  evento.destino = new Integer(0); // Coordinador
		  evento.componente = new Integer(DID.intValue());
		  evento.tipo = new Integer(DJTreeEvent.SELECCION.intValue());
		  evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

		  for (int i = 0; i < evento.path.size(); i++) {
			 //System.out.print(evento.path.elementAt(i) + ",");
		  }
		  //System.out.println("");
		  colaEnvio.nuevoEvento(evento);
		}
	 }
  }

  class HebraProcesadora
		implements Runnable {

	 ColaEventos cola = null;
	 DJTree arbol = null;

	 HebraProcesadora(ColaEventos cola, DJTree arbol) {
		this.cola = cola;
		this.arbol = arbol;
	 }

  private void setEstado(Vector v){
	 ((DMetalTreeUI)getUI()).setEstado(v);
  }

	 public void run() {
		DJTreeEvent evento = null;
		DJTreeEvent saux = null;
		DJTreeEvent respSincr = null;
		ColaEventos colaAux = new ColaEventos();

		int numEventos = colaRecepcion.tamanio(); // Para evitar quedarnos bloqueados

		// Buscamos si se ha recibido una respuesta de sincronizacion
		for (int j = 0; j < numEventos; j++) {
		  saux = (DJTreeEvent) colaRecepcion.extraerEvento();
		  if ( (respSincr == null) &&
				(saux.tipo.intValue() ==
				 DJTreeEvent.RESPUESTA_SINCRONIZACION.intValue())) {
			  System.out.print("REcibida sincronizzacion");
			  System.out.flush();
			 respSincr = saux;
		  }
		  else {
			 colaAux.nuevoEvento(saux);
		  }
		}

		activar();

		if (respSincr != null) { // Se ha recibido respuesta de sincronizacion
		  ultimoProcesado = new Integer(respSincr.ultimoProcesado.intValue());
		  setEstado(respSincr.estado);
		  /*System.out.println("HebraProcesadora(" + DID +
									"): Sincronizacion realizada");*/
		}
		else {
		  /*System.out.println("HebraProcesadora(" + DID +
									"): Iniciado sin sincronizacion");*/
		}

		// Colocamos en la cola de recepcion los eventos que deben ser
		// procesados (recibidos mientras se realizaba la sincronizacion )
		numEventos = colaAux.tamanio();
		for (int j = 0; j < numEventos; j++) {
		  saux = (DJTreeEvent) colaAux.extraerEvento();
		  if (saux.ultimoProcesado.intValue() > ultimoProcesado.intValue()) {
			 colaRecepcion.nuevoEvento(saux);
		  }
		}

		//arbol.setEnabled(true);

		while (true) {
		  evento = (DJTreeEvent) cola.extraerEvento();
		  ultimoProcesado = new Integer(evento.contador.intValue());
		  if (nivelPermisos >= 10) {
			 /*System.out.println("HebraProcesadora(" + DID +
									  "): Procesado: Tipo=" + evento.tipo+" Ult. Proc.="+evento.ultimoProcesado);*/
			 if (evento.tipo.intValue() == DJTreeEvent.SINCRONIZACION.intValue() &&
				  !evento.usuario.equals(DConector.Dusuario)) {
				DJTreeEvent aux = new DJTreeEvent();
				aux.origen = new Integer(1); // Aplicacion
				aux.destino = new Integer(0); // Coordinador
				aux.componente = new Integer(DID.intValue());
				aux.tipo = new Integer(DJTreeEvent.RESPUESTA_SINCRONIZACION.
											  intValue());
				aux.ultimoProcesado = new Integer(ultimoProcesado.intValue());
				aux.estado = getEstado();

				colaEnvio.nuevoEvento(aux);
			 }
			 if (evento.tipo.intValue() == DJTreeEvent.APERTURA_CIERRE.intValue()) {
				( (DMetalTreeUI) getUI()).procesarEvento(evento);
			 }
			 if (evento.tipo.intValue() == DJTreeEvent.SELECCION.intValue()) {
				( (DMetalTreeUI) getUI()).procesarEvento(evento);
			 }
		  }
		}
	 }
  }
}
