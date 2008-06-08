package Deventos;

import java.util.Vector;

/**
 * Cola FIFO de eventos. Permite un esquema productor-consumidor de forma
 * sencilla. Cuando un usuario intenta extraer un evento y no hay ninguno
 * quedara bloqueado hasta que se reciba alguno.
 * 
 * @author Juan Antonio Ibañez Santorum
 */
@SuppressWarnings( "unchecked" )
public class ColaEventos
{
	private Vector v = null;

	/**
	 * Constructor
	 */
	public ColaEventos()
	{
		v = new Vector();
	}

	/**
	 * Introduce un nuevo evento en la cola
	 * 
	 * @param e
	 *            Evento a introducir
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
	 * @return Evento extraido
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
	 * Devuelve el numero de eventos disponibles en la cola para ser extraidos
	 * 
	 * @return Numero de eventos en la cola
	 */
	public synchronized int tamanio()
	{
		return v.size();
	}

	/**
	 * Metodo para consultar si la cola esta o no vacia.
	 * 
	 * @return True si esta vacia. False en otro caso.
	 */
	public synchronized boolean vacia()
	{
		return ( v.size() == 0 );
	}

}
