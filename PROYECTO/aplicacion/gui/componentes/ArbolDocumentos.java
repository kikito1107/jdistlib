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
 * Clase que se encargar‡ de almacenar los datos de los documentos
 * @author anab
 */
public class ArbolDocumentos extends JTree implements Autoscroll {
	    
		private int margin = 12;


	    public void autoscroll(Point p) {
	      int realrow = getRowForLocation(p.x, p.y);
	      Rectangle outer = getBounds();
	      realrow = (p.y + outer.y <= margin ? realrow < 1 ? 0 : realrow - 1
	          : realrow < getRowCount() - 1 ? realrow + 1 : realrow);
	      scrollRowToVisible(realrow);
	    }

	    public Insets getAutoscrollInsets() {
	      Rectangle outer = getBounds();
	      Rectangle inner = getParent().getBounds();
	      return new Insets(inner.y - outer.y + margin, inner.x - outer.x
	          + margin, outer.height - inner.height - inner.y + outer.y
	          + margin, outer.width - inner.width - inner.x + outer.x
	          + margin);
	    }


	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3359982437919729727L;

	private DefaultMutableTreeNode raiz = null;
	private DefaultTreeModel model = null;
	
	TreeDragSource ds;

	TreeDropTarget dt;
	
	/**
	 * 
	 * @param raiz
	 */
	public ArbolDocumentos( DefaultMutableTreeNode raiz )
	{
		super(raiz);
		this.setRootVisible(false);
		this.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.setCellRenderer(new DocumentosCellRenderer());
		//this.putClientProperty("JTree.lineStyle", "Horizontal");
		
		ds = new TreeDragSource(this, DnDConstants.ACTION_COPY_OR_MOVE);
	    dt = new TreeDropTarget(this);
		this.raiz = raiz;
		
		model = (DefaultTreeModel) this.getModel();
		
		this.expandRow(0);
	}

	/**
	 * 
	 * @return
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
	 * 
	 * @return
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
	 * 
	 * @param n
	 * @param id
	 * @return
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
	
	
	public void eliminarNodo(int id){
		DefaultMutableTreeNode n = ArbolDocumentos.buscarFichero(raiz, id);
		model.removeNodeFromParent(n);
	}

	/**
	 * 
	 *
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
	 * 
	 *
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

			if (doc.esDirectorio()) return;

			Transfer t = new Transfer(ClienteFicheros.ipConexion, doc
					.getRutaLocal());

			byte[] datos = t.receiveFileBytes();

			try
			{
				RandomAccessFile acf = new RandomAccessFile(
						f.getAbsolutePath(), "rw");

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

	/**
	 * Recupera la metainformacion relativa a un mensaje
	 * @return la metainformacion del mensaje
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
	 * Elimina el fichero seleccionado (si se tienen los permisos suficientes)
	 * @return
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
	 * Agrega una carpeta a la carpeta seleccionada anteriormente
	 * @param nombre nuevo nombre de la carpeta
	 * @return el ficheroBD con los datos de la nueva carpeta
	 */
	public MIDocumento agregarCarpeta(String nombre)
	{
		MIDocumento f = this.getDocumentoSeleccionado();

		if (f.esDirectorio())
		{

			//creamos el nodo
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

			//recuperamos el usuario y el rol
			MIUsuario user = ClienteMetaInformacion.cmi
					.getUsuarioConectado(DConector.Dusuario);
			MIRol rol = ClienteMetaInformacion.cmi.getRol(DConector.Drol);

			if (user == null || rol == null) return null;

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
	 * Comprueba si un fichero determinado existe en un determinado nodo
	 * @param n nodo nodo del arbol en el que buscamos el documento
	 * @param ruta ruta del fichero
	 * @return true si el fichero ya existe en la ruta y false en caso contrario
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
	 * Comprueba si un fichero determinado existe en un determinado nodo
	 * @param n nodo nodo del arbol en el que buscamos el documento
	 * @param ruta ruta del fichero
	 * @return true si el fichero ya existe en la ruta y false en caso contrario
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
}

//TreeDragSource.java
//A drag source wrapper for a JTree. This class can be used to make
//a rearrangeable DnD tree with the TransferableTreeNode class as the
//transfer data type.

class TreeDragSource implements DragSourceListener, DragGestureListener
{

	DragSource source;

	DragGestureRecognizer recognizer;

	TransferableTreeNode transferable;

	DefaultMutableTreeNode oldNode;

	JTree sourceTree;

	public TreeDragSource( JTree tree, int actions )
	{
		sourceTree = tree;
		source = new DragSource();
		recognizer = source.createDefaultDragGestureRecognizer(sourceTree,
				actions, this);
	}

	/*
	 * Drag Gesture Handler
	 */
	public void dragGestureRecognized(DragGestureEvent dge)
	{
		TreePath path = sourceTree.getSelectionPath();
		if (( path == null ) || ( path.getPathCount() <= 1 ))
		{
			// We can't move the root node or an empty selection
			return;
		}
		oldNode = (DefaultMutableTreeNode) path.getLastPathComponent();
		transferable = new TransferableTreeNode(path);
		source.startDrag(dge, DragSource.DefaultMoveNoDrop, transferable, this);

		// If you support dropping the node anywhere, you should probably
		// start with a valid move cursor:
		//source.startDrag(dge, DragSource.DefaultMoveDrop, transferable,
		// this);
	}

	/*
	 * Drag Event Handlers
	 */
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
		/*
		 * to support move or copy, we have to check which occurred:
		 */
		System.out.println("Drop Action: " + dsde.getDropAction());
		if (dsde.getDropSuccess()
				&& ( dsde.getDropAction() == DnDConstants.ACTION_MOVE ))
		{
			( (DefaultTreeModel) sourceTree.getModel() )
					.removeNodeFromParent(oldNode);
		}

		/*
		 * to support move only... if (dsde.getDropSuccess()) {
		 * ((DefaultTreeModel)sourceTree.getModel()).removeNodeFromParent(oldNode); }
		 */
	}
}

//TreeDropTarget.java
//A quick DropTarget that's looking for drops from draggable JTrees.
class TreeDropTarget implements DropTargetListener
{

