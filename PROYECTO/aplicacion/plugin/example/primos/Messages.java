package aplicacion.plugin.example.primos;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Externalizacion de las cadenas de texto del plugin de
 * calculo de primos para permitir la internalizacion.
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class Messages
{
	private static final String SPANISH = "aplicacion.plugin.example.primos.messageses";
	private static final String ENGLISH = "aplicacion.plugin.example.primos.messagesen";
	
	/**
	 * Indica que el idioma es el castellano
	 */
	public static final String ES = "es";
	
	/**
	 * Indica que el idioma es el ingles
	 */
	public static final String EN = "en";

	private static final ResourceBundle RESOURCE_SPANISH = ResourceBundle
			.getBundle(SPANISH);
	
	private static final ResourceBundle RESOURCE_ENGLISH = ResourceBundle.getBundle(ENGLISH);

	private static ResourceBundle actual =  RESOURCE_SPANISH;

	/**
	 * Constructor
	 */
	private Messages()
	{
		
	}
	
	/**
	 * Permite asignar un idioma
	 * @param lan Cadena con el idioma al asignar
	 */
	public static void setLan(String lan){
		if (lan.equals(ES) )
			actual = RESOURCE_SPANISH;
		else if (lan.equals(EN)){
			actual = RESOURCE_ENGLISH;
		}
	}

	/**
	 * Obtiene la cadena asociada a una clave de cadena
	 * @param key Clave para la obtencion de una cadena
	 * @return Devuelve la cadena en el idioma especificado
	 */
	public static String getString(String key)
	{
		try
		{
			return actual.getString(key);	
		}
		catch (MissingResourceException e)
		{
			return '!' + key + '!';
		}
	}
}
