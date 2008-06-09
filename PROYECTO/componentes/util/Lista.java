package componentes.util;

import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

/**
 * Lista que solo permite seleccionar una entrada
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class Lista extends JList
{
	private static final long serialVersionUID = 1L;

	private DefaultListModel modelo = new DefaultListModel();

	/**
	 * Constructor por defecto
	 */
	public Lista()
	{
		extrasConstructor();
	}

	/**
	 * Constructor con parametros
	 * 
	 * @param p0
	 *            Conjunto de objetos para cada entrada de la lista
	 */
	public Lista( Object[] p0 )
	{
		super(p0);
		extrasConstructor();
	}

	/**
	 * Constructor con parametros
	 * 
	 * @param p0
	 *            Vector con las entradas de la lista
	 */
	@SuppressWarnings( "unchecked" )
	public Lista( Vector p0 )
	{
		super(p0);
		extrasConstructor();
	}

	/**
	 * Constructor con parametros
	 * 
	 * @param p0
	 *            Modelo de la lista
	 */
	public Lista( ListModel p0 )
	{
		super(p0);
		extrasConstructor();
	}

	private void extrasConstructor()
	{
		this.setModel(modelo);
		this.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
	}

	/**
	 * Agrega un elemento a la lista
	 * 
	 * @param elemento
	 *            Elemento a agregar
	 */
	public void aniadir(String elemento)
	{
		modelo.addElement(elemento);
	}

	/**
	 * Elimina un elemento de la lista
	 * 
	 * @param elemento
	 *            Elemento a eliminar
	 */
	public void eliminar(String elemento)
	{
		modelo.removeElement(elemento);
	}
}
