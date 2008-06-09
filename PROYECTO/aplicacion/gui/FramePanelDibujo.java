package aplicacion.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.WindowEvent;

import Deventos.enlaceJS.DConector;

import componentes.base.DComponente;
import componentes.base.DJFrame;
import componentes.gui.visualizador.DIPanelEditor;
import fisica.documentos.Documento;

/**
 * Ventana con un panel de dibujo de la clase @see DIPanelEditor
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class FramePanelDibujo extends DJFrame
{
	private static final long serialVersionUID = 84713594362863139L;

	private BorderLayout borderLayout1 = new BorderLayout();

	private DIPanelEditor lienzo = null;

	private static boolean standalone = false;

	/**
	 * Constructor
	 * @param sa Indica si la ventana funcionara como
	 * 			ventana de una aplicacion completa o si
	 * 			sera parte de otra aplicacion (sera una
	 * 			ventana lanzada por otros componentes graficos)
	 */
	public FramePanelDibujo( boolean sa )
	{
		super(true, "MousesRemotos2");
		standalone = sa;
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
	 * @throws Exception
	 */
	private void init() throws Exception
	{
		this.getContentPane().setLayout(borderLayout1);
		if (!standalone)
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		else this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.addWindowListener(new FrameDrawPanel_this_windowAdapter(this));
		lienzo = new DIPanelEditor("panelDibujo", true, null, this);
		this.getContentPane().add(lienzo, BorderLayout.CENTER);

		this.setTitle(".:: Panel Edición Compartido : " + DConector.Dusuario
				+ " ::.");
	}

	/**
	 * Accion que se produce al cerrar una ventana
	 * @param e Evento recibido
	 */
	public void alCerrarVentana(WindowEvent e)
	{

		DConector.obtenerDC()
				.cerrarFichero(this.getLienzo().getPathDocumento());

		if (standalone)
			DConector.obtenerDC().salir();

		else
		{
			setDocumento(new Documento());
			this.lienzo.setPathDocumento("");
			setVisible(false);
			System.gc();
		}
	}

	@Override
	public void setVisible(boolean b)
	{
		super.setVisible(b);
		
		repaint();
		validate();
		lienzo.repaint();

		lienzo.validate();
	}

	/**
	 * Obtiene el lienzo sobre el que se pinta
	 * @return Lienzo sobre el que se pinta
	 */
	public DIPanelEditor getLienzo()
	{
		return lienzo;
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
	}

	/**
	 * Asigna un documento para su edicion compartida
	 * @param d Documento a editar
	 */
	public void setDocumento(Documento d)
	{
		lienzo.setDocumento(d);

	}

	/**
	 * Obtiene el numero de componentes hijos
	 * @return Numero de componentes hijo
	 */
	public int obtenerNumComponentesHijos()
	{
		return 1;
	}

	/**
	 * Obtiene el componente i-esimo
	 * @param i Indice del componente a obtener
	 * @return Componente indicado o null si el componente no existe
	 */
	public DComponente obtenerComponente(int i)
	{
		DComponente dc = null;
		switch (i)
		{
			case 0:
				dc = this.lienzo;
				break;
			default:
				break;
		}
		return dc;
	}
	
	private class FrameDrawPanel_this_windowAdapter extends java.awt.event.WindowAdapter
	{
		private FramePanelDibujo adaptee;

		public FrameDrawPanel_this_windowAdapter( FramePanelDibujo adaptee )
		{
			this.adaptee = adaptee;
		}

		@Override
		public void windowClosing(WindowEvent e)
		{
			adaptee.alCerrarVentana(e);
		}
	}
}
