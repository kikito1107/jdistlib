package componentes.listeners;

import Deventos.DJMenuItemEvent;

/**
 * Interfaz para los listeners de los items de menu distribuidos
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface DJMenuItemListener
{
	/**
	 * Accion ejecutada al cambiar el estado del item de menu
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void cambioEstado(DJMenuItemEvent evento);
}
