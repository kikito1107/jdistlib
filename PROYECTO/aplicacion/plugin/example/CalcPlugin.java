package aplicacion.plugin.example;

import javax.swing.JFrame;

import aplicacion.plugin.DAbstractPlugin;
import aplicacion.plugin.example.calculadora.Calc;

public class CalcPlugin extends DAbstractPlugin
{

	private Calc eco;
	private JFrame ventana;
	
	public CalcPlugin(  ) throws Exception
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
		ventana = new JFrame(":: Calculadora ::");
		eco = new Calc();

	}

	@Override
	public void start() throws Exception
	{
		//eco.pack();
		eco.init();

		ventana.setSize(210, 200);
		ventana.add("Center", eco);
		ventana.setVisible(true);
	}

	@Override
	public void stop() throws Exception
	{
		// TODO Auto-generated method stub
		ventana.setVisible(false);
		ventana.dispose();
	}

}
