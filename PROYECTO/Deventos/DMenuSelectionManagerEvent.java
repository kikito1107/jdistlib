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

public class DMenuSelectionManagerEvent extends DEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4448368233043907008L;

	public static final Integer SINCRONIZACION = new Integer(0);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);

	public static final Integer CAMBIO_PATH = new Integer(2);

	@SuppressWarnings("unchecked")
	public Vector path = null;

}
