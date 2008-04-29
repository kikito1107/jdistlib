package componentes.gui.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import Deventos.DEvent;

import componentes.base.DComponente;
import componentes.base.DComponenteBase;
import componentes.base.HebraProcesadoraBase;
import componentes.gui.usuarios.ArbolUsuariosConectadosRol;

public class PanelPrincipalChat extends DComponenteBase
{

	private static final long serialVersionUID = 1L;

	private PanelChatVC chat = null;

	private JPanel panelControles = null;

	private JButton botonOcultarChat = null;

	private JButton ocultarVC = null;
	private ArbolUsuariosConectadosRol arbolUsuario = null;

	private String destinatario;
	private VentanaChat ventana = null;
	private String ip = null;
	
	/**
	 * This is the default constructor
	 * @param ip 
	 */
	public PanelPrincipalChat(String nombre, boolean conexionDC, DComponenteBase DCompPadre, String dest, VentanaChat v, String ip)
	{
		super(nombre, conexionDC, DCompPadre);
		destinatario = dest;
		ventana = v;
		this.ip = ip;
		initialize();
		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setLayout(new BorderLayout());
		this.add(getChat(), BorderLayout.CENTER);
		this.add(getPanelControles(), BorderLayout.SOUTH);
		this.add(getArbolUsuario(), BorderLayout.WEST);
	}
	
	private PanelChatVC getChat(){
		if (chat == null){
			chat = new PanelChatVC(this, destinatario, ventana, ip);
		}
		return chat;
	}


	public void cerrar(){
		chat.cerrarConversacion();
	}
	
	/**
	 * This method initializes panelControles	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelControles()
	{
		if (panelControles == null)
		{
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			panelControles = new JPanel();
			panelControles.setLayout(new GridBagLayout());
			panelControles.add(getBotonOcultarChat(), gridBagConstraints1);
			panelControles.add(getOcultarVC(), gridBagConstraints);
		}
		return panelControles;
	}

	/**
	 * This method initializes botonOcultarChat	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBotonOcultarChat()
	{
		if (botonOcultarChat == null)
		{
			botonOcultarChat = new JButton();
			botonOcultarChat.setIcon(new ImageIcon(getClass().getResource("/Resources/webcam.png")));
			botonOcultarChat.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					chat.esconderChat();
				}
			});
		}
		return botonOcultarChat;
	}

	/**
	 * This method initializes ocultarVC	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOcultarVC()
	{
		if (ocultarVC == null)
		{
			ocultarVC = new JButton();
			ocultarVC.setIcon(new ImageIcon(getClass().getResource("/Resources/comments.png")));
			ocultarVC.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					chat.esconderVideoConferencia();
				}
			});
		}
		return ocultarVC;
	}

	
	/**
	 * This method initializes arbolUsuario	
	 * @return
	 */
	private ArbolUsuariosConectadosRol getArbolUsuario()
	{
		arbolUsuario = new ArbolUsuariosConectadosRol(
				"ListaUsuariosConectadosRol", false, null);
		arbolUsuario.setBounds(new Rectangle(1, 196, 186, 167));
		arbolUsuario.setBorder(new LineBorder(Color.gray));
		return arbolUsuario;
	}
	
	/**
	 * Mediante una llamada a este método se envía un mensaje de peticion de
	 * sincronizacion. No se debe llamar a este método de forma directa. Será
	 * llamado de forma automatica cuando sea necesario realizar la sincronizacion
	 */
	public void sincronizar() {
		if (conectadoDC()) {
		}
	}



	/**
	 * Devuelve una nueva instancia de la hebra que se encargara de procesar
	 * los eventos que se reciban. Este metodo no debe llamarse de forma directa.
	 * Sera llamado de forma automatica cuando sea necesario.
	 * @return HebraProcesadoraBase Nueva hebra procesadora
	 */
	public HebraProcesadoraBase crearHebraProcesadora() {
		return new HebraProcesadora(this);
	}

	/**
	 * Obtiene el numero de componentes hijos de este componente. SIEMPRE devuelve 0
	 * @return int Número de componentes hijos. En este caso devuelve 8 (la lista
	 * izquierda, el boton, la lista derecha, la lista de usuarios conectados,
	 * la lista de usuarios conectados bajo nuestro rol, la lista de usuarios
	 * conectados con la informacion del rol actual, el componente de cambio de
	 * rol y la etiqueta del rol actual)
	 */
	public int obtenerNumComponentesHijos() {
		return 1;
	}
	
	public void esconderVC(){
		chat.esconderVideoConferencia();
	}

	/**
	 * Obtiene el componente indicado
	 * @param i int Indice del componente que queremos obtener. Se comienza a numerar
	 * en el 0.
	 * @return DComponente Componente indicado. Si el indice no es v‡lido devuelve
	 * null
	 */
	public DComponente obtenerComponente(int i) {
		DComponente dc = null;
		switch (i) {
			case 0:
				dc = arbolUsuario;
				break;
			case 1:
				//dc = frame.obtenerComponente(0);
				break;
		}
		return dc;
	}

	/**
	 * Procesamos los eventos que recibimos de los componentes hijos. El procesamiento
	 * se reduce a adjuntar el evento del parametro a un nuevo evento y enviarlo.
	 * Los componentes de metainformacion no emiten eventos que deban ser procesados
	 * @param evento DEvent Evento recibido
	 */
	synchronized public void procesarEventoHijo(DEvent evento) {
	}

	

	/**
	 * Hebra procesadora de eventos. Se encarga de realizar las acciones que
	 * correspondan cuando recibe un evento. Tambén se encarga en su inicio
	 * de sincronizar el componente.
	 */
	private class HebraProcesadora
	extends HebraProcesadoraBase {

		HebraProcesadora(DComponente dc) {
			super(dc);
		}

		public void run() {
			
		}
	}



	public void setInterlocutor(String i)
	{
		chat.setDestinatario(i);
		
	}

	public void setIp(String ip2)
	{
		// TODO Auto-generated method stub
		chat.setInheritsPopupMenu(ip2);
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"
