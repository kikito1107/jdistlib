package aplicacion.plugin.example;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import aplicacion.plugin.DAbstractPlugin;

public class DPluginExample extends DAbstractPlugin
{
	private static final long serialVersionUID = -9034900489624255928L;

	private JFrame ventana = null;
	
	public DPluginExample() throws Exception
	{
		super("DpluginExample", true, null);
	}
	
	public DAbstractPlugin getInstance() throws Exception
	{
		return new DPluginExample();
	}
	
	public void init() throws Exception
	{
		version = 5;
		nombre = "Ejemplo";
		jarFile = "ejemplo.jar";
		versioningEnabled = false;
		
		ventana = new JFrame(nombre);
		ventana.setSize(400, 400);
	}

	public void start() throws Exception
	{
		JOptionPane.showMessageDialog(null, "Se inicia la ventana");
		ventana.setVisible(true);
	}

	public void stop() throws Exception
	{
		JOptionPane.showMessageDialog(null, "Se cierra la ventana");
		ventana.dispose();
	}
}
