package aplicacion.fisica.eventos;

import Deventos.DEvent;
import aplicacion.fisica.documentos.FicheroBD;

/**
 * Modificar la BD
 * 
 * @author anab, carlos
 */
public class DFileEvent extends DEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1925222008496901194L;

	public static final Integer NOTIFICAR_INSERTAR_FICHERO = new Integer(177);

	public static final Integer NOTIFICAR_MODIFICACION_FICHERO = new Integer(
			178);

	public static final Integer NOTIFICAR_ELIMINAR_FICHERO = new Integer(179);

	public static final Integer RESPUESTA_INSERTAR_FICHERO = new Integer(180);

	public static final Integer RESPUESTA_MODIFICACION_FICHERO = new Integer(
			181);

	public static final Integer RESPUESTA_ELIMINAR_FICHERO = new Integer(183);

	public static final Integer SINCRONIZACION = new Integer(184);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(185);

	public FicheroBD fichero = null;

	public FicheroBD padre = null;

	public DFileEvent()
	{

	}
}
