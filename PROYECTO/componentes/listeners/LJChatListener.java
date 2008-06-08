package componentes.listeners;

/**
 * Interfaz para los listeners de los eventos de chat para el conjunto de
 * usuarios conectados
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface LJChatListener
{
	/**
	 * Accion realizada al recibir un nuevo mensaje
	 * 
	 * @param usuario
	 *            Usuario al que va dirigido el mensaje
	 * @param mensaje
	 *            Mensaje recibido
	 */
	public void nuevoMensaje(String usuario, String mensaje);
}
