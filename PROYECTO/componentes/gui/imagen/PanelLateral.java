package componentes.gui.imagen;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

public class PanelLateral extends JPanel
{

	private static final long serialVersionUID = 1L;
	private JLabel etiquetaAutor = null;
	private JLabel etiquetaRol = null;
	private JLabel etiquetaFecha = null;

	/**
	 * This is the default constructor
	 */
	public PanelLateral()
	{
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 2;
		etiquetaFecha = new JLabel();
		etiquetaFecha.setText("JLabel");
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 1;
		etiquetaRol = new JLabel();
		etiquetaRol.setText("JLabel");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		etiquetaAutor = new JLabel();
		etiquetaAutor.setText("JLabel");
		this.setSize(114, 518);
		this.setLayout(new GridBagLayout());
		this.add(etiquetaAutor, gridBagConstraints);
		this.add(etiquetaRol, gridBagConstraints1);
		this.add(etiquetaFecha, gridBagConstraints2);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
