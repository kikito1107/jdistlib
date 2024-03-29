package desarrollo;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.event.*;

import util.*;
import metainformacion.*;
import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class PanelMetaInformacion
	 extends JPanel {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

ClienteMetaInformacion cmi = ClienteMetaInformacion.cmi;

  BorderLayout borderLayout1 = new BorderLayout();
  JSplitPane jSplitPane1 = new JSplitPane();
  JPanel panelIzquierdo = new JPanel();
  JPanel panelDerecho = new JPanel();
  JPanel panelInferior = new JPanel();
  JButton botonAplicar = new JButton();
  JScrollPane jScrollPane1 = new JScrollPane();
  Lista listaUsuariosPermisos = new Lista();
  JLabel etiquetaU = new JLabel();
  JScrollPane jScrollPane2 = new JScrollPane();
  BorderLayout borderLayout3 = new BorderLayout();
  JLabel jLabel2 = new JLabel();
  Lista listaComponentes = new Lista();
  JPanel panelPermisos = new JPanel();
  BorderLayout borderLayout4 = new BorderLayout();
  JTabbedPane jTabbedPane1 = new JTabbedPane();
  JPanel jPanel4 = new JPanel();
  JRadioButton botonUsuario = new JRadioButton();
  JRadioButton botonRol = new JRadioButton();
  TitledBorder titledBorder1;
  ButtonGroup buttonGroup1 = new ButtonGroup();
  JPanel jPanel5 = new JPanel();
  BorderLayout borderLayout7 = new BorderLayout();
  JPanel jPanel6 = new JPanel();
  JScrollPane jScrollPane4 = new JScrollPane();
  BorderLayout borderLayout8 = new BorderLayout();
  JLabel etiquetaR = new JLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  Lista listaRolesPermisos = new Lista();
  CampoTextoNumerico campoPermiso = new CampoTextoNumerico();
  JPanel panelNuevo = new JPanel();
  JPanel panelEliminar = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JRadioButton botonNRol = new JRadioButton();
  JRadioButton botonNUsuario = new JRadioButton();
  ButtonGroup buttonGroup2 = new ButtonGroup();
  JPanel jPanel2 = new JPanel();
  JTextField campoNombre = new JTextField();
  JTextField campoClave = new JTextField();
  JCheckBox checkAdmin = new JCheckBox();
  ListaDesplegable listaRoles = new ListaDesplegable();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  BorderLayout borderLayout5 = new BorderLayout();
  JPanel jPanel3 = new JPanel();
  JRadioButton botonERol = new JRadioButton();
  JRadioButton botonEUsuario = new JRadioButton();
  JPanel jPanel7 = new JPanel();
  ListaDesplegable listaEliminarUsuarios = new ListaDesplegable();
  ListaDesplegable listaEliminarRoles = new ListaDesplegable();
  JButton botonEliminar = new JButton();
  JButton botonCrear = new JButton();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JPanel panelRolesPermitidos = new JPanel();
  BorderLayout borderLayout6 = new BorderLayout();
  JPanel jPanel8 = new JPanel();
  JRadioButton botonREliminar = new JRadioButton();
  JRadioButton botonRAniadir = new JRadioButton();
  JPanel jPanel9 = new JPanel();
  ListaDesplegable listaCambiarRoles = new ListaDesplegable();
  ListaDesplegable listaCambiarUsuarios = new ListaDesplegable();
  JLabel jLabel7 = new JLabel();
  JLabel jLabel8 = new JLabel();
  JButton botonEfectuarCambio = new JButton();
  JPanel jPanel10 = new JPanel();
  JScrollPane jScrollPane3 = new JScrollPane();
  BorderLayout borderLayout9 = new BorderLayout();
  ListaElementos listaCambiarRolesPermitidos = new ListaElementos();
  JLabel listaRolesPermitidos = new JLabel();
  ButtonGroup buttonGroup3 = new ButtonGroup();
  ButtonGroup buttonGroup4 = new ButtonGroup();

  public PanelMetaInformacion() {
	 try {
		jbInit();
	 }
	 catch (Exception ex) {
		ex.printStackTrace();
	 }
  }

  void jbInit() throws Exception {
	 titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.
		  white, new Color(165, 163, 151)), "Tipo permiso");
	 this.setLayout(borderLayout1);
	 jSplitPane1.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
	 panelInferior.setMinimumSize(new Dimension(10, 50));
	 panelInferior.setPreferredSize(new Dimension(10, 75));
    botonAplicar.setText("Aplicar");
    botonAplicar.addActionListener(new PanelMetaInformacion_botonAplicar_actionAdapter(this));
	 panelIzquierdo.setLayout(gridBagLayout1);
	 etiquetaU.setFont(new java.awt.Font("Dialog", 1, 11));
	 etiquetaU.setHorizontalAlignment(SwingConstants.CENTER);
	 etiquetaU.setText("Usuarios");
	 panelDerecho.setLayout(borderLayout3);
	 jLabel2.setFont(new java.awt.Font("Dialog", 1, 11));
	 jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
	 jLabel2.setText("Componentes");
	 panelPermisos.setLayout(borderLayout4);
    botonUsuario.setForeground(Color.blue);
    botonUsuario.setFocusPainted(false);
	 botonUsuario.setText("Usuario");
	 botonUsuario.addActionListener(new
		  PanelMetaInformacion_botonUsuario_actionAdapter(this));
    botonRol.setForeground(Color.blue);
    botonRol.setFocusPainted(false);
	 botonRol.setText("Rol");
	 botonRol.addActionListener(new
		  PanelMetaInformacion_botonRol_actionAdapter(this));
	 jPanel4.setBorder(titledBorder1);
	 jPanel5.setLayout(borderLayout7);
	 jPanel6.setLayout(borderLayout8);
	 etiquetaR.setFont(new java.awt.Font("Dialog", 1, 11));
	 etiquetaR.setHorizontalAlignment(SwingConstants.CENTER);
	 etiquetaR.setText("Roles");
	 jPanel6.setPreferredSize(new Dimension(31, 50));
	 jScrollPane1.setPreferredSize(new Dimension(258, 80));
    campoPermiso.setPreferredSize(new Dimension(50, 20));
    campoPermiso.setText("");
    panelNuevo.setLayout(borderLayout2);
    botonNRol.setForeground(Color.blue);
    botonNRol.setText("Rol");
    botonNRol.addActionListener(new PanelMetaInformacion_botonNRol_actionAdapter(this));
    botonNUsuario.setForeground(Color.blue);
    botonNUsuario.setText("Usuario");
    botonNUsuario.addActionListener(new PanelMetaInformacion_botonNUsuario_actionAdapter(this));
    jPanel2.setLayout(null);
    campoNombre.setText("");
    campoNombre.setBounds(new Rectangle(117, 28, 115, 20));
    campoClave.setText("");
    campoClave.setBounds(new Rectangle(117, 56, 104, 20));
    checkAdmin.setSelected(false);
    checkAdmin.setText("Administrador");
    checkAdmin.setBounds(new Rectangle(116, 113, 130, 23));
    listaRoles.setBounds(new Rectangle(117, 84, 98, 19));
    jLabel1.setText("Nombre:");
    jLabel1.setBounds(new Rectangle(20, 31, 53, 15));
    jLabel3.setText("Clave:");
    jLabel3.setBounds(new Rectangle(20, 59, 47, 15));
    jLabel4.setText("Rol por defecto:");
    jLabel4.setBounds(new Rectangle(20, 86, 89, 15));
    panelEliminar.setLayout(borderLayout5);
    botonERol.setForeground(Color.blue);
    botonERol.setText("Rol");
    botonEUsuario.setForeground(Color.blue);
    botonEUsuario.setText("Usuario");
    jPanel7.setLayout(null);
    listaEliminarUsuarios.setBounds(new Rectangle(17, 42, 102, 19));
    listaEliminarRoles.setBounds(new Rectangle(154, 42, 116, 19));
    botonEliminar.setBounds(new Rectangle(94, 82, 90, 23));
    botonEliminar.setText("Eliminar");
    botonEliminar.addActionListener(new PanelMetaInformacion_botonEliminar_actionAdapter(this));
    botonCrear.setBounds(new Rectangle(136, 163, 71, 23));
    botonCrear.setText("Crear");
    botonCrear.addActionListener(new PanelMetaInformacion_botonCrear_actionAdapter(this));
    jLabel5.setText("Usuarios");
    jLabel5.setBounds(new Rectangle(17, 12, 79, 15));
    jLabel6.setText("Roles");
    jLabel6.setBounds(new Rectangle(154, 12, 46, 15));
    panelRolesPermitidos.setLayout(borderLayout6);
    botonREliminar.setForeground(Color.blue);
    botonREliminar.setText("Eliminar");
    botonRAniadir.setForeground(Color.blue);
    botonRAniadir.setSelected(true);
    botonRAniadir.setText("Agregar");
    botonRAniadir.addActionListener(new PanelMetaInformacion_botonRAniadir_actionAdapter(this));
    jPanel9.setLayout(null);
    listaCambiarRoles.setBounds(new Rectangle(158, 38, 121, 19));
    listaCambiarUsuarios.setBounds(new Rectangle(20, 38, 108, 19));
    listaCambiarUsuarios.addActionListener(new PanelMetaInformacion_listaCambiarUsuarios_actionAdapter(this));
    jLabel7.setText("Usuario");
    jLabel7.setBounds(new Rectangle(20, 9, 75, 15));
    jLabel8.setText("Rol");
    jLabel8.setBounds(new Rectangle(158, 9, 34, 15));
    botonEfectuarCambio.setBounds(new Rectangle(88, 243, 147, 23));
    botonEfectuarCambio.setText("Efectuar cambio");
    botonEfectuarCambio.addActionListener(new PanelMetaInformacion_botonEfectuarCambio_actionAdapter(this));
    jPanel10.setBounds(new Rectangle(80, 94, 163, 139));
    jPanel10.setLayout(borderLayout9);
    listaRolesPermitidos.setText("Roles Permitidos");
    listaRolesPermitidos.setBounds(new Rectangle(80, 73, 135, 15));
    panelDerecho.add(jScrollPane2, BorderLayout.CENTER);
	 panelDerecho.add(jLabel2, BorderLayout.NORTH);
	 jScrollPane2.getViewport().add(listaComponentes, null);
	 this.add(jTabbedPane1, BorderLayout.CENTER);
	 panelInferior.add(campoPermiso, null);
    panelInferior.add(botonAplicar, null);
    panelNuevo.add(jPanel1, BorderLayout.NORTH);
    jPanel1.add(botonNUsuario, null);
    jPanel1.add(botonNRol, null);
    panelNuevo.add(jPanel2,  BorderLayout.CENTER);
    jPanel2.add(jLabel1, null);
    jPanel2.add(jLabel3, null);
    jPanel2.add(jLabel4, null);
    jPanel2.add(campoNombre, null);
    jPanel2.add(campoClave, null);
    jPanel2.add(listaRoles, null);
    jPanel2.add(checkAdmin, null);
    jPanel2.add(botonCrear, null);
	 panelPermisos.add(jPanel4, BorderLayout.NORTH);
	 jPanel4.add(botonRol, null);
	 jPanel4.add(botonUsuario, null);
	 panelPermisos.add(jSplitPane1, BorderLayout.CENTER);
	 panelIzquierdo.add(jPanel5, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
		  , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		  new Insets(0, 0, 0, 0), 0, 0));
	 jPanel5.add(jScrollPane1, BorderLayout.CENTER);
	 jPanel5.add(etiquetaU, BorderLayout.NORTH);
	 panelIzquierdo.add(jPanel6, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
		  , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		  new Insets(0, 0, 0, 0), 0, 0));
	 jPanel6.add(jScrollPane4, BorderLayout.CENTER);
	 jScrollPane4.getViewport().add(listaRolesPermisos, null);
	 jPanel6.add(etiquetaR, BorderLayout.NORTH);
	 jScrollPane1.getViewport().add(listaUsuariosPermisos, null);
	 jSplitPane1.add(panelDerecho, JSplitPane.RIGHT);
	 jSplitPane1.add(panelIzquierdo, JSplitPane.LEFT);
	 panelPermisos.add(panelInferior, BorderLayout.SOUTH);
	 jTabbedPane1.add(panelPermisos, "Gestion permisos");
	 jTabbedPane1.add(panelNuevo,   "Nuevo");
	 jSplitPane1.setDividerLocation(100);
	 buttonGroup1.add(botonUsuario);
	 buttonGroup1.add(botonRol);
    jTabbedPane1.add(panelEliminar,  "Eliminar");
    panelEliminar.add(jPanel3, BorderLayout.NORTH);
    jPanel3.add(botonEUsuario, null);
    jPanel3.add(botonERol, null);
    panelEliminar.add(jPanel7,  BorderLayout.CENTER);
    jPanel7.add(listaEliminarUsuarios, null);
	 buttonGroup2.add(botonNUsuario);
	 buttonGroup2.add(botonNRol);
    jPanel7.add(listaEliminarRoles, null);
    jPanel7.add(jLabel5, null);
    jPanel7.add(jLabel6, null);
    jPanel7.add(botonEliminar, null);
    jTabbedPane1.add(panelRolesPermitidos,   "Cambiar Roles Permitidos");
    panelRolesPermitidos.add(jPanel8, BorderLayout.NORTH);
    jPanel8.add(botonRAniadir, null);
    jPanel8.add(botonREliminar, null);
    panelRolesPermitidos.add(jPanel9,  BorderLayout.CENTER);
    jPanel9.add(jLabel7, null);
    jPanel9.add(listaCambiarUsuarios, null);
    jPanel9.add(jLabel8, null);
    jPanel9.add(listaCambiarRoles, null);
    jPanel9.add(listaRolesPermitidos, null);
    jPanel9.add(jPanel10, null);
    jPanel10.add(jScrollPane3, BorderLayout.CENTER);
    jPanel9.add(botonEfectuarCambio, null);
    jScrollPane3.getViewport().add(listaCambiarRolesPermitidos, null);
	 botonNUsuario.setSelected(true);
	 botonEUsuario.setSelected(true);

  //**********************************
	 botonRol.setSelected(true);
	 listaUsuariosPermisos.setEnabled(false);
	 etiquetaU.setEnabled(false);
	 listaRolesPermisos.setEnabled(true);
	 etiquetaR.setEnabled(true);
	 //**********************************
    buttonGroup3.add(botonEUsuario);
    buttonGroup3.add(botonERol);
    buttonGroup4.add(botonRAniadir);
    buttonGroup4.add(botonREliminar);

  }



  public void nuevoUsuario(String usuario){
	 listaUsuariosPermisos.aniadir(usuario);
}
  public void nuevoRol(String rol){
	 listaRolesPermisos.aniadir(rol);
}
  public void eliminarUsuario(String usuario){
	 listaUsuariosPermisos.eliminar(usuario);
	 listaEliminarUsuarios.borrarElemento(usuario);
	 if(listaCambiarUsuarios.getSeleccionado().equals(usuario)){
		listaCambiarRolesPermitidos.eliminarElementos();
	 }
	 listaCambiarUsuarios.borrarElemento(usuario);
  }

  public void eliminarRol(String rol){
	 listaRolesPermisos.eliminar(rol);
	 listaEliminarRoles.borrarElemento(rol);
	 listaRoles.borrarElemento(rol);
	 listaCambiarRoles.borrarElemento(rol);
}


  void botonRol_actionPerformed(ActionEvent e) {
	 if (botonRol.isSelected()) {
		listaUsuariosPermisos.setEnabled(false);
		etiquetaU.setEnabled(false);
		listaRolesPermisos.setEnabled(true);
		etiquetaR.setEnabled(true);
		actualizarPermiso();
	 }
  }

  void botonUsuario_actionPerformed(ActionEvent e) {
	 if (botonUsuario.isSelected()) {
		listaRolesPermisos.setEnabled(false);
		etiquetaR.setEnabled(false);
		listaUsuariosPermisos.setEnabled(true);
		etiquetaU.setEnabled(true);
		actualizarPermiso();
	 }
  }

  public void inicializar(String rol){
	 //************************************************
	  listaComponentes.getSelectionModel().addListSelectionListener(new ListenerListas());
	  listaUsuariosPermisos.getSelectionModel().addListSelectionListener(new ListenerListas());
	  listaRolesPermisos.getSelectionModel().addListSelectionListener(new ListenerListas());
	  campoPermiso.setEnabled(false);
	  Vector usuarios = cmi.obtenerUsuarios();
	  Vector roles = cmi.obtenerRoles();
	  Vector componentes = cmi.obtenerComponentes();
	  Vector rolesPermitidos = cmi.obtenerRolesPermitidos(cmi.usuario);
	  int i = 0;

	  listaEliminarUsuarios.aniadir("Ninguno...");
	  listaCambiarUsuarios.aniadir("NInguno...");
	  listaRoles.aniadir("Ninguno...");
	  listaEliminarRoles.aniadir("Ninguno...");
	  listaCambiarRoles.aniadir("Ninguno...");
	  
	for (i = 0; i < usuarios.size(); i++) {
	  String usuario = (String) usuarios.elementAt(i);
	  listaUsuariosPermisos.aniadir( usuario);
	  listaEliminarUsuarios.aniadir(usuario);
	  listaCambiarUsuarios.aniadir(usuario);
	}
	if(usuarios.size() > 0){
	  listaUsuariosPermisos.setSelectedIndex(0);
	}
	for (i = 0; i < roles.size(); i++) {
	  String nrol = (String) roles.elementAt(i);
	  listaRolesPermisos.aniadir(nrol);
	  listaRoles.aniadir(nrol);
	  listaEliminarRoles.aniadir(nrol);
	  listaCambiarRoles.aniadir(nrol);
	}
	if(roles.size() > 0){
	  listaRolesPermisos.setSelectedIndex(0);
	}
	for (i = 0; i < componentes.size(); i++) {
	  listaComponentes.aniadir( (String) componentes.elementAt(i));
	}
}

  private void cambiarPermiso() {
	 int permiso = -1;

	 if(campoPermiso.getText().length() > 0){
		permiso = Integer.parseInt(campoPermiso.getText());
		if (botonUsuario.isSelected()) {
		  if (listaUsuariosPermisos.getSelectedIndex() >= 0 &&
				listaComponentes.getSelectedIndex() >= 0) {
			 String componente = (String) listaComponentes.getSelectedValue();
			 String usuario = (String) listaUsuariosPermisos.getSelectedValue();
			 listaComponentes.setEnabled(false);
			 campoPermiso.setEnabled(false);
			 listaUsuariosPermisos.setEnabled(false);

			 cmi.cambiarPermisoComponenteUsuario(usuario, componente, permiso);
			 listaUsuariosPermisos.setEnabled(true);
			 listaComponentes.setEnabled(true);
			 campoPermiso.setEnabled(true);
		  }
		}
		else if (botonRol.isSelected()) {
		  if (listaRolesPermisos.getSelectedIndex() >= 0 &&
				listaComponentes.getSelectedIndex() >= 0) {
			 String componente = (String) listaComponentes.getSelectedValue();
			 String rol = (String) listaRolesPermisos.getSelectedValue();
			 listaComponentes.setEnabled(false);
			 campoPermiso.setEnabled(false);
			 listaRolesPermisos.setEnabled(false);
			 cmi.cambiarPermisoComponenteRol(rol, componente, permiso);
			 listaRolesPermisos.setEnabled(true);
			 listaComponentes.setEnabled(true);
			 campoPermiso.setEnabled(true);
		  }
		}
	 }
  }

  private void actualizarPermiso() {
	 int permiso = -1;

	 if (botonUsuario.isSelected()) {
		if (listaUsuariosPermisos.getSelectedIndex() >= 0 &&
			 listaComponentes.getSelectedIndex() >= 0) {
		  String componente = (String) listaComponentes.getSelectedValue();
		  String usuario = (String) listaUsuariosPermisos.getSelectedValue();
		  listaComponentes.setEnabled(false);
		  campoPermiso.setEnabled(false);
		  listaUsuariosPermisos.setEnabled(false);
		  permiso = cmi.obtenerPermisoComponenteUsuario(usuario, componente);
		  campoPermiso.setEnabled(true);
		  listaUsuariosPermisos.setEnabled(true);
		  listaComponentes.setEnabled(true);
		  campoPermiso.setText(permiso+"");
		  //System.out.println(permiso);
		}
	 }
	 else if (botonRol.isSelected()) {
		if (listaRolesPermisos.getSelectedIndex() >= 0 &&
			 listaComponentes.getSelectedIndex() >= 0) {
		  String componente = (String) listaComponentes.getSelectedValue();
		  String rol = (String) listaRolesPermisos.getSelectedValue();
		  listaComponentes.setEnabled(false);
		  campoPermiso.setEnabled(false);
		  listaRolesPermisos.setEnabled(false);
		  permiso = cmi.obtenerPermisoComponenteRol(rol, componente);
		  campoPermiso.setEnabled(true);
		  listaRolesPermisos.setEnabled(true);
		  listaComponentes.setEnabled(true);
		  campoPermiso.setText(permiso+"");
		}
	 }
  }

  private class ListenerListas
		implements ListSelectionListener {
	 public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();

		if (!e.getValueIsAdjusting()) {
		  actualizarPermiso();
		}
	 }

  }

  void botonAplicar_actionPerformed(ActionEvent e) {
	 cambiarPermiso();
  }

  void botonNRol_actionPerformed(ActionEvent e) {
	 campoClave.setEnabled(false);
	 listaRoles.setEnabled(false);
	 checkAdmin.setEnabled(false);
  }

  void botonNUsuario_actionPerformed(ActionEvent e) {
	 campoClave.setEnabled(true);
	 listaRoles.setEnabled(true);
	 checkAdmin.setEnabled(true);
  }

  void botonRAniadir_actionPerformed(ActionEvent e) {

  }

  void botonCrear_actionPerformed(ActionEvent e) {
	 if (botonNUsuario.isSelected()) { // Creacion de usuario
		String usuario = null;
		String clave = null;
		String rolDefecto = null;
		boolean error = false;
		usuario = campoNombre.getText();
		clave = campoClave.getText();
		rolDefecto = listaRoles.getSeleccionado();
		if (usuario.length() == 0) {
		  JOptionPane.showMessageDialog(null,
												  "No pudes dejar el campo nombre vacio",
												  "Error", JOptionPane.ERROR_MESSAGE);
		  error = true;
		}

		if (clave.length() == 0 && !error) {
		  JOptionPane.showMessageDialog(null,
												  "No pudes dejar el campo clave vacio",
												  "Error", JOptionPane.ERROR_MESSAGE);
		  error = true;
		}
		if (listaRoles.getSelectedIndex() == 0 && !error) {
		  JOptionPane.showMessageDialog(null, "Debes seleccionar un rol",
												  "Error", JOptionPane.ERROR_MESSAGE);
		  error = true;
		}
		if(!error){
		  String mensaje = ClienteMetaInformacion.obtenerCMI().nuevoUsuario(usuario,clave,rolDefecto,checkAdmin.isSelected());
		  if (mensaje.length() == 0) {
			 JOptionPane.showMessageDialog(null,
													 "Usuario creado",
													 "Creacion de usuario",
													 JOptionPane.INFORMATION_MESSAGE);
		  }
		  else {
			 JOptionPane.showMessageDialog(null,
													 mensaje,
													 "Error", JOptionPane.ERROR_MESSAGE);
		  }
		}
	 }
	 else { // Creacion de rol
		String nombre = campoNombre.getText();
		boolean error = false;
		if (nombre.length() == 0) {
		  JOptionPane.showMessageDialog(null,
												  "No pudes dejar el campo nombre vacio",
												  "Error", JOptionPane.ERROR_MESSAGE);
		  error = true;
		}
		if (!error) {
		  String mensaje = ClienteMetaInformacion.obtenerCMI().nuevoRol(nombre);
		  if (mensaje.length() == 0) {
			 JOptionPane.showMessageDialog(null,
													 "Rol creado",
													 "Creacion de rol",
													 JOptionPane.INFORMATION_MESSAGE);
		  }
		  else {
			 JOptionPane.showMessageDialog(null,
													 mensaje,
													 "Error", JOptionPane.ERROR_MESSAGE);
		  }

		}

	 }
  }

  void botonEliminar_actionPerformed(ActionEvent e) {
	 if (botonEUsuario.isSelected()) { // Eliminamos un usuario
		if (listaEliminarUsuarios.getSelectedIndex() == 0) {
		  JOptionPane.showMessageDialog(null,
												  "Debes seleccionar un usuario",
												  "Error", JOptionPane.ERROR_MESSAGE);

		}
		else {
		  String usuario = listaEliminarUsuarios.getSeleccionado();
		  String mensaje = ClienteMetaInformacion.obtenerCMI().eliminarUsuario(
				usuario);
		  if (mensaje.length() == 0) {
			 if(!usuario.equals(ClienteMetaInformacion.usuario)){
				JOptionPane.showMessageDialog(null, "Usuario eliminado",
														"OK",
														JOptionPane.INFORMATION_MESSAGE);
			 }
		  }
		  else {
			 JOptionPane.showMessageDialog(null, mensaje,
													 "Error",
													 JOptionPane.ERROR_MESSAGE);
		  }
		}
	 }
	 else { // Eliminamos un rol
		if (listaEliminarRoles.getSelectedIndex() == 0) {
		  JOptionPane.showMessageDialog(null,
												  "Debes seleccionar un rol",
												  "Error", JOptionPane.ERROR_MESSAGE);

		}
		else {
		  String rol = listaEliminarRoles.getSeleccionado();
		  String mensaje = ClienteMetaInformacion.obtenerCMI().eliminarRol(
				rol);
	 if (mensaje.length() == 0) {
		JOptionPane.showMessageDialog(null, "Rol eliminado",
												"OK",
												JOptionPane.INFORMATION_MESSAGE);
	 }
	 else {
		JOptionPane.showMessageDialog(null, mensaje,
												"Error",
												JOptionPane.ERROR_MESSAGE);
	 }

		}
	 }
  }

  void listaCambiarUsuarios_actionPerformed(ActionEvent e) {
	 listaCambiarRolesPermitidos.eliminarElementos();
	 if(listaCambiarUsuarios.getSelectedIndex() != 0){
		String usuario = listaCambiarUsuarios.getSeleccionado();
		Vector v = ClienteMetaInformacion.obtenerCMI().obtenerRolesPermitidos(usuario);
		for(int i=0; i<v.size(); i++){
		  listaCambiarRolesPermitidos.aniadirElemento((String)v.elementAt(i));
		}
	 }
  }

  void botonEfectuarCambio_actionPerformed(ActionEvent e) {
	 if(botonRAniadir.isSelected()){
		if (listaCambiarUsuarios.getSelectedIndex() != 0 &&
			 listaCambiarRoles.getSelectedIndex() != 0) {
		  String usuario = listaCambiarUsuarios.getSeleccionado();
		  String rol = listaCambiarRoles.getSeleccionado();
		  String mensaje = ClienteMetaInformacion.obtenerCMI().nuevoRolPermitido(
				usuario, rol);
		  if (mensaje.length() == 0) {
			 JOptionPane.showMessageDialog(null, "Rol a�adido",
													 "OK",
													 JOptionPane.INFORMATION_MESSAGE);
			 listaCambiarRolesPermitidos.aniadirElemento(rol);
		  }
		  else {
			 JOptionPane.showMessageDialog(null, mensaje,
													 "Error",
													 JOptionPane.ERROR_MESSAGE);
		  }
		}
	 }
	 else {
		if (listaCambiarUsuarios.getSelectedIndex() != 0 &&
			 listaCambiarRoles.getSelectedIndex() != 0) {
		  String usuario = listaCambiarUsuarios.getSeleccionado();
		  String rol = listaCambiarRoles.getSeleccionado();
		  String mensaje = ClienteMetaInformacion.obtenerCMI().eliminarRolPermitido(
				usuario, rol);
	 if (mensaje.length() == 0) {
		JOptionPane.showMessageDialog(null, "Rol eliminado",
												"OK",
												JOptionPane.INFORMATION_MESSAGE);
		listaCambiarRolesPermitidos.eliminarElemento(rol);
	 }
	 else {
		JOptionPane.showMessageDialog(null, mensaje,
												"Error",
												JOptionPane.ERROR_MESSAGE);
	 }
		}
}
  }

}

