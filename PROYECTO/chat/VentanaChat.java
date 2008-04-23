package chat;

import interfaces.DComponente;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;


import chat.webcam.VideoConferencia;
import Deventos.enlaceJS.DConector;
import componentes.DJFrame;

public class VentanaChat extends DJFrame {
	  BorderLayout borderLayout1 = new BorderLayout();
	  PanelPrincipalChat componente = null;
	  String interlocutor;

	  public VentanaChat(String interloc) {
		 super(true, "MousesRemotos3");
		 try {
			 interlocutor = interloc;
			jbInit();
		 }
		 catch (Exception ex) {
			ex.printStackTrace();
		 }
	  }

	  void jbInit() throws Exception {
		 this.getContentPane().setLayout(borderLayout1);
		 this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		 this.setResizable(true);
		 this.addWindowListener(new FrameEjemplo_this_windowAdapter(this));

		 componente = new PanelPrincipalChat("panelChat", true, null, interlocutor, this);
		 this.getContentPane().add(componente, BorderLayout.CENTER);
		 
		 this.setTitle(".:: Chat : " + DConector.Dusuario + " ::.");
	  }

	  public void esconderVC(){
		  componente.esconderVC();
	  }
	  
	  void this_windowClosing(WindowEvent e) {
		 VideoConferencia.stopped = true;
		 componente.cerrar();
	  }
	  
	  public int obtenerNumComponentesHijos() {
			return 1;
		}

		public DComponente obtenerComponente(int i) {
			DComponente dc = null;
			switch (i) {
				case 0:
					dc = this.componente;
					break;
			}
			return dc;
		}
		
		public void setInterlocutor(String i) {
			componente.setInterlocutor(i);
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
