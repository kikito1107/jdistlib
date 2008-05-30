package aplicacion.main;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.UIManager;

import aplicacion.fisica.FrameServFich;
import aplicacion.fisica.ServidorFicheros;

public class MainServidorFicheros
{
	private boolean packFrame = false;
	
	public MainServidorFicheros(){
		FrameServFich frame = new FrameServFich();
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
	
	public static void main(String args[])
	{
		try
		{
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		new MainServidorFicheros();
		new ServidorFicheros();
	}
}
