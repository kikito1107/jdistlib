package aplicacion.plugin.example;

import componentes.base.DComponenteBase;

import aplicacion.plugin.DAbstractPlugin;

public class PluginEcho extends DAbstractPlugin
{

	private echo eco;
	
	public PluginEcho(  ) throws Exception
	{
		super("PluginEcho", false, null);
		init();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8085076836531967376L;

	@Override
	public DAbstractPlugin getInstance() throws Exception
	{
		return new PluginEcho();
	}

	@Override
	public void init() throws Exception
	{
		version = 5;
		nombre = "Echo";
		jarFile = "ejemplo.jar";
		versioningEnabled = false;
		eco = new echo();

	}

	@Override
	public void start() throws Exception
	{
		//eco.pack();
		eco.setVisible(true);
	}

	@Override
	public void stop() throws Exception
	{
		// TODO Auto-generated method stub
		eco.dispose();
	}

}
