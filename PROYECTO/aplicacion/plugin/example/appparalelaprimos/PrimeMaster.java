package aplicacion.plugin.example.appparalelaprimos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import net.jini.core.entry.Entry;
import net.jini.space.JavaSpace;
import aplicacion.plugin.DAbstractPlugin;
import calculoparalelo.GenericMaster;
import calculoparalelo.GenericWorker;
import calculoparalelo.eventos.PoisonPill;

import componentes.base.DJFrame;

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

	private JPanel panelRes = null;

	private JScrollPane jScrollPane = null;

	private JTextArea jTextArea = null;

	private JPanel panelEntrada = null;

	private JTabbedPane tabs = null;

	private JPanel panelEsclavo = null;

	private JPanel panelTiempo = null;

	private JLabel etiquTiempo = null;

	private JLabel tiempo = null;

	private JButton botonIniciarEsclavo = null;
	
	int tamProblema = 0;
	
	int respuestasRecibidas = 0;
	
	Calendar inicio = null;
	Calendar fin = null;
	
	private DJFrame ventana = null;
	
	private boolean esclavoIniciado = false;

	private JButton verDatosEnviados = null;

	private JLabel etiqueta = null;

	private JFrame ventanaConsola = null;  //  @jve:decl-index=0:visual-constraint="18,445"

	private JPanel panelConsola = null;

	private JScrollPane scrollConsola = null;

	private JTextArea consola = null;

	private JButton limpiar = null;

	private JScrollPane scrollSalidaEsclavo = null;

	private JTextArea consolaEsclavo = null;
	
	private boolean standalone = false;

	private String lan = Messages.EN;

	public PrimeMaster(boolean sa) throws Exception
	{	
		super("PluginPrimos", false, null);
		standalone = sa;
		Messages.setLan(lan);
		init();
	}

	/**
	 * This method initializes this
	 * 
	 * 
	 */
	public void init()
	{
		super.init();
		
		version = 2;
		nombre = Messages.getString("PrimeMaster.1"); //$NON-NLS-1$
		jarFile = "primos.jar";
		
		try
		{
			ventana = new DJFrame(false, "");
			
			//ventana.setLocation(null);
			
			ventana.setTitle(Messages.getString("PrimeMaster.4")); // Generated //$NON-NLS-1$
			ventana.setPreferredSize(new Dimension(350, 360));  // Generated
			ventana.setContentPane(getTabs());  // Generated
			ventana.setSize(new Dimension(322, 396)); // Generated
			ventana.setResizable(false);
			if (standalone)
				ventana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			else
				ventana.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
			System.out.println(Messages.getString("PrimeMaster.5")); //$NON-NLS-1$
			template = space.snapshot(new PrimeResult());
		}
		catch (RemoteException e)
		{
			throw new RuntimeException(Messages.getString("PrimeMaster.6")); //$NON-NLS-1$
		}

		for (;;)
		{
			String primos = "";
			boolean alguno = true;
			
			PrimeResult result = (PrimeResult) takeTask(template);
			if (result != null)
			{
				
				this.respuestasRecibidas++;
				
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
			
			if (respuestasRecibidas == tamProblema) {
				fin = new GregorianCalendar();
				
				long diff = fin.getTimeInMillis() - inicio.getTimeInMillis();
				
				Calendar diferencia = new GregorianCalendar();
				diferencia.setTimeInMillis(diff);
				
				tiempo.setText(diferencia.get(Calendar.MINUTE) + ":" + 
						diferencia.get(Calendar.SECOND) + ":" + 
						diferencia.get(Calendar.MILLISECOND));
				
				tamProblema = 0;
				respuestasRecibidas = 0;
				
			}
				
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
				
				getConsola().append(Messages.getString("PrimeMaster.11")+ i +", " + superior + "]\n"); //$NON-NLS-1$
		
				tamProblema++;
				
				writeTask(new PrimeTask(i, superior));
			}
			
			inicio = new GregorianCalendar();
			
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
				GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
				gridBagConstraints21.gridx = 0;  // Generated
				gridBagConstraints21.gridy = 3;  // Generated
				etiqueta = new JLabel();
				etiqueta.setText(Messages.getString("PrimeMaster.14"));  // Generated //$NON-NLS-1$
				GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
				gridBagConstraints14.gridx = 1;  // Generated
				gridBagConstraints14.gridy = 3;  // Generated
				GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
				gridBagConstraints11.gridx = 1; // Generated
				gridBagConstraints11.gridy = 3; // Generated
				GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
				gridBagConstraints5.gridx = 0; // Generated
				gridBagConstraints5.gridy = 2; // Generated
				Tama–o = new JLabel();
				Tama–o.setText(Messages.getString("PrimeMaster.15")); // Generated //$NON-NLS-1$
				GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
				gridBagConstraints4.gridx = 0; // Generated
				gridBagConstraints4.gridy = 1; // Generated
				maximo = new JLabel();
				maximo.setText(Messages.getString("PrimeMaster.16")); // Generated //$NON-NLS-1$
				GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
				gridBagConstraints3.gridx = 0; // Generated
				gridBagConstraints3.gridy = 0; // Generated
				etqMin = new JLabel();
				etqMin.setText(Messages.getString("PrimeMaster.17")); // Generated //$NON-NLS-1$
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
				jPanel.setBorder(BorderFactory.createTitledBorder(null, Messages.getString("PrimeMaster.18"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));  // Generated //$NON-NLS-1$
				jPanel.add(getCampoMin(), gridBagConstraints); // Generated
				jPanel.add(getCampoMax(), gridBagConstraints1); // Generated
				jPanel.add(getCampoTamBloques(), gridBagConstraints2); // Generated
				jPanel.add(etqMin, gridBagConstraints3); // Generated
				jPanel.add(maximo, gridBagConstraints4); // Generated
				jPanel.add(Tama–o, gridBagConstraints5); // Generated
				jPanel.add(getVerDatosEnviados(), gridBagConstraints14);  // Generated
				jPanel.add(etiqueta, gridBagConstraints21);  // Generated
				//jPanel.add(getBotonStart(), gridBagConstraints11); // Generated
			}
			catch (java.lang.Throwable e)
			{
				
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
				campoMin.setHorizontalAlignment(JTextField.RIGHT);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				
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
				campoMax.setHorizontalAlignment(JTextField.RIGHT);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				
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
				campoTamBloques.setHorizontalAlignment(JTextField.RIGHT);  // Generated
			}
			catch (java.lang.Throwable e)
			{
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
				botonStart.setText(Messages.getString("PrimeMaster.19")); // Generated //$NON-NLS-1$
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
								
								tiempo.setText("0:00:00");
								
								tamProblema = 0;
								respuestasRecibidas = 0;
								
								jTextArea.setText("");
									
							}
							
							private void error(){
								JOptionPane.showMessageDialog(null, Messages.getString("PrimeMaster.22")); //$NON-NLS-1$
							}
						});
			}
			catch (java.lang.Throwable e)
			{
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
		if (panelRes == null)
		{
			try
			{
				GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
				gridBagConstraints12.gridx = 0;  // Generated
				gridBagConstraints12.gridy = 0;  // Generated
				GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
				gridBagConstraints6.fill = GridBagConstraints.BOTH;  // Generated
				gridBagConstraints6.gridy = 1;  // Generated
				gridBagConstraints6.weightx = 1.0;  // Generated
				gridBagConstraints6.weighty = 1.0;  // Generated
				gridBagConstraints6.gridx = 0;  // Generated
				panelRes = new JPanel();
				panelRes.setLayout(new GridBagLayout());  // Generated
				panelRes.setBorder(BorderFactory.createTitledBorder(null, Messages.getString("PrimeMaster.23"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));  // Generated //$NON-NLS-1$
				panelRes.add(getJScrollPane(), gridBagConstraints6);  // Generated
				panelRes.add(getPanelTiempo(), gridBagConstraints12);  // Generated
			}
			catch (java.lang.Throwable e)
			{
			}
		}
		return panelRes;
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
			}
		}
		return panelEntrada;
	}

	/**
	 * This method initializes tabs	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getTabs()
	{
		if (tabs == null)
		{
			try
			{
				tabs = new JTabbedPane();
				tabs.addTab(Messages.getString("PrimeMaster.24"), null, getPanelFondo(), null);  // Generated //$NON-NLS-1$
				tabs.addTab(Messages.getString("PrimeMaster.25"), null, getPanelEsclavo(), null);  // Generated //$NON-NLS-1$
			}
			catch (java.lang.Throwable e)
			{
				
			}
		}
		return tabs;
	}

	/**
	 * This method initializes panelEsclavo	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelEsclavo()
	{
		if (panelEsclavo == null)
		{
			try
			{
				GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
				gridBagConstraints15.fill = GridBagConstraints.BOTH;  // Generated
				gridBagConstraints15.gridy = 0;  // Generated
				gridBagConstraints15.weightx = 1.0;  // Generated
				gridBagConstraints15.weighty = 1.0;  // Generated
				gridBagConstraints15.gridx = 0;  // Generated
				GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
				gridBagConstraints13.gridx = 0;  // Generated
				gridBagConstraints13.gridy = 1;  // Generated
				panelEsclavo = new JPanel();
				panelEsclavo.setLayout(new GridBagLayout());  // Generated
				panelEsclavo.add(getBotonIniciarEsclavo(), gridBagConstraints13);  // Generated
				panelEsclavo.add(getScrollSalidaEsclavo(), gridBagConstraints15);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				
			}
		}
		return panelEsclavo;
	}
	
	private  synchronized void setEsclavoIniciado(boolean b){
		this.esclavoIniciado = b;
	}
	
	private synchronized boolean getEsclavoIniciado(){
		return esclavoIniciado;
	}

	/**
	 * This method initializes panelTiempo	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelTiempo()
	{
		if (panelTiempo == null)
		{
			try
			{
				GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
				gridBagConstraints10.gridx = 1;  // Generated
				gridBagConstraints10.ipadx = 20;  // Generated
				gridBagConstraints10.gridy = 0;  // Generated
				tiempo = new JLabel();
				tiempo.setText("0:00:000");  // Generated
				GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
				gridBagConstraints9.gridx = 0;  // Generated
				gridBagConstraints9.ipadx = 17;  // Generated
				gridBagConstraints9.ipady = 10;  // Generated
				gridBagConstraints9.gridy = 0;  // Generated
				etiquTiempo = new JLabel();
				etiquTiempo.setText(Messages.getString("PrimeMaster.27"));  // Generated //$NON-NLS-1$
				panelTiempo = new JPanel();
				panelTiempo.setLayout(new GridBagLayout());  // Generated
				panelTiempo.add(etiquTiempo, gridBagConstraints9);  // Generated
				panelTiempo.add(tiempo, gridBagConstraints10);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				
			}
		}
		return panelTiempo;
	}

	/**
	 * This method initializes botonIniciarEsclavo	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBotonIniciarEsclavo()
	{
		if (botonIniciarEsclavo == null)
		{
			try
			{
				botonIniciarEsclavo = new JButton();
				botonIniciarEsclavo.setText(Messages.getString("PrimeMaster.28"));  // Generated //$NON-NLS-1$
				botonIniciarEsclavo.setIcon(new ImageIcon(getClass().getResource("/Resources/control_play_blue.png")));  // Generated
				botonIniciarEsclavo.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent e)
					{
						if ( !getEsclavoIniciado()) {
							
							PoisonPill template = new PoisonPill();
							try
							{
								space.take(template, null, JavaSpace.NO_WAIT);
							}
							catch (Exception exc)
							{
								; // continue on
							}
							
							esclavo = new GenericWorker(getConsolaEsclavo());
							botonIniciarEsclavo.setText(Messages.getString("PrimeMaster.30"));  // Generated //$NON-NLS-1$
							botonIniciarEsclavo.setIcon(new ImageIcon(getClass().getResource("/Resources/control_stop_blue.png")));
							setEsclavoIniciado(true);
						}
						else {
							PoisonPill poison = new PoisonPill();

							try
							{
								space.write(poison, null, 60 * 1000 * 5);
								botonIniciarEsclavo.setText(Messages.getString("PrimeMaster.32"));  // Generated //$NON-NLS-1$
								botonIniciarEsclavo.setIcon(new ImageIcon(getClass().getResource("/Resources/control_play_blue.png")));
								setEsclavoIniciado(false);
							}
							catch (Exception ex)
							{
								System.out.println(Messages.getString("PrimeMaster.34")); //$NON-NLS-1$
							}
						}
					}
				});
			}
			catch (java.lang.Throwable e)
			{
				
			}
		}
		return botonIniciarEsclavo;
	}

	@Override
	public DAbstractPlugin getInstance() throws Exception
	{
		return new PrimeMaster(false);
	}

	@Override
	public void start() throws Exception
	{
		ventana.setVisible(true);
	}

	@Override
	public void stop() throws Exception
	{
		ventana.setVisible(false);
		ventana.dispose();
	}
	
	/**
	 * This method initializes verDatosEnviados	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getVerDatosEnviados()
	{
		if (verDatosEnviados == null)
		{
			try
			{
				verDatosEnviados = new JButton();
				verDatosEnviados.setIcon(new ImageIcon(getClass().getResource("/Resources/terminal.png")));  // Generated
				verDatosEnviados.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent e)
					{
						getVentanaConsola().setVisible(true);
					}
				});
			}
			catch (java.lang.Throwable e)
			{
				
			}
		}
		return verDatosEnviados;
	}

	/**
	 * This method initializes ventanaConsola	
	 * 	
	 * @return javax.swing.JFrame	
	 */
	private JFrame getVentanaConsola()
	{
		if (ventanaConsola == null)
		{
			try
			{
				ventanaConsola = new JFrame();
				ventanaConsola.setSize(new Dimension(272, 272));  // Generated
				ventanaConsola.setTitle(Messages.getString("PrimeMaster.36"));  // Generated //$NON-NLS-1$
				ventanaConsola.setContentPane(getPanelConsola());  // Generated
				//ventanaConsola.setLocation(null);
			}
			catch (java.lang.Throwable e)
			{
				
			}
		}
		return ventanaConsola;
	}

	/**
	 * This method initializes panelConsola	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelConsola()
	{
		if (panelConsola == null)
		{
			try
			{
				panelConsola = new JPanel();
				panelConsola.setLayout(new BorderLayout());  // Generated
				panelConsola.add(getLimpiar(), BorderLayout.SOUTH);  // Generated
				panelConsola.add(getScrollConsola(), BorderLayout.CENTER);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				
			}
		}
		return panelConsola;
	}

	/**
	 * This method initializes scrollConsola	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollConsola()
	{
		if (scrollConsola == null)
		{
			try
			{
				scrollConsola = new JScrollPane();
				scrollConsola.setBackground(Color.black);  // Generated
				scrollConsola.setViewportView(getConsola());  // Generated
				scrollConsola.setForeground(Color.white);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				
			}
		}
		return scrollConsola;
	}

	/**
	 * This method initializes consola	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getConsola()
	{
		if (consola == null)
		{
			try
			{
				consola = new JTextArea();
				consola.setBackground(Color.black);  // Generated
				consola.setForeground(Color.green);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				
			}
		}
		return consola;
	}

	/**
	 * This method initializes limpiar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getLimpiar()
	{
		if (limpiar == null)
		{
			try
			{
				limpiar = new JButton();
				limpiar.setText(Messages.getString("PrimeMaster.37"));  // Generated //$NON-NLS-1$
				limpiar.setIcon(new ImageIcon(getClass().getResource("/Resources/edit-clear_16x16.png")));  // Generated
				limpiar.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent e)
					{
						consola.setText("");
					}
				});
			}
			catch (java.lang.Throwable e)
			{
				
			}
		}
		return limpiar;
	}

	/**
	 * This method initializes scrollSalidaEsclavo	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollSalidaEsclavo()
	{
		if (scrollSalidaEsclavo == null)
		{
			try
			{
				scrollSalidaEsclavo = new JScrollPane();
				scrollSalidaEsclavo.setViewportView(getConsolaEsclavo());  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return scrollSalidaEsclavo;
	}

	/**
	 * This method initializes consolaEsclavo	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getConsolaEsclavo()
	{
		if (consolaEsclavo == null)
		{
			try
			{
				consolaEsclavo = new JTextArea();
				consolaEsclavo.setBackground(Color.black);  // Generated
				consolaEsclavo.setForeground(Color.green);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return consolaEsclavo;
	}

	public static void main(String[] args){
		try
		{
			new PrimeMaster(true).start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

} //  @jve:decl-index=0:visual-constraint="10,10"
