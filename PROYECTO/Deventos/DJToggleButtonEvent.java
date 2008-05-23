package Deventos;

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

public class DJToggleButtonEvent extends DEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8168417816387222116L;

	public static final Integer SINCRONIZACION = new Integer(0);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);

	public static final Integer PRESIONADO = new Integer(2);

	public static final Integer SOLTADO = new Integer(3);

	public Boolean presionado = null;

	public Boolean marcado = null;

	public DJToggleButtonEvent()
	{
	}

}
