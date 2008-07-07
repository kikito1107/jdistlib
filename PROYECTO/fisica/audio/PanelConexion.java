package fisica.audio;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Panel que permite conectarse a un chat de voz y abrir una ventana de
 * configuracion
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class PanelConexion extends JPanel
{
	private static final long serialVersionUID = 6138816409293179180L;

	private DatosGenerales datos;

	private JButton boton_configuracion;

	/**
	 * Contructor
	 * 
	 * @param datos_generales
	 *            Datos generales para la conexion
	 */
	public PanelConexion( DatosGenerales datos_generales )
	{
		datos = datos_generales;

		setLayout(new GridLayout(0, 1));

		JPanel panel = new JPanel();

		boton_configuracion = new JButton("Configuracion");
		boton_configuracion.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ee)
			{
				JFrame jframe = new JFrame("Configuracion");
				jframe.setSize(500, 350);
				jframe.setResizable(false);
				JTabbedPane tabbed = new JTabbedPane();
				tabbed.addTab("Audio", new PanelConfiguracionAudio(datos));
				tabbed.addTab("Conexion", new PanelConfiguracionTransmision(
						datos));
				jframe.add(tabbed);
				jframe.setVisible(true);
			}
		});

		panel.add(boton_configuracion);
		add(panel);

		getDatosAudio().setEscuchar(true);
	}

	private DatosGenerales getDatosGenerales()
	{
		return datos;
	}

	private DatosAudio getDatosAudio()
	{
		return getDatosGenerales().getChatModel();
	}

	public void conectarse(String ip)
	{
		getDatosAudio().conectar(ip);
	}

	public void desconectarse()
	{
		getDatosAudio().desconectar();
	}
}
