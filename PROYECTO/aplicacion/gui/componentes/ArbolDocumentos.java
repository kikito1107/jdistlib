package aplicacion.gui.componentes;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import metainformacion.ClienteMetaInformacion;
import metainformacion.MIRol;
import metainformacion.MIUsuario;
import Deventos.enlaceJS.DConector;
import aplicacion.fisica.ClienteFicheros;
import aplicacion.fisica.documentos.Documento;
import aplicacion.fisica.documentos.MIDocumento;
import aplicacion.fisica.eventos.DFileEvent;
import aplicacion.fisica.net.Transfer;
import aplicacion.gui.PanelPrincipal;

/**
 * Componente que muestra el arbol de documentos
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class ArbolDocumentos extends JTree implements Autoscroll
{
	private int margin = 12;

	private static final long serialVersionUID = -3359982437919729727L;

	private DefaultMutableTreeNode raiz = null;

	private DefaultTreeModel model = null;

	@SuppressWarnings( "unused" )
	private TreeDragSource ds;

	@SuppressWarnings( "unused" )
	private TreeDropTarget dt;

	/**
	 * Metodo que debemos implementar para la interfaz Autoscroll
	 * 
	 * @param p
	 *            Punto en el que estamos situados dentro del arbol
	 */
	public void autoscroll(Point p)
	{
		int realrow = getRowForLocation(p.x, p.y);
		Rectangle outer = getBounds();
		realrow = ( p.y + outer.y <= margin ? realrow < 1 ? 0 : realrow - 1
				: realrow < getRowCount() - 1 ? realrow + 1 : realrow );
		scrollRowToVisible(realrow);
	}

	/**
	 * Metodo que debemos implementar para la interfaz Autoscroll
	 * 
	 * @return Margenes del autoscroll
	 */
	public Insets getAutoscrollInsets()
	{
		Rectangle outer = getBounds();
		Rectangle inner = getParent().getBounds();
		return new Insets(inner.y - outer.y + margin, inner.x - outer.x
				+ margin, outer.height - inner.height - inner.y + outer.y
				+ margin, outer.width - inner.width - inner.x + outer.x
				+ margin);
	}

	/**
	 * Constructor
	 * 
	 * @param raiz
	 *            Raiz del arbol de documentos
	 */
	public ArbolDocumentos( DefaultMutableTreeNode raiz )
	{
		super(raiz);
		this.setRootVisible(false);
		this.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.setCellRenderer(new DocumentosCellRenderer());
		// this.putClientProperty("JTree.lineStyle", "Horizontal");

		ds = new TreeDragSource(this, DnDConstants.ACTION_COPY_OR_MOVE);
		dt = new TreeDropTarget(this);
		this.raiz = raiz;

		model = (DefaultTreeModel) this.getModel();

		this.expandRow(0);
	}

	/**
	 * Establece la raiz del arbol de documentos
	 * 
	 * @param raizNueva
	 *            Nueva raiz del arbol
	 */
	public void setRaiz(DefaultMutableTreeNode raizNueva)
	{
		if (raiz != null && model != null) model.setRoot(raizNueva);
	}

	/**
	 * Permite obtener la metainformacion del documento seleccionado en el arbol
	 * 
	 * @return Metainformacion del documento seleccionado
	 */
	public MIDocumento getDocumentoSeleccionado()
	{
		TreePath camino = getSelectionPath();

		Object[] objetos = null;

		if (camino != null) objetos = camino.getPath();

		if (( objetos != null ) && ( objetos.length > 0 ))
			return (MIDocumento) ( (DefaultMutableTreeNode) objetos[objetos.length - 1] )
					.getUserObject();
		else return null;
	}

	/**
	 * Permite obtener el nodo seleccionado en el arbol
	 * 
	 * @return Nodo seleccionado
	 */
	public DefaultMutableTreeNode getNodoSeleccionado()
	{
		TreePath camino = this.getSelectionPath();

		Object[] objetos = null;

		if (camino != null) objetos = camino.getPath();

		if (( objetos != null ) && ( objetos.length > 0 ))
			return (DefaultMutableTreeNode) objetos[objetos.length - 1];
		else return null;
	}

	/**
	 * Permite buscar un nodo en el arbol iniciando la busqueda desde otro nodo
	 * 
	 * @param n
	 *            Nodo desde el que iniciar la busqueda
	 * @param id
	 *            Identificador del nodo a buscar
	 * @return Nodo que se deseaba buscar o null si no se encontro u ocurrio
	 *         algun error.
	 */
	public static DefaultMutableTreeNode buscarFichero(
			DefaultMutableTreeNode n, int id)
	{
		if (!n.isRoot() && ( ( (MIDocumento) n.getUserObject() ).getId() == id ))
			return n;
		else if (n.getChildCount() > 0)
		{
			DefaultMutableTreeNode nodo = null;

			for (int i = 0; i < n.getChildCount(); ++i)
			{
				nodo = buscarFichero((DefaultMutableTreeNode) n.getChildAt(i),
						id);

				if (nodo != null) return nodo;
			}
			return nodo;
		}
		else return null;
	}

	/**
	 * Elimina un nodo del arbol
	 * 
	 * @param id
	 *            Identificador del nodo a eliminar
	 */
	public void eliminarNodo(int id)
	{
		DefaultMutableTreeNode n = ArbolDocumentos.buscarFichero(raiz, id);
		model.removeNodeFromParent(n);
	}

	/**
	 * Permite imprimir un documento
	 */
	public void imprimirFichero()
	{
		MIDocumento doc = getDocumentoSeleccionado();

		if (doc.esDirectorio()) return;

		Transfer t = new Transfer(ClienteFicheros.ipConexion, doc
				.getRutaLocal());

		Documento d = t.receiveDocumento(true);

		d.imprimir();
	}

	/**
	 * Permite guardar un documento de forma local
	 */
	public void guardarDocumentoLocalmente()
	{
		MIDocumento doc = this.getDocumentoSeleccionado();

		JFileChooser jfc = new JFileChooser("Guardar Documento Localmente");

		jfc.setDialogType(JFileChooser.SAVE_DIALOG);
		jfc.setSelectedFile(new File(doc.getNombre()));

		int op = jfc.showDialog(null, "Aceptar");

		if (op == JFileChooser.APPROVE_OPTION)
		{
			java.io.File f = jfc.getSelectedFile();

			if (doc.esDirectorio())
			{
				JOptionPane.showMessageDialog(null,
						"No puede guardar un directorio de forma local");
			}

			else
			{

				Transfer t = new Transfer(ClienteFicheros.ipConexion, doc
						.getRutaLocal());

				byte[] datos = t.receiveFileBytes();

				try
				{
					RandomAccessFile acf = new RandomAccessFile(f
							.getAbsolutePath(), "rw");

					acf.write(datos);

					acf.close();
				}
				catch (FileNotFoundException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				catch (IOException e3)
				{
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
			}
		}
	}

	/**
	 * Recupera la metainformacion relativa a un mensaje
	 * 
	 * @return Metainformacion del mensaje
	 */
	public MIDocumento recuperarMail()
	{
		// obtenemos el documento seleccionado
		MIDocumento doc = this.getDocumentoSeleccionado();

		// nos aseguramos de que sea un mensaje
		if (doc.esDirectorio()) return null;

		if (!doc.getTipo().equals(MIDocumento.TIPO_MENSAJE)) return null;

		// recuperamos los datos del mensaje
		Transfer t = new Transfer(ClienteFicheros.ipConexion, doc
				.getRutaLocal());

		byte[] datos = t.receiveFileBytes();

		// enviamos los datos
		doc.setMensaje(new String(datos));

		// devolvemos la metainformacion
		return doc;
	}

	/**
	 * Elimina el documento seleccionado (si se tienen los permisos suficientes)
	 * 
	 * @return True si se elimino el documento con exito. False en caso
	 *         contrario.
	 */
	public boolean eliminarFichero()
	{
		MIDocumento f = this.getDocumentoSeleccionado();

		if (f != null)
		{
			if (!f.esDirectorio()
					&& f.comprobarPermisos(DConector.Dusuario, DConector.Drol,
							MIDocumento.PERMISO_ESCRITURA))
			{

				ClienteFicheros.obtenerClienteFicheros().borrarFichero(f,
						DConector.Daplicacion);
				return true;
			}
			else if (f.comprobarPermisos(DConector.Dusuario, DConector.Drol,
					MIDocumento.PERMISO_ESCRITURA))
			{
				DefaultMutableTreeNode nodo = this.getNodoSeleccionado();

				if (nodo.getChildCount() == 0)
				{
					ClienteFicheros.obtenerClienteFicheros().borrarFichero(f,
							DConector.Daplicacion);
					return true;
				}
				else
				{
					JOptionPane
							.showMessageDialog(
									null,
									"No se puede eliminar la carpeta dado que esta tiene documentos y/o otras carpetas");

					return false;
				}

			}
			else return false;
		}
		else return false;
	}

	/**
	 * Agrega un nuevo directorio al directorio seleccionado actualmente
	 * 
	 * @param nombre
	 *            Nombre del directorio a agregar
	 * @return Metainformacion sobre el directorio creado o null si ocurrio
	 *         algun error.
	 */
	public MIDocumento agregarCarpeta(String nombre)
	{
		MIDocumento f = this.getDocumentoSeleccionado();

		if (f.esDirectorio())
		{

			// creamos el nodo
			MIDocumento nuevo = new MIDocumento();

			if (!f.getRutaLocal().equals("/"))
				nuevo.setRutaLocal(f.getRutaLocal() + "/" + nombre);
			else nuevo.setRutaLocal("/" + nombre);

			if (existeFichero((DefaultMutableTreeNode) this.getModel()
					.getRoot(), nuevo.getRutaLocal()))
			{
				JOptionPane.showMessageDialog(null, "La carpeta ya existe");
				return null;
			}

			// recuperamos el usuario y el rol
			MIUsuario user = ClienteMetaInformacion.cmi
					.getUsuarioConectado(DConector.Dusuario);
			MIRol rol = ClienteMetaInformacion.cmi.getRol(DConector.Drol);

			if (user == null || rol == null) return null;
			
			if (!f.comprobarPermisos(user.getNombreUsuario(), rol.getNombreRol(), MIDocumento.PERMISO_ESCRITURA))
			{
				JOptionPane.showMessageDialog(null, "No tiene permisos suficientes para crear la carpeta");
				return null;
			}

			nuevo.setNombre(nombre);
			nuevo.setPadre(f.getId());
			nuevo.setRol(rol);
			nuevo.setUsuario(user);
			nuevo.setPermisos("rwrw--");
			nuevo.setTipo("NULL");

			nuevo.esDirectorio(true);

			return nuevo;
		}
		else return null;
	}

	/**
	 * Comprueba si un documento existe en un determinado nodo
	 * 
	 * @param n
	 *            Nodo del arbol en el que buscamos el documento
	 * @param ruta
	 *            Path del documento
	 * @return True si el fichero existe en la ruta especificada. False en caso
	 *         contrario
	 */
	public boolean existeFichero(DefaultMutableTreeNode n, String ruta)
	{

		if (!n.isRoot()
				&& ( ( (MIDocumento) n.getUserObject() ).getRutaLocal()
						.equals(ruta) ))
			return true;

		else if (n.getChildCount() > 0)
		{

			for (int i = 0; i < n.getChildCount(); ++i)
			{
				if (existeFichero((DefaultMutableTreeNode) n.getChildAt(i),
						ruta)) return true;

			}
			return false;
		}
		else return false;
	}

	/**
	 * Buscar un documento en un nodo determinado
	 * 
	 * @param n
	 *            Nodo del arbol en el que buscamos el documento
	 * @param ruta
	 *            Path del documento
	 * @return Metainformacion del documento si fue encontrado o null si no se
	 *         pudo encontrar
	 */
	public MIDocumento buscarFichero(DefaultMutableTreeNode n, String ruta)
	{

		if (!n.isRoot()
				&& ( ( (MIDocumento) n.getUserObject() ).getRutaLocal()
						.equals(ruta) ))
			return (MIDocumento) n.getUserObject();

		else if (n.getChildCount() > 0)
		{

			MIDocumento f;

			for (int i = 0; i < n.getChildCount(); ++i)
			{
				f = buscarFichero((DefaultMutableTreeNode) n.getChildAt(i),
						ruta);
				if (f != null) return f;

			}
			return null;
		}
		else return null;
	}

	/**
	 * Sube un fichero al servidor en la carpeta que esta seleccionada
	 * actualmente. Si se intenta subir un fichero duplicado, muestra un mensaje
	 * de error indicando si el usuario desea: cancelar, cambiar el nombre o
	 * sobreescribir.
	 * 
	 * @return Evento para enviar al servidor de ficheros o null si ocurrio
	 *         algun error
	 */
	public DFileEvent subirFicheroServidor()
	{
		// obtenemos los datos del fichero asociados al nodo seleccionado
		MIDocumento carpeta = getDocumentoSeleccionado();

		// si el fichero escogido no es directorio, salimos
		if (carpeta == null)
		{
			JOptionPane.showMessageDialog(null,
					"Debe escoger un directorio al cual subir el documento");
			return null;
		}

		if (!carpeta.esDirectorio())
		{
			DefaultMutableTreeNode df = ArbolDocumentos.buscarFichero(
					(DefaultMutableTreeNode) getModel().getRoot(), carpeta
							.getPadre());

			carpeta = buscarFichero(df, ( (MIDocumento) df.getUserObject() )
					.getRutaLocal());
		}

		String path = carpeta.getRutaLocal() + "/";

		if (path.equals("//")) path = "/";

		// recuperamos el usuario y el rol
		MIUsuario user = ClienteMetaInformacion.cmi
				.getUsuarioConectado(DConector.Dusuario);
		MIRol rol = ClienteMetaInformacion.cmi.getRol(DConector.Drol);

		// si se ha producido algun error, salimos
		if (( user == null ) || ( rol == null )) return null;

		if (!carpeta.comprobarPermisos(user.getNombreUsuario(), rol
				.getNombreRol(), MIDocumento.PERMISO_ESCRITURA))
		{
			JOptionPane
					.showMessageDialog(null,
							"No tiene permiso para escribir en el directorio seleccionado");
			return null;
		}

		// mostramos el selector de ficheros
		JFileChooser jfc = new JFileChooser("Subir Documento Servidor");

		int op = jfc.showDialog(null, "Aceptar");

		// si no se ha escogido la opcion aceptar en el dialogo de apertura de
		// fichero salimos
		if (op != JFileChooser.APPROVE_OPTION) return null;

		java.io.File f = jfc.getSelectedFile();

		String nombre = f.getName();

		MIDocumento anterior = ClienteFicheros.cf.existeFichero(path + nombre,
				DConector.Daplicacion);

		while (anterior != null)
		{
			// si no tenemos permisos de escritura sobre el documento no podemos
			// sobrescribirlo
			if (!anterior.comprobarPermisos(DConector.Dusuario, DConector.Drol,
					MIDocumento.PERMISO_ESCRITURA))
			{
				JOptionPane
						.showMessageDialog(null,
								"No tiene suficientes privilegios para subir ese documento");
				return null;
			}

			Object[] options =
			{ "Sobreescribir", "Renombrar", "Cancelar" };

			int sel = JOptionPane.showOptionDialog(this,
					"El documento ya existe ¿Que desea hacer?",
					"Fichero ya existente", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

			// el usuario ha cancelado la accion
			if (sel == JOptionPane.CANCEL_OPTION)
				return null;

			// el usuario desea sobreescribir el documento
			else if (sel == JOptionPane.YES_OPTION)
			{
				ClienteFicheros.cf.generarVersion(anterior, path);

				eliminarNodo(anterior.getId());

				repaint();

				anterior = null;
			}

			// el usuario desea renombrar el fichero
			else if (sel == JOptionPane.NO_OPTION)
			{
				nombre = JOptionPane.showInputDialog("Nuevo nombre");

				if (nombre != null && !nombre.equals(""))
				{
					anterior = ClienteFicheros.cf.existeFichero(path + nombre,
							DConector.Daplicacion);
				}
				else return null;
			}
			// el usuario ha cerrado el dialogo
			else if (sel == JOptionPane.CLOSED_OPTION) return null;
		}

		byte[] bytes = null;
		try
		{
			// abrimos el fichero en modo lectura
			RandomAccessFile raf = new RandomAccessFile(f.getAbsolutePath(),
					"r");

			// consultamos el tamanio del fichero, reservamos
			// memoria suficiente, leemos el fichero y lo cerramos
			bytes = new byte[(int) raf.length()];
			raf.read(bytes);
			raf.close();
		}
		catch (FileNotFoundException ex)
		{
			JOptionPane.showMessageDialog(null, "El fichero no existe",
					"Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		catch (IOException e1)
		{
			JOptionPane.showMessageDialog(null,
					"Error en la lectura del fichero", "Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		// creamos el nuevo fichero a almacenar
		MIDocumento fbd = new MIDocumento(-1, nombre, false, "rwrw--", user,
				rol, carpeta.getId(), path + nombre, MIDocumento
						.getExtension(nombre));

		// enviamos el nuevo fichero al servidor
		Transfer t = new Transfer(ClienteFicheros.ipConexion, path + nombre);

		// si se ha producido algun error: MENSAJE y SALIMOS
		if (!t.sendFile(bytes))
		{
			JOptionPane
					.showMessageDialog(
							null,
							"No se ha podido subir el fichero.\nSe ha producido un error en la transmisión del documento",
							"Error", JOptionPane.ERROR_MESSAGE);

			return null;
		}

		// si no se ha producido ningun error al subir el fichero
		else
		{
			// insertamos el nuevo fichero en el servidor
			MIDocumento f2 = ClienteFicheros.cf.insertarNuevoFichero(fbd,
					DConector.Daplicacion);

			// si ha habido algun error salimos
			if (f2 == null)
			{
				JOptionPane
						.showMessageDialog(this,
								"Ha ocurrido un error: no se ha podido subir el documento al servidor");
				return null;
			}

			// notificamos al resto de usuarios la "novedad"
			DFileEvent evento = new DFileEvent();
			evento.fichero = f2;
			evento.padre = carpeta;
			evento.tipo = new Integer(DFileEvent.NOTIFICAR_INSERTAR_FICHERO
					.intValue());

			return evento;
		}
	}
}

/**
 * Clase que implementa el origen de datos de un Drag and Drop
 */
class TreeDragSource implements DragSourceListener, DragGestureListener
{
	private DragSource source;

	@SuppressWarnings( "unused" )
	private DragGestureRecognizer recognizer;

	private TransferableTreeNode transferable;

	private DefaultMutableTreeNode oldNode;

	private JTree sourceTree;

	public TreeDragSource( JTree tree, int actions )
	{
		sourceTree = tree;
		source = new DragSource();
		recognizer = source.createDefaultDragGestureRecognizer(sourceTree,
				actions, this);
	}

	public void dragGestureRecognized(DragGestureEvent dge)
	{
		TreePath path = sourceTree.getSelectionPath();
		if (( path == null ) || ( path.getPathCount() <= 1 ))
		{
			return;
		}
		oldNode = (DefaultMutableTreeNode) path.getLastPathComponent();
		transferable = new TransferableTreeNode(path);
		source.startDrag(dge, DragSource.DefaultMoveNoDrop, transferable, this);
	}

	public void dragEnter(DragSourceDragEvent dsde)
	{
	}

	public void dragExit(DragSourceEvent dse)
	{
	}

	public void dragOver(DragSourceDragEvent dsde)
	{
	}

	public void dropActionChanged(DragSourceDragEvent dsde)
	{
		System.out.println("Action: " + dsde.getDropAction());
		System.out.println("Target Action: " + dsde.getTargetActions());
		System.out.println("User Action: " + dsde.getUserAction());
	}

	public void dragDropEnd(DragSourceDropEvent dsde)
	{
		// System.out.println("Drop Action: " + dsde.getDropAction());
		if (dsde.getDropSuccess()
				&& ( dsde.getDropAction() == DnDConstants.ACTION_MOVE ))
		{
			( (DefaultTreeModel) sourceTree.getModel() )
					.removeNodeFromParent(oldNode);
		}
	}
}

/**
 * Clase que implementa el objetivo de un Drag and Drop
 */
class TreeDropTarget implements DropTargetListener
{

	DropTarget target;

	JTree targetTree;

	public TreeDropTarget( JTree tree )
	{
		targetTree = tree;
		target = new DropTarget(targetTree, this);
	}

	private TreeNode getNodeForEvent(DropTargetDragEvent dtde)
	{
		Point p = dtde.getLocation();
		DropTargetContext dtc = dtde.getDropTargetContext();
		JTree tree = (JTree) dtc.getComponent();
		TreePath path = tree.getClosestPathForLocation(p.x, p.y);
		return (TreeNode) path.getLastPathComponent();
	}

	public void dragEnter(DropTargetDragEvent dtde)
	{
		TreeNode node = getNodeForEvent(dtde);
		if (node.isLeaf())
		{
			dtde.rejectDrag();
		}
		else
		{
			dtde.acceptDrag(dtde.getDropAction());
		}
	}

	public void dragOver(DropTargetDragEvent dtde)
	{
		TreeNode node = getNodeForEvent(dtde);

		MIDocumento f = (MIDocumento) ( (DefaultMutableTreeNode) node )
				.getUserObject();

		// no se puede mover un documento a otro documento!!
		// ni tampoco mover cosas a una carpeta sin permiso de escritura
		if (!f.esDirectorio()
				|| !f.comprobarPermisos(DConector.Dusuario, DConector.Drol,
						MIDocumento.PERMISO_ESCRITURA))
		{
			dtde.rejectDrag();
		}
		else
		{
			Transferable tr = dtde.getTransferable();
			DataFlavor[] flavors = tr.getTransferDataFlavors();
			TreePath p;
			try
			{
				p = (TreePath) tr.getTransferData(flavors[0]);
				DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) p
						.getLastPathComponent();

				MIDocumento f2 = (MIDocumento) ( node2 ).getUserObject();

				if (f2.esDirectorio()
						|| !f2.comprobarPermisos(DConector.Dusuario,
								DConector.Drol, MIDocumento.PERMISO_ESCRITURA))
				{
					dtde.rejectDrag();
				}
				else
				{
					dtde.acceptDrag(dtde.getDropAction());
				}
			}
			catch (UnsupportedFlavorException e)
			{
				e.printStackTrace();
				dtde.rejectDrag();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				dtde.rejectDrag();
			}

		}
	}

	public void dragExit(DropTargetEvent dte)
	{
	}

	public void dropActionChanged(DropTargetDragEvent dtde)
	{
	}

	public void drop(DropTargetDropEvent dtde)
	{
		Point pt = dtde.getLocation();
		DropTargetContext dtc = dtde.getDropTargetContext();
		JTree tree = (JTree) dtc.getComponent();
		TreePath parentpath = tree.getClosestPathForLocation(pt.x, pt.y);
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) parentpath
				.getLastPathComponent();

		// rechazamos que se muevan documentos a cosas que no sean carpetas
		if (!( (MIDocumento) parent.getUserObject() ).esDirectorio())
		{
			dtde.rejectDrop();
			return;
		}

		try
		{
			Transferable tr = dtde.getTransferable();
			DataFlavor[] flavors = tr.getTransferDataFlavors();
			for (DataFlavor element : flavors)
			{
				if (tr.isDataFlavorSupported(element))
				{
					dtde.acceptDrop(dtde.getDropAction());
					TreePath p = (TreePath) tr.getTransferData(element);
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) p
							.getLastPathComponent();

					MIDocumento f = (MIDocumento) node.getUserObject();

					DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
					model.insertNodeInto(node, parent, 0);
					dtde.dropComplete(true);

					TreeNode[] nuevo = model.getPathToRoot(node);

					DefaultMutableTreeNode nuevoPadre = (DefaultMutableTreeNode) nuevo[nuevo.length - 2];

					String nuevoPath = ( (MIDocumento) nuevoPadre
							.getUserObject() ).getRutaLocal();

					if (nuevoPath.equals("/"))
						nuevoPath += f.getNombre();
					else nuevoPath += "/" + f.getNombre();

					f.setRutaLocal(nuevoPath);

					f.setPadre(( (MIDocumento) nuevoPadre.getUserObject() )
							.getId());

					DFileEvent evento = new DFileEvent();
					evento.fichero = f;

					evento.padre = (MIDocumento) nuevoPadre.getUserObject();

					evento.tipo = new Integer(
							DFileEvent.NOTIFICAR_MODIFICACION_FICHERO
									.intValue());

					PanelPrincipal.notificarModificacionFichero(evento);

					return;
				}
			}
			dtde.rejectDrop();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			dtde.rejectDrop();
		}
	}
}

/**
 * Clase que implementa un nodo que acepta el evento Drag and Drop
 */
class TransferableTreeNode implements Transferable
{
	public static DataFlavor TREE_PATH_FLAVOR = new DataFlavor(TreePath.class,
			"Tree Path");

	private DataFlavor flavors[] =
	{ TREE_PATH_FLAVOR };

	private TreePath path;

	public TransferableTreeNode( TreePath tp )
	{
		path = tp;
	}

	public synchronized DataFlavor[] getTransferDataFlavors()
	{
		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return ( flavor.getRepresentationClass() == TreePath.class );
	}

	public synchronized Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException
	{
		if (isDataFlavorSupported(flavor))
		{
			return path;
		}
		else
		{
			throw new UnsupportedFlavorException(flavor);
		}
	}
}
