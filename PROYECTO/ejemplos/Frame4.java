package ejemplos;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Deventos.enlaceJS.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Frame4
	 extends JFrame {
  BorderLayout borderLayout1 = new BorderLayout();
  Componente4 componente = new Componente4("Componente4", true, null);

  public Frame4() {
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
	 this.addWindowListener(new Frame4_this_windowAdapter(this));
	 this.getContentPane().add(componente, BorderLayout.CENTER);
  }

  void this_windowClosing(WindowEvent e) {
	 DConector.obtenerDC().salir();
  }
}

class Frame4_this_windowAdapter
	 extends java.awt.event.WindowAdapter {
  Frame4 adaptee;

  Frame4_this_windowAdapter(Frame4 adaptee) {
	 this.adaptee = adaptee;
  }

  public void windowClosing(WindowEvent e) {
	 adaptee.this_windowClosing(e);
  }
}
