package awareness.gui;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class CampoTextoNumerico extends JTextField
{
	private static final long serialVersionUID = 1L;

	public CampoTextoNumerico()
	{
		super();
		// this.addFocusListener(new ListenerFocus(this));
	}

	public CampoTextoNumerico( int cols )
	{
		super(cols);
	}

	@Override
	protected Document createDefaultModel()
	{
		return new DocumentoSoloNumeros();
	}

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
