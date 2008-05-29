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
import java.util.Calendar;
import java.util.Date;
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

import metainformacion.ClienteMetaInformacion;
import metainformacion.MIRol;
import metainformacion.MIUsuario;
import util.Separador;
import Deventos.enlaceJS.DConector;
import aplicacion.fisica.ClienteFicheros;
import aplicacion.fisica.documentos.Documento;
import aplicacion.fisica.documentos.FicheroBD;
import aplicacion.fisica.documentos.filtros.ImageFilter;
import aplicacion.fisica.documentos.filtros.PDFFilter;
import aplicacion.fisica.documentos.filtros.TXTFilter;
import aplicacion.fisica.eventos.DFileEvent;
import aplicacion.fisica.net.Transfer;
import aplicacion.gui.componentes.ArbolDocumentos;
import aplicacion.gui.editor.FramePanelDibujo;
import aplicacion.plugin.DAbstractPlugin;
import aplicacion.plugin.DPluginLoader;

import componentes.base.DComponente;
import componentes.base.DComponenteBase;
import componentes.gui.usuarios.ArbolUsuariosConectadosRol;
import Deventos.DEvent;

public class PanelPrincipal extends DComponenteBase
{

	private static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */

	private JPanel panelLateral = null;
	
	private JLabel jLabel = null;

	private JToolBar herrmientasUsuarios = null;

	private JList listaAplicaciones = null;

	private JButton botonInfo = null;

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

	private JButton botonDescargar = null;
	
	private JButton agregarCarpeta = null;

	private JButton botonImprimirDocumento = null;

	private ArbolDocumentos arbolDocumentos = null;

	private JLabel jLabel1 = null;

	private FramePanelDibujo frame = null;

	ArbolUsuariosConectadosRol arbolUsuario = null;

	DefaultMutableTreeNode raiz = null;

	private JButton botonSubir = null;

	public static Vector<DAbstractPlugin> plugins = null;

