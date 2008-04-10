package componentes.gui.imagen;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import aplicacion.fisica.documentos.Documento;

import Deventos.enlaceJS.*;
import componentes.*;
import interfaces.DComponente;
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

/**
 * Clase que implementa una ventana con un panel de dibujo
 */
public class FramePanelDibujo extends DJFrame {
	
  BorderLayout borderLayout1 = new BorderLayout();
  DIPanelDibujo lienzo = null;
  
  static boolean standalone;

  public FramePanelDibujo(boolean sa) 
  {
	 super(true, "MousesRemotos2");
	 standalone = sa;
	 try {
		jbInit();
	 }
	 catch (Exception ex) {
		ex.printStackTrace();
	 }
  }

  void jbInit() throws Exception {
	 this.getContentPane().setLayout(borderLayout1);
	 if (!standalone)
		 this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	 
	 else
		 this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	 this.setResizable(false);
	 this.addWindowListener(new FrameDrawPanel_this_windowAdapter(this));
	 lienzo = new DIPanelDibujo("panelDibujo", true, null);
	 lienzo.setPadre(this);
	 this.getContentPane().add(lienzo, BorderLayout.CENTER);
	 

	 
	 this.setTitle(".:: Panel Dibujo Compartido : " + DConector.Dusuario + " ::.");
  }

  void this_windowClosing(WindowEvent e) {
	
	  if (standalone)
		  DConector.obtenerDC().salir();
	  
	  else {
	  
	  setDocumento(new Documento());
	  System.gc();
	  }
  }
  
  public void setVisible(boolean b)
  {
	  super.setVisible(b);
	  repaint();
	  validate();
	  lienzo.repaint();
	  lienzo.validate();
  }
  
  public DIPanelDibujo getLienzo(){
	  return lienzo;
  }
  
  public void paint(Graphics g)
  {
	  super.paint(g);
  }

public void setDocumento(Documento d)
{
	lienzo.setDocumento(d);
	
}
}

class FrameDrawPanel_this_windowAdapter
	 extends java.awt.event.WindowAdapter {
  FramePanelDibujo adaptee;

  FrameDrawPanel_this_windowAdapter(FramePanelDibujo adaptee) {
	 this.adaptee = adaptee;
  }

  public void windowClosing(WindowEvent e) {
	 adaptee.this_windowClosing(e);
  }
}
