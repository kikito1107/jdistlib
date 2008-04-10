package aplicacion.gui;

import aplicacion.fisica.*;
import aplicacion.fisica.documentos.Documento;
import aplicacion.fisica.documentos.FicheroBD;
import aplicacion.fisica.eventos.DFileEvent;
import interfaces.DComponente;
import interfaces.listeners.LJButtonListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JToolBar.Separator;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import metainformacion.ClienteMetaInformacion;
import metainformacion.MIRol;
import metainformacion.MIUsuario;

import util.Separador;

import Deventos.DEvent;
import Deventos.DJLienzoEvent;
import Deventos.enlaceJS.DConector;
import aplicacion.gui.componentes.ArbolDocumentos;

import componentes.DComponenteBase;
import componentes.HebraProcesadoraBase;
import componentes.gui.imagen.FramePanelDibujo;
import componentes.gui.usuarios.ArbolUsuariosConectadosRol;
import ejemplos.EventoComponenteEjemplo;
import ejemplos.arbol.ComponenteEjemplo;

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

	private JPanel panelEspacioTrabajo = null;

	private JToolBar herraminetasDocumentos = null;

	private JButton botonAbrirDoc = null;

	private JTree arbolDocumentos = null;

	private ArbolDocumentos raizDocumentos = null;

	private JLabel jLabel1 = null;

	private FramePanelDibujo frame = null;

	ArbolUsuariosConectadosRol arbolUsuario = null;

	DefaultMutableTreeNode raiz = null;

	private JButton botonSubir  = null;


	private JButton getButonSubir(){
		if (botonSubir == null) {
			botonSubir = new JButton("");
			botonSubir.setBorder(null);
			botonSubir.setBorderPainted(false);
			botonSubir.setText("subir fichero");
			botonSubir.setIcon(new ImageIcon("./Resources/document_new.png"));
			botonSubir.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					JFileChooser jfc = new JFileChooser("Guardar Documento Localmente");

					int op = jfc.showDialog(null, "Aceptar");

					if (op == JFileChooser.APPROVE_OPTION)
					{
						java.io.File f = jfc.getSelectedFile();
						byte[] bytes = null; 
						try
						{
							RandomAccessFile raf = new RandomAccessFile(f.getAbsolutePath(), "r");

							int tamanio = (int)raf.length();

							bytes = new byte[tamanio];

							raf.read(bytes);

							raf.close();

							TreePath camino = arbolDocumentos.getSelectionPath();

							Object[] objetos = camino.getPath();

							String path = "";

							if ( !((DefaultMutableTreeNode)objetos[objetos.length - 1]).isLeaf() ){

								for (int i=2; i<objetos.length; ++i){
									path += '/' + objetos[i].toString();
								}
							}

							Transfer t = new Transfer(ClienteFicheros.ipConexion,  path+"/"+f.getName() );

							FicheroBD fbd = new FicheroBD(-1, f.getName(), false, "rwrw--", new MIUsuario(DConector.Dusuario, DConector.Drol), new MIRol(DConector.Drol), 0, path+"/"+f.getName(), "");
							
							DFileEvent evento = new DFileEvent();
							
							evento.fichero = fbd;
							evento.tipo = new Integer(DFileEvent.NOTIFICAR_INSERTAR_FICHERO.intValue());
							
							enviarEvento(evento);

							ClienteFicheros.cf.insertarNuevoFichero(fbd, DConector.Daplicacion);

							if (!t.sendFile(bytes)) {
								JOptionPane.showMessageDialog(null, "No se ha podido subir el fichero", "Error", JOptionPane.ERROR_MESSAGE);
							}
						}
						catch (FileNotFoundException ex)
						{
							ex.printStackTrace();
						}
						catch (IOException e1)
						{
							e1.printStackTrace();
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
	public PanelPrincipal(String nombre, boolean conexionDC,
			DComponenteBase padre) {
		super(nombre, conexionDC, padre);
		try {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(0);
			this.setLayout(null);
			this.add(getPanelLateral(), BorderLayout.WEST);
			this.add(getBarraHerramientas(), null);

			this.add(getPanelEspacioTrabajo(), null);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
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
			BotonAbrir.setIcon(new ImageIcon("./Resources/folder.png"));
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
			botonPersonalizar.setIcon(new ImageIcon("./Resources/customize.png"));
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
			s1.setMinimumSize(new Dimension(20,15));
			s2.setMinimumSize(new Dimension(20,15));
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
			listaAplicaciones = new JList();
			listaAplicaciones.setBounds(new Rectangle(1, 26, 186, 140));
			listaAplicaciones.setBorder(new LineBorder(Color.GRAY));
		}
		return listaAplicaciones;
	}

	/**
	 * This method initializes arbolUsuario	
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
			nuevoUsuario.setPreferredSize(new Dimension(20,20));
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
			eliminarUsuario.setIcon(new ImageIcon("./Resources/page_delete.gif"));
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
			panelEspacioTrabajo.setBorder(new LineBorder( Color.GRAY, 1 ));
			panelEspacioTrabajo.add(getHerraminetasDocumentos(), BorderLayout.NORTH);
			panelEspacioTrabajo.add(new JScrollPane(getArbolDocumentos()), BorderLayout.CENTER);
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
		if (herraminetasDocumentos == null)
		{
			herraminetasDocumentos = new JToolBar();
			herraminetasDocumentos.setBorder(new LineBorder(Color.GRAY));
			herraminetasDocumentos.add(getBoton52131());
			herraminetasDocumentos.add(getButonSubir());
		}
		return herraminetasDocumentos;
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
			arbolDocumentos = new JTree( DConector.raiz);
			arbolDocumentos.setRootVisible(false);

			arbolDocumentos.setBorder(new LineBorder(Color.GRAY, 1));

			arbolDocumentos.expandRow(0);

			arbolDocumentos.addMouseListener(new java.awt.event.MouseAdapter()
			{
				public void mouseClicked(java.awt.event.MouseEvent e)
				{
					if (e.getClickCount() == 2)
						accionAbrir();
				}
			});
		}
		return arbolDocumentos;
	}

	private void accionAbrir(){
		TreePath camino = arbolDocumentos.getSelectionPath();

		Object[] objetos = camino.getPath();

		String path = "";

		if ( ((DefaultMutableTreeNode)objetos[objetos.length - 1]).isLeaf() ){

			for (int i=2; i<objetos.length; ++i){
				path += '/' + objetos[i].toString();
			}
		} 

		if (frame == null)
		{
			frame = new FramePanelDibujo(false);

			frame.pack();
			frame.setSize(800, 720);

			//Center the window
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = frame.getSize();
			if (frameSize.height > screenSize.height) {
				frameSize.height = screenSize.height;
			}
			if (frameSize.width > screenSize.width) {
				frameSize.width = screenSize.width;
			}
			frame.setLocation( (screenSize.width - frameSize.width) / 2,
					(screenSize.height - frameSize.height) / 2);
		}


		frame.setVisible(true);

		frame.getLienzo().getLienzo().getDocumento().setPath(path);

		DConector.obtenerDC().sincronizarComponentes();

		Transfer t = new Transfer(ClienteFicheros.ipConexion, path );

		Documento p = t.receive();

		frame.setDocumento(p);
	}

	/**
	 * Mediante una llamada a este método se envía un mensaje de peticion de
	 * sincronizacion. No se debe llamar a este método de forma directa. Será
	 * llamado de forma automatica cuando sea necesario realizar la sincronizacion
	 */
	public void sincronizar() {
		if (conectadoDC()) {
			EventoComponenteEjemplo peticion = new EventoComponenteEjemplo();
			peticion.tipo = new Integer(EventoComponenteEjemplo.SINCRONIZACION.
					intValue());
			enviarEvento(peticion);
		}
	}


	void arbol_actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode seleccionado = raiz;
		if (seleccionado != null) {
			// Creamos un nuevo evento
			EventoComponenteEjemplo evento = new EventoComponenteEjemplo();
			// Establecemos el tipo del evento
			evento.tipo = new Integer(EventoComponenteEjemplo.EVENTO_ARBOL.intValue());
			// Indicamos el elemento que va a ser eliminado
			evento.elemento = new String(raiz.toString());
			// Enviamos el evento
			enviarEvento(evento);
		}
	}

	/**
	 * Devuelve una nueva instancia de la hebra que se encargara de procesar
	 * los eventos que se reciban. Este metodo no debe llamarse de forma directa.
	 * Sera llamado de forma automatica cuando sea necesario.
	 * @return HebraProcesadoraBase Nueva hebra procesadora
	 */
	public HebraProcesadoraBase crearHebraProcesadora() {
		return new HebraProcesadora(this);
	}

	/**
	 * Obtiene el numero de componentes hijos de este componente. SIEMPRE devuelve 0
	 * @return int Número de componentes hijos. En este caso devuelve 8 (la lista
	 * izquierda, el boton, la lista derecha, la lista de usuarios conectados,
	 * la lista de usuarios conectados bajo nuestro rol, la lista de usuarios
	 * conectados con la informacion del rol actual, el componente de cambio de
	 * rol y la etiqueta del rol actual)
	 */
	public int obtenerNumComponentesHijos() {
		return 1;
	}

	/**
	 * Obtiene el componente indicado
	 * @param i int Indice del componente que queremos obtener. Se comienza a numerar
	 * en el 0.
	 * @return DComponente Componente indicado. Si el indice no es v‡lido devuelve
	 * null
	 */
	public DComponente obtenerComponente(int i) {
		DComponente dc = null;
		switch (i) {
			case 0:
				dc = arbolUsuario;
				break;
		}
		return dc;
	}

	/**
	 * Procesamos los eventos que recibimos de los componentes hijos. El procesamiento
	 * se reduce a adjuntar el evento del parametro a un nuevo evento y enviarlo.
	 * Los componentes de metainformacion no emiten eventos que deban ser procesados
	 * @param evento DEvent Evento recibido
	 */
	synchronized public void procesarEventoHijo(DEvent evento) {
		try {
			EventoComponenteEjemplo ev = new EventoComponenteEjemplo();

			if(evento.nombreComponente.equals("Arbol")) {
				ev.tipo = new Integer(EventoComponenteEjemplo.EVENTO_ARBOL.
						intValue());
				ev.aniadirEventoAdjunto(evento);
				enviarEvento(ev);
			}

		}
		catch (Exception e) {

		}
	}

	@Override
	public void procesarEvento(DEvent evento){
		if (evento.tipo.intValue() == DFileEvent.NOTIFICAR_INSERTAR_FICHERO/* &&
				!evento.usuario.equals(DConector.Dusuario)*/) 
		{
			DFileEvent dfe = (DFileEvent) evento;
			DefaultTreeModel modelo = (DefaultTreeModel) arbolDocumentos.getModel();
			DefaultMutableTreeNode raiz = (DefaultMutableTreeNode)modelo.getRoot();
			modelo.insertNodeInto(new DefaultMutableTreeNode(dfe.fichero.getNombre()), (MutableTreeNode) raiz.getChildAt(0), 0);
		}
	}

	/**
	 * Hebra procesadora de eventos. Se encarga de realizar las acciones que
	 * correspondan cuando recibe un evento. Tambén se encarga en su inicio
	 * de sincronizar el componente.
	 */
	private class HebraProcesadora
	extends HebraProcesadoraBase {

		HebraProcesadora(DComponente dc) {
			super(dc);
		}

		public void run() {
			EventoComponenteEjemplo evento = null;
			EventoComponenteEjemplo saux = null;
			EventoComponenteEjemplo respSincr = null;
			Vector<Object> vaux = new Vector<Object>();

			// Obtenemos los eventos existentes en la cola de recepcion. Estos eventos
			// se han recibido en el intervalo de tiempo desde que se envio la peticion
			// de sincronizacion y el inicio de esta hebra procesadora
			DEvent[] eventos = obtenerEventosColaRecepcion();
			int numEventos = eventos.length;
			int i = 0;

			// Buscamos entre los eventos si hay alguno correspondiente a una
			// respuesta de sincronizacion
			for (int j = 0; j < numEventos; j++) {
				saux = (EventoComponenteEjemplo) eventos[j];
				if ( (respSincr == null) &&
						(saux.tipo.intValue() ==
							EventoComponenteEjemplo.RESPUESTA_SINCRONIZACION.intValue())) {
					respSincr = saux;
				}
				else {
					vaux.add(saux);
				}
			}

			if (respSincr != null) { // Se ha recibido respuesta de sincronizacion
				// Al actualizar nuestro estado con el del componente que nos envia
				// la respuesta de sincronizacion establecemos nuestro índice de cual
				// ha sido el ultimo evento procesado al mismo que el componente
				ultimoProcesado = new Integer(respSincr.ultimoProcesado.intValue());
			}

			// Todos esos eventos que se han recibido desde que se mando la peticion
			// de sincronizacion deben ser colocados en la cola de recepcion para
			// ser procesados. Solo nos interesan aquellos con un número de secuencia
			// posterior a ultimoProcesado. Los anteriores no nos interesan puesto
			// que ya han sido procesados por el componente que nos mando la respuesta
			// de sincronizacion.
			numEventos = vaux.size();
			for (int j = 0; j < numEventos; j++) {
				saux = (EventoComponenteEjemplo) vaux.elementAt(j);
				if (saux.ultimoProcesado.intValue() > ultimoProcesado.intValue()) {
					procesarEvento(saux);
				}
			}

			while (true) {
				// Extraemos un evento de la la cola de recepcion
				// Si no hay ninguno se quedara bloqueado hasta que haya
				evento = (EventoComponenteEjemplo) leerSiguienteEvento();
				// Actualizamos nuestro indicado de cual ha sido el último evento
				// que hemos procesado
				ultimoProcesado = new Integer(evento.contador.intValue());
				if (evento.tipo.intValue() ==
					EventoComponenteEjemplo.SINCRONIZACION.intValue()) {
					// Creamos un nuevo evento
					EventoComponenteEjemplo infoEstado = new EventoComponenteEjemplo();
					// Establecemos el tipo del evento
					infoEstado.tipo = new Integer(EventoComponenteEjemplo.
							RESPUESTA_SINCRONIZACION.intValue());
					// Enviamos el evento
					enviarEvento(infoEstado);
				}
				if (evento.tipo.intValue() == EventoComponenteEjemplo.EVENTO_ARBOL.intValue()){
					//no hacemos nada
				}
			}

		}
	}

}
