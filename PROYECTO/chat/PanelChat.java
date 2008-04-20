package chat;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JTextArea;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JTree;

public class PanelChat extends JPanel
{

	private static final long serialVersionUID = 1L;
	private JPanel PanelIntroTexto = null;
	private JTextField Texto = null;
	private JButton botonEviar = null;
	private JTextArea textoChat = null;
	private JPanel panel = null;

	/**
	 * This is the default constructor
	 */
	public PanelChat()
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
						//TODO evento de env’o de mensaje
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
				}
			});
		}
		return botonEviar;
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

}  //  @jve:decl-index=0:visual-constraint="7,19"
