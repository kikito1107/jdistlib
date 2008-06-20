package fisica.audio;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel con el estado de la transmision
 * 
 * @author carlosrodriguezdominguez
 */
public class PanelEstado extends JPanel
{
	private static final long serialVersionUID = 4769256494740846940L;

	private JLabel etiqueta_estado;

	private String ip_conexion;

	public PanelEstado( String ip )
	{
		super();

		ip_conexion = ip;

		DatosGenerales datos_generales = new DatosGenerales();
		setLayout(new BorderLayout());
		JComponent pane = createPane(datos_generales);
		this.add(pane, BorderLayout.CENTER);
		etiqueta_estado = new JLabel();
		this.add(etiqueta_estado, BorderLayout.SOUTH);

		setStatusLine(" ");
	}

	private JComponent createPane(DatosGenerales datos)
	{
		return new PanelConexion(datos, ip_conexion);
	}

	public void setStatusLine(String str)
	{
		etiqueta_estado.setText(str);
	}
}
