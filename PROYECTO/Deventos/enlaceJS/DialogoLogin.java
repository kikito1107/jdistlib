package Deventos.enlaceJS;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Dialogo mostrado al inicio para introducir el nombre de usuario y password
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DialogoLogin extends JDialog
{
	private static final long serialVersionUID = 8430574155310023329L;

	private JPanel panel1 = new JPanel();

	private String[] datosLogin = new String[2];

	private JTextField campoUsuario = new JTextField();

	private JTextField campoPass = new JTextField();

	private JLabel jLabel1 = new JLabel();

	private JLabel jLabel2 = new JLabel();

	private JLabel jLabel3 = new JLabel();

	private JButton botonAceptar = new JButton();

	private JButton botonCancelar = new JButton();

	/**
	 * Constructor por defecto
	 */
	public DialogoLogin()
	{
		this(null, "", false);
	}

	/**
	 * Constructor con parametros
	 * 
	 * @param frame
	 *            Ventana asociada al dialogo de login
	 * @param title
	 *            Titulo del dialogo
	 * @param modal
	 *            Indicar si es un dialogo modal o no
	 */
	public DialogoLogin( Frame frame, String title, boolean modal )
	{
		super(frame, title, modal);
		try
		{
			jbInit();
			pack();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Inicializacion de los componentes graficos
	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception
	{
		panel1.setLayout(null);
		campoUsuario.setToolTipText("");
		campoUsuario.setText("User Name");
		campoUsuario.setBounds(new Rectangle(120, 41, 171, 20));
		campoPass.setText("clave");
		campoPass.setBounds(new Rectangle(120, 66, 171, 20));
		jLabel1.setFont(new java.awt.Font("Dialog", 1, 11));
		jLabel1.setText("Usuario:");
		jLabel1.setBounds(new Rectangle(22, 44, 65, 15));
		jLabel2.setFont(new java.awt.Font("Dialog", 1, 11));
		jLabel2.setText("Password:");
		jLabel2.setBounds(new Rectangle(22, 69, 81, 15));
		jLabel3.setFont(new java.awt.Font("Dialog", 1, 11));
		jLabel3.setText("Introduzca su nombre de usuario y password");
		jLabel3.setBounds(new Rectangle(22, 16, 290, 15));
		botonAceptar.setBounds(new Rectangle(70, 100, 84, 23));
		botonAceptar.setText("Aceptar");
		botonAceptar
				.addActionListener(new DialogoLogin_botonAceptar_actionAdapter(
						this));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setModal(true);
		this.setResizable(false);
		this.addWindowListener(new DialogoLogin_this_windowAdapter(this));
		botonCancelar.setBounds(new Rectangle(178, 100, 96, 23));
		botonCancelar.setText("Cancelar");
		botonCancelar
				.addActionListener(new DialogoLogin_jButton1_actionAdapter(this));
		getContentPane().add(panel1);
		panel1.add(jLabel1, null);
		panel1.add(campoUsuario, null);
		panel1.add(jLabel2, null);
		panel1.add(campoPass, null);
		panel1.add(jLabel3, null);
		panel1.add(botonAceptar, null);
		panel1.add(botonCancelar, null);

		campoUsuario.select(0, campoUsuario.getText().length());

		this.getRootPane().setDefaultButton(botonAceptar);
	}

	/**
	 * Permite obtener un vector con los datos introducidos por el usuario
	 * 
	 * @return Array con dos cadenas de texto: El usuario y la clave
	 *         introducidos
	 */
	public String[] obtenerDatosLogin()
	{
		setSize(330, 200);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		setLocation(( screenSize.width - frameSize.width ) / 2,
				( screenSize.height - frameSize.height ) / 2);

		campoUsuario.select(0, campoUsuario.getText().length());
		setVisible(true);
		return datosLogin;
	}

	/**
	 * Accion realizada al pulsar el boton aceptar
	 * 
	 * @param e
	 *            Evento recibido
	 */
	private void botonAceptar_actionPerformed(ActionEvent e)
	{
		if (( campoUsuario.getText().length() > 0 )
				&& ( campoPass.getText().length() > 0 ))
		{
			datosLogin[0] = campoUsuario.getText();
			datosLogin[1] = campoPass.getText();
			setVisible(false);
		}
		else JOptionPane.showMessageDialog(null,
				"No debes dejar ningún campo vacío", "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Accion realizada al pulsar el boton cancelar
	 * 
	 * @param e
	 *            Evento recibido
	 */
	private void botonCancelar_actionPerformed(ActionEvent e)
	{
		// DConector.obtenerDC().salir();
		System.exit(0);
	}

	/**
	 * Accion realizada al cerrarse la ventana
	 * 
	 * @param e
	 *            Evento recibido
	 */
	private void this_windowClosing(WindowEvent e)
	{
		// DConector.obtenerDC().salir();
		System.exit(0);
	}

	/*
	 * **************************************** Adaptadores para las acciones
	 * ****************************************
	 */

	private class DialogoLogin_botonAceptar_actionAdapter implements
			java.awt.event.ActionListener
	{
		private DialogoLogin adaptee;

		public DialogoLogin_botonAceptar_actionAdapter( DialogoLogin adaptee )
		{
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e)
		{
			adaptee.botonAceptar_actionPerformed(e);
		}
	}

	private class DialogoLogin_jButton1_actionAdapter implements
			java.awt.event.ActionListener
	{
		private DialogoLogin adaptee;

		public DialogoLogin_jButton1_actionAdapter( DialogoLogin adaptee )
		{
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e)
		{
			adaptee.botonCancelar_actionPerformed(e);
		}
	}

	private class DialogoLogin_this_windowAdapter extends
			java.awt.event.WindowAdapter
	{
		private DialogoLogin adaptee;

		public DialogoLogin_this_windowAdapter( DialogoLogin adaptee )
		{
			this.adaptee = adaptee;
		}

		@Override
		public void windowClosing(WindowEvent e)
		{
			adaptee.this_windowClosing(e);
		}
	}
}
