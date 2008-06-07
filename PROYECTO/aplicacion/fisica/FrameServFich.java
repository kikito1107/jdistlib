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

/**
 * Ventana para servidor de ficheros
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class FrameServFich extends JFrame
{
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JScrollPane scroll = null;

	private static JTextArea consola = new JTextArea();

	private JPanel panelSur = null;

	private JButton botonSalir = null;

	/**
	 * Constructor por defecto
	 */
	public FrameServFich()
	{
		super();
		initialize();
	}

	/**
	 * Metodo que inicializa los componentes graficos de la ventana
	 */
	private void initialize()
	{
		consola.setBackground(Color.black); // Generated
		consola.setForeground(Color.green); // Generated
		this.setSize(419, 322);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Generated
		this.setContentPane(getJContentPane());
		this.setTitle(".:: Servidor Ficheros ::.");
	}

	/**
	 * Metodo que inicializa el panel contenedor de la ventana.
	 * 
	 * @return JPanel que actuara como panel contenedor (ContentPane).
	 */
	private JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getScroll(), BorderLayout.CENTER); // Generated
			jContentPane.add(getPanelSur(), BorderLayout.SOUTH); // Generated
		}
		return jContentPane;
	}

	/**
	 * Metodo que inicializa un panel con scroll para el area de texto donde se
	 * mostraran los mensajes recibidos por el servidor de ficheros.
	 * 
	 * @return JScrollPane para la ventana de mensajes
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
	 * Area de texto para mostrar los mensajes recibidos por el servidor de
	 * ficheros
	 * 
	 * @return Area de texto inicializada
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

	/**
	 * Panel donde se situara un boton para poder salir del servidor de
	 * ficheros.
	 * 
	 * @return JPanel inicializado
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
	 * Pinta una cadena de texto al final de la consola
	 * 
	 * @param s
	 *            la cadena a pintar
	 */
	public static void println(String s)
	{
		consola.append(s + "\n");
	}

	/**
	 * Metodo que inicializa el boton que permite salir del servidor de ficheros
	 * 
	 * @return JButton ya inicializado
	 */
	private JButton getBotonSalir()
	{
		if (botonSalir == null)
		{
			try
			{
				botonSalir = new JButton();
				botonSalir.setIcon(new ImageIcon("Resources/door_open.png")); // Generated
				botonSalir.setText("Salir"); // Generated
				botonSalir
						.addActionListener(new java.awt.event.ActionListener()
						{
							public void actionPerformed(
									java.awt.event.ActionEvent e)
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

}
