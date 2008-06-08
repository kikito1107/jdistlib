package componentes.base;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import Deventos.ColaEventos;
import Deventos.DEvent;
import Deventos.DJChatEvent;
import Deventos.enlaceJS.DConector;
import aplicacion.fisica.webcam.VideoConferencia;
import aplicacion.fisica.webcam.VideoFrame;

import componentes.gui.chat.PanelChatPrivado;
import componentes.listeners.DJChatListener;
import componentes.listeners.LJChatListener;

/**
 * Implementacion de la clase captadora de eventos para el componente Chat
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DJChat extends JPanel
{
	private static final long serialVersionUID = 1L;

	private Vector<Object> djchatlisteners = new Vector<Object>(5);

	private Vector<Object> ljchatlisteners = new Vector<Object>(5);

	private Integer DID = new Integer(-1);

	private String nombre = null;

	private ColaEventos colaRecepcion = new ColaEventos();

	private ColaEventos colaEnvio = null;

	private Integer ultimoProcesado = new Integer(-1);

	private int nivelPermisos = 10;

	private BorderLayout borderLayout1 = new BorderLayout();

	private JPanel PanelTexto = new JPanel();

	private JScrollPane PanelScroll = new JScrollPane();

	private JScrollPane PanelScroll2 = new JScrollPane(
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	private BorderLayout borderLayout2 = new BorderLayout();

	private JTextArea areaTexto = new JTextArea();

	private JPanel PanelInferior = new JPanel();

	private JTextField campoTexto = new JTextField();

	private JButton botonEnvio = new JButton();

	private Vector<String> nombres = new Vector<String>();

	private Vector<PanelChatPrivado> conversaciones = new Vector<PanelChatPrivado>();

	private Vector<JFrame> ventanas = new Vector<JFrame>();

	/**
	 * Constructor por defecto
	 */
	public DJChat()
	{
		this.nombre = null;
		try
		{
			jbInit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Inicializacion de los componentes graficos
	 * 
	 * @throws Exception
	 */
	void jbInit() throws Exception
	{
		this.setLayout(borderLayout1);
		PanelTexto.setLayout(borderLayout2);
		PanelInferior.setLayout(new BorderLayout());
		// campoTexto.setPreferredSize(new Dimension(350, 40));

		campoTexto.addKeyListener(new java.awt.event.KeyAdapter()
		{
			@Override
			public void keyTyped(java.awt.event.KeyEvent e)
			{
				if (e.getKeyChar() == '\n')
					if (campoTexto.getText().length() > 0)
					{
						DJChatEvent evento = new DJChatEvent();
						evento.tipo = new Integer(DJChatEvent.MENSAJE
								.intValue());
						evento.mensaje = new String(campoTexto.getText());
						for (int i = 0; i < djchatlisteners.size(); i++)
							( (DJChatListener) djchatlisteners.elementAt(i) )
									.nuevoMensaje(evento);

						campoTexto.setText("");
					}
			}
		});

		botonEnvio.setIcon(new ImageIcon("Resources/comment.png"));
		botonEnvio.setToolTipText("Envia el mensaje escrito");
		botonEnvio.addActionListener(new DJChat_botonEnvio_actionAdapter(this));
		areaTexto.setEditable(false);
		areaTexto.setLineWrap(true);
		areaTexto.setWrapStyleWord(true);
		this.setBorder(BorderFactory.createEtchedBorder());
		this.add(PanelTexto, BorderLayout.CENTER);
		PanelTexto.add(PanelScroll, BorderLayout.CENTER);
		PanelScroll.getViewport().add(areaTexto, null);
		this.add(PanelInferior, BorderLayout.SOUTH);
		PanelInferior.add(PanelScroll2, BorderLayout.CENTER);
		PanelInferior.add(botonEnvio, BorderLayout.EAST);
		PanelScroll2.getViewport().add(campoTexto, null);

		// desactivar();
	}

	/**
	 * Habilita el componente
	 */
	public void activar()
	{
		areaTexto.setEnabled(true);
		campoTexto.setEnabled(true);
		botonEnvio.setEnabled(true);
	}

	/**
	 * Deshabilita el componente
	 */
	public void desactivar()
	{
		areaTexto.setEnabled(false);
		campoTexto.setEnabled(false);
		botonEnvio.setEnabled(false);
	}

	/**
	 * Inicia la hebra de procesamiento de eventos
	 */
	public void iniciarHebraProcesadora()
	{
		Thread t = new Thread(new HebraProcesadora(colaRecepcion, this));
		t.start();
	}

	/**
	 * Procesa un evento
	 * 
	 * @param evento
	 *            Evento a procesar
	 */
	@SuppressWarnings( "unchecked" )
	public void procesarEvento(DEvent evento)
	{
		DJChatEvent ev = (DJChatEvent) evento;
		int i;
		if (evento.tipo.intValue() == DJChatEvent.MENSAJE.intValue())
		{
			String cadena = new String("[" + evento.usuario + "]: "
					+ ev.mensaje + "\n");
			areaTexto.append(cadena);
			PanelScroll.getVerticalScrollBar().setValue(
					PanelScroll.getVerticalScrollBar().getMaximum());
			areaTexto.repaint();

			Vector v = getLJChatListeners();
			for (i = 0; i < v.size(); i++)
				( (LJChatListener) v.elementAt(i) ).nuevoMensaje(
						evento.usuario, ev.mensaje);

		}
		else if (evento.tipo.intValue() == DJChatEvent.MENSAJE_PRIVADO
				.intValue())
		{

			DJChatEvent ev1 = (DJChatEvent) evento;

			if (ev1.usuario.equals(DConector.Dusuario)) return;

			if (!ev1.receptores.contains(DConector.Dusuario)) return;

			if (!this.nombres.contains(evento.usuario))
			{
				nombres.add(evento.usuario);
				conversaciones.add(new PanelChatPrivado(this));
				JFrame nuevaVentana = new JFrame();
				nuevaVentana.setTitle(":: Conversación privada con "
						+ ev1.usuario + " ::");
				nuevaVentana.getContentPane().add(conversaciones.lastElement());

				conversaciones.lastElement().agregarReceptor(evento.usuario);

				nuevaVentana
						.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

				nuevaVentana.addWindowListener(new WindowAdapter()
				{

					PanelChatPrivado p = conversaciones.lastElement();

					int index = nombres.size() - 1;

					@Override
					public void windowClosing(WindowEvent e)
					{
						p.cerrarConversacion();
						nombres.remove(index);
						conversaciones.remove(index);
						ventanas.remove(index);
					}
				});

				conversaciones.lastElement().setDestinatario(ev1.usuario);
				nuevaVentana.pack();

				nuevaVentana.setVisible(true);
				conversaciones.lastElement().nuevoMensaje(ev1.usuario,
						ev1.mensaje);
				ventanas.add(nuevaVentana);
			}
			else
			{
				int index = nombres.indexOf(evento.usuario);
				conversaciones.lastElement().agregarReceptor(evento.usuario);
				conversaciones.get(index)
						.nuevoMensaje(ev1.usuario, ev1.mensaje);
			}

			Vector v = getLJChatListeners();
			for (i = 0; i < v.size(); i++)
				( (LJChatListener) v.elementAt(i) ).nuevoMensaje(
						evento.usuario, ev1.mensaje);
		}
		else if (evento.tipo.intValue() == DJChatEvent.FIN_CONVERSACION_PRIVADA
				.intValue())
		{

			DJChatEvent ev2 = (DJChatEvent) evento;

			if (ev2.usuario.equals(DConector.Dusuario)) return;

			if (this.nombres.contains(evento.usuario))
			{
				int index = nombres.indexOf(evento.usuario);

				nombres.remove(index);
				JFrame f = ventanas.remove(index);
				conversaciones.remove(index);

				f.setVisible(false);
				f.dispose();
			}

			Vector v = getLJChatListeners();
			for (i = 0; i < v.size(); i++)
				( (LJChatListener) v.elementAt(i) ).nuevoMensaje(
						evento.usuario, ev2.mensaje);
		}
		else if (evento.tipo.intValue() == DJChatEvent.INICIAR_VC.intValue())
		{

			DJChatEvent ev2 = (DJChatEvent) evento;

			if (ev2.usuario.equals(DConector.Dusuario)) return;

			if (!ev2.receptores.contains(DConector.Dusuario)) return;

			if (( ev2.ipVC != null ) && ( !ev2.ipVC.equals("") ))
			{

				VideoConferencia.establecerOrigen();
				VideoFrame ventana = new VideoFrame(ev2.ipVC);
				ventana.setSize(400, 400);
				ventana.setLocationRelativeTo(null);
				ventana.run();
			}

			DJChatEvent respuesta = new DJChatEvent();

			respuesta.tipo = new Integer(DJChatEvent.RESPUESTA_INICIAR_VC);
			try
			{
				respuesta.ipVC = InetAddress.getLocalHost().getHostAddress();
			}
			catch (UnknownHostException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			respuesta.receptores.add(ev2.usuario);

			this.enviarEvento(respuesta);
		}
		else if (evento.tipo.intValue() == DJChatEvent.RESPUESTA_INICIAR_VC
				.intValue())
		{

			DJChatEvent ev2 = (DJChatEvent) evento;

			// chequeamos que no sea un auto mensaje o que no seamos los
			// receptores del mensaje
			if (ev2.usuario.equals(DConector.Dusuario)) return;

			if (!ev2.receptores.contains(DConector.Dusuario)) return;

			// iniciamos la VC
			if (( ev2.ipVC != null ) && ( !ev2.ipVC.equals("") ))
			{
				VideoConferencia.establecerOrigen();
				VideoFrame ventana = new VideoFrame(ev2.ipVC);
				ventana.setSize(400, 400);
				ventana.setLocationRelativeTo(null);
				ventana.run();
			}
		}
	}

	/**
	 * Envia un evento de Chat
	 * 
	 * @param e
	 *            Evento a enviar
	 */
	public void enviarEvento(DJChatEvent e)
	{
		for (int i = 0; i < djchatlisteners.size(); i++)
			( (DJChatListener) djchatlisteners.elementAt(i) ).nuevoMensaje(e);
	}

	/**
	 * Sincroniza las instancias del componente
	 */
	public void sincronizar()
	{
		// No nos interesa sincronizar
	}

	/**
	 * Obtiene el nivel de permisos del componente
	 * 
	 * @return Nivel de permisos del componente
	 */
	public int getNivelPermisos()
	{
		return nivelPermisos;
	}

	/**
	 * Pone permiso de solo lectura
	 */
	public void permisoLectura()
	{
		campoTexto.setEnabled(false);
		botonEnvio.setEnabled(false);
	}

	/**
	 * Pone permiso de lectura y escritura
	 */
	public void permisoLecturaEscritura()
	{
		campoTexto.setEnabled(true);
		botonEnvio.setEnabled(true);
	}

	/**
	 * Asigna un nivel de permisos a un componente y realiza los cambios
	 * oportunos en el
	 * 
	 * @param nivel
	 *            Nivel de permisos a asignar
	 */
	public void setNivelPermisos(int nivel)
	{
		nivelPermisos = nivel;

		if (nivelPermisos >= 20)
		{
			campoTexto.setEnabled(true);
			botonEnvio.setEnabled(true);
		}
		else
		{
			campoTexto.setEnabled(false);
			botonEnvio.setEnabled(false);
		}
	}

	/**
	 * Obtiene el identificador del componente
	 * 
	 * @return Identificador del componente
	 */
	public Integer getID()
	{
		return DID;
	}

	/**
	 * Obtiene el nombre del componente
	 * 
	 * @return
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * Obtiene la cola de recepcion de eventos
	 * 
	 * @return Cola de eventos
	 */
	public ColaEventos obtenerColaRecepcion()
	{
		return colaRecepcion;
	}

	/**
	 * Obtiene la cola de envio de eventos
	 * 
	 * @return Cola de eventos
	 */
	public ColaEventos obtenerColaEnvio()
	{
		return colaEnvio;
	}

	/**
	 * Crea la hebra de procesamiento de eventos
	 * 
	 * @return Hebra procesadora de eventos. En este caso devuelve null siempre.
	 */
	public HebraProcesadoraBase crearHebraProcesadora()
	{
		return null;
	}

	/**
	 * Permite agregar un listener para los eventos producidos
	 * 
	 * @param listener
	 *            Listener a agregar
	 */
	public void addDJChatListener(DJChatListener listener)
	{
		djchatlisteners.add(listener);
	}

	/**
	 * Permite agregar un listener para los eventos que sean propagados para
	 * todos los usuarios conectados
	 * 
	 * @param listener
	 *            Listener a agregar
	 */
	public void addLJChatListener(LJChatListener listener)
	{
		ljchatlisteners.add(listener);
	}

	/**
	 * Permite obtener el vector de Listeners para los eventos distribuidos que
	 * se produzcan
	 * 
	 * @return Vector de listeners
	 */
	@SuppressWarnings( "unchecked" )
	public Vector getDJChatListeners()
	{
		return djchatlisteners;
	}

	/**
	 * Permite obtener el vector de Listeners para los eventos producidos para
	 * los usuarios conectados
	 * 
	 * @return Vector de listeners
	 */
	@SuppressWarnings( "unchecked" )
	public Vector getLJChatListeners()
	{
		return ljchatlisteners;
	}

	/**
	 * Elimina los listener que reciben los eventos distribuidos
	 */
	public void removeDJChatListeners()
	{
		djchatlisteners.removeAllElements();
	}

	/**
	 * Elimina los listener que reciben los eventos para los usuarios conectados
	 * en el sistema
	 */
	public void removeLJChatListeners()
	{
		ljchatlisteners.removeAllElements();
	}

	/**
	 * Accion ejecutada al pulsar el boton de envio de un mensaje en el chat
	 * 
	 * @param e
	 *            Evento a enviar
	 */
	private void botonEnvio_actionPerformed(ActionEvent e)
	{
		if (campoTexto.getText().length() > 0)
		{
			DJChatEvent evento = new DJChatEvent();
			evento.tipo = new Integer(DJChatEvent.MENSAJE.intValue());
			evento.mensaje = new String(campoTexto.getText());
			for (int i = 0; i < djchatlisteners.size(); i++)
				( (DJChatListener) djchatlisteners.elementAt(i) )
						.nuevoMensaje(evento);

			campoTexto.setText("");
		}
	}

	/**
	 * Permite escuchar los eventos producidos
	 */
	@SuppressWarnings( "unused" )
	private class Listener implements DJChatListener
	{
		public void nuevoMensaje(DJChatEvent evento)
		{
			if (nivelPermisos >= 20)
			{
				evento.origen = new Integer(1); // Aplicacion
				evento.destino = new Integer(0); // Coordinador
				evento.componente = new Integer(DID.intValue());
				evento.tipo = new Integer(DJChatEvent.MENSAJE.intValue());
				evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

				colaEnvio.nuevoEvento(evento);
			}
		}
	}

	/**
	 * Hebra procesadora de eventos
	 */
	class HebraProcesadora implements Runnable
	{

		ColaEventos cola = null;

		DJChat chat = null;

		HebraProcesadora( ColaEventos cola, DJChat chat )
		{
			this.cola = cola;
			this.chat = chat;
		}

		@SuppressWarnings( "unchecked" )
		public void run()
		{
			DJChatEvent evento = null;
			int i = 0;

			// En este componente no nos interesa sincronizar ya que no estamos
			// interesados
			// en lo que se haya hablado antes de llegar nosotros

			activar();

			while (true)
			{
				evento = (DJChatEvent) cola.extraerEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());
				if (nivelPermisos >= 10)
					/*
					 * System.out.println("HebraProcesadora(" + DID + "):
					 * Procesado: Tipo=" + evento.tipo + " Ult. Proc.=" +
					 * evento.ultimoProcesado);
					 */
					if (evento.tipo.intValue() == DJChatEvent.MENSAJE
							.intValue())
					{
						String cadena = new String("(" + evento.usuario + "): "
								+ evento.mensaje + "\n");
						areaTexto.append(cadena);
						PanelScroll.getVerticalScrollBar()
								.setValue(
										PanelScroll.getVerticalScrollBar()
												.getMaximum());
						areaTexto.repaint();

						Vector v = getLJChatListeners();
						for (i = 0; i < v.size(); i++)
							( (LJChatListener) v.elementAt(i) ).nuevoMensaje(
									evento.usuario, evento.mensaje);

					}
			}
		}
	}

	public void limpiarTexto()
	{
		areaTexto.setText("");

	}

	public String getTexto()
	{
		return areaTexto.getText();

	}

	public void setFuente(Font f)
	{
		areaTexto.setFont(f);

	}

	private class DJChat_botonEnvio_actionAdapter implements
			java.awt.event.ActionListener
	{
		DJChat adaptee;

		DJChat_botonEnvio_actionAdapter( DJChat adaptee )
		{
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e)
		{
			adaptee.botonEnvio_actionPerformed(e);
		}
	}
}
