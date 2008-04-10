/*
 * @(#)MetalTreeUI.java	1.22 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package lookandfeel.Dmetal;

import java.beans.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.tree.*;

import Deventos.*;
import componentes.*;
import interfaces.listeners.*;

/**
 * The metal look and feel implementation of <code>TreeUI</code>.
 * <p>
 * <code>MetalTreeUI</code> allows for configuring how to
 * visually render the spacing and delineation between nodes. The following
 * hints are supported:
 *
 * <table summary="Descriptions of supported hints: Angled, Horizontal, and None">
 *  <tr>
 *    <th><p align="left">Angled</p></th>
 *    <td>A line is drawn connecting the child to the parent. For handling
 *          of the root node refer to
 *          {@link javax.swing.JTree#setRootVisible} and
 *          {@link javax.swing.JTree#setShowsRootHandles}.
 *    </td>
 *  </tr>
 *  <tr>
 *     <th><p align="left">Horizontal</p></th>
 *     <td>A horizontal line is drawn dividing the children of the root node.</td>
 *  </tr>
 *  <tr>
 *      <th><p align="left">None</p></th>
 *      <td>Do not draw any visual indication between nodes.</td>
 *  </tr>
 * </table>
 *
 * <p>
 * As it is typically impratical to obtain the <code>TreeUI</code> from
 * the <code>JTree</code> and cast to an instance of <code>MetalTreeUI</code>
 * you enable this property via the client property
 * <code>JTree.lineStyle</code>. For example, to switch to
 * <code>Horizontal</code> style you would do:
 * <code>tree.putClientProperty("JTree.lineStyle", "Horizontal");</code>
 * <p>
 * The default is <code>Angled</code>.
 *
 * @version 1.22 01/23/03
 * @author Tom Santos
 * @author Steve Wilson (value add stuff)
 */
