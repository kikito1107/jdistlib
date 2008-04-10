package ejemplos;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import util.*;

/**
 * Componente implementado para el ejemplo 1
 */

public class Componente1
	 extends JPanel {
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

  public Componente1() {
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
	 botonCambio.addActionListener(new Componente1_botonCambio_actionAdapter(this));
	 jPanel2.setBounds(new Rectangle(267, 48, 149, 171));
	 jPanel2.setLayout(borderLayout2);
	 botonEliminar.setBounds(new Rectangle(298, 236, 87, 23));
	 botonEliminar.setText("Eliminar");
	 botonEliminar.addActionListener(new Componente1_botonEliminar_actionAdapter(this));
	 jLabel1.setFont(new java.awt.Font("Dialog", 1, 15));
	 jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
	 jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
	 jLabel1.setText("Aplicacion de ejemplo 1");
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

  void botonCambio_actionPerformed(ActionEvent e) {
	 String seleccionado = listaIzda.obtenerElementoSeleccionado();
	 if (seleccionado != null) {
		listaIzda.eliminarElemento(seleccionado);
		listaDcha.aniadirElemento(seleccionado);
	 }
  }

  void botonEliminar_actionPerformed(ActionEvent e) {
	 String seleccionado = listaDcha.obtenerElementoSeleccionado();
	 if (seleccionado != null) {
		listaDcha.eliminarElemento(seleccionado);
	 }
  }
}

class Componente1_botonCambio_actionAdapter
	 implements java.awt.event.ActionListener {
  Componente1 adaptee;

  Componente1_botonCambio_actionAdapter(Componente1 adaptee) {
	 this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
	 adaptee.botonCambio_actionPerformed(e);
  }
}

class Componente1_botonEliminar_actionAdapter
	 implements java.awt.event.ActionListener {
  Componente1 adaptee;

  Componente1_botonEliminar_actionAdapter(Componente1 adaptee) {
	 this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
	 adaptee.botonEliminar_actionPerformed(e);
  }
}
