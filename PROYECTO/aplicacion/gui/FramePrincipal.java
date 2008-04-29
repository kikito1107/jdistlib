package aplicacion.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;

import Deventos.enlaceJS.DConector;

import componentes.base.DJFrame;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class FramePrincipal
	 extends DJFrame {
  BorderLayout borderLayout1 = new BorderLayout();
  PanelPrincipal componente = null;

  public FramePrincipal() {
	 super(false, "MousesRemotos");
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
	 this.addWindowListener(new FrameEjemplo_this_windowAdapter(this));

	 this.setIconImage(new ImageIcon("./Resources/information.png").getImage());
	 
	 componente = new PanelPrincipal("Componente5", true, null);
	 this.getContentPane().add(componente, BorderLayout.CENTER);

	 
  }

  void this_windowClosing(WindowEvent e) {
	 DConector.obtenerDC().salir();
  }

}

class FrameEjemplo_this_windowAdapter
	 extends java.awt.event.WindowAdapter {
  FramePrincipal adaptee;

  FrameEjemplo_this_windowAdapter(FramePrincipal adaptee) {
	 this.adaptee = adaptee;
  }

  public void windowClosing(WindowEvent e) {
	 adaptee.this_windowClosing(e);
  }
}
