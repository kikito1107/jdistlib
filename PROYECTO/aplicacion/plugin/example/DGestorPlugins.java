package aplicacion.plugin.example;

import aplicacion.plugin.DAbstractPlugin;
import aplicacion.plugin.example.gestor.GestorPlugins;

public class DGestorPlugins extends DAbstractPlugin
{

	private GestorPlugins gestorPlugin = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6310087937591625336L;

	public DGestorPlugins() throws Exception
	{
		super("GestorPlugin", false, null);
		init();
		// TODO Auto-generated constructor stub
	}

	@Override
	public DAbstractPlugin getInstance() throws Exception
	{
		return new DGestorPlugins();
	}

	@Override
	public void init() throws Exception
	{
		// TODO Auto-generated method stub
		version = 1;
		nombre = "Gestor Plugins";
		jarFile = "gestor.jar";

		gestorPlugin = new GestorPlugins();
	}

	@Override
	public void start() throws Exception
	{
		// TODO Auto-generated method stub

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
