package ejemplos;

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

public class Main1 {
  boolean packFrame = false;

  //DConector d = null;


  //Construct the application
  public Main1() {
	 // d = new DConector("AplicacionDePrueba");
	 //d.inicializar();

	 Frame1 frame = new Frame1();

	 if (packFrame) {
		frame.pack();
	 }
	 else {
		frame.validate();
	 }

	 frame.setSize(450, 300);

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

	 //d.sincronizarComponentes(frame);
  }

  //Main method
  public static void main(String[] args) {

	 try {
		//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		//UIManager.setLookAndFeel("lookandfeel.Dmetal.MetalLookAndFeel");
	 }
	 catch (Exception e) {
		e.printStackTrace();
	 }
	 new Main1();
  }

}