class PanelMetaInformacion_botonRol_actionAdapter
	 implements java.awt.event.ActionListener {
  PanelMetaInformacion adaptee;

  PanelMetaInformacion_botonRol_actionAdapter(PanelMetaInformacion adaptee) {
	 this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
	 adaptee.botonRol_actionPerformed(e);
  }
}

class PanelMetaInformacion_botonUsuario_actionAdapter
	 implements java.awt.event.ActionListener {
  PanelMetaInformacion adaptee;

  PanelMetaInformacion_botonUsuario_actionAdapter(PanelMetaInformacion adaptee) {
	 this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
	 adaptee.botonUsuario_actionPerformed(e);
  }
}

class PanelMetaInformacion_botonAplicar_actionAdapter implements java.awt.event.ActionListener {
  PanelMetaInformacion adaptee;

  PanelMetaInformacion_botonAplicar_actionAdapter(PanelMetaInformacion adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.botonAplicar_actionPerformed(e);
  }
}

class PanelMetaInformacion_botonNRol_actionAdapter implements java.awt.event.ActionListener {
  PanelMetaInformacion adaptee;

  PanelMetaInformacion_botonNRol_actionAdapter(PanelMetaInformacion adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.botonNRol_actionPerformed(e);
  }
}

class PanelMetaInformacion_botonNUsuario_actionAdapter implements java.awt.event.ActionListener {
  PanelMetaInformacion adaptee;

