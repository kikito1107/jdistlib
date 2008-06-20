package fisica.audio;

/**
 * Diversas constantes de audio para las clases
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class ConstantesAudio
{
	public static final int DIR_MIC = 0;

	public static final int DIR_SPK = 1;

	public static final int CODIGO_FORMATO_CD = 1;

	public static final int CODIGO_FORMATO_RADIO = 2;

	public static final int CODIGO_FORMATO_TELEFONO = 3;

	public static final int CODIGO_FORMATO_GSM = 4;

	public static final String[] NOMBRES_FORMATOS =
	{ "13.2KBit/s - Modem", "64KBit/s - RDSI", "352.8KBit/s - ADSL",
			"705.6KBit/s - LAN" };

	public static final int[] CODIGOS_FORMATO =
	{ CODIGO_FORMATO_GSM, CODIGO_FORMATO_TELEFONO, CODIGO_FORMATO_RADIO, CODIGO_FORMATO_CD };

	public static final String[] BUFFER_SIZE_MILLIS_STR =
	{ "30", "40", "50", "70", "85", "100", "130", "150", "180", "220", "400" };

	public static final String CONNECTION_PROPERTY = "CONNECTION";

	public static final String AUDIO_PROPERTY = "AUDIO";

	public static final int PROTOCOL_MAGIC = 0x43484154;

	public static final int PROTOCOL_VERSION = 1;

	public static final int PROTOCOL_ACK = 1001;

	public static final int PROTOCOL_ERROR = 1002;

	public static final int[] TIPOS_CONEXION =
	{ fisica.net.ConfiguracionConexion.TIPO_CONEXION_TCP,
			fisica.net.ConfiguracionConexion.TIPO_CONEXION_UDP };

	public static final String[] NOMBRES_TIPOS_CONEXION =
	{ "TCP (para LAN)", "UDP (para WAN)" };
}