	DropTarget target;

	JTree targetTree;

	public TreeDropTarget( JTree tree )
	{
		targetTree = tree;
		target = new DropTarget(targetTree, this);
	}

	/*
	 * Drop Event Handlers
	 */
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
			// start by supporting move operations
			//dtde.acceptDrag(DnDConstants.ACTION_MOVE);
			dtde.acceptDrag(dtde.getDropAction());
		}
	}

	public void dragOver(DropTargetDragEvent dtde)
	{
		TreeNode node = getNodeForEvent(dtde);
		
		MIDocumento f = (MIDocumento) ((DefaultMutableTreeNode)node).getUserObject();
		
		// no se puede mover un documento a otro documento!!
		// ni tampoco mover cosas a una carpeta sin permiso de escritura
		if (!f.esDirectorio() || !f.comprobarPermisos(DConector.Dusuario, DConector.Drol, MIDocumento.PERMISO_ESCRITURA))
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
				
				MIDocumento f2 = (MIDocumento) (node2).getUserObject();
		
				if (f2.esDirectorio() || !f2.comprobarPermisos(DConector.Dusuario, DConector.Drol, MIDocumento.PERMISO_ESCRITURA))
				{
					dtde.rejectDrag();
				} 
				else 
				{
				
					// start by supporting move operations
					//dtde.acceptDrag(DnDConstants.ACTION_MOVE);
					dtde.acceptDrag(dtde.getDropAction());
				}
			}
			catch (UnsupportedFlavorException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				dtde.rejectDrag();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
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
		if ( !((MIDocumento)parent.getUserObject()).esDirectorio() )
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
					
					MIDocumento f = (MIDocumento)node.getUserObject();
					
					
					DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
					model.insertNodeInto(node, parent, 0);
					dtde.dropComplete(true);
					
					//TODO cambiar el path al al nodo movido
					
					
					//JOptionPane.showMessageDialog(null, "el nodo movido es: " + f.getNombre());
					
					TreeNode[] nuevo = model.getPathToRoot(node);
					
					DefaultMutableTreeNode nuevoPadre = (DefaultMutableTreeNode)nuevo[nuevo.length-2];
					
					String nuevoPath = ((MIDocumento) nuevoPadre.getUserObject()).getRutaLocal();
					
					if (nuevoPath.equals("/"))
						nuevoPath +=  f.getNombre();
					else
						nuevoPath +=  "/" + f.getNombre();
					
					
					
					f.setRutaLocal(nuevoPath);
					
					f.setPadre(((MIDocumento) nuevoPadre.getUserObject()).getId());
					
					
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

//TransferableTreeNode.java
//A Transferable TreePath to be used with Drag & Drop applications.
//
class TransferableTreeNode implements Transferable
{

	public static DataFlavor TREE_PATH_FLAVOR = new DataFlavor(TreePath.class,
			"Tree Path");

	DataFlavor flavors[] =
	{ TREE_PATH_FLAVOR };

	TreePath path;

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
