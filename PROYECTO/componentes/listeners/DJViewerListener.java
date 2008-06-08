package componentes.listeners;

import Deventos.DJViewerEvent;

/**
 * Interfaz para los listeners de los visores de imagenes distribuidos
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public interface DJViewerListener
{
	/**
	 * Accion realizada al cargar una imagen
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void cargado(DJViewerEvent evento);
}
