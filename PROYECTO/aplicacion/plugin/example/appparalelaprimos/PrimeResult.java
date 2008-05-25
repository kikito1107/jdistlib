package aplicacion.plugin.example.appparalelaprimos;

import net.jini.core.entry.Entry;

public class PrimeResult implements Entry
{
	
	public int primos[] = null;
	
	public PrimeResult(){
		
	}
	
	public PrimeResult(int[] pr, int numPrimos) {
		this.primos = pr;

	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 4058510368249155569L;

}
