package aplicacion.gui.componentes;

import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import Deventos.enlaceJS.DConector;
import aplicacion.fisica.documentos.FicheroBD;

public class CustomCellRenderer extends DefaultTreeCellRenderer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Icon iconoCarpeta = null;

	private Icon iconoEditar = null;

	public CustomCellRenderer( Icon cn, Icon edit)
	{
		iconoCarpeta = cn;
		iconoEditar = edit;
	}

	@Override
	public java.awt.Component getTreeCellRendererComponent(JTree tree,
			Object value, boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		if (leaf && isFolder(value))
		{
			setIcon(iconoCarpeta);
			setToolTipText("Carpeta vac’a");
		}
		else if(leaf && estaEditandose(value))
			setIcon(iconoEditar);
	

		return this;
	}
	
	private boolean estaEditandose(Object value){
		

		if (value != null) 
		{
		
			DefaultMutableTreeNode dftn = (DefaultMutableTreeNode) value;

			FicheroBD f = (FicheroBD) dftn.getUserObject();
			
			Vector<String> v = DConector.obtenerDC().consultarEditores(f.getRutaLocal());
			
			if (v == null) return false;
			else return true;
		}
		return false;
	}

	private boolean isFolder(Object value)
	{
		boolean res = false;

		if (value != null)
		{

			DefaultMutableTreeNode dftn = (DefaultMutableTreeNode) value;

			FicheroBD f = (FicheroBD) dftn.getUserObject();

			if (f.esDirectorio())
				res = true;
			else res = false;
		}

		return res;
	}
}
