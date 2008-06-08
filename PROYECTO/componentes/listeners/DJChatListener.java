package componentes.listeners;

import Deventos.DJChatEvent;

/**
 * Interfaz para los listeners del chat
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface DJChatListener
{
	/**
	 * Accion al recibir un nuevo mensaje
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void nuevoMensaje(DJChatEvent evento);
}
