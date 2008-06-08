package Deventos.enlaceJS;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Dialogo mostrado al realizar la sincronizacion de componentes
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DialogoSincronizacion extends JDialog
{
	private static final long serialVersionUID = 1L;

	private JPanel panel1 = new JPanel();

	private JLabel etiquetaIcono = new JLabel();

	private JLabel etiqueta = new JLabel();

	private final int x = 10;

	private final int y = 10;

	/*
	 * Secuencia de iconos mostrada en la animacion del componente
	 */
	private ImageIcon icono1 = null;

	private ImageIcon icono2 = null;

	private ImageIcon icono3 = null;

	/*
	 * Hebra para la animacion del icono
	 */
	private volatile Thread t = new Thread(new HebraAnimacion(this));

	private Monitor monitor = new Monitor();

	/**
	 * Constructor
	 */
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

	/**
	 * Inicializacion de componentes graficos
	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception
	{
		panel1.setLayout(null);
		etiquetaIcono.setFont(new java.awt.Font("Dialog", 1, 11));
		icono1 = new ImageIcon("Resources/logo_1.png");
		icono2 = new ImageIcon("Resources/logo_2.png");
		icono3 = new ImageIcon("Resources/logo_3.png");
		etiquetaIcono.setIcon(icono1);
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

	/**
	 * Muestra el dialogo
	 * 
	 * @param mensaje
	 *            Mensaje a mostrar en el dialogo
	 * @param animacion
	 *            Indica si queremos realizar la animacion del icono
	 */
	public void mostrar(String mensaje, boolean animacion)
	{
		etiqueta.setText(mensaje);
		// etiquetaIcono.setIcon(icono);
		if (!isVisible())
		{
			this.setLocation(x, y);
			this.setVisible(true);
		}

		monitor.setAnimacion(animacion);
	}

	/**
	 * Oculta el dialogo
	 */
	public void ocultar()
	{
		this.setVisible(false);
		monitor.setAnimacion(false);
	}

	/**
	 * Accion ejecutada al cerrar el dialogo
	 * 
	 * @param e
	 *            Evento recibido
	 */
	private void this_windowClosing(WindowEvent e)
	{
		DConector.obtenerDC().salir();
	}

	/**
	 * Hebra que realiza la animacion del icono
	 */
	private class HebraAnimacion implements Runnable
	{
		private DialogoSincronizacion dialogo = null;

		/**
		 * Constructor
		 * 
		 * @param dialogo
		 *            Dialog al cual realizar la animacion
		 */
		public HebraAnimacion( DialogoSincronizacion dialogo )
		{
			this.dialogo = dialogo;
		}

		/**
		 * Ejecucion de la hebra
		 */
		public void run()
		{
			while (true)
				try
				{
					dialogo.etiquetaIcono.setIcon(dialogo.icono1);
					Thread.sleep(150L);
					dialogo.etiquetaIcono.setIcon(dialogo.icono2);
					Thread.sleep(150L);
					dialogo.etiquetaIcono.setIcon(dialogo.icono3);
					Thread.sleep(150L);

					// Si no debe parpadear se quedara bloqueado hasta que se
					// indique lo contrario
					dialogo.monitor.getAnimacion();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
		}
	}

	/**
	 * Sincroniza la animacion para evitar problemas cuando el dialogo se
	 * ejecuta en mas de una instancia
	 */
	private class Monitor
	{
		private boolean animacion = false;

		/**
		 * Bloquea la hebra que llame a este metodo si no se debe realizar la
		 * animacion
		 * 
		 * @return Indica si se realiza la animacion
		 */
		public synchronized boolean getAnimacion()
		{
			if (!animacion) try
			{
				wait();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return animacion;
		}

		/**
		 * Permite indicar si debemos realizar la animacion. Desbloquea las
		 * hebras bloqueadas si se paro la animacion anteriormente
		 * 
		 * @param b
		 *            Indica si se debe realizar la animacion o no
		 */
		public synchronized void setAnimacion(boolean b)
		{
			animacion = b;
			if (animacion) notifyAll();
		}
	}

	/**
	 * Adaptador para la accion de cerrar el dialogo
	 */
	private class MensajesIni_this_windowAdapter extends
			java.awt.event.WindowAdapter
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
}
