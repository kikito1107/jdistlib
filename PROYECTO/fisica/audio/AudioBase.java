package fisica.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

/**
 * Clase abstracta que permite implementar un sistema de escucha para una linea
 * de entrada
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public abstract class AudioBase implements LineListener
{
	/**
	 * Formato de la linea de audio
	 */
	protected AudioFormat formato_linea;

	/**
	 * Formato de red para la linea de audio
	 */
	protected AudioFormat formato_red;

	/**
	 * Codigo de formato para el audio
	 */
	protected int codigo_formato = -1;

	/**
	 * Milisegundos que tendra el buffer almacenado
	 */
	protected int milisegundos_buffer;

	/**
	 * Tamaño del buffer. Se calculara en base al tiempo del buffer y a los
	 * formatos usados
	 */
	protected int tam_buffer;

	/**
	 * Mezclador de audio
	 */
	protected Mixer mixer;

	/**
	 * Linea de datos para el audio
	 */
	protected DataLine linea;

	/**
	 * Ultimo nivel de sonido capturado
	 */
	protected int anterior_nivel = -1;

	/**
	 * Indica si la captura esta en silencio o no
	 */
	protected boolean en_silencio = false;

	/**
	 * Constructor
	 * 
	 * @param fc
	 *            Codigo de formato
	 * @param mix
	 *            Mezclador usado
	 * @param tbufferMils
	 *            Tamaño del buffer en milisegundos
	 */
	protected AudioBase( int fc, Mixer mix, int tbufferMils )
	{
		this.milisegundos_buffer = tbufferMils;
		this.mixer = mix;
		try
		{
			setFormatCode(fc);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Abre la captura de linea
	 * 
	 * @throws Exception
	 */
	public void open() throws Exception
	{
		closeLine(false);
		destroyLine();
		createLine();
		openLine();
	}

	/**
	 * Implementacion de como crear la linea de audio
	 * 
	 * @throws Exception
	 */
	protected abstract void creacionLinea() throws Exception;

	/**
	 * Crea la linea de audio
	 * 
	 * @throws Exception
	 */
	private void createLine() throws Exception
	{
		try
		{
			linea = null;
			creacionLinea();
			linea.addLineListener(this);
		}
		catch (LineUnavailableException ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Implementacion de como abrir la linea de audio
	 * 
	 * @throws Exception
	 */
	protected abstract void aperturaLinea() throws Exception;

	/**
	 * Apertura de la linea
	 * 
	 * @throws Exception
	 */
	private void openLine() throws Exception
	{
		try
		{
			tam_buffer = (int) Util.millis2bytes(milisegundos_buffer,
					formato_linea);
			tam_buffer -= tam_buffer % formato_linea.getFrameSize();
			aperturaLinea();
			tam_buffer = linea.getBufferSize();
		}
		catch (LineUnavailableException ex)
		{
			throw new Exception(ex);
		}
	}

	/**
	 * Comienza la captura de la linea
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception
	{
		if (linea == null) throw new Exception("No se pudo iniciar la linea");
		linea.flush();
		linea.start();
	}

	/**
	 * Cierra la captura de la linea
	 */
	public void close()
	{
		close(false);
	}

	/**
	 * Cierra la captura de la linea
	 * 
	 * @param abrir_despues
	 *            Indica si se abrira de nuevo otra vez o no
	 */
	public void close(boolean abrir_despues)
	{
		closeLine(abrir_despues);
		destroyLine();
	}

	/**
	 * Cierra el stream de la linea
	 * 
	 * @param abrir_despues
	 *            Indica si se abrira de nuevo otra vez o no
	 */
	protected void closeLine(boolean abrir_despues)
	{
		if (!abrir_despues) anterior_nivel = -1;
		if (linea != null)
		{
			linea.flush();
			linea.stop();
			linea.close();
		}
	}

	/**
	 * Destruye la linea de audio
	 */
	private void destroyLine()
	{
		if (linea != null) linea.removeLineListener(this);
		linea = null;
	}

	/**
	 * Comprueba si esta iniciada la captura o no
	 * 
	 * @return True si la linea esta inicializada y activada. False en caso
	 *         contrario
	 */
	public boolean isStarted()
	{
		return ( linea != null ) && ( linea.isActive() );
	}

	/**
	 * Comprueba si la linea esta abierta o no
	 * 
	 * @return True si la linea esta inicializada y abierta. False en caso
	 *         contrario
	 */
	public boolean isOpen()
	{
		return ( linea != null ) && ( linea.isOpen() );
	}

	/**
	 * Obtiene el tamaño del buffer
	 * 
	 * @return Tamaño del buffer
	 */
	public int getBufferSize()
	{
		return tam_buffer;
	}

	/**
	 * Obtiene el tamaño del buffer en tiempo (milisegundos)
	 * 
	 * @return Tamaño del buffer en milisegundos
	 */
	public int getBufferSizeMillis()
	{
		return milisegundos_buffer;
	}

	/**
	 * Asigna el tamaño del buffer en tiempo (milisegundos)
	 * 
	 * @param bufferSizeMillis
	 *            Tamaño del buffer en milisegundos
	 * @throws Exception
	 */
	public void setBufferSizeMillis(int bufferSizeMillis) throws Exception
	{
		if (this.milisegundos_buffer == bufferSizeMillis) return;
		boolean estaba_abierto = isOpen();
		boolean estaba_iniciado = isStarted();
		closeLine(true);

		this.milisegundos_buffer = bufferSizeMillis;

		if (estaba_abierto)
		{
			openLine();
			if (estaba_iniciado) start();
		}
	}

	/**
	 * Obtiene el codigo de formato del audio
	 * 
	 * @return Codigo de formato del audio
	 */
	public int getFormatCode()
	{
		return codigo_formato;
	}

	/**
	 * Asigna el codigo de formato para el audio
	 * 
	 * @param formatCode
	 *            Codigo de formato
	 * @throws Exception
	 */
	public void setFormatCode(int formatCode) throws Exception
	{
		if (this.codigo_formato == formatCode) return;
		if (isOpen())
			throw new Exception(
					"No se puede cambiar el formato mientras esta abierto el canal");
		this.formato_linea = Util.getLineAudioFormat(formatCode);
		this.formato_red = Util.getNetAudioFormat(formatCode);
	}

	/**
	 * Asigna el mezclador para la captura
	 * 
	 * @param mixer
	 *            Mezclador usado
	 * @throws Exception
	 */
	public void setMixer(Mixer mixer) throws Exception
	{
		if (this.mixer == mixer) return;
		boolean estaba_abierto = isOpen();
		boolean estaba_iniciado = isStarted();
		close(true);

		this.mixer = mixer;

		if (estaba_abierto)
		{
			createLine();
			openLine();
			if (estaba_iniciado) start();
		}
	}

	/**
	 * Asigna si debemos silenciar la captura
	 * 
	 * @param silenciar
	 *            Indica si debemos silenciar o no la captura
	 */
	public void setSilenciado(boolean silenciar)
	{
		this.en_silencio = silenciar;
	}

	/**
	 * Comprueba si la linea esta silenciada o no
	 * 
	 * @return True si la linea esta en silencio. False en otro caso
	 */
	public boolean estaSilenciado()
	{
		return this.en_silencio;
	}

	/**
	 * Obtiene el ultimo nivel de audio capturado
	 * 
	 * @return Ultimo nivel de audio capturado
	 */
	public int getLevel()
	{
		return anterior_nivel;
	}

	/**
	 * Calcula el nivel de audio actual
	 * 
	 * @param b
	 *            Vector de bytes capturados
	 * @param off
	 *            Offset en el vector
	 * @param len
	 *            Longitud a tomar del vector
	 */
	protected void calcCurrVol(byte[] b, int off, int len)
	{
		int end = off + len;
		int sampleSize = ( formato_linea.getSampleSizeInBits() + 7 ) / 8;
		int max = 0;
		if (sampleSize == 1)
		{
			for (; off < end; off++)
			{
				int sample = (byte) ( b[off] + 128 );
				if (sample < 0) sample = -sample;
				if (sample > max) max = sample;
			}
			anterior_nivel = max;
		}
		else if (sampleSize == 2)
		{
			if (formato_linea.isBigEndian()) // 16-bit big endian
				for (; off < end; off += 2)
				{
					int sample = (short) ( ( b[off] << 8 ) | ( b[off + 1] & 0xFF ) );
					if (sample < 0) sample = -sample;
					if (sample > max) max = sample;
				}
			else // 16-bit little endian
			for (; off < end; off += 2)
			{
				int sample = (short) ( ( b[off + 1] << 8 ) | ( b[off] & 0xFF ) );
				if (sample < 0) sample = -sample;
				if (sample > max) max = sample;
			}
			anterior_nivel = max >> 8;
		}
		else anterior_nivel = -1;
	}

	/**
	 * Pone en silencio todo el audio de un buffer
	 * 
	 * @param b
	 *            Vector de bytes del buffer
	 * @param off
	 *            Offset en el vector
	 * @param len
	 *            Longitud a tomar del vector
	 */
	protected void silenciarBuffer(byte[] b, int off, int len)
	{
		int end = off + len;
		int sampleSize = ( formato_linea.getSampleSizeInBits() + 7 ) / 8;
		byte filler = 0;
		if (sampleSize == 1) filler = -128;
		for (; off < end; off++)
			b[off] = filler;
	}
}
