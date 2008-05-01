package aplicacion.plugin.example;

import java.awt.BorderLayout;

import componentes.base.DComponenteBase;
import componentes.base.DJFrame;
import componentes.gui.DIChat;
import componentes.gui.usuarios.ArbolUsuariosConectadosRol;

import Deventos.DEvent;
import Deventos.enlaceJS.DConector;
import aplicacion.plugin.DAbstractPlugin;
import aplicacion.plugin.example.gestor.GestorPlugins;
import aplicacion.plugin.example.pizarra.ControlesPizarra;
import aplicacion.plugin.example.pizarra.Pizarra;

public class DGestorPlugins extends DAbstractPlugin
{
	
	
	private GestorPlugins gestorPlugin = null;
	Pizarra p = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6310087937591625336L;

	public DGestorPlugins(  ) throws Exception
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
		nombre = "GestorPlugins";
		jarFile = "ejemplo.jar";
		
		gestorPlugin = new GestorPlugins();
	}
	
	
	@Override
	public void start() throws Exception
	{
		// TODO Auto-generated method stub
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
