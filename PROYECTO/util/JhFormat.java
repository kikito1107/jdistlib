package util;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Clase utilizada para realizar varias operaciones de formateo de texto, y
 * números.<br>
 * 
 * @author: Quicky
 * @version: 1.0
 */
public class JhFormat
{
	/**
	 * Campos para indicar si queremos trabajar con un número o con un
	 * porcentaje
	 */
	public static final int NUMBER = 0;

	public static final int PERCENT = 1;

	/**
	 * Método que cambia la coma del texto introducido por punto:
	 * 
	 * @param java.lang.String
	 *            texto: El texto a ser formateado
	 * @return java.lang.String Texto ya formateado
	 */

	public static String CambiarComa(String texto)
	{
		int Long = ( texto.length() );

		String Entera;
		String Decimal = "";

		int IndiceComa = texto.indexOf(",");

		if (IndiceComa == ( -1 ))
			Entera = texto;
		else
		{
			Entera = texto.substring(0, IndiceComa);
			Decimal = texto.substring(IndiceComa + 1, Long);
		}

		texto = Entera + "." + Decimal;

		return texto;
	}

	/**
	 * Método que cambia el punto del texto introducido por coma:
	 * 
	 * @param java.lang.String
	 *            texto: El texto a ser formateado
	 * @return java.lang.String Texto ya formateado
	 */

	public static String CambiarPunto(String texto)
	{
		int Long = ( texto.length() );

		String Entera;
		String Decimal = "";

		int IndiceComa = texto.indexOf(".");

		if (IndiceComa == ( -1 ))
			Entera = texto;
		else
		{
			Entera = texto.substring(0, IndiceComa);
			Decimal = texto.substring(IndiceComa + 1, Long);
		}

		if (IndiceComa == -1)
			texto = Entera + ",00";
		else if (( texto.length() - IndiceComa ) == 2)
			texto = Entera + "," + Decimal + "0";
		else texto = Entera + "," + Decimal;

		return texto;
	}

	/**
	 * Método que devuelve la cadena rellena con espacios por la derecha, hasta
	 * ocupar "lon" posiciones.
	 * <ul>
	 * <li>Utiliza el espacio como símbolo de relleno</li>
	 * 
	 * </ul>
	 * 
	 * @param java.lang.Object
	 *            cadena: Cadena a ser formateada
	 * @param int
	 *            longitud: Indica la longitud de la cadena resultante
	 * @return java.lang.String Cadena rellena
	 */

	public static String fill(Object cadena, int longitud)
	{
		return fill(cadena.toString(), ' ', longitud, 1);
	}

	/**
	 * Método que devuelve la cadena rellena con el caracter "car", hasta ocupar
	 * "lon" posiciones, desde el lado indicado.
	 * <ul>
	 * <li>Utiliza el caracter como símbolo de relleno</li>
	 * <li>Valores posibles de lado
	 * <ul>
	 * <li>0: izquierda</li>
	 * <li>1: derecha</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * 
	 * @param java.lang.String
	 *            cadena: Cadena a ser formateada
	 * @param char
	 *            caracterRelleno: Caracter con el que se va a rellenar
	 * @param int
	 *            longitud: Indica la longitud de la cadena resultante
	 * @param int
	 *            lado: nos indica en que lado situar los caracteres de Relleno
	 * @return java.lang.String Cadena rellena
	 */
	public static String fill(String cadena, char caracterRelleno,
			int longitud, int lado)
	{
		String salida = cadena;

		// Si tiene la misma longitud la devuelve
		if (salida.length() == longitud) return salida;

		// Si es más larga la trunca
		if (salida.length() > longitud) return salida.substring(0, longitud);

		// Si es menor, entonces modificamos
		if (salida.length() < longitud)
		{
			if (lado == 1)
				// Rellenar por la derecha
				for (int k = cadena.length(); k < longitud; k++)
					salida = salida + caracterRelleno;
			else if (lado == 0) // Rellenar por la izquierda
				for (int k = cadena.length(); k < longitud; k++)
					salida = caracterRelleno + salida;
			else return "Lado pasado a la función fill es incorrecto";

			return salida;
		}

		return cadena;
	}

