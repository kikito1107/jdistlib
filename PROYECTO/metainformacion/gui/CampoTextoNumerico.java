package metainformacion.gui;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

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
