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
import java.awt.Font;

public class GestorPlugins extends JFrame
{

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JTable tablaPlugins = null;

	private JPanel PanelBotones = null;

	private JButton EliminarPlugin = null;

	private JButton AgregarPlugin = null;

	private DefaultTableModel modeloTabla = null;

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
				getClass().getResource("/Resources/logo.png")));
		this.setContentPane(getJContentPane());
		this.setTitle(":: Gestor de Plugins ::");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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

	/**
	 * Inicializa el panel de scroll sobre el que esta tablaPlugin
	 * @return el scroll
	 */
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
			EliminarPlugin.setFont(new Font("Lucida Sans", Font.PLAIN, 11));  // Generated
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

	/**
	 * Elimina un plugin: lo borra de la lista de plugins de la aplicacion y elimina el fichero .jar asociado al plugin
	 * @param numPlugin posicion que le plugin ocupa en la lista
	 */
	private void eliminarPlugin(int numPlugin)
	{
		
		if(numPlugin < 0) return;
		
		int numPlugins = PanelPrincipal.numPlugins();
		
		if (numPlugins <= numPlugin || numPlugins < 1) return;
		
		java.io.File fichero = new java.io.File("plugin/" + PanelPrincipal.getPluginJarName(numPlugin));
		
		System.out.println("El nombre del fichero a borrar " + fichero.getAbsolutePath());
		
		if (!fichero.delete())
			System.err.println("Error al eliminar el fichero");
		
		PanelPrincipal.eliminarPlugin((String) tablaPlugins.getValueAt(numPlugin, 0));

		
		modeloTabla.removeRow(numPlugin);
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
						return;
					}
					
					// agregarlo a la lista de plugins de PanelPrincipal
					PanelPrincipal.agregarPlugin(nuevo);
					
					// agregarlo a la tabla
					inicializarModelo();
					
					
				}
			});
		}
		return AgregarPlugin;
	}

	/**
	 * Inicializa el modelo de la tabla de plugisn
	 * @return
	 */
	private DefaultTableModel getDefaultTableModel()
	{

		if (modeloTabla == null)
		{

			modeloTabla = new DefaultTableModel();
			modeloTabla.setRowCount(3);
			modeloTabla.addColumn("Nombre");
			modeloTabla.addColumn("Version");
			modeloTabla.addColumn("JAR");

		}

		return modeloTabla;
	}

	/**
	 * Inicializa el modelo de la tabla
	 *
	 */
	public void inicializarModelo()
	{
		int numPlugins = PanelPrincipal.numPlugins();
		
		if ( numPlugins == 0 || numPlugins == -1) return;
		
		for (int i = 0; i < this.modeloTabla.getRowCount(); ++i)
			modeloTabla.removeRow(i);

		modeloTabla.setNumRows(numPlugins);

		for (int i = 0; i < numPlugins; ++i)
		{
			modeloTabla.setValueAt(PanelPrincipal.getPluginName(i), i, 0);
			modeloTabla.setValueAt(PanelPrincipal.getVersionPlugin(i), i, 1);
			modeloTabla.setValueAt(PanelPrincipal.getPluginJarName(i), i, 2);
		}
	}

} // @jve:decl-index=0:visual-constraint="10,10"
