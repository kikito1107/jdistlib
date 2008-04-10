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

public class Frame3
	 extends JFrame {
  BorderLayout borderLayout1 = new BorderLayout();
  Componente3 componente = new Componente3("Componente3", true, null);

  public Frame3() {
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
	 this.addWindowListener(new Frame3_this_windowAdapter(this));
	 this.getContentPane().add(componente, BorderLayout.CENTER);
  }

  void this_windowClosing(WindowEvent e) {
	 DConector.obtenerDC().salir();
  }
}

class Frame3_this_windowAdapter
	 extends java.awt.event.WindowAdapter {
  Frame3 adaptee;

  Frame3_this_windowAdapter(Frame3 adaptee) {
	 this.adaptee = adaptee;
  }

  public void windowClosing(WindowEvent e) {
	 adaptee.this_windowClosing(e);
  }
}
