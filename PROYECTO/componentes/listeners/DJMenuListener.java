package componentes.listeners;

import Deventos.DJMenuEvent;

/**
 * Interfaz para los listeners de los menus distribuidos
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface DJMenuListener
{
	/**
	 * Accion realizada al cambiar el estado del menu
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void cambioEstado(DJMenuEvent evento);
}
