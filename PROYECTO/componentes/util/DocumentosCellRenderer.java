package componentes.util;

import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import aplicacion.fisica.documentos.MIDocumento;


import Deventos.enlaceJS.DConector;

/**
 * Clase que modela cada una de las celdas del arbol de documentos
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class DocumentosCellRenderer extends DefaultTreeCellRenderer
{
	private static final long serialVersionUID = 1L;

	private Icon iconoCarpeta = new ImageIcon("Resources/folder.gif");

	private Icon iconoEditar = new ImageIcon("Resources/nodo_edit.png");;

	private Icon iconoPDF = new ImageIcon("Resources/file_acrobat.gif");

	private Icon iconoIMG = new ImageIcon("Resources/image.gif");

	private Icon iconoDOC = new ImageIcon("Resources/page_white_word.png");

	private Icon iconoTXT = new ImageIcon("Resources/page_white_text.png");

	private Icon iconoUNK = new ImageIcon("Resources/page_white.png");

	private Icon mail = new ImageIcon("Resources/icon_email.gif");

	private Icon iconInbox = new ImageIcon("Resources/folder_inbox.png");

	/**
	 * Constructor
	 */
	public DocumentosCellRenderer()
	{
		setLeafIcon(iconoUNK);
		setClosedIcon(iconoCarpeta);
		setOpenIcon(iconoCarpeta);
	}

	/**
	 * Componente asociado a una celda
	 * 
	 * @param tree
	 *            Arbol al que le vamos a asociar la celda
	 * @param value
	 *            Valor de la celda
	 * @param sel
	 *            Indica si esta seleccionada o no
	 * @param leaf
	 *            Indica si es un nodo hoja o no
	 * @param row
	 *            Indica que fila del arbol es la de la celda
	 * @param hasFocus
	 *            Indica si tiene el foco de seleccion
	 * @return Componente asociado a la celda
	 */
	@Override
	public java.awt.Component getTreeCellRendererComponent(JTree tree,
			Object value, boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		if (leaf && estaEditandose(value))
			setIcon(iconoEditar);
		else if (leaf && isPDF(value))
			setIcon(iconoPDF);
		else if (leaf && isIMG(value))
			setIcon(this.iconoIMG);
		else if (leaf && isDOC(value))
			setIcon(this.iconoDOC);
		else if (leaf && isTXT(value))
			setIcon(this.iconoTXT);
		else if (leaf && isMAIL(value))
			setIcon(mail);
		else if (isINBOX(value))
			setIcon(iconInbox);
		else if (leaf && isFolder(value))
		{
			setIcon(iconoCarpeta);
			setToolTipText("Carpeta vacía");
		}

		return this;
	}

	private boolean isINBOX(Object value)
	{
		if (value != null)
		{

			DefaultMutableTreeNode dftn = (DefaultMutableTreeNode) value;

			MIDocumento f = (MIDocumento) dftn.getUserObject();

			if (f != null)
			{
				String extension = f.getTipo();

				if (!f.esDirectorio()) return false;

				if (extension != null
						&& extension.equals(MIDocumento.TIPO_BANDEJA_MAIL))
					return true;
				else return false;
			}
			else return false;
		}
		return false;
	}

	private boolean isDOC(Object value)
	{
		if (value != null)
		{

			DefaultMutableTreeNode dftn = (DefaultMutableTreeNode) value;

			MIDocumento f = (MIDocumento) dftn.getUserObject();

			String extension = f.getTipo();

			if (extension != null && extension.equals(MIDocumento.TIPO_DOC))
				return true;
			else return false;
		}
		return false;
	}

	private boolean isIMG(Object value)
	{
		if (value != null)
		{

			DefaultMutableTreeNode dftn = (DefaultMutableTreeNode) value;

			MIDocumento f = (MIDocumento) dftn.getUserObject();

			String extension = f.getTipo();

			if (extension != null && extension.equals(MIDocumento.TIPO_IMAGEN))
				return true;
			else return false;
		}
		return false;
	}

	private boolean isPDF(Object value)
	{
		if (value != null)
		{

			DefaultMutableTreeNode dftn = (DefaultMutableTreeNode) value;

			MIDocumento f = (MIDocumento) dftn.getUserObject();

			String extension = f.getTipo();

			if (extension != null && extension.equals(MIDocumento.TIPO_PDF))
				return true;
			else return false;
		}
		return false;
	}

	private boolean isMAIL(Object value)
	{
		if (value != null)
		{

			DefaultMutableTreeNode dftn = (DefaultMutableTreeNode) value;

			MIDocumento f = (MIDocumento) dftn.getUserObject();

			String extension = f.getTipo();

			if (extension != null && extension.equals(MIDocumento.TIPO_MENSAJE))
				return true;
			else return false;
		}
		return false;
	}

	private boolean isTXT(Object value)
	{
		if (value != null)
		{

			DefaultMutableTreeNode dftn = (DefaultMutableTreeNode) value;

			MIDocumento f = (MIDocumento) dftn.getUserObject();

			String extension = f.getTipo();

			if (extension != null
					&& extension.equals(MIDocumento.TIPO_BANDEJA_TXT))
				return true;
			else return false;
		}
		return false;
	}

	private boolean estaEditandose(Object value)
	{

		if (value != null)
		{

			DefaultMutableTreeNode dftn = (DefaultMutableTreeNode) value;

			MIDocumento f = (MIDocumento) dftn.getUserObject();

			Vector<String> v = DConector.obtenerDC().consultarEditores(
					f.getRutaLocal());

			if (v == null)
				return false;
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

			MIDocumento f = (MIDocumento) dftn.getUserObject();

			if (f.esDirectorio())
				res = true;
			else res = false;
		}

		return res;
	}
}
