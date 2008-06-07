package aplicacion.gui.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Panel con la barra inferior del editor. Permite cambiar de pagina
 * en el documento que se este editando.
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class BarraEstado extends JPanel
{
	private static final long serialVersionUID = 1L;

	private JPanel jPanel = null;

	private JTextField jTextField = null;

	private JPanel jPanel1 = null;

	private JLabel jLabel = null;

	private JButton jButton = null;

	private JButton jButton1 = null;

	DILienzo lienzo;
	
	public BarraEstado(){
		super();
		lienzo = new DILienzo("",false, null);
		initialize();
	}

	/**
	 * Constructor
	 * @param lienzo1 Lienzo que muestra el documento
	 */
	public BarraEstado( DILienzo lienzo1 )
	{
		super();
		lienzo = lienzo1;
		initialize();
	}

	/**
	 * Inicializacion de los componentes graficos
	 */
	private void initialize()
	{
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.gridy = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridheight = -1;
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.ipadx = 562;
		gridBagConstraints1.gridx = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(562, 37);
		this.add(getJPanel(), gridBagConstraints1);
		this.add(getJPanel1(), gridBagConstraints4);

		( new Thread(new HebraActualizacionPaginas()) ).start();
	}

	private JPanel getJPanel()
	{
		if (jPanel == null)
		{
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
		}
		return jPanel;
	}

	private JTextField getJTextField()
	{
		if (jTextField == null)
		{
			jTextField = new JTextField();
			jTextField.setText(lienzo.getPaginaActual() + "");
			jTextField.setPreferredSize(new Dimension(35, 22));
			jTextField.setHorizontalAlignment(SwingConstants.CENTER);

			jTextField.addKeyListener(new java.awt.event.KeyAdapter()
			{
				@Override
				public void keyTyped(java.awt.event.KeyEvent e)
				{
					if (e.getKeyChar() == '\n') try
					{
						int numPagina = Integer.parseInt(jTextField.getText());

						if (numPagina <= lienzo.getNumPaginas())
							lienzo.setPaginaActual(numPagina);
						else jTextField.setText("" + lienzo.getPaginaActual());
					}
					catch (NumberFormatException ex)
					{
						jTextField.setText("" + lienzo.getPaginaActual());
					}
				}
			});

		}
		return jTextField;
	}

	/**
	 * Asigna el numero total de paginas del documento
	 * @param numTotal Numero total de paginas del documento
	 */
	public void setNumTotalPaginas(int numTotal)
	{
		if (( numTotal > 0 ) && ( numTotal <= lienzo.getNumPaginas() ))
			jLabel.setText("/ " + numTotal);
	}

	/**
	 * Asigna la pagina que se muestra actualmente del documento
	 * @param num Numero de pagina a mostrar 
	 */
	public void setPaginaActual(int num)
	{
		jTextField.setText("" + num);
	}

	private JPanel getJPanel1()
	{
		if (jPanel1 == null)
		{
			jLabel = new JLabel();
			jLabel.setText("/" + lienzo.getNumPaginas());
			jLabel.setForeground(Color.darkGray);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.gridx = 0;
			jPanel1 = new JPanel();
			jPanel1.setLayout(new FlowLayout());
			jPanel1.add(getBotonAnterior(), null);
			jPanel1.add(getJTextField(), null);
			jPanel1.add(jLabel, null);
			jPanel1.add(getBotonSiguiente(), null);
		}
		return jPanel1;
	}

	private JButton getBotonAnterior()
	{
		if (jButton == null)
		{

			jButton = new JButton();
			jButton.setBorder(null);
			jButton.setBorderPainted(false);
			jButton.setPreferredSize(new Dimension(30, 22));
			jButton.setIcon(new ImageIcon("Resources/resultset_previous.png"));
			jButton.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					lienzo.setPaginaActual(lienzo.getPaginaActual() - 1);
					setNumTotalPaginas(lienzo.getNumPaginas());
					setPaginaActual(lienzo.getPaginaActual());

					if (lienzo.getPaginaActual() == 1)
						jButton.setEnabled(false);
					jButton1.setEnabled(true);
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonSiguiente()
	{
		if (jButton1 == null)
		{
			jButton1 = new JButton();
			jButton1.setBorder(null);
			jButton1.setBorderPainted(false);
			jButton1.setPreferredSize(new Dimension(30, 22));
			jButton1.setIcon(new ImageIcon("Resources/resultset_next.png"));

			jButton1.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					lienzo.setPaginaActual(lienzo.getPaginaActual() + 1);
					setNumTotalPaginas(lienzo.getNumPaginas());
					setPaginaActual(lienzo.getPaginaActual());

					if (lienzo.getPaginaActual() == lienzo.getNumPaginas())
						jButton1.setEnabled(false);
					jButton.setEnabled(true);
				}
			});
		}
		return jButton1;
	}

	/**
	 * Hebra que actualiza el numero de paginas que se muestra
	 */
	private class HebraActualizacionPaginas extends Thread
	{
		/**
		 * Ejecucion de la hebra
		 */
		@Override
		public void run()
		{
			while (true)
			{
				setNumTotalPaginas(lienzo.getNumPaginas());
				setPaginaActual(lienzo.getPaginaActual());

				if (lienzo.getNumPaginas() == 1)
				{
					jButton.setEnabled(false);
					jButton1.setEnabled(false);
				}
				else if (lienzo.getPaginaActual() == 1)
				{
					jButton.setEnabled(false);
					jButton1.setEnabled(true);
				}
				else if (lienzo.getPaginaActual() == lienzo.getNumPaginas())
				{
					jButton1.setEnabled(false);
					jButton.setEnabled(true);
				}

				try
				{
					Thread.sleep(1000L);
				}
				catch (InterruptedException e)
				{
				}
			}
		}
	}

}
