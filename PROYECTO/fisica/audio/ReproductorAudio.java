package fisica.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

/**
 * Clase encargada de reproducir el audio de una captura
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class ReproductorAudio extends AudioBase
{
	protected AudioInputStream ais;

	private PlayThread hebra_play;

	/**
	 * Constructor
	 * @param fc Codigo de formato
	 * @param mixer Mezclador de audio
	 * @param tam_buffer Tamaño del buffer en milisegundos
	 */
	public ReproductorAudio( int fc, Mixer mixer, int tam_buffer )
	{
		super(fc, mixer, tam_buffer);
	}

	@Override
	protected void creacionLinea() throws Exception
	{
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, formato_linea);

		// get the playback data line for capture.
		if (mixer != null)
			linea = (SourceDataLine) mixer.getLine(info);
		else linea = AudioSystem.getSourceDataLine(formato_linea);
	}

	@Override
	protected void aperturaLinea() throws Exception
	{
		SourceDataLine sdl = (SourceDataLine) linea;
		sdl.open(formato_linea, tam_buffer);
	}

	@Override
	public synchronized void start() throws Exception
	{
		boolean needStartThread = false;
		if (( hebra_play != null ) && hebra_play.estaTerminando())
		{
			hebra_play.terminar();
			needStartThread = true;
		}
		if (( hebra_play == null ) || needStartThread)
		{
			// start thread
			hebra_play = new PlayThread();
			hebra_play.start();
		}
		super.start();
	}

	@Override
	protected void closeLine(boolean willReopen)
	{
		PlayThread oldThread = null;
		synchronized (this)
		{
			if (!willReopen && ( hebra_play != null )) hebra_play.terminar();
			super.closeLine(willReopen);
			if (!willReopen && ( hebra_play != null ))
			{
				oldThread = hebra_play;
				hebra_play = null;
			}
		}
		if (oldThread != null) oldThread.esperar();
	}

	/**
	 * Asigna el flujo de entrada de audio
	 * @param ais Flujo de entrada de audio
	 */
	public void setAudioInputStream(AudioInputStream ais)
	{
		this.ais = AudioSystem.getAudioInputStream(formato_linea, ais);
	}

	/**
	 * Hebra de reproduccion
	 */
	private class PlayThread extends Thread
	{
		private boolean fin = false;

		private boolean terminado = false;

		@Override
		public void run()
		{
			byte[] buffer = new byte[getBufferSize()];
			try
			{
				while (!fin)
				{
					SourceDataLine sdl = (SourceDataLine) linea;
					if (ais != null)
					{
						int r = ais.read(buffer, 0, buffer.length);
						if (r > 0)
						{
							if (estaSilenciado()) silenciarBuffer(buffer, 0, r);
							calcCurrVol(buffer, 0, r);
							if (sdl != null) sdl.write(buffer, 0, r);
						}
						else if (r == 0) synchronized (this)
						{
							this.wait(40);
						}
					}
					else synchronized (this)
					{
						this.wait(50);
					}
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}

			terminado = true;
		}

		public synchronized void terminar()
		{
			fin = true;
			this.notifyAll();
		}

		public synchronized boolean estaTerminando()
		{
			return fin || terminado;
		}

		public synchronized void esperar()
		{
			if (!terminado) try
			{
				this.join();
			}
			catch (InterruptedException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public void update(LineEvent event)
	{

	}
}
