package metainformacion.gui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class CampoTextoNumerico extends JTextField{

	private static final long serialVersionUID = 1L;
	
	public CampoTextoNumerico(){
		super();
		//this.addFocusListener(new ListenerFocus(this));
	}
	public CampoTextoNumerico(int cols) {
		super(cols);
	}

	protected Document createDefaultModel() {
		return new DocumentoSoloNumeros();
	}

	private void focoGanado(FocusEvent e){
		this.setSelectionStart(0);
		this.setSelectionEnd(this.getText().length());
  }

	static class DocumentoSoloNumeros extends PlainDocument {

		private static final long serialVersionUID=3L;
		
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			//boolean novalida = false;
			if (str == null) {
				return;
			}
			char[] numeros = str.toCharArray();
			for (int i = numeros.length - 1; i >= 0; i--) {
				if((numeros[i] < '0') || numeros[i] > '9'){ return; }

			}
			super.insertString(offs, new String(numeros), a);
		}
	}

	class ListenerFocus extends FocusAdapter{
		CampoTextoNumerico campo;

		ListenerFocus(CampoTextoNumerico campo) {
			this.campo = campo;
		}

		public void focusGained(FocusEvent e) {
			campo.focoGanado(e);
		}
    }
}
