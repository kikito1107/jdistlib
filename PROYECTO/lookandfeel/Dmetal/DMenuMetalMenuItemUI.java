package lookandfeel.Dmetal;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;

import util.DMenuSelectionManager;

import componentes.base.DJMenuItem;
import componentes.listeners.DJMenuItemListener;
import Deventos.DJMenuItemEvent;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings("unused")
public class DMenuMetalMenuItemUI extends BasicMenuItemUI
{
	
	

	public static ComponentUI createUI(JComponent x)
	{
		return new DMenuMetalMenuItemUI();
	}

	public MenuElement[] getPath()
	{
		MenuSelectionManager m = DMenuSelectionManager.defaultManager();
		MenuElement oldPath[] = m.getSelectedPath();
		MenuElement newPath[];
		int i = oldPath.length;
		if (i == 0)
		{
			return new MenuElement[0];
		}
		Component parent = menuItem.getParent();
		if (oldPath[i - 1].getComponent() == parent)
		{
			// The parent popup menu is the last so far
			newPath = new MenuElement[i + 1];
			System.arraycopy(oldPath, 0, newPath, 0, i);
			newPath[i] = menuItem;
		}
		else
		{
			// A sibling menuitem is the current selection
			//
			// This probably needs to handle 'exit submenu into
			// a menu item. Search backwards along the current
			// selection until you find the parent popup menu,
			// then copy up to that and add yourself...
			int j;
			for (j = oldPath.length - 1; j >= 0; j--)
			{
				if (oldPath[j].getComponent() == parent)
				{
					break;
				}
			}
			newPath = new MenuElement[j + 2];
			System.arraycopy(oldPath, 0, newPath, 0, j + 1);
			newPath[j + 1] = menuItem;
			/*
			 * System.out.println("Sibling condition -- ");
			 * System.out.println("Old array : ");
			 * printMenuElementArray(oldPath, false); System.out.println("New
			 * array : "); printMenuElementArray(newPath, false);
			 */
		}
		return newPath;
	}

	protected MouseInputListener createMouseInputListener(JComponent c)
	{
		return new DMouseInputHandler();
	}

	
	
	@SuppressWarnings("unchecked")
	private Vector pathToVector(MenuElement[] path)
	{
		Vector v = new Vector();
		int i, j, n;
		v.add(new Integer(0));
		for (i = 1; i < path.length; i++)
		{
			if (path[i] instanceof JPopupMenu)
			{
				v.add(new Integer(-5));
			}
			else
			{
				MenuElement[] me = path[i - 1].getSubElements();
				n = -1;
				for (j = 0; j < me.length; j++)
				{
					if (me[j].equals(path[i]))
					{
						n = j;
					}
				}
				v.add(new Integer(n));
			}
		}
		return v;
	}

	// *****************************************************************************
	protected class DMouseInputHandler implements MouseInputListener
	{
		public void mouseClicked(MouseEvent e)
		{
		}

		public void mousePressed(MouseEvent e)
		{
		}

		public void mouseReleased(MouseEvent e)
		{
			MenuSelectionManager manager = DMenuSelectionManager
					.defaultManager();
			Point p = e.getPoint();
			if (p.x >= 0 && p.x < menuItem.getWidth() && p.y >= 0
					&& p.y < menuItem.getHeight())
			{
				// *****************************************
				Vector v = ( (DJMenuItem) menuItem ).getDJMenuItemListeners();
				DJMenuItemEvent evento = new DJMenuItemEvent();
				evento.path = new Vector();
				for (int i = 0; i < v.size(); i++)
				{
					( (DJMenuItemListener) v.elementAt(i) )
							.cambioEstado(evento);
				}
				// *****************************************
			}
			else
			{
				// manager.processMouseEvent(e);
			}
		}

		public void mouseEntered(MouseEvent e)
		{
			
			MenuSelectionManager manager = DMenuSelectionManager
					.defaultManager();
			// int modifiers = e.getModifiers();
			// 4188027: drag enter/exit added in JDK 1.1.7A, JDK1.2
			/*
			 * if ( (modifiers & (InputEvent.BUTTON1_MASK |
			 * InputEvent.BUTTON2_MASK | InputEvent.BUTTON3_MASK)) != 0) {
			 * DMenuSelectionManager.defaultManager().processMouseEvent(e); }
			 * else { manager.setSelectedPath(getPath()); }
			 */

			// *****************************************
			Vector v = ( (DJMenuItem) menuItem ).getDJMenuItemListeners();
			DJMenuItemEvent evento = new DJMenuItemEvent();
			evento.path = pathToVector(getPath());
			for (int i = 0; i < v.size(); i++)
			{
				( (DJMenuItemListener) v.elementAt(i) ).cambioEstado(evento);
			}
			// *****************************************

		}

		public void mouseExited(MouseEvent e)
		{
			MenuSelectionManager manager = DMenuSelectionManager
					.defaultManager();

			/*
			 * int modifiers = e.getModifiers(); // 4188027: drag enter/exit
			 * added in JDK 1.1.7A, JDK1.2 if ( (modifiers &
			 * (InputEvent.BUTTON1_MASK | InputEvent.BUTTON2_MASK |
			 * InputEvent.BUTTON3_MASK)) != 0) {
			 * DMenuSelectionManager.defaultManager().processMouseEvent(e); }
			 * else {
			 * 
			 * MenuElement path[] = manager.getSelectedPath(); if (path.length >
			 * 1) { MenuElement newPath[] = new MenuElement[path.length - 1];
			 * int i, c; for (i = 0, c = path.length - 1; i < c; i++) newPath[i] =
			 * path[i]; manager.setSelectedPath(newPath); } }
			 */
			// *****************************************
			MenuElement path[] = manager.getSelectedPath();
			if (path.length > 1)
			{
				MenuElement newPath[] = new MenuElement[path.length - 1];
				int i, c;
				for (i = 0, c = path.length - 1; i < c; i++)
				{
					newPath[i] = path[i];
				}
				Vector v = ( (DJMenuItem) menuItem ).getDJMenuItemListeners();
				DJMenuItemEvent evento = new DJMenuItemEvent();
				evento.path = pathToVector(newPath);
				for (i = 0; i < v.size(); i++)
				{
					( (DJMenuItemListener) v.elementAt(i) )
							.cambioEstado(evento);
				}
			}
			// *****************************************

		}

		public void mouseDragged(MouseEvent e)
		{
			// No nos interesan los eventos mouseDragged
			// MenuSelectionManager.defaultManager().processMouseEvent(e);
		}

		public void mouseMoved(MouseEvent e)
		{
		}
	}

}
