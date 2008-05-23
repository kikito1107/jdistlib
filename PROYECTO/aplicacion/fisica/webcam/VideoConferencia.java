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

public class VideoConferencia
{

	private String ip_origen;

	private static final int port = 4445;

	private static boolean serverExecuted = false;

	private Vector<ObjectOutputStream> salida = null;

	public static boolean stopped = true;

	public static ImageIcon image_now;

	public static ImageIcon image_now_local;

	public VideoConferencia( String ip_orig )
	{
		ip_origen = ip_orig;
	}

	public void set_ip_origen(String ip)
	{
		ip_origen = ip;

	}

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

	public ImageIcon receive(int w, int h)
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

	private class ServerThread extends Thread
	{

		public boolean conversacionParada = true;

		private CaptureSystem system = null;

		public ServerThread()
		{
		}

		@Override
		public void run()
		{
			try
			{
				final CaptureSystemFactory factory = DefaultCaptureSystemFactorySingleton
						.instance();
				system = factory.createCaptureSystem();
				system.init();
				ServerSocket servidor = new ServerSocket(4445);
				Socket conexion = null;
				salida = new Vector<ObjectOutputStream>();
				CaptureStream captureStream = iniciarCaptura();

				while (true)
					if (!stopped)
					{

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
					else
					{

						if (!conversacionParada) captureStream.stop();
						conversacionParada = true;
						Thread.sleep(1500);
					}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

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
