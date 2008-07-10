package plugins.examples;

import plugins.DAbstractPlugin;
import plugins.PluginContainer;

/**
 * Plugin que permite gestionar los plugins de la plataforma
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class DManagerPlugin extends DAbstractPlugin
{
	private GestorPlugins gestorPlugin = null;

	private static final long serialVersionUID = -6310087937591625336L;

	/**
	 * Constructor
	 * @throws Exception
	 */
	public DManagerPlugin() throws Exception
	{
		super("GestorPlugin", false, PluginContainer.getPC(), "Gestor", 1, "gestor.jar", true);
		init();
	}

	@Override
	public void init() throws Exception
	{
		categoria = DAbstractPlugin.CATEGORIA_UTILIDADES;
		descripcion = "Gestor de los plugins instalados en esta aplicacion";

		gestorPlugin = new GestorPlugins();
	}

	@Override
	public void start() throws Exception
	{

		gestorPlugin.inicializarModelo();
		gestorPlugin.pack();
		gestorPlugin.setLocationRelativeTo(null);
		gestorPlugin.setVisible(true);
	}

	@Override
	public void stop() throws Exception
	{
		// TODO Auto-generated method stub
		gestorPlugin.dispose();
	}

}
