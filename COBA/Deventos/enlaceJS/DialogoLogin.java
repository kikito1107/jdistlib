package Deventos.enlaceJS;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Dialogo mostrado al inicio para introducir el nombre de usuario y password
 */

public class DialogoLogin
	 extends JDialog {
  /**
	 * 
	 */
	private static final long serialVersionUID = 8430574155310023329L;
JPanel panel1 = new JPanel();
  private String[] datosLogin = new String[2];
  JTextField campoUsuario = new JTextField();
  JTextField campoPass = new JTextField();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JButton botonAceptar = new JButton();
  JButton jButton1 = new JButton();

  public DialogoLogin(Frame frame, String title, boolean modal) {
	 super(frame, title, modal);
	 try {
		jbInit();
		pack();
	 }
	 catch (Exception ex) {
		ex.printStackTrace();
	 }
  }

  public DialogoLogin() {
	 this(null, "", false);
  }

  private void jbInit() throws Exception {
	 panel1.setLayout(null);
	 campoUsuario.setToolTipText("");
	 campoUsuario.setText("DarthVader");
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
	 botonAceptar.addActionListener(new DialogoLogin_botonAceptar_actionAdapter(this));
	 this.setDefaultCloseOperation(3);
	 this.setModal(true);
	 this.setResizable(false);
	 this.addWindowListener(new DialogoLogin_this_windowAdapter(this));
	 jButton1.setBounds(new Rectangle(178, 100, 96, 23));
	 jButton1.setText("Cancelar");
	 jButton1.addActionListener(new DialogoLogin_jButton1_actionAdapter(this));
	 getContentPane().add(panel1);
	 panel1.add(jLabel1, null);
	 panel1.add(campoUsuario, null);
	 panel1.add(jLabel2, null);
	 panel1.add(campoPass, null);
	 panel1.add(jLabel3, null);
	 panel1.add(botonAceptar, null);
	 panel1.add(jButton1, null);

	 campoUsuario.select(0, campoUsuario.getText().length());
	 
	 this.getRootPane().setDefaultButton(botonAceptar);
  }

  public String[] obtenerDatosLogin() {
	 setSize(330, 200);
	 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	 Dimension frameSize = getSize();
	 if (frameSize.height > screenSize.height) {
		frameSize.height = screenSize.height;
	 }
	 if (frameSize.width > screenSize.width) {
		frameSize.width = screenSize.width;
	 }
	 setLocation( (screenSize.width - frameSize.width) / 2,
					 (screenSize.height - frameSize.height) / 2);

	 setVisible(true);
	 return datosLogin;
  }

  void botonAceptar_actionPerformed(ActionEvent e) {
	 if (campoUsuario.getText().length() > 0 && campoPass.getText().length() > 0) {
		datosLogin[0] = campoUsuario.getText();
		datosLogin[1] = campoPass.getText();
		setVisible(false);
	 }
	 else {
		JOptionPane.showMessageDialog(null, "No debes dejar ning�n campo vac�arbol.expandir();o",
												"Error", JOptionPane.ERROR_MESSAGE);

	 }
  }

  void jButton1_actionPerformed(ActionEvent e) {
	 
	  DConector.obtenerDC().salir();
	  System.exit(0);
  }

  void this_windowClosing(WindowEvent e) {
	 DConector.obtenerDC().salir();
	 System.exit(0);
  }

}

class DialogoLogin_botonAceptar_actionAdapter
	 implements java.awt.event.ActionListener {
  DialogoLogin adaptee;

  DialogoLogin_botonAceptar_actionAdapter(DialogoLogin adaptee) {
	 this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
	 adaptee.botonAceptar_actionPerformed(e);
  }
}

class DialogoLogin_jButton1_actionAdapter
	 implements java.awt.event.ActionListener {
  DialogoLogin adaptee;

  DialogoLogin_jButton1_actionAdapter(DialogoLogin adaptee) {
	 this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
	 adaptee.jButton1_actionPerformed(e);
  }
}

class DialogoLogin_this_windowAdapter
	 extends java.awt.event.WindowAdapter {
  DialogoLogin adaptee;

  DialogoLogin_this_windowAdapter(DialogoLogin adaptee) {
	 this.adaptee = adaptee;
  }

  public void windowClosing(WindowEvent e) {
	 adaptee.this_windowClosing(e);
  }
}
