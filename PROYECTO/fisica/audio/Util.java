package fisica.audio;

import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Clase con diversos metodos utiles para la implementacion
 */
public class Util
{
	private static final float[] netSampleRate =
	{ 1.0f, // ninguno
			44100.0f, // CD
			22050.0f, // FM
			8000.0f, // Telefono
			8000.0f // GSM
	};

	public static long bytes2millis(long bytes, AudioFormat format)
	{
		return (long) ( bytes / format.getFrameRate() * 1000 / format
				.getFrameSize() );
	}

	public static long millis2bytes(long ms, AudioFormat format)
	{
		return (long) ( ms * format.getFrameRate() / 1000 * format
				.getFrameSize() );
	}

	public static AudioFormat getLineAudioFormat(int formatCode)
	{
		return getLineAudioFormat(netSampleRate[formatCode]);
	}

	public static AudioFormat getLineAudioFormat(float sampleRate)
	{
		return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, // sampleRate
				16, // sampleSizeInBits
				1, // channels
				2, // frameSize
				sampleRate, // frameRate
				false); // bigEndian
	}

	public static AudioFormat getNetAudioFormat(int formatCode)
			throws UnsupportedAudioFileException
	{
		if (formatCode == ConstantesAudio.CODIGO_FORMATO_CD)
			return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0f, // sampleRate
					16, // sampleSizeInBits
					1, // channels
					2, // frameSize
					44100.0f, // frameRate
					true); // bigEndian
		else if (formatCode == ConstantesAudio.CODIGO_FORMATO_RADIO)
			return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 22050.0f, // sampleRate
					16, // sampleSizeInBits
					1, // channels
					2, // frameSize
					22050.0f, // frameRate
					true); // bigEndian
		else if (formatCode == ConstantesAudio.CODIGO_FORMATO_TELEFONO)
			return new AudioFormat(AudioFormat.Encoding.ULAW, 8000.0f, // sampleRate
					8, // sampleSizeInBits
					1, // channels
					1, // frameSize
					8000.0f, // frameRate
					false); // bigEndian
		else if (formatCode == ConstantesAudio.CODIGO_FORMATO_GSM)
			return new AudioFormat(new AudioFormat.Encoding("GSM0610"),
					8000.0F, // sampleRate
					-1, // sampleSizeInBits
					1, // channels
					33, // frameSize
					50.0F, // frameRate
					false); // bigEndian
		throw new RuntimeException("Wrong format code!");
	}

	public static AudioInputStream createNetAudioInputStream(int formatCode,
			InputStream stream)
	{
		try
		{
			AudioFormat format = getNetAudioFormat(formatCode);
			return new AudioInputStream(stream, format,
					AudioSystem.NOT_SPECIFIED);
		}
		catch (UnsupportedAudioFileException e)
		{
			return null;
		}
	}

	public static int getFormatCode(AudioFormat format)
	{
		AudioFormat.Encoding encoding = format.getEncoding();
		// very simple check...
		if (encoding.equals(AudioFormat.Encoding.PCM_SIGNED))
			if (format.getSampleRate() == 44100.0f)
				return ConstantesAudio.CODIGO_FORMATO_CD;
			else return ConstantesAudio.CODIGO_FORMATO_RADIO;
		if (encoding.equals(AudioFormat.Encoding.ULAW))
			return ConstantesAudio.CODIGO_FORMATO_TELEFONO;
		if (encoding.toString().equals("GSM0610"))
			return ConstantesAudio.CODIGO_FORMATO_GSM;
		throw new RuntimeException("Wrong Format");
	}

}
