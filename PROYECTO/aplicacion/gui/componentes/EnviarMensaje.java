package aplicacion.gui.componentes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

import Deventos.enlaceJS.DConector;
import aplicacion.fisica.ClienteFicheros;
import aplicacion.fisica.documentos.MIDocumento;
import awareness.ClienteMetaInformacion;
import awareness.MIRol;
import awareness.MIUsuario;

import javax.swing.JToolBar;
import javax.swing.JSplitPane;

import util.Separador;

/**
 * Dialogo que permite enviar un mensaje de texto a alguno de los usuarios del
 * sistema
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class EnviarMensaje extends JDialog
{
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel panelSur = null;

	private JButton Enviar = null;

	private JPanel panelCentro = null;

	private JPanel panelOeste = null;

	private JScrollPane jScrollPane = null;

	private JTextArea area = null;

	private JList arbol = null;

	private JPanel panelNorte = null;

	private JTextField destinatario = null;

	private JPanel panelCentral = null;

	private String dest = null;

	private JLabel jLabel = null;

	private JTextField asunto = null;

	private JLabel label = null;

	private MIDocumento mif = null;

	private String asunt = "";

	private JLabel Asunto = null;

	private JToolBar barra = null;

	private JButton Adjuntar = null;

	private JSplitPane split = null;

	private JButton botonAgenda = null;

	private boolean agendaMostrada = true;

	/**
	 * Constructor
	 * 
	 * @param destin
	 *            Usuario de destino del mensaje
	 * @param asunto
	 *            Asunto del mensaje
	 * @param mensaje
	 *            Mensaje a enviar
	 */
	public EnviarMensaje( String destin, String asunto, String mensaje )
	{
		super();

		dest = destin;

		asunt = asunto;

		initialize();

		area.setText(mensaje);
	}

	/**
	 * Inicializacion de los componentes graficos
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
				Object[] options =
				{ "Salir", "Cancelar" };

				int elecc = JOptionPane
						.showOptionDialog(
								null,
								"El mensaje no ha sido enviado todavía\n¿Realmente desea salir?",
								"Atención", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[1]);

				if (elecc == JOptionPane.YES_OPTION) setVisible(false);
			}
		});
	}

	private JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(0); // Generated
			borderLayout.setVgap(0); // Generated
			jContentPane = new JPanel();
			jContentPane.setLayout(borderLayout); // Generated
			jContentPane.add(getPanelSur(), BorderLayout.NORTH); // Generated
			jContentPane.add(getSplit(), BorderLayout.CENTER); // Generated
		}
		return jContentPane;
	}

	private JPanel getPanelSur()
	{
		if (panelSur == null)
		{
			try
			{
				panelSur = new JPanel();
				panelSur.setLayout(new BorderLayout()); // Generated
				panelSur.add(getBarra(), BorderLayout.NORTH); // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelSur;
	}

	private JButton getEnviar()
	{
		if (Enviar == null)
		{
			try
			{
				Enviar = new JButton();
				Enviar.setText("Enviar"); // Generated
				Enviar.setIcon(new ImageIcon("Resources/icon_email.gif")); // Generated
				Enviar.setPreferredSize(new Dimension(61, 24)); // Generated
				Enviar.setBorderPainted(false);
				Enviar.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent e)
					{
						if (destinatario.getText().equals("")
								|| asunto.getText().equals("")
								|| area.getText().equals(""))
						{
							JOptionPane.showMessageDialog(null,
									"¡Quedan campos sin rellenar!");
							return;
						}

						mif = new MIDocumento();
						MIDocumento buzon = ClienteFicheros
								.obtenerClienteFicheros().existeFichero(
										"/Incoming", DConector.Daplicacion);

						if (buzon == null)
						{
							JOptionPane
									.showMessageDialog(null,
											"Buzon no encontrado, no se puede enviar el mensjae");
							setVisible(false);
							mif = null;
							return;
						}
						mif.setNombre(getAsunto().getText() + "."
								+ MIDocumento.TIPO_MENSAJE);
						mif.setTipo(MIDocumento.TIPO_MENSAJE);
						mif.setRutaLocal("/Incoming/" + mif.toString() + "."
								+ MIDocumento.TIPO_MENSAJE);
						mif.setId(-1);
						mif.setPermisos("rw----");

						mif.setPadre(buzon.getId());

						MIUsuario destino = ClienteMetaInformacion.obtenerCMI()
								.obtenerDatosUsuario(destinatario.getText());// .getUsuario();

						if (destino == null)
						{
							JOptionPane.showMessageDialog(null,
									"Usuario no existente:  "
											+ arbol.getSelectedValue()
													.toString());
							setVisible(false);
							mif = null;
							return;
						}
						MIRol rol = ClienteMetaInformacion.obtenerCMI().getRol(
								destino.getRolPorDefecto());
						mif.setUsuario(destino);
						mif.setRol(rol);
						mif.setMensaje("De: " + DConector.Dusuario + "\n"
								+ "Asunto: " + getAsunto().getText() + "\n"
								+ "\n" + area.getText());
						setVisible(false);

					}
				});
			}
			catch (java.lang.Throwable e)
			{

			}
		}
		return Enviar;
	}

	private JPanel getPanelCentro()
	{
		if (panelCentro == null)
		{
			try
			{
				GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
				gridBagConstraints2.fill = GridBagConstraints.BOTH; // Generated
				gridBagConstraints2.gridy = 0; // Generated
				gridBagConstraints2.weightx = 1.0; // Generated
				gridBagConstraints2.weighty = 1.0; // Generated
				gridBagConstraints2.insets = new Insets(2, 2, 2, 3); // Generated
				gridBagConstraints2.gridx = 0; // Generated
				panelCentro = new JPanel();
				panelCentro.setLayout(new GridBagLayout()); // Generated
				panelCentro.setBorder(BorderFactory
						.createTitledBorder(BorderFactory.createMatteBorder(1,
								0, 0, 0, Color.gray), "Mensaje",
								TitledBorder.DEFAULT_JUSTIFICATION,
								TitledBorder.DEFAULT_POSITION, new Font(
										"Lucida Grande", Font.PLAIN, 13),
								Color.black)); // Generated
				panelCentro.add(getJScrollPane(), gridBagConstraints2); // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelCentro;
	}

	private JPanel getPanelOeste()
	{
		if (panelOeste == null)
		{
			try
			{
				BorderLayout borderLayout2 = new BorderLayout();
				borderLayout2.setHgap(1);
				borderLayout2.setVgap(1);
				panelOeste = new JPanel();
				panelOeste.setLayout(borderLayout2);
				panelOeste.setBorder(BorderFactory
						.createTitledBorder(BorderFactory.createMatteBorder(1,
								0, 0, 0, Color.gray), "Agenda",
								TitledBorder.DEFAULT_JUSTIFICATION,
								TitledBorder.DEFAULT_POSITION, new Font(
										"Lucida Grande", Font.PLAIN, 13),
								Color.black));
				panelOeste.setPreferredSize(new Dimension(120, 28));
				panelOeste.add(getArbol(), BorderLayout.WEST);
			}
			catch (java.lang.Throwable e)
			{

			}
		}
		return panelOeste;
	}

	private JScrollPane getJScrollPane()
	{
		if (jScrollPane == null)
		{
			try
			{
				jScrollPane = new JScrollPane();
				jScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0,
						0));
				jScrollPane.setViewportView(getArea()); 
			}
			catch (java.lang.Throwable e)
			{
				
			}
		}
		return jScrollPane;
	}

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
				
			}
		}
		return area;
	}

	@SuppressWarnings( "unchecked" )
	private JList getArbol()
	{
		if (arbol == null)
		{
			try
			{
				Vector v = ClienteMetaInformacion.obtenerCMI()
						.obtenerUsuarios();
				int seleccionado = -1;
				for (int i = 0; i < v.size(); ++i)
				{
					if (v.get(i).equals(dest))
					{
						seleccionado = i;
						this.getDestinatario().setText(dest);
					}
				}

				arbol = new JList(v);

				if (seleccionado >= 0) arbol.setSelectedIndex(seleccionado);

				arbol.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
				arbol.setToolTipText("Lista de usuarios"); 
				arbol.setPreferredSize(new Dimension(108, 28)); 
				arbol.setVisible(true); 

				arbol.setBorder(new LineBorder(Color.GRAY, 1));

				arbol
						.addListSelectionListener(new javax.swing.event.ListSelectionListener()
						{
							public void valueChanged(
									javax.swing.event.ListSelectionEvent e)
							{
								if (arbol.getSelectedValue() != null)
									getDestinatario()
											.setText(
													arbol.getSelectedValue()
															.toString());
							}
						});
			}
			catch (java.lang.Throwable e)
			{
				
			}
		}
		return arbol;
	}

	private JPanel getPanelNorte()
	{
		if (panelNorte == null)
		{
			try
			{
				GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
				gridBagConstraints7.gridx = 0; // Generated
				gridBagConstraints7.fill = GridBagConstraints.BOTH; // Generated
				gridBagConstraints7.insets = new Insets(0, 4, 0, 0); // Generated
				gridBagConstraints7.gridy = 1; // Generated
				Asunto = new JLabel();
				Asunto.setText("Asunto:"); // Generated
				Asunto
						.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Generated
				GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
				gridBagConstraints6.gridx = 0; // Generated
				gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL; // Generated
				gridBagConstraints6.gridy = 1; // Generated
				GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
				gridBagConstraints5.gridx = 0; // Generated
				gridBagConstraints5.fill = GridBagConstraints.BOTH; // Generated
				gridBagConstraints5.insets = new Insets(0, 4, 0, 0); // Generated
				gridBagConstraints5.gridy = 0; // Generated
				GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
				gridBagConstraints4.fill = GridBagConstraints.BOTH; // Generated
				gridBagConstraints4.gridy = 1; // Generated
				gridBagConstraints4.weightx = 1.0; // Generated
				gridBagConstraints4.ipadx = 1; // Generated
				gridBagConstraints4.insets = new Insets(3, 10, 2, 2); // Generated
				gridBagConstraints4.gridx = 1; // Generated
				GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
				gridBagConstraints3.fill = GridBagConstraints.BOTH; // Generated
				gridBagConstraints3.gridy = 0; // Generated
				gridBagConstraints3.weightx = 1.0; // Generated
				gridBagConstraints3.ipadx = 1; // Generated
				gridBagConstraints3.insets = new Insets(3, 10, 2, 2); // Generated
				gridBagConstraints3.gridx = 1; // Generated
				panelNorte = new JPanel();
				panelNorte.setLayout(new GridBagLayout()); // Generated
				panelNorte.setBorder(BorderFactory
						.createTitledBorder(BorderFactory.createMatteBorder(1,
								0, 0, 0, Color.gray), "Datos Envío",
								TitledBorder.DEFAULT_JUSTIFICATION,
								TitledBorder.DEFAULT_POSITION, new Font(
										"Lucida Grande", Font.PLAIN, 13),
								Color.black)); // Generated
				panelNorte.add(getDestinatario(), gridBagConstraints3); // Generated
				panelNorte.add(getAsunto(), gridBagConstraints4); // Generated
				panelNorte.add(getJLabel(), gridBagConstraints5); // Generated
				panelNorte.add(Asunto, gridBagConstraints7); // Generated
				panelNorte.add(label, gridBagConstraints6); // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelNorte;
	}

	private JTextField getDestinatario()
	{
		if (destinatario == null)
		{
			try
			{
				destinatario = new JTextField();
				destinatario.setBorder(new LineBorder(Color.GRAY, 1));
				destinatario.setPreferredSize(new Dimension(200, 18)); // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return destinatario;
	}

	private JPanel getPanelCentral()
	{
		if (panelCentral == null)
		{
			try
			{
				BorderLayout borderLayout1 = new BorderLayout();
				borderLayout1.setHgap(2); // Generated
				borderLayout1.setVgap(5); // Generated
				panelCentral = new JPanel();
				panelCentral.setLayout(borderLayout1); // Generated
				panelCentral.add(getPanelNorte(), BorderLayout.NORTH); // Generated
				panelCentral.add(getPanelCentro(), BorderLayout.CENTER); // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return panelCentral;
	}

	private JLabel getJLabel()
	{
		if (jLabel == null)
		{
			try
			{
				jLabel = new JLabel();
				jLabel.setText("Destinatario:"); // Generated
				jLabel
						.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return jLabel;
	}

	private JTextField getAsunto()
	{
		if (asunto == null)
		{
			try
			{
				asunto = new JTextField(this.asunt);
				asunto.setBorder(new LineBorder(Color.GRAY, 1)); // Generated
				asunto.setPreferredSize(new Dimension(200, 18)); // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return asunto;
	}
	
	private JToolBar getBarra()
	{
		if (barra == null)
		{
			try
			{
				barra = new JToolBar();
				barra.setFloatable(false); // Generated
				barra
						.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT); // Generated
				barra.setPreferredSize(new Dimension(69, 32)); // Generated
				barra.add(getAdjuntar()); // Generated
				barra.add(getBotonAgenda()); // Generated
				barra.add(new Separador());
				barra.add(getEnviar()); // Generated

			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return barra;
	}

	private JButton getAdjuntar()
	{
		if (Adjuntar == null)
		{
			try
			{
				Adjuntar = new JButton();
				Adjuntar.setPreferredSize(new Dimension(61, 24)); // Generated
				Adjuntar.setIcon(new ImageIcon("Resources/attach.png")); // Generated
				Adjuntar.setText("Adjuntar"); // Generated
				Adjuntar.setBorderPainted(false); // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return Adjuntar;
	}

	private JSplitPane getSplit()
	{
		if (split == null)
		{
			try
			{
				split = new JSplitPane();
				split.setLeftComponent(this.getPanelOeste());
				split.setRightComponent(this.getPanelCentral());
				split.setOneTouchExpandable(false);
				split.setDividerLocation(120);
				split.setResizeWeight(0.0);
				split.setEnabled(false);
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return split;
	}

	private JButton getBotonAgenda()
	{
		if (botonAgenda == null)
		{
			try
			{
				botonAgenda = new JButton();
				botonAgenda.setPreferredSize(new Dimension(61, 24)); // Generated
				botonAgenda.setIcon(new ImageIcon("Resources/book.png")); // Generated
				botonAgenda.setText("Ocultar Agenda"); // Generated
				botonAgenda.setBorderPainted(false); // Generated
				botonAgenda
						.addActionListener(new java.awt.event.ActionListener()
						{
							public void actionPerformed(
									java.awt.event.ActionEvent e)
							{
								if (agendaMostrada)
								{
									split.setDividerLocation(0);
									agendaMostrada = false;
									botonAgenda.setText("Mostrar Agenda"); // Generated
								}
								else
								{
									split.setDividerLocation(120);
									agendaMostrada = true;
									botonAgenda.setText("Ocultar Agenda"); // Generated
								}
							}
						});
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return botonAgenda;
	}

	/**
	 * Crea una ventana de mensaje para su envio o reenvio
	 * @param nombre Nombre del destinatario del mensaje
	 * @param asunto Asunto del mensaje
	 * @param mensaje Mensaje a enviar o reenviar
	 * @return Metainformacion del mensaje enviado
	 */
	public static MIDocumento getMensaje(String nombre, String asunto,
			String mensaje)
	{
		EnviarMensaje em = new EnviarMensaje(nombre, asunto, mensaje);
		em.setModal(true);
		em.setVisible(true);
		return em.mif;
	}
}
