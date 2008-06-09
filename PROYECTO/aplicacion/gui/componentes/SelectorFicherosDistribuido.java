package aplicacion.gui.componentes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import aplicacion.fisica.documentos.MIDocumento;



/**
 * Dialogo para abrir un documento del servidor de ficheros
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class SelectorFicherosDistribuido extends JDialog
{
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel jPanel = null;

	private JButton botonAceptar = null;

	private JButton botonCancelar = null;

	private JPanel jPanel1 = null;

	private DefaultMutableTreeNode root = null;

	private ArbolDocumentos arbol = null;

	private MIDocumento fichero = null;

	/**
	 * Constructor
	 * @param owner Ventana padre del dialogo
	 * @param raiz Nodo que se usara como raiz del arbol mostrado
	 */
	public SelectorFicherosDistribuido( Frame owner, DefaultMutableTreeNode raiz )
	{
		super(owner);
		root = raiz;
		initialize();
	}

	/**
	 * Inicializacion de los componentes graficos
	 */
	private void initialize()
	{
		this.setSize(500, 391);
		this.setResizable(false);
		this.setContentPane(getJContentPane());
	}

	private JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel1(), BorderLayout.SOUTH);
			jContentPane.add(new JScrollPane(getArbol()), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	private JPanel getJPanel()
	{
		if (jPanel == null)
		{
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(getJButton(), gridBagConstraints);
			jPanel.add(getBotonCancelar(), gridBagConstraints1);
		}
		return jPanel;
	}

	private JButton getJButton()
	{
		if (botonAceptar == null)
		{
			botonAceptar = new JButton();
			botonAceptar.setIcon(new ImageIcon(
					"Resources/folder-open_16x16.png"));
			botonAceptar.setText("Abrir");
			botonAceptar.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					accionAceptar();
				}
			});
		}
		return botonAceptar;
	}

	/**
	 * Muestra el arbol de documentos y obtiene la metainformacion 
	 * del documento seleccionado
	 * @param owner Ventana padre para el dialogo
	 * @param raiz Nodo que se usara como raiz del arbols
	 * @return Metainformacion del documento seleccionado
	 */
	public static MIDocumento getDatosFichero(Frame owner,
			DefaultMutableTreeNode raiz)
	{
		SelectorFicherosDistribuido sfd = new SelectorFicherosDistribuido(
				owner, raiz);

		sfd.getPath();

		return sfd.fichero;

	}

	/**
	 * Configura el dialogo para que se pueda seleccionar un documento
	 */
	private void getPath()
	{
		setTitle("Selecciona el documento");

		setSize(330, 200);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		setLocation(( screenSize.width - frameSize.width ) / 2,
				( screenSize.height - frameSize.height ) / 2);

		this.setModal(true);

		setVisible(true);
	}

	private JButton getBotonCancelar()
	{
		if (botonCancelar == null)
		{
			botonCancelar = new JButton();
			botonCancelar.setIcon(new ImageIcon("Resources/cancel.png"));
			botonCancelar.setText("Cancelar");
			botonCancelar.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					setVisible(false);
				}
			});
		}
		return botonCancelar;
	}

	private JPanel getJPanel1()
	{
		if (jPanel1 == null)
		{
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BorderLayout());
			jPanel1.add(getJPanel(), BorderLayout.EAST);
		}
		return jPanel1;
	}

	private JTree getArbol()
	{
		if (arbol == null)
		{
			arbol = new ArbolDocumentos(root);
			arbol.setRootVisible(false);
			arbol.addKeyListener(new java.awt.event.KeyAdapter()
			{
				@Override
				public void keyPressed(java.awt.event.KeyEvent e)
				{
					if (e.getKeyChar() == '\n') accionAceptar();

				}
			});
			arbol.addMouseListener(new java.awt.event.MouseAdapter()
			{
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e)
				{
					if (e.getClickCount() == 2) accionAceptar();
				}
			});
			arbol.expandRow(0);
		}
		return arbol;
	}

	/**
	 * Accion que se realiza al hacer doble click en un nodo del arbol
	 */
	private void accionAceptar()
	{
		TreePath[] dtp = arbol.getSelectionPaths();

		if (( dtp != null ) && ( dtp.length > 0 ))
		{

			fichero = arbol.getDocumentoSeleccionado();

			setVisible(false);
		}
	}

}
