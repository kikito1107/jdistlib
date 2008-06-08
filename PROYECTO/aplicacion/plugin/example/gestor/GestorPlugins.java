package aplicacion.plugin.example.gestor;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import util.FiltroFichero;

import aplicacion.plugin.DAbstractPlugin;
import aplicacion.plugin.DPluginLoader;
import aplicacion.plugin.PluginContainer;

import java.awt.Font;

/**
 * Ventana para la gestion de plugins.
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
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

	/**
	 * Constructor
	 */
	public GestorPlugins()
	{
		super();
		initialize();
	}

	/**
	 * Inicializar los componentes graficos 
	*/
	private void initialize()
	{
		this.setSize(379, 379);
		this.setIconImage(new ImageIcon("Resources/logo.png").getImage());
		this.setContentPane(getJContentPane());
		this.setTitle(":: Gestor de Plugins ::");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

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

	private JTable getTablaPluginsInstalados()
	{
		if (tablaPlugins == null)
		{
			tablaPlugins = new JTable()
			{
				/**
				 * 
				 */
				private static final long serialVersionUID = 5351964601477898681L;

				@Override
				public boolean isCellEditable(int row, int col)
				{
					if (col != 3)return false;
					else return true;
				}
				
				@SuppressWarnings("unchecked")
				public Class getColumnClass(int column)
				{
					return getValueAt(0, column).getClass();
				}
			};
			
			 
			
			tablaPlugins.setModel(this.getDefaultTableModel());
			tablaPlugins.setAutoCreateColumnsFromModel(true);
			tablaPlugins.setShowGrid(true);

			//tablaPlugins.setCellSelectionEnabled(false);
			tablaPlugins.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			tablaPlugins.setRowSelectionAllowed(true);
			
			TableColumn includeColumn = tablaPlugins.getColumnModel().getColumn(3);
			includeColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()));
		}
		return tablaPlugins;
	}

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

	private JButton getEliminarPlugin()
	{
		if (EliminarPlugin == null)
		{
			EliminarPlugin = new JButton();
			EliminarPlugin.setText("Eliminar Plugin");
			EliminarPlugin.setFont(new Font("Lucida Sans", Font.PLAIN, 11)); // Generated
			EliminarPlugin.setIcon(new ImageIcon("Resources/brick_delete.png"));
			EliminarPlugin
					.addActionListener(new java.awt.event.ActionListener()
					{
						public void actionPerformed(java.awt.event.ActionEvent e)
						{
							Object[] options = {"Eliminar Plugin","Cancelar"};
							
							int opc = JOptionPane.showOptionDialog(null, "Una vez eliminado el plugin esta acción no se puede deshacer\n¿Seguro que desea eliminar el plugin?", "Confirmacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
								    null,
								    options,
								    options[1]);
							
							if (opc == JOptionPane.YES_OPTION)
								eliminarPlugin(tablaPlugins.getSelectedRow());
						}
					});
		}
		return EliminarPlugin;
	}

	/**
	 * Elimina un plugin: Lo borra de la lista de plugins de la aplicacion y
	 * elimina el fichero .jar asociado al plugin
	 * 
	 * @param numPlugin
	 *            posicion que le plugin ocupa en la lista
	 */
	private void eliminarPlugin(int numPlugin)
	{

		if (numPlugin < 0) return;

		int numPlugins = PluginContainer.numPlugins();

		if (numPlugins <= numPlugin || numPlugins < 1) return;

		java.io.File fichero = new java.io.File("plugin/"
				+ PluginContainer.getPluginJarName(numPlugin));

		System.out.println("El nombre del fichero a borrar "
				+ fichero.getAbsolutePath());

		if (!fichero.delete())
			System.err.println("Error al eliminar el fichero");

		PluginContainer.eliminarPlugin((String) tablaPlugins.getValueAt(
				numPlugin, 0));

		modeloTabla.removeRow(numPlugin);
	}

	private JButton getAgregarPlugin()
	{
		if (AgregarPlugin == null)
		{
			AgregarPlugin = new JButton();
			AgregarPlugin.setText("Agregar nuevo plugin");
			AgregarPlugin.setIcon(new ImageIcon("Resources/brick_add.png"));

			AgregarPlugin.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					// Seleccionar archivo a cargar

					// mostramos el selector de ficheros
					JFileChooser jfc = new JFileChooser(
							"Subir Documento Servidor");
					jfc.setAcceptAllFileFilterUsed(false);
					FiltroFichero filtro = new FiltroFichero("jar",
							"Java ARchive");
					jfc.setFileFilter(filtro);

					int op = jfc.showDialog(null, "Aceptar");

					// si no se ha escogido la opcion aceptar en el dialogo de
					// apertura de
					// fichero salimos
					if (op != JFileChooser.APPROVE_OPTION) return;

					java.io.File origen = jfc.getSelectedFile();
					java.io.File destino = new java.io.File("plugin/"
							+ origen.getName());

					// copiarlo a la carpeta de plugins
					if (!origen.renameTo(destino))
						JOptionPane
								.showMessageDialog(null,
										"Ha ocurrido un error durante la copia del plugin");

					DAbstractPlugin nuevo = null;

					// probar que es un plugin valido
					try
					{
						nuevo = DPluginLoader.getPlugin(destino
								.getAbsolutePath());
					}
					catch (Exception e1)
					{
						nuevo = null;
					}

					if (nuevo == null)
					{
						JOptionPane
								.showMessageDialog(null,
										"El fichero añadido no contiene un plugin válido.");
						if (!destino.delete())
							JOptionPane
									.showMessageDialog(null,
											"Error al  intentar borrar un plugin no valido");
						return;
					}

					// agregarlo a la lista de plugins de PluginManager
					PluginContainer.agregarPlugin(nuevo);

					// agregarlo a la tabla
					inicializarModelo();

				}
			});
		}
		return AgregarPlugin;
	}

	private DefaultTableModel getDefaultTableModel()
	{

		if (modeloTabla == null)
		{

			modeloTabla = new DefaultTableModel();
			modeloTabla.setRowCount(2);
			modeloTabla.addColumn("Nombre");
			modeloTabla.addColumn("Version");
			modeloTabla.addColumn("JAR");
			modeloTabla.addColumn("Visible");
			
			modeloTabla.addTableModelListener(new ListenerPlugins());

		}

		return modeloTabla;
	}

	/**
	 * Listener de plugins
	 */
	private class ListenerPlugins implements TableModelListener {
        public void tableChanged(TableModelEvent evt) {
            if (evt.getType() == TableModelEvent.UPDATE && evt.getColumn() == 3) {
            	
            	int fila = evt.getFirstRow();
            	String nombrePlugin = modeloTabla.getValueAt(fila, 0).toString();
            	boolean estado = (Boolean) modeloTabla.getValueAt(fila, 3);
            	
            	
            	PluginContainer.setVisible(estado, nombrePlugin);
            }
        }
    }
	
	/**
	 * Inicializa el modelo de la tabla
	 */
	public void inicializarModelo()
	{
		int numPlugins = PluginContainer.numPlugins();

		if (numPlugins == 0 || numPlugins == -1) return;

		for (int i = 0; i < this.modeloTabla.getRowCount(); ++i)
			modeloTabla.removeRow(i);

		modeloTabla.setNumRows(numPlugins);

		for (int i = 0; i < numPlugins; ++i)
		{
			modeloTabla.setValueAt(PluginContainer.getPluginName(i), i, 0);
			modeloTabla.setValueAt(PluginContainer.getVersionPlugin(i), i, 1);
			modeloTabla.setValueAt(PluginContainer.getPluginJarName(i), i, 2);
			modeloTabla.setValueAt(PluginContainer.isVisible(i), i, 3);
		}
	}

}
