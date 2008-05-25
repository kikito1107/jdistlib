package aplicacion.plugin.example.appparalelaprimos;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import net.jini.core.entry.Entry;
import calculoparalelo.GenericMaster;
import calculoparalelo.GenericWorker;

public class PrimeMaster extends GenericMaster
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6440774130079581275L;

	private JPanel jPanel = null;

	private JTextField campoMin = null;

	private JTextField campoMax = null;

	private JTextField campoTamBloques = null;

	private JLabel etqMin = null;

	private JLabel maximo = null;

	private JLabel Tama–o = null;

	private boolean start = false;

	private JButton botonStart = null;
	
	private int  tamBloques = -1;
	int min;
	int max;

	private JPanel panelFondo = null;

	private JPanel panelREs = null;

	private JScrollPane jScrollPane = null;

	private JTextArea jTextArea = null;

	private JPanel panelEntrada = null;
	
	public PrimeMaster()
	{
		// TODO Auto-generated constructor stub
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * 
	 */
	private void initialize()
	{
		super.init();
		
		new GenericWorker();
		
		try
		{
			this.setTitle("PrimeMaster"); // Generated
			this.setSize(new Dimension(277, 307)); // Generated
				this.setContentPane(getPanelFondo());  // Generated
				this.setResizable(false);
				this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);  // Generated
		}
		catch (java.lang.Throwable e)
		{
			// Do Something
		}
	}

	@Override
	protected void collectResults()
	{
		Entry template;

		try
		{
			System.out.println("Creando Plantilla");
			template = space.snapshot(new PrimeResult());
		}
		catch (RemoteException e)
		{
			throw new RuntimeException("Can't create a snapshot");
		}

		for (;;)
		{
			String primos = "";
			boolean alguno = true;
			
			PrimeResult result = (PrimeResult) takeTask(template);
			if (result != null)
			{
				
				if (result.primos!= null) {

					alguno = false;
					
					for (int i=0; i<result.primos.length; ++i)
						if (result.primos[i] != -1) {
							primos += result.primos[i] + "   ";
							alguno = true;
						}
					
					if (alguno)
						primos += '\n';
				}
			}
			
			this.jTextArea.append(primos);
		}
			
	}

	@Override
	protected void generateTasks()
	{
		while(true){
			
			while (!start)
			{
				try
				{
					Thread.sleep(500L);
				}
				catch (InterruptedException e)
				{
				}
			}

			int superior;
			
			for (int i=min; i<=max; i+=tamBloques) {
				
				superior = i+tamBloques-1;
				
				if (superior > max )
					superior = max;
				
				System.err.println("Enviando el intervalo ["+ i +", " + superior + "]");
		
				writeTask(new PrimeTask(i, superior));
			}
			start = false;
		}
		
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel()
	{
		if (jPanel == null)
		{
			try
			{
				GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
				gridBagConstraints11.gridx = 1; // Generated
				gridBagConstraints11.gridy = 3; // Generated
				GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
				gridBagConstraints5.gridx = 0; // Generated
				gridBagConstraints5.gridy = 2; // Generated
				Tama–o = new JLabel();
				Tama–o.setText("Tama–o Intervalos    "); // Generated
				GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
				gridBagConstraints4.gridx = 0; // Generated
				gridBagConstraints4.gridy = 1; // Generated
				maximo = new JLabel();
				maximo.setText("Maximo   "); // Generated
				GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
				gridBagConstraints3.gridx = 0; // Generated
				gridBagConstraints3.gridy = 0; // Generated
				etqMin = new JLabel();
				etqMin.setText("Minimo    "); // Generated
				GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
				gridBagConstraints2.fill = GridBagConstraints.VERTICAL; // Generated
				gridBagConstraints2.gridy = 2; // Generated
				gridBagConstraints2.weightx = 1.0; // Generated
				gridBagConstraints2.gridx = 1; // Generated
				GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
				gridBagConstraints1.fill = GridBagConstraints.VERTICAL; // Generated
				gridBagConstraints1.gridy = 1; // Generated
				gridBagConstraints1.weightx = 1.0; // Generated
				gridBagConstraints1.gridx = 1; // Generated
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.fill = GridBagConstraints.VERTICAL; // Generated
				gridBagConstraints.gridy = 0; // Generated
				gridBagConstraints.weightx = 1.0; // Generated
				gridBagConstraints.gridx = 1; // Generated
				jPanel = new JPanel();
				jPanel.setLayout(new GridBagLayout()); // Generated
				jPanel.setBorder(BorderFactory.createTitledBorder(null, "Datos", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));  // Generated
				jPanel.add(getCampoMin(), gridBagConstraints); // Generated
				jPanel.add(getCampoMax(), gridBagConstraints1); // Generated
				jPanel.add(getCampoTamBloques(), gridBagConstraints2); // Generated
				jPanel.add(etqMin, gridBagConstraints3); // Generated
				jPanel.add(maximo, gridBagConstraints4); // Generated
				jPanel.add(Tama–o, gridBagConstraints5); // Generated
				//jPanel.add(getBotonStart(), gridBagConstraints11); // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return jPanel;
	}

	/**
	 * This method initializes campoMin
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCampoMin()
	{
		if (campoMin == null)
		{
			try
			{
				campoMin = new JTextField();
				campoMin.setPreferredSize(new Dimension(120, 28));  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return campoMin;
	}

	/**
	 * This method initializes campoMax
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCampoMax()
	{
		if (campoMax == null)
		{
			try
			{
				campoMax = new JTextField();
				campoMax.setPreferredSize(new Dimension(120, 28));  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return campoMax;
	}

	/**
	 * This method initializes campoTamBloques
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCampoTamBloques()
	{
		if (campoTamBloques == null)
		{
			try
			{
				campoTamBloques = new JTextField();
				campoTamBloques.setPreferredSize(new Dimension(120, 28));  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return campoTamBloques;
	}

	/**
	 * This method initializes botonStart
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonStart()
	{
		if (botonStart == null)
		{
			try
			{
				botonStart = new JButton();
				botonStart.setText("Start!"); // Generated
				botonStart
						.addActionListener(new java.awt.event.ActionListener()
						{
							public void actionPerformed(
									java.awt.event.ActionEvent e)
							{
								int inferior = -1, superior, tam = -1;
								try
								{
									superior = Integer.parseInt(getCampoMax()
											.getText());
									inferior = Integer.parseInt(getCampoMin()
											.getText());
									tam = Integer.parseInt(getCampoTamBloques()
											.getText());
								}
								catch (NumberFormatException e1)
								{
									error();
									return;
								}
								
								if (inferior >= superior) {
									error();
									return;
								}
								
								tamBloques = tam;
								min = inferior;
								max = superior;
								
								start = true;
								
								jTextArea.setText("");
									
							}
							
							private void error(){
								JOptionPane.showMessageDialog(null, "Datos incorrectos");
							}
						});
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return botonStart;
	}

	/**
	 * This method initializes panelFondo	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelFondo()
	{
		if (panelFondo == null)
		{
			try
			{
				BorderLayout borderLayout = new BorderLayout();
				borderLayout.setHgap(4);  // Generated
				borderLayout.setVgap(4);  // Generated
				panelFondo = new JPanel();
				panelFondo.setLayout(borderLayout);  // Generated
				panelFondo.add(getJPanel(), BorderLayout.NORTH);  // Generated
				panelFondo.add(getPanelEntrada(), BorderLayout.SOUTH);  // Generated
				panelFondo.add(getPanelREs(), BorderLayout.CENTER);  // Generated
				//panelFondo.add(getJPanel(), null);  // Generated
				
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelFondo;
	}

	/**
	 * This method initializes panelREs	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelREs()
	{
		if (panelREs == null)
		{
			try
			{
				GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
				gridBagConstraints6.fill = GridBagConstraints.BOTH;  // Generated
				gridBagConstraints6.gridy = 0;  // Generated
				gridBagConstraints6.weightx = 1.0;  // Generated
				gridBagConstraints6.weighty = 1.0;  // Generated
				gridBagConstraints6.gridx = 0;  // Generated
				panelREs = new JPanel();
				panelREs.setLayout(new GridBagLayout());  // Generated
				panelREs.setBorder(BorderFactory.createTitledBorder(null, "Resultados", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));  // Generated
				panelREs.add(getJScrollPane(), gridBagConstraints6);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelREs;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane()
	{
		if (jScrollPane == null)
		{
			try
			{
				jScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
				jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);  // Generated
				jScrollPane.setViewportView(getJTextArea());  // Generated
				jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea()
	{
		if (jTextArea == null)
		{
			try
			{
				jTextArea = new JTextArea();
				jTextArea.setWrapStyleWord(true);  // Generated
				jTextArea.setEnabled(false);  // Generated
				jTextArea.setEditable(false);  // Generated
				jTextArea.setLineWrap(true);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return jTextArea;
	}

	/**
	 * This method initializes panelEntrada	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelEntrada()
	{
		if (panelEntrada == null)
		{
			try
			{
				GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
				gridBagConstraints8.gridx = 0;  // Generated
				gridBagConstraints8.gridy = 0;  // Generated
				GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
				gridBagConstraints7.gridx = 0;  // Generated
				gridBagConstraints7.gridy = 0;  // Generated
				panelEntrada = new JPanel();
				panelEntrada.setLayout(new GridBagLayout());  // Generated
				panelEntrada.add(getBotonStart(), gridBagConstraints8);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO				panelEntrada.add(getJPanel(), gridBagConstraints7);  // Generated Something
			}
		}
		return panelEntrada;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		
		new PrimeMaster().setVisible(true);

	}

} //  @jve:decl-index=0:visual-constraint="10,10"
