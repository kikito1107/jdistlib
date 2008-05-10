package aplicacion.gui;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import metainformacion.ClienteMetaInformacion;
import metainformacion.MIRol;
import metainformacion.MIUsuario;
import util.Separador;
import Deventos.DEvent;
import Deventos.enlaceJS.DConector;
import aplicacion.fisica.ClienteFicheros;
import aplicacion.fisica.documentos.Documento;
import aplicacion.fisica.documentos.FicheroBD;
import aplicacion.fisica.eventos.DFileEvent;
import aplicacion.fisica.net.Transfer;
import aplicacion.gui.componentes.ArbolDocumentos;
import aplicacion.plugin.DAbstractPlugin;
import aplicacion.plugin.DPluginLoader;

import componentes.base.DComponente;
import componentes.base.DComponenteBase;
import componentes.gui.editor.FramePanelDibujo;
import componentes.gui.usuarios.ArbolUsuariosConectadosRol;


public class PanelPrincipal extends DComponenteBase
{

	private static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */

	private JPanel panelLateral = null;

	private JToolBar barraHerramientas = null;

	private JButton botonNuevo = null;

	private JButton BotonAbrir = null;

	private JButton botonGuardar = null;

	private JButton botonEliminar = null;

	private JButton botonPersonalizar = null;

	private JLabel jLabel = null;

	private JToolBar herrmientasUsuarios = null;

	private JList listaAplicaciones = null;

	private JButton botonBuscar = null;

	private JButton botonInspector = null;

	private JButton botonInfo = null;

	private JButton botonAtras = null;

	private JButton botonRecargar = null;

	private JButton botonAdelante = null;

	private JButton botonImprimir = null;

	private JButton botonAyuda = null;

	private JButton nuevoUsuario = null;

	private JButton eliminarUsuario = null;

	private JButton editarUsuario = null;

	private JButton iniciarChat = null;

	private JButton enviarMensaje = null;

	private JButton configurar = null;

	private JButton botonEliminarFich = null;

	private JPanel panelEspacioTrabajo = null;

	private JToolBar herramientasDocumentos = null;

	private JButton botonAbrirDoc = null;

	private JTree arbolDocumentos = null;

	private JLabel jLabel1 = null;

	private FramePanelDibujo frame = null;

	ArbolUsuariosConectadosRol arbolUsuario = null;

	DefaultMutableTreeNode raiz = null;

	private JButton botonSubir = null;
	
