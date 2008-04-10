package ejemplos;

import java.awt.*;
import java.awt.event.*;

import Deventos.enlaceJS.*;
import componentes.*;
import interfaces.listeners.*;
import metainformacion.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Frame5
	 extends DJFrame {
  BorderLayout borderLayout1 = new BorderLayout();
  Componente5 componente = new Componente5("Componente5", true, null);
  DJMenuBar barraMenu = new DJMenuBar("barramenu");
  DJMenu menu1 = new DJMenu("menu1");
  DJMenuItem menuItem1 = new DJMenuItem("item1_1");

  public Frame5() {
	 super(true, "MousesRemotos");
	 try {
		jbInit();
	 }
	 catch (Exception ex) {
		ex.printStackTrace();
	 }
  }

  void jbInit() throws Exception {
	 this.getContentPane().setLayout(borderLayout1);
	 this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	 this.setResizable(false);
	 this.addWindowListener(new Frame5_this_windowAdapter(this));
	 this.setJMenuBar(barraMenu);
	 menu1.setText("Menu1");
	 menuItem1.setText("AbrirCMI");
	 this.getContentPane().add(componente, BorderLayout.CENTER);
	 barraMenu.add(menu1);
	 menu1.add(menuItem1);
	 menuItem1.addLUJMenuItemListener(new ListenerMI());
  }

  void this_windowClosing(WindowEvent e) {
	 DConector.obtenerDC().salir();
  }

  private class ListenerMI
		implements LJMenuItemListener {
	 public void dispararAccion() {
		ClienteMetaInformacion.obtenerCMI().hacerVisibleDialogo();
	 }
  }
}

class Frame5_this_windowAdapter
	 extends java.awt.event.WindowAdapter {
  Frame5 adaptee;

  Frame5_this_windowAdapter(Frame5 adaptee) {
	 this.adaptee = adaptee;
  }

  public void windowClosing(WindowEvent e) {
	 adaptee.this_windowClosing(e);
  }
}
