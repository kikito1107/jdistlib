package aplicacion.plugin.example.gestor;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import aplicacion.plugin.DAbstractPlugin;
import aplicacion.plugin.DPluginLoader;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import java.awt.Toolkit;

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
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/brick.png")));
		this.setContentPane(getJContentPane());
		this.setTitle(":: Gestor de Plugins ::");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void setPlugins(Vector<DAbstractPlugin> plgs){
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
	
	private JScrollPane getScroll(){
		if (panelScroll == null){
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
			EliminarPlugin.setIcon(new ImageIcon(getClass().getResource("/Resources/brick_delete.png")));
			EliminarPlugin.addActionListener(new java.awt.event.ActionListener()
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
		// TODO Auto-generated method stub
		
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
			AgregarPlugin.setIcon(new ImageIcon(getClass().getResource("/Resources/brick_add.png")));
			AgregarPlugin.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					agregarNuevoPlugin();
				}
			});
		}
		return AgregarPlugin;
	}
	
	
	
	private void agregarNuevoPlugin(){
		
	}
	
	@SuppressWarnings("deprecation")
	private DefaultTableModel getDefaultTableModel() {

		if (defaultTableModel == null) {

			defaultTableModel = new DefaultTableModel();
			defaultTableModel.setRowCount(3);
			defaultTableModel.addColumn("Nombre");
			defaultTableModel.addColumn("Version");
			
			

			this.inicializarModelo();
		}

		return defaultTableModel;
	}
	
	
	
	private void inicializarModelo()
	{
		String nombre;
		long version;
		
		if (plugins != null){
			
			defaultTableModel.setRowCount(plugins.size());
			
			for (int i=0; i <plugins.size(); ++i){
				nombre = plugins.get(i).getName();
				version = plugins.get(i).getVersion();
				
				defaultTableModel.setValueAt(nombre, i, 0);
				defaultTableModel.setValueAt(version, i, 1);
			}
		}
		

		
	}

	public static void main(String[] args){

		new GestorPlugins().setVisible(true);
		
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"