public class DMetalTreeUI
	 extends BasicTreeUI {

  private static Color lineColor;

  private static final String LINE_STYLE = "JTree.lineStyle";

  private static final String LEG_LINE_STYLE_STRING = "Angled";
  private static final String HORIZ_STYLE_STRING = "Horizontal";
  private static final String NO_STYLE_STRING = "None";

  private static final int LEG_LINE_STYLE = 2;
  private static final int HORIZ_LINE_STYLE = 1;
  private static final int NO_LINE_STYLE = 0;

  private int lineStyle = LEG_LINE_STYLE;
  private PropertyChangeListener lineStyleListener = new LineListener();

  // Boilerplate
  public static ComponentUI createUI(JComponent x) {
	 return new DMetalTreeUI();
  }

  public DMetalTreeUI() {
	 super();
  }

//*************************************************
	protected MouseListener createMouseListener() {
	  return new DMouseHandler();
	}

  /**
	* Nos traduce un TreePath a un array de enteros de forma que en cada posición
	* del array nos indica cual es el elemento seleccionado en ese nivel. Por
	* ejemplo el array {0,2,1} nos indicaria que el hijo seleccionado del raiz
	* es el tercer elemento (ya que se empieza a numerar en 0) y de ese elemento
	* está seleccionado su segundo hijo.
	* @param path TreePath El TreePath que queremos traducir
	* @return int[] El array resultado de la traduccion
	*/
  private int[] pathToArray(TreePath path) {
	 int[] PathNum = new int[path.getPathCount()];
	 Object elemPath = null;
	 Object nodoActual = getModel().getRoot();
	 if (PathNum.length > 0) {
		// El primero siempre a ser 0 pues solo hay una posible raiz
		PathNum[0] = 0;
	 }
	 for (int i = 1; i < PathNum.length; i++) {
		elemPath = path.getPathComponent(i);
		PathNum[i] = buscarEnArbol(elemPath, nodoActual);

		nodoActual = getModel().getChild(nodoActual, PathNum[i]);

	 }
	 return PathNum;
  }

  /**
	* Funcion de apoyo a pathToNumeros. Busca un nodo en un nivel del arbol.
	* @param nodo Object Nodo que queremos buscar
	* @param arbol Object El nodo se buscara entre los hijos de este elemento
	* @return int
	*/
  private int buscarEnArbol(Object nodo, Object arbol) {
	 int index = -1;
	 int numHijos = getModel().getChildCount(arbol);
	 Object hijo = null;
	 for (int i = 0; i < numHijos; i++) {
		hijo = getModel().getChild(arbol, i);
		if (nodo.equals(hijo)) {
		  index = i;
		}
	 }
	 return index;
  }

  private TreePath arrayToPath(int[] array) {
	 Object[] path = new Object[array.length];
	 Object nodoActual = getModel().getRoot();

	 if (array.length > 0) {
		path[0] = getModel().getRoot();
		for (int i = 1; i < array.length; i++) {
		  nodoActual = getModel().getChild(nodoActual, array[i]);
		  path[i] = nodoActual;
		}
	 }

	 return new TreePath(path);
  }

  private Vector arrayToVector(int[] array) {
	 Vector v = new Vector();
	 for (int i = 0; i < array.length; i++) {
		v.add(new Integer(array[i]));
	 }
	 return v;
  }

  private int[] vectorToArray(Vector v) {
	 int array[] = new int[v.size()];
	 for (int i = 0; i < v.size(); i++) {
		array[i] = ( (Integer) v.elementAt(i)).intValue();
	 }
	 return array;
  }

  protected void installKeyboardActions() {
	 //
  }

  public void procesarEvento(DJTreeEvent evento) {
	 int[] array = null;
	 TreePath tp = null;
	 if (evento.tipo.intValue() == DJTreeEvent.APERTURA_CIERRE.intValue()) {
		array = vectorToArray(evento.path);
		tp = arrayToPath(array);
		toggleExpandState(tp);
	 }
	 if (evento.tipo.intValue() == DJTreeEvent.SELECCION.intValue()) {
		array = vectorToArray(evento.path);
		tp = arrayToPath(array);
		tree.setSelectionPath(tp);
	 }
  }

  private void cerrarNodosHijos(TreePath path, Object nodo) {
	 if (!getModel().isLeaf(nodo)) {
		int i, j;
		int numHijos = getModel().getChildCount(nodo);
		int tamPath = path.getPathCount();
		// Obtenemos los hijos
		Object[] hijos = new Object[numHijos];
		for (i = 0; i < numHijos; i++) {
		  hijos[i] = getModel().getChild(nodo, i);
		}
		// Obtenemos los elementos del path
		Object[] objpath = new Object[tamPath + 1];
		for (j = 0; j < tamPath; j++) {
		  objpath[j] = path.getPathComponent(j);
		}

		for (i = 0; i < numHijos; i++) {
		  objpath[tamPath] = hijos[i];
		  cerrarNodosHijos(new TreePath(objpath), hijos[i]);
		}
		if (tree.isExpanded(path)) {
		  tree.collapsePath(path);
		}
	 }
  }

  protected void toggleExpandState(TreePath path) {
	 if (tree.isExpanded(path)) {
		cerrarNodosHijos(path, path.getLastPathComponent());
	 }
	 else {
		super.toggleExpandState(path);
	 }

	 /*	 Vector todo = getEstado();
		Vector v = (Vector)todo.elementAt(1);
		System.out.println("----------------------");
		for (int i = 0; i < v.size(); i++) {
		Vector aux = (Vector) v.elementAt(i);
		for (int j = 0; j < aux.size(); j++) {
		  Integer in = (Integer) aux.elementAt(j);
		  System.out.print(in.intValue() + ",");
		}
		System.out.println("");
		}
		System.out.println("----------------------");*/
  }

  public Vector getEstado() {
	 Vector v = new Vector();
	 Vector expandidos = new Vector();
	 Vector seleccionado = null;
	 aniadirExpandidas(expandidos, new TreePath(new Object[] {getModel().getRoot()}));
	 TreePath pathSelec = tree.getSelectionPath();
	 if (pathSelec != null) {
		seleccionado = arrayToVector(pathToArray(tree.getSelectionPath()));
	 }
	 else {
		seleccionado = new Vector();
	 }
	 v.add(seleccionado);
	 v.add(expandidos);

	 return v;
  }

  public void setEstado(Vector v) {
	 Vector seleccionado = (Vector) v.elementAt(0);
	 Vector expandidos = (Vector) v.elementAt(1);

	 for (int i = 0; i < expandidos.size(); i++) {
		Vector aux = (Vector) expandidos.elementAt(i);
		tree.expandPath(arrayToPath(vectorToArray(aux)));
	 }

	 if (seleccionado.size() > 0) {
		tree.setSelectionPath(arrayToPath(vectorToArray(seleccionado)));
	 }
  }

  private void aniadirExpandidas(Vector v, TreePath path) {
	 Object nodoActual = path.getLastPathComponent();
	 if (!getModel().isLeaf(nodoActual)) {
		int i, j;
		int numHijos = getModel().getChildCount(nodoActual);
		int tamPath = path.getPathCount();
		// Obtenemos los hijos
		Object[] hijos = new Object[numHijos];
		for (i = 0; i < numHijos; i++) {
		  hijos[i] = getModel().getChild(nodoActual, i);
		}
		// Obtenemos los elementos del path
		Object[] objpath = new Object[tamPath + 1];
		for (j = 0; j < tamPath; j++) {
		  objpath[j] = path.getPathComponent(j);
		}

		for (i = 0; i < numHijos; i++) {
		  objpath[tamPath] = hijos[i];
		  aniadirExpandidas(v, new TreePath(objpath));
		}

		if (tree.isExpanded(path)) {
		  Vector lista = arrayToVector(pathToArray(path));
		  v.add(lista);
		}
	 }

  }

//*************************************************

	protected int getHorizontalLegBuffer() {
	  return 4;
	}

  public void installUI(JComponent c) {
	 super.installUI(c);
	 lineColor = UIManager.getColor("Tree.line");

	 Object lineStyleFlag = c.getClientProperty(LINE_STYLE);
	 decodeLineStyle(lineStyleFlag);
	 c.addPropertyChangeListener(lineStyleListener);

  }

  public void uninstallUI(JComponent c) {
	 c.removePropertyChangeListener(lineStyleListener);
	 super.uninstallUI(c);
  }

  /** this function converts between the string passed into the client property
	* and the internal representation (currently and int)
	*
	*/
  protected void decodeLineStyle(Object lineStyleFlag) {
	 if (lineStyleFlag == null ||
		  lineStyleFlag.equals(LEG_LINE_STYLE_STRING)) {
		lineStyle = LEG_LINE_STYLE; // default case
	 }
	 else {
		if (lineStyleFlag.equals(NO_STYLE_STRING)) {
		  lineStyle = NO_LINE_STYLE;
		}
		else if (lineStyleFlag.equals(HORIZ_STYLE_STRING)) {
		  lineStyle = HORIZ_LINE_STYLE;
		}
	 }

  }

  protected boolean isLocationInExpandControl(int row, int rowLevel,
															 int mouseX, int mouseY) {
	 if (tree != null && !isLeaf(row)) {
		int boxWidth;

		if (getExpandedIcon() != null) {
		  boxWidth = getExpandedIcon().getIconWidth() + 6;
		}
		else {
		  boxWidth = 8;

		}
		Insets i = tree.getInsets();
		int boxLeftX = (i != null) ? i.left : 0;

		boxLeftX += ( ( (rowLevel + depthOffset - 1) * totalChildIndent) +
						 getLeftChildIndent()) - boxWidth / 2;

		int boxRightX = boxLeftX + boxWidth;

		return mouseX >= boxLeftX && mouseX <= boxRightX;
	 }
	 return false;
  }

  public void paint(Graphics g, JComponent c) {
	 super.paint(g, c);

	 // Paint the lines
	 if (lineStyle == HORIZ_LINE_STYLE && !largeModel) {
		paintHorizontalSeparators(g, c);
	 }
  }

  protected void paintHorizontalSeparators(Graphics g, JComponent c) {
	 g.setColor(lineColor);

	 Rectangle clipBounds = g.getClipBounds();

	 int beginRow = getRowForPath(tree, getClosestPathForLocation
											(tree, 0, clipBounds.y));
	 int endRow = getRowForPath(tree, getClosestPathForLocation
										 (tree, 0, clipBounds.y + clipBounds.height - 1));

	 if (beginRow <= -1 || endRow <= -1) {
		return;
	 }

	 for (int i = beginRow; i <= endRow; ++i) {
		TreePath path = getPathForRow(tree, i);

		if (path != null && path.getPathCount() == 2) {
		  Rectangle rowBounds = getPathBounds(tree, getPathForRow
														  (tree, i));

		  // Draw a line at the top
		  if (rowBounds != null) {
			 g.drawLine(clipBounds.x, rowBounds.y,
							clipBounds.x + clipBounds.width, rowBounds.y);
		  }
		}
	 }

  }

  protected void paintVerticalPartOfLeg(Graphics g, Rectangle clipBounds,
													 Insets insets, TreePath path) {
	 if (lineStyle == LEG_LINE_STYLE) {
		super.paintVerticalPartOfLeg(g, clipBounds, insets, path);
	 }
  }

  protected void paintHorizontalPartOfLeg(Graphics g, Rectangle clipBounds,
														Insets insets, Rectangle bounds,
														TreePath path, int row,
														boolean isExpanded,
														boolean hasBeenExpanded, boolean
														isLeaf) {
	 if (lineStyle == LEG_LINE_STYLE) {
		super.paintHorizontalPartOfLeg(g, clipBounds, insets, bounds,
												 path, row, isExpanded,
												 hasBeenExpanded, isLeaf);
	 }
  }

  /** This class listens for changes in line style */
  class LineListener
		implements PropertyChangeListener {
	 public void propertyChange(PropertyChangeEvent e) {
		String name = e.getPropertyName();
		if (name.equals(LINE_STYLE)) {
		  decodeLineStyle(e.getNewValue());
		}
	 }
  } // end class PaletteListener

//**********************************************
	private class DMouseHandler
		 extends MouseAdapter
		 implements MouseMotionListener {
	  /**
		* Invoked when a mouse button has been pressed on a component.
		*/
	  public void mousePressed(MouseEvent e) {
		 if (!e.isConsumed()) {
			handleSelection(e);
			selectedOnPress = true;
		 }
		 else {
			selectedOnPress = false;
		 }
	  }

	  void handleSelection(MouseEvent e) {
		 if (tree != null && tree.isEnabled()) {
			if (isEditing(tree) && tree.getInvokesStopCellEditing() &&
				 !stopEditing(tree)) {
			  return;
			}

			if (tree.isRequestFocusEnabled()) {
			  tree.requestFocus();
			}

			TreePath path = getClosestPathForLocation(tree, e.getX(), e.getY());
			/***
				int[] ax = pathToNumeros(path);
				for(int k=0; k<ax.length; k++){
				  System.out.print(ax[k]+",");
			 }
				System.out.println("");
//***/

			if (path != null) {
			  Rectangle bounds = getPathBounds(tree, path);

			  if (e.getY() > (bounds.y + bounds.height)) {
				 return;
			  }

			  // Preferably checkForClickInExpandControl could take
			  // the Event to do this it self!
			  if (SwingUtilities.isLeftMouseButton(e)) {
				 //checkForClickInExpandControl(path, e.getX(), e.getY());
				 if (isLocationInExpandControl(path, e.getX(), e.getY())) {

					//handleExpandControlClick(path, e.getX(), e.getY());

					// Notificamos a los listeners el evento
					Vector v = ( (DJTree) tree).getDJTreeListeners();
					DJTreeEvent ev = new DJTreeEvent();
					ev.tipo = new Integer(DJTreeEvent.APERTURA_CIERRE.intValue());
					int[] pathNumeros = pathToArray(path);
					ev.path = arrayToVector(pathNumeros);
					for (int i = 0; i < v.size(); i++) {
					  ( (DJTreeListener) v.elementAt(i)).apertura_cierre(ev);
					}
				 }
			  }

			  int x = e.getX();

			  // Perhaps they clicked the cell itself. If so,
			  // select it.
			  if (x > bounds.x) {
				 if (x <= (bounds.x + bounds.width) &&
					  !startEditing(path, e)) {
					//selectPathForEvent(path, e);

					// Notificamos a los listeners el evento
					Vector v = ( (DJTree) tree).getDJTreeListeners();
					DJTreeEvent ev = new DJTreeEvent();
					ev.tipo = new Integer(DJTreeEvent.SELECCION.intValue());
					int[] pathNumeros = pathToArray(path);
					ev.path = arrayToVector(pathNumeros);
					for (int i = 0; i < v.size(); i++) {
					  ( (DJTreeListener) v.elementAt(i)).seleccion(ev);
					}
					/*if(isToggleEvent(e)){
					  ev.tipo = new Integer(DJTreeEvent.APERTURA_CIERRE.intValue());
					  for (int i = 0; i < v.size(); i++) {
					  ( (DJTreeListener) v.elementAt(i)).apertura_cierre(ev);
					  }
							}*/

				 }
			  }
			}
		 }
	  }

	  public void mouseDragged(MouseEvent e) {
	  }

	  /**
		* Invoked when the mouse button has been moved on a component
		* (with no buttons no down).
		*/
	  public void mouseMoved(MouseEvent e) {
	  }

	  public void mouseReleased(MouseEvent e) {
		 if ( (!e.isConsumed()) && (!selectedOnPress)) {
			handleSelection(e);
		 }
	  }

	  boolean selectedOnPress;
	} // End of BasicTreeUI.MouseHandler

}
