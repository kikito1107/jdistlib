package awareness.gui;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * Componente que implementa un campo de texto que acepta solo numeros
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class CampoTextoNumerico extends JTextField
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor por defecto
	 */
	public CampoTextoNumerico()
	{
		super();
		// this.addFocusListener(new ListenerFocus(this));
	}

	/**
	 * Constructor con parametros
	 * 
	 * @param cols
	 *            Indica el numero de columnas del campo de texto
	 */
	public CampoTextoNumerico( int cols )
	{
		super(cols);
	}

	@Override
	protected Document createDefaultModel()
	{
		return new DocumentoSoloNumeros();
	}

	/**
	 * Clase encargada de aceptar entradas en el campo de texto que sean numeros
	 * y rechazar el resto
	 */
	private static class DocumentoSoloNumeros extends PlainDocument
	{
		private static final long serialVersionUID = 3L;

		@Override
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException
		{
			// boolean novalida = false;
			if (str == null) return;
			char[] numeros = str.toCharArray();
			for (int i = numeros.length - 1; i >= 0; i--)
				if (( numeros[i] < '0' ) || ( numeros[i] > '9' )) return;
			super.insertString(offs, new String(numeros), a);
		}
	}
}
