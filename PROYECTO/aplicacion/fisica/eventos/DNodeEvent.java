package aplicacion.fisica.eventos;

import javax.swing.tree.DefaultMutableTreeNode;

import Deventos.DEvent;


/**
 * Eventos para obtener nodos del sistem de ficheros a partir de la BD
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez.
 */
public class DNodeEvent extends DEvent
{
	private static final long serialVersionUID = 1L;

	/*
	 * Conjunto de constantes que indican los tipos de eventos soportados. 
	 */
	public static final Integer OBTENER_NODO = new Integer(1);

	public static final Integer SINCRONIZACION = new Integer(100);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(200);

	/**
	 * Nodo raiz de los documentos
	 */
	public DefaultMutableTreeNode raiz = null;

	/**
	 * Direccion ip para las comunicaciones mediante RMI.
	 */
	public String direccionRMI = null;

	/**
	 * Constructor por defecto
	 */
	public DNodeEvent()
	{

	}
}
