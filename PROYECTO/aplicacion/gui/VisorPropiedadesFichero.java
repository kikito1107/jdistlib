package aplicacion.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import Deventos.enlaceJS.DConector;
import aplicacion.fisica.documentos.FicheroBD;

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

	private JLabel etqOtris = null;

	private JLabel Lectura = null;

	private JLabel etqEscritura = null;

	private JPanel panelDatos = null;

	private JLabel jLabel = null;

	private JLabel jLabel1 = null;

	private JTextField rol = null;

	private JLabel jLabel2 = null;

	private JButton jButton = null;

	private JButton jButton1 = null;
	private FicheroBD fichero = null;

	private JCheckBox esDirectorio = null;

	private JLabel Directorio = null;

	/**
	 * @param owner
	 */
	public VisorPropiedadesFichero( Frame owner, FicheroBD f )
	{
		super(owner);
		fichero = f;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(273, 357);
		this.setContentPane(getJContentPane());
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
			jContentPane.setLayout(null);
			jContentPane.add(getPanelDatos(), null);
			jContentPane.add(getPanelPermisos(), null);
			jContentPane.add(getJButton(), null);
			jContentPane.add(getJButton1(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes nombreFichero	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getNombreFichero()
	{
		if (nombreFichero == null)
		{
			nombreFichero = new JTextField(fichero.getNombre());
			nombreFichero.setPreferredSize(new Dimension(150,22));
		}
		return nombreFichero;
	}

	/**
	 * This method initializes pathFichero	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getPathFichero()
	{
		if (usuario == null)
		{
			usuario = new JTextField(fichero.getUsuario().getNombreUsuario());
			usuario.setPreferredSize(new Dimension(150,22));
			usuario.setEditable(false);
		}
		return usuario;
	}

	/**
	 * This method initializes panelPermisos	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelPermisos()
	{
		if (panelPermisos == null)
		{
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 2;
			gridBagConstraints10.insets = new Insets(1, 2, 0, 2);
			gridBagConstraints10.gridy = 0;
			etqEscritura = new JLabel();
			etqEscritura.setText(" Escritura  ");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.insets = new Insets(1, 2, 0, 7);
			gridBagConstraints9.gridy = 0;
			Lectura = new JLabel();
			Lectura.setText("  Lectura  ");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridy = 3;
			etqOtris = new JLabel();
			etqOtris.setText("Otros");
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 2;
			etqRol = new JLabel();
			etqRol.setText("Rol");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 1;
			etqUsuario = new JLabel();
			etqUsuario.setText("Usuario");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.gridy = 3;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 3;
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
			panelPermisos.setBorder(BorderFactory.createTitledBorder(null, "Permisos", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			panelPermisos.setBounds(new Rectangle(17, 165, 240, 117));
			panelPermisos.add(getLecturaU(), gridBagConstraints);
			panelPermisos.add(getEscrituraU(), gridBagConstraints1);
			panelPermisos.add(getLecturaR(), gridBagConstraints2);
			panelPermisos.add(getEscrituraR(), gridBagConstraints3);
			panelPermisos.add(getLecturaO(), gridBagConstraints4);
			panelPermisos.add(getEscrituraO(), gridBagConstraints5);
			panelPermisos.add(etqUsuario, gridBagConstraints6);
			panelPermisos.add(etqRol, gridBagConstraints7);
			panelPermisos.add(etqOtris, gridBagConstraints8);
			panelPermisos.add(Lectura, gridBagConstraints9);
			panelPermisos.add(etqEscritura, gridBagConstraints10);
		}
		return panelPermisos;
	}

	/**
	 * This method initializes lecturaU	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getLecturaU()
	{
		if (lecturaU == null)
		{
			lecturaU = new JCheckBox();
			if (fichero.getPermisos().charAt(0) == FicheroBD.PERMISO_LECTURA )
				lecturaU.setSelected(true);
			else
				lecturaU.setSelected(false);
			
		}
		return lecturaU;
	}

	/**
	 * This method initializes escrituraU	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getEscrituraU()
	{
		if (escrituraU == null)
		{
			escrituraU = new JCheckBox();
			if (fichero.getPermisos().charAt(1) == FicheroBD.PERMISO_ESCRITURA )
				escrituraU.setSelected(true);
			else
				escrituraU.setSelected(false);
		}
		return escrituraU;
	}

	/**
	 * This method initializes lecturaR	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getLecturaR()
	{
		if (lecturaR == null)
		{
			lecturaR = new JCheckBox();
			if (fichero.getPermisos().charAt(2) == FicheroBD.PERMISO_LECTURA )
				lecturaR.setSelected(true);
			else
				lecturaR.setSelected(false);
		}
		return lecturaR;
	}

	/**
	 * This method initializes escrituraR	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getEscrituraR()
	{
		if (escrituraR == null)
		{
			escrituraR = new JCheckBox();
			if (fichero.getPermisos().charAt(3) == FicheroBD.PERMISO_ESCRITURA )
				escrituraR.setSelected(true);
			else
				escrituraR.setSelected(false);
		}
		return escrituraR;
	}

	/**
	 * This method initializes lecturaO	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getLecturaO()
	{
		if (lecturaO == null)
		{
			lecturaO = new JCheckBox();
			if (fichero.getPermisos().charAt(4) == FicheroBD.PERMISO_LECTURA )
				lecturaO.setSelected(true);
			else
				lecturaO.setSelected(false);
		}
		return lecturaO;
	}

	/**
	 * This method initializes escrituraO	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getEscrituraO()
	{
		if (escrituraO == null)
		{
			escrituraO = new JCheckBox();
			if (fichero.getPermisos().charAt(5) == FicheroBD.PERMISO_ESCRITURA )
				escrituraO.setSelected(true);
			else
				escrituraO.setSelected(false);
		}
		return escrituraO;
	}

	/**
	 * This method initializes panelDatos	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelDatos()
	{
		if (panelDatos == null)
		{
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 0;
			gridBagConstraints18.gridy = 3;
			Directorio = new JLabel();
			Directorio.setText("Directorio");
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 1;
			gridBagConstraints17.gridy = 3;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.gridy = 2;
			jLabel2 = new JLabel();
			jLabel2.setText("Rol");
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints15.gridy = 2;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.gridx = 1;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.gridy = 1;
			jLabel1 = new JLabel();
			jLabel1.setText("Due–o");
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText("Nombre");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints12.gridy = 1;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints11.gridy = 0;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.gridx = 1;
			panelDatos = new JPanel();
			panelDatos.setLayout(new GridBagLayout());
			panelDatos.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			panelDatos.setBounds(new Rectangle(15, 18, 242, 133));
			panelDatos.add(getNombreFichero(), gridBagConstraints11);
			panelDatos.add(getPathFichero(), gridBagConstraints12);
			panelDatos.add(jLabel, gridBagConstraints13);
			panelDatos.add(jLabel1, gridBagConstraints14);
			panelDatos.add(getJTextField(), gridBagConstraints15);
			panelDatos.add(jLabel2, gridBagConstraints16);
			panelDatos.add(getEsDirectorio(), gridBagConstraints17);
			panelDatos.add(Directorio, gridBagConstraints18);
		}
		return panelDatos;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField()
	{
		if (rol == null)
		{
			rol = new JTextField(fichero.getRol().getNombreRol());
			rol.setPreferredSize(new Dimension(150,22));
			rol.setEditable(false);
		}
		return rol;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton()
	{
		if (jButton == null)
		{
			jButton = new JButton();
			jButton.setBounds(new Rectangle(139, 293, 116, 29));
			jButton.setIcon(new ImageIcon(getClass().getResource("/Resources/tick.png")));
			jButton.setText("Aceptar");
			jButton.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					if (fichero.comprobarPermisos(DConector.Dusuario, DConector.Drol, FicheroBD.PERMISO_ESCRITURA)){
						fichero.setNombre(nombreFichero.getText());
						
						String path = fichero.getRutaLocal();
						
						String[] carpetas = path.split("/");
						path = "";
						
						for (int i=0; i<carpetas.length-1; ++i){
							
							if (carpetas[i] != "")
								path += carpetas[i] + "/";
							System.err.print(carpetas[i] + ",\t");
						}
						
						path += fichero.getNombre();
						
						System.err.println("\n\nnueva ruta local " + path);
						
						fichero.setRutaLocal(path);
						
						String permisos = "";
						
						if (lecturaU.isSelected())
							permisos += "r";
						else permisos += "-";
						
						if(escrituraU.isSelected())
							permisos += "w";
						else permisos += "-";
						
						if (lecturaR.isSelected())
							permisos += "r";
						else permisos += "-";
						
						if(escrituraR.isSelected())
							permisos += "w";
						else permisos += "-";
						
						if (lecturaO.isSelected())
							permisos += "r";
						else permisos += "-";
						
						if(escrituraO.isSelected())
							permisos += "w";
						else permisos += "-";
						
						fichero.setPermisos(permisos);
					}
					
					else 
						fichero = null;
					
					setVisible(false);
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
	private JButton getJButton1()
	{
		if (jButton1 == null)
		{
			jButton1 = new JButton();
			jButton1.setBounds(new Rectangle(17, 292, 116, 29));
			jButton1.setIcon(new ImageIcon(getClass().getResource("/Resources/cancel.png")));
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
	
	
	private void verPropiedades(){
		setTitle("Propiedades " + this.fichero.getNombre());
		
		 setSize(273, 357);
		 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		 Dimension frameSize = getSize();
		 if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		 }
		 if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		 }
		 setLocation( (screenSize.width - frameSize.width) / 2,
						 (screenSize.height - frameSize.height) / 2);
		 
		 this.setResizable(false);
		
		 this.setModal(true);
		 
		setVisible(true); 
	}
	
	public static FicheroBD verInfoFichero(FicheroBD fichero, Frame owner){
		
		VisorPropiedadesFichero f = new VisorPropiedadesFichero(owner, fichero);
		
		f.verPropiedades();
		
		return f.fichero;
	}

	/**
	 * This method initializes esDirectorio	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getEsDirectorio()
	{
		if (esDirectorio == null)
		{
			esDirectorio = new JCheckBox();
			
			esDirectorio.setEnabled(false);
			
			if (fichero.esDirectorio())
				esDirectorio.setSelected(true);
			else
				esDirectorio.setSelected(false);
		}
		return esDirectorio;
	}

}  //  @jve:decl-index=0:visual-constraint="6,-2"
