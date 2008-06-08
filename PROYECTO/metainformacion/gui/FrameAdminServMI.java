package metainformacion.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import metainformacion.ServidorMetaInformacion;

/**
 * Frame que se abre al iniciar el servidor de Metainformacion
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */

public class FrameAdminServMI extends JFrame
{
	private static final long serialVersionUID = 1L;

	BorderLayout borderLayout1 = new BorderLayout();

	ServidorMetaInformacion smi = new ServidorMetaInformacion();

	private JPanel panel = null;

	private JPanel panelSur = null;

	private JButton botonSalir = null;

	private JScrollPane scroll = null;

	public static JTextArea consola = new JTextArea();

	public FrameAdminServMI()
	{
		try
		{
			jbInit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception
	{
		this.getContentPane().setLayout(borderLayout1);
		consola.setBackground(Color.black); // Generated
		consola.setForeground(Color.green); // Generated
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(getPanel()); // Generated
		this.setSize(new Dimension(352, 298)); // Generated
		this.setResizable(false);
		this.setTitle(".:: Servidor MetaInformacion ::.");
	}

	void botonCerrar_actionPerformed(ActionEvent e)
	{
		System.exit(0);
	}

	/**
	 * This method initializes panel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPanel()
	{
		if (panel == null)
		{
			try
			{
				panel = new JPanel();
				panel.setLayout(new BorderLayout()); // Generated
				panel.add(getPanelSur(), BorderLayout.SOUTH); // Generated
				panel.add(getScroll(), BorderLayout.CENTER); // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panel;
	}

	/**
	 * This method initializes panelSur
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPanelSur()
	{
		if (panelSur == null)
		{
			try
			{
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 0; // Generated
				gridBagConstraints.gridy = 0; // Generated
				panelSur = new JPanel();
				panelSur.setLayout(new GridBagLayout()); // Generated
				panelSur.add(getBotonSalir(), gridBagConstraints); // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelSur;
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
								Object[] etiquetas = new Object[]
								{ "Salir", "Cancelar" };
								int opcion = JOptionPane
										.showOptionDialog(
												null,
												"Si sale del servidor debera reiniciar los servicios de JINI\n"
														+ "para que el funcionamiento del sistema cuando vuelva a iniciarlo\n"
														+ "sea el esperado. Disculpe las molestias.",
												"Aviso",
												JOptionPane.YES_NO_OPTION,
												JOptionPane.WARNING_MESSAGE,
												null, etiquetas, etiquetas[0]);
								if (opcion == JOptionPane.YES_OPTION)
								{
									System.exit(0);
								}
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

	/**
	 * This method initializes scroll
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScroll()
	{
		if (scroll == null)
		{
			try
			{
				scroll = new JScrollPane();
				scroll.setViewportView(getConsola()); // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return scroll;
	}

	/**
	 * This method initializes consola
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getConsola()
	{
		if (consola == null)
		{
			try
			{
				consola = new JTextArea();
				consola.setBackground(Color.black); // Generated
				consola.setForeground(Color.green); // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return consola;
	}

	public static void println(String s)
	{
		consola.append(s + "\n");
	}
} // @jve:decl-index=0:visual-constraint="10,10"
