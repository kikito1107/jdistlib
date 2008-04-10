package aplicacion;

import java.awt.*;
import javax.swing.*;

import aplicacion.gui.FramePrincipal;



import Deventos.enlaceJS.*;

/**
 * 
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class MainCliente {
  boolean packFrame = false;
  DConector d = null;

  //Construct the application
  public MainCliente() {
	 d = new DConector("AplicacionDePrueba");
	 d.inicializar();

	 FramePrincipal frame = new FramePrincipal();

	 if (packFrame) {
		frame.pack();
	 }
	 else {
		frame.validate();
	 }

	 frame.setSize(568, 545);
	 d.sincronizarComponentes();
	 
	 //Center the window
	 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	 Dimension frameSize = frame.getSize();
	 if (frameSize.height > screenSize.height) {
		frameSize.height = screenSize.height;
	 }
	 if (frameSize.width > screenSize.width) {
		frameSize.width = screenSize.width;
	 }
	 frame.setLocation( (screenSize.width - frameSize.width) / 2,
							 (screenSize.height - frameSize.height) / 2);
	 frame.setVisible(true);

	 
	 String nombreUsuario = d.Dusuario;
	 frame.setTitle(".:: Grupo de trabajo : " + nombreUsuario + "  ::.");
  }

  //Main method
  public static void main(String[] args) {

	 try {
		UIManager.setLookAndFeel("lookandfeel.Dmetal.MetalLookAndFeel");
		// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
	 }
	 catch (Exception e) {
		e.printStackTrace();
	 }
	 new MainCliente();
  }

}
