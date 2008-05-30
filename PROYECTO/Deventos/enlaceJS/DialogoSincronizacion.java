package Deventos.enlaceJS;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

public class DialogoSincronizacion extends JDialog
{

	private static final long serialVersionUID = 1L;

	JPanel panel1 = new JPanel();

	Frame frame = new JFrame();

	protected JLabel etiquetaIcono = new JLabel();

	JLabel etiqueta = new JLabel();

	private final int x = 10;

	private final int y = 10;

	protected Icon icono = null;

	private volatile Thread t = new Thread(new HebraParpadeo(this));

	private Monitor monitor = new Monitor();

	public DialogoSincronizacion()
	{
		super();
		this.setTitle("Inicializando...");
		try
		{
			jbInit();
			pack();
			this.setSize(400, 85);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		panel1.setLayout(null);
		etiquetaIcono.setFont(new java.awt.Font("Dialog", 1, 11));
		icono = new ImageIcon("Resources/logo.png");
		etiquetaIcono.setIcon(icono);
		etiquetaIcono.setText("");
		etiquetaIcono.setBounds(new Rectangle(7, 3, 53, 54));
		etiqueta.setFont(new java.awt.Font("Dialog", 1, 11));
		etiqueta.setForeground(Color.black);
		etiqueta.setBounds(new Rectangle(66, 23, 439, 15));
		this
				.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.addWindowListener(new MensajesIni_this_windowAdapter(this));
		getContentPane().add(panel1);
		panel1.add(etiquetaIcono, null);
		panel1.add(etiqueta, null);
		this.setLocation(x, y);

		t.start();
	}

	public void mostrar(String mensaje, boolean parpadeo)
	{
		etiqueta.setText(mensaje);
		// etiquetaIcono.setIcon(icono);
		if (!isVisible())
		{
			this.setLocation(x, y);
			this.setVisible(true);
		}

		monitor.setParpadeo(parpadeo);
	}

	public void ocultar()
	{
		this.setVisible(false);
		monitor.setParpadeo(false);
	}

	void this_windowClosing(WindowEvent e)
	{
		DConector.obtenerDC().salir();
	}

	private class HebraParpadeo implements Runnable
	{
		DialogoSincronizacion dialogo = null;

		HebraParpadeo( DialogoSincronizacion dialogo )
		{
			this.dialogo = dialogo;
		}

		public void run()
		{
			int tiempo = 500;
			while (true)
				try
				{
					dialogo.etiquetaIcono.setIcon(null);
					Thread.sleep(tiempo);
					dialogo.etiquetaIcono.setIcon(dialogo.icono);
					Thread.sleep(tiempo);

					// Si no debe parpadear se quedará bloqueado hasta que se
					// indique
					// lo contrario
					dialogo.monitor.getParpadeo();

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

		}
	}

	private class Monitor
	{
		private boolean parpadeo = false;

		public synchronized boolean getParpadeo()
		{
			if (!parpadeo) try
			{
				wait();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return parpadeo;

		}

		public synchronized void setParpadeo(boolean b)
		{
			parpadeo = b;
			if (parpadeo) notifyAll();
		}
	}

}

class MensajesIni_this_windowAdapter extends java.awt.event.WindowAdapter
{
	DialogoSincronizacion adaptee;

	MensajesIni_this_windowAdapter( DialogoSincronizacion adaptee )
	{
		this.adaptee = adaptee;
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		adaptee.this_windowClosing(e);
	}
}
