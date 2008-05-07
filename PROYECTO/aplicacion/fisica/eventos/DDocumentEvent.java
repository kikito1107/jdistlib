package aplicacion.fisica.eventos;

import Deventos.DEvent;

/**
 * Obtiene un fichero 
 * @author anab, carlos
 */
public class DDocumentEvent extends DEvent
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -39377765259477678L;
	
	
	public static final Integer OBTENER_FICHERO = new Integer(333);
	public static final Integer	RESPUESTA_FICHERO = new Integer(334);
	public static final Integer SINCRONIZACION = new Integer(336);
	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(335);
	
	public String path = null;
	public String direccionRespuesta = null;
	
	public DDocumentEvent()
	{
		
	}
}
