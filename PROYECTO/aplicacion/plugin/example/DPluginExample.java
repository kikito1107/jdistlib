package aplicacion.plugin.example;

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

	@Override
	public DAbstractPlugin getInstance() throws Exception
	{
		return new DPluginExample();
	}

	@Override
	public void init() throws Exception
	{
		version = 1;
		nombre = "Videoconferencia";
		jarFile = "ejemplo.jar";
		versioningEnabled = false;
	}

	@Override
	public void start() throws Exception
	{

		VideoConferencia.establecerOrigen();

		String ip = "localhost";

		// ip = JOptionPane.showInputDialog("Introduce la ip de destino");

		ventana = new VideoFrame(ip);
		ventana.setSize(400, 400);
		ventana.setLocationRelativeTo(null);
		ventana.run();
	}

	@Override
	public void stop() throws Exception
	{
		ventana.dispose();
	}
}
