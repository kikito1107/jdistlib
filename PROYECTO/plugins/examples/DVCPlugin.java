package plugins.examples;

import componentes.gui.chat.VideoFrame;

import fisica.webcam.VideoConferencia;
import plugins.DAbstractPlugin;
import plugins.PluginContainer;

/**
 * Ejemplo basico de creacion de un plugin. Establece una videoconferencia
 * con nuestro propio ordenador
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class DVCPlugin extends DAbstractPlugin
{
	private static final long serialVersionUID = -9034900489624255928L;

	private VideoFrame ventana = null;

	/**
	 * Constructor
	 * @throws Exception
	 */
	public DVCPlugin() throws Exception
	{
		super("DpluginExample", false, PluginContainer.getPC(), "Video Conferencia", 2, "VC.jar", true);
		init();
	}

	@Override
	public DAbstractPlugin getInstance() throws Exception
	{
		return new DVCPlugin();
	}

	@Override
	public void init() throws Exception
	{
		versioningEnabled = true;
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
