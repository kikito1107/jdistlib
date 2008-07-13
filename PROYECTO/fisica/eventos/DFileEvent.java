package fisica.eventos;

import fisica.documentos.MIDocumento;
import Deventos.DEvent;

/**
 * Eventos enviados desde y hacia el servidor de ficheros
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez.
 */
public class DFileEvent extends DEvent
{
	private static final long serialVersionUID = -1925222008496901194L;

	/*
	 * Conjunto de constantes que indican los tipos de eventos soportados.
	 */
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

	public static final Integer EXISTE_FICHERO = new Integer(186);

	public static final Integer RESPUESTA_EXISTE_FICHERO = new Integer(187);

	/**
	 * Path del fichero
	 */
	public String path = null;

	/**
	 * Metainformacion de un documento
	 */
	public MIDocumento fichero = null;

	/**
	 * Directorio padre de un documento
	 */
	public MIDocumento padre = null;

	/**
	 * Resultado del acceso a un fichero
	 */
	public Boolean res = null;

	/**
	 * Constructor por defecto
	 */
	public DFileEvent()
	{

	}
}
