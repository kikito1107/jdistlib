package awareness.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import awareness.ClienteMetaInformacion;

/**
 * Dialogo encargado de modificar la metainformacion del sistema. Solo deberan
 * acceder a este dialogo los usuarios con permiso de administrador
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DialogoMetaInformacion extends JDialog
{
	private static final long serialVersionUID = 1L;

	private PanelMetaInformacion jPanel1 = new PanelMetaInformacion();

	private JPanel content = null;

	private JPanel panelInferior = null;

	private JButton botonGuardar = null;

	private JButton botonSalir = null;

	/**
	 * Constructor por defecto
	 */
	public DialogoMetaInformacion()
	{
		this(null, "", false);
	}

	/**
	 * Constructor con parametros
	 * 
	 * @param frame
	 *            Ventana padre del dialogo
	 * @param title
	 *            Titulo del dialogo
	 * @param modal
	 *            Indica si es un dialogo modal o no
	 */
	public DialogoMetaInformacion( Frame frame, String title, boolean modal )
	{
		super(frame, title, modal);
		initialize();
		try
		{
			pack();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		inicializar("");
	}

	/**
	 * Inicializacion de dialogo
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

	/**
	 * Pone un nuevo usuario en el panel
	 * 
	 * @param usuario
	 *            Nombre del usuario
	 */
	public void nuevoUsuario(String usuario)
	{
		jPanel1.nuevoUsuario(usuario);
	}

	/**
	 * Pone un nuevo rol en el panel
	 * 
	 * @param rol
	 *            Nombre del rol
	 */
	public void nuevoRol(String rol)
	{
		jPanel1.nuevoRol(rol);
	}

	/**
	 * Elimina un usuario del panel
	 * 
	 * @param usuario
	 *            Nombre del usuario
	 */
	public void eliminarUsuario(String usuario)
	{
		jPanel1.eliminarUsuario(usuario);
	}

	/**
	 * Elimina un rol del panel
	 * 
	 * @param rol
	 *            Nombre del rol
	 */
	public void eliminarRol(String rol)
	{
		jPanel1.eliminarRol(rol);
	}

	/**
	 * Inicializa un nuevo rol en el panel
	 * 
	 * @param rol
	 *            Nombre del rol
	 */
	public void inicializar(String rol)
	{
		jPanel1.inicializar(rol);
	}

	@Override
	public void setVisible(boolean b)
	{
		if (b)
		{
			setSize(450, 500);
			setLocation(10, 10);
		}
		super.setVisible(b);
	}

	/**
	 * Inicializa el panel completo de contenido, agregandole el panel con la
	 * informacion y el panel inferior que permite salir y guardar
	 * 
	 * @return Panel inicializado
	 */
	private JPanel getContent()
	{
		if (content == null)
		{
			try
			{
				content = new JPanel();
				content.setLayout(new BorderLayout());
				content.add(jPanel1, BorderLayout.CENTER);
				content.add(getPanelInferior(), BorderLayout.SOUTH);
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return content;
	}

	/**
	 * Obtiene el panel inferior con el boton de guardar y el boton de salir
	 * 
	 * @return Panel ya inicializado
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
										.guardarCambios();
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