	/**
	 * Método que devuelve un número convertido en texto bien formateado:
	 * <ul>
	 * <li>Utiliza la coma como separador decimal</li>
	 * <li>Utiliza el punto como separador de miles</li>
	 * <li>Si tiene más decimales que los indicados realiza un redondeo hacia
	 * arriba</li>
	 * </ul>
	 * 
	 * @param java.math.BigDecimal
	 *            cantidad: El número a ser formateado.
	 * @param int
	 *            nDecimales: El número de decimales a mostrar.
	 * @return java.lang.String Número ya formateado
	 */
	private static String formatearNumero(BigDecimal importe, int nDecimales)
	{
		// Preparo un objeto para realizar el formateo
		NumberFormat formateo = NumberFormat.getNumberInstance();
		formateo.setMaximumFractionDigits(nDecimales);
		formateo.setMinimumFractionDigits(nDecimales);
		return formateo.format(importe);
	}

	/**
	 * <p>
	 * Método que devuelve un número convertido en texto bien formateado como
	 * porcentaje. El número entrante debe ser del tipo 0.53 para convertirlo en
	 * 53%
	 * </p>
	 * 
	 * Puntos a tener en cuenta:
	 * <ul>
	 * <li>Utiliza la coma como separador decimal</li>
	 * <li>Utiliza el punto como separador de miles</li>
	 * <li>Si tiene más decimales que los indicados realiza un redondeo hacia
	 * arriba</li>
	 * <li>Añade el símbolo de porcentaje al final</li>
	 * </ul>
	 * 
	 * @param java.math.BigDecimal
	 *            cantidad: El número a ser formateado.
	 * @param int
	 *            nDecimales: El número de decimales a mostrar.
	 * @return java.lang.String Número ya formateado
	 */
	private static String formatearPorcentaje(BigDecimal importe,
			int nDecimales, boolean conSimbolo)
	{
		// double imp = importe.doubleValue();

		// Preparo un objeto para realizar el formateo
		NumberFormat formateo = NumberFormat.getPercentInstance();
		formateo.setMaximumFractionDigits(nDecimales);
		formateo.setMinimumFractionDigits(nDecimales);
		String salida = formateo.format(importe);
		if (!conSimbolo) salida = salida.substring(0, salida.length() - 1);

		return salida;
	}

	/**
	 * Método que devuelve un número convertido en texto bien formateado:
	 * <ul>
	 * <li>Utiliza la coma como separador decimal</li>
	 * <li>Utiliza el punto como separador de miles</li>
	 * <li>Si tiene más decimales que los indicados realiza un redondeo hacia
	 * arriba</li>
	 * </ul>
	 * Realiza una llamada al otro método indicando que no queremos el símbolo
	 * del porcentaje al final
	 * 
	 * @param java.math.BigDecimal
	 *            cantidad: El número a ser formateado.
	 * @param int
	 *            nDecimales: El número de decimales a mostrar.
	 * @param int
	 *            TipoCampo: indicamos si vamos a formatear un Numero o un
	 *            Porcentaje
	 * @return java.lang.String Número ya formateado
	 */
	public static String getFormatedNumber(BigDecimal cantidad, int nDecimales,
			int TipoCampo)
	{
		return getFormatedNumber(cantidad, nDecimales, TipoCampo, false);
	}

	/**
	 * Método que devuelve un número convertido en texto bien formateado:
	 * <ul>
	 * <li>Utiliza la coma como separador decimal</li>
	 * <li>Utiliza el punto como separador de miles</li>
	 * <li>Si tiene más decimales que los indicados realiza un redondeo hacia
	 * arriba</li>
	 * </ul>
	 * 
	 * @param java.math.BigDecimal
	 *            cantidad: El número a ser formateado.
	 * @param int
	 *            nDecimales: El número de decimales a mostrar.
	 * @param int
	 *            TipoCampo: indicamos si vamos a formatear un Numero o un
	 *            Porcentaje
	 * @return java.lang.String Número ya formateado
	 */
	public static String getFormatedNumber(BigDecimal cantidad, int nDecimales,
			int TipoCampo, boolean simboloPorcentaje)
	{
		if (TipoCampo == JhFormat.NUMBER)
			// Es un número
			return formatearNumero(cantidad, nDecimales);
		else if (TipoCampo == JhFormat.PERCENT) // Es un porcentaje
			return formatearPorcentaje(cantidad, nDecimales, simboloPorcentaje);
		else // Han pasado un valor no válido como tipo de datos
		return "Tercer parámetro pasado a getFormatedNumber érroneo";
	}

