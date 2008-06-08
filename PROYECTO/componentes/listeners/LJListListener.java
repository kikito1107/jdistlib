package componentes.listeners;

/**
 * Interfaz para los listeners de los eventos de lista para el conjunto de
 * usuarios conectados
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface LJListListener
{
	/**
	 * Accion realizada al cambiar la posicion de la lista
	 * 
	 * @param nuevaPosicion
	 *            Indice seleccionado de la lista
	 */
	public void cambioPosicion(int nuevaPosicion);
}
