package aplicacion.plugin.example;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import util.JFontChooser;
import Deventos.enlaceJS.DConector;
import aplicacion.plugin.DAbstractPlugin;

import componentes.base.DJFrame;
import componentes.gui.DIChat;
import componentes.gui.usuarios.ArbolUsuariosConectadosRol;
import Deventos.DJChatEvent;

public class DChatPlugin extends DAbstractPlugin
{

	private DJFrame ventanaChat = null;

	ArbolUsuariosConectadosRol arbol = null;

	DIChat chat;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6310087937591625336L;

	public DChatPlugin() throws Exception
	{
		super("chatplugin", false, null);
		ventanaChat = new DJFrame(false, "");
		init();
	}

	@Override
	public DAbstractPlugin getInstance() throws Exception
	{
		return new DChatPlugin();
	}

	@Override
	public void init() throws Exception
	{
		version = 5;
		nombre = "Chat";
		shouldShow = true;
		jarFile = "ejemplo.jar";

		arbol = new ArbolUsuariosConectadosRol("ListaUsuariosConectadosRol",
				true, null);
		chat = new DIChat("chat", true, null);

		JPanel oeste = new JPanel(new BorderLayout()), centro = new JPanel(
				new BorderLayout());
		oeste.add(new JLabel(" Usuarios Conectados "), BorderLayout.NORTH);
		oeste.add(arbol, BorderLayout.CENTER);
		centro.add(new PanelControlChat(), BorderLayout.NORTH);
		centro.add(chat, BorderLayout.CENTER);

		ventanaChat.getContentPane().setLayout(new BorderLayout());
		ventanaChat.getContentPane().add(centro, BorderLayout.CENTER);
		ventanaChat.getContentPane().add(oeste, BorderLayout.WEST);
		ventanaChat.setTitle(":: Chat - " + DConector.Dusuario + " ::");
		ventanaChat.setResizable(true);
	}

	@Override
	public void start() throws Exception
	{
		// TODO Auto-generated method stub
		ventanaChat.pack();
		ventanaChat.setSize(550, 430);

		ventanaChat.setLocationRelativeTo(null);

		ventanaChat.setVisible(true);
	}

	@Override
	public void stop() throws Exception
	{
		// TODO Auto-generated method stub
		ventanaChat.dispose();
	}

	/**
	 * 
	 * @author anab
	 */
	private class PanelControlChat extends JPanel
	{

		private static final long serialVersionUID = 1L;

		private JToolBar barra = null;

		private JButton botonNuevoPrivado = null;

		private JButton botonNuevaVC = null;

		private JButton botonSalir = null;

		private JButton botonGuardarConversacion = null;

		private JButton botonLimpiar = null;

		private JButton botonFuente = null;

		/**
		 * This is the default constructor
		 */
		public PanelControlChat()
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
			this.setSize(496, 39);
			this.setPreferredSize(new Dimension(373, 39));
			this.setMinimumSize(new Dimension(373, 39));
			this.setLayout(new BorderLayout());
			this.add(getBarra(), BorderLayout.CENTER);
		}

		/**
		 * This method initializes barra
		 * 
		 * @return javax.swing.JToolBar
		 */
		private JToolBar getBarra()
		{
			if (barra == null)
			{
				barra = new JToolBar();
				barra.setFloatable(true);

				barra.add(getBotonNuevoPrivado());
				barra.add(getBotonNuevaVC());
				barra.add(getBotonGuardarConversacion());
				barra.add(getBotonLimpiar());
				barra.add(getBotonFuente());
				barra.add(getBotonSalir());

			}
			return barra;
		}

		/**
		 * This method initializes botonNuevoPrivado
		 * 
		 * @return javax.swing.JButton
		 */
		private JButton getBotonNuevoPrivado()
		{
			if (botonNuevoPrivado == null)
			{
				botonNuevoPrivado = new JButton(" ");
				botonNuevoPrivado.setIcon(new ImageIcon(getClass().getResource(
						"/Resources/comments.png")));
				botonNuevoPrivado
						.setToolTipText("Invitar a un usuario a iniciar una conversación privada");
				this.botonNuevoPrivado.setBorder(null);
				this.botonNuevoPrivado.setBorderPainted(false);
				botonNuevoPrivado
						.addActionListener(new java.awt.event.ActionListener()
						{
							public void actionPerformed(
									java.awt.event.ActionEvent e)
							{

								String usuario = arbol.getUsuarioSeleccionado();

								if (( usuario != null )
										&& !usuario.equals(DConector.Dusuario))
								{
									DJChatEvent evento = new DJChatEvent();
									evento.tipo = new Integer(
											DJChatEvent.MENSAJE_PRIVADO);
									evento.receptores.add(usuario);
									evento.mensaje = "Solicita una nueva conversación";

									chat.enviarEvento(evento);
								}
								else JOptionPane
										.showMessageDialog(null,
												"No puedes mantener una conversación contigo mismo");
							}
						});
			}
			return botonNuevoPrivado;
		}

