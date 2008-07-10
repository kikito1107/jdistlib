package plugins.examples;

import javax.swing.JFrame;

import plugins.DAbstractPlugin;
import plugins.PluginContainer;


/**
 * Plugin que implementa una calculadora totalmente
 * local para cada usuario.
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class DCalcPlugin extends DAbstractPlugin
{
	private static final long serialVersionUID = -8085076836531967376L;
	
	private Calc eco;

	private JFrame ventana;

	/**
	 * Constructor
	 * @throws Exception
	 */
	public DCalcPlugin() throws Exception
	{
		super("CalcPlugin", false, PluginContainer.getPC(), "Calculadora", 1, "calc.jar", true);
		init();
	}

	@Override
	public void init() throws Exception
	{
		categoria = DAbstractPlugin.CATEGORIA_UTILIDADES;
		versioningEnabled = false;
		shouldShow = true;
		descripcion = "Calculadora muy simple que funciona con el teclado";
		ventana = new JFrame(":: Calculadora ::");
		eco = new Calc();

	}

	@Override
	public void start() throws Exception
	{
		// eco.pack();
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