	private JButton getButonSubir()
	{
		if (botonSubir == null)
		{
			botonSubir = new JButton();;
			botonSubir.setBorderPainted(false);
			botonSubir.setText("");

			botonSubir
					.setIcon(new ImageIcon("./Resources/subir_documento.png"));
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
	 * Sube un fichero al servidor en la carpeta que esta seleccionada
	 * actualmente. Si no hay seleccionada ninguana carpeta no hace nada.
	 * Si se intenta subir un fichero duplicado, muestra un mensaje de error
	 * indicando si el usuario desea: cancelar, cambiar el nombre o sobreescribir
	 */
	private void subirFicheroServidor()
	{
		// obtenemos los datos del fichero asociados a ese nodo
		FicheroBD carpeta = arbolDocumentos.getDocumentoSeleccionado();

		// si el fichero escogido no es directorio, salimos
		if (carpeta == null || !carpeta.esDirectorio()) return;

		String path = carpeta.getRutaLocal() + "/";
		
		if (path.equals("//"))
			path = "/";

		// recuperamos el usuario y el rol
		MIUsuario user = ClienteMetaInformacion.cmi.getUsuario(DConector.Dusuario);
		MIRol rol = ClienteMetaInformacion.cmi.getRol(DConector.Drol);

		// si se ha producido algun error, salimos
		if (( user == null ) || ( rol == null ) ) return;

		// mostramos el selector de ficheros
		JFileChooser jfc = new JFileChooser("Subir Documento Servidor");

		int op = jfc.showDialog(null, "Aceptar");

		// si no se ha escogido la opcion aceptar en el dialogo de apertura de
		// fichero salimos
		if (op != JFileChooser.APPROVE_OPTION) return;

		java.io.File f = jfc.getSelectedFile();
		
		String nombre = f.getName();
		
		FicheroBD anterior = arbolDocumentos.buscarFichero( (DefaultMutableTreeNode)arbolDocumentos.getModel().getRoot() , path + nombre);

		while (anterior != null){
			int sel = JOptionPane.showConfirmDialog(this, "El documento ya existe ÀDesea sobrescribirlo?", "ÀSobrescribir?", JOptionPane.YES_NO_CANCEL_OPTION);
			
			// el usuario ha cancelado la accion
			if (sel == 2) return;
			
			// el usuario desea sobreescribir el documento
			else if (sel == 0) {
				//aki es donde hay que hacer es:
				// 1) Renombrar el fichero anterior en el servidor
				// 2) Guardar el nuevo fichero
								
				String nombreVersion = anterior.getNombre() + Calendar.getInstance().getTimeInMillis();
				
				anterior.setNombre(nombreVersion);
				
				anterior.setRutaLocal(path+nombreVersion);
				
				anterior.setTipo("VER");
				
				ClienteFicheros.cf.modificarFichero(anterior, DConector.Daplicacion);
				
				JOptionPane.showMessageDialog(null, "Generada version: "+ anterior.getNombre());
				
				anterior = null;
			}
			
			// el usuario no desea sobreescribir el fichero
			else if(sel == 1){
				nombre = JOptionPane.showInputDialog("Nuevo nombre");
				anterior = arbolDocumentos.buscarFichero( (DefaultMutableTreeNode)arbolDocumentos.getModel().getRoot() , path + nombre);
			}
		}
		
		byte[] bytes = null;
		try
		{
			// abrimos el fichero en modo lectura
			RandomAccessFile raf = new RandomAccessFile(f.getAbsolutePath(),"r");

			// consultamos el tamanio del fichero, reservamos
			// memoria suficiente,
			// leemos el fichero y lo cerramos
			bytes = new byte[(int) raf.length()];
			raf.read(bytes);
			raf.close();
		}
		catch (FileNotFoundException ex)
		{
			JOptionPane.showMessageDialog(null, "El fichero no existe",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		catch (IOException e1)
		{
			JOptionPane.showMessageDialog(null,
					"Error en la lectura del fichero", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String nombreFichero = nombre;
		nombreFichero = nombreFichero.replace('.', ':');

		String[] desc = nombreFichero.split(":");

		String extension = desc[desc.length - 1];

		extension = FicheroBD.getTipoFichero(extension);

		
		// creamos el nuevo fichero a almacenar
		FicheroBD fbd = new FicheroBD(-1, nombre, false, "rwrw--", user,
				rol, carpeta.getId(), path + nombre, extension);

		// enviamos el nuevo fichero al servidor
		Transfer t = new Transfer(ClienteFicheros.ipConexion, path
				+ nombre);

		if (!t.sendFile(bytes))
		{
			JOptionPane.showMessageDialog(
							null,
							"No se ha podido subir el fichero.\nSe ha producido un error en la transmisi—n del documento",
							"Error", JOptionPane.ERROR_MESSAGE);
		}

		// si no se ha producido ningun error al subir el fichero
		else
		{

			//insertamos el nuevo fichero en el servidor
			FicheroBD f2 = ClienteFicheros.cf.insertarNuevoFichero(fbd, DConector.Daplicacion);
			
			// si ha habido algun error salimos
			if (f2 == null) {
				JOptionPane.showMessageDialog(this, "Ha ocurrido un error: no se ha podido subir el documento al servidor");
				return;
			}

			JOptionPane.showMessageDialog(this, "Nombre efectivo del fichero " + f2.getNombre());
			
			// notificamos al resto de usuarios la "novedad"
			DFileEvent evento = new DFileEvent();
			evento.fichero = f2;
			evento.padre = carpeta;
			evento.tipo = new Integer(DFileEvent.NOTIFICAR_INSERTAR_FICHERO
					.intValue());
			enviarEvento(evento);
		}				
		
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
			//dar soporte para documentos
			Documento.addFilter(new ImageFilter());
			Documento.addFilter(new PDFFilter());
			Documento.addFilter(new TXTFilter());
			
			plugins = DPluginLoader.getAllPlugins("plugin");

			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(0);
			this.setLayout(null);
			this.add(getPanelLateral(), BorderLayout.WEST);


			inicializarEditor();

			// inicializarChat();

			this.add(getPanelEspacioTrabajo(), null);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

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
			panelLateral.setBounds(new Rectangle(6, 16, 188, 398));
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

			Vector<DAbstractPlugin> data = new Vector<DAbstractPlugin>();
			
			for (int i = 0; i < plugins.size(); ++i)
			{
				if (plugins.get(i).shouldShowIt())
					data.add(plugins.get(i));
			}
			
			listaAplicaciones = new JList(data.toArray());
			listaAplicaciones.setBounds(new Rectangle(1, 26, 186, 140));
			listaAplicaciones.setBorder(new LineBorder(Color.GRAY));

			listaAplicaciones
					.addMouseListener(new java.awt.event.MouseAdapter()
					{
						@Override
						public void mouseClicked(java.awt.event.MouseEvent e)
						{

							if (e.getClickCount() == 2)
								try
								{

									if (plugins != null
											&& plugins.size() > 0
											&& listaAplicaciones
													.getSelectedIndex() > -1)
									{
										((DAbstractPlugin)listaAplicaciones.getSelectedValue()).start();
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
	 * This method initializes nuevoUsuario
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getNuevoUsuario()
	{
		if (nuevoUsuario == null)
		{
			nuevoUsuario = new JButton();
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
			panelEspacioTrabajo.setBounds(new Rectangle(210, 16, 349, 398));
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
			herramientasDocumentos.add(this.getBotonImprimirDocumento());
			herramientasDocumentos.add(new Separador());
			herramientasDocumentos.add(getButonSubir());
			herramientasDocumentos.add(getBotonDescargar());
			herramientasDocumentos.add(new Separador());
			herramientasDocumentos.add(this.getAgregarCarpeta());
			herramientasDocumentos.add(new Separador());
			herramientasDocumentos.add(getBotonEliminarFichero());
			herramientasDocumentos.add(new Separador());
			herramientasDocumentos.add(getBotonInfo());
		}
		return herramientasDocumentos;
	}

	/**
	 * 
	 * @return
	 */
	public JButton getBotonDescargar()
	{
		if (botonDescargar == null)
		{
			botonDescargar = new JButton();;
			botonDescargar.setBorderPainted(false);
			botonDescargar.setText("");

			botonDescargar.setIcon(new ImageIcon(
					"./Resources/page_white_put.png"));
			
			
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
	 * 
	 * @return
	 */
	public JButton getBotonImprimirDocumento()
	{
		if (botonImprimirDocumento == null)
		{
			botonImprimirDocumento = new JButton();;
			botonImprimirDocumento.setBorderPainted(false);
			botonImprimirDocumento.setText("");

			botonImprimirDocumento.setIcon(new ImageIcon(
					"./Resources/printer.png"));
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
	 * 
	 * @return
	 */
	private JButton getBotonEliminarFichero()
	{

		if (botonEliminarFich == null)
		{
			botonEliminarFich = new JButton();
			botonEliminarFich.setText("");
			botonEliminarFich.setBorderPainted(false);
			botonEliminarFich.setIcon(new ImageIcon("./Resources/delete2.png"));
			botonEliminarFich
					.addActionListener(new java.awt.event.ActionListener()
					{
						public void actionPerformed(java.awt.event.ActionEvent e)
						{

							FicheroBD f = arbolDocumentos.getDocumentoSeleccionado();
							
							if (arbolDocumentos.eliminarFichero()){

								DFileEvent evento = new DFileEvent();
								evento.fichero = f;
								evento.tipo = new Integer(
										DFileEvent.NOTIFICAR_ELIMINAR_FICHERO
												.intValue());
								enviarEvento(evento);
							}
								
						}
					});

		}
		return this.botonEliminarFich;
	}



	private JButton getBotonInfo()
	{
		if (botonInfo == null)
		{
			botonInfo = new JButton();
			botonInfo.setText("");
			botonInfo.setBorderPainted(false);
			botonInfo.setIcon(new ImageIcon("./Resources/information.png"));
			botonInfo.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					FicheroBD f = arbolDocumentos.getDocumentoSeleccionado();

					if (f == null) return;

					f = VisorPropiedadesFichero.verInfoFichero(f, null);

					if (f != null)
					{
						DFileEvent evento = new DFileEvent();
						evento.fichero = f;

						DefaultMutableTreeNode r = (DefaultMutableTreeNode) arbolDocumentos.getNodoSeleccionado()
								.getParent();

						evento.padre = (FicheroBD) r.getUserObject();

						System.err.println("directorio padre: "
								+ evento.padre.getNombre());

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
			botonAbrirDoc.setText("");
			botonAbrirDoc.setIcon(new ImageIcon(
					"./Resources/folder-open_16x16.png"));
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
			arbolDocumentos = new ArbolDocumentos(DConector.raiz);

			arbolDocumentos.addMouseListener(new java.awt.event.MouseAdapter()
			{
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e)
				{
					if (e.getClickCount() == 2) accionAbrir();
				}
			});
		}
		return arbolDocumentos;
	}

	private void accionAbrir()
	{
		
		FicheroBD f = arbolDocumentos.getDocumentoSeleccionado();
		
		if (f == null) return;

		if (f.esDirectorio()) return;
		if (!f.comprobarPermisos(DConector.Dusuario, DConector.Drol,
						FicheroBD.PERMISO_LECTURA))
		{
			JOptionPane
					.showMessageDialog(null,
							"No tienes permisos para abrir el fichero "
									+ f.getNombre());
			return;
		}

		Documento p = new Documento();
		p.setDatosBD(arbolDocumentos.getDocumentoSeleccionado());
		p.setPath(f.getRutaLocal());

		if (frame == null)
		{
			frame = new FramePanelDibujo(false);

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

			frame.setDocumento(p);
			frame.getLienzo().setPathDocumento(f.getRutaLocal());
		}
		else
		{
			frame.setDocumento(p);
			frame.getLienzo().setPathDocumento(f.getRutaLocal());
			frame.getLienzo().getLienzo().sincronizar();
		}

		frame.setVisible(true);

		frame.getLienzo().getLienzo().getDocumento().setPath(f.getRutaLocal());

		if (this.arbolDocumentos != null) arbolDocumentos.repaint();

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
	@Override
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
				// dc = frame.obtenerComponente(0);
				break;
		}
		return dc;
	}

	@Override
	public void procesarEvento(DEvent evento)
	{
		if (evento.tipo.intValue() == DFileEvent.NOTIFICAR_INSERTAR_FICHERO)
		{
			DFileEvent dfe = (DFileEvent) evento;

			if (dfe.fichero.comprobarPermisos(DConector.Dusuario,
					DConector.Drol, FicheroBD.PERMISO_LECTURA))
			{

				DefaultTreeModel modelo = (DefaultTreeModel) arbolDocumentos
						.getModel();
				DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) modelo
						.getRoot();

				int id_papa = dfe.padre.getId();
				DefaultMutableTreeNode papi = ArbolDocumentos.buscarFichero(raiz, id_papa);

				modelo.insertNodeInto(new DefaultMutableTreeNode(dfe.fichero),
						papi, 0);

			}

			comprobarPermisosDocumentoActual(dfe.fichero, true);
			if (this.arbolDocumentos != null) arbolDocumentos.repaint();
		}
		else if (evento.tipo.intValue() == DFileEvent.NOTIFICAR_MODIFICACION_FICHERO)
		{

			DFileEvent dfe = (DFileEvent) evento;
			DefaultTreeModel modelo = (DefaultTreeModel) arbolDocumentos
					.getModel();
			DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) modelo
					.getRoot();

			int id_doc = dfe.fichero.getId();
			DefaultMutableTreeNode nodo = ArbolDocumentos.buscarFichero(raiz, id_doc);

			if (nodo == null)
			{

				if (dfe.fichero.comprobarPermisos(DConector.Dusuario,
						DConector.Drol, FicheroBD.PERMISO_LECTURA))
				{

					DefaultMutableTreeNode padre = ArbolDocumentos.buscarFichero(raiz,
							dfe.padre.getId());
					modelo.insertNodeInto(new DefaultMutableTreeNode(
							dfe.fichero), padre, modelo.getChildCount(padre));
				}
			}
			else if (dfe.fichero.comprobarPermisos(DConector.Dusuario,
					DConector.Drol, FicheroBD.PERMISO_LECTURA))
				nodo.setUserObject(dfe.fichero);
			else modelo.removeNodeFromParent(nodo);

			this.arbolDocumentos.repaint();

			comprobarPermisosDocumentoActual(dfe.fichero, false);
			if (this.arbolDocumentos != null) arbolDocumentos.repaint();

			String pathEditor = frame.getLienzo().getPathDocumento();

			if (pathEditor != null
					&& pathEditor.equals(dfe.fichero.getRutaLocal())
					&& !dfe.fichero.comprobarPermisos(DConector.Dusuario,
							DConector.Drol, FicheroBD.PERMISO_LECTURA))
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
			DefaultTreeModel modelo = (DefaultTreeModel) arbolDocumentos
					.getModel();
			DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) modelo
					.getRoot();

			int id_doc = dfe.fichero.getId();
			DefaultMutableTreeNode nodo = ArbolDocumentos.buscarFichero(raiz, id_doc);

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

			if (this.arbolDocumentos != null) arbolDocumentos.repaint();

		}

	}

	

	/**
	 * Comprueba que los permisos actuales del documentos permiten que Žste siga editandose
	 * @param f fichero a comprobar
	 * @param eliminado indica si el documentos ha sido editado
	 */
	public void comprobarPermisosDocumentoActual(FicheroBD f, boolean eliminado)
	{
		if (!f.comprobarPermisos(DConector.Dusuario, DConector.Drol,
				FicheroBD.PERMISO_LECTURA)
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

	/**
	 * 
	 * @return
	 */
	private JButton getAgregarCarpeta()
	{
		
		if (agregarCarpeta == null){
			agregarCarpeta = new JButton();
			agregarCarpeta.setBorderPainted(false);
			agregarCarpeta.setText("");

			
			agregarCarpeta
					.setIcon(new ImageIcon("./Resources/nueva_carpeta.png"));
			agregarCarpeta.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					FicheroBD f = arbolDocumentos.getDocumentoSeleccionado();
					
					if (f == null || !f.esDirectorio()) return;
					
					String nombre = JOptionPane.showInputDialog("Introduce el nuevo nombre para la carpeta");
					
					if (nombre == null) return;
					
					f = arbolDocumentos.agregarCarpeta(nombre);
					
					if (f== null) return;
					
					f = ClienteFicheros.cf.insertarNuevoFichero(f, DConector.Daplicacion);
					
					if (f!= null){
						
						System.out.println("ID de la nueva carpeta " + f.getId());
						
						DFileEvent evento = new DFileEvent();
						evento.padre = arbolDocumentos.getDocumentoSeleccionado();
						evento.fichero = f;
						evento.tipo = new Integer(DFileEvent.NOTIFICAR_INSERTAR_FICHERO
								.intValue());
						enviarEvento(evento);
					}
					
				}
			});
		}
		
		return agregarCarpeta;
	}
	
	public void salir(){
		
		if (frame != null && frame.getLienzo() != null && frame.getLienzo().getPathDocumento() != null)
			DConector.obtenerDC().cerrarFichero(frame.getLienzo().getPathDocumento());
	}
}
