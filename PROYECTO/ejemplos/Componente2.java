package ejemplos;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import componentes.*;
import interfaces.*;
import util.*;

/**
 * Componente implementado para el segundo ejemplo
 */

public class Componente2
	 extends DComponenteBase {
  JPanel jPanel1 = new JPanel();
  JButton botonCambio = new JButton();
  JPanel jPanel2 = new JPanel();
  JScrollPane jScrollPane1 = new JScrollPane();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  JScrollPane jScrollPane2 = new JScrollPane();
  JButton botonEliminar = new JButton();
  ListaElementos listaIzda = new ListaElementos();
  ListaElementos listaDcha = new ListaElementos();
  JLabel jLabel1 = new JLabel();

  public Componente2(String nombre, boolean conexionDC,
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
	 botonCambio.setBounds(new Rectangle(181, 121, 71, 23));
	 botonCambio.setText(">>");
	 botonCambio.addActionListener(new Componente2_botonCambio_actionAdapter(this));
	 jPanel2.setBounds(new Rectangle(267, 48, 149, 171));
	 jPanel2.setLayout(borderLayout2);
	 botonEliminar.setBounds(new Rectangle(298, 236, 87, 23));
	 botonEliminar.setText("Eliminar");
	 botonEliminar.addActionListener(new Componente2_botonEliminar_actionAdapter(this));
	 jLabel1.setFont(new java.awt.Font("Dialog", 1, 15));
	 jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
	 jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
	 jLabel1.setText("Aplicacion de ejemplo 2");
	 jLabel1.setBounds(new Rectangle(110, 17, 202, 15));
	 this.add(jPanel1, null);
	 jPanel1.add(jScrollPane1, BorderLayout.CENTER);
	 this.add(jPanel2, null);
	 jPanel2.add(jScrollPane2, BorderLayout.CENTER);
	 this.add(botonCambio, null);
	 this.add(botonEliminar, null);
	 this.add(jLabel1, null);
	 jScrollPane2.getViewport().add(listaDcha, null);
	 jScrollPane1.getViewport().add(listaIzda, null);

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
		// Eliminamos el elemento seleccionado de la lista derecha
		listaDcha.eliminarElemento(seleccionado);
	 }
  }

  /**
	* Hebra procesadora de eventos. Se encarga de realizar las acciones que
	* correspondan cuando recibe un evento.
	*/
  class HebraProcesadora
		extends HebraProcesadoraBase {

	 HebraProcesadora(DComponente dc) {
		super(dc);
	 }

	 public void run() {
		EventoComponenteEjemplo evento = null;

		int i = 0;
		while (true) {
		  // Extraemos un evento de la la cola de recepcion
		  // Si no hay ninguno se quedara bloqueado hasta que haya
		  evento = (EventoComponenteEjemplo) leerSiguienteEvento();
		  // Actualizamos nuestro indicado de cual ha sido el último evento
		  // que hemos procesado
		  ultimoProcesado = new Integer(evento.contador.intValue());

		  if (evento.tipo.intValue() ==
				EventoComponenteEjemplo.CAMBIO_ELEMENTO.intValue()) {
			 if (listaIzda.obtenerPosicionElemento(evento.elemento) != -1) {
				// Eliminamos el elemento de la lista izquierda
				listaIzda.eliminarElemento(evento.elemento);
				// Lo añadimos a la lista derecha
				listaDcha.aniadirElemento(evento.elemento);
			 }
		  }
		}

	 }
  }

}

class Componente2_botonCambio_actionAdapter
	 implements java.awt.event.ActionListener {
  Componente2 adaptee;

  Componente2_botonCambio_actionAdapter(Componente2 adaptee) {
	 this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
	 adaptee.botonCambio_actionPerformed(e);
  }
}

class Componente2_botonEliminar_actionAdapter
	 implements java.awt.event.ActionListener {
  Componente2 adaptee;

  Componente2_botonEliminar_actionAdapter(Componente2 adaptee) {
	 this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
	 adaptee.botonEliminar_actionPerformed(e);
  }
}
