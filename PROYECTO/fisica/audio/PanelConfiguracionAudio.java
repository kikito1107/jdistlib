package fisica.audio;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Port;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Panel para la configuracion del audio
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class PanelConfiguracionAudio extends JPanel implements ActionListener,
		ItemListener, ChangeListener, PropertyChangeListener
{
	private static final long serialVersionUID = -1211093879092247548L;

	private JButton boton_desconectar;

	private JButton boton_test;

	private JComboBox[] selector_mezclador = new JComboBox[2];

	private JProgressBar[] medidor_volumen = new JProgressBar[2];

	private JSlider[] desp_volumen = new JSlider[2];

	private JComboBox[] puerto_volumen = new JComboBox[2];

	private JCheckBox[] boton_silenciar = new JCheckBox[2];

	private JComboBox[] seleccion_buffer = new JComboBox[2];

	private DatosGenerales datos;

	public PanelConfiguracionAudio( DatosGenerales datos_generales )
	{
		this.datos = datos_generales;
		setLayout(new GridLayout(1, 2));

		createGUI(0, "Microfono");
		createGUI(1, "Altavoz");

		getChatModel().addPropertyChangeListener(this);
		enableButtons();
	}

	private void createGUI(int d, String title)
	{
		JPanel p = new JPanel();
		p.setLayout(new StripeLayout(0, 2, 0, 2, 3));
		p.add(new JLabel(title));

		p.add(new JLabel("Mezclador:"));
		selector_mezclador[d] = new JComboBox(getAudioSettings()
				.getNombresMezcladores(d).toArray());
		selector_mezclador[d].addItemListener(this);
		p.add(selector_mezclador[d]);

		p.add(new JLabel("Nivel:"));
		medidor_volumen[d] = new JProgressBar(SwingConstants.HORIZONTAL, 0, 128);
		p.add(medidor_volumen[d]);

		desp_volumen[d] = new JSlider(0, 100, 100);
		desp_volumen[d].addChangeListener(this);
		p.add(desp_volumen[d]);

		puerto_volumen[d] = new JComboBox(getAudioSettings().getPuertos(d)
				.toArray());
		puerto_volumen[d].addItemListener(this);
		p.add(puerto_volumen[d]);

		boton_silenciar[d] = new JCheckBox("Silencio");
		boton_silenciar[d].addItemListener(this);
		p.add(boton_silenciar[d]);

		p.add(new JLabel("Tamaño del buffer en milisegundos:"));
		seleccion_buffer[d] = new JComboBox(
				ConstantesAudio.BUFFER_SIZE_MILLIS_STR);
		seleccion_buffer[d].setSelectedIndex(getAudioSettings()
				.getIndiceTamBuffer(d));
		seleccion_buffer[d].addItemListener(this);
		p.add(seleccion_buffer[d]);

		if (d == ConstantesAudio.DIR_SPK)
		{
			boton_desconectar = new JButton("Desconectar");
			boton_desconectar.addActionListener(this);
			p.add(boton_desconectar);
		}
		else
		{
			boton_test = new JButton("Iniciar test de microfono");
			boton_test.addActionListener(this);
			p.add(boton_test);
		}
		add(p);
		getAudioSettings().setPuertoSeleccionado(d, 0);
		initNewPort(d);
	}

	private DatosAudio getChatModel()
	{
		return datos.getChatModel();
	}

	private ConfiguracionAudio getAudioSettings()
	{
		return datos.getAudioSettings();
	}

	private AudioBase getAudio(int d)
	{
		return getChatModel().getAudio(d);
	}

	private void initNewPort(int d)
	{
		Port p = getAudioSettings().getPuertoSeleccionado(d);
		FloatControl c = getAudioSettings().getControlVolumenSeleccionado(d);
		desp_volumen[d].setEnabled(( p != null ) && ( c != null ));
		updateVolumeSlider(d);
	}

	private void updateVolumeSlider(int d)
	{
		FloatControl c = getAudioSettings().getControlVolumenSeleccionado(d);
		if (( c != null ) && desp_volumen[d].isEnabled())
		{
			int newPos = (int) ( ( ( c.getValue() - c.getMinimum() ) / ( c
					.getMaximum() - c.getMinimum() ) ) * 100.0f );
			if (newPos != desp_volumen[d].getValue())
				desp_volumen[d].setValue(newPos);
		}
	}

	private void updateVolume(int d)
	{
		FloatControl c = getAudioSettings().getControlVolumenSeleccionado(d);
		if (( c != null ) && desp_volumen[d].isEnabled())
		{

			float newVol = ( ( desp_volumen[d].getValue() / 100.0f ) * ( c
					.getMaximum() - c.getMinimum() ) )
					+ c.getMinimum();
			c.setValue(newVol);
		}
	}

	private void initNewMixer(int d)
	{
		try
		{
			getAudio(d)
					.setMixer(getAudioSettings().getMezcladorSeleccionado(d));
			if (d == ConstantesAudio.DIR_MIC)
				getChatModel().inicializarAudioStream();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void initNewBufferSize(int d)
	{
		try
		{
			getAudio(d).setBufferSizeMillis(
					getAudioSettings().getTamBufferMilisegundos(d));
			if (d == ConstantesAudio.DIR_MIC)
				getChatModel().inicializarAudioStream();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void itemStateChanged(ItemEvent e)
	{
		int d = -1;
		if (e.getSource() == puerto_volumen[0])
			d = 0;
		else if (e.getSource() == puerto_volumen[1]) d = 1;
		if (( d >= 0 ) && ( e.getStateChange() == ItemEvent.SELECTED ))
		{
			getAudioSettings().setPuertoSeleccionado(d,
					puerto_volumen[d].getSelectedIndex());
			initNewPort(d);
			return;
		}
		d = -1;
		if (e.getSource() == selector_mezclador[0])
			d = 0;
		else if (e.getSource() == selector_mezclador[1]) d = 1;
		if (( d >= 0 ) && ( e.getStateChange() == ItemEvent.SELECTED ))
		{
			getAudioSettings().setMezcladorSeleccionado(d,
					selector_mezclador[d].getSelectedIndex());
			initNewMixer(d);
			return;
		}
		d = -1;
		if (e.getSource() == seleccion_buffer[0])
			d = 0;
		else if (e.getSource() == seleccion_buffer[1]) d = 1;
		if (( d >= 0 ) && ( e.getStateChange() == ItemEvent.SELECTED ))
		{
			getAudioSettings().setIndiceTamBuffer(d,
					seleccion_buffer[d].getSelectedIndex());
			initNewBufferSize(d);
			return;
		}

		d = -1;
		if (e.getSource() == boton_silenciar[0])
			d = 0;
		else if (e.getSource() == boton_silenciar[1]) d = 1;
		if (( d >= 0 ) && ( getAudio(d) != null ))
			getAudio(d).setSilenciado(boton_silenciar[d].isSelected());
	}

	public void stateChanged(ChangeEvent e)
	{
		int d;
		if (e.getSource() == desp_volumen[0])
			d = 0;
		else if (e.getSource() == desp_volumen[1])
			d = 1;
		else return;
		updateVolume(d);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == boton_test)
			getChatModel().intercambiarTestAudio();
		else if (e.getSource() == boton_desconectar)
			getChatModel().desconectar();

	}

	private void enableButtons()
	{
		if (getChatModel().estaConectado())
		{
			boton_desconectar.setEnabled(true);
			boton_test.setEnabled(false);
			boton_test.setText("Iniciar test de microfono");
		}
		else
		{
			boton_desconectar.setEnabled(false);
			boton_test.setEnabled(true);
			if (getChatModel().audioActivado())
				boton_test.setText("Parar test de microfono");
			else boton_test.setText("Iniciar test de microfono");
		}
	}

	public void propertyChange(PropertyChangeEvent e)
	{
		if (e.getPropertyName().equals(ConstantesAudio.AUDIO_PROPERTY))
			if (getChatModel().audioActivado()) startLevelMeterThread();
		enableButtons();
	}

	private void startLevelMeterThread()
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					while (getChatModel().audioActivado())
					{
						for (int d = 0; d < 2; d++)
						{
							AudioBase ab = getAudio(d);
							if (ab != null)
							{
								int level = ab.getLevel();
								if (level >= 0)
									medidor_volumen[d].setValue(level);
								else medidor_volumen[d].setValue(0);

							}
						}
						Thread.sleep(30);
					}

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				medidor_volumen[0].setValue(0);
				medidor_volumen[1].setValue(0);
			}
		}).start();
	}

}
