package ejemplos.imagen;

import java.awt.*;
import javax.swing.*;

import componentes.gui.imagen.FramePanelDibujo;

import Deventos.enlaceJS.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Ana Belen Pelegrina Ortiz
 * @version 1.0
 */

public class MainPanelDibujo {
  boolean packFrame = false;
  DConector d = null;

  //Construct the application
  public MainPanelDibujo() {
	 d = new DConector("AplicacionDePrueba");
	 d.inicializar();

	 FramePanelDibujo frame = new FramePanelDibujo(true);

	 if (packFrame) {
		frame.pack();
	 }
	 else {
		frame.validate();
	 }

	 frame.setSize(800, 720);

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

	 d.sincronizarComponentes();
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
	 new MainPanelDibujo();
  }

}
