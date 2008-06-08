package componentes.listeners;

import Deventos.DJTextFieldEvent;

/**
 * Interfaz para los listeners de los campos de texto distribuidos
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface DJTextFieldListener
{
	/**
	 * Accion realizada al reemplazar un texto en el campo de texto
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void replace(DJTextFieldEvent evento);

	/**
	 * Accion realizada al eliminar un texto en el campo de texto
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void remove(DJTextFieldEvent evento);

	/**
	 * Accion realizada al insertar texto en el campo de texto
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void insert(DJTextFieldEvent evento);
}
