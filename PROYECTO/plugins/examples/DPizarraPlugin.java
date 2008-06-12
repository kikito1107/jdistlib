package plugins.examples;

import java.awt.BorderLayout;

import plugins.DAbstractPlugin;
import plugins.PluginContainer;
import plugins.examples.blackboard.ControlesPizarra;
import plugins.examples.blackboard.Pizarra;


import componentes.base.DComponenteBase;
import componentes.base.DJFrame;
import Deventos.DEvent;

/**
 * Plugin con una pizarra compartida para realizar
 * dibujos con otros usuarios
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class DPizarraPlugin extends DAbstractPlugin
{
	private DJFrame ventanaPizarra = null;

	private Pizarra p = null;

	private static final long serialVersionUID = -6310087937591625336L;

	/**
	 * Constructor
	 * @throws Exception
	 */
	public DPizarraPlugin() throws Exception
	{
		super("pizarra plugin", false, PluginContainer.getPC(), "Pizarra", 5, "pizarrra.jar", true);
		ventanaPizarra = new DJFrame(true, "mousesRemotos3");
		init();
	}

	@Override
	public DAbstractPlugin getInstance() throws Exception
	{
		return new DPizarraPlugin();
	}

	@Override
	public void init() throws Exception
	{
		categoria = DAbstractPlugin.CATEGORIA_EDICION;
		descripcion = "Pizarra para pintar junto a tus colaboradores";
		
		this.versioningEnabled = true;
		
		p = new Pizarra(nombre, true, null);

		ventanaPizarra.getContentPane().setLayout(new BorderLayout());
		ventanaPizarra.getContentPane().add(p, BorderLayout.CENTER);
		ventanaPizarra.getContentPane().add(new ControlesPizarra(p),
				BorderLayout.NORTH);
		ventanaPizarra.setTitle(":: Pizarra ::");
	}

	@Override
	public int obtenerNumComponentesHijos()
	{
		return 1;
	}

	@Override
	public synchronized void procesarEventoHijo(DEvent e)
	{
		p.procesarEvento(e);
	}

	@Override
	public DComponenteBase obtenerComponente(int i)
	{
		if (i == 0) return p;
		return null;
	}

	@Override
	public void start() throws Exception
	{
		// TODO Auto-generated method stub
		ventanaPizarra.pack();
		ventanaPizarra.setSize(530, 445);
		ventanaPizarra.setLocationRelativeTo(null);

		ventanaPizarra.setVisible(true);
	}

	@Override
	public void stop() throws Exception
	{
		// TODO Auto-generated method stub
		ventanaPizarra.dispose();
	}

}
