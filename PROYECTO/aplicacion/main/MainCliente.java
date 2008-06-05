package aplicacion.main;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.UIManager;

import Deventos.enlaceJS.DConector;
import aplicacion.gui.FramePrincipal;
import aplicacion.plugin.PluginContainer;

/**
 * 
 * <p>
 * Title: Main Cliente
 * </p>
 * <p>
 * Description: Main de la aplicacion de los clientes
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: 
 * </p>
 * 
 * @author Ana B., Carlos
 * @version 1.2
 */
public class MainCliente
{
	boolean packFrame = false;

	DConector d = null;

	// Construct the application
	private MainCliente()
	{
		d = new DConector("AplicacionDePrueba");
		d.inicializar();

		FramePrincipal frame = new FramePrincipal();

		if (packFrame)
			frame.pack();
		else frame.validate();

		frame.setSize(570, 520);
		d.sincronizarComponentes();

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

		String nombreUsuario = DConector.Dusuario;
		frame.setTitle(".:: Grupo de trabajo : " + nombreUsuario + "  ::.");
	}

	/**
	 * Metodo main. Establece el Look&Feel, inicia el contendor del Plugins y la ventana principal de la aplicacion
	 * @param args argumentos. son ignorados
	 */
	public static void main(String[] args)
	{

		try
		{
			UIManager.setLookAndFeel("lookandfeel.Dmetal.MetalLookAndFeel");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		// inicializamos el contenedor de plugins
		new PluginContainer();
		
		// iniciamos la GUI
		new MainCliente();
	}

}
