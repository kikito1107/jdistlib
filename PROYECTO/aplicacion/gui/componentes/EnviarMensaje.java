package aplicacion.gui.componentes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import metainformacion.ClienteMetaInformacion;
import metainformacion.MIRol;
import metainformacion.MIUsuario;
import Deventos.enlaceJS.DConector;
import aplicacion.fisica.ClienteFicheros;
import aplicacion.fisica.documentos.MIFichero;

public class EnviarMensaje extends JDialog
{

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel panelSur = null;

	private JButton Enviar = null;

	private JButton Cancelar = null;

	private JPanel panelCentro = null;

	private JPanel panelOeste = null;

	private JScrollPane jScrollPane = null;

	private JTextArea area = null;

	private JList arbol = null;

	private JPanel panelNorte = null;

	private JTextField destinatario = null;

	private JPanel panelCentral = null;
	
	private String dest = null;

	private JPanel panelDest = null;

	private JLabel jLabel = null;

	private JPanel panelAsunto = null;

	private JTextField asunto = null;

	private JLabel label = null;
	
	private MIFichero mif = null;
	
	private String asunt = "";

	/**
	 * This is the default constructor
	 */
	public EnviarMensaje(String destin, String asunto, String mensaje)
	{
		super();
		
		dest = destin;
		
		asunt = asunto;
		
		initialize();
		
		area.setText(mensaje);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		
		this.setSize(638, 452);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		setLocation(( screenSize.width - frameSize.width ) / 2,
				( screenSize.height - frameSize.height ) / 2);
		this.setContentPane(getJContentPane());
		this.setTitle(".:: Enviar  Nota ::.");
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			@Override
			public void windowClosing(java.awt.event.WindowEvent e)
			{
				int elecc = JOptionPane.showConfirmDialog(null, "El mensaje no ha sido enviado todavía\n¿Realmente desea salir?", "¡Atención!", JOptionPane.YES_NO_OPTION);
				
				if (elecc == 0)
					setVisible(false);
			}
		});
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getPanelSur(), BorderLayout.SOUTH);  // Generated
			jContentPane.add(getPanelOeste(), BorderLayout.WEST);  // Generated
			jContentPane.add(getPanelCentral(), BorderLayout.CENTER);  // Generated
		}
		return jContentPane;
	}

	/**
	 * This method initializes panelSur	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelSur()
	{
		if (panelSur == null)
		{
			try
			{
				GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
				gridBagConstraints1.gridx = 1;  // Generated
				gridBagConstraints1.gridy = 0;  // Generated
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 0;  // Generated
				gridBagConstraints.gridy = 0;  // Generated
				panelSur = new JPanel();
				panelSur.setLayout(new GridBagLayout());  // Generated
				panelSur.add(getEnviar(), gridBagConstraints);  // Generated
				panelSur.add(getCancelar(), gridBagConstraints1);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelSur;
	}

	/**
	 * This method initializes Enviar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getEnviar()
	{
		if (Enviar == null)
		{
			try
			{
				Enviar = new JButton();
				Enviar.setText("Enviar");  // Generated
				Enviar.setIcon(new ImageIcon("Resources/icon_email.gif"));  // Generated
				Enviar.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent e)
					{
						if (destinatario.getText().equals("") || asunto.getText().equals("") || area.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "¡Quedan campos sin rellenar!");
							return;
						}
							
						
						mif = new MIFichero();
						MIFichero buzon = ClienteFicheros.obtenerClienteFicheros().existeFichero("/Incoming", DConector.Daplicacion);
						
						if (buzon == null) {
							JOptionPane.showMessageDialog(null, "Buzon no encontrado");
							 setVisible(false);
							 mif = null;
							return;
						}
						mif.setNombre(getAsunto().getText()+"."+MIFichero.TIPO_MENSAJE);
						mif.setTipo(MIFichero.TIPO_MENSAJE);
						mif.setRutaLocal("/Incoming/" + mif.toString()+"."+MIFichero.TIPO_MENSAJE);
						mif.setId(-1);
						mif.setPermisos("rw----");
						
						mif.setPadre(buzon.getId());
						
						MIUsuario destino = ClienteMetaInformacion.obtenerCMI().obtenerDatosUsuario(destinatario.getText());//.getUsuario();
						
						
						if (destino == null) {
							JOptionPane.showMessageDialog(null, "Usuario no existente:  " + arbol.getSelectedValue().toString());
							 setVisible(false);
							 mif = null;
							return;
						}
						MIRol rol = ClienteMetaInformacion.obtenerCMI().getRol(destino.getRolPorDefecto());
						mif.setUsuario( destino  );
						mif.setRol( rol );
						mif.setMensaje(area.getText());
						setVisible(false);
						
					}
				});
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return Enviar;
	}

	/**
	 * This method initializes Cancelar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelar()
	{
		if (Cancelar == null)
		{
			try
			{
				Cancelar = new JButton();
				Cancelar.setText("Cancelar");  // Generated
				Cancelar.setIcon(new ImageIcon("Resources/cancel.png"));  // Generated
				Cancelar.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent e)
					{
						mif = null;
						setVisible(false);
					}
				});
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return Cancelar;
	}

	/**
	 * This method initializes panelCentro	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelCentro()
	{
		if (panelCentro == null)
		{
			try
			{
				GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
				gridBagConstraints2.fill = GridBagConstraints.BOTH;  // Generated
				gridBagConstraints2.gridy = 0;  // Generated
				gridBagConstraints2.weightx = 1.0;  // Generated
				gridBagConstraints2.weighty = 1.0;  // Generated
				gridBagConstraints2.gridx = 0;  // Generated
				panelCentro = new JPanel();
				panelCentro.setLayout(new GridBagLayout());  // Generated
				panelCentro.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray, 2), "Mensaje", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Lucida Grande", Font.PLAIN, 13), Color.black));  // Generated
				panelCentro.add(getJScrollPane(), gridBagConstraints2);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelCentro;
	}

	/**
	 * This method initializes panelOeste	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelOeste()
	{
		if (panelOeste == null)
		{
			try
			{
				BorderLayout borderLayout2 = new BorderLayout();
				borderLayout2.setHgap(1);  // Generated
				borderLayout2.setVgap(1);  // Generated
				panelOeste = new JPanel();
				panelOeste.setLayout(borderLayout2);  // Generated
				panelOeste.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray, 2), "Agenda", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Lucida Grande", Font.PLAIN, 13), Color.black));  // Generated
				panelOeste.setPreferredSize(new Dimension(120, 28));  // Generated
				panelOeste.add(getArbol(), BorderLayout.WEST);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelOeste;
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
				jScrollPane = new JScrollPane();
				jScrollPane.setViewportView(getArea());  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return jScrollPane;
	}

	/**
	 * This method initializes area	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getArea()
	{
		if (area == null)
		{
			try
			{
				area = new JTextArea();
				area.setBorder(new LineBorder(Color.GRAY, 1));
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return area;
	}

	/**
	 * This method initializes arbol	
	 * 	
	 * @return componentes.gui.usuarios.ArbolUsuariosConectadosRol	
	 */
	private JList getArbol()
	{
		if (arbol == null)
		{
			try
			{
				Vector v = ClienteMetaInformacion.obtenerCMI().obtenerUsuarios();
				int seleccionado = -1;
				for (int i=0; i<v.size(); ++i){
					if (v.get(i).equals(dest)) {
						seleccionado = i;
						this.getDestinatario().setText(dest);
					}
				}
				
				arbol = new JList(v);
				
				if (seleccionado >= 0)
					arbol.setSelectedIndex(seleccionado);
				
				arbol.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // Generated
				arbol.setToolTipText("Lista de usuarios");  // Generated
				arbol.setPreferredSize(new Dimension(108, 28));  // Generated
				arbol.setVisible(true);  // Generated
				
				arbol.setBorder(new LineBorder(Color.GRAY, 1));
					
				arbol.addListSelectionListener(new javax.swing.event.ListSelectionListener()
				{
					public void valueChanged(javax.swing.event.ListSelectionEvent e)
					{
						if (arbol.getSelectedValue() != null)
							getDestinatario().setText(arbol.getSelectedValue().toString());
					}
				});
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return arbol;
	}

	/**
	 * This method initializes panelNorte	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelNorte()
	{
		if (panelNorte == null)
		{
			try
			{
				GridLayout gridLayout = new GridLayout();
				gridLayout.setRows(2);  // Generated
				gridLayout.setVgap(7);  // Generated
				gridLayout.setHgap(6);  // Generated
				panelNorte = new JPanel();
				panelNorte.setLayout(gridLayout);  // Generated
				panelNorte.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray, 2), "Datos Envío", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Lucida Grande", Font.PLAIN, 13), Color.black));  // Generated
				panelNorte.add(getPanelDest(), null);  // Generated
				panelNorte.add(getPanelAsunto(), null);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelNorte;
	}

	/**
	 * This method initializes destinatario	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDestinatario()
	{
		if (destinatario == null)
		{
			try
			{
				destinatario = new JTextField();
				destinatario.setBorder(new LineBorder(Color.GRAY, 1));
				destinatario.setPreferredSize(new Dimension(200, 18));  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return destinatario;
	}

	/**
	 * This method initializes panelCentral	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelCentral()
	{
		if (panelCentral == null)
		{
			try
			{
				BorderLayout borderLayout1 = new BorderLayout();
				borderLayout1.setHgap(1);  // Generated
				borderLayout1.setVgap(1);  // Generated
				panelCentral = new JPanel();
				panelCentral.setLayout(borderLayout1);  // Generated
				panelCentral.add(getPanelNorte(), BorderLayout.NORTH);  // Generated
				panelCentral.add(getPanelCentro(), BorderLayout.CENTER);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelCentral;
	}

	/**
	 * This method initializes panelDest	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelDest()
	{
		if (panelDest == null)
		{
			try
			{
				panelDest = new JPanel();
				panelDest.setLayout(new BorderLayout());  // Generated
				panelDest.add(getDestinatario(), BorderLayout.CENTER);  // Generated
				panelDest.add(getJLabel(), BorderLayout.WEST);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelDest;
	}

	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getJLabel()
	{
		if (jLabel == null)
		{
			try
			{
				jLabel = new JLabel();
				jLabel.setText("Destinatario:     ");  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return jLabel;
	}

	/**
	 * This method initializes panelAsunto	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelAsunto()
	{
		if (panelAsunto == null)
		{
			try
			{
				label = new JLabel();
				label.setText("Asunto:             ");  // Generated
				panelAsunto = new JPanel();
				panelAsunto.setLayout(new BorderLayout());  // Generated
				panelAsunto.add(getAsunto(), BorderLayout.CENTER);  // Generated
				panelAsunto.add(label, BorderLayout.WEST);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelAsunto;
	}

	/**
	 * This method initializes asunto	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getAsunto()
	{
		if (asunto == null)
		{
			try
			{
				asunto = new JTextField(this.asunt);
				asunto.setBorder(new LineBorder(Color.GRAY, 1));  // Generated
				asunto.setPreferredSize(new Dimension(200, 18));  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return asunto;
	}
	
	public static MIFichero getMensaje(String nombre, String asunto,String mensaje){
		EnviarMensaje em = new EnviarMensaje(nombre, asunto, mensaje);
		em.setModal(true);
		em.setVisible(true);
		return em.mif;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
