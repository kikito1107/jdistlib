package aplicacion.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import plugins.DAbstractPlugin;
import plugins.PluginContainer;

import Deventos.DEvent;
import Deventos.DJChatEvent;
import Deventos.DMIEvent;
import Deventos.enlaceJS.DConector;
import aplicacion.gui.util.EnviarMensaje;
import aplicacion.gui.util.VentanaCambiarRol;
import aplicacion.gui.util.VisorPropiedadesFichero;
import awareness.ClienteMetaInformacion;

import componentes.base.DComponente;
import componentes.base.DComponenteBase;
import componentes.gui.docs.DIArbolDocumentos;
import componentes.gui.usuarios.ArbolUsuariosConectadosRol;
import componentes.util.Separador;
import fisica.ClienteFicheros;
import fisica.documentos.Documento;
import fisica.documentos.MIDocumento;
import fisica.eventos.DFileEvent;

/**
 * Panel de la ventana de la aplicacion principal
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class PanelPrincipal extends DComponenteBase
{
	private static final long serialVersionUID = 1L;

	private JPanel panelLateral = null;

	private JLabel jLabel = null;

	private JToolBar herramientasUsuarios = null;

	private JList listaAplicaciones = null;

	private JButton botonInfo = null;

	private JButton editarUsuario = null;

	private JButton iniciarChat = null;

	private JButton iniciarVC = null;

	private JButton enviarMensaje = null;

	private JButton botonEliminarFich = null;

	private JPanel panelEspacioTrabajo = null;

	private JToolBar herramientasDocumentos = null;

	private JButton botonAbrirDoc = null;

	private JButton botonDescargar = null;

	private JButton agregarCarpeta = null;

	private JButton botonImprimirDocumento = null;

	private DIArbolDocumentos arbolDocumentos = null;

	private JLabel jLabel1 = null;

	private FramePanelDibujo frame = null;

	private ArbolUsuariosConectadosRol arbolUsuario = null;

	private JButton botonSubir = null;

	private JButton cambiarRol = null;

	private JButton reenviar = null;

	private static final String separador = "\n\n----------------------------------------------\n";

	private DefaultListModel modeloAplicaciones = null;

	private static PanelPrincipal esto = null;

	private Font fuente = new Font("Lucida Sans", Font.PLAIN, 12);

	private JProgressBar barraProgreso = null;

	private MonitorAbrir monitor = null;

	private VentanaCambiarRol vRol = null;

	// ============= INICIALIZACIÓN ===========================

	/**
	 * Constructor
	 * 
	 * @param nombre
	 *            Nombre del componente para registrarlo en el servidor de
	 *            metainformacion.
	 * @param conexionDC
	 *            Indicamos si deseamos realizar una conexion directa con
	 *            DConector para comprobacion de permisos sobre el componente
	 *            distribuido
	 * @param padre
	 *            Componente padre en el que estara situado este componente
	 */
	public PanelPrincipal( String nombre, boolean conexionDC,
			DComponenteBase padre )
	{
		super(nombre, conexionDC, padre);
		try
		{
			this.setFont(fuente);

			// dar soporte para documentos
			BorderLayout b = new BorderLayout();

			b.setHgap(6); // Generated
			b.setVgap(6);

			JPanel aux = new JPanel(b);
			aux.add(getPanelLateral(), BorderLayout.WEST);
			aux.add(getPanelEspacioTrabajo(), BorderLayout.CENTER);
			BorderLayout borderLayout = new BorderLayout();

			borderLayout.setHgap(6); // Generated
			borderLayout.setVgap(6); // Generated

			this.setLayout(borderLayout);

			this.add(new JPanel(), BorderLayout.NORTH);
			this.add(new JPanel(), BorderLayout.EAST);
			this.add(new JPanel(), BorderLayout.WEST);

			this.add(aux, BorderLayout.CENTER);

			BorderLayout barrLay = new BorderLayout();
			barrLay.setHgap(6);
			barrLay.setVgap(6);
			JPanel barr = new JPanel(barrLay);
			barr.add(new JPanel(), BorderLayout.WEST);
			barr.add(getBarraProgreso(), BorderLayout.CENTER);
			barr.add(new JPanel(), BorderLayout.EAST);
			barr.add(new JPanel(), BorderLayout.SOUTH);

			this.add(barr, BorderLayout.SOUTH);

			inicializarEditor();
			inicializarVRol();

			esto = this;

			monitor = new MonitorAbrir();

			new HebraPlugins();
			new HebraAbrir();

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Inicializa la ventana de cambio de rol
	 */
	private void inicializarVRol()
	{
		vRol = new VentanaCambiarRol();
		vRol.setVisible(false);
		vRol.pack();
		vRol.setSize(250, 400);

		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension vRolSize = vRol.getSize();
		if (vRolSize.height > screenSize.height)
			vRolSize.height = screenSize.height;
		if (vRolSize.width > screenSize.width)
			vRolSize.width = screenSize.width;
		vRol.setLocation(( screenSize.width - vRolSize.width ) / 2,
				( screenSize.height - vRolSize.height ) / 2);
	}

	/**
	 * Inicia la ventana del editor colaborativo
	 */
	private void inicializarEditor()
	{
		frame = new FramePanelDibujo(false);
		frame.setVisible(false);
		frame.pack();
		frame.setSize(800, 720);

		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		frame.setLocation(( screenSize.width - frameSize.width ) / 2,
				( screenSize.height - frameSize.height ) / 2);

		if (this.arbolDocumentos != null) this.arbolDocumentos.repaint();
	}

	// ============= GUI =================================================
	/**
	 * Obtiene el panel lateral con la lista de usuarios
	 * 
	 * @return El panel ya inicializado
	 */
	private JPanel getPanelLateral()
	{
		if (panelLateral == null)
		{
			jLabel1 = new JLabel();
			jLabel1.setFont(new Font("Lucida Sans", Font.BOLD, 12));
			jLabel1.setText("Usuarios");
			jLabel1
					.setToolTipText("Lista de los usuarios conectados, organizados por rol");
			jLabel1.setIcon(new ImageIcon("Resources/group.png"));
			jLabel = new JLabel();
			jLabel.setText("Aplicaciones");
			jLabel
					.setToolTipText("Lista de las aplicaciones disponibles. Doble Click sobre el nombre para ejecutar");
			jLabel.setFont(new Font("Lucida Sans", Font.BOLD, 12));
			jLabel.setIcon(new ImageIcon("Resources/bricks.png"));
			panelLateral = new JPanel();

			panelLateral.setMinimumSize(new Dimension(300, 200));
			panelLateral.setBorder(new LineBorder(Color.GRAY, 2));

			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.VERTICAL; // Generated
			gridBagConstraints4.gridy = 4; // Generated
			gridBagConstraints4.weightx = 1.0; // Generated
			gridBagConstraints4.gridx = 0; // Generated
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH; // Generated
			gridBagConstraints3.gridy = 3; // Generated
			gridBagConstraints3.weightx = 1.0; // Generated
			gridBagConstraints3.weighty = 1.0; // Generated
			gridBagConstraints3.gridx = 0; // Generated
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0; // Generated
			gridBagConstraints2.gridy = 2; // Generated

			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH; // Generated
			gridBagConstraints1.gridy = 1; // Generated
			gridBagConstraints1.weightx = 1.0; // Generated
			gridBagConstraints1.weighty = 1.0; // Generated
			gridBagConstraints1.gridx = 0; // Generated
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0; // Generated
			gridBagConstraints.gridy = 0; // Generated

			panelLateral.setLayout(new GridBagLayout());
			panelLateral.add(jLabel, gridBagConstraints); // Generated

			JScrollPane jscrollpane = new JScrollPane(getListaAplicaciones());
			jscrollpane.setBorder(new MatteBorder(2, 0, 2, 0, Color.GRAY));
			panelLateral.add(jscrollpane, gridBagConstraints1);
			panelLateral.add(jLabel1, gridBagConstraints2); // Generated
			panelLateral.add(getArbolUsuario(), gridBagConstraints3); // Generated
			panelLateral.add(getHerramientasUsuarios(), gridBagConstraints4); // Generated
		}
		return panelLateral;
	}

	/**
	 * Obtiene el panel con el arbol de documentos
	 * 
	 * @return Panel con el arbol de documentos ya inicializado
	 */
	private JPanel getPanelEspacioTrabajo()
	{
		if (panelEspacioTrabajo == null)
		{
			BorderLayout borderLayout2 = new BorderLayout();
			borderLayout2.setHgap(0);
			borderLayout2.setVgap(0);

			panelEspacioTrabajo = new JPanel();
			panelEspacioTrabajo.setFont(fuente);
			panelEspacioTrabajo.setLayout(borderLayout2);
			panelEspacioTrabajo.setBorder(new LineBorder(Color.GRAY, 1));
			panelEspacioTrabajo.add(getHerramientasDocumentos(),
					BorderLayout.NORTH);

			JScrollPane scrollArbol = new JScrollPane(getArbolDocumentos());

			scrollArbol.setBorder(new LineBorder(Color.GRAY, 1));

			panelEspacioTrabajo.add(scrollArbol, BorderLayout.CENTER);
		}
		return panelEspacioTrabajo;
	}

	/**
	 * Obtiene la barra de herramientas para los documentos
	 * 
	 * @return Barra de herramientas inicializada
	 */
	private JToolBar getHerramientasDocumentos()
	{
		if (herramientasDocumentos == null)
		{
			herramientasDocumentos = new JToolBar();
			herramientasDocumentos.setFont(fuente);
			herramientasDocumentos.setBorder(new LineBorder(Color.GRAY, 1));
			herramientasDocumentos.add(getBotonAbrirDoc());
			herramientasDocumentos.add(this.getReenviar());
			herramientasDocumentos.add(this.getBotonImprimirDocumento());
			herramientasDocumentos.add(new Separador());
			herramientasDocumentos.add(getButonSubir());
			herramientasDocumentos.add(getBotonDescargar());
			herramientasDocumentos.add(new Separador());
			herramientasDocumentos.add(getBotonEliminarFichero());
			herramientasDocumentos.add(this.getAgregarCarpeta());
			herramientasDocumentos.add(new Separador());
			herramientasDocumentos.add(getBotonInfo());
		}
		return herramientasDocumentos;
	}

	/**
	 * Obtiene la barra de progreso
	 * 
	 * @return Barra de progreso correctamente inicializada
	 */
	private JProgressBar getBarraProgreso()
	{
		if (barraProgreso == null)
		{
			barraProgreso = new JProgressBar();

			barraProgreso.setIndeterminate(false);

			barraProgreso.setValue(0);

			barraProgreso.setMaximumSize(new Dimension(20, 200));

			barraProgreso.setForeground(Color.GRAY);
			barraProgreso.setBackground(Color.LIGHT_GRAY);
		}
		return barraProgreso;
	}

	/**
	 * Obtiene el boton para agregar una carpeta
	 * 
	 * @return Boton para agregar una carpeta ya inicializado
	 */
	private JButton getAgregarCarpeta()
	{

		if (agregarCarpeta == null)
		{
			agregarCarpeta = new JButton();
			agregarCarpeta.setBorderPainted(false);
			agregarCarpeta.setText("");

			agregarCarpeta
					.setIcon(new ImageIcon("Resources/nueva_carpeta.png"));

			agregarCarpeta
					.setToolTipText("Agrega una carpeta nueva a la carpeta seleccionada");

			agregarCarpeta
					.addActionListener(new java.awt.event.ActionListener()
					{
						public void actionPerformed(java.awt.event.ActionEvent e)
						{
							MIDocumento f = arbolDocumentos
									.getDocumentoSeleccionado();

							if (f == null || !f.esDirectorio()) return;

							String nombre = JOptionPane
									.showInputDialog("Introduce el nuevo nombre para la carpeta");

							if (nombre == null) return;

							f = arbolDocumentos.agregarCarpeta(nombre);

							if (f == null)
							{

							}

						}
					});
		}

		return agregarCarpeta;
	}

	/**
	 * Obtiene el boton para descargar un documento
	 * 
	 * @return Boton para descargar un documento ya inicializado
	 */
	private JButton getBotonDescargar()
	{
		if (botonDescargar == null)
		{
			botonDescargar = new JButton();;
			botonDescargar.setBorderPainted(false);
			botonDescargar.setText("");

			botonDescargar
					.setIcon(new ImageIcon("Resources/page_white_put.png"));

			botonDescargar
					.setToolTipText("Descarga el documento seleccionado de forma local");

			botonDescargar
					.addActionListener(new java.awt.event.ActionListener()
					{
						public void actionPerformed(java.awt.event.ActionEvent e)
						{
							arbolDocumentos.guardarDocumentoLocalmente();
						}
					});

		}
		return botonDescargar;
	}

	/**
	 * Obtiene el boton para imprimir un documento
	 * 
	 * @return Boton para imprimir un documento ya inicializado
	 */
	private JButton getBotonImprimirDocumento()
	{
		if (botonImprimirDocumento == null)
		{
			botonImprimirDocumento = new JButton();;
			botonImprimirDocumento.setBorderPainted(false);
			botonImprimirDocumento.setText("");
			botonImprimirDocumento
					.setToolTipText("Imprime el documento seleccionado");

			botonImprimirDocumento.setIcon(new ImageIcon(
					"Resources/printer.png"));
			botonImprimirDocumento
					.addActionListener(new java.awt.event.ActionListener()
					{
						public void actionPerformed(java.awt.event.ActionEvent e)
						{
							arbolDocumentos.imprimirFichero();
						}
					});
		}

		return botonImprimirDocumento;
	}

	/**
	 * Obtiene el boton para eliminar un documento
	 * 
	 * @return Boton para eliminar un documento ya inicializado
	 */
	private JButton getBotonEliminarFichero()
	{

		if (botonEliminarFich == null)
		{
			botonEliminarFich = new JButton();
			botonEliminarFich.setText("");
			botonEliminarFich.setBorderPainted(false);

			botonEliminarFich
					.setToolTipText("Eliminar el documento o directorio seleccionado (solo elimina directorios vacios)");

			botonEliminarFich.setIcon(new ImageIcon(
					"Resources/bin.png"));
			botonEliminarFich
					.addActionListener(new java.awt.event.ActionListener()
					{
						public void actionPerformed(java.awt.event.ActionEvent e)
						{

							Object[] options =
							{ "Eliminar", "Cancelar", };

							int opcion = JOptionPane
									.showOptionDialog(
											null,
											"¿Seguro que desea eliminar el documento o directorio seleccionado?\nEsta acción no se podrá deshacer",
											"Aviso", JOptionPane.YES_NO_OPTION,
											JOptionPane.QUESTION_MESSAGE, null,
											options, options[1]);

							if (opcion == JOptionPane.YES_OPTION)
							{
								if (!arbolDocumentos.eliminarFichero())
									JOptionPane
											.showMessageDialog(null,
													"No tiene permisos suficientes para eliminar este fichero/directorio");
							}
						}
					});

		}
		return this.botonEliminarFich;
	}

	/**
	 * Obtiene el boton para obtener informacion de un documento
	 * 
	 * @return Boton para obtener informacion de un documento ya inicializado
	 */
	private JButton getBotonInfo()
	{
		if (botonInfo == null)
		{
			botonInfo = new JButton();
			botonInfo.setText("");
			botonInfo.setBorderPainted(false);
			botonInfo.setIcon(new ImageIcon("Resources/information.png"));
			botonInfo
					.setToolTipText("Obtiene la informacion del documento o directorio seleccionado. Permite cambiar los permisos de acceso");

			botonInfo.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					MIDocumento f = arbolDocumentos.getDocumentoSeleccionado();

					if (f == null) return;

					f = VisorPropiedadesFichero.verInfoFichero(f, null);

					if (f != null)
					{
						arbolDocumentos.cambiarMIDocumento(f);
					}
				}
			});
		}
		return botonInfo;
	}

	/**
	 * Obtiene el boton para abrir un documento
	 * 
	 * @return Boton para abrir un documento ya inicializado
	 */
	private JButton getBotonAbrirDoc()
	{
		if (botonAbrirDoc == null)
		{
			botonAbrirDoc = new JButton();

			botonAbrirDoc.setIcon(new ImageIcon(
					"Resources/folder_page_white.png"));

			botonAbrirDoc
					.setToolTipText("Abre el documento seleccionado para visualizarlo o anotar sobre el");

			botonAbrirDoc.setBorderPainted(false);
			botonAbrirDoc.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					monitor.notificarAbrir();
				}
			});
		}
		return botonAbrirDoc;
	}

	/**
	 * Obtiene el boton para cambiar de rol
	 * 
	 * @return Boton para cambiar de rol ya inicializado
	 */
	private JButton getBotonCambiarRol()
	{
		if (cambiarRol == null)
		{
			cambiarRol = new JButton();

			cambiarRol.setIcon(new ImageIcon("Resources/user_edit.png"));

			cambiarRol.setBorderPainted(false);
			cambiarRol.setToolTipText("Cambiar el rol actual");

			cambiarRol.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					mostrarVentanaCambiarRol();

				}

			});
		}
		return cambiarRol;
	}

	/**
	 * Muestra la ventana de cambio de rol
	 */
	private void mostrarVentanaCambiarRol()
	{
		vRol.setVisible(true);
	}

	/**
	 * Obtiene el arbol de documentos
	 * 
	 * @return Arbol de documentos ya inicializado
	 */
	private DIArbolDocumentos getArbolDocumentos()
	{
		if (arbolDocumentos == null)
		{
			arbolDocumentos = new DIArbolDocumentos("ArbolDoc", false, this,
					DConector.raiz);

			arbolDocumentos.setFont(fuente);

			arbolDocumentos.arbol
					.addMouseListener(new java.awt.event.MouseAdapter()
					{
						@Override
						public void mouseClicked(java.awt.event.MouseEvent e)
						{
							if (e.getClickCount() == 2)
							{
								monitor.notificarAbrir();
							}
						}
					});
		}
		return arbolDocumentos;
	}

	/**
	 * Obtiene el boton para editar usuarios
	 * 
	 * @return Boton para editar el sistema ya inicializado
	 */
	private JButton getEditarUsuario()
	{
		if (editarUsuario == null)
		{
			editarUsuario = new JButton();
			editarUsuario.setIcon(new ImageIcon("Resources/group_gear.png"));
			editarUsuario.setBorderPainted(false);
			editarUsuario
					.setToolTipText("Edita la informacion del sistema sobre usuarios, roles y permisos sobre componentes. Solo para administradores");

			if (!ClienteMetaInformacion.obtenerCMI().permisosAdministracion())
			{
				editarUsuario.setEnabled(false);
			}

			else
			{
				editarUsuario
						.addActionListener(new java.awt.event.ActionListener()
						{
							public void actionPerformed(
									java.awt.event.ActionEvent e)
							{
								ClienteMetaInformacion.obtenerCMI()
										.mostrarDialogo();
							}
						});
			}
		}
		return editarUsuario;
	}

	/**
	 * Obtiene la barra de herramientas para los usuarios
	 * 
	 * @return Barra de herramientas para los usuarios ya inicializada
	 */
	private JToolBar getHerramientasUsuarios()
	{
		if (herramientasUsuarios == null)
		{
			herramientasUsuarios = new JToolBar();
			// herramientasUsuarios.setBorder(new LineBorder(Color.GRAY, 1));
			herramientasUsuarios.setSize(new Dimension(183, 32));
			herramientasUsuarios.setLocation(new Point(3, 364));

			Separador s1 = new Separador();
			Separador s2 = new Separador();
			s1.setMinimumSize(new Dimension(20, 15));
			s2.setMinimumSize(new Dimension(20, 15));
			herramientasUsuarios.setFloatable(false);
			herramientasUsuarios.add(getEditarUsuario()); // edicion de
			// usuarios
			herramientasUsuarios.add(this.getBotonCambiarRol()); // cambio de
			// rol
			herramientasUsuarios.add(s1); // separador
			herramientasUsuarios.add(getIniciarChat()); // chat entre usuarios
			herramientasUsuarios.add(getEnviarMensaje()); // envio de mensajes
			herramientasUsuarios.add(this.getIniciarVC()); // videoconferencia
		}
		return herramientasUsuarios;
	}

	/**
	 * Obtiene el arbol de usuarios conectados
	 * 
	 * @return Arbol de usuarios conectados ya inicializado
	 */
	private ArbolUsuariosConectadosRol getArbolUsuario()
	{
		if (arbolUsuario == null)
		{
			arbolUsuario = new ArbolUsuariosConectadosRol(
					"ListaUsuariosConectadosRol", false, this);

			arbolUsuario.setFont(fuente);
			arbolUsuario.setPreferredSize(new Dimension(180, 140));
		}
		return arbolUsuario;
	}

	/**
	 * Obtiene el boton para iniciar un chat entre usuarios
	 * 
	 * @return Boton para iniciar un chat ya inicializado
	 */
	private JButton getIniciarChat()
	{
		if (iniciarChat == null)
		{
			iniciarChat = new JButton();
			iniciarChat.setIcon(new ImageIcon("Resources/comment.gif"));
			iniciarChat.setBorderPainted(false);
			iniciarChat
					.setToolTipText("Inicia un chat privado con el usuario seleccionado");

			iniciarChat.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					for (int i = 0; i < PluginContainer.numPlugins(); ++i)
						if (PluginContainer.getPlugin(i).getName().equals(
								"Chat"))
						{
							String usuario = arbolUsuario
									.getUsuarioSeleccionado();

							if (( usuario != null )
									&& !usuario.equals(DConector.Dusuario))
							{
								DJChatEvent evento = new DJChatEvent();
								evento.tipo = new Integer(
										DJChatEvent.MENSAJE_PRIVADO);
								evento.receptores.add(usuario);
								evento.mensaje = "Solicita una nueva conversación";

								PluginContainer.getPlugin(i).enviarEvento(
										evento);
							}
							else if (usuario == null)
							{
								JOptionPane
										.showMessageDialog(null,
												"Debe seleccionar el usuario con el que mantener la conversacion");
							}
							else if (usuario.equals(DConector.Dusuario))
							{
								JOptionPane
										.showMessageDialog(null,
												"No puedes mantener una conversación contigo mismo");
							}
						}

				}
			});
		}
		return iniciarChat;
	}

	/**
	 * Obtiene el boton para iniciar una videoconferencia entre usuarios
	 * 
	 * @return Boton para iniciar una videoconferencia ya inicializado
	 */
	private JButton getIniciarVC()
	{
		if (iniciarVC == null)
		{
			iniciarVC = new JButton();
			iniciarVC.setIcon(new ImageIcon("Resources/webcam.png"));
			iniciarVC.setBorderPainted(false);
			iniciarVC
					.setToolTipText("Inicia una videoconferencia con el usuario seleccionado");

			iniciarVC.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					for (int i = 0; i < PluginContainer.numPlugins(); ++i)
						if (PluginContainer.getPlugin(i).getName().equals(
								"Chat"))
						{

							String usuario = arbolUsuario
									.getUsuarioSeleccionado();

							if (( usuario != null )
									&& !usuario.equals(DConector.Dusuario))
							{
								DJChatEvent evento = new DJChatEvent();

								evento.receptores.add(usuario);

								evento.tipo = new Integer(
										DJChatEvent.INICIAR_VC.intValue());
								try
								{
									evento.ipVC = InetAddress.getLocalHost()
											.getHostAddress();

									evento.mensaje = "Solicita una nueva conversación";

									PluginContainer.getPlugin(i).enviarEvento(
											evento);

								}
								catch (UnknownHostException e1)
								{
									JOptionPane
											.showMessageDialog(null,
													"Ha ocurrido un error en la comunicación. Inténtelo mas tarde");
									return;
								}

							}
							else if (usuario == null)
							{
								JOptionPane
										.showMessageDialog(null,
												"Debe seleccionar el usuario con el que mantener la conversacion");
							}
							else if (usuario.equals(DConector.Dusuario))
							{
								JOptionPane
										.showMessageDialog(null,
												"No puedes mantener una conversación contigo mismo");
							}
						}

				}
			});
		}
		return iniciarVC;
	}

	/**
	 * Obtiene el boton para enviar mensajes entre usuarios
	 * 
	 * @return Boton para enviar mensajes ya inicializado
	 */
	private JButton getEnviarMensaje()
	{
		if (enviarMensaje == null)
		{
			enviarMensaje = new JButton();
			enviarMensaje.setIcon(new ImageIcon("Resources/icon_email.gif"));
			enviarMensaje.setBorderPainted(false);
			enviarMensaje
					.setToolTipText("Envia un mensaje a un usuario para que lo lea la proxima vez que se conecte");

			enviarMensaje.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					MIDocumento f = EnviarMensaje.getMensaje(arbolUsuario
							.getUsuarioSeleccionado(), "", "");

					if (f != null)
					{
						enviarMail(f);
					}
				}
			});
		}
		return enviarMensaje;
	}

	/**
	 * Obtiene el boton para subir documentos
	 * 
	 * @return Boton para subir documentos ya inicializado
	 */
	private JButton getButonSubir()
	{
		if (botonSubir == null)
		{
			botonSubir = new JButton();;
			botonSubir.setBorderPainted(false);
			botonSubir.setText("");
			botonSubir
					.setToolTipText("Sube un fichero local para compartirlo con el resto de usuarios");

			botonSubir.setIcon(new ImageIcon("Resources/subir_documento.png"));
			botonSubir.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					subirFicheroServidor();
				}
			});
		}
		return botonSubir;
	}

	/**
	 * Obtiene el boton para reenviar mensajes
	 * 
	 * @return Boton para reenviar mensajes ya inicializado
	 */
	private JButton getReenviar()
	{
		if (reenviar == null)
		{
			reenviar = new JButton();;
			reenviar.setBorderPainted(false);
			reenviar.setText("");
			reenviar.setToolTipText("Reenvia el mensaje seleccionado");

			reenviar.setIcon(new ImageIcon("Resources/email_go.png"));
			reenviar.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					MIDocumento f = arbolDocumentos.getDocumentoSeleccionado();
					if (f.getTipo().equals(MIDocumento.TIPO_MENSAJE))
					{
						f = arbolDocumentos.recuperarMail();

						if (f != null)
						{
							f = EnviarMensaje.getMensaje("", "Re "
									+ f.toString(), separador + f.getMensaje());
							if (f != null) enviarMail(f);
						}
					}
				}
			});
		}
		return reenviar;
	}

	/**
	 * Sube un fichero al servidor en la carpeta que esta seleccionada
	 * actualmente. Si no hay seleccionada ninguana carpeta no hace nada. Si se
	 * intenta subir un fichero duplicado, muestra un mensaje de error indicando
	 * si el usuario desea: cancelar, cambiar el nombre o sobreescribir
	 */
	private void subirFicheroServidor()
	{
		arbolDocumentos.subirFicheroServidor();
	}

	/**
	 * Obtiene la lista de aplicaciones
	 * 
	 * @return Lista de aplicaciones ya inicializada
	 */
	private JList getListaAplicaciones()
	{
		if (listaAplicaciones == null)
		{
			listaAplicaciones = new PluginList(getModelo());
			listaAplicaciones.setFont(fuente);

			listaAplicaciones
					.addMouseListener(new java.awt.event.MouseAdapter()
					{
						@Override
						public void mouseClicked(java.awt.event.MouseEvent e)
						{
							if (e.getClickCount() == 2)
								try
								{
									if (PluginContainer.numPlugins() > 0
											&& listaAplicaciones
													.getSelectedIndex() > -1)
									{
										DAbstractPlugin seleccionado = (DAbstractPlugin) listaAplicaciones
												.getSelectedValue();
										seleccionado.start();
									}
								}
								catch (Exception e1)
								{
									e1.printStackTrace();
								}
						}
					});
		}
		return listaAplicaciones;
	}

	/**
	 * Obtiene el modelo de datos de la lista de aplicaciones
	 * 
	 * @return El modelo ya inicializado
	 */
	private DefaultListModel getModelo()
	{

		if (this.modeloAplicaciones == null)
		{
			modeloAplicaciones = new DefaultListModel();

			for (int i = 0; i < PluginContainer.numPlugins(); ++i)
			{
				if (PluginContainer.getPlugin(i).shouldShowIt())
					modeloAplicaciones.addElement(PluginContainer.getPlugin(i));
			}
		}

		return modeloAplicaciones;
	}

	/**
	 * Envia un mensaje
	 * 
	 * @param f
	 *            Metainformacion del mensaje
	 */
	private void enviarMail(MIDocumento f)
	{
		DFileEvent dfe = MIDocumento.enviarMail(f);

		if (dfe != null) enviarEvento(dfe);
	}

	/**
	 * Abre el documento actualmente seleccionado en el arbol de documentos.
	 */
	private void accionAbrir()
	{
		MIDocumento f = arbolDocumentos.getDocumentoSeleccionado();

		if (f == null || f.esDirectorio()) return;

		if (!f.comprobarPermisos(DConector.Dusuario, DConector.Drol,
				MIDocumento.PERMISO_LECTURA))
		{
			JOptionPane
					.showMessageDialog(null,
							"No tienes permisos para abrir el fichero "
									+ f.getNombre());
			return;
		}

		// le pasamos al editor el path del nuevo documento
		Documento p = new Documento();
		p.setMetainformacion(arbolDocumentos.getDocumentoSeleccionado());
		p.setPath(f.getRutaLocal());

		frame.setDocumento(p);
		frame.getLienzo().setPathDocumento(f.getRutaLocal());

		barraProgreso.setIndeterminate(true);

		frame.getLienzo().getLienzo().sincronizar();

		barraProgreso.setIndeterminate(false);

		if (!frame.getLienzo().getLienzo().getDocumento().getPath().equals(""))
		{
			frame.setVisible(true);
		}
		else
		{
			frame.alCerrarVentana(null);
		}

		if (this.arbolDocumentos != null) arbolDocumentos.repaint();
	}

	// ========= DCOMPONENTES
	// =========================================================
	/**
	 * Obtiene el numero de componentes hijo que son distribuidos en este
	 * componente
	 * 
	 * @return Numero de componentes hijo.
	 */
	@Override
	public int obtenerNumComponentesHijos()
	{
		return 2;
	}

	/**
	 * Obtiene el componente i-esimo de este componente
	 * 
	 * @param i
	 *            Indice del componente a obtener
	 * @return Componente obtenido o null si el componente i no existe
	 */
	@Override
	public DComponente obtenerComponente(int i)
	{
		DComponente dc = null;
		switch (i)
		{
			case 0:
				dc = arbolUsuario;
				break;
			case 1:
				dc = this.arbolDocumentos;
				break;
			default:
				break;
		}
		return dc;
	}

	// ========= EVENTOS =================================================

	@Override
	public void procesarEventoHijo(DEvent evento)
	{
		this.enviarEvento(evento);
	}

	// *******************************************************************************
	// PROCESADO DE EVENTOS
	// *******************************************************************************
	@Override
	public void procesarEvento(DEvent evento)
	{
		System.err.println("Enviar evento " + evento.tipo);

		if (evento.tipo.intValue() == DFileEvent.NOTIFICAR_INSERTAR_FICHERO)
		{
			DFileEvent dfe = (DFileEvent) evento;

			if (dfe.fichero.comprobarPermisos(DConector.Dusuario,
					DConector.Drol, MIDocumento.PERMISO_LECTURA))
			{

				DefaultTreeModel modelo = arbolDocumentos.getModelo();
				DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) modelo
						.getRoot();

				int id_papa = dfe.padre.getId();
				DefaultMutableTreeNode papi = DIArbolDocumentos.buscarFichero(
						raiz, id_papa);

				modelo.insertNodeInto(new DefaultMutableTreeNode(dfe.fichero),
						papi, 0);

			}

			repaint();
		}
		else if (evento.tipo.intValue() == DFileEvent.NOTIFICAR_MODIFICACION_FICHERO)
		{

			DFileEvent dfe = (DFileEvent) evento;
			DefaultTreeModel modelo = arbolDocumentos.getModelo();
			DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) modelo
					.getRoot();

			int id_doc = dfe.fichero.getId();
			DefaultMutableTreeNode nodo = DIArbolDocumentos.buscarFichero(raiz,
					id_doc);

			if (nodo == null)
			{

				if (dfe.fichero.getUsuario().getNombreUsuario().equals(
						DConector.Dusuario)
						|| dfe.fichero.comprobarPermisos(DConector.Dusuario,
								DConector.Drol, MIDocumento.PERMISO_LECTURA))
				{
					DefaultMutableTreeNode padre = DIArbolDocumentos
							.buscarFichero(raiz, dfe.padre.getId());
					modelo.insertNodeInto(new DefaultMutableTreeNode(
							dfe.fichero), padre, modelo.getChildCount(padre));
				}
			}
			else
			{

				if (!evento.usuario.equals(DConector.Dusuario))
					modelo.removeNodeFromParent(nodo);

				// si tenemos permiso de acceso
				if (dfe.fichero.getUsuario().getNombreUsuario().equals(
						DConector.Dusuario)
						|| dfe.fichero.comprobarPermisos(DConector.Dusuario,
								DConector.Drol, MIDocumento.PERMISO_LECTURA))
				{

					// buscamos al nuevo padre
					DefaultMutableTreeNode padre = DIArbolDocumentos
							.buscarFichero(raiz, dfe.padre.getId());

					if (padre == null)
					{
						return;
					}
					else
					{
						// nos aseguramos de que no seamos nosotros los que
						// hemos movido el nodo
						if (!evento.usuario.equals(DConector.Dusuario))
						// colgamos el nodo del nuevo padre
							modelo.insertNodeInto(nodo, padre, modelo
									.getChildCount(padre));

						// actualizamos la informacion del documento
						nodo.setUserObject(dfe.fichero);
					}
				}
				else
				{
					if (evento.usuario.equals(DConector.Dusuario))
						modelo.removeNodeFromParent(nodo);
				}
			}

			repaint();

			comprobarPermisosDocumentoActual(dfe.fichero, false);
			if (this.arbolDocumentos != null) arbolDocumentos.repaint();

			String pathEditor = frame.getLienzo().getPathDocumento();

			if (pathEditor != null
					&& pathEditor.equals(dfe.fichero.getRutaLocal())
					&& !dfe.fichero.comprobarPermisos(DConector.Dusuario,
							DConector.Drol, MIDocumento.PERMISO_LECTURA))
			{

				JOptionPane
						.showMessageDialog(frame,
								"Los permisos del fichero han cambiado\nNo puede seguir editandolo");
				frame.dispose();
				frame.setDocumento(new Documento());
			}

		}
		else if (evento.tipo.intValue() == DFileEvent.NOTIFICAR_ELIMINAR_FICHERO)
		{

			DFileEvent dfe = (DFileEvent) evento;
			DefaultTreeModel modelo = arbolDocumentos.getModelo();
			DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) modelo
					.getRoot();

			int id_doc = dfe.fichero.getId();
			DefaultMutableTreeNode nodo = DIArbolDocumentos.buscarFichero(raiz,
					id_doc);

			if (nodo == null) return;

			modelo.removeNodeFromParent(nodo);
			comprobarPermisosDocumentoActual(dfe.fichero, true);

			String pathEditor = frame.getLienzo().getPathDocumento();

			if (pathEditor != null
					&& pathEditor.equals(dfe.fichero.getRutaLocal()))
			{
				JOptionPane
						.showMessageDialog(frame,
								"El fichero ha sido eliminado\nNo puede seguir editandolo");
				frame.setDocumento(new Documento());
				frame.dispose();
			}

			repaint();
		}
	}

	/**
	 * Notifica la modificacion de un fichero al servidor
	 * 
	 * @param f
	 *            evento a enviar
	 */
	public static void notificarModificacionFichero(DFileEvent f)
	{
		esto.enviarEvento(f);
		ClienteFicheros.obtenerClienteFicheros().modificarFichero(f.fichero,
				DConector.Daplicacion);
	}

	/**
	 * Procesa un evento de metainformacion
	 * 
	 * @param e
	 *            Evento de metainformacion
	 */
	@Override
	public void procesarMetaInformacion(DMIEvent e)
	{
		super.procesarMetaInformacion(e);

		if (e.tipo.intValue() == DMIEvent.NOTIFICACION_CAMBIO_ROL_USUARIO
				.intValue()
				&& e.usuario.equals(DConector.Dusuario))
		{
			ClienteFicheros.cf.inicializar();
			// arbolDocumentos.setRaiz(ClienteFicheros.cf.getArbolDoc());
		}
	}

	// ========= PERMISOS
	// ============================================================

	/**
	 * Comprueba que los permisos actuales del documentos permiten que este siga
	 * editandose
	 * 
	 * @param f
	 *            fichero a comprobar
	 * @param eliminado
	 *            indica si el documentos ha sido editado
	 */
	public void comprobarPermisosDocumentoActual(MIDocumento f,
			boolean eliminado)
	{
		if (!f.comprobarPermisos(DConector.Dusuario, DConector.Drol,
				MIDocumento.PERMISO_LECTURA)
				|| eliminado)
			if (frame.isVisible()
					&& frame.getLienzo().getLienzo().getDocumento().getPath()
							.equals(f.getRutaLocal()))
			{

				JOptionPane
						.showMessageDialog(
								null,
								"Los permisos del fichero han cambiado y usted"
										+ "\n no puede seguir accediendo a este documento");

				this.frame.setVisible(false);
				Documento d = new Documento();
				d.setPath("");
				frame.setDocumento(d);
			}
	}

	// ============= SALIR ==============================================
	/**
	 * Accion a efectuar al salir de la aplicacion
	 */
	public void salir()
	{
		if (frame == null) return;
		if (frame.getLienzo() == null) return;
		if (frame.getLienzo().getLienzo() == null) return;
		if (frame.getLienzo().getLienzo().getDocumento() == null) return;
		if (frame.getLienzo().getLienzo().getDocumento().getPath() == null)
			return;

		if (!frame.getLienzo().getLienzo().getDocumento().getPath().equals(""))
			DConector.obtenerDC().cerrarFichero(
					frame.getLienzo().getLienzo().getDocumento().getPath());
	}

	// ============= HEBRAS ==============================================
	/**
	 * Hebra que se encarga de abrir los documentos
	 */
	private class HebraAbrir implements Runnable
	{
		/**
		 * Constructor
		 */
		public HebraAbrir()
		{
			Thread hebra = new Thread(this);
			hebra.start();
		}

		/**
		 * Ejecucion de la hebra
		 */
		public void run()
		{
			while (true)
			{
				// esperamos a que se solicite la lectura
				monitor.abrir();

				// abrimos el documento
				accionAbrir();
			}
		}
	}

	/**
	 * Hebra encargada de mantener la lista de plugins actualizada
	 */
	private class HebraPlugins implements Runnable
	{
		/**
		 * Constructor
		 */
		public HebraPlugins()
		{
			Thread hebra = new Thread(this);
			hebra.start();
		}

		/**
		 * Ejecucion de la hebra
		 */
		public void run()
		{
			while (true)
			{
				// esperamos a que se actualicen los plugins
				PluginContainer.actualizar();

				// eliminamos todos los plugins de la lista
				esto.getModelo().removeAllElements();

				// cargamos de nuevo la lista
				for (int i = 0; i < PluginContainer.numPlugins(); ++i)
				{
					if (PluginContainer.getPlugin(i).shouldShowIt())
						esto.getModelo().addElement(
								PluginContainer.getPlugin(i));
				}

				// repintamos la lista
				esto.getListaAplicaciones().repaint();
			}
		}
	}

	/**
	 * Monitor que controla la apertura de documentos
	 */
	private class MonitorAbrir
	{
		/**
		 * Operaciones del monitor al abrir un documento
		 */
		public synchronized void abrir()
		{
			try
			{
				wait();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		/**
		 * Operaciones del monitor a la hora de notificar la apertura de un
		 * documento
		 */
		public synchronized void notificarAbrir()
		{
			notifyAll();
		}
	}
}
