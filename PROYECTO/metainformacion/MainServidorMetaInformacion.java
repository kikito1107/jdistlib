package metainformacion;

import javax.swing.UIManager;

import aplicacion.main.MainServidorMI;

public class MainServidorMetaInformacion
{
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
		new MainServidorMI();
	}
}
