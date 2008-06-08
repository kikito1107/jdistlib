package componentes.listeners;

import Deventos.DJComboBoxEvent;

/**
 * Interfaz para los listeners de los combobox distribuidos
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface DJComboBoxListener
{
	/**
	 * Accion cuando se abre el combobox
	 */
	public void abierto();

	/**
	 * Accion cuando se cierra el combobox
	 */
	public void cerrado();

	/**
	 * Accion cuando se cambia la seleccion de un elemento de la lista
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void cambioSeleccionLista(DJComboBoxEvent evento);

	/**
	 * Accion cuando se realiza una seleccion en la lista
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void seleccion(DJComboBoxEvent evento);
}
