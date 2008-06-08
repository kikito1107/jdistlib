package componentes.listeners;

import Deventos.DJButtonEvent;

/**
 * Interfaz para los listeners de los botones distribuidos
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface DJButtonListener
{
	/**
	 * Accion cuando se pulsa el boton
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void presionado(DJButtonEvent evento);

	/**
	 * Accion cuando se suelta el boton
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void soltado(DJButtonEvent evento);
}
