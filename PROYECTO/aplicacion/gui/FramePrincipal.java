package aplicacion.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;

import Deventos.enlaceJS.DConector;

import componentes.base.DJFrame;

/**
 * Ventana principal de la plataforma.
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class FramePrincipal extends DJFrame
{
	private static final long serialVersionUID = 3179403669427211858L;

	private BorderLayout borderLayout1 = new BorderLayout();

	private PanelPrincipal componente = null;

	/**
	 * Constructor
	 */
	public FramePrincipal()
	{
		super(false, "MousesRemotos");
		try
		{
			init();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Inicializa los componentes graficos
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception
	{
		this.getContentPane().setLayout(borderLayout1);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(true);
		this.addWindowListener(new FrameEjemplo_this_windowAdapter(this));

		this.setIconImage(new ImageIcon("Resources/logo.png").getImage());

		componente = new PanelPrincipal("Componente5", true, null);
		this.getContentPane().add(componente, BorderLayout.CENTER);

	}

	/**
	 * Accion ejecutada al cerrar la ventana. Se enviara una se–al al
	 * 
	 * @see DConector indicando que saldremos de la aplicacion.
	 * @param e
	 *            Evento recibido
	 */
	private void this_windowClosing(WindowEvent e)
	{
		componente.salir();
		DConector.obtenerDC().salir();
	}

	/**
	 * WindowAdapter que permite agregar eventos a la ventana
	 */
	private class FrameEjemplo_this_windowAdapter extends
			java.awt.event.WindowAdapter
	{
		private FramePrincipal adaptee;

		/**
		 * Constructor
		 * 
		 * @param adaptee
		 *            Ventana a la que agregar los eventos
		 */
		public FrameEjemplo_this_windowAdapter( FramePrincipal adaptee )
		{
			this.adaptee = adaptee;
		}

		/**
		 * Evento de cierre de la ventana
		 */
		@Override
		public void windowClosing(WindowEvent e)
		{
			adaptee.this_windowClosing(e);
		}
	}
}
