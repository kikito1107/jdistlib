package aplicacion.plugin.example;

import javax.swing.JFrame;

import aplicacion.plugin.DAbstractPlugin;
import aplicacion.plugin.example.calculadora.Calc;

public class DCalcPlugin extends DAbstractPlugin
{

	private Calc eco;
	private JFrame ventana;
	
	public DCalcPlugin(  ) throws Exception
	{
		super("CalcPlugin", false, null);
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
		return new DCalcPlugin();
	}

	@Override
	public void init() throws Exception
	{
		version = 5;
		nombre = "Calculadora";
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
		ventana.setLocationRelativeTo(null);
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
