package aplicacion.gui;

import componentes.base.DJFrame;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import componentes.gui.usuarios.DICambioRol;

public class VentanaCambiarRol extends DJFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4130806553519520953L;
	private JPanel panelPrincipal = null;
	private DICambioRol DICambioRol = null;
	/**
	 * This method initializes 
	 * 
	 */
	public VentanaCambiarRol() {
		super(false, "");
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		try {
            this.setSize(new Dimension(366, 307));  // Generated
            this.setContentPane(getPanelPrincipal());  // Generated
            this.setTitle(".:: Cambiar Rol ::.");  // Generated
				
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}

	/**
	 * This method initializes panelPrincipal	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelPrincipal()
	{
		if (panelPrincipal == null)
		{
			try
			{
				panelPrincipal = new JPanel();
				panelPrincipal.setLayout(new BorderLayout());  // Generated
				panelPrincipal.add(getDICambioRol(), BorderLayout.CENTER);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelPrincipal;
	}

	/**
	 * This method initializes DICambioRol	
	 * 	
	 * @return componentes.gui.usuarios.DICambioRol	
	 */
	private DICambioRol getDICambioRol()
	{
		if (DICambioRol == null)
		{
			try
			{
				DICambioRol = new DICambioRol();
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return DICambioRol;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
