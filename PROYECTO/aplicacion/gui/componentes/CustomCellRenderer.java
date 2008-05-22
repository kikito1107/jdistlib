package aplicacion.gui.componentes;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import aplicacion.fisica.documentos.FicheroBD;

public class CustomCellRenderer extends DefaultTreeCellRenderer
	{
		  /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Icon iconoCarpeta = null;
		  
		  public CustomCellRenderer(Icon cn)
		  {
			 iconoCarpeta = cn;
		  }
		  
		  public java.awt.Component getTreeCellRendererComponent(
	            JTree tree,
	            Object value,
	            boolean sel,
	            boolean expanded,
	            boolean leaf,
	            int row,
	            boolean hasFocus) 
		  {

			  super.getTreeCellRendererComponent(
	            tree, value, sel,
	            expanded, leaf, row,
	            hasFocus);
			  
			  if (leaf && isFolder(value)){
				  setIcon(iconoCarpeta);
				  setToolTipText("Carpeta vac’a");
			  }

			  return this;
		  }
		  
		  private boolean isFolder(Object value)
		  {
			  boolean res = false;
			  
			  if (value != null) {
			  
				  DefaultMutableTreeNode dftn = (DefaultMutableTreeNode)value;
				  
				  FicheroBD f = (FicheroBD)dftn.getUserObject();
				  
				  if ( f.esDirectorio()) 
					  res = true;
				  else
					  res = false;
			  }
			  
			  return res;
		  }
	}