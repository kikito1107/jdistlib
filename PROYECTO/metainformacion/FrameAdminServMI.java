package metainformacion;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Frame que se abre al iniciar el servidor de Metainformacion
 */

public class FrameAdminServMI
	 extends JFrame {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JButton botonCerrar = new JButton();
  JPanel jPanel2 = new JPanel();
  JLabel jLabel2 = new JLabel();
  JButton botonGuardar = new JButton();
  ServidorMetaInformacion smi = new
		ServidorMetaInformacion();

  public FrameAdminServMI() {
	 try {
		jbInit();
	 }
	 catch (Exception ex) {
		ex.printStackTrace();
	 }
  }

  void jbInit() throws Exception {
	 this.getContentPane().setLayout(borderLayout1);
	 botonCerrar.setText("Cerrar Servidor");
	 botonCerrar.addActionListener(new
											 FrameAdminServMI_botonCerrar_actionAdapter(this));
	 jPanel2.setLayout(null);
	 jLabel2.setFont(new java.awt.Font("Dialog", 1, 13));
	 jLabel2.setText("Guardar datos del servidor de metaInformacion");
	 jLabel2.setBounds(new Rectangle(32, 23, 284, 15));
	 botonGuardar.setBounds(new Rectangle(154, 91, 90, 23));
	 botonGuardar.setText("Guardar");
	 botonGuardar.addActionListener(new
											  FrameAdminServMI_botonGuardar_actionAdapter(this));
	 this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	 this.setResizable(false);
	 this.setTitle(".:: Servidor MetaInformacion ::.");
	 this.getContentPane().add(jPanel1, BorderLayout.SOUTH);
	 jPanel1.add(botonCerrar, null);
	 this.getContentPane().add(jPanel2, BorderLayout.CENTER);
	 jPanel2.add(jLabel2, null);
	 jPanel2.add(botonGuardar, null);
  }

  void botonGuardar_actionPerformed(ActionEvent e) {
	 int res = JOptionPane.showConfirmDialog(null, "Desea guardar los contenidos del servidor de metainformacion?", "Aviso", JOptionPane.YES_NO_OPTION);
	 
	 if (res == JOptionPane.YES_OPTION)
		smi.salvar();
  }

  void botonCerrar_actionPerformed(ActionEvent e) {
	 System.exit(0);
  }
}

class FrameAdminServMI_botonGuardar_actionAdapter
	 implements java.awt.event.ActionListener {
  FrameAdminServMI adaptee;

  FrameAdminServMI_botonGuardar_actionAdapter(FrameAdminServMI adaptee) {
	 this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
	 adaptee.botonGuardar_actionPerformed(e);
  }
}

class FrameAdminServMI_botonCerrar_actionAdapter
	 implements java.awt.event.ActionListener {
  FrameAdminServMI adaptee;

  FrameAdminServMI_botonCerrar_actionAdapter(FrameAdminServMI adaptee) {
	 this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
	 adaptee.botonCerrar_actionPerformed(e);
  }
}
