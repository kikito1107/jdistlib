package plugins.examples.primes;

import plugins.calculoparalelo.eventos.ResultEntry;

/**
 * Entrada para el canal de comunicaciones
 * que permite enviar un resultado
 * @author carlosrodriguezdominguez
 *
 */
public class PrimeResult extends ResultEntry
{
	private static final long serialVersionUID = 4058510368249155569L;
	
	/**
	 * Primos encontrados
	 */
	public int primos[] = null;
	
	/**
	 * Constructor por defecto
	 */
	public PrimeResult(){
		
	}
	
	/**
	 * Constructor con parametros
	 * @param pr Primos encontrados
	 * @param mensaje Mensaje a enviar
	 */
	public PrimeResult(int[] pr, String mensaje) {
		this.primos = pr;
		this.mensaje = mensaje;
	}
}
