package componentes.listeners;

/**
 * Interfaz para los listeners de los eventos de combobox para el conjunto de
 * usuarios conectados
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface LJComboBoxListener
{
	/**
	 * Accion realizada al cambiar la seleccion de un elemento
	 * 
	 * @param elemento
	 *            Indice de la lista seleccionado
	 */
	public void elementoSeleccionado(int elemento);
}
