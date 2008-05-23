package componentes.gui.usuarios;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Clase que encapsula el comportamiento del ‡rbol que lista los usuarios por
 * rol
 * 
 * @author Ana Belen Pelegrina Ortiz
 */
public class ArbolUsuarios extends JTree
{
	private static final long serialVersionUID = 6421964450995473440L;

	/**
	 * Nodo ra’z de ‡rbol
	 */
	DefaultMutableTreeNode raiz;

	DefaultTreeModel modelo = null;

	/**
	 * 
	 */
	ArbolUsuarios esto;

	/**
	 * Constructor del çrbol
	 * 
	 * @param root
	 *            raiz del ‡rbol
	 */
	public ArbolUsuarios( DefaultMutableTreeNode root )
	{
		super(root);
		raiz = root;
		esto = this;
		modelo = (DefaultTreeModel) this.getModel();
		expandir();
	}

	/**
	 * Expande el arbol
	 */
	public void expandir()
	{
		int row = 0;
		while (row < this.getRowCount())
		{
			this.expandRow(row);
			row++;
		}
	}

	/**
	 * Inserta un nuevo usuario en el ‡rbol en la rama correspondiente a su rol
	 * 
	 * @param Rol
	 *            rol del nuevo usuario
	 * @param usuario
	 *            nombre del nuevo usuario
	 */
	public void insertarNuevoUsuario(String Rol, String usuario)
	{
		DefaultMutableTreeNode unNodoRol = buscarRol(Rol), hijo = new DefaultMutableTreeNode(
				usuario);

		if (( Rol != null ) && ( usuario != null ) && ( usuario != "" )
				&& ( Rol != "" ) && ( unNodoRol != null ))
		{

			System.out.println("El rol encontrado es "
					+ unNodoRol.getUserObject());
			modelo.insertNodeInto(hijo, unNodoRol, unNodoRol.getChildCount());

			this.expandir();
		}
	}

	/**
	 * Busca el nodo del ‡rbol del que cuelgan todos los usuario bajo el rol Rol
	 * 
	 * @param Rol
	 *            rol a buscar
	 * @return el nodo en cuesti—n. Si no lo encuentra devuelve <b>null</b>
	 */
	public DefaultMutableTreeNode buscarRol(String Rol)
	{
		int numRoles = modelo.getChildCount(raiz);
		DefaultMutableTreeNode tmp, nodo = null;
		boolean encontrado = false;

		for (int i = 0; ( i < numRoles ) && !encontrado; ++i)
		{
			tmp = (DefaultMutableTreeNode) modelo.getChild(raiz, i);

			System.out.println("comparando con " + tmp.getUserObject());
			if (tmp.getUserObject().equals(Rol))
			{
				encontrado = true;
				nodo = tmp;
			}
		}

		return nodo;
	}

	/**
	 * Elimina un usuario del arbol
	 * 
	 * @param Usuario
	 *            nombre del usuario a eliminar
	 */
	public void eliminarUsuario(String Usuario)
	{
		int numHijos = modelo.getChildCount(raiz);
		DefaultMutableTreeNode unHijo = null;
		boolean seguir = true;

		for (int i = 0; ( i < numHijos ) && seguir; ++i)
		{
			unHijo = (DefaultMutableTreeNode) modelo.getChild(raiz, i);

			DefaultMutableTreeNode posUsuario = buscarUsuario(unHijo, Usuario);

			if (posUsuario != null)
			{
				try
				{
					modelo.removeNodeFromParent(posUsuario);
					seguir = false;
				}
				catch (Exception e)
				{
				};
			}
		}
	}

	/**
	 * Elimina un usuario del arbol
	 * 
	 * @param Rol
	 *            rol bajo el que esta actualmente el usuario
	 * @param Usuario
	 *            usuario a eliminar
	 */
	public void eliminarUsuario(String Rol, String Usuario)
	{
		DefaultMutableTreeNode nodoRol = this.buscarRol(Rol);

		DefaultMutableTreeNode hijo = this.buscarUsuario(nodoRol, Usuario);

		nodoRol.remove(hijo);

		this.expandir();
	}

	/**
	 * Cambiar el rol de un usuario en el arbol
	 * 
	 * @param antiguoRol
	 *            rol actual del usuario
	 * @param nuevoRol
	 *            nuevo rol
	 * @param usuario
	 *            nombre del usuario
	 */
	public void cambiarRol(String antiguoRol, String nuevoRol, String usuario)
	{
		this.eliminarUsuario(antiguoRol, usuario);
		this.insertarNuevoUsuario(nuevoRol, usuario);

		this.expandir();
	}

	/**
	 * Busca un usuario dentro del arbol
	 * 
	 * @param Rol
	 *            rol bajo el que se encuentra actualmete el usuario
	 * @param usuario
	 *            nombre del usuario
	 * @return posicion en la que se encuentra el usuario
	 */
	public int buscarUsuario(String Rol, String usuario)
	{
		DefaultMutableTreeNode ramaRol = buscarRol(Rol);
		DefaultMutableTreeNode unaHoja = null;
		int indice = -1;
		boolean seguir = true;

		if (ramaRol != null)
		{
			int numHojas = ramaRol.getChildCount();

			for (int i = 0; ( i < numHojas ) && seguir; ++i)
			{
				unaHoja = (DefaultMutableTreeNode) modelo.getChild(ramaRol, i);

				if (unaHoja.getUserObject().equals(usuario))
				{
					indice = i;
					seguir = false;
				}
			}

		}
		return indice;
	}

	/**
	 * Busca un usuario en el ‡rbol
	 * 
	 * @param ramaRol
	 *            nodo en el que se encuentra el usuario
	 * @param usuario
	 *            nombre del usuario
	 * @return indice donde se encuentra el usuario
	 */
	public DefaultMutableTreeNode buscarUsuario(DefaultMutableTreeNode ramaRol,
			String usuario)
	{
		int numHojas = modelo.getChildCount(ramaRol);
		DefaultMutableTreeNode unaHoja = null, hijo = null;

		int indice = -1;
		boolean seguir = true;

		for (int i = 0; ( i < numHojas ) && seguir; ++i)
		{
			unaHoja = (DefaultMutableTreeNode) modelo.getChild(ramaRol, i);

			if (unaHoja.getUserObject().equals(usuario))
			{
				indice = i;
				seguir = false;
				hijo = unaHoja;
			}
		}
		return hijo;
	}
}
