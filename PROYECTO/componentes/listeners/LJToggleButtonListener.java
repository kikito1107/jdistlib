package componentes.listeners;

/**
 * Interfaz para los listeners de los eventos de toggle button para el conjunto
 * de usuarios conectados
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface LJToggleButtonListener
{
	/**
	 * Accion realizada al cambiar el estado del boton
	 * 
	 * @param seleccionado
	 *            Indica si el boton esta seleccionado o no
	 */
	public void cambioEstado(boolean seleccionado);
}
