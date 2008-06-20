package fisica.audio;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Panel con los botones que permiten conectase a un chat de voz y abrir una
 * ventana de configuracion
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class PanelConexion extends JPanel implements ActionListener,
		PropertyChangeListener
{
	private static final long serialVersionUID = 6138816409293179180L;

	private DatosGenerales datos;

	private JButton boton_conexion;

	private JButton boton_configuracion;

	/**
	 * Contructor
	 * 
	 * @param datos_generales
	 *            Datos generales para la conexion
	 */
	public PanelConexion( DatosGenerales datos_generales)
	{
		datos = datos_generales;

		setLayout(new GridLayout(0, 1));

		JPanel panel = new JPanel();
		boton_conexion = new JButton("Comenzar");
		boton_conexion.addActionListener(this);
		boton_conexion.setActionCommand("connect");
		panel.add(boton_conexion);
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

		getDatosAudio().addPropertyChangeListener(this);
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

	public void actionPerformed(ActionEvent ae)
	{
		String comando = ae.getActionCommand();
		if (comando.equals("connect"))
		{
			String ip_conexion = JOptionPane.showInputDialog(null, "IP del otro:");
			if (ip_conexion != null)
			{
				// System.out.println(ip_conexion);
				getDatosAudio().conectar(ip_conexion);
			}
		}
	}

	/**
	 * Permite reflejar que se ha activado una conexion
	 */
	private void setConexionActiva(boolean bActive)
	{
		boton_conexion.setEnabled(!bActive);
	}

	public void propertyChange(PropertyChangeEvent e)
	{
		if (e.getPropertyName().equals(ConstantesAudio.CONNECTION_PROPERTY))
		{
			boolean conectado = ( (Boolean) e.getNewValue() ).booleanValue();
			setConexionActiva(conectado);
		}
	}
}
