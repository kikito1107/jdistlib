/**
 * 
 */
package beans;

import Deventos.enlaceJS.DConector;
import componentes.base.DJFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.BorderLayout;
import componentes.gui.usuarios.DICambioRol;

/**
 * @author anab
 *
 */
public class GestorRoles extends DJFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3982683797632001181L;
	private JPanel PanelPrincipal = null;
	/**
	 * @param mostrarPunterosRemotos
	 * @param nombreGestorMousesRemotos
	 */
	public GestorRoles( boolean mostrarPunterosRemotos,
			String nombreGestorMousesRemotos )
	{
		super(mostrarPunterosRemotos, nombreGestorMousesRemotos);
		initialize();
		this.setTitle(":: Gestor Roles ::");
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public GestorRoles()
	{
		super(false, "");
		
		// TODO Auto-generated constructor stub
		initialize();
		this.setTitle(":: Gestor Roles ::");
	}
		

	/**
	 * @param nombreAplicacion
	 * @param nombreJS
	 * @param mostrarPunterosRemotos
	 * @param nombreGestorMousesRemotos
	 */
	public GestorRoles( String nombreAplicacion, String nombreJS,
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
            this.setTitle(":: Gestor Roles ::");  // Generated
            this.setContentPane(getPanelPrincipal());  // Generated
            this.setSize(new Dimension(403, 376));  // Generated
				
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}

	/**
	 * This method initializes PanelPrincipal	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelPrincipal()
	{
		if (PanelPrincipal == null)
		{
			try
			{
				PanelPrincipal = new JPanel();
				PanelPrincipal.setLayout(new BorderLayout());  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return PanelPrincipal;
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

		GestorRoles frame = new GestorRoles();

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

		String nombreUsuario = DConector.Dusuario;

	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
