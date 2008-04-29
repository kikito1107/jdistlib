package aplicacion.plugin.example;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import aplicacion.fisica.webcam.VideoConferencia;
import aplicacion.fisica.webcam.VideoFrame;
import aplicacion.plugin.DAbstractPlugin;

public class DPluginExample extends DAbstractPlugin
{
	private static final long serialVersionUID = -9034900489624255928L;

	private VideoFrame ventana = null;
	
	public DPluginExample() throws Exception
	{
		super("DpluginExample", false, null);
		init();
	}
	
	public DAbstractPlugin getInstance() throws Exception
	{
		return new DPluginExample();
	}
	
	public void init() throws Exception
	{
		version = 5;
		nombre = "Videoconferencia";
		jarFile = "ejemplo.jar";
		versioningEnabled = false;
		
		ventana = new VideoFrame("127.0.0.1");
		System.out.println("aki");
		ventana.setSize(400, 400);
	}

	public void start() throws Exception
	{
		JOptionPane.showMessageDialog(null, "Se inicia la ventana");
		
		VideoConferencia.establecerOrigen();
		ventana.run();
	}

	public void stop() throws Exception
	{
		JOptionPane.showMessageDialog(null, "Se cierra la ventana");
		ventana.dispose();
	}
}
