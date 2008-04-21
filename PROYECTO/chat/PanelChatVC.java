package chat;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import chat.webcam.PanelVC;
import chat.webcam.VideoConferencia;

import componentes.DComponenteBase;
import componentes.gui.usuarios.ArbolUsuariosConectadosRol;

import java.awt.Dimension;

public class PanelChatVC extends JSplitPane
{

	private static final long serialVersionUID = 1L;
	public static final int SIZE_X = 340;
	public static final int SIZE_Y = 330;
	
	PanelChat chat = null;
	

	/**
	 * This is the default constructor
	 */
	public PanelChatVC(DComponenteBase d, String destinatario)
	{
		super();
		initialize(d, destinatario);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(DComponenteBase d, String destinatario)
	{
		
		this.setRightComponent(new PanelVC(null, "127.0.0.1"));
		chat = new PanelChat("chat", true, d, destinatario);
		this.setLeftComponent(chat);
		this.setOneTouchExpandable(true);
		this.setSize(SIZE_X*2, SIZE_Y);
		this.setDividerLocation(1.0);
	}
	
	public void esconderVideoConferencia() {
		this.setDividerLocation(1.0);
		//this.setSize(SIZE_X, SIZE_Y);
	}
	
	public void esconderChat() {
		this.setDividerLocation(0.0);
		//this.setSize(SIZE_X, SIZE_Y);
	}

	public void setDestinatario(String i)
	{
		chat.setDestinatario(i);
	}

}  //  @jve:decl-index=0:visual-constraint="46,88"
