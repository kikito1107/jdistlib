package componentes.util;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * Clase que permite renderizar una celda del arbol de usuarios con iconos para
 * cada uno de los componentes personalizados
 */
public class ArbolUsuariosCellRenderer extends DefaultTreeCellRenderer
{
	private static final long serialVersionUID = -7075327761579832712L;

	private Icon noIcon = null;

	/**
	 * Constructor
	 * 
	 * @param n
	 *            Icono para un nodo cualquiera
	 */
	public ArbolUsuariosCellRenderer( Icon n )
	{
		noIcon = n;
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		if (leaf && isRolNode(value))
		{
			setIcon(noIcon);
			setToolTipText("Sin usuarios conectados.");
		}
		else setToolTipText(null); // no tool tip

		return this;
	}

	/**
	 * Comprueba si el nodo se correponde al de un rol
	 * 
	 * @param value
	 *            Nodo a examinar
	 * @return True si el nodo es un nodo de rol. False en otro caso
	 */
	private boolean isRolNode(Object value)
	{
		boolean res = false;

		if (value != null)
		{

			DefaultMutableTreeNode dftn = (DefaultMutableTreeNode) value;

			if (dftn.getParent() == null)
				res = false;

			else if (dftn.getParent().getParent() == null) res = true;
		}

		return res;
	}
}

