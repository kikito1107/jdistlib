/**
 * 
 */
package plugins.examples;

import plugins.DAbstractPlugin;
import plugins.examples.primes.PrimeMaster;

/**
 * Plugin para el calculo paralelo de numeros primos
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class PluginCalculoPrimos extends DAbstractPlugin
{
	private static final long serialVersionUID = 3319470838794298216L;
	
	private PrimeMaster p;
	
	/**
	 * Constructor
	 * @throws Exception
	 */
	public PluginCalculoPrimos( ) throws Exception
	{
		super("CalcPrimosPlugin", false, null);
		init();
	}

	@Override
	public DAbstractPlugin getInstance() throws Exception
	{
		return new PluginCalculoPrimos();
	}

	@Override
	public void init() throws Exception
	{
		version = 5;
		nombre = "Calculo Distribuido";
		jarFile = "primos.jar";
		versioningEnabled = false;
		categoria = DAbstractPlugin.CATEGORIA_CIENTIFICO;
		descripcion = "Calculo de numeros primos en paralelo. Comunicate con tus colaboradores para que pongan su ordenador a tu disposicion para los calculos!!";
		
		p = new PrimeMaster(false);
	}

	@Override
	public void start() throws Exception
	{
		p.start();
	}

	@Override
	public void stop() throws Exception
	{
		p.stop();
	}

}
