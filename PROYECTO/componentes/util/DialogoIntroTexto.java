package componentes.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Dialogo para la introduccion de un texto
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DialogoIntroTexto extends JDialog
{
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel jPanel = null;

	private JButton botonCancelar = null;

	private JButton botonAceptar = null;

	private JScrollPane jScrollPane = null;

	private JTextArea jTextArea = null;

	private String texto = "";

	private JPanel jPanel1 = null;

	/**
	 * Constructor
	 * 
	 * @param owner
	 *            Ventana padre del dialogo
	 */
	public DialogoIntroTexto( Frame owner )
	{
		super(owner);
		initialize();
	}

	/**
	 * Inicializa el dialogo
	 */
	private void initialize()
	{
		this.setSize(324, 199);
		this.setResizable(false);
		this.setContentPane(getJContentPane());
	}

	private JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(1);
			borderLayout.setVgap(1);
			jContentPane = new JPanel();
			jContentPane.setLayout(borderLayout);
			jContentPane.add(getJPanel(), BorderLayout.SOUTH);
			jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	private JPanel getJPanel()
	{
		if (jPanel == null)
		{
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add(getJPanel1(), BorderLayout.EAST);
		}
		return jPanel;
	}

	private JButton getBotonCancelar()
	{
		if (botonCancelar == null)
		{
			botonCancelar = new JButton();
			botonCancelar.setText("Cancelar");

			botonCancelar.setIcon(new ImageIcon("Resources/cancel.png"));

			botonCancelar.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					texto = null;
					setVisible(false);
				}
			});
		}
		return botonCancelar;
	}

	private JButton getBotonAceptar()
	{
		if (botonAceptar == null)
		{
			botonAceptar = new JButton();
			botonAceptar.setText("Aceptar");

			botonAceptar.setIcon(new ImageIcon("Resources/tick.png"));

			botonAceptar.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					texto = jTextArea.getText();
					setVisible(false);
				}
			});
		}
		return botonAceptar;
	}

	private JScrollPane getJScrollPane()
	{
		if (jScrollPane == null)
		{
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTextArea());
		}
		return jScrollPane;
	}

	private JTextArea getJTextArea()
	{
		if (jTextArea == null)
		{
			jTextArea = new JTextArea();
			jTextArea.addKeyListener(new java.awt.event.KeyAdapter()
			{
				@Override
				public void keyTyped(java.awt.event.KeyEvent e)
				{
					if (e.getKeyChar() == '\t') botonAceptar.requestFocus();
				}
			});
		}
		return jTextArea;
	}

	private JPanel getJPanel1()
	{
		if (jPanel1 == null)
		{
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(4, 4, 4, 4);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.ipadx = 0;
			gridBagConstraints.gridwidth = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.NONE;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.ipadx = 0;
			gridBagConstraints1.insets = new Insets(4, 4, 4, 4);
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(getBotonAceptar(), gridBagConstraints1);
			jPanel1.add(getBotonCancelar(), gridBagConstraints);
		}
		return jPanel1;
	}

	/**
	 * Obtiene el texto introducido
	 * 
	 * @return Texto introducido
	 */
	public String obtenerTexto()
	{
		setTitle("Introduce el texto");

		setSize(330, 200);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		setLocation(( screenSize.width - frameSize.width ) / 2,
				( screenSize.height - frameSize.height ) / 2);

		this.setModal(true);

		setVisible(true);

		return texto;

	}
}
