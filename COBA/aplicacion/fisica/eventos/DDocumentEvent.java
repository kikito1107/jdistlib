package aplicacion.fisica.eventos;

import Deventos.DEvent;
import aplicacion.fisica.*;
import aplicacion.fisica.documentos.Documento;
import aplicacion.fisica.documentos.FicheroBD;

/**
 * Obtiene un fichero 
 * @author anab, carlos
 */
public class DDocumentEvent extends DEvent
{

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
