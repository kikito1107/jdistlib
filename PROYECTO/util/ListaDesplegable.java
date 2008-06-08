package util;

import javax.swing.JComboBox;

/**
 * Lista desplegable con multitud de metodos utiles que facilitan su uso
 * 
 * @author LooPer (ejido2002@hotmail.com)
 */

public class ListaDesplegable extends JComboBox
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor por defecto
	 */
	public ListaDesplegable()
	{
		super();
	}

	/**
	 * Constructor al que se le pasa la lista de elementos a representar
	 * 
	 * @param listaDatos
	 *            Array de elementosq que deseamos añadir
	 */
	public ListaDesplegable( String[] listaDatos )
	{
		super(listaDatos);
	}

	/**
	 * Seleccion de un elemento por su indice
	 * 
	 * @param i
	 *            Indice del elemento que queremos seleccionar
	 */
	public void seleccionar(int i)
	{
		this.setSelectedIndex(i);
	}

	/**
	 * Obtener indice del elemento seleccionado
	 * 
	 * @returns Indice del elemento seleccionado
	 */
	public int getIndiceSeleccionado()
	{
		return this.getSelectedIndex();
	}

	/**
	 * Añadir un elemento a la lista
	 * 
	 * @param cadena
	 *            Elemento que deseamos añadir
	 */
	public void aniadir(String cadena)
	{
		this.addItem(cadena);
	}

	/**
	 * Añadir un array de elementos a la lista
	 * 
	 * @param cadenas
	 *            Array de elementos que deseamos añadir
	 */
	public void aniadir(String[] cadenas)
	{
		for (String element : cadenas)
			this.addItem(element);
	}

	/**
	 * Obtener numero de elementos en la lista
	 * 
	 * @returns Numero de elementos en la lista
	 */
	public int getNumeroElementos()
	{
		return this.getItemCount();
	}

	/**
	 * Obtener elemento seleccionado
	 * 
	 * @returns Elemento seleccionado
	 */
	public String getSeleccionado()
	{
		return (String) this.getSelectedItem();
	}

	/**
	 * Obtener elemento en el indice elegido
	 * 
	 * @param i
	 *            Indice del elemento que deseamos obtener
	 * @returns Elemento deseado
	 */
	public String getElementoEnIndice(int i)
	{
		return (String) this.getItemAt(i);
	}

	/**
	 * Seleccionar elemento en el indice i
	 * 
	 * @param i
	 *            Indice del elemento que deseamos seleccionar
	 */
	public void seleccionarIndice(int i)
	{
		this.setSelectedIndex(i);
	}

	/**
	 * Elemento que deseamos seleccionar
	 * 
	 * @param cadena
	 *            Elemento que deseamos seleccionar
	 */
	public void seleccionar(String cadena)
	{
		this.setSelectedItem(cadena);
	}

	/**
	 * Eliminar todos los elementos
	 * 
	 */
	public void vaciar()
	{
		this.removeAllItems();
	}

	/**
	 * Borra el elemento deseado
	 * 
	 * @param cadena
	 *            Elemento que deseamos borrar
	 */
	public void borrarElemento(String cadena)
	{
		this.removeItem(cadena);
	}

	/**
	 * Eliminar elemento en el indice deseado
	 * 
	 * @param i
	 *            Indice del elemento que deseamos eliminar
	 */
	public void borrarElementoEnIndice(int i)
	{
		this.removeItemAt(i);
	}

	/**
	 * Ordenar los elementos en orden ascendente
	 */
	public void ordenarAscendente()
	{
		this.ordenar(false);
	}

	/**
	 * Ordenar los elementos en orden descendente
	 */
	public void ordenarDescendente()
	{
		this.ordenar(true);
	}

	/**
	 * Ordenar los elementos en el orden deseado
	 * 
	 * @param inverso
	 *            false para ordenar en orden ascendente. true para ordenar en
	 *            orden descendente
	 */
	private void ordenar(boolean inverso)
	{
		int num = this.getNumeroElementos();
		String[] aux = new String[num];
		// Obtenemos los elementos en la lista
		for (int i = 0; i < num; i++)
			aux[i] = this.getElementoEnIndice(i);
		for (int i = 0; i < num - 1; i++)
			for (int j = i + 1; j < num; j++)
			{
				boolean condicion;
				if (!inverso)
					condicion = aux[i].compareToIgnoreCase(aux[j]) > 0;
				else condicion = aux[i].compareToIgnoreCase(aux[j]) < 0;
				if (condicion)
				{
					// Intercambiamos los elementos
					String aux2 = new String(aux[i]);
					aux[i] = new String(aux[j]);
					aux[j] = new String(aux2);
				}
			}
		// Vaciamos la lista y añadimos los nuevos elementos
		this.vaciar();
		this.aniadir(aux);
	}
}
