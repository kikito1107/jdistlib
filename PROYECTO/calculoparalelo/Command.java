package calculoparalelo;

import net.jini.core.entry.Entry;
import net.jini.space.JavaSpace;

/**
 * Entrada que permite enviar entrada de JS con c—digo ejecutable.
 * @author anab
 */
public interface Command extends Entry {    
    public Entry execute(JavaSpace space);
}
