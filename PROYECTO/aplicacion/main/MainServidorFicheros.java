package aplicacion.main;

import javax.swing.UIManager;

import aplicacion.fisica.ServidorFicheros;

public class MainServidorFicheros
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
		new ServidorFicheros();
	}
}
