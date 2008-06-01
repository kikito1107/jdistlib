/**
 * 
 */
package aplicacion.plugin.example;

import aplicacion.plugin.DAbstractPlugin;
import aplicacion.plugin.example.primos.PrimeMaster;

/**
 * @author anab
 *
 */
public class PluginCalculoPrimos extends DAbstractPlugin
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3319470838794298216L;
	PrimeMaster p;
	
	/**
	 * @param nombre
	 * @param conexionDC
	 * @param padre
	 * @throws Exception
	 */
	public PluginCalculoPrimos( ) throws Exception
	{
		super("CalcPrimosPlugin", false, null);
		init();
	}

	/* (non-Javadoc)
	 * @see aplicacion.plugin.DAbstractPlugin#getInstance()
	 */
	@Override
	public DAbstractPlugin getInstance() throws Exception
	{
		return new PluginCalculoPrimos();
	}

	/* (non-Javadoc)
	 * @see aplicacion.plugin.DAbstractPlugin#init()
	 */
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

	/* (non-Javadoc)
	 * @see aplicacion.plugin.DAbstractPlugin#start()
	 */
	@Override
	public void start() throws Exception
	{
		p.start();
	}

	/* (non-Javadoc)
	 * @see aplicacion.plugin.DAbstractPlugin#stop()
	 */
	@Override
	public void stop() throws Exception
	{
		p.stop();
	}

}
