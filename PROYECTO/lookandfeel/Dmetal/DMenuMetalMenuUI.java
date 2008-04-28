package lookandfeel.Dmetal;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

import Deventos.*;
import componentes.*;
import componentes.listeners.*;
import util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DMenuMetalMenuUI
	 extends BasicMenuUI {
  private static boolean instaladoListenerMovimiento = false;
  public DMenuMetalMenuUI() {

  }

  public static ComponentUI createUI(JComponent x) {
	 return new DMenuMetalMenuUI();
  }

  protected MouseInputListener createMouseInputListener(JComponent c) {
	 return new DMouseInputHandler();
  }

  protected void installListeners() {
	 super.installListeners();
  }

  public MenuElement[] getPath() {
	 MenuSelectionManager m = DMenuSelectionManager.defaultManager();
	 MenuElement oldPath[] = m.getSelectedPath();
	 MenuElement newPath[];
	 int i = oldPath.length;
	 if (i == 0) {
		return new MenuElement[0];
	 }
	 Component parent = menuItem.getParent();
	 if (oldPath[i - 1].getComponent() == parent) {
		// The parent popup menu is the last so far
		newPath = new MenuElement[i + 1];
		System.arraycopy(oldPath, 0, newPath, 0, i);
		newPath[i] = menuItem;
	 }
	 else {
		// A sibling menuitem is the current selection
		//
		//  This probably needs to handle 'exit submenu into
		// a menu item.  Search backwards along the current
		// selection until you find the parent popup menu,
		// then copy up to that and add yourself...
		int j;
		for (j = oldPath.length - 1; j >= 0; j--) {
		  if (oldPath[j].getComponent() == parent) {
			 break;
		  }
		}
		newPath = new MenuElement[j + 2];
		System.arraycopy(oldPath, 0, newPath, 0, j + 1);
		newPath[j + 1] = menuItem;
		/*
			  System.out.println("Sibling condition -- ");
			  System.out.println("Old array : ");
			  printMenuElementArray(oldPath, false);
			  System.out.println("New array : ");
			  printMenuElementArray(newPath, false);
		 */
	 }
	 return newPath;
  }

  private static void appendPath(MenuElement[] path, MenuElement elem) {
	 MenuElement newPath[] = new MenuElement[path.length + 1];
	 System.arraycopy(path, 0, newPath, 0, path.length);
	 newPath[path.length] = elem;
	 DMenuSelectionManager.defaultManager().setSelectedPath(newPath);
  }

  private Vector pathToVector(MenuElement[] path) {
	 Vector v = new Vector();
	 int i, j, n;
	 v.add(new Integer(0));
	 for (i = 1; i < path.length; i++) {
		if (path[i] instanceof JPopupMenu) {
		  v.add(new Integer( -5));
		}
		else {
		  MenuElement[] me = path[i - 1].getSubElements();
		  n = -1;
		  for (j = 0; j < me.length; j++) {
			 if (me[j].equals(path[i])) {
				n = j;
			 }
		  }
		  v.add(new Integer(n));
		}
	 }
	 return v;
  }

//******************************************************************************
	private class DMouseInputHandler
		 implements MouseInputListener {
	  public void mouseClicked(MouseEvent e) {}

	  /**
		* Invoked when the mouse has been clicked on the menu. This
		* method clears or sets the selection path of the
		* MenuSelectionManager.
		*
		* @param e the mouse event
		*/
	  public void mousePressed(MouseEvent e) {
		 if (!instaladoListenerMovimiento) {
			Container cnt = menuItem.getParent();
			while (! (cnt instanceof JFrame)) {
			  cnt = cnt.getParent();
			}
			if (cnt instanceof java.awt.Frame) {
			  ( (JFrame) cnt).addComponentListener(new ListenerMovimientoFrame());
			  instaladoListenerMovimiento = true;
			}
		 }

		 JMenu menu = (JMenu) menuItem;
		 if (!menu.isEnabled()) {
			return;
		 }

		 MenuSelectionManager manager =
			  DMenuSelectionManager.defaultManager();
		 if (menu.isTopLevelMenu()) {
			if (menu.isSelected()) {
			  //manager.clearSelectedPath();
			  //*****************************************
				Vector v = ( (DJMenu) menu).getDJMenuListeners();
			  DJMenuEvent evento = new DJMenuEvent();
			  evento.path = new Vector();
			  for (int i = 0; i < v.size(); i++) {
				 ( (DJMenuListener) v.elementAt(i)).cambioEstado(evento);
			  }
			  //*****************************************
			}
			else {
			  Container cnt = menu.getParent();
			  //********************
				/*Component[] comp = cnt.getComponents();
					  for (int i = 0; i < comp.length; i++) {
					  if (comp[i].equals(menu))
				  System.out.println("Abierto menu " + i);
					  }*/
				//********************
			  if (cnt != null && cnt instanceof JMenuBar) {
				 MenuElement me[] = new MenuElement[3];
				 me[0] = (MenuElement) cnt;
				 me[1] = menu;
				 me[2] = menu.getPopupMenu();

				 //manager.setSelectedPath(me);
				 //*****************************************
				 Vector v = ( (DJMenu) menu).getDJMenuListeners();
				 DJMenuEvent evento = new DJMenuEvent();
				 evento.path = pathToVector(me);
				 for (int i = 0; i < v.size(); i++) {
					( (DJMenuListener) v.elementAt(i)).cambioEstado(evento);
				 }
				 //*****************************************
			  }
			}
		 }

		 /*MenuElement selectedPath[] = manager.getSelectedPath();
			 if (selectedPath.length > 0 &&
			selectedPath[selectedPath.length - 1] != menu.getPopupMenu()) {

			if (menu.isTopLevelMenu() ||
			menu.getDelay() == 0) {
			appendPath(selectedPath, menu.getPopupMenu());
			}
			else {
			appendPath(selectedPath, menu.getPopupMenu());
			//setupPostTimer(menu);
			}
			 }*/
	  }

	  /**
		* Invoked when the mouse has been released on the menu. Delegates the
		* mouse event to the MenuSelectionManager.
		*
		* @param e the mouse event
		*/
	  public void mouseReleased(MouseEvent e) {
		 JMenu menu = (JMenu) menuItem;
		 if (!menu.isEnabled()) {
			return;
		 }
		 MenuSelectionManager manager =
			  DMenuSelectionManager.defaultManager();
		 manager.processMouseEvent(e);
		 if (!e.isConsumed()) {
			manager.clearSelectedPath();
		 }
	  }

	  /**
		* Invoked when the cursor enters the menu. This method sets the selected
		* path for the MenuSelectionManager and handles the case
		* in which a menu item is used to pop up an additional menu, as in a
		* hierarchical menu system.
		*
		* @param e the mouse event; not used
		*/
	  public void mouseEntered(MouseEvent e) {
		 JMenu menu = (JMenu) menuItem;
		 if (!menu.isEnabled()) {
			return;
		 }

		 MenuSelectionManager manager =
			  DMenuSelectionManager.defaultManager();
		 MenuElement selectedPath[] = manager.getSelectedPath();
		 if (!menu.isTopLevelMenu()) {
			if (! (selectedPath.length > 0 &&
					 selectedPath[selectedPath.length - 1] ==
					 menu.getPopupMenu())) {

//  appendPath(getPath(), menu.getPopupMenu());
			  //*********************************
				MenuElement[] me = getPath();
			  MenuElement newPath[] = new MenuElement[me.length + 1];
			  System.arraycopy(me, 0, newPath, 0, me.length);
			  newPath[me.length] = menu.getPopupMenu();
			  Vector v = ( (DJMenu) menu).getDJMenuListeners();
			  DJMenuEvent evento = new DJMenuEvent();
			  evento.path = pathToVector(newPath);
			  for (int i = 0; i < v.size(); i++) {
				 ( (DJMenuListener) v.elementAt(i)).cambioEstado(evento);
			  }
			  //*****************************************
				//setupPostTimer(menu);

			}
		 }
		 else {
			if (selectedPath.length > 0 &&
				 selectedPath[0] == menu.getParent()) {
			  MenuElement newPath[] = new MenuElement[3];
			  // A top level menu's parent is by definition
			  // a JMenuBar
			  newPath[0] = (MenuElement) menu.getParent();
			  newPath[1] = menu;
			  newPath[2] = menu.getPopupMenu();

			  //manager.setSelectedPath(newPath);
			  //*****************************************
			  Vector v = ( (DJMenu) menu).getDJMenuListeners();
			  DJMenuEvent evento = new DJMenuEvent();
			  evento.path = pathToVector(newPath);
			  for (int i = 0; i < v.size(); i++) {
				 ( (DJMenuListener) v.elementAt(i)).cambioEstado(evento);
			  }
			  //*****************************************
			}
		 }
	  }

	  public void mouseExited(MouseEvent e) {
	  }

	  public void mouseDragged(MouseEvent e) {
		 // No nos interesan los eventos mouseDragged
		 /*JMenu menu = (JMenu) menuItem;
			 if (!menu.isEnabled())
			return;
			 DMenuSelectionManager.defaultManager().processMouseEvent(e);*/
	  }

	  public void mouseMoved(MouseEvent e) {
	  }
	}

  private class ListenerMovimientoFrame
		extends java.awt.event.ComponentAdapter {

	 public void componentMoved(ComponentEvent e) {
		MenuElement[] path = DMenuSelectionManager.defaultManager().
			 getSelectedPath();
		if (path.length > 0) {
		  DMenuSelectionManager.defaultManager().setSelectedPath(path);
		}
	 }
  }

}
