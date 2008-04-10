package Deventos;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DMouseEvent
	 extends DEvent {
  public static final Integer CAMBIO_POSICION = new Integer(0);
  public static final Integer PETICION_INFORMACION = new Integer(1);
  public static final Integer OCULTACION = new Integer(2);

  public Integer px = null;
  public Integer py = null;
  public String rol = null;

}
