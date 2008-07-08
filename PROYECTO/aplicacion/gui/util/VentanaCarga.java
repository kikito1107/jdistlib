package aplicacion.gui.util;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Dialogo que se muestra al cargar la aplicacion
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class VentanaCarga extends JFrame
{
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JLabel etiquetaIcono = null;

	protected ImageIcon icono1 = null;

	protected ImageIcon icono2 = null;

	protected ImageIcon icono3 = null;

	/**
	 * Constructor
	 */
	public VentanaCarga()
	{
		super();

		initialize();
		Thread hp = new Thread(new HebraParpadeo(this));
		hp.start();
	}

	/**
	 * Inicializacion de los componentes graficos
	 */
	private void initialize()
	{
		this.setSize(330, 85);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setContentPane(getJContentPane());

		icono1 = new ImageIcon("Resources/logo_1.png");
		icono2 = new ImageIcon("Resources/logo_2.png");
		icono3 = new ImageIcon("Resources/logo_3.png");
	}

	/**
	 * Muesta el dialogo de carga
	 * 
	 * @param titulo
	 *            Titulo del dialogo
	 * @param mensaje
	 *            Mensaje a mostrar en el dialogo
	 * @param parpadeo
	 *            Indica si deseamos que se produzca un parpadeo en el logotipo
	 *            o no.
	 */
	public void mostrar(String titulo, String mensaje, boolean parpadeo)
	{
		setTitle(titulo);
		etiquetaIcono.setText(mensaje);
		etiquetaIcono.setIcon(icono3);
		setLocationRelativeTo(null);
		if (!isVisible())
		{
			// this.setLocation(x, y);
			this.setVisible(true);
		}
	}

	/**
	 * Permite ocultar el dialogo
	 */
	public void ocultar()
	{
		this.setVisible(false);
	}

	/**
	 * Obtiene el panel contenedor del dialogo
	 * 
	 * @return Panel contenedor ya inicializado
	 */
	private JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(9); // Generated
			borderLayout.setVgap(9); // Generated
			etiquetaIcono = new JLabel();
			etiquetaIcono.setText("Mensaje..."); // Generated
			etiquetaIcono.setIcon(new ImageIcon("Resources/logo_1.png")); // Generated
			jContentPane = new JPanel();
			jContentPane.setLayout(borderLayout); // Generated
			jContentPane.add(etiquetaIcono, BorderLayout.CENTER); // Generated
		}
		return jContentPane;
	}

	/**
	 * Hebra que permite crear el efecto de parpadeo sobre el logotipo
	 */
	private class HebraParpadeo implements Runnable
	{
		private VentanaCarga vc = null;

		/**
		 * Constructor
		 * 
		 * @param v
		 *            Ventana de carga a la que vamos a realizar el parpadeo del
		 *            logotipo
		 */
		public HebraParpadeo( VentanaCarga v )
		{
			vc = v;
		}

		/**
		 * Ejecucion de la hebra
		 */
		public void run()
		{
			while (true)
				try
				{
					vc.etiquetaIcono.setIcon(icono1);
					vc.repaint();
					Thread.sleep(150L);

					vc.etiquetaIcono.setIcon(icono2);
					vc.repaint();
					Thread.sleep(150L);

					vc.etiquetaIcono.setIcon(icono3);
					vc.repaint();
					Thread.sleep(150L);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

		}
	}
}
