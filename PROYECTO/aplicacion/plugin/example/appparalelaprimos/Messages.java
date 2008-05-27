package aplicacion.plugin.example.appparalelaprimos;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages
{
	private static final String SPANISH = "aplicacion.plugin.example.appparalelaprimos.messageses"; //$NON-NLS-1$
	private static final String ENGLISH = "aplicacion.plugin.example.appparalelaprimos.messagesen";
	
	public static final String ES = "es";
	public static final String EN = "en";

	private static final ResourceBundle RESOURCE_SPANISH = ResourceBundle
			.getBundle(SPANISH);
	
	private static final ResourceBundle RESOURCE_ENGLISH = ResourceBundle.getBundle(ENGLISH);

	private static ResourceBundle actual =  RESOURCE_SPANISH;
	
	private Messages()
	{
		
	}
	
	public static void setLan(String lan){
		if (lan.equals(ES) )
			actual = RESOURCE_SPANISH;
		else if (lan.equals(EN)){
			actual = RESOURCE_ENGLISH;
		}
	}

	
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
