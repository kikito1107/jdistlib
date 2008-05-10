package Deventos;

import java.util.Vector;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class DJChatEvent extends DEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7521236626655504658L;

	public static final Integer SINCRONIZACION = new Integer(0);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);

	public static final Integer MENSAJE = new Integer(2);

	public static final Integer MENSAJE_PRIVADO = new Integer(3);
	
	public static final Integer FIN_CONVERSACION_PRIVADA = new Integer(4);
	
	public static final Integer INICIAR_VC = new Integer(5);
	
	public static final Integer RESPUESTA_INICIAR_VC = new Integer(6);

	public String mensaje = null;

	public Vector<String> receptores = null;
	
	public String ipVC = null;
	
	public DJChatEvent(){
		super();
		receptores = new Vector<String>();
	}
}
