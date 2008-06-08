package componentes.listeners;

import Deventos.DJLienzoEvent;

/**
 * Interfaz para los listeners de los lienzos distribuidos
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public interface DJLienzoListener
{
	/**
	 * Accion al cargar un elemento en el lienzo
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void cargado(DJLienzoEvent evento);
}
