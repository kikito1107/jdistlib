package aplicacion;

import javax.swing.UIManager;

import aplicacion.fisica.ServidorFicheros;


import metainformacion.MainServidorMI;

public class MainServidorFicheros
{
	public static void main(String args[])
	{
		 try {
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			 }
			 catch (Exception e) {
				e.printStackTrace();
			 }
			 new ServidorFicheros();
	}
}
