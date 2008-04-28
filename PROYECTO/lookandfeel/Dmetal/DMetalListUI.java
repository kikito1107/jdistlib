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

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DMetalListUI
	 extends BasicListUI {
  public DMetalListUI() {
  }

  public static ComponentUI createUI(JComponent list) {
	 return new DMetalListUI();
  }

  protected MouseInputListener createMouseInputListener() {
	 return new MouseInputHandler();
  }

  protected void installListeners() {

	 focusListener = createFocusListener();
	 mouseInputListener = createMouseInputListener();
	 propertyChangeListener = createPropertyChangeListener();
	 listSelectionListener = createListSelectionListener();
	 listDataListener = createListDataListener();

	 list.addFocusListener(focusListener);
	 list.addMouseListener(mouseInputListener);
	 list.addMouseMotionListener(mouseInputListener);
	 list.addPropertyChangeListener(propertyChangeListener);

	 ListModel model = list.getModel();
	 if (model != null) {
		model.addListDataListener(listDataListener);
	 }

	 ListSelectionModel selectionModel = list.getSelectionModel();
	 if (selectionModel != null) {
		selectionModel.addListSelectionListener(listSelectionListener);
	 }
  }

  protected void installKeyboardActions() {
  }

  private int convertirLocationToRow(int x, int y0, boolean closest) {
	 int size = list.getModel().getSize();

	 if (size <= 0) {
		return -1;
	 }
	 Insets insets = list.getInsets();
	 if (cellHeights == null) {
		int row = (cellHeight == 0) ? 0 :
			 ( (y0 - insets.top) / cellHeight);
		if (closest) {
		  if (row < 0) {
			 row = 0;
		  }
		  else if (row >= size) {
			 row = size - 1;
		  }
		}
		return row;
	 }
	 else if (size > cellHeights.length) {
		return -1;
	 }
	 else {
		int y = insets.top;
		int row = 0;

		if (closest && y0 < y) {
		  return 0;
		}
		int i;
		for (i = 0; i < size; i++) {
		  if ( (y0 >= y) && (y0 < y + cellHeights[i])) {
			 return row;
		  }
		  y += cellHeights[i];
		  row += 1;
		}
		return i - 1;
	 }
  }

  private int convertirLocationToModel(int x, int y) {
	 int row = convertirLocationToRow(x, y, true);
	 if (row >= 0) {
		return row;
	 }
	 return -1;
  }

  public class MouseInputHandler
		implements MouseInputListener {
	 public void mouseClicked(MouseEvent e) {}

	 public void mouseEntered(MouseEvent e) {}

	 public void mouseExited(MouseEvent e) {}

	 public void mousePressed(MouseEvent e) {
		if (e.isConsumed()) {
		  selectedOnPress = false;
		  return;
		}
		selectedOnPress = true;
		adjustFocusAndSelection(e);
	 }

	 void adjustFocusAndSelection(MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) {
		  return;
		}

		if (!list.isEnabled()) {
		  return;
		}

		/* Request focus before updating the list selection.  This implies
		 * that the current focus owner will see a focusLost() event
		 * before the lists selection is updated IF requestFocus() is
		 * synchronous (it is on Windows).  See bug 4122345
		 */
		if (!list.hasFocus() && list.isRequestFocusEnabled()) {
		  list.requestFocus();
		}

		int row = convertirLocationToModel(e.getX(), e.getY());
		if (row != -1) {
		  DJListEvent evento = new DJListEvent(row);
		  // Notificamos el cambio de posicion a los listeners
		  Vector v = ( (DJList) list).getDJListListeners();
		  evento.tipo = new Integer(DJListEvent.CAMBIO_POSICION.intValue());
		  for (int i = 0; i < v.size(); i++) {
			 ( (DJListListener) v.elementAt(i)).cambioPosicion(evento);
		  }

		  //list.setSelectedIndex(row);


		  /*boolean adjusting = (e.getID() == MouseEvent.MOUSE_PRESSED) ? true : false;
				 list.setValueIsAdjusting(adjusting);
				 int anchorIndex = list.getAnchorSelectionIndex();
				 if (e.isControlDown()) {
				 if (list.isSelectedIndex(row)) {
				 list.removeSelectionInterval(row, row);
				 }
				 else {
				 list.addSelectionInterval(row, row);
				 }
				 }
				 else if (e.isShiftDown() && (anchorIndex != -1)) {
				 list.setSelectionInterval(anchorIndex, row);
				 }
				 else {
				 list.setSelectionInterval(row, row);
				 }*/
		}
	 }

	 public void mouseDragged(MouseEvent e) {
		/*if (e.isConsumed()) {
		  return;
			}
			if (!SwingUtilities.isLeftMouseButton(e)) {
		  return;
			}
			if (!list.isEnabled()) {
		  return;
			}
			if (e.isShiftDown() || e.isControlDown()) {
		  return;
			}

			int row = convertirLocationToModel(e.getX(), e.getY());
			if (row != -1) {
		  Rectangle cellBounds = getCellBounds(list, row, row);
		  if (cellBounds != null) {
		  list.scrollRectToVisible(cellBounds);
		  list.setSelectionInterval(row, row);
		  }
			}*/
	 }

	 public void mouseMoved(MouseEvent e) {
	 }

	 public void mouseReleased(MouseEvent e) {
		/*if (selectedOnPress) {
		  if (!SwingUtilities.isLeftMouseButton(e)) {
		  return;
		  }

		  list.setValueIsAdjusting(false);
			}
			else {
		  adjustFocusAndSelection(e);
			}*/
	 }

	 private boolean selectedOnPress;
  }

}
