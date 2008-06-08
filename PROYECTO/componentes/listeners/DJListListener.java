package componentes.listeners;

import Deventos.DJListEvent;

/**
 * Interfaz para los listeners de las listas distribuidas
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface DJListListener
{
	/**
	 * Accion al cambiar la posicion seleccionada en la lista
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void cambioPosicion(DJListEvent evento);
}
