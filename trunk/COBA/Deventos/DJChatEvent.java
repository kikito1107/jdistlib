package Deventos;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DJChatEvent
	 extends DEvent {
  public static final Integer SINCRONIZACION = new Integer(0);
  public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);
  public static final Integer MENSAJE = new Integer(2);

  public String mensaje = null;

}
