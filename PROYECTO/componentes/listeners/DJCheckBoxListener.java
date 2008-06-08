package componentes.listeners;

import Deventos.DJCheckBoxEvent;

/**
 * Interfaz para los listeners de los checkbox distribuidos
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface DJCheckBoxListener
{
	/**
	 * Accion cuando se pulsa el checkbox
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void presionado(DJCheckBoxEvent evento);

	/**
	 * Accion cuando se suelta el checkbox
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void soltado(DJCheckBoxEvent evento);
}
