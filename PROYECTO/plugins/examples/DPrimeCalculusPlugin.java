/**
 * 
 */
package plugins.examples;

import plugins.DAbstractPlugin;
import plugins.PluginContainer;
import plugins.examples.primes.PrimeMaster;

/**
 * Plugin para el calculo paralelo de numeros primos
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class DPrimeCalculusPlugin extends DAbstractPlugin
{
	private static final long serialVersionUID = 3319470838794298216L;
	
	private PrimeMaster p;
	
	/**
	 * Constructor
	 * @throws Exception
	 */
	public DPrimeCalculusPlugin( ) throws Exception
	{
		super("CalcPrimosPlugin", false, PluginContainer.getPC(), "Calculo Primos", 1, "primos.jar", true);
		init();
	}

	@Override
	public DAbstractPlugin getInstance() throws Exception
	{
		return new DPrimeCalculusPlugin();
	}

	@Override
	public void init() throws Exception
	{
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
