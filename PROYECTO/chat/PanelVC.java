package chat;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

public class PanelVC extends JPanel
{

	private static final long serialVersionUID = 1L;
	private JLabel Lable = null;

	/**
	 * This is the default constructor
	 */
	public PanelVC()
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
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		Lable = new JLabel();
		Lable.setText("Panel Video Conferencia");
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(Lable, gridBagConstraints);
	}

}
