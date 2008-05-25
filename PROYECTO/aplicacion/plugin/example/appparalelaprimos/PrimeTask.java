/**
 * 
 */
package aplicacion.plugin.example.appparalelaprimos;

import java.util.Vector;

import calculoparalelo.TaskEntry;

import net.jini.core.entry.Entry;
import net.jini.space.JavaSpace;

/**
 * @author anab
 *
 */
public class PrimeTask extends TaskEntry
{

	public Integer min = null;
	public Integer MAX = null;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3356655897333712808L;

	/**
	 * 
	 */
	public PrimeTask()
	{
		// TODO Auto-generated constructor stub
	}
	
	public PrimeTask(int minimo, int MAXIMO){
		min = new Integer(minimo);
		MAX = new Integer(MAXIMO);
		
	}
	
	public Entry execute(JavaSpace space)
	{
		int minimo = min.intValue();
		int MAXIMO = MAX.intValue();
		int[] primos = new int[MAX-min+1];
		
		System.err.println("Recibido el intervalo ["+ minimo +", " + MAXIMO + "]");
		
		for (int i=0; i<primos.length; ++i)
			primos[i] = -1;
		
		int contador = 0;
		
		for ( int i=minimo; i<=MAXIMO; ++i )
			if (esPrimo(i)) {
				primos[contador] = i;
				contador++;
			}
		
		return new PrimeResult(primos, contador);
	}

	private boolean esPrimo(int c)
	{
		if (c == 1)
			return false;
		if (c == 2 || c == 1)
			return true;
		
		for (int i=2; i<Math.sqrt(c)+1; ++i){
			if (c % i == 0)
				return false;
		}
		
		return true;
	}

}
