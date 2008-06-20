package fisica.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

/**
 * Interfaz para la implementacion de un protocolo de red
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public interface Network
{
	/**
	 * Conectar a una conexion IP
	 * 
	 * @param dir
	 *            Direccion IP del otro
	 */
	public void conectar(InetAddress dir);

	/**
	 * Desconecta la conexion
	 */
	public void desconectar();

	/**
	 * Obtiene si estamos conectados o no
	 * 
	 * @return True si estamos conectados. False en otro caso
	 */
	public boolean conectado();

	/**
	 * Obtiene la IP del otro Peer
	 * 
	 * @return Direccion IP del otro Peer
	 */
	public InetAddress getIPPeer();

	/**
	 * Asigna si nos ponemos en modo escucha o no
	 * 
	 * @param esc
	 *            Indica si queremos ponernos en modo escucha o no
	 */
	public void escuchar(boolean esc);

	/**
	 * Obtiene si estamos en modo escucha o no
	 * 
	 * @return True si estamos escuchando. False en otro caso
	 */
	public boolean escuchando();

	/**
	 * Crea un buffer de entrada para la red
	 * 
	 * @return Buffer de entrada inicializado
	 * @throws IOException
	 */
	public InputStream crearBufferEntrada() throws IOException;

	/**
	 * Crea un buffer de salida para la red
	 * 
	 * @return Buffer de salida inicializado
	 * @throws IOException
	 */
	public OutputStream crearBufferSalida() throws IOException;
}
