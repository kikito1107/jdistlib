package componentes.base;

import java.awt.*;

import javax.swing.*;

import aplicacion.fisica.webcam.VideoConferencia;
import aplicacion.fisica.webcam.VideoFrame;

import componentes.gui.chat.PanelChatPrivado;
import componentes.listeners.*;

import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import Deventos.*;
import Deventos.enlaceJS.DConector;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
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

	BorderLayout borderLayout1 = new BorderLayout();

	JPanel PanelTexto = new JPanel();

	JScrollPane PanelScroll = new JScrollPane();

	JScrollPane PanelScroll2 = new JScrollPane(
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	BorderLayout borderLayout2 = new BorderLayout();

	JTextArea areaTexto = new JTextArea();

	JPanel PanelInferior = new JPanel();

	JTextField campoTexto = new JTextField();

	FlowLayout flowLayout1 = new FlowLayout();

	JButton botonEnvio = new JButton();

	Vector<String> nombres = new Vector<String>();

	Vector<PanelChatPrivado> conversaciones = new Vector<PanelChatPrivado>();

	Vector<JFrame> ventanas = new Vector<JFrame>();

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

		/*
		 * addDJChatListener(new Listener()); DID = new
		 * Integer(DConector.alta(this)); colaEnvio =
		 * DConector.getColaEventos();
		 */
	}

	void jbInit() throws Exception
	{
		this.setLayout(borderLayout1);
		PanelTexto.setLayout(borderLayout2);
		PanelInferior.setLayout(new BorderLayout());
		// campoTexto.setPreferredSize(new Dimension(350, 40));

		campoTexto.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyTyped(java.awt.event.KeyEvent e)
			{
				if (e.getKeyChar() == '\n')
				{
					if (campoTexto.getText().length() > 0)
					{
						DJChatEvent evento = new DJChatEvent();
						evento.tipo = new Integer(DJChatEvent.MENSAJE
								.intValue());
						evento.mensaje = new String(campoTexto.getText());
						for (int i = 0; i < djchatlisteners.size(); i++)
						{
							( (DJChatListener) djchatlisteners.elementAt(i) )
									.nuevoMensaje(evento);
						}

						campoTexto.setText("");
					}
				}
			}
		});

		botonEnvio.setIcon(new ImageIcon(getClass().getResource(
				"/Resources/comment.png")));
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

	public void activar()
	{
		areaTexto.setEnabled(true);
		campoTexto.setEnabled(true);
		botonEnvio.setEnabled(true);
	}

	public void desactivar()
	{
		areaTexto.setEnabled(false);
		campoTexto.setEnabled(false);
		botonEnvio.setEnabled(false);
	}

	public void iniciarHebraProcesadora()
	{
		Thread t = new Thread(new HebraProcesadora(colaRecepcion, this));
		t.start();
	}

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
			{
				( (LJChatListener) v.elementAt(i) ).nuevoMensaje(
						evento.usuario, ev.mensaje);
			}

		}
		else if (evento.tipo.intValue() == DJChatEvent.MENSAJE_PRIVADO
				.intValue())
		{

			DJChatEvent ev1 = (DJChatEvent) evento;

			if (ev1.usuario.equals(DConector.Dusuario)) { 
				return;
			}

			if (!ev1.receptores.contains(DConector.Dusuario)) return;

			if (!this.nombres.contains(evento.usuario))
			{
				nombres.add(evento.usuario);
				conversaciones.add(new PanelChatPrivado(this));
				JFrame nuevaVentana = new JFrame();
				nuevaVentana.setTitle(":: Conversaci—n privada con "
						+ ev1.usuario + " ::");
				nuevaVentana.getContentPane().add(conversaciones.lastElement());
				
				conversaciones.lastElement().agregarReceptor(evento.usuario);

				nuevaVentana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				nuevaVentana.addWindowListener(new WindowAdapter()
				{

					PanelChatPrivado p = conversaciones.lastElement();

					int index = nombres.size() - 1;

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
			{
				( (LJChatListener) v.elementAt(i) ).nuevoMensaje(
						evento.usuario, ev1.mensaje);
			}
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
			{
				( (LJChatListener) v.elementAt(i) ).nuevoMensaje(
						evento.usuario, ev2.mensaje);
			}
		}
		else if (evento.tipo.intValue() == DJChatEvent.INICIAR_VC.intValue())
		{

			
			
			DJChatEvent ev2 = (DJChatEvent) evento;
			
			if (ev2.usuario.equals(DConector.Dusuario)) { 
				return;
			}

			if (!ev2.receptores.contains(DConector.Dusuario)) return;
			
			if (ev2.ipVC != null && ev2.ipVC != ""){
				
				
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
		else if (evento.tipo.intValue() == DJChatEvent.RESPUESTA_INICIAR_VC.intValue())
		{

			DJChatEvent ev2 = (DJChatEvent) evento;

			// chequeamos que no sea un auto mensaje o que no seamos los receptores del mensaje
			if (ev2.usuario.equals(DConector.Dusuario))  return;

			if (!ev2.receptores.contains(DConector.Dusuario)) return;
			
			
			// iniciamos la VC
			if (ev2.ipVC != null && ev2.ipVC != ""){
				
				
				JOptionPane.showMessageDialog(null, "IP de la nueva conversaci—n " + ev2.ipVC);
				
				VideoConferencia.establecerOrigen();
				VideoFrame ventana = new VideoFrame(ev2.ipVC);
				ventana.setSize(400, 400);
				ventana.setLocationRelativeTo(null);
				ventana.run();
			}
		}
	}

	public void enviarEvento(DJChatEvent e)
	{
		for (int i = 0; i < djchatlisteners.size(); i++)
		{
			( (DJChatListener) djchatlisteners.elementAt(i) ).nuevoMensaje(e);
		}
	}

	public void sincronizar()
	{
		// No nos interesa sincronizar
	}

	public int getNivelPermisos()
	{
		return nivelPermisos;
	}

	public void permisoLectura()
	{
		campoTexto.setEnabled(false);
		botonEnvio.setEnabled(false);
	}

	public void permisoLecturaEscritura()
	{
		campoTexto.setEnabled(true);
		botonEnvio.setEnabled(true);
	}

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

	public Integer getID()
	{
		return DID;
	}

	public String getNombre()
	{
		return nombre;
	}

	public ColaEventos obtenerColaRecepcion()
	{
		return colaRecepcion;
	}

	public ColaEventos obtenerColaEnvio()
	{
		return colaEnvio;
	}

	public HebraProcesadoraBase crearHebraProcesadora()
	{

		return null;
	}

	public void addDJChatListener(DJChatListener listener)
	{
		djchatlisteners.add(listener);
	}

	public void addLJChatListener(LJChatListener listener)
	{
		ljchatlisteners.add(listener);
	}

	public Vector getDJChatListeners()
	{
		return djchatlisteners;
	}

	public Vector getLJChatListeners()
	{
		return ljchatlisteners;
	}

	public void removeDJChatListeners()
	{
		djchatlisteners.removeAllElements();
	}

	public void removeLJChatListeners()
	{
		ljchatlisteners.removeAllElements();
	}

	void botonEnvio_actionPerformed(ActionEvent e)
	{
		if (campoTexto.getText().length() > 0)
		{
			DJChatEvent evento = new DJChatEvent();
			evento.tipo = new Integer(DJChatEvent.MENSAJE.intValue());
			evento.mensaje = new String(campoTexto.getText());
			for (int i = 0; i < djchatlisteners.size(); i++)
			{
				( (DJChatListener) djchatlisteners.elementAt(i) )
						.nuevoMensaje(evento);
			}

			campoTexto.setText("");
		}
	}

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

	class HebraProcesadora implements Runnable
	{

		ColaEventos cola = null;

		DJChat chat = null;

		HebraProcesadora( ColaEventos cola, DJChat chat )
		{
			this.cola = cola;
			this.chat = chat;
		}

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
				{
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
						{
							( (LJChatListener) v.elementAt(i) ).nuevoMensaje(
									evento.usuario, evento.mensaje);
						}

					}
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

}

class DJChat_botonEnvio_actionAdapter implements java.awt.event.ActionListener
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