  PanelMetaInformacion_botonNUsuario_actionAdapter(PanelMetaInformacion adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.botonNUsuario_actionPerformed(e);
  }
}

class PanelMetaInformacion_botonRAniadir_actionAdapter implements java.awt.event.ActionListener {
  PanelMetaInformacion adaptee;

  PanelMetaInformacion_botonRAniadir_actionAdapter(PanelMetaInformacion adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.botonRAniadir_actionPerformed(e);
  }
}

class PanelMetaInformacion_botonCrear_actionAdapter implements java.awt.event.ActionListener {
  PanelMetaInformacion adaptee;

  PanelMetaInformacion_botonCrear_actionAdapter(PanelMetaInformacion adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.botonCrear_actionPerformed(e);
  }
}

class PanelMetaInformacion_botonEliminar_actionAdapter implements java.awt.event.ActionListener {
  PanelMetaInformacion adaptee;

  PanelMetaInformacion_botonEliminar_actionAdapter(PanelMetaInformacion adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.botonEliminar_actionPerformed(e);
  }
}

class PanelMetaInformacion_listaCambiarUsuarios_actionAdapter implements java.awt.event.ActionListener {
  PanelMetaInformacion adaptee;

  PanelMetaInformacion_listaCambiarUsuarios_actionAdapter(PanelMetaInformacion adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.listaCambiarUsuarios_actionPerformed(e);
  }
}

class PanelMetaInformacion_botonEfectuarCambio_actionAdapter implements java.awt.event.ActionListener {
  PanelMetaInformacion adaptee;

  PanelMetaInformacion_botonEfectuarCambio_actionAdapter(PanelMetaInformacion adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.botonEfectuarCambio_actionPerformed(e);
  }
}
