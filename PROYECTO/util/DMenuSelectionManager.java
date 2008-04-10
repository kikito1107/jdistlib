package util;

import javax.swing.MenuSelectionManager;
import javax.swing.*;
import interfaces.listeners.*;
import interfaces.*;
import Deventos.*;
import java.util.*;
import Deventos.enlaceJS.*;
import java.awt.*;
import componentes.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DMenuSelectionManager extends MenuSelectionManager {
  private static final DMenuSelectionManager dinstance =
		new DMenuSelectionManager();
  private static final boolean TRACE =   true; // trace creates and disposes


  private boolean inicializado = false;
  private int contador=0;
  private Vector dmenuselectionmanagerlisteners = new Vector(5);
  private Integer DID = new Integer(-1);
  private String nombre = null;
  private ColaEventos colaRecepcion = new ColaEventos();
  private ColaEventos colaEnvio = null;

  private boolean sincronizado = false;
  private int estadoSincronizacion = 0;
  private Integer ultimoProcesado = new Integer( -1);
  private int nivelPermisos = 20;
  private JMenuBar barraMenu = null;
  private int setNivelPermisos = 20;

  public void inicializar(JMenuBar barraMenu){
	 if(!inicializado){
		addDMenuSelectionManagerListener(new Listener());
		//DID = new Integer(DConector.alta(this));
		colaEnvio = DConector.getColaEventos();
		this.barraMenu = barraMenu;
		inicializado = true;
}
}

  public JMenuBar getBarraMenu(){
	 return barraMenu;
}

  public boolean oculto(){
	 return false;
  }

  public void padreOcultado() {
  }
  public void padreMostrado(){
}
  public DComponente obtenerPadre(){
	 return null;
}

  public int obtenerNumComponentesHijos() {
	 return 0;
  }
  public void addDMenuSelectionManagerListener(DMenuSelectionManagerListener listener){
	 dmenuselectionmanagerlisteners.add(listener);
  }

  public Vector getDMenuSelectionManagerListeners(){
	 return dmenuselectionmanagerlisteners;
  }

  public static MenuSelectionManager defaultManager() {
		return dinstance;
  }

  public void setSelectedPath(MenuElement[] path) {
	 /*DMenuSelectionManagerEvent evento = new DMenuSelectionManagerEvent();
	 evento.path = pathToVector(path);
	 Vector v = getDMenuSelectionManagerListeners();
	 for(int i=0; i<v.size(); i++){
		((DMenuSelectionManagerListener)v.elementAt(i)).cambioPath(evento);
	 }*/
	 super.setSelectedPath(path);
	 //System.out.println("["+contador++ +"S]DMenuSelecionManager: setSelectedPath()");
  }

  private void superSetSelectedPath(MenuElement[] path){
	 super.setSelectedPath(path);
}
  public MenuElement[] getSelectedPath() {
	 //System.out.println("["+contador++ +"G]DMenuSelecionManager: getSelectedPath()");
	 return super.getSelectedPath();
  }

  public void activar() {
	 //
  }

  public void desactivar() {
	 //
  }

  public void iniciarHebraProcesadora(){
	 Thread t = new Thread(new HebraProcesadora(colaRecepcion, this));
	 t.start();
	 //System.out.println("DMenuSelectionManager("+DID+"): Iniciada hebra procesadora");
  }

  public void procesarEvento(DEvent evento) {
	 colaRecepcion.nuevoEvento(evento);
  }

  public void procesarMetaInformacion(DMIEvent evento){

}

  public void sincronizar() {
	 DMenuSelectionManagerEvent evento = new DMenuSelectionManagerEvent();

	 evento.origen = new Integer(1); // Aplicacion
	 evento.destino = new Integer(0); // Coordinador
	 evento.componente = new Integer(DID.intValue());
	 evento.tipo = new Integer(DMenuSelectionManagerEvent.SINCRONIZACION.intValue());
	 evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());
	 colaEnvio.nuevoEvento(evento);
  }

  public void setNivelPermisos(int nivel){
	 nivelPermisos = nivel;
}
  public int getNivelPermisos(){
	 return nivelPermisos;
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

  private Vector pathToVector(MenuElement[] path) {
	 Vector v = new Vector();
	 int i, j, n;
	 v.add(new Integer(0));
	 for (i = 1; i < path.length; i++) {
		if (path[i] instanceof JPopupMenu) {
		  v.add(new Integer(-5));
		}
		else {
		  MenuElement[] me = path[i - 1].getSubElements();
		  n = -1;
		  for (j = 0; j < me.length; j++) {
			 if (me[j].equals(path[i])) {
				n = j;
			 }
		  }
		  v.add(new Integer(n));
		}
	 }
	 return v;
  }

  private MenuElement[] vectorToPath(Vector v) {
	 MenuElement[] me = new MenuElement[v.size()];
	 MenuElement elementoActual = null;
	 int i, j;
	 if (me.length > 0) {
		me[0] = barraMenu;
		elementoActual = barraMenu;
		for (i = 1; i < v.size(); i++) {
		  if ( ( (Integer) v.elementAt(i)).intValue() == -5) { // es un popup
			 me[i] = ( (JMenu) elementoActual).getPopupMenu();
			 elementoActual = me[i];
		  }
		  else {
			 int indice = ( (Integer) v.elementAt(i)).intValue();
			 me[i] = (MenuElement) ( (Container) elementoActual).getComponent(
				indice);
			 elementoActual = me[i];
		  }
		}
	 }
	 return me;
  }

  public void prueba(){
	 MenuElement[] me = dinstance.getSelectedPath();
	 dinstance.clearSelectedPath();
	 Vector v = pathToVector(me);
	 me = vectorToPath(v);
	 dinstance.setSelectedPath(me);
}

//*****************************************************************************
  private class Listener implements DMenuSelectionManagerListener{
	 public void cambioPath(DMenuSelectionManagerEvent evento){
		evento.componente = new Integer(DID.intValue());
		evento.origen = new Integer(1); // Aplicacion
		evento.destino = new Integer(0); // Coordinador
		evento.componente = new Integer(DID.intValue());
		evento.tipo = new Integer(DMenuSelectionManagerEvent.CAMBIO_PATH.intValue());
		evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

		colaEnvio.nuevoEvento(evento);

}
}

  class HebraProcesadora
	 implements Runnable {

  ColaEventos cola = null;
  DMenuSelectionManager manager = null;

  HebraProcesadora(ColaEventos cola, DMenuSelectionManager manager) {
	 this.cola = cola;
	 this.manager = manager;
  }

  public void run() {
	 DMenuSelectionManagerEvent evento = null;
	 DMenuSelectionManagerEvent saux = null;
	 DMenuSelectionManagerEvent respSincr = null;
	 ColaEventos colaAux = new ColaEventos();

	 int numEventos = colaRecepcion.tamanio(); // Para evitar quedarnos bloqueados
	 //int i=0;
	 //int posicion=-1;
	 //boolean encontradaRespuestaSincronizacion = false;


	 if(respSincr != null){// Se ha recibido respuesta de sincronizacion
		ultimoProcesado = new Integer(respSincr.ultimoProcesado.intValue());
		superSetSelectedPath(vectorToPath(respSincr.path));
		//System.out.println("HebraProcesadora("+DID+"): Sincronizacion realizada");
	 }else{
		//System.out.println("HebraProcesadora("+DID+"): Iniciado sin sincronizacion");
	 }

	 // Colocamos en la cola de recepcion los eventos que deben ser
	 // procesados (recibidos mientras se realizaba la sincronizacion )
	 numEventos = colaAux.tamanio();
	 for(int j=0; j<numEventos; j++){
		saux = (DMenuSelectionManagerEvent)colaAux.extraerEvento();
		if(saux.ultimoProcesado.intValue() > ultimoProcesado.intValue()){
		  colaRecepcion.nuevoEvento(saux);
		}
	 }


	 while (true) {
		evento = (DMenuSelectionManagerEvent) cola.extraerEvento();
		ultimoProcesado = new Integer(evento.contador.intValue());
		//System.out.println("HebraProcesadora("+DID+"): Recibido evento del tipo "+evento.tipo);
		if (evento.tipo.intValue() == DMenuSelectionManagerEvent.SINCRONIZACION.intValue() &&
			 !evento.usuario.equals(DConector.Dusuario)) {
		  DMenuSelectionManagerEvent aux = new DMenuSelectionManagerEvent();
		  aux.origen = new Integer(1); // Aplicacion
		  aux.destino = new Integer(0); // Coordinador
		  aux.componente = new Integer(DID.intValue());
		  aux.tipo = new Integer(DJListEvent.RESPUESTA_SINCRONIZACION.intValue());
		  aux.ultimoProcesado = new Integer(ultimoProcesado.intValue());
		  aux.path = pathToVector(dinstance.getSelectedPath());
		  colaEnvio.nuevoEvento(aux);
		}
		if (evento.tipo.intValue() == DMenuSelectionManagerEvent.CAMBIO_PATH.intValue()) {
		  MenuElement[] me = vectorToPath(evento.path);
		  superSetSelectedPath(me);
		}
	 }
  }

}

}
