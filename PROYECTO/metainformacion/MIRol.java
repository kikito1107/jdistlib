package metainformacion;

import java.io.Serializable;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

@SuppressWarnings("serial")
public class MIRol
	 extends MIUsuario
	 implements Serializable {

  public MIRol(String nombreRol) {
	 super(nombreRol, new String(""));
  }

  public String getNombreRol() {
	 return super.getNombreUsuario();
  }

  public String toString() {
	 String cadena = new String("[ ++++++++++ ROL +++++++++]\n");
	 cadena = new String(cadena + super.toString());
	 cadena = new String(cadena + "[ ++++++++++++++++++++++++]\n");
	 return cadena;
  }

}
