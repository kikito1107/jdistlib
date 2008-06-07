package aplicacion.fisica.webcam;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.lti.civil.CaptureDeviceInfo;
import com.lti.civil.CaptureException;
import com.lti.civil.CaptureObserver;
import com.lti.civil.CaptureStream;
import com.lti.civil.CaptureSystem;
import com.lti.civil.CaptureSystemFactory;
import com.lti.civil.DefaultCaptureSystemFactorySingleton;
import com.lti.civil.awt.AWTImageConverter;

/**
 * Clase que se encarga de la captura de imagenes desde la webcam y de su envio
 * a traves de RMI al usuario con el que estamos realizando la videoconferencia.
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class VideoConferencia
{
	private String ip_origen;

	private static final int port = 4445;

	private static boolean serverExecuted = false;

	private Vector<ObjectOutputStream> salida = null;

	private static boolean stopped = true;

	@SuppressWarnings( "unused" )
	private static ImageIcon image_now = null;

	private static ImageIcon image_now_local = null;

	/**
	 * Constructor
	 * 
	 * @param ip_orig
	 *            IP del usuario con el que queremos hacer una videoconferencia
	 */
	public VideoConferencia( String ip_orig )
	{
		ip_origen = ip_orig;
	}

	/**
	 * Asigna la direccion IP del usuario con el que queremos realizar la
	 * videoconferencia.
	 * 
	 * @param ip
	 *            IP del usuario con el que queremos realizar la
	 *            videoconferencia
	 */
	public void set_ip_origen(String ip)
	{
		ip_origen = ip;

	}

	/**
	 * Permite parar la captura y envio de imagenes de la webcam.
	 * 
	 * @param b
	 *            Indica si queremos parar o no la captura y envio de imagenes
	 *            de la webcam
	 */
	public static void setStopped(boolean b)
	{
		stopped = b;
	}

	/**
	 * Permite obtener la imagen capturada por nuestra webcam
	 * 
	 * @return Imagen capturada por nuestra webcam
	 */
	public static ImageIcon getImageActualLocal()
	{
		return image_now_local;
	}

	/**
	 * Establece un servidor RMI para el envio de imagenes de la webcam.
	 */
	public static void establecerOrigen()
	{
		if (!serverExecuted)
		{
			VideoConferencia ts = new VideoConferencia("");

			ServerThread s = ts.new ServerThread();
			new Thread(s).start();

			serverExecuted = true;
		}
	}

	/**
	 * Recibe una imagen del usuario con el que estamos realizando la
	 * videoconferencia.
	 * 
	 * @return Imagen recibida del usuario
	 */
	public ImageIcon receive()
	{
		try
		{
			Socket cliente = new Socket(ip_origen, port);
			ObjectInputStream entrada = new ObjectInputStream(cliente
					.getInputStream());

			byte[] bytesImagen = (byte[]) entrada.readObject();
			ByteArrayInputStream entradaImagen = new ByteArrayInputStream(
					bytesImagen);
			BufferedImage bufferedImage = ImageIO.read(entradaImagen);

			cliente.close();

			return new ImageIcon(bufferedImage);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Permite capturar directamente desde la webcam
	 */
	class MyCaptureObserver implements CaptureObserver
	{

		public void onError(CaptureStream sender, CaptureException e)
		{
			e.printStackTrace();
		}

		public void onNewImage(CaptureStream sender, com.lti.civil.Image image)
		{
			try
			{
				BufferedImage im = AWTImageConverter.toBufferedImage(image);
				if (!stopped)
				{
					image_now = new ImageIcon(im);

					if (( salida != null ) && ( salida.size() > 0 ))
					{
						ByteArrayOutputStream salidaImagen = new ByteArrayOutputStream();
						ImageIO.write(im, "jpg", salidaImagen);
						byte[] bytesImagen = salidaImagen.toByteArray();

						for (int i = 0; i < salida.size(); i++)
							try
							{
								salida.get(i).writeObject(bytesImagen);
								salida.get(i).flush();
							}
							catch (Exception ee)
							{
							}
					}
				}

				image_now_local = new ImageIcon(im);
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Hebra que ejecuta el servidor RMI para el envio y recepcion de imagenes
	 * de una webcam.
	 */
	private class ServerThread extends Thread
	{
		/**
		 * Indica si se debe parar de capturar imagenes
		 */
		public boolean conversacionParada = true;

		private CaptureSystem system = null;

		/**
		 * Constructor
		 */
		public ServerThread()
		{
		}

		/**
		 * Ejecucion de la hebra
		 */
		@Override
		public void run()
		{
			try
			{
				// Definir un sistema de captura
				final CaptureSystemFactory factory = DefaultCaptureSystemFactorySingleton
						.instance();
				system = factory.createCaptureSystem();
				system.init();

				// establecer el servidor de sockets
				ServerSocket servidor = new ServerSocket(4445);
				Socket conexion = null;
				salida = new Vector<ObjectOutputStream>();

				// iniciar la captura desde la webcam
				CaptureStream captureStream = iniciarCaptura();

				// ejecucion del servidor
				while (true)
				{
					// solo capturar si no hemos parado la captura
					if (!stopped)
					{
						// si debemos comenzar la captura por primera vez...
						if (conversacionParada)
						{
							captureStream.start();
							conversacionParada = false;
							System.out.println("Inicializando la camara");
						}
						conexion = servidor.accept();
						salida.add(new ObjectOutputStream(conexion
								.getOutputStream()));
					}

					// si hemos parado la captura...
					else
					{

						if (!conversacionParada) captureStream.stop();
						conversacionParada = true;
						Thread.sleep(1500);
					}
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		/**
		 * Permite comenzar a capturar desde la webcam
		 * 
		 * @return Flujo de imagenes de la webcam
		 * @throws IOException
		 * @throws CaptureException
		 */
		@SuppressWarnings( "unchecked" )
		private CaptureStream iniciarCaptura() throws IOException,
				CaptureException
		{
			CaptureStream captureStream = null;
			List list = system.getCaptureDeviceInfoList();
			for (int i = 0; i < list.size(); ++i)
			{
				CaptureDeviceInfo info = (CaptureDeviceInfo) list.get(i);

				captureStream = system.openCaptureDeviceStream(info
						.getDeviceID());
				captureStream.setObserver(new MyCaptureObserver());

				break;
			}

			return captureStream;
		}
	}
}
