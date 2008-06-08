package componentes.listeners;

import Deventos.DJToggleButtonEvent;

/**
 * Interfaz para los listeners de los togglebutton distribuidos
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface DJToggleButtonListener
{
	/**
	 * Accion cuando se pulsa el boton
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void presionado(DJToggleButtonEvent evento);

	/**
	 * Accion cuando se suelta el boton
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void soltado(DJToggleButtonEvent evento);
}
