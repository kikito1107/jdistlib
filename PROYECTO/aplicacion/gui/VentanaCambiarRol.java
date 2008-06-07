package aplicacion.gui;

import componentes.base.DJFrame;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import componentes.gui.usuarios.DICambioRol;

/**
 * Ventana que permite realizar el cambio de rol de un usuario
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class VentanaCambiarRol extends DJFrame
{
	private static final long serialVersionUID = 4130806553519520953L;

	private JPanel panelPrincipal = null;

	private DICambioRol DICambioRol = null;

	/**
	 * Constructor
	 */
	public VentanaCambiarRol()
	{
		super(false, "");
		initialize();
	}

	/**
	 * Inicializacion de los componentes
	 */
	private void initialize()
	{
		try
		{
			this.setSize(new Dimension(366, 307)); // Generated
			this.setContentPane(getPanelPrincipal()); // Generated
			this.setTitle(".:: Cambiar Rol ::."); // Generated

		}
		catch (java.lang.Throwable e)
		{
		}
	}

	/**
	 * Obtiene el panel principal
	 * 
	 * @return Panel principal de la ventana ya inicializado
	 */
	private JPanel getPanelPrincipal()
	{
		if (panelPrincipal == null)
		{
			try
			{
				panelPrincipal = new JPanel();
				panelPrincipal.setLayout(new BorderLayout()); // Generated
				panelPrincipal.add(getDICambioRol(), BorderLayout.CENTER); // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelPrincipal;
	}

	/**
	 * Obtiene el panel que contiene los roles permitidos para un usuario, asi
	 * como un boton para cambiar entre estos roles.
	 * 
	 * @return Panel con la lista de roles permitidos para el usuario y el boton
	 *         para cambiar entre roles.
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

}
