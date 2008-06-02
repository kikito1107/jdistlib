package aplicacion.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JList;
import javax.swing.ListModel;

import aplicacion.plugin.DAbstractPlugin;

class PluginList extends JList
{
	private static final long serialVersionUID = 8824514161904171898L;

	public PluginList()
	{
		super();

		// Attach a mouse motion adapter to let us know the mouse is over an
		// item and to show the tip.
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
					DAbstractPlugin text = (DAbstractPlugin) model.getElementAt(index);
					theList.setToolTipText(text.getDescripcion());
				}
			}
		});
	}
	
	public PluginList(ListModel lm)
	{
		super(lm);

		// Attach a mouse motion adapter to let us know the mouse is over an
		// item and to show the tip.
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
					try {
						DAbstractPlugin text = (DAbstractPlugin) model.getElementAt(index);
						theList.setToolTipText(text.getDescripcion());
					}
					catch (ClassCastException e2) {
						System.out.println("valor de index " + index);
						return;
					}
					
				}
			}
		});
	}


	// Expose the getToolTipText event of our JList
	public String getToolTipText(MouseEvent e)
	{
		return super.getToolTipText();
	}

}
