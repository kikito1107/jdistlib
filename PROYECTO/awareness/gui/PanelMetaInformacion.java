package awareness.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import util.Lista;
import util.ListaDesplegable;
import util.ListaElementos;
import javax.swing.ImageIcon;

import awareness.ClienteMetaInformacion;

/**
 * Panel con la metainformacion del sistema
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class PanelMetaInformacion extends JPanel
{
	private static final long serialVersionUID = 1L;

	private ClienteMetaInformacion cmi = ClienteMetaInformacion.cmi;

	private BorderLayout borderLayout1 = new BorderLayout();

	private JSplitPane jSplitPane1 = new JSplitPane();

	private JPanel panelIzquierdo = new JPanel();

	private JPanel panelDerecho = new JPanel();

	private JPanel panelInferior = new JPanel();

	private JButton botonAplicar = new JButton();

	private JScrollPane jScrollPane1 = new JScrollPane();

	private Lista listaUsuariosPermisos = new Lista();

	private JLabel etiquetaU = new JLabel();

	private JScrollPane jScrollPane2 = new JScrollPane();

	private BorderLayout borderLayout3 = new BorderLayout();

	private JLabel jLabel2 = new JLabel();

	private Lista listaComponentes = new Lista();

	private JPanel panelPermisos = new JPanel();

	private BorderLayout borderLayout4 = new BorderLayout();

	private JTabbedPane jTabbedPane1 = new JTabbedPane();

	private JPanel jPanel4 = new JPanel();

	private JRadioButton botonUsuario = new JRadioButton();

	private JRadioButton botonRol = new JRadioButton();

	private TitledBorder titledBorder1;

	private ButtonGroup buttonGroup1 = new ButtonGroup();

	private JPanel jPanel5 = new JPanel();

	private BorderLayout borderLayout7 = new BorderLayout();

	private JPanel jPanel6 = new JPanel();

	private JScrollPane jScrollPane4 = new JScrollPane();

	private BorderLayout borderLayout8 = new BorderLayout();

	private JLabel etiquetaR = new JLabel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private Lista listaRolesPermisos = new Lista();

	private CampoTextoNumerico campoPermiso = new CampoTextoNumerico();

	private JPanel panelNuevo = new JPanel();

	private JPanel panelEliminar = new JPanel();

	private BorderLayout borderLayout2 = new BorderLayout();

	private JPanel jPanel1 = new JPanel();

	private JRadioButton botonNRol = new JRadioButton();

	private JRadioButton botonNUsuario = new JRadioButton();

	private ButtonGroup buttonGroup2 = new ButtonGroup();

	private JPanel jPanel2 = new JPanel();

	private JTextField campoNombre = new JTextField();

	private JTextField campoClave = new JTextField();

	private JCheckBox checkAdmin = new JCheckBox();

	private ListaDesplegable listaRoles = new ListaDesplegable();

	private JLabel jLabel1 = new JLabel();

	private JLabel jLabel3 = new JLabel();

	private JLabel jLabel4 = new JLabel();

	private BorderLayout borderLayout5 = new BorderLayout();

	private JPanel jPanel3 = new JPanel();

	private JRadioButton botonERol = new JRadioButton();

	private JRadioButton botonEUsuario = new JRadioButton();

	private JPanel jPanel7 = new JPanel();

	private ListaDesplegable listaEliminarUsuarios = new ListaDesplegable();

	private ListaDesplegable listaEliminarRoles = new ListaDesplegable();

	private JButton botonEliminar = new JButton();

	private JButton botonCrear = new JButton();

	private JLabel jLabel5 = new JLabel();

	private JLabel jLabel6 = new JLabel();

	private JPanel panelRolesPermitidos = new JPanel();

	private BorderLayout borderLayout6 = new BorderLayout();

	private JPanel jPanel8 = new JPanel();

	private JRadioButton botonREliminar = new JRadioButton();

	private JRadioButton botonRAniadir = new JRadioButton();

	private JPanel jPanel9 = new JPanel();

	private ListaDesplegable listaCambiarRoles = new ListaDesplegable();

	private ListaDesplegable listaCambiarUsuarios = new ListaDesplegable();

	private JLabel jLabel7 = new JLabel();

	private JLabel jLabel8 = new JLabel();

	private JButton botonEfectuarCambio = new JButton();

	private JPanel jPanel10 = new JPanel();

	private JScrollPane jScrollPane3 = new JScrollPane();

	private BorderLayout borderLayout9 = new BorderLayout();

	private ListaElementos listaCambiarRolesPermitidos = new ListaElementos();

	private JLabel listaRolesPermitidos = new JLabel();

	private ButtonGroup buttonGroup3 = new ButtonGroup();

	private ButtonGroup buttonGroup4 = new ButtonGroup();

	/**
	 * Constructor
	 */
	public PanelMetaInformacion()
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

	/**
	 * Inicializacion de los componentes graficos
	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception
	{
		titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(165, 163, 151)), "Tipo permiso");
		this.setLayout(borderLayout1);
		jSplitPane1.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		panelInferior.setMinimumSize(new Dimension(10, 50));
		panelInferior.setPreferredSize(new Dimension(10, 75));
		botonAplicar.setText("Aplicar");
		botonAplicar
				.addActionListener(new PanelMetaInformacion_botonAplicar_actionAdapter(
						this));
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
		botonUsuario
				.addActionListener(new PanelMetaInformacion_botonUsuario_actionAdapter(
						this));
		botonRol.setForeground(Color.blue);
		botonRol.setFocusPainted(false);
		botonRol.setText("Rol");
		botonRol
				.addActionListener(new PanelMetaInformacion_botonRol_actionAdapter(
						this));
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
		botonNRol
				.addActionListener(new PanelMetaInformacion_botonNRol_actionAdapter(
						this));
		botonNUsuario.setForeground(Color.blue);
		botonNUsuario.setText("Usuario");
		botonNUsuario
				.addActionListener(new PanelMetaInformacion_botonNUsuario_actionAdapter(
						this));
		jPanel2.setLayout(null);
		campoNombre.setText("");
		campoNombre.setBounds(new Rectangle(125, 27, 160, 20));
		campoClave.setText("");
		campoClave.setBounds(new Rectangle(125, 55, 160, 20));
		checkAdmin.setSelected(false);
		checkAdmin.setText("Administrador");
		checkAdmin.setBounds(new Rectangle(140, 112, 130, 23));
		listaRoles.setBounds(new Rectangle(125, 83, 160, 20));
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
		botonEliminar
				.addActionListener(new PanelMetaInformacion_botonEliminar_actionAdapter(
						this));
		botonCrear.setBounds(new Rectangle(126, 163, 87, 23));
		botonCrear.setText("Crear");
		botonCrear
				.addActionListener(new PanelMetaInformacion_botonCrear_actionAdapter(
						this));
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
		botonRAniadir
				.addActionListener(new PanelMetaInformacion_botonRAniadir_actionAdapter(
						this));
		jPanel9.setLayout(null);
		listaCambiarRoles.setBounds(new Rectangle(158, 38, 121, 19));
		listaCambiarUsuarios.setBounds(new Rectangle(20, 38, 108, 19));
		listaCambiarUsuarios
				.addActionListener(new PanelMetaInformacion_listaCambiarUsuarios_actionAdapter(
						this));
		jLabel7.setText("Usuario");
		jLabel7.setBounds(new Rectangle(20, 9, 75, 15));
		jLabel8.setText("Rol");
		jLabel8.setBounds(new Rectangle(158, 9, 34, 15));
		botonEfectuarCambio.setBounds(new Rectangle(88, 243, 147, 23));
		botonEfectuarCambio.setText("Efectuar cambio");
		botonEfectuarCambio
				.addActionListener(new PanelMetaInformacion_botonEfectuarCambio_actionAdapter(
						this));
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
		panelNuevo.add(jPanel2, BorderLayout.CENTER);
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
		panelIzquierdo.add(jPanel5, new GridBagConstraints(0, 0, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanel5.add(jScrollPane1, BorderLayout.CENTER);
		jPanel5.add(etiquetaU, BorderLayout.NORTH);
		panelIzquierdo.add(jPanel6, new GridBagConstraints(0, 1, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanel6.add(jScrollPane4, BorderLayout.CENTER);
		jScrollPane4.getViewport().add(listaRolesPermisos, null);
		jPanel6.add(etiquetaR, BorderLayout.NORTH);
		jScrollPane1.getViewport().add(listaUsuariosPermisos, null);
		jSplitPane1.add(panelDerecho, JSplitPane.RIGHT);
		jSplitPane1.add(panelIzquierdo, JSplitPane.LEFT);
		panelPermisos.add(panelInferior, BorderLayout.SOUTH);
		jTabbedPane1.addTab("Gestion permisos", new ImageIcon(
				"Resources/tick.png"), panelPermisos, null); // Generated
		jSplitPane1.setDividerLocation(100);
		jTabbedPane1.addTab("Nuevo", new ImageIcon("Resources/user_add.png"),
				panelNuevo, null); // Generated
		buttonGroup1.add(botonUsuario);
		buttonGroup1.add(botonRol);
		panelEliminar.add(jPanel3, BorderLayout.NORTH);
		jPanel3.add(botonEUsuario, null);
		jPanel3.add(botonERol, null);
		panelEliminar.add(jPanel7, BorderLayout.CENTER);
		jPanel7.add(listaEliminarUsuarios, null);
		buttonGroup2.add(botonNUsuario);
		buttonGroup2.add(botonNRol);
		jPanel7.add(listaEliminarRoles, null);
		jPanel7.add(jLabel5, null);
		jPanel7.add(jLabel6, null);
		jPanel7.add(botonEliminar, null);
		jTabbedPane1.addTab("Eliminar", new ImageIcon(
				"Resources/user_delete.png"), panelEliminar, null); // Generated
		jTabbedPane1.addTab("Cambiar Roles Permitidos", new ImageIcon(
				"Resources/user_edit.png"), panelRolesPermitidos, null); // Generated
		panelRolesPermitidos.add(jPanel8, BorderLayout.NORTH);
		jPanel8.add(botonRAniadir, null);
		jPanel8.add(botonREliminar, null);
		panelRolesPermitidos.add(jPanel9, BorderLayout.CENTER);
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

		// **********************************
		botonRol.setSelected(true);
		listaUsuariosPermisos.setEnabled(false);
		etiquetaU.setEnabled(false);
		listaRolesPermisos.setEnabled(true);
		etiquetaR.setEnabled(true);
		// **********************************
		buttonGroup3.add(botonEUsuario);
		buttonGroup3.add(botonERol);
		buttonGroup4.add(botonRAniadir);
		buttonGroup4.add(botonREliminar);

	}

	/**
	 * Agrega un nuevo usuario a la lista
	 * 
	 * @param usuario
	 *            Nombre de usuario
	 */
	public void nuevoUsuario(String usuario)
	{
		listaUsuariosPermisos.aniadir(usuario);
	}

	/**
	 * Agrega un nuevo rol a la lista
	 * 
	 * @param rol
	 *            Nombre del rol
	 */
	public void nuevoRol(String rol)
	{
		listaRolesPermisos.aniadir(rol);
	}

	/**
	 * Elimina un usuario de la lista
	 * 
	 * @param usuario
	 *            Nombre del usuario
	 */
	public void eliminarUsuario(String usuario)
	{
		listaUsuariosPermisos.eliminar(usuario);
		listaEliminarUsuarios.borrarElemento(usuario);
		if (listaCambiarUsuarios.getSeleccionado().equals(usuario))
			listaCambiarRolesPermitidos.eliminarElementos();
		listaCambiarUsuarios.borrarElemento(usuario);
	}

	/**
	 * Elimina un rol de la lista
	 * 
	 * @param rol
	 *            Nombre del rol
	 */
	public void eliminarRol(String rol)
	{
		listaRolesPermisos.eliminar(rol);
		listaEliminarRoles.borrarElemento(rol);
		listaRoles.borrarElemento(rol);
		listaCambiarRoles.borrarElemento(rol);
	}

	private void botonRol_actionPerformed(ActionEvent e)
	{
		if (botonRol.isSelected())
		{
			listaUsuariosPermisos.setEnabled(false);
			etiquetaU.setEnabled(false);
			listaRolesPermisos.setEnabled(true);
			etiquetaR.setEnabled(true);
			actualizarPermiso();
		}
	}

	private void botonUsuario_actionPerformed(ActionEvent e)
	{
		if (botonUsuario.isSelected())
		{
			listaRolesPermisos.setEnabled(false);
			etiquetaR.setEnabled(false);
			listaUsuariosPermisos.setEnabled(true);
			etiquetaU.setEnabled(true);
			actualizarPermiso();
		}
	}

	@SuppressWarnings( "unchecked" )
	public void inicializar(String rol)
	{
		listaComponentes.getSelectionModel().addListSelectionListener(
				new ListenerListas());
		listaUsuariosPermisos.getSelectionModel().addListSelectionListener(
				new ListenerListas());
		listaRolesPermisos.getSelectionModel().addListSelectionListener(
				new ListenerListas());
		campoPermiso.setEnabled(false);
		Vector usuarios = cmi.obtenerUsuarios();
		Vector roles = cmi.obtenerRoles();
		Vector componentes = cmi.obtenerComponentes();
		int i = 0;

		listaEliminarUsuarios.aniadir("Ninguno...");
		listaCambiarUsuarios.aniadir("NInguno...");
		listaRoles.aniadir("Ninguno...");
		listaEliminarRoles.aniadir("Ninguno...");
		listaCambiarRoles.aniadir("Ninguno...");

		for (i = 0; i < usuarios.size(); i++)
		{
			String usuario = (String) usuarios.elementAt(i);
			listaUsuariosPermisos.aniadir(usuario);
			listaEliminarUsuarios.aniadir(usuario);
			listaCambiarUsuarios.aniadir(usuario);
		}
		if (usuarios.size() > 0) listaUsuariosPermisos.setSelectedIndex(0);
		for (i = 0; i < roles.size(); i++)
		{
			String nrol = (String) roles.elementAt(i);
			listaRolesPermisos.aniadir(nrol);
			listaRoles.aniadir(nrol);
			listaEliminarRoles.aniadir(nrol);
			listaCambiarRoles.aniadir(nrol);
		}
		if (roles.size() > 0) listaRolesPermisos.setSelectedIndex(0);
		for (i = 0; i < componentes.size(); i++)
			listaComponentes.aniadir((String) componentes.elementAt(i));
	}

	private void cambiarPermiso()
	{
		int permiso = -1;

		if (campoPermiso.getText().length() > 0)
		{
			permiso = Integer.parseInt(campoPermiso.getText());
			if (botonUsuario.isSelected())
			{
				if (( listaUsuariosPermisos.getSelectedIndex() >= 0 )
						&& ( listaComponentes.getSelectedIndex() >= 0 ))
				{
					String componente = (String) listaComponentes
							.getSelectedValue();
					String usuario = (String) listaUsuariosPermisos
							.getSelectedValue();
					listaComponentes.setEnabled(false);
					campoPermiso.setEnabled(false);
					listaUsuariosPermisos.setEnabled(false);

					cmi.cambiarPermisoComponenteUsuario(usuario, componente,
							permiso);
					listaUsuariosPermisos.setEnabled(true);
					listaComponentes.setEnabled(true);
					campoPermiso.setEnabled(true);
				}
			}
			else if (botonRol.isSelected())
				if (( listaRolesPermisos.getSelectedIndex() >= 0 )
						&& ( listaComponentes.getSelectedIndex() >= 0 ))
				{
					String componente = (String) listaComponentes
							.getSelectedValue();
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

	private void actualizarPermiso()
	{
		int permiso = -1;

		if (botonUsuario.isSelected())
		{
			if (( listaUsuariosPermisos.getSelectedIndex() >= 0 )
					&& ( listaComponentes.getSelectedIndex() >= 0 ))
			{
				String componente = (String) listaComponentes
						.getSelectedValue();
				String usuario = (String) listaUsuariosPermisos
						.getSelectedValue();
				listaComponentes.setEnabled(false);
				campoPermiso.setEnabled(false);
				listaUsuariosPermisos.setEnabled(false);
				permiso = cmi.obtenerPermisoComponenteUsuario(usuario,
						componente);
				campoPermiso.setEnabled(true);
				listaUsuariosPermisos.setEnabled(true);
				listaComponentes.setEnabled(true);
				campoPermiso.setText(permiso + "");
				// System.out.println(permiso);
			}
		}
		else if (botonRol.isSelected())
			if (( listaRolesPermisos.getSelectedIndex() >= 0 )
					&& ( listaComponentes.getSelectedIndex() >= 0 ))
			{
				String componente = (String) listaComponentes
						.getSelectedValue();
				String rol = (String) listaRolesPermisos.getSelectedValue();
				listaComponentes.setEnabled(false);
				campoPermiso.setEnabled(false);
				listaRolesPermisos.setEnabled(false);
				permiso = cmi.obtenerPermisoComponenteRol(rol, componente);
				campoPermiso.setEnabled(true);
				listaRolesPermisos.setEnabled(true);
				listaComponentes.setEnabled(true);
				campoPermiso.setText(permiso + "");
			}
	}

	private class ListenerListas implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			if (!e.getValueIsAdjusting()) actualizarPermiso();
		}

	}

	private void botonAplicar_actionPerformed(ActionEvent e)
	{
		cambiarPermiso();
	}

	private void botonNRol_actionPerformed(ActionEvent e)
	{
		campoClave.setEnabled(false);
		listaRoles.setEnabled(false);
		checkAdmin.setEnabled(false);
	}

	private void botonNUsuario_actionPerformed(ActionEvent e)
	{
		campoClave.setEnabled(true);
		listaRoles.setEnabled(true);
		checkAdmin.setEnabled(true);
	}

	private void botonRAniadir_actionPerformed(ActionEvent e)
	{

	}

	private void botonCrear_actionPerformed(ActionEvent e)
	{
		if (botonNUsuario.isSelected())
		{ // Creacion de usuario
			String usuario = null;
			String clave = null;
			String rolDefecto = null;
			boolean error = false;
			usuario = campoNombre.getText();
			clave = campoClave.getText();
			rolDefecto = listaRoles.getSeleccionado();

			if (usuario.length() == 0)
			{
				JOptionPane.showMessageDialog(null,
						"No pudes dejar el campo nombre vacio", "Error",
						JOptionPane.ERROR_MESSAGE);
				error = true;
			}

			if (( clave.length() == 0 ) && !error)
			{
				JOptionPane.showMessageDialog(null,
						"No pudes dejar el campo clave vacio", "Error",
						JOptionPane.ERROR_MESSAGE);
				error = true;
			}

			if (( listaRoles.getSelectedIndex() == 0 ) && !error)
			{
				JOptionPane.showMessageDialog(null, "Debes seleccionar un rol",
						"Error", JOptionPane.ERROR_MESSAGE);
				error = true;
			}
			if (!error)
			{
				String mensaje = ClienteMetaInformacion.obtenerCMI()
						.nuevoUsuario(usuario, clave, rolDefecto,
								checkAdmin.isSelected());

				String mensaje2 = ClienteMetaInformacion.obtenerCMI()
						.nuevoRolPermitido(usuario, rolDefecto);

				if (mensaje.length() == 0 && mensaje2.length() == 0)
					JOptionPane.showMessageDialog(null, "Usuario creado",
							"Creacion de usuario",
							JOptionPane.INFORMATION_MESSAGE);
				if (mensaje.length() != 0)
					JOptionPane.showMessageDialog(null, mensaje, "Error",
							JOptionPane.ERROR_MESSAGE);
				if (mensaje2.length() != 0)
					JOptionPane.showMessageDialog(null, mensaje2, "Error",
							JOptionPane.ERROR_MESSAGE);
			}
		}
		else
		{ // Creacion de rol
			String nombre = campoNombre.getText();
			boolean error = false;
			if (nombre.length() == 0)
			{
				JOptionPane.showMessageDialog(null,
						"No pudes dejar el campo nombre vacio", "Error",
						JOptionPane.ERROR_MESSAGE);
				error = true;
			}
			if (!error)
			{
				String mensaje = ClienteMetaInformacion.obtenerCMI().nuevoRol(
						nombre);
				if (mensaje.length() == 0)
					JOptionPane.showMessageDialog(null, "Rol creado",
							"Creacion de rol", JOptionPane.INFORMATION_MESSAGE);
				else JOptionPane.showMessageDialog(null, mensaje, "Error",
						JOptionPane.ERROR_MESSAGE);

			}

		}
	}

	private void botonEliminar_actionPerformed(ActionEvent e)
	{
		if (botonEUsuario.isSelected())
		{ // Eliminamos un usuario
			if (listaEliminarUsuarios.getSelectedIndex() == 0)
				JOptionPane.showMessageDialog(null,
						"Debes seleccionar un usuario", "Error",
						JOptionPane.ERROR_MESSAGE);
			else
			{
				String usuario = listaEliminarUsuarios.getSeleccionado();
				String mensaje = ClienteMetaInformacion.obtenerCMI()
						.eliminarUsuario(usuario);
				if (mensaje.length() == 0)
				{
					if (!usuario.equals(ClienteMetaInformacion.usuario))
						JOptionPane.showMessageDialog(null,
								"Usuario eliminado", "OK",
								JOptionPane.INFORMATION_MESSAGE);
				}
				else JOptionPane.showMessageDialog(null, mensaje, "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (listaEliminarRoles.getSelectedIndex() == 0)
			JOptionPane.showMessageDialog(null, "Debes seleccionar un rol",
					"Error", JOptionPane.ERROR_MESSAGE);
		else
		{
			String rol = listaEliminarRoles.getSeleccionado();
			String mensaje = ClienteMetaInformacion.obtenerCMI().eliminarRol(
					rol);
			if (mensaje.length() == 0)
				JOptionPane.showMessageDialog(null, "Rol eliminado", "OK",
						JOptionPane.INFORMATION_MESSAGE);
			else JOptionPane.showMessageDialog(null, mensaje, "Error",
					JOptionPane.ERROR_MESSAGE);

		}
	}

	@SuppressWarnings( "unchecked" )
	private void listaCambiarUsuarios_actionPerformed(ActionEvent e)
	{
		listaCambiarRolesPermitidos.eliminarElementos();
		if (listaCambiarUsuarios.getSelectedIndex() != 0)
		{
			String usuario = listaCambiarUsuarios.getSeleccionado();
			Vector v = ClienteMetaInformacion.obtenerCMI()
					.obtenerRolesPermitidos(usuario);
			for (int i = 0; i < v.size(); i++)
				listaCambiarRolesPermitidos.aniadirElemento((String) v
						.elementAt(i));
		}
	}

	private void botonEfectuarCambio_actionPerformed(ActionEvent e)
	{
		if (botonRAniadir.isSelected())
		{
			if (( listaCambiarUsuarios.getSelectedIndex() != 0 )
					&& ( listaCambiarRoles.getSelectedIndex() != 0 ))
			{
				String usuario = listaCambiarUsuarios.getSeleccionado();
				String rol = listaCambiarRoles.getSeleccionado();
				String mensaje = ClienteMetaInformacion.obtenerCMI()
						.nuevoRolPermitido(usuario, rol);
				if (mensaje.length() == 0)
				{
					JOptionPane.showMessageDialog(null, "Rol añadido", "OK",
							JOptionPane.INFORMATION_MESSAGE);
					listaCambiarRolesPermitidos.aniadirElemento(rol);
				}
				else JOptionPane.showMessageDialog(null, mensaje, "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (( listaCambiarUsuarios.getSelectedIndex() != 0 )
				&& ( listaCambiarRoles.getSelectedIndex() != 0 ))
		{
			String usuario = listaCambiarUsuarios.getSeleccionado();
			String rol = listaCambiarRoles.getSeleccionado();
			String mensaje = ClienteMetaInformacion.obtenerCMI()
					.eliminarRolPermitido(usuario, rol);
			if (mensaje.length() == 0)
			{
				JOptionPane.showMessageDialog(null, "Rol eliminado", "OK",
						JOptionPane.INFORMATION_MESSAGE);
				listaCambiarRolesPermitidos.eliminarElemento(rol);
			}
			else JOptionPane.showMessageDialog(null, mensaje, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private class PanelMetaInformacion_botonRol_actionAdapter implements
			java.awt.event.ActionListener
	{
		PanelMetaInformacion adaptee;

		PanelMetaInformacion_botonRol_actionAdapter(
				PanelMetaInformacion adaptee )
		{
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e)
		{
			adaptee.botonRol_actionPerformed(e);
		}
	}

	private class PanelMetaInformacion_botonUsuario_actionAdapter implements
			java.awt.event.ActionListener
	{
		PanelMetaInformacion adaptee;

		PanelMetaInformacion_botonUsuario_actionAdapter(
				PanelMetaInformacion adaptee )
		{
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e)
		{
			adaptee.botonUsuario_actionPerformed(e);
		}
	}

	private class PanelMetaInformacion_botonAplicar_actionAdapter implements
			java.awt.event.ActionListener
	{
		PanelMetaInformacion adaptee;

		PanelMetaInformacion_botonAplicar_actionAdapter(
				PanelMetaInformacion adaptee )
		{
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e)
		{
			adaptee.botonAplicar_actionPerformed(e);
		}
	}

	private class PanelMetaInformacion_botonNRol_actionAdapter implements
			java.awt.event.ActionListener
	{
		PanelMetaInformacion adaptee;

		PanelMetaInformacion_botonNRol_actionAdapter(
				PanelMetaInformacion adaptee )
		{
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e)
		{
			adaptee.botonNRol_actionPerformed(e);
		}
	}

	private class PanelMetaInformacion_botonNUsuario_actionAdapter implements
			java.awt.event.ActionListener
	{
		PanelMetaInformacion adaptee;

		PanelMetaInformacion_botonNUsuario_actionAdapter(
				PanelMetaInformacion adaptee )
		{
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e)
		{
			adaptee.botonNUsuario_actionPerformed(e);
		}
	}

	private class PanelMetaInformacion_botonRAniadir_actionAdapter implements
			java.awt.event.ActionListener
	{
		PanelMetaInformacion adaptee;

		PanelMetaInformacion_botonRAniadir_actionAdapter(
				PanelMetaInformacion adaptee )
		{
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e)
		{
			adaptee.botonRAniadir_actionPerformed(e);
		}
	}

	private class PanelMetaInformacion_botonCrear_actionAdapter implements
			java.awt.event.ActionListener
	{
		PanelMetaInformacion adaptee;

		PanelMetaInformacion_botonCrear_actionAdapter(
				PanelMetaInformacion adaptee )
		{
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e)
		{
			adaptee.botonCrear_actionPerformed(e);
		}
	}

	private class PanelMetaInformacion_botonEliminar_actionAdapter implements
			java.awt.event.ActionListener
	{
		PanelMetaInformacion adaptee;

		PanelMetaInformacion_botonEliminar_actionAdapter(
				PanelMetaInformacion adaptee )
		{
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e)
		{
			adaptee.botonEliminar_actionPerformed(e);
		}
	}

	private class PanelMetaInformacion_listaCambiarUsuarios_actionAdapter
			implements java.awt.event.ActionListener
	{
		PanelMetaInformacion adaptee;

		PanelMetaInformacion_listaCambiarUsuarios_actionAdapter(
				PanelMetaInformacion adaptee )
		{
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e)
		{
			adaptee.listaCambiarUsuarios_actionPerformed(e);
		}
	}

	private class PanelMetaInformacion_botonEfectuarCambio_actionAdapter
			implements java.awt.event.ActionListener
	{
		PanelMetaInformacion adaptee;

		PanelMetaInformacion_botonEfectuarCambio_actionAdapter(
				PanelMetaInformacion adaptee )
		{
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e)
		{
			adaptee.botonEfectuarCambio_actionPerformed(e);
		}
	}
}