	/**
	 * Método que devuelve un número convertido en texto bien formateado:
	 * <ul>
	 * <li>Utiliza la coma como separador decimal</li>
	 * <li>Utiliza el punto como separador de miles</li>
	 * <li>Si tiene más decimales que los indicados realiza un redondeo hacia
	 * arriba</li>
	 * </ul>
	 * 
	 * <pre>
	 * Convierte la cadena pasada en un BigDecimal y llama al método &quot;getFormatedNumber&quot;
	 * </pre>
	 * 
	 * @param java.lang.String
	 *            numero400: El número a ser formateado (sin símbolo decimal)
	 * @param int
	 *            nDecimalesEntrada: El número de decimales que tiene el número
	 *            de Entrada.
	 * @param int
	 *            nDecimalesSalida: El número de decimales que tendrá el
	 *            resultado
	 * @param int
	 *            TipoCampo: indicamos si vamos a formatear un Numero o un
	 *            Porcentaje
	 * @return java.lang.String Número ya formateado
	 */
	public static String getFormatedNumberForAS400(String numero400,
			int nDecimalesEntrada, int nDecimalesSalida, int TipoCampo)
	{
		return getFormatedNumberForAS400(numero400, nDecimalesEntrada,
				nDecimalesSalida, TipoCampo, false);
	}

	/**
	 * Método que devuelve un número convertido en texto bien formateado:
	 * <ul>
	 * <li>Utiliza la coma como separador decimal</li>
	 * <li>Utiliza el punto como separador de miles</li>
	 * <li>Si tiene más decimales que los indicados realiza un redondeo hacia
	 * arriba</li>
	 * </ul>
	 * 
	 * <pre>
	 * Convierte la cadena pasada en un BigDecimal y llama al método &quot;getFormatedNumber&quot;
	 * </pre>
	 * 
	 * @param java.lang.String
	 *            numero400: El número a ser formateado (sin símbolo decimal)
	 * @param int
	 *            nDecimalesEntrada: El número de decimales que tiene el número
	 *            de Entrada.
	 * @param int
	 *            nDecimalesSalida: El número de decimales que tendrá el
	 *            resultado
	 * @param int
	 *            TipoCampo: indicamos si vamos a formatear un Numero o un
	 *            Porcentaje
	 * @param boolean
	 *            simboloPorcentaje: indica si debe mostrar o no el símbolo del
	 *            Porcentaje
	 * @return java.lang.String Número ya formateado
	 */
	public static String getFormatedNumberForAS400(String numero400,
			int nDecimalesEntrada, int nDecimalesSalida, int TipoCampo,
			boolean simboloPorcentaje)
	{
		// Convierto el String en un BigDecimal
		BigDecimal importeEntrada = new BigDecimal(numero400)
				.setScale(nDecimalesEntrada);

		// Divido el importe entre 10 elevado a nDecimalesEntrada
		double divisor = Math.pow(10, Double.parseDouble(String
				.valueOf(nDecimalesEntrada)));
		importeEntrada = importeEntrada.divide(new BigDecimal(divisor),
				BigDecimal.ROUND_HALF_UP);

		// Llamo a la función principal
		return getFormatedNumber(importeEntrada, nDecimalesSalida, TipoCampo,
				simboloPorcentaje);
	}

	/**
	 * Método que elimina el símbolo indicado de una cadena.
	 * 
	 * @param java.lang.String
	 *            cadena: Cadena de donde eliminaremos el símbolo
	 * @param char
	 *            simbolo: simbolo a eliminar
	 * @return java.lang.String Cadena modificada
	 */

	public static String quitarSimbolo(String cadena, char simbolo)
	{
		char[] txt = cadena.toCharArray();
		String Resultado = "";

		for (int i = 0; i < cadena.length(); i++)
			Resultado += simbolo != txt[i] ? String.valueOf(txt[i]) : "";

		return Resultado;
	}
}
