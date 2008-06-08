package componentes.listeners;

import Deventos.DMenuSelectionManagerEvent;

/**
 * Interfaz para los listeners de los manager de seleccion de menu distribuidos
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface DMenuSelectionManagerListener
{
	/**
	 * Accion realizada al cambiar path de seleccion de un menu
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void cambioPath(DMenuSelectionManagerEvent evento);
}