	Vector<DAbstractPlugin> v = null;
	
	
	
	
	private JButton getButonSubir()
	{
		if (botonSubir == null)
		{
			botonSubir = new JButton("");
			botonSubir.setBorder(null);
			botonSubir.setBorderPainted(false);
			botonSubir.setText("Subir Fichero ");
			botonSubir
					.setIcon(new ImageIcon("./Resources/subir_documento.png"));
			botonSubir.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					
					// obtenemos el path hasta el nodo seleccionado
					TreePath camino = arbolDocumentos
							.getSelectionPath();
					Object[] objetos = null;
					if (camino != null) 
						objetos = camino.getPath();
					else
						return;
					
					String path = "/";

					// obtenemos la carpeta en donde queremos subir el
					// nuevo fichero
					DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) objetos[objetos.length - 1];
					
					// obtenemos los datos del fichero asociados a ese
					// nodo
					FicheroBD carpeta = (FicheroBD) nodo
							.getUserObject();

					MIUsuario user = null;
					MIRol rol = null;


					// comprobamos que el nodo elegido es una carpeta
					if (carpeta.esDirectorio())
					{

						// reconstruimos el path
						for (int i = 2; i < objetos.length; ++i)
							path += objetos[i].toString() + '/';

						// recuperamos el usuario y el rol
						user = ClienteMetaInformacion.cmi
								.getUsuario(DConector.Dusuario);
						rol = ClienteMetaInformacion.cmi
								.getRol(DConector.Drol);
					}
					else
					{
						return;
					}
					
					JFileChooser jfc = new JFileChooser(
							"Guardar Documento Localmente");

					int op = jfc.showDialog(null, "Aceptar");

					if (op == JFileChooser.APPROVE_OPTION)
					{
						
						
						
						java.io.File f = jfc.getSelectedFile();
						byte[] bytes = null;
						try
						{
							// abrimos el fichero en modo lectura
							RandomAccessFile raf = new RandomAccessFile(f
									.getAbsolutePath(), "r");

							// consultamos el tamanio del fichero, reservamos
							// memoria suficiente,
							// leemos el fichero y lo cerramos
							int tamanio = (int) raf.length();

							bytes = new byte[tamanio];

							raf.read(bytes);

							raf.close();

							

							// si todo ha ido OK
							if (user != null && rol != null && path != "")
							{
								// creamos el nuevo fichero a almacenar
								FicheroBD fbd = new FicheroBD(-1, f.getName(),
										false, "rwrw--", user, rol, carpeta
												.getId(), path + f.getName(),
										"");

								// notificamos al resto de usuarios la "novedad"
								DFileEvent evento = new DFileEvent();
								evento.fichero = fbd;
								evento.padre = carpeta;
								evento.tipo = new Integer(
										DFileEvent.NOTIFICAR_INSERTAR_FICHERO
												.intValue());
								enviarEvento(evento);

								// insertamos el nuevo fichero en el servidor
								ClienteFicheros.cf.insertarNuevoFichero(fbd,
										DConector.Daplicacion);

								// enviamos el nuevo fichero al servidor
								Transfer t = new Transfer(
										ClienteFicheros.ipConexion, path + "/"
												+ f.getName());
								if (!t.sendFile(bytes))
								{
									JOptionPane
											.showMessageDialog(
													null,
													"No se ha podido subir el fichero.\nSe ha producido un error en la transmisi—n del documento",
													"Error",
													JOptionPane.ERROR_MESSAGE);
								}
								
								VisorPropiedadesFichero.verInfoFichero(fbd, null);
							}
							else
							{
								if (path == "")
									JOptionPane
											.showMessageDialog(
													null,
													"No se ha podido subir el fichero. No se ha podido escribir el fichero en la direcci—n de destino",
													"Error",
													JOptionPane.ERROR_MESSAGE);
							}
						}
						catch (FileNotFoundException ex)
						{
							JOptionPane.showMessageDialog(null,
									"El fichero no existe", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
						catch (IOException e1)
						{
							JOptionPane.showMessageDialog(null,
									"Error en la lectura del fichero", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
		}
		return botonSubir;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	public PanelPrincipal( String nombre, boolean conexionDC,
			DComponenteBase padre )
	{
		super(nombre, conexionDC, padre);
		try
		{
			
			v = DPluginLoader.getAllPlugins("aplicacion/plugin/example");
			
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(0);
			this.setLayout(null);
			this.add(getPanelLateral(), BorderLayout.WEST);
			this.add(getBarraHerramientas(), null);
			
			

			inicializarEditor();
			
			//inicializarChat();

			this.add(getPanelEspacioTrabajo(), null);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void inicializarEditor(){
		frame = new FramePanelDibujo(false);
		frame.setVisible(false);
		frame.pack();
		frame.setSize(800, 720);

		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height)
		{
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width)
		{
			frameSize.width = screenSize.width;
		}
		frame.setLocation(( screenSize.width - frameSize.width ) / 2,
				( screenSize.height - frameSize.height ) / 2);
	}

	/**
	 * This method initializes panelLateral
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPanelLateral()
	{
		if (panelLateral == null)
		{
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(60, 174, 80, 16));
			jLabel1.setText("Usuarios");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText("Aplicaciones");
			jLabel.setBounds(new Rectangle(52, 4, 90, 21));
			panelLateral = new JPanel();
			panelLateral.setLayout(null);
			panelLateral.setBounds(new Rectangle(6, 76, 188, 398));
			panelLateral.add(jLabel, gridBagConstraints);
			panelLateral.add(getHerrmientasUsuarios(), null);
			panelLateral.add(getListaAplicaciones(), null);
			panelLateral.add(getArbolUsuario(), null);
			panelLateral.setBorder(new LineBorder(Color.GRAY, 2));
			panelLateral.add(jLabel1, null);
		}
		return panelLateral;
	}

	/**
	 * This method initializes barraHerramientas
	 * 
	 * @return javax.swing.JToolBar
	 */
	private JToolBar getBarraHerramientas()
	{
		if (barraHerramientas == null)
		{
			util.Separador separator1 = new util.Separador();
			separator1.setPreferredSize(new Dimension(50, 35));

			util.Separador separator2 = new util.Separador();
			separator2.setPreferredSize(new Dimension(50, 35));

			util.Separador separator3 = new util.Separador();
			separator3.setPreferredSize(new Dimension(50, 35));

			util.Separador separator4 = new util.Separador();
			separator4.setPreferredSize(new Dimension(50, 35));

			util.Separador separator5 = new util.Separador();
			separator5.setPreferredSize(new Dimension(50, 35));

			barraHerramientas = new JToolBar();
			barraHerramientas.setBounds(new Rectangle(3, -2, 563, 67));
			barraHerramientas.add(getBotonNuevo());
			barraHerramientas.add(getBotonAbrir());
			barraHerramientas.add(getBotonGuardar());
			barraHerramientas.add(getBotonImprimir());
			barraHerramientas.add(separator1);

			barraHerramientas.add(getBotonEliminar());
			barraHerramientas.add(separator2);
			barraHerramientas.add(getBotonPersonalizar());
			barraHerramientas.add(getBotonBuscar());
			barraHerramientas.add(separator3);
			barraHerramientas.add(getBotonInspector());
			barraHerramientas.add(separator5);
			barraHerramientas.add(getBotonAtras());
			barraHerramientas.add(getBotonRecargar());
			barraHerramientas.add(getBotonAdelante());
			barraHerramientas.add(separator4);
			barraHerramientas.add(getBotonAyuda());

			barraHerramientas.setFloatable(false);
		}
		return barraHerramientas;
	}

	/**
	 * This method initializes botonNuevo
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonNuevo()
	{
		if (botonNuevo == null)
		{
			botonNuevo = new JButton();
			botonNuevo.setBorder(null);
			botonNuevo.setBorderPainted(false);
			botonNuevo.setIcon(new ImageIcon("./Resources/document_new.png"));
		}
		return botonNuevo;
	}

	/**
	 * This method initializes BotonAbrir
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonAbrir()
	{
		if (BotonAbrir == null)
		{
			BotonAbrir = new JButton();
			BotonAbrir.setBorder(null);
			BotonAbrir.setBorderPainted(false);
			BotonAbrir.setIcon(new ImageIcon("./Resources/folder_big.png"));
		}
		return BotonAbrir;
	}

	/**
	 * This method initializes botonGuardar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonGuardar()
	{
		if (botonGuardar == null)
		{
			botonGuardar = new JButton();
			botonGuardar.setBorder(null);
			botonGuardar.setBorderPainted(false);
			botonGuardar.setIcon(new ImageIcon("./Resources/save.png"));
		}
		return botonGuardar;
	}

	/**
	 * This method initializes botonEliminar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonEliminar()
	{
		if (botonEliminar == null)
		{
			botonEliminar = new JButton();
			botonEliminar.setBorder(null);
			botonEliminar.setBorderPainted(false);
			botonEliminar.setIcon(new ImageIcon("./Resources/delete.png"));
		}
		return botonEliminar;
	}

	/**
	 * This method initializes botonPersonalizar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonPersonalizar()
	{
		if (botonPersonalizar == null)
		{
			botonPersonalizar = new JButton();
			botonPersonalizar.setBorderPainted(false);
			botonPersonalizar
					.setIcon(new ImageIcon("./Resources/customize.png"));
		}
		return botonPersonalizar;
	}

	/**
	 * This method initializes herrmientasUsuarios
	 * 
	 * @return javax.swing.JToolBar
	 */
	private JToolBar getHerrmientasUsuarios()
	{
		if (herrmientasUsuarios == null)
		{
			herrmientasUsuarios = new JToolBar();
			herrmientasUsuarios.setSize(new Dimension(183, 32));
			herrmientasUsuarios.setLocation(new Point(3, 364));

			Separador s1 = new Separador();
			Separador s2 = new Separador();
			s1.setMinimumSize(new Dimension(20, 15));
			s2.setMinimumSize(new Dimension(20, 15));
			herrmientasUsuarios.setFloatable(false);
			herrmientasUsuarios.add(getNuevoUsuario());
			herrmientasUsuarios.add(getEliminarUsuario());
			herrmientasUsuarios.add(getEditarUsuario());
			herrmientasUsuarios.add(s1);
			herrmientasUsuarios.add(getIniciarChat());
			herrmientasUsuarios.add(getEnviarMensaje());
			herrmientasUsuarios.add(s2);
			herrmientasUsuarios.add(getConfigurar());
		}
		return herrmientasUsuarios;
	}

	/**
	 * This method initializes listaAplicaciones
	 * 
	 * @return javax.swing.JList
	 */
	private JList getListaAplicaciones()
	{
		if (listaAplicaciones == null)
		{

			String[] data = new String[v.size()];

			for (int i=0; i<data.length; ++i)
				data[i] = v.get(i).getName();
			
			
			//String[] data = {"hola","adios"};
			listaAplicaciones = new JList(data);
			listaAplicaciones.setBounds(new Rectangle(1, 26, 186, 140));
			listaAplicaciones.setBorder(new LineBorder(Color.GRAY));

			listaAplicaciones
					.addMouseListener(new java.awt.event.MouseAdapter()
					{
						public void mouseClicked(java.awt.event.MouseEvent e)
						{
							
							if (e.getClickCount() == 2){
							/*		&& listaAplicaciones.getSelectedIndex() == 0)
							{
								iniciarChat();
								
							}
							*/
							
								try
								{
									v.get(listaAplicaciones.getSelectedIndex()).start();
								}
								catch (Exception e1)
								{
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
					});

		}
		return listaAplicaciones;
	}
	
	
	/**
	 * This method initializes arbolUsuario
	 * 
	 * @return
	 */
	private ArbolUsuariosConectadosRol getArbolUsuario()
	{
		arbolUsuario = new ArbolUsuariosConectadosRol(
				"ListaUsuariosConectadosRol", false, this);
		arbolUsuario.setBounds(new Rectangle(1, 196, 186, 167));
		arbolUsuario.setBorder(new LineBorder(Color.gray));
		return arbolUsuario;
	}

	/**
	 * This method initializes botonBuscar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonBuscar()
	{
		if (botonBuscar == null)
		{
			botonBuscar = new JButton();
			botonBuscar.setBorder(null);
			botonBuscar.setIcon(new ImageIcon("./Resources/find.png"));
			botonBuscar.setBorderPainted(false);
		}
		return botonBuscar;
	}

	/**
	 * This method initializes botonInspector
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonInspector()
	{
		if (botonInspector == null)
		{
			botonInspector = new JButton();
			botonInspector.setBorder(null);
			botonInspector.setIcon(new ImageIcon("./Resources/inspector.png"));
			botonInspector.setBorderPainted(false);
		}
		return botonInspector;
	}

	/**
	 * This method initializes botonAtras
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonAtras()
	{
		if (botonAtras == null)
		{
			botonAtras = new JButton();
			botonAtras.setBorder(null);
			botonAtras.setIcon(new ImageIcon("./Resources/back.png"));
			botonAtras.setBorderPainted(false);
		}
		return botonAtras;
	}

	/**
	 * This method initializes botonRecargar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonRecargar()
	{
		if (botonRecargar == null)
		{
			botonRecargar = new JButton();
			botonRecargar.setBorder(null);
			botonRecargar.setIcon(new ImageIcon("./Resources/reload.png"));
			botonRecargar.setBorderPainted(false);
		}
		return botonRecargar;
	}

	/**
	 * This method initializes botonAdelante
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonAdelante()
	{
		if (botonAdelante == null)
		{
			botonAdelante = new JButton();
			botonAdelante.setBorder(null);
			botonAdelante.setIcon(new ImageIcon("./Resources/forward.png"));
			botonAdelante.setBorderPainted(false);
		}
		return botonAdelante;
	}

	/**
	 * This method initializes botonImprimir
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonImprimir()
	{
		if (botonImprimir == null)
		{
			botonImprimir = new JButton();
			botonImprimir.setBorder(null);
			botonImprimir.setIcon(new ImageIcon("./Resources/print.png"));
			botonImprimir.setBorderPainted(false);
		}
		return botonImprimir;
	}

	/**
	 * This method initializes botonAyuda
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonAyuda()
	{
		if (botonAyuda == null)
		{
			botonAyuda = new JButton();
			botonAyuda.setBorder(null);
			botonAyuda.setIcon(new ImageIcon("./Resources/help.png"));
			botonAyuda.setBorderPainted(false);
		}
		return botonAyuda;
	}

	/**
	 * This method initializes nuevoUsuario
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getNuevoUsuario()
	{
		if (nuevoUsuario == null)
		{
			nuevoUsuario = new JButton();
			nuevoUsuario.setBorder(null);
			nuevoUsuario.setIcon(new ImageIcon("./Resources/page_new.gif"));
			nuevoUsuario.setBorderPainted(false);
			nuevoUsuario.setPreferredSize(new Dimension(20, 20));
		}
		return nuevoUsuario;
	}

	/**
	 * This method initializes eliminarUsuario
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getEliminarUsuario()
	{
		if (eliminarUsuario == null)
		{
			eliminarUsuario = new JButton();
			eliminarUsuario.setBorder(null);
			eliminarUsuario
					.setIcon(new ImageIcon("./Resources/page_delete.gif"));
			eliminarUsuario.setBorderPainted(false);
		}
		return eliminarUsuario;
	}

	/**
	 * This method initializes editarUsuario
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getEditarUsuario()
	{
		if (editarUsuario == null)
		{
			editarUsuario = new JButton();
			editarUsuario.setBorder(null);
			editarUsuario.setIcon(new ImageIcon("./Resources/page_edit.gif"));
			editarUsuario.setBorderPainted(false);
		}
		return editarUsuario;
	}

	/**
	 * This method initializes iniciarChat
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getIniciarChat()
	{
		if (iniciarChat == null)
		{
			iniciarChat = new JButton();
			iniciarChat.setBorder(null);
			iniciarChat.setIcon(new ImageIcon("./Resources/comment.gif"));
			iniciarChat.setBorderPainted(false);
		}
		return iniciarChat;
	}

	/**
	 * This method initializes enviarMensaje
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getEnviarMensaje()
	{
		if (enviarMensaje == null)
		{
			enviarMensaje = new JButton();
			enviarMensaje.setBorder(null);
			enviarMensaje.setIcon(new ImageIcon("./Resources/icon_email.gif"));
			enviarMensaje.setBorderPainted(false);
		}
		return enviarMensaje;
	}

	/**
	 * This method initializes configurar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getConfigurar()
	{
		if (configurar == null)
		{
			configurar = new JButton();
			configurar.setBorder(null);
			configurar.setIcon(new ImageIcon("./Resources/icon_settings.gif"));
			configurar.setBorderPainted(false);
		}
		return configurar;
	}

	/**
	 * This method initializes panelEspacioTrabajo
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPanelEspacioTrabajo()
	{
		if (panelEspacioTrabajo == null)
		{
			BorderLayout borderLayout2 = new BorderLayout();
			borderLayout2.setHgap(0);
			borderLayout2.setVgap(0);
			panelEspacioTrabajo = new JPanel();
			panelEspacioTrabajo.setLayout(borderLayout2);
			panelEspacioTrabajo.setBounds(new Rectangle(210, 76, 349, 397));
			panelEspacioTrabajo.setBorder(new LineBorder(Color.GRAY, 1));
			panelEspacioTrabajo.add(getHerraminetasDocumentos(),
					BorderLayout.NORTH);
			panelEspacioTrabajo.add(new JScrollPane(getArbolDocumentos()),
					BorderLayout.CENTER);
		}
		return panelEspacioTrabajo;
	}

	/**
	 * This method initializes herraminetasDocumentos
	 * 
	 * @return javax.swing.JToolBar
	 */
	private JToolBar getHerraminetasDocumentos()
	{
		if (herramientasDocumentos == null)
		{
			herramientasDocumentos = new JToolBar();
			herramientasDocumentos.setBorder(new LineBorder(Color.GRAY));
			herramientasDocumentos.add(getBoton52131());
			herramientasDocumentos.add(new Separador());
			herramientasDocumentos.add(getButonSubir());
			herramientasDocumentos.add(new Separador());
			herramientasDocumentos.add(getBotonEliminarFichero());
			herramientasDocumentos.add(new Separador());
			herramientasDocumentos.add(getBotonInfo());
		}
		return herramientasDocumentos;
	}

	private JButton getBotonEliminarFichero()
	{
		// TODO Auto-generated method stub

		if (botonEliminarFich == null)
		{
			botonEliminarFich = new JButton();
			botonEliminarFich.setText("Elimnar");
			botonEliminarFich.setBorderPainted(false);
			botonEliminarFich.setIcon(new ImageIcon("./Resources/delete2.png"));
			botonEliminarFich
					.addActionListener(new java.awt.event.ActionListener()
					{
						public void actionPerformed(java.awt.event.ActionEvent e)
						{

							FicheroBD f = getDocumentoSeleccionado();

							if (f != null)
							{

								if (!f.esDirectorio())
								{

									DFileEvent evento = new DFileEvent();
									evento.fichero = f;
									evento.tipo = new Integer(
											DFileEvent.NOTIFICAR_ELIMINAR_FICHERO
													.intValue());
									enviarEvento(evento);

									ClienteFicheros.obtenerClienteFicheros()
											.borrarFichero(f,
													DConector.Daplicacion);
								}
								else
								{
									int res = JOptionPane
											.showConfirmDialog(
													null,
													"Si elimina un directorio tambiŽn se eliminar‡n todos\nlos "
															+ "ficheros contenidos en la carpeta y/o en las subcarpetas.\nÀSeguro que desea continuar?",
													"ÀDesea continuar?",
													JOptionPane.OK_CANCEL_OPTION);

									System.out.println("Res = " +res);
									
									if (res == 0)
									{
										if (!comprobarDirectorio(f))
										{
											JOptionPane
													.showMessageDialog(
															null,
															"No se puede eliminar el directorio: contiene\ndocumentos y/o carpetas de las cuales usted " +
															"\nno tiene permiso de borrado",
															"No se puede borrar la carpeta",
															JOptionPane.ERROR_MESSAGE);
										}
										else { 
											System.out.println("Si se puede borrar");
											DFileEvent evento = new DFileEvent();
											evento.fichero = f;
											evento.tipo = new Integer(
													DFileEvent.NOTIFICAR_ELIMINAR_FICHERO
															.intValue());
											enviarEvento(evento);

											ClienteFicheros.obtenerClienteFicheros()
													.borrarFichero(f,
															DConector.Daplicacion);
										}
									}

								}
							}
						}
					});

		}
		return this.botonEliminarFich;
	}

	private boolean comprobarDirectorio(FicheroBD dir)
	{
		String u = DConector.Dusuario;
		String r = DConector.Drol;

		boolean permisosDir = dir.comprobarPermisos(u, r,
				FicheroBD.PERMISO_ESCRITURA);

		if (!dir.esDirectorio())
		{
			return permisosDir;
		}
		else if (permisosDir)
		{
			// si es un directorio
			boolean res = true;

			DefaultTreeModel modelo = (DefaultTreeModel) arbolDocumentos
					.getModel();
			DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) modelo
					.getRoot();

			DefaultMutableTreeNode nodo = buscarFichero(raiz, dir.getId());

			// tenemos que recorrer todos los directorios que cuelgan de dir
			int numHijos = modelo.getChildCount(nodo);

			for (int i = 0; i < numHijos; ++i)
			{
				FicheroBD h = (FicheroBD) ( (DefaultMutableTreeNode) modelo
						.getChild(nodo, i) ).getUserObject();

				// comprobamos si tenemos permiso escritura en el fichero
				if (!h.comprobarPermisos(u, r, FicheroBD.PERMISO_ESCRITURA))
					return false;

				// si es un directorio nos aseguramos de que tengamso acceso a
				// todos los ficheros
				if (h.esDirectorio())
					if (!comprobarDirectorio(h)) return false;
			}

			return res;
		}
		else return false;
	}

	private JButton getBotonInfo()
	{
		if (botonInfo == null)
		{
			botonInfo = new JButton();
			botonInfo.setText("Atributos");
			botonInfo.setBorderPainted(false);
			botonInfo.setIcon(new ImageIcon("./Resources/information.png"));
			botonInfo.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					FicheroBD f = VisorPropiedadesFichero.verInfoFichero(
							getDocumentoSeleccionado(), null);
					if (f != null)
					{
						DFileEvent evento = new DFileEvent();
						evento.fichero = f;
						
						DefaultMutableTreeNode r = (DefaultMutableTreeNode)arbolDocumentos.getModel().getRoot();
						
						evento.padre = buscarDirectorioPadre((DefaultMutableTreeNode)r.getChildAt(0), f.getId());
						
						evento.tipo = new Integer(
								DFileEvent.NOTIFICAR_MODIFICACION_FICHERO
										.intValue());
						enviarEvento(evento);
						ClienteFicheros.obtenerClienteFicheros()
								.modificarFichero(f, DConector.Daplicacion);
					}
				}
			});
		}
		return botonInfo;
	}

	/**
	 * This method initializes boton52131
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBoton52131()
	{
		if (botonAbrirDoc == null)
		{
			botonAbrirDoc = new JButton();
			botonAbrirDoc.setText("Abrir");
			botonAbrirDoc.setIcon(new ImageIcon("./Resources/folder-open_16x16.png"));
			botonAbrirDoc.setBorderPainted(false);
			botonAbrirDoc.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					accionAbrir();

				}
			});
		}
		return botonAbrirDoc;
	}

	/**
	 * This method initializes arbolDocuementos
	 * 
	 * @return javax.swing.JTree
	 */
	private JTree getArbolDocumentos()
	{
		if (arbolDocumentos == null)
		{
			arbolDocumentos = new JTree(DConector.raiz);
			arbolDocumentos.setRootVisible(false);

			arbolDocumentos.setBorder(new LineBorder(Color.GRAY, 1));
			
			ArbolDocumentos.cambiarIconosArbol(arbolDocumentos, "./Resources/folder.gif", "./Resources/page_white.png"); 

			arbolDocumentos.expandRow(0);

			arbolDocumentos.addMouseListener(new java.awt.event.MouseAdapter()
			{
				public void mouseClicked(java.awt.event.MouseEvent e)
				{
					if (e.getClickCount() == 2) accionAbrir();
				}
			});
		}
		return arbolDocumentos;
	}

	private FicheroBD getDocumentoSeleccionado()
	{
		TreePath camino = arbolDocumentos.getSelectionPath();
		
		
		Object[] objetos = null;
		
		if (camino != null)
			objetos = camino.getPath();

		if (objetos != null && objetos.length > 0)
			return (FicheroBD) ( (DefaultMutableTreeNode) objetos[objetos.length - 1] )
				.getUserObject();
		else 
			return null;
	}

	private void accionAbrir()
	{
		TreePath camino = arbolDocumentos.getSelectionPath();

		if (camino == null)
			return;
		
		Object[] objetos = camino.getPath();

		String path = "";

		if (( (DefaultMutableTreeNode) objetos[objetos.length - 1] ).isLeaf())
		{

			for (int i = 2; i < objetos.length; ++i)
			{
				path += '/' + objetos[i].toString();

			}
		}
		else return;

		FicheroBD f = (FicheroBD) ( (DefaultMutableTreeNode) objetos[objetos.length - 1] )
				.getUserObject();

		if (f.esDirectorio()
				|| !f.comprobarPermisos(DConector.Dusuario, DConector.Drol,
						FicheroBD.PERMISO_LECTURA))
		{
			JOptionPane
					.showMessageDialog(null,
							"No tienes permisos para abrir el fichero "
									+ f.getNombre());
			return;
		}

		Documento p = new Documento();
		p.setDatosBD(this.getDocumentoSeleccionado());
		p.setPath(path);
		
		if (frame == null)
		{
			frame = new FramePanelDibujo(false);

			frame.pack();
			frame.setSize(800, 720);

			// Center the window
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = frame.getSize();
			if (frameSize.height > screenSize.height)
			{
				frameSize.height = screenSize.height;
			}
			if (frameSize.width > screenSize.width)
			{
				frameSize.width = screenSize.width;
			}
			frame.setLocation(( screenSize.width - frameSize.width ) / 2,
					( screenSize.height - frameSize.height ) / 2);

			frame.setDocumento(p);
			frame.getLienzo().setPathDocumento(path);
		}
		else
		{
			frame.setDocumento(p);
			frame.getLienzo().setPathDocumento(path);
			frame.getLienzo().getLienzo().sincronizar();
		}

		frame.setVisible(true);

		frame.getLienzo().getLienzo().getDocumento().setPath(path);

	}

	/**
	 * Mediante una llamada a este método se envía un mensaje de peticion de
	 * sincronizacion. No se debe llamar a este método de forma directa. Será
	 * llamado de forma automatica cuando sea necesario realizar la
	 * sincronizacion
	 */
	public void sincronizar(){

	}



	/**
	 * Obtiene el numero de componentes hijos de este componente. SIEMPRE
	 * devuelve 0
	 * 
	 * @return int Número de componentes hijos. En este caso devuelve 8 (la
	 *         lista izquierda, el boton, la lista derecha, la lista de usuarios
	 *         conectados, la lista de usuarios conectados bajo nuestro rol, la
	 *         lista de usuarios conectados con la informacion del rol actual,
	 *         el componente de cambio de rol y la etiqueta del rol actual)
	 */
	public int obtenerNumComponentesHijos()
	{
		return 1;
	}

	/**
	 * Obtiene el componente indicado
	 * 
	 * @param i
	 *            int Indice del componente que queremos obtener. Se comienza a
	 *            numerar en el 0.
	 * @return DComponente Componente indicado. Si el indice no es v‡lido
	 *         devuelve null
	 */
	public DComponente obtenerComponente(int i)
	{
		DComponente dc = null;
		switch (i)
		{
			case 0:
				dc = arbolUsuario;
				break;
			case 1:
				// dc = frame.obtenerComponente(0);
				break;
		}
		return dc;
	}

	/**
	 * Procesamos los eventos que recibimos de los componentes hijos. El
	 * procesamiento se reduce a adjuntar el evento del parametro a un nuevo
	 * evento y enviarlo. Los componentes de metainformacion no emiten eventos
	 * que deban ser procesados
	 * 
	 * @param evento
	 *            DEvent Evento recibido
	 */
	synchronized public void procesarEventoHijo(DEvent evento)
	{

	}

	@Override
	public void procesarEvento(DEvent evento)
	{
		if (evento.tipo.intValue() == DFileEvent.NOTIFICAR_INSERTAR_FICHERO)
		{
			DFileEvent dfe = (DFileEvent) evento;
			
			
			if (dfe.fichero.comprobarPermisos(DConector.Dusuario, DConector.Drol, FicheroBD.PERMISO_LECTURA)){
			
				DefaultTreeModel modelo = (DefaultTreeModel) arbolDocumentos
						.getModel();
				DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) modelo
						.getRoot();
	
				int id_papa = dfe.padre.getId();
				DefaultMutableTreeNode papi = buscarFichero(raiz, id_papa);
	
				modelo.insertNodeInto(new DefaultMutableTreeNode(dfe.fichero),
						papi, 0);
				
			}
			
			comprobarPermisosDocumentoActual(dfe.fichero, true);
		}
		else if (evento.tipo.intValue() == DFileEvent.NOTIFICAR_MODIFICACION_FICHERO)
		{

			DFileEvent dfe = (DFileEvent) evento;
			DefaultTreeModel modelo = (DefaultTreeModel) arbolDocumentos
					.getModel();
			DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) modelo
					.getRoot();

			int id_doc = dfe.fichero.getId();
			DefaultMutableTreeNode nodo = buscarFichero(raiz, id_doc);
			
			if (nodo == null){
				if (dfe.fichero.comprobarPermisos(DConector.Dusuario, DConector.Drol, FicheroBD.PERMISO_LECTURA)){
					DefaultMutableTreeNode padre = buscarFichero(raiz, dfe.padre.getId());
					
					modelo.insertNodeInto(new DefaultMutableTreeNode(dfe.fichero), padre, modelo.getChildCount(padre));
				}
			}
			else {
				if (dfe.fichero.comprobarPermisos(DConector.Dusuario, DConector.Drol, FicheroBD.PERMISO_LECTURA))
					nodo.setUserObject(dfe.fichero);
				else 
					modelo.removeNodeFromParent(nodo);
			}
			comprobarPermisosDocumentoActual(dfe.fichero, false);

		}
		else if (evento.tipo.intValue() == DFileEvent.NOTIFICAR_ELIMINAR_FICHERO)
		{

			DFileEvent dfe = (DFileEvent) evento;
			DefaultTreeModel modelo = (DefaultTreeModel) arbolDocumentos
					.getModel();
			DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) modelo
					.getRoot();

			int id_doc = dfe.fichero.getId();
			DefaultMutableTreeNode nodo = buscarFichero(raiz, id_doc);

			modelo.removeNodeFromParent(nodo);
			comprobarPermisosDocumentoActual(dfe.fichero, true);

		}
		
	}

	private DefaultMutableTreeNode buscarFichero(DefaultMutableTreeNode n,
			int id)
	{
		if (!n.isRoot() && ( (FicheroBD) n.getUserObject() ).getId() == id)
		{
			return n;
		}
		else
		{
			if (n.getChildCount() > 0)
			{
				DefaultMutableTreeNode nodo = null;

				for (int i = 0; i < n.getChildCount(); ++i)
				{
					nodo = buscarFichero((DefaultMutableTreeNode) n
							.getChildAt(i), id);

					if (nodo != null) return nodo;
				}
				return nodo;
			}
			else return null;
		}
	}
	
	private FicheroBD buscarDirectorioPadre(DefaultMutableTreeNode n, int id){

		
			if (n!= null&&!n.isRoot() && ((FicheroBD)n.getUserObject()).getId() == id)
				return (FicheroBD)n.getUserObject();
		
			if (n!=null && n.getChildCount() > 0)
			{
				
				FicheroBD nodo = null;

				for (int i = 0; i < n.getChildCount(); ++i)
				{
					nodo = buscarDirectorioPadre((DefaultMutableTreeNode) n
							.getChildAt(i), id);

					if (nodo != null) return (FicheroBD)n.getUserObject();
				}
				return null;
			}
			else return null;
	}
	
	
	public void comprobarPermisosDocumentoActual(FicheroBD f, boolean eliminado) {
		if (!f.comprobarPermisos(DConector.Dusuario, DConector.Drol, FicheroBD.PERMISO_LECTURA) || eliminado)
			if (frame.isVisible() && frame.getLienzo().getLienzo().getDocumento().getPath().equals(f.getRutaLocal())) {
				
				
			JOptionPane.showMessageDialog(null, "Los permisos del fichero han cambiado y usted" +
						"\n no puede seguir accediendo a este documento");
			
			
			this.frame.setVisible(false);
			Documento d = new Documento();
			d.setPath("");
			frame.setDocumento(d);
		}		
	}
		

	

}
