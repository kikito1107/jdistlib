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

public class DJMenuItemEvent extends DEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1733797648708141088L;

	public static final Integer SINCRONIZACION = new Integer(0);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);

	public static final Integer CAMBIO_ESTADO = new Integer(2);

	@SuppressWarnings( "unchecked" )
	public Vector path = null;

}
