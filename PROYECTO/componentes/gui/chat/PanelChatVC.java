package componentes.gui.chat;

import javax.swing.JSplitPane;

import aplicacion.fisica.webcam.PanelVC;

import componentes.base.DComponenteBase;

public class PanelChatVC extends JSplitPane
{

	private static final long serialVersionUID = 1L;
	public static final int SIZE_X = 340;
	public static final int SIZE_Y = 330;
	
	String ip = null;
	
	PanelChatPrivado chat = null;
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
		//chat = new PanelChatPrivado("chat", true, d, destinatario, v);
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
