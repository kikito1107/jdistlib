package aplicacion.main;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.UIManager;

import awareness.ServidorMetaInformacion;
import awareness.gui.FrameAdminServMI;


/**
 * Clase principal para lanzar el servidor de metainformacion
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class MainServidorMI
{
	private boolean packFrame = false;

	public MainServidorMI()
	{
		FrameAdminServMI frame = new FrameAdminServMI();
		// Validate frames that have preset sizes
		// Pack frames that have useful preferred size info, e.g. from their
		// layout

		if (packFrame)
			frame.pack();
		else frame.validate();

		frame.setSize(350, 250);

		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		frame.setLocation(( screenSize.width - frameSize.width ) / 2,
				( screenSize.height - frameSize.height ) / 2);
		frame.setVisible(true);
	}

	/**
	 * Metodo main. Establece el Look&Feel y abre la ventana de la aplicacion
	 * 
	 * @param args
	 *            Argumentos de la aplicacion. Son ignorados
	 */
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		new MainServidorMI();
		new ServidorMetaInformacion();
	}

}
