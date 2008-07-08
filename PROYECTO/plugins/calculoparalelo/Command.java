package plugins.calculoparalelo;

import net.jini.core.entry.Entry;
import net.jini.space.JavaSpace;

/**
 * Entrada que permite enviar entrada de JS con codigo ejecutable.
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public interface Command extends Entry
{
	/**
	 * Metodo que realiza la ejecucion requerida
	 * 
	 * @param space
	 *            JavaSpace en el cual escribiremos el comando
	 * @return Entrada en el JavaSpace
	 */
	public Entry execute(JavaSpace space);
}
