package ejemplos.imagen;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Deventos.enlaceJS.*;
import componentes.*;
import metainformacion.*;
import componentes.gui.imagen.DIViewer;
import componentes.listeners.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class FrameViewer
	 extends DJFrame 
{
  BorderLayout borderLayout1 = new BorderLayout();
  DIViewer componente = null;
  DJMenuBar barraMenu = new DJMenuBar("barramenu");
  DJMenu menu1 = new DJMenu("menu1");
  DJMenuItem menuItem1 = new DJMenuItem("item1_1");
  DJMenuItem menuItem2 = new DJMenuItem("item1_2");

  public FrameViewer() 
  {
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
	 menuItem2.setText("Abrir Imagen");
	 componente = new DIViewer("visor", true, null);
	 
	 this.getContentPane().add(componente, BorderLayout.CENTER);
	 barraMenu.add(menu1);
	 menu1.add(menuItem1);
	 menu1.add(menuItem2);
	 menuItem1.addLUJMenuItemListener(new ListenerMI());
	 menuItem2.addLUJMenuItemListener(new ListenerMI2());
	 
	 //componente.setImage("./Resources/7b1doisneaubaiser.jpg");
  }

  void this_windowClosing(WindowEvent e) {
	 DConector.obtenerDC().salir();
  }
  
  public void paint(Graphics g)
  {
	  super.paint(g);
  }
  
  private class ListenerMI2
  implements LJMenuItemListener {
	  public void dispararAccion()
	  {
		  JFileChooser jfc = new JFileChooser();
		  int op = jfc.showDialog(null, "Aceptar");
		  
		  if (op == JFileChooser.APPROVE_OPTION)
		  {
			  java.io.File f = jfc.getSelectedFile();
			  componente.setImage(f.getAbsolutePath());
			  Deventos.DJViewerEvent evt = new Deventos.DJViewerEvent();
			  Image img = componente.getImage();
			  
			  evt.contenido = new ImageIcon(img);
			  evt.tipo = Deventos.DJViewerEvent.CARGADO;
			  
			  componente.enviarEvento(evt);
		  }
	  }
	  
	  
	  /**
		* Obtiene el numero de componentes hijos de este componente. SIEMPRE devuelve 0
		* @return int Número de componentes hijos. En este caso devuelve 8 (la lista
		* izquierda, el boton, la lista derecha, la lista de usuarios conectados,
		* la lista de usuarios conectados bajo nuestro rol, la lista de usuarios
		* conectados con la informacion del rol actual, el componente de cambio de
		* rol y la etiqueta del rol actual)
		*/
	  public int obtenerNumComponentesHijos() {
		 return 1;
	  }

	  /**
		* Obtiene el componente indicado
		* @param i int Indice del componente que queremos obtener. Se comienza a numerar
		* en el 0.
		* @return DComponente Componente indicado. Si el indice no es v‡lido devuelve
		* null
		*/
	  public DComponente obtenerComponente(int i) {
		 DComponente dc = null;
		 switch (i) {
			case 0:
			  dc = componente;
			  break;
		 }
		 return dc;
	  }
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
  FrameViewer adaptee;

  Frame5_this_windowAdapter(FrameViewer adaptee) {
	 this.adaptee = adaptee;
  }

  public void windowClosing(WindowEvent e) {
	 adaptee.this_windowClosing(e);
  }
}
