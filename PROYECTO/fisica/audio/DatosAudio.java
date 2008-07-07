package fisica.audio;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import fisica.net.ConfiguracionConexion;
import fisica.net.Network;
import fisica.net.TCPNetwork;

/**
 * Datos de configuracion para el audio
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class DatosAudio
{
	private DatosGenerales datos;

	private PropertyChangeSupport soporte_cambio_propiedades;

	private Network red;

	private ListenThread hebra_escucha;

	private DataInputStream dis;

	private OutputStream os;

	private AudioBase[] audio = new AudioBase[2];

	private boolean activo;

	public DatosAudio( DatosGenerales datos_generales )
	{
		datos = datos_generales;
		soporte_cambio_propiedades = new PropertyChangeSupport(this);
		inicializarRed();
		audio[ConstantesAudio.DIR_MIC] = new CapturaAudio(getAudioSettings()
				.getCodigoFormato(), getAudioSettings()
				.getMezcladorSeleccionado(ConstantesAudio.DIR_MIC),
				getAudioSettings().getTamBufferMilisegundos(
						ConstantesAudio.DIR_MIC));
		audio[ConstantesAudio.DIR_SPK] = new ReproductorAudio(
				getAudioSettings().getCodigoFormato(), getAudioSettings()
						.getMezcladorSeleccionado(ConstantesAudio.DIR_SPK),
				getAudioSettings().getTamBufferMilisegundos(
						ConstantesAudio.DIR_SPK));
		activo = false;
	}

	private DatosGenerales getModeloGeneral()
	{
		return datos;
	}

	private ConfiguracionConexion getConfiguracionConexion()
	{
		return getModeloGeneral().getConnectionSettings();
	}

	private ConfiguracionAudio getAudioSettings()
	{
		return getModeloGeneral().getAudioSettings();
	}

	private void inicializarRed()
	{
		if (getConfiguracionConexion().getTipoConexion() == fisica.net.ConfiguracionConexion.TIPO_CONEXION_TCP)
			red = new TCPNetwork(getModeloGeneral().getConnectionSettings());
	}

	private Network obtenerRed()
	{
		return red;
	}

	public AudioBase getAudio(int d)
	{
		return audio[d];
	}

	public void conectar(String ip_conexion)
	{
		InetAddress addr = null;
		try
		{
			addr = InetAddress.getByName(ip_conexion);
		}
		catch (UnknownHostException e)
		{
			JOptionPane.showMessageDialog(null, "Host desconocido "
					+ ip_conexion);
		}
		if (addr != null) obtenerRed().conectar(addr);
		if (!obtenerRed().conectado())
			JOptionPane.showMessageDialog(null,
					"No se pudo establecer la conexion con " + ip_conexion,
					"Error", JOptionPane.ERROR_MESSAGE);
		else inicializarConexion(true);
	}

	public void desconectar()
	{
		cerrarAudio();
		if (estaConectado())
		{
			obtenerRed().desconectar();
			notifyConnection();
		}
	}

	public void setEscuchar(boolean escuchar)
	{
		if (escuchar != estaEscuchando()) if (escuchar)
		{
			hebra_escucha = new ListenThread();
			hebra_escucha.start();
		}
		else hebra_escucha.setTerminar();
	}

	public boolean estaEscuchando()
	{
		return hebra_escucha != null;
	}

	/**
	 * Inicializa los flujos de datos y establece la conexion (negocia el
	 * formato de audio)
	 * 
	 * @param es_activo
	 *            True si es un extremo activo. False si es un extremo pasivo
	 */
	private void inicializarConexion(boolean es_activo)
	{
		try
		{
			dis = new DataInputStream(obtenerRed().crearBufferEntrada());
			os = obtenerRed().crearBufferSalida();
		}
		catch (IOException e)
		{
			error("Hubo problemas al establecer la conexion");
		}

		boolean correcto = false;
		if (es_activo)
			correcto = handShakeActivo();

		else correcto = handShakePasivo();

		if (correcto)
		{
			if (estaConectado()) inicializarRedAudio();
			notifyConnection();
		}
		else
		{
			dis = null;
			os = null;
		}
	}

	private boolean handShakeActivo()
	{
		DataOutputStream dos = new DataOutputStream(getStreamEnvio());

		try
		{
			dos.writeInt(ConstantesAudio.PROTOCOL_MAGIC);
			dos.writeInt(ConstantesAudio.PROTOCOL_VERSION);
			dos.writeInt(getAudioSettings().getCodigoFormato());
		}
		catch (IOException e)
		{
			error("Error en el protocolo handshake (active, fase I)");
			return false;
		}

		byte[] buffer = new byte[4];
		try
		{
			getStreamRecepcion().readFully(buffer);
		}
		catch (IOException e)
		{
			error("Error en el protocolo handshake (active, fase II)");
			return false;
		}

		int w = ( ( buffer[0] & 0xFF ) << 24 ) | ( ( buffer[1] & 0xFF ) << 16 )
				| ( ( buffer[2] & 0xFF ) << 8 ) | ( buffer[3] & 0xFF );

		if (w != ConstantesAudio.PROTOCOL_ACK)
		{
			error("Error en el extremo contrario");
			return false;
		}
		return true;
	}

	private boolean handShakePasivo()
	{
		boolean correcto = true;

		byte[] buffer = new byte[12];
		try
		{
			int nRead = getStreamRecepcion().read(buffer);
			if (nRead != 12)
			{
				error("Error en el protocolo handshake (pasivo, fase I)");
				correcto = false;
			}
		}
		catch (IOException e)
		{
			error("Error en el protocolo handshake (passive, fase I)");
			correcto = false;
		}
		if (correcto)
		{
			int w = ( buffer[0] << 24 ) | ( buffer[1] << 16 )
					| ( buffer[2] << 8 ) | buffer[3];

			if (w != ConstantesAudio.PROTOCOL_MAGIC)
			{
				error("Magic incorrecto");
				correcto = false;
			}
			else
			{
				w = ( buffer[4] << 24 ) | ( buffer[5] << 16 )
						| ( buffer[6] << 8 ) | buffer[7];

				if (w != ConstantesAudio.PROTOCOL_VERSION)
				{
					error("Version del protocolo incorrecto");
					correcto = false;
				}
				else
				{
					w = ( buffer[8] << 24 ) | ( buffer[9] << 16 )
							| ( buffer[10] << 8 ) | buffer[11];

					if (( w < 0 )
							|| ( w > ConstantesAudio.CODIGOS_FORMATO.length ))
					{
						error("Codigo de formato incorrecto");
						correcto = false;
					}
				}
			}
		}

		DataOutputStream dos = new DataOutputStream(getStreamEnvio());
		try
		{
			if (correcto)
				dos.writeInt(ConstantesAudio.PROTOCOL_ACK);
			else dos.writeInt(ConstantesAudio.PROTOCOL_ERROR);
		}
		catch (IOException e)
		{
			error("Error en el protocolo handshake (pasivo, fase II)");
			correcto = false;
		}
		return correcto;
	}

	public boolean estaConectado()
	{
		return obtenerRed().conectado();
	}

	private void inicializarRedAudio()
	{
		try
		{
			( (CapturaAudio) getAudio(ConstantesAudio.DIR_MIC) )
					.setOutputStream(getStreamEnvio());
			( (ReproductorAudio) getAudio(ConstantesAudio.DIR_SPK) )
					.setAudioInputStream(Util.createNetAudioInputStream(
							getAudioSettings().getCodigoFormato(),
							getStreamRecepcion()));
			iniciarAudio(ConstantesAudio.DIR_MIC);
			iniciarAudio(ConstantesAudio.DIR_SPK);
			activarAudio(true);
		}
		catch (Exception e)
		{
			error(e.getMessage());
		}
	}

	public void inicializarAudioStream()
	{
		if (esTestMicrofono())
			( (ReproductorAudio) getAudio(ConstantesAudio.DIR_SPK) )
					.setAudioInputStream(( (CapturaAudio) getAudio(ConstantesAudio.DIR_MIC) )
							.getAudioInputStream());
	}

	private void cerrarAudio()
	{
		activarAudio(false);
		cerrarAudio(ConstantesAudio.DIR_SPK);
		cerrarAudio(ConstantesAudio.DIR_MIC);
	}

	public boolean esTestMicrofono()
	{
		return audioActivado()
				&& ( ( (CapturaAudio) getAudio(ConstantesAudio.DIR_MIC) )
						.getOutputStream() == null );
	}

	public boolean audioActivado()
	{
		return activo;
	}

	public void activarAudio(boolean active)
	{
		activo = active;
		notifyAudio();
	}

	private void cerrarAudio(int d)
	{
		if (getAudio(d) != null) getAudio(d).close();
	}

	private void iniciarAudio(int d) throws Exception
	{
		if (!audioActivado())
			getAudio(d).setFormatCode(getAudioSettings().getCodigoFormato());
		getAudio(d).open();
		getAudio(d).start();
	}

	public void intercambiarTestAudio()
	{
		if (estaConectado()) return;
		try
		{
			if (activo)
			{
				cerrarAudio(ConstantesAudio.DIR_MIC);
				cerrarAudio(ConstantesAudio.DIR_SPK);
				activarAudio(false);
			}
			else
			{
				iniciarAudio(ConstantesAudio.DIR_MIC);
				( (CapturaAudio) getAudio(ConstantesAudio.DIR_MIC) )
						.setOutputStream(null);
				iniciarAudio(ConstantesAudio.DIR_SPK);
				activarAudio(true);
				inicializarAudioStream();
			}
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, new Object[]
			{ "Error: ", ex.getMessage() }, "Error", JOptionPane.ERROR_MESSAGE);
			cerrarAudio(0);
			cerrarAudio(1);
			activarAudio(false);
			notifyAudio();
		}
	}

	public DataInputStream getStreamRecepcion()
	{
		return dis;
	}

	public OutputStream getStreamEnvio()
	{
		return os;
	}

	private void error(String strError)
	{
		JOptionPane.showMessageDialog(null, new Object[]
		{ strError, "Se finalizara la conexion de audio" }, "Error",
				JOptionPane.ERROR_MESSAGE);
		obtenerRed().desconectar();
		cerrarAudio();
		notifyConnection();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		soporte_cambio_propiedades.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		soporte_cambio_propiedades.removePropertyChangeListener(listener);
	}

	private void notifyConnection()
	{
		soporte_cambio_propiedades.firePropertyChange(
				ConstantesAudio.CONNECTION_PROPERTY, estaConectado(),
				!estaConectado());
	}

	private void notifyAudio()
	{
		soporte_cambio_propiedades.firePropertyChange(
				ConstantesAudio.AUDIO_PROPERTY, audioActivado(),
				!audioActivado());
	}

	/**
	 * Hebra de escucha
	 */
	private class ListenThread extends Thread
	{
		private boolean terminar = false;

		public void setTerminar()
		{
			terminar = true;
		}

		@Override
		public void run()
		{
			obtenerRed().escuchar(true);

			while (!terminar)
				if (obtenerRed().escuchando())
				{
					System.out.println(obtenerRed().getIPPeer());
					// aqui se puede poner un mensaje de que hemos
					// recibido una peticion para que al aceptarlo
					// empiece la comunicacion. Si se rechaza
					// habra que poner obtenerRed().desconectar()
					try
					{
						sleep(5000); // dar tiempo para el establecimiento
						// del handshake
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}

					inicializarConexion(false);
					setEscuchar(false);
				}

			obtenerRed().escuchar(false);
		}
	}
}
