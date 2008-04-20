package chat;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import chat.webcam.PanelVC;
import chat.webcam.VideoConferencia;

import componentes.gui.usuarios.ArbolUsuariosConectadosRol;

import java.awt.Dimension;

public class PanelChatVC extends JSplitPane
{

	private static final long serialVersionUID = 1L;
	public static final int SIZE_X = 340;
	public static final int SIZE_Y = 330;
	

	/**
	 * This is the default constructor
	 */
	public PanelChatVC()
	{
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		VideoConferencia.establecerOrigen();
		String ip = JOptionPane.showInputDialog("Inserte IP de otro ordenador");
		this.setRightComponent(new PanelVC(null, ip));
		this.setLeftComponent(new PanelChat());
		this.setOneTouchExpandable(true);
		this.setSize(SIZE_X*2, SIZE_Y);
		this.setDividerLocation(0.5);
	}
	
	public void esconderVideoConferencia() {
		this.setDividerLocation(1.0);
		//this.setSize(SIZE_X, SIZE_Y);
	}
	
	public void esconderChat() {
		this.setDividerLocation(0.0);
		//this.setSize(SIZE_X, SIZE_Y);
	}

}  //  @jve:decl-index=0:visual-constraint="46,88"
