package aplicacion.fisica.eventos;

import javax.swing.tree.DefaultMutableTreeNode;

import Deventos.DEvent;

/**
 * Obtener nodos del FS a partir de la BD
 * @author anab, carlos
 */
public class DNodeEvent extends DEvent
{
	private static final long serialVersionUID = 1L;
	
	public static final Integer OBTENER_NODO  = new Integer(1);
	public static final Integer SINCRONIZACION = new Integer(100);
	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(200);
	
	public DefaultMutableTreeNode raiz = null;
	public String direccionRMI = null;
	
	public DNodeEvent()
	{
	  
	}
}
