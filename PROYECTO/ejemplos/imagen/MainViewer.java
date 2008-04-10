package ejemplos.imagen;

import java.awt.*;
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

public class MainViewer {
  boolean packFrame = false;
  DConector d = null;

  //Construct the application
  public MainViewer() {
	 d = new DConector("AplicacionDePrueba");
	 d.inicializar();

	 FrameViewer frame = new FrameViewer();

	 if (packFrame) {
		frame.pack();
	 }
	 else {
		frame.validate();
	 }

	 frame.setSize(650, 550);

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
	 new MainViewer();
  }

}
