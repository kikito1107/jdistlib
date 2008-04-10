package Deventos;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DJTextFieldEvent
	 extends DEvent {
  public static final Integer SINCRONIZACION = new Integer(0);
  public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);
  public static final Integer REPLACE = new Integer(2);
  public static final Integer REMOVE = new Integer(3);
  public static final Integer INSERT = new Integer(4);

  public Integer p1 = null;
  public Integer p2 = null;
  public String contenido = null;
}
