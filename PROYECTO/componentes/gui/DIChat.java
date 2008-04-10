package componentes.gui;

import java.util.*;

import java.awt.*;

import Deventos.*;
import componentes.*;
import interfaces.*;
import interfaces.listeners.*;

/**
 * Chat con el que pueden hablar todos los usuarios de la aplicacion
 */

public class DIChat
	 extends DComponenteBase {
  DJChat chat = new DJChat();

  /**
	* @param nombre String Nombre del componente.
	* @param conexionDC boolean TRUE si esta en contacto directo con el DConector
	* (no es hijo de ningun otro componente) y FALSE en otro caso
	* @param padre DComponenteBase Componente padre de este componente. Si no
	* tiene padre establecer a null
	*/
  public DIChat(String nombre, boolean conexionDC, DComponenteBase padre) {
	 super(nombre, conexionDC, padre);

	 try {
		jbInit();
	 }
	 catch (Exception e) {
		e.printStackTrace();
	 }
  }

  private void jbInit() throws Exception {
	 this.setLayout(new BorderLayout());
	 this.add(chat, null);
	 chat.addDJChatListener(crearDJListener());
	 //desactivar();//*******************************************************************************
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
		chat.permisoLectura();
	 }
	 else {
		chat.permisoLecturaEscritura();
	 }
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
	* @param listener DJChatListener Listener a añadir
	*/
  public void addDJChatListener(DJChatListener listener) {
	 chat.addDJChatListener(listener);
  }

  /**
	* Añade un LListener al componente para ser notificado cuando cambie el
	* estado del componente
	* @param listener LJChatListener Listener a añadir
	*/
  public void addLJChatListener(LJChatListener listener) {
	 chat.addLJChatListener(listener);
  }

  /**
	* Obtiene los DJListener que tiene asociado el componente
	* @return Vector Vector de listeners DJChatListener
	*/
  public Vector getDJChatListeners() {
	 return chat.getDJChatListeners();
  }

  /**
	* Obtiene los LJListener que tiene asociado el componente
	* @return Vector Vector de listeners LJChatListener
	*/
  public Vector getLJChatListeners() {
	 return chat.getLJChatListeners();
  }

  /**
	* Elimina todos los DJListeners que tiene asociado el componente
	*/
  public void removeDJChatListeners() {
	 chat.removeDJChatListeners();
  }

  /**
	* Elimina todos los LJListeners que tiene asociado el componente
	*/
  public void removeLJChatListeners() {
	 chat.removeLJChatListeners();
  }

  /**
	* Activa el componente. No se recomienda llamar a este metodo desde el
	* programa. Sera llamado de forma automatica cuando sea necesario
	*/
  public void activar() {
	 chat.activar();
  }

  /**
	* Desctiva el componente. No se recomienda llamar a este metodo desde el
	* programa. Sera llamado de forma automatica cuando sea necesario
	*/
  public void desactivar() {
	 chat.desactivar();
  }

  /**
	* Mediante una llamada a este método se envía un mensaje de peticion de
	* sincronizacion. En este componente no se realiza sincronizacion por lo
	* que no tiene utilidad ninguna.
	*/
  public void sincronizar() {
	 // Este componente no realiza sincronizacion
  }

  public DJChatListener crearDJListener() {
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

  private class Listener
		implements DJChatListener {
	 public void nuevoMensaje(DJChatEvent evento) {
		enviarEvento(evento);
	 }
  }

  /**
	* Hebra procesadora de eventos. Se encarga de realizar las acciones que
	* correspondan cuando recibe un evento. En este caso no se realiza
	* sincronizacion dado que no es necesario.
	*/
  class HebraProcesadora
		extends HebraProcesadoraBase {

	 HebraProcesadora(DComponente dc) {
		super(dc);
	 }

	 public void run() {
		DJChatEvent evento = null;
		//System.out.println("iniciada hebra procesadora");

		activar();

		while (true) {
		  evento = (DJChatEvent) leerSiguienteEvento();
		  ultimoProcesado = new Integer(evento.contador.intValue());
		  chat.procesarEvento(evento);
		}

	 }
  }

}
