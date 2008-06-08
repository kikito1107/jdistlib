package componentes.listeners;

/**
 * Interfaz para los listeners de los eventos de checkbox para el conjunto de
 * usuarios conectados
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface LJCheckBoxListener
{
	/**
	 * Accion realizada al cambiar el estado del checkbox
	 * 
	 * @param seleccionado
	 *            Indica si se ha selecciona o no el ckeckbox
	 */
	public void cambioEstado(boolean seleccionado);
}
