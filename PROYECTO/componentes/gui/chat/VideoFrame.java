package componentes.gui.chat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import fisica.audio.PanelEstado;
import fisica.webcam.ImageComponent;
import fisica.webcam.VideoConferencia;

/**
 * Ventana que muestra las imagenes capturadas por la webcam.
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class VideoFrame extends JFrame
{
	private static final long serialVersionUID = 6810880621391638536L;

	private ImageComponent img_remota = new ImageComponent(this);

	private ImageComponent img_local = new ImageComponent(this);

	private boolean init = false;

	private String ip = null;

	private HebraRecepcionRemoto th = new HebraRecepcionRemoto();

	private javax.swing.JButton ini_stop;

	private javax.swing.JButton botonCaptura;

	private javax.swing.JPanel myWebcam;

	private javax.swing.JPanel webcamPanel;

	private HebraMiWebcam th2 = new HebraMiWebcam();

	private PanelEstado panelAudio = new PanelEstado();

	/**
	 * Constructor
	 * 
	 * @param ip
	 *            IP del usuario con el que queremos realizar una
	 *            videoconferencia
	 */
	public VideoFrame( String ip )
	{
		super("Videoconferencia");

		initComponents();

		this.ip = ip;
		this.webcamPanel.add(img_remota);
		this.myWebcam.add(img_local);
	}

	/**
	 * Convierte una imagen a una BufferedImage
	 * 
	 * @param im
	 *            Imagen a convertir
	 * @return Imagen convertida en una BufferedImage
	 */
	private static BufferedImage convert(Image im)
	{
		BufferedImage bi = new BufferedImage(im.getWidth(null), im
				.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics bg = bi.getGraphics();
		bg.drawImage(im, 0, 0, null);
		bg.dispose();
		return bi;
	}

	/**
	 * Inicializacion de todos los componentes graficos
	 */
	private void initComponents()
	{

		webcamPanel = new javax.swing.JPanel();
		myWebcam = new javax.swing.JPanel();
		botonCaptura = new javax.swing.JButton();
		ini_stop = new javax.swing.JButton();

		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.addWindowListener(new WindowAdapter()
		{

			@Override
			public void windowClosing(WindowEvent e)
			{
				try
				{
					init = false;
					ini_stop.setText("Iniciar");
					VideoConferencia.setStopped(true);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});

		setResizable(false);

		webcamPanel.setBackground(new java.awt.Color(102, 102, 102));

		myWebcam.setBackground(new java.awt.Color(0, 0, 0));
		myWebcam.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
		{
			@Override
			public void mouseDragged(java.awt.event.MouseEvent evt)
			{
				myWebcamMouseDragged(evt);
			}
		});

		org.jdesktop.layout.GroupLayout myWebcamLayout = new org.jdesktop.layout.GroupLayout(
				myWebcam);
		myWebcam.setLayout(myWebcamLayout);
		myWebcamLayout.setHorizontalGroup(myWebcamLayout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(0, 132,
				Short.MAX_VALUE));
		myWebcamLayout.setVerticalGroup(myWebcamLayout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(0, 102,
				Short.MAX_VALUE));

		org.jdesktop.layout.GroupLayout webcamPanelLayout = new org.jdesktop.layout.GroupLayout(
				webcamPanel);
		webcamPanel.setLayout(webcamPanelLayout);
		webcamPanelLayout.setHorizontalGroup(webcamPanelLayout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(
						webcamPanelLayout.createSequentialGroup().add(myWebcam,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(410, Short.MAX_VALUE)));
		webcamPanelLayout
				.setVerticalGroup(webcamPanelLayout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								org.jdesktop.layout.GroupLayout.TRAILING,
								webcamPanelLayout
										.createSequentialGroup()
										.addContainerGap(319, Short.MAX_VALUE)
										.add(
												myWebcam,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)));

		botonCaptura.setText("Captura");
		botonCaptura.setIcon(new ImageIcon("Resources/camera_16x16.png")); // Generated
		botonCaptura.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				botonCapturaActionPerformed(evt);
			}
		});

		ini_stop.setText("Iniciar");
		ini_stop.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				ini_stopActionPerformed(evt);
			}
		});

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);

		layout.setHorizontalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				org.jdesktop.layout.GroupLayout.TRAILING,

				layout.createSequentialGroup().add(29, 29, 29).add(ini_stop)
						.addPreferredGap(
								org.jdesktop.layout.LayoutStyle.RELATED, 299,
								Short.MAX_VALUE).add(botonCaptura).add(60, 60,
								60).add(panelAudio).add(90, 90, 90)).add(
				org.jdesktop.layout.GroupLayout.TRAILING, webcamPanel,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								layout
										.createSequentialGroup()
										.add(
												webcamPanel,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(botonCaptura).add(
																ini_stop))
										.addContainerGap(21, Short.MAX_VALUE)
										.add(panelAudio).add(90, 90, 90)));
		ini_stop.setIcon(new ImageIcon("Resources/control_play_blue.png"));

		pack();
	}

	/**
	 * Accion que se ejecuta al capturar una imagen para guardarla en un fichero
	 * 
	 * @param evt
	 *            Evento recibido
	 */
	private void botonCapturaActionPerformed(java.awt.event.ActionEvent evt)
	{
		try
		{
			BufferedImage bi = VideoFrame.convert(img_remota.getImage());

			File f = new File("captura.jpg");

			FileOutputStream flujoSalida = new FileOutputStream(f);

			javax.imageio.ImageIO.write(bi, "jpg", flujoSalida);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Accion ejecutada al iniciar o parar un envio de imagenes desde nuestra
	 * webcam
	 * 
	 * @param evt
	 *            Evento recibido
	 */
	private void ini_stopActionPerformed(java.awt.event.ActionEvent evt)
	{
		if (init)
			try
			{
				init = false;
				this.ini_stop.setText("Iniciar");

				ini_stop.setIcon(new ImageIcon(
						"Resources/control_play_blue.png"));

				VideoConferencia.setStopped(true);
				panelAudio.getPanelConexion().desconectarse();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		else try
		{
			VideoConferencia.setStopped(false);
			th = new HebraRecepcionRemoto();
			th.start();
			init = true;
			this.ini_stop.setText("Detener");
			ini_stop.setIcon(new ImageIcon("Resources/control_pause_blue.png"));
			panelAudio.getPanelConexion().conectarse(ip);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Evento de raton que permite mover la imagen que esta enviando nuestra
	 * webcam a traves de la imagen que estamos recibiendo.
	 * 
	 * @param evt
	 *            Evento de raton recibido
	 */
	private void myWebcamMouseDragged(java.awt.event.MouseEvent evt)
	{

		int x = evt.getX() + myWebcam.getX();
		int y = evt.getY() + myWebcam.getY();

		if (x < 0) x = 0;

		if (y < 0) y = 0;

		if (x + myWebcam.getWidth() > webcamPanel.getWidth())
			x = webcamPanel.getWidth() - myWebcam.getWidth();

		if (y + myWebcam.getHeight() > webcamPanel.getHeight())
			y = webcamPanel.getHeight() - myWebcam.getHeight();

		myWebcam.setLocation(x, y);
	}

	/**
	 * Hebra para la recepcion de imagenes del usuario remoto
	 */
	private class HebraRecepcionRemoto extends Thread
	{
		/**
		 * Ejecucion de la hebra
		 */
		@Override
		public void run()
		{
			VideoConferencia t = new VideoConferencia(ip);
			Image im = null;
			boolean pintado = false;
			while (true)
				if (( webcamPanel.getWidth() != 0 )
						&& ( webcamPanel.getHeight() != 0 ))
				{
					try
					{
						im = t.receive().getImage();
					}
					catch (Exception ex)
					{
						im = null;
					}

					if (im != null)
					{
						pintado = false;
						img_remota.setImage(im);
					}

					else if (!pintado && ( im == null ))
					{
						pintado = true;
						Graphics g = img_remota.getGraphics();
						g.setColor(Color.MAGENTA);
						g.drawString("Sin Conexion", img_remota.getWidth() / 3,
								img_remota.getHeight() / 2);
					}
					
					try
					{
						//dejar durmiendo 30 milisegundos hasta recibir la siguiente imagen
						Thread.sleep(30);
					}
					catch (InterruptedException e)
					{
					}
				}
		}
	}

	/**
	 * Hebra que muestra la imagen que nosotros estamos enviando al usuario con
	 * el que estamos realizando la videoconferencia.
	 */
	private class HebraMiWebcam extends Thread
	{
		/**
		 * Ejecucion de la hebra
		 */
		@Override
		public void run()
		{
			// Graphics g = myWebcam.getGraphics();
			// Image img = null;
			while (true)
				try
				{
					if (( myWebcam.getWidth() != 0 )
							&& ( myWebcam.getHeight() != 0 )
							&& ( VideoConferencia.getImageActualLocal() != null ))
						img_local
								.setImage(VideoConferencia
										.getImageActualLocal().getImage()
										.getScaledInstance(myWebcam.getWidth(),
												myWebcam.getHeight(),
												Image.SCALE_FAST));

					Thread.sleep(100);
				}
				catch (InterruptedException ex)
				{
				}
		}
	}

	/**
	 * Metodo que muestra esta ventana e inicia todas las hebras necesarias para
	 * su correcto funcionamiento.
	 */
	public void run()
	{
		setLocation(200, 200);

		th.start();
		th2.start();

		setVisible(true);
		pack();
	}
}
