package util;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Clase con la que se simplifica el uso de las tablas JTable añadiendo metodos
 * utiles para su utilizacion.
 * 
 * @author Juan Antonio Ibañez Santorum - ejido2002@hotmail.com
 */

public class TablaElementos extends JTable
{

	private static final long serialVersionUID = 1L;

	public TablaElementos( String[] nombreColumnas, TableModel modelo )
	{
		super(modelo);
	}

	/**
	 * @param nombreColumnas
	 *            Array con el nombre de cada una de las columnas
	 */
	public TablaElementos( String[] nombreColumnas )
	{
		super();
		super.setModel(new ModeloTabla(nombreColumnas));
	}

	/**
	 * Añadir una fila a la tabla
	 * 
	 * @param fila
	 *            Fila que deseamos añadir
	 */
	public void aniadir(String[] fila)
	{
		( (ModeloTabla) this.getModel() ).aniadirFila(fila);
	}

	/**
	 * Añadir varias filas a la tabla
	 * 
	 * @param filas
	 *            Filas que deseamos añadir
	 */
	public void aniadir(String[][] filas)
	{
		ModeloTabla modelo = (ModeloTabla) this.getModel();
		for (int i = 0; i < filas.length; i++)
			modelo.aniadirFila(filas[i]);
	}

	public void eliminarFila(int numFila)
	{
		ModeloTabla modelo = (ModeloTabla) this.getModel();
		modelo.eliminarFila(numFila);
	}

	/**
	 * Eliminar todos los elementos de la tabla
	 */
	public void vaciar()
	{
		( (ModeloTabla) this.getModel() ).vaciar();
	}

	/**
	 * Numero de filas en la tabla
	 * 
	 * @return Numero de filas en la tabla
	 */
	public int numFilas()
	{
		return ( (ModeloTabla) this.getModel() ).getRowCount();
	}

	/**
	 * Obtiene una fila de la tabla
	 * 
	 * @param fila
	 *            Numero de la fila que deseamos obtener
	 * @return Fila que queremos obtener
	 */
	public String[] getFila(int fila)
	{
		return ( (ModeloTabla) this.getModel() ).getFila(fila);
	}

	/**
	 * Obtener la primera fila de la tabla
	 * 
	 * @return Primera fila de la tabla
	 */
	public String[] getPrimeraFila()
	{
		return ( (ModeloTabla) this.getModel() ).getPrimeraFila();
	}

	/**
	 * Obtener la ultima fila de la tabla
	 * 
	 * @return Ultima fila de la tabla
	 */
	public String[] getUltimaFila()
	{
		return ( (ModeloTabla) this.getModel() ).getUltimaFila();
	}

	public void setAnchoColumna(int indexColumna, int tamanio)
	{
		this.getColumnModel().getColumn(indexColumna)
				.setPreferredWidth(tamanio);
	}

	// **************************************************************
	// MODELO USADO PARA LA TABLA
	// **************************************************************

	private class ModeloTabla extends AbstractTableModel
	{
		private static final long serialVersionUID = 1L;

		private String[] nombreColumnas = null;

		private Vector v = null;

		/**
		 * @param nombreColumnas
		 *            Nombre de cada una de las columnas
		 */
		public ModeloTabla( String[] nombreColumnas )
		{
			this.nombreColumnas = nombreColumnas;
			v = new Vector();
		}

		/**
		 * Numero de columnas que tenemos
		 * 
		 * @return Numero de columnas
		 */
		public int getColumnCount()
		{
			return nombreColumnas.length;
		}

		/**
		 * Numero de filas que tenemos
		 * 
		 * @return Numero de filas
		 */
		public int getRowCount()
		{
			return v.size();
		}

		/**
		 * Nombre de la columna deseada
		 * 
		 * @param col
		 *            Columna de la que queremos saber el nombre
		 * @return Nombre de la columna
		 */
		@Override
		public String getColumnName(int col)
		{
			return nombreColumnas[col];
		}

		/**
		 * Valor de la celda en la posicion row,col
		 * 
		 * @param row
		 *            Fila de la celda de la que queremos conocer el valor
		 * @param col
		 *            Columna de la celda de la que queremos conocer el valor
		 * @return Valor de la celda
		 */
		public Object getValueAt(int row, int col)
		{
			String[] aux = (String[]) v.elementAt(row);
			return aux[col];
		}

		/**
		 * Saber si una celda es o no editable
		 * 
		 * @param row
		 *            Fila de la celda para la que queremos conocer si es
		 *            editable
		 * @param col
		 *            Columna de la celda para la que queremos conocer si es
		 *            editable
		 * @return En la actual implementacion ninguna de las columnas es
		 *         editable por lo que devuelve siempre false
		 */
		@Override
		public boolean isCellEditable(int row, int col)
		{
			return false;
		}

		/**
		 * Añadir una fila a la tabla
		 * 
		 * @param fila
		 *            Fila que queremos insertar
		 */
		public void aniadirFila(String[] fila)
		{
			v.add(fila);
			this.fireTableRowsInserted(v.size(), v.size());
		}

		public void eliminarFila(int numFila)
		{
			v.remove(numFila);
			this.fireTableRowsDeleted(numFila, numFila);
		}

		/**
		 * Vaciar toda la tabla
		 */
		public void vaciar()
		{
			int size = v.size();
			if (size > 0)
			{
				v = new Vector();
				this.fireTableRowsDeleted(0, size - 1);
			}
		}

		/**
		 * Obtener una fila
		 * 
		 * @param numFila
		 *            Numero de la fila que queremos obtener
		 * @return Fila deseada
		 */
		public String[] getFila(int numFila)
		{
			return (String[]) v.elementAt(numFila);
		}

		/**
		 * Obtener la primera fila de la tabla
		 * 
		 * @return La primera fila de la tabla
		 */
		public String[] getPrimeraFila()
		{
			return (String[]) v.firstElement();
		}

		/**
		 * Obtener la ultima fila de la tabla
		 * 
		 * @return La ultima fila de la tabla
		 */
		public String[] getUltimaFila()
		{
			return (String[]) v.lastElement();
		}

	}

}
