package fisica.audio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

/**
 * Clase que implementa la captura de audio desde una linea
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class CapturaAudio extends AudioBase
{
	/**
	 * Flujo de entrada para el audio
	 */
	protected AudioInputStream ais;

	/**
	 * Flujo de salida para el audio
	 */
	protected OutputStream outputStream;

	/**
	 * Hebra que captura el audio de la linea
	 */
	private CaptureThread thread;

	/**
	 * Constructor
	 * @param formatCode Codigo de formato para el audio
	 * @param mixer Mezclador de audio usado
	 * @param bufferSizeMillis Tamaño del buffer en milisegundos
	 */
	public CapturaAudio( int formatCode, Mixer mixer, int bufferSizeMillis )
	{
		super(formatCode, mixer, bufferSizeMillis);
	}

	@Override
	protected void creacionLinea() throws Exception
	{
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, formato_linea);

		if (mixer != null)
			linea = (TargetDataLine) mixer.getLine(info);
		else linea = AudioSystem.getTargetDataLine(formato_linea);
	}

	@Override
	protected void aperturaLinea() throws Exception
	{
		TargetDataLine tdl = (TargetDataLine) linea;
		tdl.open(formato_linea, tam_buffer);
		ais = new MedidorLinea(tdl);
		ais = AudioSystem.getAudioInputStream(formato_red, ais);
	}

	@Override
	public synchronized void start() throws Exception
	{
		boolean iniciar_hebra = false;
		if (( thread != null )
				&& ( thread.isTerminating() || ( outputStream == null ) ))
		{
			thread.terminate();
			iniciar_hebra = true;
		}
		if (( ( thread == null ) || iniciar_hebra )
				&& ( outputStream != null ))
		{
			thread = new CaptureThread();
			thread.start();
		}
		super.start();
	}

	@Override
	protected void closeLine(boolean willReopen)
	{
		CaptureThread hebra_aux = null;
		synchronized (this)
		{
			if (!willReopen && ( thread != null )) thread.terminate();
			super.closeLine(willReopen);
			if (!willReopen)
			{
				if (ais != null) try
				{
					ais.close();
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
				if (thread != null)
				{
					if (outputStream != null)
					{
						try
						{
							outputStream.close();
						}
						catch (IOException ioe)
						{
						}
						outputStream = null;
					}
					hebra_aux = thread;
				}
			}
		}
		if (hebra_aux != null) hebra_aux.esperar();
	}

	/**
	 * Obtiene el buffer de entrada para el audio
	 * @return Buffer de entrada para el audio
	 */
	public AudioInputStream getAudioInputStream()
	{
		return ais;
	}

	/**
	 * Asigna el flujo de salida para el audio
	 * @param stream Flujo de salida para el audio
	 */
	public synchronized void setOutputStream(OutputStream stream)
	{
		this.outputStream = stream;
		if (( this.outputStream == null ) && ( thread != null ))
		{
			thread.terminate();
			thread = null;
		}
	}

	/**
	 * Obtiene el flujo de salida para el audio
	 * @return Flujo de salida para el audio
	 */
	public synchronized OutputStream getOutputStream()
	{
		return this.outputStream;
	}

	/**
	 * Hebra para la captura del audio
	 */
	private class CaptureThread extends Thread
	{
		private boolean fin = false;

		private boolean finalizado = false;

		/**
		 * Ejecucion de la hebra
		 */
		@Override
		public void run()
		{
			byte[] buffer = new byte[getBufferSize()];
			try
			{
				AudioInputStream ais_aux = ais;
				while (!fin)
					if (ais_aux != null)
					{
						int r = ais_aux.read(buffer, 0, buffer.length);
						if (r > 0)
						{
							synchronized (CapturaAudio.this)
							{
								if (outputStream != null)
									outputStream.write(buffer, 0, r);
							}
							if (outputStream == null) synchronized (this)
							{
								this.wait(100);
							}
						}
						else if (r == 0) synchronized (this)
						{
							this.wait(20);
						}
					}
					else synchronized (this)
					{
						this.wait(50);
					}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			finalizado = true;
		}

		/**
		 * Finalizar la hebra
		 */
		public synchronized void terminate()
		{
			fin = true;
			this.notifyAll();
		}

		/**
		 * Comprobar si se esta finalizando la ejecucion
		 * de la hebra
		 * @return True si se esta finalizando. False en otro caso
		 */
		public synchronized boolean isTerminating()
		{
			return fin || finalizado;
		}

		/**
		 * Esperar la finalizacion de la hebra
		 */
		public synchronized void esperar()
		{
			if (!finalizado) 
			try
			{
				this.join();
			}
			catch (InterruptedException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Clase que lee de la linea de entrada y calcula el nivel
	 * de audio para cada elemento leido
	 */
	private class MedidorLinea extends AudioInputStream
	{
		/**
		 * Linea desde la que capturar
		 */
		private TargetDataLine line;

		/**
		 * Constructor
		 * @param l Linea de captura
		 */
		public MedidorLinea( TargetDataLine l )
		{
			super(new ByteArrayInputStream(new byte[0]), l.getFormat(),
					AudioSystem.NOT_SPECIFIED);
			this.line = l;
		}

		@Override
		public int available() throws IOException
		{
			return line.available();
		}

		@Override
		public int read() throws IOException
		{
			throw new IOException("illegal call to TargetDataLineMeter.read()!");
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException
		{
			try
			{
				int ret = line.read(b, off, len);

				if (estaSilenciado()) silenciarBuffer(b, off, ret);
				// run some simple analysis
				if (ret > 0) calcCurrVol(b, off, ret);
				return ret;
			}
			catch (IllegalArgumentException e)
			{
				throw new IOException(e.getMessage());
			}
		}

		@Override
		public void close() throws IOException
		{
			if (line.isActive())
			{
				line.flush();
				line.stop();
			}
			line.close();
		}

		@Override
		public int read(byte[] b) throws IOException
		{
			return read(b, 0, b.length);
		}

		@Override
		public long skip(long n) throws IOException
		{
			return 0;
		}

		@Override
		public void mark(int readlimit)
		{
		}

		@Override
		public void reset() throws IOException
		{
		}

		@Override
		public boolean markSupported()
		{
			return false;
		}

	}

	public void update(LineEvent event)
	{

	}
}
