package aplicacion.gui.componentes;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import aplicacion.fisica.ClienteFicheros;
import aplicacion.fisica.GestorFicherosBD;
import aplicacion.fisica.documentos.FicheroBD;

/**
 * Clase que se encargar‡ de almacenar los datos de los documentos NOTA: de
 * momento esto es una clase STUB, los datos son generados desde el programa.
 * 
 * @author anab
 */
public class ArbolDocumentos
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3359982437919729727L;

	DefaultMutableTreeNode raiz = null;

	ClienteFicheros cf = null;

	GestorFicherosBD gestor = null;

	String usuario = null;

	String rol = null;

	public ArbolDocumentos( String user, String rol2, String clave,
			String aplicacion )
	{
		raiz = new DefaultMutableTreeNode("");
		usuario = user;
		rol = rol2;
		gestor = new GestorFicherosBD();
		cf = new ClienteFicheros(aplicacion, user, clave, rol2);
		raiz = cf.getRaiz();

		cf.insertarNuevoFichero(new FicheroBD(), aplicacion);
		cf.borrarFichero(new FicheroBD(), aplicacion);
		cf.modificarFichero(new FicheroBD(), aplicacion);
	}

	public DefaultMutableTreeNode getRaiz()
	{
		return raiz;
	}

	public void insertarDocumento(String nombre,
			DefaultMutableTreeNode carpeta_padre)
	{
		insertarDocumento(nombre, carpeta_padre, "rwr---");
	}

	public void insertarDocumento(String nombre,
			DefaultMutableTreeNode carpeta_padre, String permisos)
	{

	}

	public void insertarCarpeta(String nombre,
			DefaultMutableTreeNode carpeta_padre)
	{
		insertarCarpeta(nombre, carpeta_padre, "rwr---");
	}

	public void insertarCarpeta(String nombre,
			DefaultMutableTreeNode carpeta_padre, String permisos)
	{

	}

	public File obtenerFichero(DefaultMutableTreeNode nodo)
	{
		if (nodo.getChildCount() == 0)
		{
			if (buscarNodo(nodo.toString()) != null)
				return null;
			else return null;
		}
		else return null;
	}

	public DefaultMutableTreeNode buscarNodo(String nombre)
	{
		java.util.Enumeration<DefaultMutableTreeNode> en = raiz
				.preorderEnumeration();
		DefaultMutableTreeNode actual = null;

		while (en.hasMoreElements())
		{
			actual = en.nextElement();
			if (actual.toString().compareTo(nombre) == 0) return actual;
		}

		return null;
	}

	public static void cambiarIconosArbol(JTree arbol, String close, String leaf)
	{
		// Retrieve the three icons
		Icon leafIcon = new ImageIcon(leaf);
		Icon closedIcon = new ImageIcon(close);
		Icon editIcon = new ImageIcon("./Resources/nodo_edit.png");

		// Update only one tree instance
		CustomCellRenderer renderer = new CustomCellRenderer(closedIcon, editIcon);
		renderer.setLeafIcon(leafIcon);
		renderer.setClosedIcon(closedIcon);
		renderer.setOpenIcon(closedIcon);

		arbol.setCellRenderer(renderer);
	}
}
