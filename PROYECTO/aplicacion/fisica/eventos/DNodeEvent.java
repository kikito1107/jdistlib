package aplicacion.fisica.eventos;

import Deventos.DEvent;
import aplicacion.fisica.documentos.FicheroBD;
import javax.swing.tree.DefaultMutableTreeNode;

import aplicacion.fisica.*;

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
