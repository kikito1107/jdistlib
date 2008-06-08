package aplicacion.gui;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import Deventos.enlaceJS.DConector;
import aplicacion.fisica.documentos.MIDocumento;

/**
 * Dialogo que muestra las propiedades de un documento
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class VisorPropiedadesFichero extends JDialog
{
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JTextField nombreFichero = null;

	private JTextField usuario = null;

	private JPanel panelPermisos = null;

	private JCheckBox lecturaU = null;

	private JCheckBox escrituraU = null;

	private JCheckBox lecturaR = null;

	private JCheckBox escrituraR = null;

	private JCheckBox lecturaO = null;

	private JCheckBox escrituraO = null;

	private JLabel etqUsuario = null;

	private JLabel etqRol = null;

	private JLabel etqOtros = null;

	private JLabel Lectura = null;

	private JLabel etqEscritura = null;

	private JPanel panelDatos = null;

	private JLabel etiquetaNombreFichero = null;

	private JLabel etiquetaOwner = null;

	private JTextField rol = null;

	private JLabel etiquetaRol = null;

	private JButton jButton = null;

	private JButton jButton1 = null;

	private MIDocumento fichero = null;

	private JCheckBox esDirectorio = null;

	private JLabel Directorio = null;

	private JTextArea areaTextoEditores = null;

	private JPanel panelEditores = null;

	/**
	 * Constructor
	 * 
	 * @param owner
	 *            Ventana que hace uso de este dialogo
	 * @param f
	 *            Metainformacion del documento cuyas propiedades deseamos
	 *            mostrar
	 */
	public VisorPropiedadesFichero( Frame owner, MIDocumento f )
	{
		super(owner);
		fichero = f;
		initialize();
	}

	/**
	 * Inicializacion de los componentes graficos
	 */
	private void initialize()
	{
		this.setSize(316, 485);
		this.setContentPane(getJContentPane());
	}

	private JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			jContentPane = new JPanel();
			jContentPane.setLayout(null); // Generated
			jContentPane.add(getPanelDatos(), null); // Generated
			jContentPane.add(getPanelEditores(), null); // Generated
			jContentPane.add(getPanelPermisos(), null); // Generated
			jContentPane.add(getJButton(), null); // Generated
			jContentPane.add(getJButton1(), null); // Generated
		}
		return jContentPane;
	}

	private JTextField getNombreFichero()
	{
		if (nombreFichero == null)
		{
			nombreFichero = new JTextField(fichero.getNombre());
			nombreFichero.setPreferredSize(new Dimension(160, 22));
			nombreFichero
					.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT); // Generated
		}
		return nombreFichero;
	}

	private JTextField getPathFichero()
	{
		if (usuario == null)
		{
			if (fichero.getUsuario() != null)
			{
				usuario = new JTextField(fichero.getUsuario()
						.getNombreUsuario());
			}

			else
			{
				usuario = new JTextField("");
			}
			usuario.setPreferredSize(new Dimension(160, 22));
			usuario.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT); // Generated
			usuario.setEditable(false);
		}
		return usuario;
	}

	private JPanel getPanelPermisos()
	{
		if (panelPermisos == null)
		{
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 2;
			gridBagConstraints10.insets = new Insets(1, 2, 0, 2);
			gridBagConstraints10.gridy = 0;
			etqEscritura = new JLabel();
			etqEscritura.setText("     Escritura    ");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.insets = new Insets(1, 2, 0, 7);
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL; // Generated
			gridBagConstraints9.gridy = 0;
			Lectura = new JLabel();
			Lectura.setText("      Lectura     ");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL; // Generated
			gridBagConstraints8.gridy = 4;
			etqOtros = new JLabel();
			etqOtros.setText("Otros:");
			etqOtros
					.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Generated
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL; // Generated
			gridBagConstraints7.gridy = 2;
			etqRol = new JLabel();
			etqRol.setText("Rol:");
			etqRol.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Generated
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL; // Generated
			gridBagConstraints6.gridy = 1;
			etqUsuario = new JLabel();
			etqUsuario.setText("Usuario:");
			etqUsuario
					.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Generated
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.gridy = 4;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 4;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 2;
			gridBagConstraints3.gridy = 2;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 2;
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			panelPermisos = new JPanel();
			panelPermisos.setLayout(new GridBagLayout());
			panelPermisos.setBorder(BorderFactory.createTitledBorder(null,
					"Permisos", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));
			panelPermisos.setBounds(new Rectangle(13, 298, 289, 111)); // Generated
			panelPermisos.add(getLecturaU(), gridBagConstraints);
			panelPermisos.add(getEscrituraU(), gridBagConstraints1);
			panelPermisos.add(getLecturaR(), gridBagConstraints2);
			panelPermisos.add(getEscrituraR(), gridBagConstraints3);
			panelPermisos.add(getLecturaO(), gridBagConstraints4); // Generated
			panelPermisos.add(getEscrituraO(), gridBagConstraints5); // Generated
			panelPermisos.add(etqUsuario, gridBagConstraints6); // Generated
			panelPermisos.add(etqRol, gridBagConstraints7); // Generated
			panelPermisos.add(etqOtros, gridBagConstraints8); // Generated
			panelPermisos.add(Lectura, gridBagConstraints9); // Generated
			panelPermisos.add(etqEscritura, gridBagConstraints10);
		}
		return panelPermisos;
	}

	private JCheckBox getLecturaU()
	{
		if (lecturaU == null)
		{
			lecturaU = new JCheckBox();
			if (fichero.getPermisos().charAt(0) == MIDocumento.PERMISO_LECTURA)
				lecturaU.setSelected(true);
			else lecturaU.setSelected(false);

		}
		return lecturaU;
	}

	private JCheckBox getEscrituraU()
	{
		if (escrituraU == null)
		{
			escrituraU = new JCheckBox();
			if (fichero.getPermisos().charAt(1) == MIDocumento.PERMISO_ESCRITURA)
				escrituraU.setSelected(true);
			else escrituraU.setSelected(false);
		}
		return escrituraU;
	}

	private JCheckBox getLecturaR()
	{
		if (lecturaR == null)
		{
			lecturaR = new JCheckBox();
			if (fichero.getPermisos().charAt(2) == MIDocumento.PERMISO_LECTURA)
				lecturaR.setSelected(true);
			else lecturaR.setSelected(false);
		}
		return lecturaR;
	}

	private JCheckBox getEscrituraR()
	{
		if (escrituraR == null)
		{
			escrituraR = new JCheckBox();
			if (fichero.getPermisos().charAt(3) == MIDocumento.PERMISO_ESCRITURA)
				escrituraR.setSelected(true);
			else escrituraR.setSelected(false);
		}
		return escrituraR;
	}

	private JCheckBox getLecturaO()
	{
		if (lecturaO == null)
		{
			lecturaO = new JCheckBox();
			if (fichero.getPermisos().charAt(4) == MIDocumento.PERMISO_LECTURA)
				lecturaO.setSelected(true);
			else lecturaO.setSelected(false);
		}
		return lecturaO;
	}

	private JCheckBox getEscrituraO()
	{
		if (escrituraO == null)
		{
			escrituraO = new JCheckBox();
			if (fichero.getPermisos().charAt(5) == MIDocumento.PERMISO_ESCRITURA)
				escrituraO.setSelected(true);
			else escrituraO.setSelected(false);
		}
		return escrituraO;
	}

	private JPanel getPanelDatos()
	{
		if (panelDatos == null)
		{
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.fill = GridBagConstraints.BOTH; // Generated
			gridBagConstraints24.gridy = 3; // Generated
			gridBagConstraints24.weightx = 1.0; // Generated
			gridBagConstraints24.insets = new Insets(2, 20, 2, 3); // Generated
			gridBagConstraints24.gridx = 1; // Generated
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.fill = GridBagConstraints.BOTH; // Generated
			gridBagConstraints23.gridy = 1; // Generated
			gridBagConstraints23.weightx = 1.0; // Generated
			gridBagConstraints23.insets = new Insets(2, 20, 2, 3); // Generated
			gridBagConstraints23.gridx = 1; // Generated
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.fill = GridBagConstraints.BOTH; // Generated
			gridBagConstraints22.gridy = 0; // Generated
			gridBagConstraints22.weightx = 1.0; // Generated
			gridBagConstraints22.insets = new Insets(2, 20, 2, 3); // Generated
			gridBagConstraints22.gridx = 1; // Generated
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0; // Generated
			gridBagConstraints21.gridy = 6; // Generated
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 2; // Generated
			gridBagConstraints20.gridy = 4; // Generated
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 0;
			gridBagConstraints18.fill = GridBagConstraints.HORIZONTAL; // Generated
			gridBagConstraints18.insets = new Insets(2, 0, 2, 0); // Generated
			gridBagConstraints18.gridy = 4;
			Directorio = new JLabel();
			Directorio.setText("Directorio:");
			Directorio
					.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Generated
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 1;
			gridBagConstraints17.fill = GridBagConstraints.BOTH; // Generated
			gridBagConstraints17.insets = new Insets(2, 20, 2, 3); // Generated
			gridBagConstraints17.gridy = 4;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.fill = GridBagConstraints.HORIZONTAL; // Generated
			gridBagConstraints16.insets = new Insets(2, 0, 2, 0); // Generated
			gridBagConstraints16.gridy = 3;
			etiquetaRol = new JLabel();
			etiquetaRol.setText("Rol:");
			etiquetaRol
					.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Generated
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints15.gridy = 3;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.gridx = 1;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL; // Generated
			gridBagConstraints14.insets = new Insets(2, 0, 2, 0); // Generated
			gridBagConstraints14.gridy = 0;
			etiquetaOwner = new JLabel();
			etiquetaOwner.setText("Dueño:");
			etiquetaOwner
					.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Generated
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL; // Generated
			gridBagConstraints13.insets = new Insets(2, 0, 2, 0); // Generated
			gridBagConstraints13.gridy = 1;
			etiquetaNombreFichero = new JLabel();
			etiquetaNombreFichero.setText("Nombre:");
			etiquetaNombreFichero
					.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Generated
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints12.gridy = 2;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.gridx = 1;
			panelDatos = new JPanel();
			panelDatos.setLayout(new GridBagLayout());
			panelDatos.setBorder(BorderFactory.createTitledBorder(null,
					"Basico", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));
			// panelDatos.add(getPathFichero(), gridBagConstraints12);
			panelDatos.setBackground(this.getContentPane().getBackground());
			panelDatos.setBounds(new Rectangle(13, 11, 289, 114)); // Generated
			panelDatos.add(etiquetaOwner, gridBagConstraints13); // Generated
			panelDatos.add(etiquetaNombreFichero, gridBagConstraints14); // Generated
			panelDatos.add(etiquetaRol, gridBagConstraints16); // Generated
			panelDatos.add(getEsDirectorio(), gridBagConstraints17); // Generated
			panelDatos.add(Directorio, gridBagConstraints18); // Generated
			panelDatos.add(getNombreFichero(), gridBagConstraints22); // Generated
			panelDatos.add(getPathFichero(), gridBagConstraints23); // Generated
			panelDatos.add(getJTextField(), gridBagConstraints24); // Generated
		}
		return panelDatos;
	}

	private JTextField getJTextField()
	{
		if (rol == null)
		{
			if (fichero.getRol() != null)
			{
				rol = new JTextField(fichero.getRol().getNombreRol());
			}

			else
			{
				rol = new JTextField("");
			}
			rol.setPreferredSize(new Dimension(160, 22));
			rol.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT); // Generated
			rol.setEditable(false);
		}
		return rol;
	}

	private JButton getJButton()
	{
		if (jButton == null)
		{
			jButton = new JButton();
			jButton.setIcon(new ImageIcon("Resources/tick.png"));
			jButton.setBounds(new Rectangle(77, 421, 106, 28)); // Generated
			jButton.setText("Aceptar");
			jButton.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					try
					// por si el fichero es la raiz "/" (no tiene usuario ni
					// rol)
					{
						if (fichero.comprobarPermisos(DConector.Dusuario,
								DConector.Drol, MIDocumento.PERMISO_ESCRITURA)
								|| ( fichero.getUsuario().getNombreUsuario()
										.equals(DConector.Dusuario) && fichero
										.getRol().getNombreRol().equals(
												DConector.Drol) ))
						{
							fichero.setNombre(nombreFichero.getText());

							String path = fichero.getRutaLocal();

							String[] carpetas = path.split("/");
							path = "";

							for (int i = 0; i < carpetas.length - 1; ++i)
							{

								if (!carpetas[i].equals(""))
									path += carpetas[i] + "/";
								System.err.print(carpetas[i] + ",\t");
							}

							if (path.equals("") || path.charAt(0) != '/')
								path = "/" + path;

							path += fichero.getNombre();

							System.err.println("\n\nnueva ruta local " + path);

							fichero.setRutaLocal(path);

							String permisos = "";

							if (lecturaU.isSelected())
								permisos += "r";
							else permisos += "-";

							if (escrituraU.isSelected())
								permisos += "w";
							else permisos += "-";

							if (lecturaR.isSelected())
								permisos += "r";
							else permisos += "-";

							if (escrituraR.isSelected())
								permisos += "w";
							else permisos += "-";

							if (lecturaO.isSelected())
								permisos += "r";
							else permisos += "-";

							if (escrituraO.isSelected())
								permisos += "w";
							else permisos += "-";

							fichero.setPermisos(permisos);
						}
						else fichero = null;

					}
					catch (NullPointerException ex)
					{
					}

					setVisible(false);
				}
			});
		}
		return jButton;
	}

	private JButton getJButton1()
	{
		if (jButton1 == null)
		{
			jButton1 = new JButton();
			jButton1.setIcon(new ImageIcon("Resources/cancel.png"));
			jButton1.setBounds(new Rectangle(194, 421, 108, 28)); // Generated
			jButton1.setText("Cancelar");
			jButton1.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					fichero = null;
					setVisible(false);
				}
			});
		}
		return jButton1;
	}

	private void verPropiedades()
	{
		setTitle("Propiedades " + this.fichero.getNombre());

		setSize(316, 485);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		setLocation(( screenSize.width - frameSize.width ) / 2,
				( screenSize.height - frameSize.height ) / 2);

		this.setResizable(false);

		this.setModal(true);

		setVisible(true);
	}

	private JCheckBox getEsDirectorio()
	{
		if (esDirectorio == null)
		{
			esDirectorio = new JCheckBox();

			esDirectorio.setEnabled(false);

			esDirectorio
					.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Generated
			if (fichero.esDirectorio())
				esDirectorio.setSelected(true);
			else esDirectorio.setSelected(false);
		}
		return esDirectorio;
	}

	private JTextArea getAreaTextoEditores()
	{
		if (areaTextoEditores == null)
		{
			areaTextoEditores = new JTextArea();
			areaTextoEditores.setLineWrap(true);
			areaTextoEditores.setWrapStyleWord(true);
			areaTextoEditores.setAutoscrolls(true);

			Vector<String> editores = DConector.obtenerDC().consultarEditores(
					fichero.getRutaLocal());

			if (editores != null)
			{
				int numEditores = editores.size();

				for (int i = 0; i < numEditores; ++i)
					areaTextoEditores.append(editores.get(i) + '\n');
			}

			areaTextoEditores.setEditable(false);
			areaTextoEditores
					.setToolTipText("Usuarios que están editando el documento en este momento");
			areaTextoEditores.setBackground(this.getContentPane()
					.getBackground());
		}
		return areaTextoEditores;
	}

	private JPanel getPanelEditores()
	{
		if (panelEditores == null)
		{
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.fill = GridBagConstraints.BOTH;
			gridBagConstraints19.gridy = 0;
			gridBagConstraints19.weightx = 1.0;
			gridBagConstraints19.weighty = 1.0;
			gridBagConstraints19.gridx = 0;
			panelEditores = new JPanel();
			panelEditores.setLayout(new GridBagLayout());
			panelEditores.setBorder(BorderFactory.createTitledBorder(null,
					"Editores", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));
			panelEditores.setBounds(new Rectangle(13, 136, 289, 151)); // Generated
			panelEditores.add(getAreaTextoEditores(), gridBagConstraints19);
		}
		return panelEditores;
	}

	/**
	 * Muestra las propiedades de un documento
	 * 
	 * @param fichero
	 *            Metainformacion del documento cuyas propiedades deseamos
	 *            mostrar en una ventana
	 * @param owner
	 *            Ventana padre del dialogo a mostrar
	 * @return La metainformacion del documento si todo fue correcto o null si
	 *         algo fue mal.
	 */
	public static MIDocumento verInfoFichero(MIDocumento fichero, Frame owner)
	{

		VisorPropiedadesFichero f = new VisorPropiedadesFichero(owner, fichero);

		f.verPropiedades();

		return f.fichero;
	}
}
