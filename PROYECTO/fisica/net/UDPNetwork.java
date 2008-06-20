package fisica.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

/**
 * Implementacion del protocolo UDP
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class UDPNetwork extends BaseNetwork
{
	/**
	 * Constructor
	 * 
	 * @param conf
	 *            Configuracion de la conexion
	 */
	public UDPNetwork( ConfiguracionConexion conf )
	{
		super(conf);
	}

	public void conectar(InetAddress dir)
	{

	}

	public void desconectar()
	{

	}

	public boolean conectado()
	{
		return false;
	}

	public InetAddress getIPPeer()
	{
		return null;
	}

	public void escuchar(boolean esc)
	{
	}

	public boolean escuchando()
	{
		return conectado();
	}

	public InputStream crearBufferEntrada() throws IOException
	{
		return null;
	}

	public OutputStream crearBufferSalida() throws IOException
	{
		return null;
	}
}
