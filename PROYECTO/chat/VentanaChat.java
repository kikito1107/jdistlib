package chat;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import Deventos.enlaceJS.DConector;
import componentes.DJFrame;

public class VentanaChat extends DJFrame {
	  BorderLayout borderLayout1 = new BorderLayout();
	  PanelPrincipalChat componente = null;

	  public VentanaChat() {
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
		 this.setResizable(true);
		 this.addWindowListener(new FrameEjemplo_this_windowAdapter(this));

		 componente = new PanelPrincipalChat("Componente5", true, null);
		 this.getContentPane().add(componente, BorderLayout.CENTER);

		 
	  }

	  void this_windowClosing(WindowEvent e) {
		 DConector.obtenerDC().salir();
	  }

	}

	class FrameEjemplo_this_windowAdapter
		 extends java.awt.event.WindowAdapter {
	  VentanaChat adaptee;

	  FrameEjemplo_this_windowAdapter(VentanaChat adaptee) {
		 this.adaptee = adaptee;
	  }

	  public void windowClosing(WindowEvent e) {
		 adaptee.this_windowClosing(e);
	  }
	}
