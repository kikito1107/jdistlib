package aplicacion.plugin.example;

import java.awt.BorderLayout;

import componentes.base.DComponenteBase;
import componentes.base.DJFrame;
import componentes.gui.DIChat;
import componentes.gui.usuarios.ArbolUsuariosConectadosRol;

import Deventos.enlaceJS.DConector;
import aplicacion.plugin.DAbstractPlugin;

public class DChatPlugin extends DAbstractPlugin
{
	
	
	private DJFrame chat = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6310087937591625336L;

	public DChatPlugin(  ) throws Exception
	{
		super("chatplugin", false, null);
		chat = new DJFrame(false,"");
		init();
		// TODO Auto-generated constructor stub
	}

	@Override
	public DAbstractPlugin getInstance() throws Exception
	{
		return new DChatPlugin();
	}

	@Override
	public void init() throws Exception
	{
		// TODO Auto-generated method stub
		version = 5;
		nombre = "Chat";
		jarFile = "ejemplo.jar";
		chat.getContentPane().setLayout(new BorderLayout());
		chat.getContentPane().add(new DIChat("chat", true, null), BorderLayout.CENTER);
		chat.getContentPane().add(new ArbolUsuariosConectadosRol(
				"ListaUsuariosConectadosRol", true, null), BorderLayout.WEST);
		chat.setTitle(":: Chat ::");
	}

	@Override
	public void start() throws Exception
	{
		// TODO Auto-generated method stub
		chat.pack();
		chat.setSize(500, 430);
		
		chat.setLocationRelativeTo(null);
		
		chat.setVisible(true);
	}

	@Override
	public void stop() throws Exception
	{
		// TODO Auto-generated method stub
		chat.dispose();
	}

}
