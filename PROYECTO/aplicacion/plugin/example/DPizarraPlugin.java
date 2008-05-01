package aplicacion.plugin.example;

import java.awt.BorderLayout;

import Deventos.DEvent;
import aplicacion.plugin.DAbstractPlugin;
import aplicacion.plugin.example.pizarra.ControlesPizarra;
import aplicacion.plugin.example.pizarra.Pizarra;

import componentes.base.DComponenteBase;
import componentes.base.DJFrame;

public class DPizarraPlugin extends DAbstractPlugin
{
	
	
	private DJFrame ventanaPizarra = null;
	Pizarra p = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6310087937591625336L;

	public DPizarraPlugin(  ) throws Exception
	{
		super("pizarra plugin", false, null);
		ventanaPizarra = new DJFrame(true,"mousesRemotos3");
		init();
		// TODO Auto-generated constructor stub
	}

	@Override
	public DAbstractPlugin getInstance() throws Exception
	{
		return new DPizarraPlugin();
	}

	@Override
	public void init() throws Exception
	{
		// TODO Auto-generated method stub
		version = 5;
		nombre = "Pizarra";
		jarFile = "ejemplo.jar";
		
		
		p = new Pizarra(nombre, true, null);
		
		ventanaPizarra.getContentPane().setLayout(new BorderLayout());
		ventanaPizarra.getContentPane().add(p, BorderLayout.CENTER);
		ventanaPizarra.getContentPane().add(new ControlesPizarra(p), BorderLayout.NORTH);
		ventanaPizarra.setTitle(":: Pizarra ::");
	}
	
	public int obtenerNumComponentesHijos()
	{
		return 1;	
	}
	
	public void procesarEventoHijo(DEvent e){
		p.procesarEvento(e);
	}

	
	public DComponenteBase obtenerComponente(int i){
		if (i==0)
			return p;
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
