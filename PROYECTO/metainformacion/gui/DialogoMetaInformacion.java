package metainformacion.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import metainformacion.ClienteMetaInformacion;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class DialogoMetaInformacion extends JDialog
{

	private static final long serialVersionUID = 1L;

	Frame frame = null;

	BorderLayout borderLayout1 = new BorderLayout();

	PanelMetaInformacion jPanel1 = new PanelMetaInformacion();

	private JPanel content = null;

	private JPanel panelInferior = null;

	private JButton botonGuardar = null;

	private JButton botonSalir = null;

	public DialogoMetaInformacion( Frame frame, String title, boolean modal )
	{
		super(frame, title, modal);
		initialize();
		try
		{
			this.frame = frame;
			jbInit();
			pack();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		inicializar("");
	}

	public DialogoMetaInformacion()
	{
		this(null, "", false);
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize()
	{
		try
		{
			this.setContentPane(getContent()); // Generated

		}
		catch (java.lang.Throwable e)
		{
			// Do Something
		}
	}

	public void nuevoUsuario(String usuario)
	{
		jPanel1.nuevoUsuario(usuario);
	}

	public void nuevoRol(String rol)
	{
		jPanel1.nuevoRol(rol);
	}

	public void eliminarUsuario(String usuario)
	{
		jPanel1.eliminarUsuario(usuario);
	}

	public void eliminarRol(String rol)
	{
		jPanel1.eliminarRol(rol);
	}

	private void jbInit() throws Exception
	{

	}

	public void inicializar(String rol)
	{
		jPanel1.inicializar(rol);
	}

	@Override
	public void setVisible(boolean b)
	{
		if (b)
		{
			// int alturaFrame = frame.getSize().height;
			setSize(450, 500);
			setLocation(10, 10);
		}
		super.setVisible(b);
	}

	/**
	 * This method initializes content
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getContent()
	{
		if (content == null)
		{
			try
			{
				content = new JPanel();
				content.setLayout(new BorderLayout()); // Generated
				content.add(jPanel1, BorderLayout.CENTER); // Generated
				content.add(getPanelInferior(), BorderLayout.SOUTH); // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return content;
	}

	/**
	 * This method initializes panelInferior
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPanelInferior()
	{
		if (panelInferior == null)
		{
			try
			{
				GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
				gridBagConstraints1.gridx = 1; // Generated
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 0; // Generated
				gridBagConstraints.gridy = 0; // Generated
				panelInferior = new JPanel();
				panelInferior.setLayout(new GridBagLayout()); // Generated
				panelInferior.add(getBotonGuardar(), gridBagConstraints1); // Generated
				panelInferior.add(getBotonSalir(), gridBagConstraints); // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelInferior;
	}

	/**
	 * This method initializes botonGuardar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonGuardar()
	{
		if (botonGuardar == null)
		{
			try
			{
				botonGuardar = new JButton();
				botonGuardar.setText("Guardar"); // Generated
				botonGuardar.setIcon(new ImageIcon("Resources/disk.png")); // Generated
				botonGuardar
						.addActionListener(new java.awt.event.ActionListener()
						{
							public void actionPerformed(
									java.awt.event.ActionEvent e)
							{
								ClienteMetaInformacion.obtenerCMI()
										.guardarCambiosBD();
								setVisible(false);
							}
						});
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return botonGuardar;
	}

	/**
	 * This method initializes botonSalir
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonSalir()
	{
		if (botonSalir == null)
		{
			try
			{
				botonSalir = new JButton();
				botonSalir.setText("Salir"); // Generated
				botonSalir.setIcon(new ImageIcon("Resources/door_open.png")); // Generated
				botonSalir
						.addActionListener(new java.awt.event.ActionListener()
						{
							public void actionPerformed(
									java.awt.event.ActionEvent e)
							{
								setVisible(false);
							}
						});
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return botonSalir;
	}

}
