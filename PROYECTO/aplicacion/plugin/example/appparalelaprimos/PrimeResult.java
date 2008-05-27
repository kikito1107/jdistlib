package aplicacion.plugin.example.appparalelaprimos;

import calculoparalelo.eventos.ResultEntry;

public class PrimeResult extends ResultEntry
{
	
	public int primos[] = null;
	
	public PrimeResult(){
		
	}
	
	public PrimeResult(int[] pr, int numPrimos, String mensaje) {
		this.primos = pr;
		this.mensaje = mensaje;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 4058510368249155569L;

}
