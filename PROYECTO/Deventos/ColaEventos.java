package Deventos;

import java.util.Vector;

/**
 * Cola FIFO de eventos. Permite un esquema productor-consumidor de forma
 * sencilla. Cuando un usuario intenta extraer un evento y no hay ninguno
 * quedara bloqueado hasta que se reciba alguno.
 */
@SuppressWarnings( "unchecked" )
public class ColaEventos
{
	private Vector v = null;

	public ColaEventos()
	{
		v = new Vector();
	}

	/**
	 * Introduce un nuevo evento en la cola
	 * 
	 * @param e
	 *            DEvent Evento a introducir
	 */

	public synchronized void nuevoEvento(DEvent e)
	{
		v.add(e);
		notifyAll();
	}

	/**
	 * Extrae un evento de la cola. Si la cola esta vacia el que llama al metodo
	 * quedara bloqueado hasta que haya eventos en la cola que extraer.
	 * 
	 * @return DEvent Evento extraido
	 */
	public synchronized DEvent extraerEvento()
	{
		DEvent evento = null;

		while (evento == null)
			if (v.size() > 0)
			{
				evento = (DEvent) v.elementAt(0);
				v.remove(0);
			}
			else try
			{
				wait();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		return evento;
	}

	/**
	 * Devuelve el nñmero de eventos disponibles en la cola para ser extraidos
	 * 
	 * @return int
	 */
	public synchronized int tamanio()
	{
		return v.size();
	}

	/**
	 * Mñtodo para consultar si la cola esta o no vacia.
	 * 
	 * @return boolean TRUE si esta vacia y FALSE en otro caso.
	 */
	public synchronized boolean vacia()
	{
		return ( v.size() == 0 );
	}

}
