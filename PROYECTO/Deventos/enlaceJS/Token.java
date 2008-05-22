package Deventos.enlaceJS;

import net.jini.core.entry.Entry;

/**
 * Token usado para el algoritmo de escritura ordenada de eventos.
 */

public class Token
	 implements Entry {
  public Long sec = null; // Numero de secuencia
  public String aplicacion = null;

  public Token() {
  }

  public Token(long numeroSecuencia) {
	 this.sec = new Long(numeroSecuencia);
  }

}
