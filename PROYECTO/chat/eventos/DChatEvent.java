package chat.eventos;

import Deventos.DEvent;

public class DChatEvent extends DEvent
{
private static final long serialVersionUID = 1L;
	
	public static final Integer NUEVA_CONVERSACION = new Integer(250);
	public static final Integer RESPUESTA_NUEVA_CONVERSACION = new Integer(251);
	public static final Integer NUEVO_MENSAJE = new Integer(242);
	public static final Integer FIN_CONVERSACION = new Integer(253);
	public static final Integer SOLICITAR_VC = new Integer(245);
	public static final Integer RESPUESTA_SOLICITAR_VC = new Integer(255);
	
	public String mensaje = null;
	public String ip_vc = null;
	public String destinatario = null;
	
	public DChatEvent () {
		
	}
}
