package aplicacion.plugin.example.gestor;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import util.FiltroFichero;

import aplicacion.gui.PanelPrincipal;
import aplicacion.plugin.DAbstractPlugin;
import aplicacion.plugin.DPluginLoader;

public class GestorPlugins extends JFrame
{

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JTable tablaPlugins = null;

	private JPanel PanelBotones = null;

	private JButton EliminarPlugin = null;

	private JButton AgregarPlugin = null;

	private DefaultTableModel defaultTableModel = null;

	private JScrollPane panelScroll = null;

	Vector<DAbstractPlugin> plugins = null;

	/**
	 * This is the default constructor
	 */
	public GestorPlugins()
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
		this.setSize(379, 379);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("/Resources/brick.png")));
		this.setContentPane(getJContentPane());
		this.setTitle(":: Gestor de Plugins ::");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void setPlugins(Vector<DAbstractPlugin> plgs)
	{
		plugins = plgs;

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
			jContentPane.add(getPanelBotones(), BorderLayout.SOUTH);
			jContentPane.add(getScroll(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	private JScrollPane getScroll()
	{
		if (panelScroll == null)
		{
			panelScroll = new JScrollPane();
			panelScroll.setViewportView(getTablaPluginsInstalados());
		}

		return panelScroll;
	}

	/**
	 * This method initializes TablaPluginsInstalados
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getTablaPluginsInstalados()
	{
		if (tablaPlugins == null)
		{
			tablaPlugins = new JTable();
			tablaPlugins.setModel(this.getDefaultTableModel());
			tablaPlugins.setAutoCreateColumnsFromModel(true);
			tablaPlugins.setShowGrid(true);
			tablaPlugins.setCellSelectionEnabled(false);
			tablaPlugins.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tablaPlugins.setRowSelectionAllowed(true);
		}
		return tablaPlugins;
	}

	/**
	 * This method initializes PanelBotones
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPanelBotones()
	{
		if (PanelBotones == null)
		{
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			PanelBotones = new JPanel();
			PanelBotones.setLayout(new GridBagLayout());
			PanelBotones.add(getEliminarPlugin(), gridBagConstraints);
			PanelBotones.add(getAgregarPlugin(), gridBagConstraints1);
		}
		return PanelBotones;
	}

	/**
	 * This method initializes EliminarPlugin
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getEliminarPlugin()
	{
		if (EliminarPlugin == null)
		{
			EliminarPlugin = new JButton();
			EliminarPlugin.setText("Eliminar Plugin");
			EliminarPlugin.setIcon(new ImageIcon(getClass().getResource(
					"/Resources/brick_delete.png")));
			EliminarPlugin
					.addActionListener(new java.awt.event.ActionListener()
					{
						public void actionPerformed(java.awt.event.ActionEvent e)
						{
							eliminarPlugin(tablaPlugins.getSelectedRow());
						}
					});
		}
		return EliminarPlugin;
	}

	private void eliminarPlugin(int numPlugin)
	{
		
		if(numPlugin < 0) return;
		
		if (PanelPrincipal.plugins == null) return;
		
		if (PanelPrincipal.plugins.size() <= numPlugin) return;
		
		java.io.File fichero = new java.io.File("plugins/" + PanelPrincipal.plugins.get(numPlugin).getJarFile());
		
		System.out.println("El nombre del fichero a borrar " + fichero.getAbsolutePath());
		
		if (!fichero.delete())
			System.err.println("Error al eliminar el fichero");
		
		PanelPrincipal.plugins.remove(numPlugin);
	}

	/**
	 * This method initializes AgregarPlugin
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAgregarPlugin()
	{
		if (AgregarPlugin == null)
		{
			AgregarPlugin = new JButton();
			AgregarPlugin.setText("Agregar nuevo plugin");
			AgregarPlugin.setIcon(new ImageIcon(getClass().getResource(
					"/Resources/brick_add.png")));
			
			AgregarPlugin.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					// Seleccionar archivo a cargar
					
					//mostramos el selector de ficheros
					JFileChooser jfc = new JFileChooser("Subir Documento Servidor");
					jfc.setAcceptAllFileFilterUsed(false);
					FiltroFichero filtro = new FiltroFichero("jar", "Java ARchive");
					jfc.setFileFilter(filtro);
					

					int op = jfc.showDialog(null, "Aceptar");

					// si no se ha escogido la opcion aceptar en el dialogo de apertura de
					// fichero salimos
					if (op != JFileChooser.APPROVE_OPTION) return;

					java.io.File origen  = jfc.getSelectedFile();
					java.io.File destino = new java.io.File("plugin/"+origen.getName()); 
					
					//copiarlo a la carpeta de plugins
					if (!origen.renameTo(destino))
						JOptionPane.showMessageDialog(null,"Ha ocurrido un error durante la copia del plugin");
					
					
					DAbstractPlugin nuevo = null;
					
					// probar que es un plugin valido
					try
					{
						nuevo = DPluginLoader.getPlugin(destino.getAbsolutePath());
					}
					catch (Exception e1)
					{
						nuevo = null;
					}
					
					if (nuevo == null) {
						JOptionPane.showMessageDialog(null,"El fichero a–adido no contiene un plugin v‡lido.");
						if (!destino.delete())
							JOptionPane.showMessageDialog(null,"Error al  intentar borrar un plugin no valido");
					}
						
					
				}
			});
		}
		return AgregarPlugin;
	}

	@SuppressWarnings( "deprecation" )
	private DefaultTableModel getDefaultTableModel()
	{

		if (defaultTableModel == null)
		{

			defaultTableModel = new DefaultTableModel();
			defaultTableModel.setRowCount(3);
			defaultTableModel.addColumn("Nombre");
			defaultTableModel.addColumn("Version");

		}

		return defaultTableModel;
	}

	public void inicializarModelo()
	{
		if (PanelPrincipal.plugins == null) return;

		defaultTableModel.setNumRows(PanelPrincipal.plugins.size());

		for (int i = 0; i < PanelPrincipal.plugins.size(); ++i)
		{
			defaultTableModel.setValueAt(PanelPrincipal.plugins.get(i)
					.getName(), i, 0);
			defaultTableModel.setValueAt(PanelPrincipal.plugins.get(i)
					.getVersion(), i, 1);
		}
	}

	public static void main(String[] args)
	{

		new GestorPlugins().setVisible(true);

	}

} // @jve:decl-index=0:visual-constraint="10,10"
