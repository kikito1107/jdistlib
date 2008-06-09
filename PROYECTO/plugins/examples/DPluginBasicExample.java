package plugins.examples;

import fisica.webcam.VideoConferencia;
import fisica.webcam.VideoFrame;
import plugins.DAbstractPlugin;

/**
 * Ejemplo basico de creacion de un plugin. Establece una videoconferencia
 * con nuestro propio ordenador
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class DPluginBasicExample extends DAbstractPlugin
{
	private static final long serialVersionUID = -9034900489624255928L;

	private VideoFrame ventana = null;

	/**
	 * Constructor
	 * @throws Exception
	 */
	public DPluginBasicExample() throws Exception
	{
		super("DpluginExample", false, null);
		init();
	}

	@Override
	public DAbstractPlugin getInstance() throws Exception
	{
		return new DPluginBasicExample();
	}

	@Override
	public void init() throws Exception
	{
		version = 1;
		nombre = "Videoconferencia";
		jarFile = "vc.jar";
		versioningEnabled = false;
		categoria = DAbstractPlugin.CATEGORIA_OTROS;
	}

	@Override
	public void start() throws Exception
	{
		VideoConferencia.establecerOrigen();

		String ip = "localhost";
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
