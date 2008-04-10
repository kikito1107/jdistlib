package ejemplos;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Frame1
	 extends JFrame {
  BorderLayout borderLayout1 = new BorderLayout();
  Componente1 componente = new Componente1();

  public Frame1() {
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
	 this.addWindowListener(new Frame1_this_windowAdapter(this));
	 this.getContentPane().add(componente, BorderLayout.CENTER);
  }

  void this_windowClosing(WindowEvent e) {
  }
}

class Frame1_this_windowAdapter
	 extends java.awt.event.WindowAdapter {
  Frame1 adaptee;

  Frame1_this_windowAdapter(Frame1 adaptee) {
	 this.adaptee = adaptee;
  }

  public void windowClosing(WindowEvent e) {
	 adaptee.this_windowClosing(e);
  }
}
