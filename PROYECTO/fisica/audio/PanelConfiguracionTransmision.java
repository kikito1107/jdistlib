package fisica.audio;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fisica.net.ConfiguracionConexion;

/**
 * Panel para la configuracion de la conexion de datos
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class PanelConfiguracionTransmision extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 3068817356174683515L;

	private DatosGenerales datos;

	private JTextField texto_puerto;

	private JComboBox combo_tipo;

	private JComboBox combo_calidad;

	public PanelConfiguracionTransmision( DatosGenerales masterModel )
	{
		datos = masterModel;
		setLayout(new BorderLayout());

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

		JPanel p = new JPanel();
		p.add(new JLabel("Puerto:"));
		texto_puerto = new JTextField(5);
		p.add(texto_puerto);
		centerPanel.add(p);

		p = new JPanel();
		p.add(new JLabel("Tipo de conexion:"));
		combo_tipo = new JComboBox(ConstantesAudio.NOMBRES_TIPOS_CONEXION);
		p.add(combo_tipo);
		centerPanel.add(p);

		p = new JPanel();
		p.add(new JLabel("Calidad de audio:"));
		combo_calidad = new JComboBox(ConstantesAudio.NOMBRES_FORMATOS);
		p.add(combo_calidad);
		centerPanel.add(p);

		add(centerPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();

		JButton applyButton = new JButton("Aplicar");
		applyButton.setActionCommand("apply");
		applyButton.addActionListener(this);
		buttonPanel.add(applyButton);
		JButton resetButton = new JButton("Resetear");
		resetButton.setActionCommand("reset");
		resetButton.addActionListener(this);
		buttonPanel.add(resetButton);

		add(buttonPanel, BorderLayout.SOUTH);

		reset();
	}

	private DatosGenerales getDatosGenerales()
	{
		return datos;
	}

	private ConfiguracionConexion getConfiguracionConexion()
	{
		return getDatosGenerales().getConnectionSettings();
	}

	private ConfiguracionAudio getConfiguracionAudio()
	{
		return getDatosGenerales().getAudioSettings();
	}

	public void actionPerformed(ActionEvent ae)
	{
		String strActionCommand = ae.getActionCommand();
		if (strActionCommand.equals("apply"))
			aplicar();
		else if (strActionCommand.equals("reset")) reset();
	}

	private void aplicar()
	{
		int port = Integer.parseInt(texto_puerto.getText());
		getConfiguracionConexion().setPuerto(port);
		int nFormatCode = ConstantesAudio.CODIGOS_FORMATO[combo_calidad.getSelectedIndex()];
		getConfiguracionAudio().setCodigoFormato(nFormatCode);
		int nConnectionType = ConstantesAudio.TIPOS_CONEXION[combo_tipo
				.getSelectedIndex()];
		getConfiguracionConexion().setTipoConexion(nConnectionType);
	}

	private void reset()
	{
		int port = getConfiguracionConexion().getPuerto();
		texto_puerto.setText("" + port);
		int nFormatCode = getConfiguracionAudio().getCodigoFormato();
		int nIndex = -1;
		for (int i = 0; i < ConstantesAudio.CODIGOS_FORMATO.length; i++)
			if (nFormatCode == ConstantesAudio.CODIGOS_FORMATO[i])
			{
				nIndex = i;
				break;
			}
		combo_calidad.setSelectedIndex(nIndex);

		int nConnectionType = getConfiguracionConexion().getTipoConexion();
		nIndex = -1;
		for (int i = 0; i < ConstantesAudio.TIPOS_CONEXION.length; i++)
			if (nConnectionType == ConstantesAudio.TIPOS_CONEXION[i])
			{
				nIndex = i;
				break;
			}
		combo_tipo.setSelectedIndex(nIndex);
	}

}
