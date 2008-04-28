package metainformacion.gui;

import java.awt.*;
import javax.swing.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DialogoMetaInformacion
	 extends JDialog {

  private static final long serialVersionUID = 1L;
  Frame frame = null;
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  private final int anchura = 240;
  PanelMetaInformacion jPanel1 = new PanelMetaInformacion();

  public DialogoMetaInformacion(Frame frame, String title, boolean modal) {
	 super(frame, title, modal);
	 try {
		this.frame = frame;
		jbInit();
		pack();
	 }
	 catch (Exception ex) {
		ex.printStackTrace();
	 }

  inicializar("");
  }

  public DialogoMetaInformacion() {
	 this(null, "", false);
  }


  public void nuevoUsuario(String usuario){
	 jPanel1.nuevoUsuario(usuario);
}
  public void nuevoRol(String rol){
	 jPanel1.nuevoRol(rol);
}
  public void eliminarUsuario(String usuario){
	 jPanel1.eliminarUsuario(usuario);
  }
  public void eliminarRol(String rol){
	 jPanel1.eliminarRol(rol);
}

  private void jbInit() throws Exception {
	 panel1.setLayout(borderLayout1);
	 getContentPane().add(panel1);
	 panel1.add(jPanel1, BorderLayout.CENTER);

  }

  public void inicializar(String rol){
	  jPanel1.inicializar(rol);
}

  public void setVisible(boolean b) {
	 if (b) {
		//int alturaFrame = frame.getSize().height;
		setSize(450, 500);
		setLocation(10,10);
	 }
	 super.setVisible(b);
  }

  private Point calcularPosicion() {
	 Point posFrame = frame.getLocation();
	 Point posDialogo = new Point();

	 posDialogo.x = posFrame.x - anchura;
	 posDialogo.y = posFrame.y;

	 return posDialogo;
  }

  private Point calcularPosicionFrame() {
	 Point posDialogo = getLocation();
	 Point posFrame = new Point();

	 posFrame.x = posDialogo.x + anchura;
	 posFrame.y = posDialogo.y;

	 return posFrame;
  }

}
