package componentes.listeners;

import Deventos.DJTreeEvent;

/**
 * Interfaz para los listeners de los arboles distribuidos
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface DJTreeListener
{
	/**
	 * Accion realizada al abrir o cerrar una rama del arbol
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void apertura_cierre(DJTreeEvent evento);

	/**
	 * Accion realizada al cambiar la seleccion de nodo del arbol
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	public void seleccion(DJTreeEvent evento);
}
