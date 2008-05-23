package metainformacion;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.UIManager;

/**
 * Clase para lanzar el servidor de Metainformacion
 */

public class MainServidorMI
{
	boolean packFrame = false;

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
	}

}
