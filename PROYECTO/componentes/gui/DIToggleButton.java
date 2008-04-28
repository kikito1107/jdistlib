package componentes.gui;

import java.util.*;

import java.awt.*;
import javax.swing.*;

import Deventos.*;
import Deventos.enlaceJS.*;
import componentes.*;
import componentes.listeners.*;

/**
 * Toggle button compartido. Consultar documentación del proyecto para ver
 * su funcionamiento
 */

public class DIToggleButton
	 extends DComponenteBase {
	
  private static final long serialVersionUID = 1L;
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  DJToggleButton boton = null;

  /**
	* @param nombre String Nombre del componente.
	* @param conexionDC boolean TRUE si esta en contacto directo con el DConector
	* (no es hijo de ningun otro componente) y FALSE en otro caso
	* @param padre DComponenteBase Componente padre de este componente. Si no
	* tiene padre establecer a null
	*/
  public DIToggleButton(String nombre, boolean conexionDC,
								DComponenteBase padre) {
	 super(nombre, conexionDC, padre);
	 this.boton = new DJToggleButton("Boton Toggle");
	 try {
		jbInit();
	 }
	 catch (Exception e) {
		e.printStackTrace();
	 }
  }

  /**
	* @param textoBoton String Cadena de texto que presentara el togglebutton.
	* @param nombre String Nombre del componente.
	* @param conexionDC boolean TRUE si esta en contacto directo con el DConector
	* (no es hijo de ningun otro componente) y FALSE en otro caso
	* @param padre DComponenteBase Componente padre de este componente. Si no
	* tiene padre establecer a null
	*/
  public DIToggleButton(String textoBoton, String nombre, boolean conexionDC,
								DComponenteBase padre) {
	 super(nombre, conexionDC, padre);
	 this.boton = new DJToggleButton(textoBoton);

	 try {
		jbInit();
	 }
	 catch (Exception e) {
		e.printStackTrace();
	 }
  }

  private void jbInit() throws Exception {
	 this.setLayout(new FlowLayout());
	 this.add(boton, null);
	 boton.addDJToggleButtonListener(crearDJListener());
	 //desactivar();//*******************************************************************************
  }

  /**
	* Devuelve la instancia de la clase captadora que está usando.
	* @return DJToggleButton Clase captadora
	*/
  public DJToggleButton obtenerJComponente() {
	 return boton;
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
		boton.setForeground(Color.GRAY);
	 }
	 else {
		boton.setForeground(Color.BLACK);
	 }
  }

  public DJToggleButtonEvent obtenerInfoEstado() {
	 return boton.obtenerInfoEstado();
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
	* @param listener DJToggleButtonListener Listener a añadir
	*/
  public void addDJToggleButtonListener(DJToggleButtonListener listener) {
	 boton.addDJToggleButtonListener(listener);
  }

  /**
	* Añade un LListener al componente para ser notificado cuando cambie el
	* estado del componente
	* @param listener LJToggleButtonListener Listener a añadir
	*/
  public void addLJToggleButtonListener(LJToggleButtonListener listener) {
	 boton.addLJToggleButtonListener(listener);
  }

  /**
	* Añade un LUListener al componente para ser notificado cuando cambie el
	* estado del componente. Solo sera notificado cuando el cambio de estado
	* se deba a una accion realizada por el usuario de la aplicacion.
	* @param listener LJButtonListener Listener a añadir
	*/
  public void addLUJToggleButtonListener(LJToggleButtonListener listener) {
	 boton.addLUJToggleButtonListener(listener);
  }

  /**
	* Obtiene los DJListener que tiene asociado el componente
	* @return Vector Vector de listeners DJToggleButtonListener
	*/
  public Vector getDJToggleButtonListeners() {
	 return boton.getDJToggleButtonListeners();
  }

  /**
	* Obtiene los LJListener que tiene asociado el componente
	* @return Vector Vector de listeners LJToggleButtonListener
	*/
  public Vector getLJToggleButtonListeners() {
	 return boton.getLJToggleButtonListeners();
  }

  /**
	* Obtiene los LUJListener que tiene asociado el componente
	* @return Vector Vector de listeners LJToggleButtonListener
	*/
  public Vector getLUJToggleButtonListeners() {
	 return boton.getLUJToggleButtonListeners();
  }

  public void removeDJToggleButtonListeners() {
	 boton.removeDJToggleButtonListeners();
  }

  public void removeLJToggleButtonListeners() {
	 boton.removeLJToggleButtonListeners();
  }

  public void removeLUJToggleButtonListeners() {
	 boton.removeLUJToggleButtonListeners();
  }

  /**
	* Activa el componente. No se recomienda llamar a este metodo desde el
	* programa. Sera llamado de forma automatica cuando sea necesario
	*/
  public void activar() {
	 boton.activar();
  }

  /**
	* Desctiva el componente. No se recomienda llamar a este metodo desde el
	* programa. Sera llamado de forma automatica cuando sea necesario
	*/
  public void desactivar() {
	 boton.desactivar();
  }

  /**
	* Mediante una llamada a este método se envía un mensaje de peticion de
	* sincronizacion. No se debe llamar a este método de forma directa. Será
	* llamado de forma automatica cuando sea necesario realizar la sincronizacion
	*/
  public void sincronizar() {
	 if (conectadoDC()) {
		DJToggleButtonEvent peticion = new DJToggleButtonEvent();
		peticion.tipo = new Integer(DJToggleButtonEvent.SINCRONIZACION.intValue());
		enviarEvento(peticion);
	 }
  }

  /**
	* Devuelve una instancia de un listener que se encargara de tratar los
	* eventos que se reciben desde la clase captadora. Normalmente este tratamiento
	* se reduce a enviar el evento.
	* @return DJToggleButtonListener Listener creado
	*/
  public DJToggleButtonListener crearDJListener() {
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
		implements DJToggleButtonListener {

	 public void presionado(DJToggleButtonEvent evento) {
		enviarEvento(evento);
	 }

	 public void soltado(DJToggleButtonEvent evento) {
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
		DJToggleButtonEvent evento = null;
		DJToggleButtonEvent saux = null;
		DJToggleButtonEvent respSincr = null;
		Vector vaux = new Vector();

		DEvent[] eventos = obtenerEventosColaRecepcion();
		int numEventos = eventos.length;
		int i = 0;

		// Buscamos si se ha recibido una respuesta de sincronizacion
		for (int j = 0; j < numEventos; j++) {
		  saux = (DJToggleButtonEvent) eventos[j];
		  if ( (respSincr == null) &&
				(saux.tipo.intValue() ==
				 DJToggleButtonEvent.RESPUESTA_SINCRONIZACION.intValue())) {
			 respSincr = saux;
		  }
		  else {
			 vaux.add(saux);
		  }
		}

		activar();

		if (respSincr != null) { // Se ha recibido respuesta de sincronizacion
		  ultimoProcesado = new Integer(respSincr.ultimoProcesado.intValue());
		  boton.setSelected(respSincr.marcado.booleanValue());
		  boton.getModel().setPressed(respSincr.presionado.booleanValue());
		  boton.getModel().setArmed(respSincr.presionado.booleanValue());
		}

		// Colocamos en la cola de recepcion los eventos que deben ser
		// procesados (recibidos mientras se realizaba la sincronizacion )
		numEventos = vaux.size();
		for (int j = 0; j < numEventos; j++) {
		  saux = (DJToggleButtonEvent) vaux.elementAt(j);
		  if (saux.ultimoProcesado.intValue() > ultimoProcesado.intValue()) {
			 boton.procesarEvento(saux);
		  }
		}

		while (true) {
		  evento = (DJToggleButtonEvent) leerSiguienteEvento();
		  ultimoProcesado = new Integer(evento.contador.intValue());
		  if (evento.tipo.intValue() ==
				DJToggleButtonEvent.SINCRONIZACION.intValue() &&
				!evento.usuario.equals(DConector.Dusuario)) {
			 DJToggleButtonEvent infoEstado = obtenerInfoEstado();
			 infoEstado.tipo = new Integer(DJToggleButtonEvent.
													 RESPUESTA_SINCRONIZACION.intValue());
			 enviarEvento(infoEstado);
		  }
		  else {
			 boton.procesarEvento(evento);
		  }
		}

	 }
  }

}
