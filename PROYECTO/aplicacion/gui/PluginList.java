package aplicacion.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JList;
import javax.swing.ListModel;

import aplicacion.plugin.DAbstractPlugin;

/**
 * Componente con la lista de plugins del sistema
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
class PluginList extends JList
{
	private static final long serialVersionUID = 8824514161904171898L;

	/**
	 * Constructor por defecto
	 */
	public PluginList()
	{
		super();

		addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseMoved(MouseEvent e)
			{
				PluginList theList = (PluginList) e.getSource();
				ListModel model = theList.getModel();
				int index = theList.locationToIndex(e.getPoint());
				if (index > -1)
				{
					theList.setToolTipText(null);
					DAbstractPlugin text = (DAbstractPlugin) model
							.getElementAt(index);
					theList.setToolTipText(text.getDescripcion());
				}
			}
		});
	}

	/**
	 * Constructor con parametros
	 * 
	 * @param lm
	 *            Modelo para la lista de aplicaciones
	 */
	public PluginList( ListModel lm )
	{
		super(lm);

		addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseMoved(MouseEvent e)
			{
				PluginList theList = (PluginList) e.getSource();
				ListModel model = theList.getModel();
				int index = theList.locationToIndex(e.getPoint());
				if (index > -1)
				{
					theList.setToolTipText(null);
					try
					{
						DAbstractPlugin text = (DAbstractPlugin) model
								.getElementAt(index);
						theList.setToolTipText(text.getDescripcion());
					}
					catch (ClassCastException e2)
					{
						return;
					}

				}
			}
		});
	}

	/**
	 * Permite mostrar tooltips en cada entrada de la lista
	 * 
	 * @param e
	 *            Evento de raton recibido
	 * @return Cadena de texto con el tooltip del elemento en el que este
	 *         situado el raton en este momento
	 */
	@Override
	public String getToolTipText(MouseEvent e)
	{
		return super.getToolTipText();
	}

}
