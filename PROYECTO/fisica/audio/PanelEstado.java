package fisica.audio;

import java.awt.BorderLayout;

import javax.swing.JButton;
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

	private PanelConexion pane;

	public PanelEstado()
	{
		super();

		DatosGenerales datos_generales = new DatosGenerales();
		setLayout(new BorderLayout());
		pane = new PanelConexion(datos_generales);
		this.add(pane, BorderLayout.CENTER);
		etiqueta_estado = new JLabel();
		this.add(etiqueta_estado, BorderLayout.SOUTH);

		setStatusLine(" ");
	}

	public void setStatusLine(String str)
	{
		etiqueta_estado.setText(str);
	}

	public PanelConexion getPanelConexion()
	{
		return pane;
	}
	
	public JButton getBoton()
	{
		return pane.getBoton();
	}
}
