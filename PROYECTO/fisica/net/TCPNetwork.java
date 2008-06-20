package fisica.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Implementacion del protocolo TCP
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class TCPNetwork extends BaseNetwork
{
	private static final boolean TCP_SIN_DELAY = false;

	// Si ponemos -1 indica no usar buffer
	private static final int TAM_BUFFER_RECEPCION = 1024;

	private static final int TAM_BUFFER_ENVIO = 1024;

	private ServerSocket socket_servidor;

	private Socket socket_comunicacion;

	/**
	 * Constructor
	 * @param conf Configuracion de la conexion
	 */
	public TCPNetwork( ConfiguracionConexion conf )
	{
		super(conf);
	}

	public void conectar(InetAddress dir)
	{
		try
		{
			socket_comunicacion = new Socket(dir, getPuerto());
			setOpcionesSockets(socket_comunicacion);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public InetAddress getIPPeer()
	{
		return socket_comunicacion.getInetAddress();
	}

	public void desconectar()
	{
		try
		{
			socket_comunicacion.shutdownInput();
			socket_comunicacion.shutdownOutput();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		try
		{
			socket_comunicacion.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public boolean conectado()
	{
		return ( socket_comunicacion != null ) && !socket_comunicacion.isClosed();
	}

	public void escuchar(boolean esc)
	{
		if (esc != esta_escuchando()) if (esc)
			try
			{
				socket_servidor = new ServerSocket(getPuerto());
				socket_servidor.setSoTimeout(2000);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		else
		{
			try
			{
				socket_servidor.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			socket_servidor = null;
		}
	}

	private boolean esta_escuchando()
	{
		return socket_servidor != null;
	}

	public boolean escuchando()
	{
		Socket s = null;
		try
		{
			s = socket_servidor.accept();
			setOpcionesSockets(s);
		}
		catch (Exception e)
		{
		}

		if (s != null)
		{
			socket_comunicacion = s;
			return true;
		}
		else return false;
	}

	public InputStream crearBufferEntrada() throws IOException
	{
		return socket_comunicacion.getInputStream();
	}

	public OutputStream crearBufferSalida() throws IOException
	{
		return socket_comunicacion.getOutputStream();
	}

	private static void setOpcionesSockets(Socket socket)
	{
		try
		{
			socket.setTcpNoDelay(TCP_SIN_DELAY);
			if (TAM_BUFFER_ENVIO > 0)
				socket.setSendBufferSize(TAM_BUFFER_ENVIO);
			if (TAM_BUFFER_RECEPCION > 0)
				socket.setReceiveBufferSize(TAM_BUFFER_RECEPCION);
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
	}
}
