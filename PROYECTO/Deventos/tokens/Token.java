package Deventos.tokens;

import net.jini.core.entry.Entry;

/**
 * Entrada del JavaSpace asociada a un token usado para el algoritmo de
 * escritura ordenada de eventos.
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class Token implements Entry
{
	private static final long serialVersionUID = 3164856190277343160L;

	/**
	 * Numero de secuencia
	 */
	public Long sec = null;

	/**
	 * Aplicacion asociada a la entrada
	 */
	public String aplicacion = null;

	/**
	 * Constructor por defecto
	 */
	public Token()
	{
	}

	/**
	 * Contructor con parametros
	 * 
	 * @param numeroSecuencia
	 *            Numero de secuencia del evento
	 */
	public Token( long numeroSecuencia )
	{
		this.sec = new Long(numeroSecuencia);
	}

}
