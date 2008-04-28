package componentes.gui;

import java.util.*;

import java.awt.*;
import javax.swing.*;

import Deventos.*;
import Deventos.enlaceJS.*;
import componentes.*;
import componentes.base.DComponente;
import componentes.base.DComponenteBase;
import componentes.base.DJCheckBox;
import componentes.base.HebraProcesadoraBase;
import componentes.listeners.*;

/**
 * Checkbox compartido. Consultar documentacion del proyecto para ver su
 * funcionamiento
 */

public class DICheckBox
	 extends DComponenteBase {

	
  private static final long serialVersionUID = 1L;
	
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  DJCheckBox checkbox = null;

  /**
	* @param nombre String Nombre del componente.
	* @param conexionDC boolean TRUE si esta en contacto directo con el DConector
	* (no es hijo de ningun otro componente) y FALSE en otro caso
	* @param padre DComponenteBase Componente padre de este componente. Si no
	* tiene padre establecer a null
	*/
  public DICheckBox(String nombre, boolean conexionDC, DComponenteBase padre) {
	 super(nombre, conexionDC, padre);
	 this.checkbox = new DJCheckBox("CheckBox");

	 try {
		jbInit();
	 }
	 catch (Exception e) {
		e.printStackTrace();
	 }
  }

  /**
	* @param textoCheckBox String Cadena de texto que presentara la checkbox.
	* @param nombre String Nombre del componente.
	* @param conexionDC boolean TRUE si esta en contacto directo con el DConector
	* (no es hijo de ningun otro componente) y FALSE en otro caso
	* @param padre DComponenteBase Componente padre de este componente. Si no
	* tiene padre establecer a null
	*/
  public DICheckBox(String textoCheckBox, String nombre, boolean conexionDC,
						  DComponenteBase padre) {
	 super(nombre, conexionDC, padre);
	 this.checkbox = new DJCheckBox(textoCheckBox);

	 try {
		jbInit();
	 }
	 catch (Exception e) {
		e.printStackTrace();
	 }
  }

  private void jbInit() throws Exception {
	 this.setLayout(new FlowLayout());
	 this.add(checkbox, null);
	 checkbox.addDJCheckBoxListener(crearDJListener());
	 //desactivar();//*******************************************************************************
  }

  /**
	* Devuelve la instancia de la clase captadora que está usando.
	* @return DJCheckBox Clase captadora
	*/
  public DJCheckBox obtenerJComponente() {
	 return checkbox;
  }

  /**
	* Establece el nivel de permisos de este componente. No se recomienda
	* llamar a este método desde el programa. Será llamado de forma automática
	* cuando sea necesario
	* @param nivel int Nivel que queremos establecer
	*/
  public void setNivelPermisos(int nivel) {
	 super.setNivelPermisos(nivel);
	 if (nivel < 20) {
		checkbox.setForeground(Color.GRAY);
	 }
	 else {
		checkbox.setForeground(Color.BLACK);
	 }
  }

  public DJCheckBoxEvent obtenerInfoEstado() {
	 return checkbox.obtenerInfoEstado();
  }

  /**
	* Obtiene el numero de componentes hijos de este componente. SIEMPRE devuelve 0
	* @return int Número de componentes hijos. SIEMPRE devuelve 0.
	*/
  public int obtenerNumComponentesHijos() {
	 return 0;
  }

  /**
	* Añade un DJListener a la clase captadora para recibir los eventos generados
	* por la interaccion del usuario con el componente
	* @param listener DJCheckBoxListener Listener a añadir
	*/
  public void addDJCheckBoxListener(DJCheckBoxListener listener) {
	 checkbox.addDJCheckBoxListener(listener);
  }

  /**
	* Añade un LListener al componente para ser notificado cuando cambie el
	* estado del componente
	* @param listener LJCheckBoxListener Listener a añadir
	*/
  public void addLJCheckBoxListener(LJCheckBoxListener listener) {
	 checkbox.addLJCheckBoxListener(listener);
  }

  /**
	* Añade un LUListener al componente para ser notificado cuando cambie el
	* estado del componente. Solo sera notificado cuando el cambio de estado
	* se deba a una accion realizada por el usuario de la aplicacion.
	* @param listener LJButtonListener Listener a añadir
	*/
  public void addLUJCheckBoxListener(LJCheckBoxListener listener) {
	 checkbox.addLUJCheckBoxListener(listener);
  }

  /**
	* Obtiene los DJListener que tiene asociado el componente
	* @return Vector Vector de listeners DJCheckBoxListener
	*/
  public Vector getDJCheckBoxListeners() {
	 return checkbox.getDJCheckBoxListeners();
  }

  /**
	* Obtiene los LJListener que tiene asociado el componente
	* @return Vector Vector de listeners LJCheckBoxListener
	*/
  public Vector getLJCheckBoxListeners() {
	 return checkbox.getLJCheckBoxListeners();
  }

  /**
	* Obtiene los LUJListener que tiene asociado el componente
	* @return Vector Vector de listeners LJCheckBoxListener
	*/
  public Vector getLUJCheckBoxListeners() {
	 return checkbox.getLUJCheckBoxListeners();
  }

  /**
	* Elimina todos los DJListeners que tiene asociado el componente
	*/
  public void removeDJCheckBoxListeners() {
	 checkbox.removeDJCheckBoxListeners();
  }

  /**
	* Elimina todos los LJListeners que tiene asociado el componente
	*/
  public void removeLJCheckBoxListeners() {
	 checkbox.removeLJCheckBoxListeners();
  }

  /**
	* Elimina todos los LUJListeners que tiene asociado el componente
	*/
  public void removeLUJCheckBoxListeners() {
	 checkbox.removeLUJCheckBoxListeners();
  }

  /**
	* Activa el componente. No se recomienda llamar a este metodo desde el
	* programa. Sera llamado de forma automatica cuando sea necesario
	*/
  public void activar() {
	 checkbox.activar();
  }

  /**
	* Desctiva el componente. No se recomienda llamar a este metodo desde el
	* programa. Sera llamado de forma automatica cuando sea necesario
	*/
  public void desactivar() {
	 checkbox.desactivar();
  }

  /**
	* Mediante una llamada a este método se envía un mensaje de peticion de
	* sincronizacion. No se debe llamar a este método de forma directa. Será
	* llamado de forma automatica cuando sea necesario realizar la sincronizacion
	*/
  public void sincronizar() {
	 if (conectadoDC()) {
		DJCheckBoxEvent peticion = new DJCheckBoxEvent();
		peticion.tipo = new Integer(DJCheckBoxEvent.SINCRONIZACION.intValue());
		enviarEvento(peticion);
	 }
  }

  /**
	* Devuelve una instancia de un listener que se encargara de tratar los
	* eventos que se reciben desde la clase captadora. Normalmente este tratamiento
	* se reduce a enviar el evento.
	* @return DJCheckBoxListener Listener creado
	*/
  public DJCheckBoxListener crearDJListener() {
	 return new Listener();
  }

  /**
	* Devuelve una nueva instancia de la hebra que se encargara de procesar
	* los eventos que se reciban. Este metodo no debe llamarse de forma directa.
	* Sera llamado de forma automatica cuando sea necesario.
	* @return HebraProcesadoraBase Nueva hebra procesadora
	*/
  public HebraProcesadoraBase crearHebraProcesadora() {
	 return new HebraProcesadora(this);
  }

  /**
	* Listener encargado de gestionar los eventos procedentes de la clase
	* captadora
	*/
  private class Listener
		implements DJCheckBoxListener {

	 public void presionado(DJCheckBoxEvent evento) {
		enviarEvento(evento);
	 }

	 public void soltado(DJCheckBoxEvent evento) {
		enviarEvento(evento);
	 }

  }

  /**
	* Hebra procesadora de eventos. Se encarga de realizar las acciones que
	* correspondan cuando recibe un evento. Tambén se encarga en su inicio
	* de sincronizar el componente.
	*/
  class HebraProcesadora
		extends HebraProcesadoraBase {

	 HebraProcesadora(DComponente dc) {
		super(dc);
	 }

	 public void run() {
		DJCheckBoxEvent evento = null;
		DJCheckBoxEvent saux = null;
		DJCheckBoxEvent respSincr = null;
		Vector vaux = new Vector();

		DEvent[] eventos = obtenerEventosColaRecepcion();
		int numEventos = eventos.length;
		int i = 0;

		// Buscamos si se ha recibido una respuesta de sincronizacion
		for (int j = 0; j < numEventos; j++) {
		  saux = (DJCheckBoxEvent) eventos[j];
		  if ( (respSincr == null) &&
				(saux.tipo.intValue() ==
				 DJCheckBoxEvent.RESPUESTA_SINCRONIZACION.intValue())) {
			 respSincr = saux;
		  }
		  else {
			 vaux.add(saux);
		  }
		}

		activar();

		if (respSincr != null) { // Se ha recibido respuesta de sincronizacion
		  ultimoProcesado = new Integer(respSincr.ultimoProcesado.intValue());
		  checkbox.setSelected(respSincr.marcada.booleanValue());
		  checkbox.getModel().setPressed(respSincr.presionada.booleanValue());
		  checkbox.getModel().setArmed(respSincr.presionada.booleanValue());
		}

		// Colocamos en la cola de recepcion los eventos que deben ser
		// procesados (recibidos mientras se realizaba la sincronizacion )
		numEventos = vaux.size();
		for (int j = 0; j < numEventos; j++) {
		  saux = (DJCheckBoxEvent) vaux.elementAt(j);
		  if (saux.ultimoProcesado.intValue() > ultimoProcesado.intValue()) {
			 checkbox.procesarEvento(saux);
		  }
		}

		while (true) {
		  evento = (DJCheckBoxEvent) leerSiguienteEvento();
		  ultimoProcesado = new Integer(evento.contador.intValue());
		  if (evento.tipo.intValue() == DJCheckBoxEvent.SINCRONIZACION.intValue() &&
				!evento.usuario.equals(DConector.Dusuario)) {
			 DJCheckBoxEvent infoEstado = obtenerInfoEstado();
			 infoEstado.tipo = new Integer(DJCheckBoxEvent.
													 RESPUESTA_SINCRONIZACION.intValue());
			 enviarEvento(infoEstado);
		  }
		  else {
			 checkbox.procesarEvento(evento);
		  }
		}

	 }
  }

}
