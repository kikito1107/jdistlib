package aplicacion.fisica;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class FrameServFich extends JFrame
{

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JScrollPane scroll = null;

	private static JTextArea consola = new JTextArea();

	private JPanel panelSur = null;

	private JButton botonSalir = null;

	/**
	 * This is the default constructor
	 */
	public FrameServFich()
	{
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		consola.setBackground(Color.black);  // Generated
		consola.setForeground(Color.green);  // Generated
		this.setSize(419, 322);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Generated
		this.setContentPane(getJContentPane());
		this.setTitle(".:: Servidor Ficheros ::.");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getScroll(), BorderLayout.CENTER);  // Generated
			jContentPane.add(getPanelSur(), BorderLayout.SOUTH);  // Generated
		}
		return jContentPane;
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
				scroll.setViewportView(getConsola());  // Generated
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
				consola.setBackground(Color.black);  // Generated
				consola.setForeground(Color.green);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return consola;
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
				gridBagConstraints.gridx = 0;  // Generated
				gridBagConstraints.gridy = 0;  // Generated
				panelSur = new JPanel();
				panelSur.setLayout(new GridBagLayout());  // Generated
				panelSur.add(getBotonSalir(), gridBagConstraints);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelSur;
	}

	public static void println(String s ){
		consola.append(s + "\n");
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
				botonSalir.setIcon(new ImageIcon(getClass().getResource("Resources/door_open.png")));  // Generated
				botonSalir.setText("Salir");  // Generated
				botonSalir.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent e)
					{
						System.exit(0);
					}
				});
			}
			catch (java.lang.Throwable e)
			{
				System.exit(1);
			}
		}
		return botonSalir;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
