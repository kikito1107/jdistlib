package componentes.util;

import java.awt.Component;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

/**
 * Componente lista que hace uso de entradas que tiene asociado un icono y un
 * texto
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
@SuppressWarnings( "unchecked" )
public class ListaElementos extends JList
{
	private static final long serialVersionUID = 1L;

	private Modelo model = null;

	/**
	 * Constructor
	 */
	public ListaElementos()
	{
		super();
		extrasConstructor();
	}

	private void extrasConstructor()
	{
		this.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		this.setCellRenderer(new CellRenderer());
		this.model = new Modelo();
		this.setModel(model);
	}

	/**
	 * Agrega un elemento a la lista
	 * 
	 * @param texto
	 *            Texto a agregar
	 */
	public void aniadirElemento(String texto)
	{
		model.addElement(new ElementoLista(null, texto));
	}

	/**
	 * Agrega un elemento a la lista en una posicion determinada
	 * 
	 * @param texto
	 *            Texto a agregar
	 * @param posicion
	 *            Posicion en la lista
	 */
	public void aniadirElemento(String texto, int posicion)
	{
		model.add(posicion, new ElementoLista(null, texto));
	}

	public void aniadirElemento(ImageIcon imagen, String texto)
	{
		model.addElement(new ElementoLista(imagen, texto));
		// model.addElement(new ElementoLista(this.img, texto));
	}

	/**
	 * Agrega un elemento a la lista con icono, un texto y en una posicion
	 * determinada
	 * 
	 * @param imagen
	 *            Icono del elemento
	 * @param texto
	 *            Texto del elemento
	 * @param posicion
	 *            Posicion del elemento en la lista
	 */
	public void aniadirElemento(ImageIcon imagen, String texto, int posicion)
	{
		model.add(posicion, new ElementoLista(imagen, texto));
	}

	/**
	 * Agrega un conjunto de elementos a la lista
	 * 
	 * @param elementos
	 *            Array de cadenas de texto a agregar
	 */
	public void aniadirElementos(String[] elementos)
	{
		for (String element : elementos)
			model.addElement(new ElementoLista(null, element));
	}

	/**
	 * Obtiene el numero de elementos que hay en la lista
	 * 
	 * @return Numero de elementos de la lista
	 */
	public int obtenerNumElementos()
	{
		return model.getSize();
	}

	/**
	 * Obtiene los elementos de la lista
	 * 
	 * @return Vector con los elementos de la lista (clase
	 * @see ElementoLista)
	 */
	@SuppressWarnings( "unchecked" )
	public Vector obtenerElementos()
	{
		Vector v = new Vector();
		ElementoLista el = null;
		for (int i = 0; i < obtenerNumElementos(); i++)
		{
			el = (ElementoLista) model.elementAt(i);
			v.add(new ElementoLista(el));
		}
		return v;
	}

	/**
	 * Obtiene el elemento seleccionado
	 * 
	 * @return Cadena con el texto del elemento seleccionado
	 */
	public String obtenerElementoSeleccionado()
	{
		String elemento = null;
		ElementoLista el = (ElementoLista) getSelectedValue();
		if (el != null) elemento = el.texto;
		return elemento;
	}

	/**
	 * Obtiene los elementos seleccionados en la lista
	 * 
	 * @return Elementos seleccionados
	 */
	public String[] obtenerElementosSeleccionados()
	{
		ElementoLista[] el = (ElementoLista[]) getSelectedValues();
		String[] els = new String[el.length];
		for (int i = 0; i < els.length; i++)
			els[i] = el[i].texto;

		return els;
	}

	/**
	 * Obtiene la posicion de un elemento
	 * 
	 * @param elemento
	 *            Elemento a buscar
	 * @return Posicion del elemento o -1 si no se encontro
	 */
	public int obtenerPosicionElemento(String elemento)
	{
		int pos = -1;
		try
		{
			int n = getModel().getSize();
			for (int i = 0; i < n; i++)
			{
				ElementoLista el = (ElementoLista) getModel().getElementAt(i);
				if (el != null) if (el.texto.equals(elemento)) pos = i;
			}
		}
		catch (Exception e)
		{
		}
		return pos;
	}

	/**
	 * Elimina todos los elementos de la lista
	 */
	public void eliminarElementos()
	{
		model.removeAllElements();
	}

	/**
	 * Elimina un elemento concreto de la lista
	 * 
	 * @param elemento
	 *            Elemento a eliminar
	 */
	public void eliminarElemento(String elemento)
	{
		model.removeElement(elemento);
	}

	/**
	 * Elimina el elemento de una posicion de la lista
	 * 
	 * @param posicion
	 *            Posicion del elemento a eliminar
	 */
	public void eliminarElemento(int posicion)
	{
		if (posicion < obtenerNumElementos()) model.remove(posicion);
	}

	/**
	 * Renderiza los elementos de la lista
	 */
	private class CellRenderer extends JLabel implements ListCellRenderer
	{
		private static final long serialVersionUID = -6591636148692100033L;

		/**
		 * Constructor por defecto
		 */
		public CellRenderer()
		{
			setOpaque(true);
			setHorizontalAlignment(LEFT);
			setVerticalAlignment(CENTER);
		}

		/**
		 * Pone el icono al elemento de la lista correspondiente
		 */
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus)
		{
			if (isSelected)
			{
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			}
			else
			{
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			// Set the icon and text. If icon was null, say so.
			ImageIcon icon = ( (ElementoLista) value ).imagen;

			String pet = ( (ElementoLista) value ).texto;
			setFont(list.getFont());
			setText(pet);
			setIcon(icon);

			return this;
		}
	}

	/**
	 * Modelo para la lista
	 */
	private class Modelo extends DefaultListModel
	{
		private static final long serialVersionUID = 1L;

		@Override
		public boolean removeElement(Object obj)
		{
			boolean eliminado = false;
			if (obj instanceof String)
			{
				ElementoLista el = new ElementoLista(null, (String) obj);
				eliminado = super.removeElement(el);
			}
			return eliminado;
		}

	}

}
