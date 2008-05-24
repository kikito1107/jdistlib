package aplicacion.gui.componentes;

import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
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

	private Icon iconoPDF = new ImageIcon("./Resources/file_acrobat.gif");
	
	private Icon iconoIMG = new ImageIcon("./Resources/image.gif");
	
	private Icon iconoDOC = new ImageIcon("./Resources/page_white_word.png");
	
	private Icon iconoTXT = new ImageIcon("./Resources/page_white_text.png");

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
		else if(leaf && isPDF(value))
			setIcon(iconoPDF );
		else if (leaf && isIMG(value))
			setIcon(this.iconoIMG);
		else if (leaf && isDOC(value))
			setIcon(this.iconoDOC);
		else if (leaf && isTXT(value))
			setIcon(this.iconoTXT);
		
		return this;
	}
	
	private boolean isDOC(Object value)
	{
		if (value != null) 
		{
		
			DefaultMutableTreeNode dftn = (DefaultMutableTreeNode) value;

			FicheroBD f = (FicheroBD) dftn.getUserObject();
			
			String extension  = f.getTipo();
			
			if (extension != null && extension.equals("doc")) return true;
			else return false;
		}
		return false;
	}

	private boolean isIMG(Object value)
	{
		if (value != null) 
		{
		
			DefaultMutableTreeNode dftn = (DefaultMutableTreeNode) value;

			FicheroBD f = (FicheroBD) dftn.getUserObject();
			
			String extension  = f.getTipo();
			
			if (extension != null && extension.equals("img")) return true;
			else return false;
		}
		return false;
	}

	private boolean isPDF(Object value)
	{
		if (value != null) 
		{
		
			DefaultMutableTreeNode dftn = (DefaultMutableTreeNode) value;

			FicheroBD f = (FicheroBD) dftn.getUserObject();
			
			String extension  = f.getTipo();
			
			
			if ( extension != null && extension.equals("pdf")) return true;
			else return false;
		}
		return false;
	}

	private boolean isTXT(Object value)
	{
		if (value != null) 
		{
		
			DefaultMutableTreeNode dftn = (DefaultMutableTreeNode) value;

			FicheroBD f = (FicheroBD) dftn.getUserObject();
			
			String extension  = f.getTipo();
			
			if (extension != null && extension.equals("txt")) return true;
			else return false;
		}
		return false;
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
