package chat;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import chat.eventos.DChatEvent;
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
	
	String ip = null;
	
	PanelChat chat = null;
	PanelVC vc = null;
	

	/**
	 * This is the default constructor
	 * @param ip 
	 */
	public PanelChatVC(DComponenteBase d, String destinatario, VentanaChat v, String ip)
	{
		super();
	
		this.ip = ip;
		initialize(d, destinatario, v);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(DComponenteBase d, String destinatario, VentanaChat v)
	{
		vc = new PanelVC(null, ip);
		this.setRightComponent(vc);
		chat = new PanelChat("chat", true, d, destinatario, v);
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

	public void cerrarConversacion(){
		chat.cerrarConversacion();
	}

	public void setInheritsPopupMenu(String ip)
	{
		// TODO Auto-generated method stub
		vc.setIP(ip);
	}
	
}  //  @jve:decl-index=0:visual-constraint="46,88"
