package ejemplos;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Deventos.*;
import componentes.*;
import componentes.gui.*;
import componentes.gui.usuarios.DICambioRol;
import componentes.gui.usuarios.DIEtiquetaRolActual;
import componentes.gui.usuarios.DIListaUsuariosConectados;
import componentes.gui.usuarios.DIListaUsuariosConectadosInfoRol;
import componentes.gui.usuarios.DIListaUsuariosConectadosRol;
import interfaces.*;
import interfaces.listeners.*;

/**
 * Componente implementado para el cuarto ejemplo
 */
public class Componente5
	 extends DComponenteBase {
	
  private static final long serialVersionUID = 1L;
  JPanel jPanel1 = new JPanel();
  DIButton botonCambio = new DIButton(">>", "Boton", false, this);
  JPanel jPanel2 = new JPanel();
  DILista listaIzda = new DILista("Lista", true, false, this);
  DILista listaDcha = new DILista("Lista2", true, false, this);
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  JButton botonEliminar = new JButton();
  
  JLabel jLabel1 = new JLabel();
  
  DIListaUsuariosConectados usrConectados = new DIListaUsuariosConectados(
		"ListaUsuariosConectados", false, this);
  DIListaUsuariosConectadosRol usrConectadosR = new DIListaUsuariosConectadosRol(
		"ListaUsuariosConectadosRol", false, this);
  DIListaUsuariosConectadosInfoRol usrConectadosIR = new
		DIListaUsuariosConectadosInfoRol("ListaUsuariosConectadosInfoRol", false, this);
  DICambioRol cambioRol = new DICambioRol("CambioRol", false, this);
  DIEtiquetaRolActual rolActual = new DIEtiquetaRolActual("EtiquetaRolActual", false, this);

  public Componente5(String nombre, boolean conexionDC,
							DComponenteBase padre) {
	 super(nombre, conexionDC, padre);
	 try {
		jbInit();
	 }
	 catch (Exception ex) {
		ex.printStackTrace();
	 }
  }

  void jbInit() throws Exception {
	 this.setLayout(null);
	 jPanel1.setBounds(new Rectangle(14, 50, 149, 171));
	 jPanel1.setLayout(borderLayout1);
	 botonCambio.setBounds(new Rectangle(181, 121, 71, 40));
	 jPanel2.setBounds(new Rectangle(267, 48, 149, 171));
	 jPanel2.setLayout(borderLayout2);
	 botonEliminar.setBounds(new Rectangle(298, 236, 87, 23));
	 botonEliminar.setText("Eliminar");
	 botonEliminar.addActionListener(new Componente5_botonEliminar_actionAdapter(this));
	 jLabel1.setFont(new java.awt.Font("Dialog", 1, 15));
	 jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
	 jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
	 jLabel1.setText("Aplicacion de ejemplo 5");
	 jLabel1.setBounds(new Rectangle(110, 17, 202, 15));
	 usrConectados.setBounds(new Rectangle(440, 20, 140, 110));
	 usrConectadosR.setBounds(new Rectangle(440, 200, 180, 110));
	 usrConectadosIR.setBounds(new Rectangle(250, 300, 150, 122));
	 cambioRol.setBounds(new Rectangle(20, 300, 150, 200));
	 rolActual.setBounds(new Rectangle(450, 350, 150, 46));
	 this.add(jPanel1, null);
	 jPanel1.add(listaIzda, BorderLayout.CENTER);
	 this.add(jPanel2, null);
	 jPanel2.add(listaDcha, BorderLayout.CENTER);
	 this.add(botonCambio, null);
	 this.add(botonEliminar, null);
	 this.add(jLabel1, null);
	 this.add(usrConectados, null);
	 this.add(usrConectadosR, null);
	 this.add(usrConectadosIR, null);
	 this.add(cambioRol, null);
	 this.add(rolActual, null);

	 //****** ELEMENTOS DE LA LISTA IZQUIERDA ******
	  listaIzda.aniadirElemento("Perro");
	 listaIzda.aniadirElemento("Gato");
	 listaIzda.aniadirElemento("Coche");
	 listaIzda.aniadirElemento("Llave");
	 listaIzda.aniadirElemento("Barco");
	 listaIzda.aniadirElemento("Pajaro");
	 listaIzda.aniadirElemento("Pantalones");
	 listaIzda.aniadirElemento("Tornillo");
	 listaIzda.aniadirElemento("Bicicleta");
	 listaIzda.aniadirElemento("Pez");
	 listaIzda.aniadirElemento("Camisa");
	 listaIzda.aniadirElemento("Tortuga");
	 listaIzda.aniadirElemento("Ventana");
	 //*********************************************

	  // Añadimos un LUListener al boton para que se nos notifique el evento
	  // de soltar el boton
	 botonCambio.addLUJButtonListener(new ListenerBotonCambio());
  }

  /**
	* Mediante una llamada a este método se envía un mensaje de peticion de
	* sincronizacion. No se debe llamar a este método de forma directa. Será
	* llamado de forma automatica cuando sea necesario realizar la sincronizacion
	*/
  public void sincronizar() {
	 if (conectadoDC()) {
		EventoComponenteEjemplo peticion = new EventoComponenteEjemplo();
		peticion.tipo = new Integer(EventoComponenteEjemplo.SINCRONIZACION.
											 intValue());
		enviarEvento(peticion);
	 }
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
	* Obtiene el numero de componentes hijos de este componente. SIEMPRE devuelve 0
	* @return int Número de componentes hijos. En este caso devuelve 8 (la lista
	* izquierda, el boton, la lista derecha, la lista de usuarios conectados,
	* la lista de usuarios conectados bajo nuestro rol, la lista de usuarios
	* conectados con la informacion del rol actual, el componente de cambio de
	* rol y la etiqueta del rol actual)
	*/
  public int obtenerNumComponentesHijos() {
	 return 8;
  }

  /**
	* Obtiene el componente indicado
	* @param i int Índice del componente que queremos obtener. Se comienza a numerar
	* en el 0.
	* @return DComponente Componente indicado. Si el indice no es válido devuelve
	* null
	*/
  public DComponente obtenerComponente(int i) {
	 DComponente dc = null;
	 switch (i) {
		case 0:
		  dc = listaIzda;
		  break;
		case 1:
		  dc = botonCambio;
		  break;
		case 2:
		  dc = listaDcha;
		  break;
		case 3:
		  dc = usrConectados;
		  break;
		case 4:
		  dc = usrConectadosR;
		  break;
		case 5:
		  dc = usrConectadosIR;
		  break;
		case 6:
		  dc = cambioRol;
		  break;
		case 7:
		  dc = rolActual;
		  break;
	 }
	 return dc;
  }

  /**
	* Procesamos los eventos que recibimos de los componentes hijos. El procesamiento
	* se reduce a adjuntar el evento del parametro a un nuevo evento y enviarlo.
	* Los componentes de metainformacion no emiten eventos que deban ser procesados
	* @param evento DEvent Evento recibido
	*/
  synchronized public void procesarEventoHijo(DEvent evento) {
	 try {
		EventoComponenteEjemplo ev = new EventoComponenteEjemplo();
		if (evento.nombreComponente.equals("Boton")) {
		  ev.tipo = new Integer(EventoComponenteEjemplo.EVENTO_BOTON.intValue());
		  ev.aniadirEventoAdjunto(evento);
		  enviarEvento(ev);
		}
		else if (evento.nombreComponente.equals("Lista")) {
		  ev.tipo = new Integer(EventoComponenteEjemplo.EVENTO_LISTA_IZDA.
										intValue());
		  ev.aniadirEventoAdjunto(evento);
		  enviarEvento(ev);
		}
		else if (evento.nombreComponente.equals("Lista2")) {
		  ev.tipo = new Integer(EventoComponenteEjemplo.EVENTO_LISTA_DCHA.
										intValue());
		  ev.aniadirEventoAdjunto(evento);
		  enviarEvento(ev);
		}

	 }
	 catch (Exception e) {

	 }
  }

  void botonCambio_actionPerformed(ActionEvent e) {
	 String seleccionado = listaIzda.obtenerElementoSeleccionado();
	 if (seleccionado != null) {
		// Creamos un nuevo evento
		EventoComponenteEjemplo evento = new EventoComponenteEjemplo();
		// Establecemos el tipo del evento
		evento.tipo = new Integer(EventoComponenteEjemplo.CAMBIO_ELEMENTO.
										  intValue());
		// Indicamos el elemento que va a ser cambiado
		evento.elemento = new String(seleccionado);
		// Enviamos el evento
		enviarEvento(evento);
	 }
  }

  void botonEliminar_actionPerformed(ActionEvent e) {
	 String seleccionado = listaDcha.obtenerElementoSeleccionado();
	 if (seleccionado != null) {
		// Creamos un nuevo evento
		EventoComponenteEjemplo evento = new EventoComponenteEjemplo();
		// Establecemos el tipo del evento
		evento.tipo = new Integer(EventoComponenteEjemplo.
										  ELIMINAR_ELEMENTO_LISTA_DCHA.intValue());
		// Indicamos el elemento que va a ser eliminado
		evento.elemento = new String(seleccionado);
		// Enviamos el evento
		enviarEvento(evento);
	 }
  }

  /**
	* Listener encargado de escuchar el evento de soltar el boton
	*/
  private class ListenerBotonCambio
		implements LJButtonListener {
	 public void pulsado() {
		// No nos interesa el evento de pulsar el boton
	 }

	 public void soltado() {
		// Obtenemos el elemento seleccionado
		String seleccionado = listaIzda.obtenerElementoSeleccionado();
		if (seleccionado != null) {
		  // Creamos un nuevo evento
		  EventoComponenteEjemplo evento = new EventoComponenteEjemplo();
		  // Establecemos el tipo del evento
		  evento.tipo = new Integer(EventoComponenteEjemplo.CAMBIO_ELEMENTO.
											 intValue());
		  // Indicamos el evento que se va a cambiar
		  evento.elemento = new String(seleccionado);
		  // Enviamos el evento
		  enviarEvento(evento);
		}

	 }
  }

  /**
	* Hebra procesadora de eventos. Se encarga de realizar las acciones que
	* correspondan cuando recibe un evento. Tambén se encarga en su inicio
	* de sincronizar el componente.
	*/
  private class HebraProcesadora
		extends HebraProcesadoraBase {

	 HebraProcesadora(DComponente dc) {
		super(dc);
	 }

	 public void run() {
		EventoComponenteEjemplo evento = null;
		EventoComponenteEjemplo saux = null;
		EventoComponenteEjemplo respSincr = null;
		Vector<Object> vaux = new Vector<Object>();

		// Obtenemos los eventos existentes en la cola de recepcion. Estos eventos
		// se han recibido en el intervalo de tiempo desde que se envio la peticion
		// de sincronizacion y el inicio de esta hebra procesadora
		DEvent[] eventos = obtenerEventosColaRecepcion();
		int numEventos = eventos.length;
		int i = 0;

		// Buscamos entre los eventos si hay alguno correspondiente a una
		// respuesta de sincronizacion
		for (int j = 0; j < numEventos; j++) {
		  saux = (EventoComponenteEjemplo) eventos[j];
		  if ( (respSincr == null) &&
				(saux.tipo.intValue() ==
				 EventoComponenteEjemplo.RESPUESTA_SINCRONIZACION.intValue())) {
			 respSincr = saux;
		  }
		  else {
			 vaux.add(saux);
		  }
		}

		if (respSincr != null) { // Se ha recibido respuesta de sincronizacion
		  // Al actualizar nuestro estado con el del componente que nos envia
		  // la respuesta de sincronizacion establecemos nuestro índice de cual
		  // ha sido el ultimo evento procesado al mismo que el componente
		  ultimoProcesado = new Integer(respSincr.ultimoProcesado.intValue());
		  // Le pasamos la info de estado a la lista izquierda
		  listaIzda.procesarInfoEstado(respSincr.infoEstadoListaIzda);
		  // Le pasamos la info de estado a la lista derecha
		  listaDcha.procesarInfoEstado(respSincr.infoEstadoListaDcha);
		  // Le pasamos la info de estado a la lista derecha
		  botonCambio.procesarInfoEstado(respSincr.infoEstadoBoton);
		}

		// Todos esos eventos que se han recibido desde que se mando la peticion
		// de sincronizacion deben ser colocados en la cola de recepcion para
		// ser procesados. Solo nos interesan aquellos con un número de secuencia
		// posterior a ultimoProcesado. Los anteriores no nos interesan puesto
		// que ya han sido procesados por el componente que nos mando la respuesta
		// de sincronizacion.
		numEventos = vaux.size();
		for (int j = 0; j < numEventos; j++) {
		  saux = (EventoComponenteEjemplo) vaux.elementAt(j);
		  if (saux.ultimoProcesado.intValue() > ultimoProcesado.intValue()) {
			 procesarEvento(saux);
		  }
		}

		while (true) {
		  // Extraemos un evento de la la cola de recepcion
		  // Si no hay ninguno se quedara bloqueado hasta que haya
		  evento = (EventoComponenteEjemplo) leerSiguienteEvento();
		  // Actualizamos nuestro indicado de cual ha sido el último evento
		  // que hemos procesado
		  ultimoProcesado = new Integer(evento.contador.intValue());
		  if (evento.tipo.intValue() ==
				EventoComponenteEjemplo.SINCRONIZACION.intValue()) {
			 // Creamos un nuevo evento
			 EventoComponenteEjemplo infoEstado = new EventoComponenteEjemplo();
			 // Establecemos el tipo del evento
			 infoEstado.tipo = new Integer(EventoComponenteEjemplo.
													 RESPUESTA_SINCRONIZACION.intValue());
			 // Añadimos el esato de la lista izquierda
			 infoEstado.infoEstadoListaIzda = listaIzda.obtenerInfoEstado();
			 // Añadimos el esato de la lista derecha
			 infoEstado.infoEstadoListaDcha = listaDcha.obtenerInfoEstado();
			 // Añadimos el estado del boton
			 infoEstado.infoEstadoBoton = botonCambio.obtenerInfoEstado();
			 // Enviamos el evento
			 enviarEvento(infoEstado);
		  }
		  if (evento.tipo.intValue() ==
				EventoComponenteEjemplo.CAMBIO_ELEMENTO.intValue()) {
			 if (listaIzda.obtenerPosicionElemento(evento.elemento) != -1) {
				// Eliminamos el elemento de la lista izquierda
				listaIzda.eliminarElemento(evento.elemento);
				// Añadimos el elemento a la lista derecha
				listaDcha.aniadirElemento(evento.elemento);
			 }
		  }
		  if (evento.tipo.intValue() ==
				EventoComponenteEjemplo.EVENTO_BOTON.intValue()) {
			 // Le pasamos el evento al botón para que lo procese
			 botonCambio.procesarEvento(evento.extraerEventoAdjunto());
		  }
		  if (evento.tipo.intValue() ==
				EventoComponenteEjemplo.EVENTO_LISTA_IZDA.intValue()) {
			 // Le pasamos el evento a la lista para que lo procese
			 listaIzda.procesarEvento(evento.extraerEventoAdjunto());
		  }
		  if (evento.tipo.intValue() ==
				EventoComponenteEjemplo.EVENTO_LISTA_DCHA.intValue()) {
			 // Le pasamos el evento a la lista para que lo procese
			 listaDcha.procesarEvento(evento.extraerEventoAdjunto());
		  }
		  if (evento.tipo.intValue() ==
				EventoComponenteEjemplo.ELIMINAR_ELEMENTO_LISTA_DCHA.intValue()) {
			 // Eliminamos el elemento de la lista derecha
			 listaDcha.eliminarElemento(evento.elemento);
		  }
		}

	 }
  }

}

class Componente5_botonCambio_actionAdapter
	 implements java.awt.event.ActionListener {
  Componente5 adaptee;

  Componente5_botonCambio_actionAdapter(Componente5 adaptee) {
	 this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
	 adaptee.botonCambio_actionPerformed(e);
  }
}

class Componente5_botonEliminar_actionAdapter
	 implements java.awt.event.ActionListener {
  Componente5 adaptee;

  Componente5_botonEliminar_actionAdapter(Componente5 adaptee) {
	 this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
	 adaptee.botonEliminar_actionPerformed(e);
  }
}
