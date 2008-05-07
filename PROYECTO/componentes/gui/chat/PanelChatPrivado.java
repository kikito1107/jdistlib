package componentes.gui.chat;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Deventos.DJChatEvent;
import Deventos.enlaceJS.DConector;

import componentes.base.DJChat;

public class PanelChatPrivado extends JPanel
{

	private static final long serialVersionUID = 1L;
	private JPanel PanelIntroTexto = null;
	private JTextField Texto = null;
	private JButton botonEviar = null;
	private JTextArea textoChat = null;
	private JPanel panel = null;
	private String destinatario = null;
	private DJChat padre = null;
	/**
	 * This is the default constructor
	 */
	public PanelChatPrivado(DJChat p)
	{
		super();
		padre = p;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		BorderLayout borderLayout1 = new BorderLayout();
		borderLayout1.setHgap(2);
		borderLayout1.setVgap(2);
		this.setLayout(borderLayout1);
		this.setSize(347, 293);
		this.setPreferredSize(new Dimension(531, 350));
		this.add(getPanel(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes PanelIntroTexto	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelIntroTexto()
	{
		if (PanelIntroTexto == null)
		{
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(8);
			borderLayout.setVgap(0);
			PanelIntroTexto = new JPanel();
			PanelIntroTexto.setLayout(borderLayout);
			PanelIntroTexto.add(getTexto(), BorderLayout.CENTER);
			PanelIntroTexto.add(getBotonEviar(), BorderLayout.EAST);
		}
		return PanelIntroTexto;
	}

	/**
	 * This method initializes Texto	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTexto()
	{
		if (Texto == null)
		{
			Texto = new JTextField();
			Texto.setPreferredSize(new Dimension(200, 16));
			Texto.addKeyListener(new java.awt.event.KeyAdapter()
			{
				public void keyTyped(java.awt.event.KeyEvent e)
				{
					if (e.getKeyChar() == '\n') {
						enviarMensaje(Texto.getText());
						Texto.setText("");
					}
				}
			});
		}
		return Texto;
	}

	/**
	 * This method initializes botonEviar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBotonEviar()
	{
		if (botonEviar == null)
		{
			botonEviar = new JButton();
			botonEviar.setText("");
			botonEviar.setIcon(new ImageIcon(getClass().getResource("/Resources/comment.png")));
			botonEviar.setPreferredSize(new Dimension(48, 42));
			botonEviar.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					// TODO Evento de env’o de mensaje
					enviarMensaje(Texto.getText());
					Texto.setText("");
					
				}
			});
		}
		return botonEviar;
	}
	
	/**
	 * Envia un mesaje al usuario con el que estamos conversando
	 * @param mensaje mensaje 
	 */
	public void enviarMensaje(String mensaje){
		
		if (!mensaje.equals("") && destinatario != null) {
			DJChatEvent ev = new DJChatEvent();
			ev.mensaje = new String(mensaje);
			ev.tipo = new Integer(DJChatEvent.MENSAJE_PRIVADO);
			ev.receptor = new String(this.destinatario);
			
			this.nuevoMensaje(DConector.Dusuario, Texto.getText());
			padre.enviarEvento(ev);
		}
	}

	/**
	 * This method initializes textoChat	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTextoChat()
	{
		if (textoChat == null)
		{
			textoChat = new JTextArea();
			textoChat.setWrapStyleWord(true);
			textoChat.setLineWrap(true);
			textoChat.setEditable(false);
		}
		return textoChat;
	}

	/**
	 * This method initializes panel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanel()
	{
		if (panel == null)
		{
			panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.add(getPanelIntroTexto(), BorderLayout.SOUTH);
			panel.add(new JScrollPane(getTextoChat(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		}
		return panel;
	}
	
	

	public void setDestinatario(String d)
	{
		if (destinatario == null || !destinatario.equals(d)) {
			destinatario = d;
			textoChat.setText("");
		}
	}

	public String getDestinatario()
	{
		return destinatario;
	}

	public void cerrarConversacion()
	{
		DJChatEvent ev = new DJChatEvent();
		ev.tipo = new Integer(DJChatEvent.FIN_CONVERSACION_PRIVADA.intValue());
		ev.receptor = new String(destinatario);
		padre.enviarEvento(ev);
	}

	public void nuevoMensaje(String usuario, String mensaje)
	{
		textoChat.setText( textoChat.getText() + "\n[" + usuario + "]: " + mensaje );
	}

}  //  @jve:decl-index=0:visual-constraint="7,19"
