/**
 * 
 */
package beans;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import Deventos.enlaceJS.DConector;

import componentes.base.DJFrame;
import componentes.gui.DIButton;
import componentes.gui.usuarios.ArbolUsuariosConectadosRol;
import java.awt.Rectangle;
import java.awt.BorderLayout;
import componentes.gui.DIChat;
import componentes.gui.DIComboBox;

/**
 * @author anab
 *
 */
public class PruebaBeans extends DJFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8563413483894566833L;
	private JPanel Panel = null;
	private ArbolUsuariosConectadosRol arbol = null;
	private DIChat Chat = null;
	private DIComboBox DIComboBox = null;
	private JPanel paneInferior = null;
	
	/**
	 * @param mostrarPunterosRemotos mostrar puenteros remotos
	 * @param nombreGestorMousesRemotos nombre del gestor de punteros remotos
	 */
	public PruebaBeans( boolean mostrarPunterosRemotos,
			String nombreGestorMousesRemotos )
	{
		super(mostrarPunterosRemotos, nombreGestorMousesRemotos);
		initialize();
	}

	/**
	 * 
	 */
	public PruebaBeans()
	{
		super(false, "");

		// TODO Auto-generated constructor stub
		initialize();
	}
		

	/**
	 * @param nombreAplicacion
	 * @param nombreJs nombre del Java Space
	 * @param mostrarPunterosRemotos
	 * @param nombreGestorMousesRemotos
	 */
	public PruebaBeans( String nombreAplicacion, String nombreJS,
			boolean mostrarPunterosRemotos, String nombreGestorMousesRemotos )
	{
		super(nombreAplicacion, nombreJS, mostrarPunterosRemotos,
				nombreGestorMousesRemotos);
		initialize();
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		try {
            this.setSize(new Dimension(394, 293));  // Generated
            this.setTitle("Prueba Beans");  // Generated
            this.setContentPane(getPanel());  // Generated
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
            this.addWindowListener(new java.awt.event.WindowAdapter()
            {
            	public void windowClosing(java.awt.event.WindowEvent e)
            	{
            		DConector.obtenerDC().salir();
            	}
            });
				
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}

	/**
	 * This method initializes Panel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanel()
	{
		if (Panel == null)
		{
			try
			{
				Panel = new JPanel();
				Panel.setLayout(new BorderLayout());  // Generated
				Panel.add(getArbol(), BorderLayout.WEST);  // Generated
				Panel.add(getPaneInferior(), BorderLayout.SOUTH);  // Generated
				Panel.add(getChat(), BorderLayout.CENTER);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return Panel;
	}

	/**
	 * This method initializes arbol	
	 * 	
	 * @return componentes.gui.usuarios.ArbolUsuariosConectadosRol	
	 */
	private ArbolUsuariosConectadosRol getArbol()
	{
		if (arbol == null)
		{
			try
			{
				arbol = new ArbolUsuariosConectadosRol("Arbol", true, null);
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return arbol;
	}

	/**
	 * This method initializes Chat	
	 * 	
	 * @return componentes.gui.DIChat	
	 */
	private DIChat getChat()
	{
		if (Chat == null)
		{
			try
			{
				Chat = new DIChat("chat", true, null);
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return Chat;
	}

	/**
	 * This method initializes DIComboBox	
	 * 	
	 * @return componentes.gui.DIComboBox	
	 */
	private DIComboBox getDIComboBox()
	{
		if (DIComboBox == null)
		{
			try
			{
				String[] elementos = {"A", "B","C","D","E"};
				
				DIComboBox = new DIComboBox(elementos, "comboBox", true, null);
				DIComboBox.setName("DIComboBox");  // Generated
				DIComboBox.setToolTipText("Combo Box la mar de mono");  // Generated
				
				
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return DIComboBox;
	}

	/**
	 * This method initializes paneInferior	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPaneInferior()
	{
		if (paneInferior == null)
		{
			try
			{
				paneInferior = new JPanel();
				paneInferior.setLayout(new GridBagLayout());  // Generated
				paneInferior.add(getDIComboBox(), new GridBagConstraints());  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return paneInferior;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		try
		{
			UIManager.setLookAndFeel("lookandfeel.Dmetal.MetalLookAndFeel");
			// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Error al inicializar la GUI");
			System.exit(-1);
		}

		boolean packFrame = false;
		
		DConector d = new DConector("AplicacionDePrueba");
		d.inicializar();

		PruebaBeans frame = new PruebaBeans();

		if (packFrame)
			frame.pack();
		else frame.validate();

		frame.setSize(568, 545);
		d.sincronizarComponentes();

		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		frame.setLocation(( screenSize.width - frameSize.width ) / 2,
				( screenSize.height - frameSize.height ) / 2);
		frame.setVisible(true);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
