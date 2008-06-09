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
import java.io.IOException;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import Deventos.enlaceJS.DConector;
import aplicacion.fisica.documentos.MIDocumento;
import aplicacion.fisica.eventos.DFileEvent;
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
	
	public DefaultMutableTreeNode getRaiz(){
		return raiz;
	}
	
	public DefaultTreeModel getModelo(){
		return model;
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