		/**
		 * This method initializes botonNuevaVC
		 * 
		 * @return javax.swing.JButton
		 */
		private JButton getBotonNuevaVC()
		{
			if (botonNuevaVC == null)
			{
				botonNuevaVC = new JButton(" ");
				botonNuevaVC.setIcon(new ImageIcon(getClass().getResource(
						"/Resources/webcam.png")));
				this.botonNuevaVC.setBorder(null);
				this.botonNuevaVC.setBorderPainted(false);
				botonNuevaVC
						.setToolTipText("Invitar a un usuario a iniciar una videoconferencia");
				botonNuevaVC
						.addActionListener(new java.awt.event.ActionListener()
						{
							public void actionPerformed(
									java.awt.event.ActionEvent e)
							{
								String user = arbol.getUsuarioSeleccionado();

								if (( user != null )
										&& ( user != DConector.Dusuario ))
								{

									DJChatEvent ev = new DJChatEvent();

									ev.tipo = new Integer(
											DJChatEvent.INICIAR_VC.intValue());
									try
									{
										ev.ipVC = InetAddress.getLocalHost()
												.getHostAddress();
										ev.receptores.add(new String(user));

										chat.enviarEvento(ev);

									}
									catch (UnknownHostException e1)
									{
										JOptionPane
												.showMessageDialog(null,
														"Ha ocurrido un error en la comunicación. Inténtelo más tarde");
										return;
									}

								}
							}
						});
			}
			return botonNuevaVC;
		}

		/**
		 * This method initializes botonSalir
		 * 
		 * @return javax.swing.JButton
		 */
		private JButton getBotonSalir()
		{
			if (botonSalir == null)
			{
				botonSalir = new JButton(" ");
				botonSalir.setIcon(new ImageIcon(getClass().getResource(
						"/Resources/cancel.png")));
				botonSalir.setBorder(null);
				this.botonSalir.setBorderPainted(false);
				botonSalir.setToolTipText("Salir del chat");
				botonSalir
						.addActionListener(new java.awt.event.ActionListener()
						{
							public void actionPerformed(
									java.awt.event.ActionEvent e)
							{

							}
						});
			}
			return botonSalir;
		}

		/**
		 * This method initializes botonGuardarConversacion
		 * 
		 * @return javax.swing.JButton
		 */
		private JButton getBotonGuardarConversacion()
		{
			if (botonGuardarConversacion == null)
			{
				botonGuardarConversacion = new JButton(" ");
				botonGuardarConversacion.setBorder(null);
				this.botonGuardarConversacion.setBorderPainted(false);
				botonGuardarConversacion.setIcon(new ImageIcon(getClass()
						.getResource("/Resources/disk_local.png")));
				botonGuardarConversacion
						.setToolTipText("Guarda la conversación en un documento de texto");
				botonGuardarConversacion
						.addActionListener(new java.awt.event.ActionListener()
						{
							public void actionPerformed(
									java.awt.event.ActionEvent e)
							{

								JFileChooser jfc = new JFileChooser(
										"Guardar Conversación");

								jfc.setDialogType(JFileChooser.SAVE_DIALOG);

								jfc
										.setFileSelectionMode(JFileChooser.FILES_ONLY);

								int op = jfc.showDialog(null,
										"Guardar Conversación");

								if (op == JFileChooser.APPROVE_OPTION)
								{
									File aFile = jfc.getSelectedFile();

									// use buffering
									Writer output = null;
									try
									{
										output = new BufferedWriter(
												new FileWriter(aFile));
									}
									catch (IOException e1)
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									try
									{
										// FileWriter always assumes default
										// encoding is OK!
										Calendar c = Calendar.getInstance();

										String fecha = c
												.get(Calendar.DAY_OF_MONTH)
												+ "/"
												+ ( c.get(Calendar.MONTH) + 1 )
												+ "/" + c.get(Calendar.YEAR);

										String hora = c
												.get(Calendar.HOUR_OF_DAY)
												+ ":"
												+ c.get(Calendar.MINUTE)
												+ ":" + c.get(Calendar.SECOND);

										output.write("Chat Log  saved at "
												+ hora + " on " + fecha + ":\n"
												+ chat.getTexto());
										output.close();
									}
									catch (IOException e2)
									{
										// TODO Auto-generated catch block
										e2.printStackTrace();
									}
								}
							}
						});
			}
			return botonGuardarConversacion;
		}

		/**
		 * This method initializes botonLimpiar
		 * 
		 * @return javax.swing.JButton
		 */
		private JButton getBotonLimpiar()
		{
			if (botonLimpiar == null)
			{
				botonLimpiar = new JButton(" ");
				botonLimpiar.setBorder(null);
				botonLimpiar.setBorderPainted(false);
				botonLimpiar.setIcon(new ImageIcon(getClass().getResource(
						"/Resources/edit-clear_16x16.png")));
				botonLimpiar
						.addActionListener(new java.awt.event.ActionListener()
						{
							public void actionPerformed(
									java.awt.event.ActionEvent e)
							{
								chat.limpiarTexto();
							}
						});
			}
			return botonLimpiar;
		}

		/**
		 * This method initializes botonFuente
		 * 
		 * @return javax.swing.JButton
		 */
		private JButton getBotonFuente()
		{
			if (botonFuente == null)
			{
				botonFuente = new JButton(" ");
				botonFuente.setBorder(null);
				this.botonFuente.setBorderPainted(false);
				botonFuente.setIcon(new ImageIcon(getClass().getResource(
						"/Resources/font.png")));
				botonFuente
						.addActionListener(new java.awt.event.ActionListener()
						{
							public void actionPerformed(
									java.awt.event.ActionEvent e)
							{
								JFontChooser fontChooser = new JFontChooser();
								int retValue = fontChooser.showDialog(null);
								// get the selected font
								if (retValue == JFontChooser.OK_OPTION)
								{
									Font selected = fontChooser
											.getSelectedFont();
									// do something...

									chat.setFuente(selected);
								}
							}
						});
			}
			return botonFuente;
		}
	}
}
