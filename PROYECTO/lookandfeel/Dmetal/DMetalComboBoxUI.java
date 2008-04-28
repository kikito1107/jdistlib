package lookandfeel.Dmetal;

import java.beans.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

import Deventos.*;
import componentes.*;
import componentes.base.DJComboBox;
import componentes.listeners.*;

/**
 * Metal UI for JComboBox
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans<sup><font size="-2">TM</font></sup>
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @see MetalComboBoxEditor
 * @see MetalComboBoxButton
 * @version 1.42 01/23/03
 * @author Tom Santos
 */
public class DMetalComboBoxUI
	 extends BasicComboBoxUI {
  public DMetalComboPopup mcp = null;

  // Con este indice indicamos que se ha enviado la peticion de seleccionar
  // ese indice de la lista aunque aun no se ha seleccionado
  // asi evitamos envios innecesarios en el metodo updateListBoxSelectionForEvent
  private int indiceVirtual = -1;

//************************************************************
	protected void installKeyboardActions() {
	  // Dejando el contenido de este método vacío hacemos que el componente
	  // no responda a eventos del teclado
	}

  protected void installListeners() {
	 super.installListeners();
	 //System.out.println("DMetalComboBoxUI: Instalados listeners");
  }

  public void setItemLista(int index) {
	 mcp.setItemLista(index);
	 indiceVirtual = index;
  }

  public int getItemSeleccionado() {
	 return mcp.getItemSeleccionado();
  }

  public void setPopupVisible(JComboBox c, boolean v) {
	 if (v) {
		mcp.show();
	 }
	 else {
		mcp.hide();
	 }
	 mcp.repaint();
  }

//************************************************************

	public static ComponentUI createUI(JComponent c) {
	  return new DMetalComboBoxUI();
	}

  public void paint(Graphics g, JComponent c) {
  }

  protected ComboBoxEditor createEditor() {
	 return new MetalComboBoxEditor.UIResource();
  }

  protected ComboPopup createPopup() {
	 //System.out.println("DMetalComboBoxUI: createPopup()");
	 mcp = new DMetalComboPopup(comboBox);
	 //mcp.setUI(new lookandfeel.Dmetal.MetalPopupMenuUI());
	 return mcp;
  }

  protected JButton createArrowButton() {
	 JButton button = new MetalComboBoxButton(comboBox,
															new MetalComboBoxIcon(),
															comboBox.isEditable(),
															currentValuePane,
															listBox);
	 button.setMargin(new Insets(0, 1, 1, 3));
	 return button;
  }

  public PropertyChangeListener createPropertyChangeListener() {
	 return new DMetalPropertyChangeListener();
  }

  /**
	* This inner class is marked &quot;public&quot; due to a compiler bug.
	* This class should be treated as a &quot;protected&quot; inner class.
	* Instantiate it only within subclasses of <FooUI>.
	*/
  public class DMetalPropertyChangeListener
		extends BasicComboBoxUI.PropertyChangeHandler {
	 public void propertyChange(PropertyChangeEvent e) {
		super.propertyChange(e);
		String propertyName = e.getPropertyName();

		if (propertyName.equals("editable")) {
		  MetalComboBoxButton button = (MetalComboBoxButton) arrowButton;
		  button.setIconOnly(comboBox.isEditable());
		  comboBox.repaint();
		}
		else if (propertyName.equals("background")) {
		  Color color = (Color) e.getNewValue();
		  arrowButton.setBackground(color);
		  listBox.setBackground(color);

		}
		else if (propertyName.equals("foreground")) {
		  Color color = (Color) e.getNewValue();
		  arrowButton.setForeground(color);
		  listBox.setForeground(color);
		}
	 }
  }

  /**
	* As of Java 2 platform v1.4 this method is no longer used. Do not call or
	* override. All the functionality of this method is in the
	* MetalPropertyChangeListener.
	*
	* @deprecated As of Java 2 platform v1.4.
	*/
  protected void editablePropertyChanged(PropertyChangeEvent e) {}

  protected LayoutManager createLayoutManager() {
	 return new DMetalComboBoxLayoutManager();
  }

  /**
	* This inner class is marked &quot;public&quot; due to a compiler bug.
	* This class should be treated as a &quot;protected&quot; inner class.
	* Instantiate it only within subclasses of <FooUI>.
	*/
  public class DMetalComboBoxLayoutManager
		extends BasicComboBoxUI.ComboBoxLayoutManager {
	 public void layoutContainer(Container parent) {
		layoutComboBox(parent, this);
	 }

	 public void superLayout(Container parent) {
		super.layoutContainer(parent);
	 }
  }

  // This is here because of a bug in the compiler.
  // When a protected-inner-class-savvy compiler comes out we
  // should move this into MetalComboBoxLayoutManager.
  public void layoutComboBox(Container parent,
									  DMetalComboBoxLayoutManager manager) {
	 if (comboBox.isEditable()) {
		manager.superLayout(parent);
	 }
	 else {
		if (arrowButton != null) {
		  Insets insets = comboBox.getInsets();
		  int width = comboBox.getWidth();
		  int height = comboBox.getHeight();
		  arrowButton.setBounds(insets.left, insets.top,
										width - (insets.left + insets.right),
										height - (insets.top + insets.bottom));
		}
	 }
  }

  /**
	* As of Java 2 platform v1.4 this method is no
	* longer used.
	*
	* @deprecated As of Java 2 platform v1.4.
	*/
  protected void removeListeners() {
	 if (propertyChangeListener != null) {
		comboBox.removePropertyChangeListener(propertyChangeListener);
	 }
  }

  // These two methods were overloaded and made public. This was probably a
  // mistake in the implementation. The functionality that they used to
  // provide is no longer necessary and should be removed. However,
  // removing them will create an uncompatible API change.

  public void configureEditor() {
	 super.configureEditor();
  }

  public void unconfigureEditor() {
	 super.unconfigureEditor();
  }

  public Dimension getMinimumSize(JComponent c) {
	 if (!isMinimumSizeDirty) {
		return new Dimension(cachedMinimumSize);
	 }

	 Dimension size = null;

	 if (!comboBox.isEditable() &&
		  arrowButton != null &&
		  arrowButton instanceof MetalComboBoxButton) {

		MetalComboBoxButton button = (MetalComboBoxButton) arrowButton;
		Insets buttonInsets = button.getInsets();
		Insets insets = comboBox.getInsets();

		size = getDisplaySize();
		size.width += insets.left + insets.right;
		size.width += buttonInsets.left + buttonInsets.right;
		size.width += buttonInsets.right + button.getComboIcon().getIconWidth();
		size.height += insets.top + insets.bottom;
		size.height += buttonInsets.top + buttonInsets.bottom;
	 }
	 else if (comboBox.isEditable() &&
				 arrowButton != null &&
				 editor != null) {
		size = super.getMinimumSize(c);
		Insets margin = arrowButton.getMargin();
		size.height += margin.top + margin.bottom;
		size.width += margin.left + margin.right;
	 }
	 else {
		size = super.getMinimumSize(c);
	 }

	 cachedMinimumSize.setSize(size.width, size.height);
	 isMinimumSizeDirty = false;

	 return new Dimension(cachedMinimumSize);
  }

  /**
	* This inner class is marked &quot;public&quot; due to a compiler bug.
	* This class should be treated as a &quot;protected&quot; inner class.
	* Instantiate it only within subclasses of <FooUI>.
	*
	* This class is now obsolete and doesn't do anything and
	* is only included for backwards API compatibility. Do not call or
	* override.
	*
	* deprecated As of Java 2 platform v1.4.
	*/
  public class DMetalComboPopup
		extends BasicComboPopup {
	 private static final String DuiClassID = "DDD";

	 int i = 0;

	 public DMetalComboPopup(JComboBox cBox) {
		super(cBox);
		// Cambiamos la UI de la lista
		list.setUI(new MetalComboListUI());
		//this.setUI(new MetalPopupMenuUI());
	 }

	 public String getUIClassID() {
		return DuiClassID;
	 }

	 public void setItemLista(int index) {
		list.setSelectedIndex(index);
		list.ensureIndexIsVisible(index);
	 }

	 public int getItemSeleccionado() {
		return list.getSelectedIndex();
	 }

	 public void setVisible(boolean b) {
		super.setVisible(b);
		//System.out.println("DMEtalComboBoxUI: setVisible("+b+")");
	 }

	 public void mostrarPopup() {
		super.setVisible(true);
	 }

	 public void ocultarPopup() {
		super.setVisible(false);
	 }

	 /* public void hide(){
	  super.hide();
	  System.out.println("DMetalComboBoxUI: hide() "+i++);

	  }*/

	 public void menuSelectionChanged(boolean isIncluded) {
		//super.menuSelectionChanged(isIncluded);
		System.out.println("menu selection changed");
	 }

//******************
	  protected MouseListener createMouseListener() {
		 //System.out.println("Instalado MouseListener");
		 return new DInvocationMouseHandler();
	  }

	 protected MouseMotionListener createMouseMotionListener() {
		//System.out.println("Instalado MouseMotionListener");
		return new DInvocationMouseMotionHandler();
	 }

	 protected MouseListener createListMouseListener() {
		//System.out.println("Instalado ListMouseListener");
		return new DListMouseHandler();
	 }

	 protected MouseMotionListener createListMouseMotionListener() {
		//System.out.println("Instalado ListMouseMotionListener");
		return new DListMouseMotionHandler();
	 }

	 /**
	  * Este metodo es el que decide la cuando cambiar la selección de la lista.
	  * Por ejemplo cuando mueves el raton de un elemento de la lista a otro
	  * @param anEvent MouseEvent
	  * @param shouldScroll boolean
	  */
	 protected void updateListBoxSelectionForEvent(MouseEvent anEvent,
																  boolean shouldScroll) {
		// XXX - only seems to be called from this class. shouldScroll flag is
		// never true
		Point location = anEvent.getPoint();
		if (list == null) {
		  return;
		}
		int index = list.locationToIndex(location);
		if (index == -1) {
		  if (location.y < 0) {
			 index = 0;
		  }
		  else {
			 index = comboBox.getModel().getSize() - 1;
		  }
		}

		if (list.getSelectedIndex() != index && index != indiceVirtual) {
		  System.out.println("DMetalComboBoxUI: Cambio selección a item " + index);
		  indiceVirtual = index;
		  /*list.setSelectedIndex(index);
				 if (shouldScroll)
				 list.ensureIndexIsVisible(index);*/

		  DJComboBoxEvent evento = new DJComboBoxEvent();
		  evento.seleccionLista = new Integer(index);
		  evento.tipo = new Integer(DJComboBoxEvent.CAMBIO_SELECCION_LISTA.
											 intValue());
		  Vector v = ( (DJComboBox) comboBox).getDJComboBoxListeners();
		  for (int i = 0; i < v.size(); i++) {
			 ( (DJComboBoxListener) v.elementAt(i)).cambioSeleccionLista(evento);
		  }
		}

	 }

//************************
	  // This method was overloaded and made public. This was probably
	  // mistake in the implementation. The functionality that they used to
	  // provide is no longer necessary and should be removed. However,
	  // removing them will create an uncompatible API change.

	  public void delegateFocus(MouseEvent e) {
		 super.delegateFocus(e);
	  }

	 /**
	  * This listener hides the popup when the mouse is released in the list.
	  */
	 protected class DListMouseHandler
		  extends MouseAdapter {
		public DListMouseHandler() {
		  //System.out.println("Creada instancia DListMouseHandler");
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent anEvent) {
//		  comboBox.setSelectedIndex(list.getSelectedIndex());

		  DJComboBoxEvent evento = new DJComboBoxEvent();
		  evento.itemSeleccionado = new Integer(list.getSelectedIndex());
		  evento.tipo = new Integer(DJComboBoxEvent.SELECCIONADO.intValue());
		  Vector v = ( (DJComboBox) comboBox).getDJComboBoxListeners();
		  for (int i = 0; i < v.size(); i++) {
			 ( (DJComboBoxListener) v.elementAt(i)).seleccion(evento);
			 System.out.println(
				  "DListMouseHandler: mouseReleased. Informado listner " + i);
		  }

//		  comboBox.setPopupVisible(false);
		  // workaround for cancelling an edited item (bug 4530953)
		  if (comboBox.isEditable() && comboBox.getEditor() != null) {
			 comboBox.configureEditor(comboBox.getEditor(),
											  comboBox.getSelectedItem());
		  }

		  System.out.println("DListMouseHandler: mouseReleased");
		}
	 }

	 /**
	  * This listener changes the selected item as you move the mouse over the list.
	  * The selection change is not committed to the model, this is for user feedback only.
	  */
	 protected class DListMouseMotionHandler
		  extends MouseMotionAdapter {

		public DListMouseMotionHandler() {
		  //System.out.println("Creada instancia DListMouseMotionHandler");
		}

		public void mouseMoved(MouseEvent anEvent) {
		  Point location = anEvent.getPoint();
		  Rectangle r = new Rectangle();
		  list.computeVisibleRect(r);
		  if (r.contains(location)) {
			 updateListBoxSelectionForEvent(anEvent, false);
		  }
		  //System.out.println("DListMouseMotionHandler: mouseMoved");
		}
	 }

	 /**
	  * A listener to be registered upon the combo box
	  * (<em>not</em> its popup menu)
	  * to handle mouse events
	  * that affect the state of the popup menu.
	  * The main purpose of this listener is to make the popup menu
	  * appear and disappear.
	  * This listener also helps
	  * with click-and-drag scenarios by setting the selection if the mouse was
	  * released over the list during a drag.
	  *
	  * <p>
	  * <strong>Warning:</strong>
	  * We recommend that you <em>not</em>
	  * create subclasses of this class.
	  * If you absolutely must create a subclass,
	  * be sure to invoke the superclass
	  * version of each method.
	  *
	  * @see BasicComboPopup#createMouseListener
	  */
	 protected class DInvocationMouseHandler
		  extends MouseAdapter {
		int i = 0;

		public DInvocationMouseHandler() {
		  //System.out.println("Creada instancia DInvocationMouseHandler");
		}

		public void mouseDragged(MouseEvent e) {
		  //System.out.println("DInvocationMouseHandler: mouseDragged " + i++);
		  if (isVisible()) {
			 MouseEvent newEvent = convertMouseEvent(e);
			 Rectangle r = new Rectangle();
			 list.computeVisibleRect(r);

			 if (newEvent.getPoint().y >= r.y &&
				  newEvent.getPoint().y <= r.y + r.height - 1) {
				hasEntered = true;
				if (isAutoScrolling) {
				  stopAutoScrolling();
				}
				Point location = newEvent.getPoint();
				if (r.contains(location)) {
				  updateListBoxSelectionForEvent(newEvent, false);
				}
			 }
			 else {
				if (hasEntered) {
				  int directionToScroll = newEvent.getPoint().y < r.y ? SCROLL_UP :
						SCROLL_DOWN;
				  if (isAutoScrolling && scrollDirection != directionToScroll) {
					 stopAutoScrolling();
					 startAutoScrolling(directionToScroll);
				  }
				  else if (!isAutoScrolling) {
					 startAutoScrolling(directionToScroll);
				  }
				}
				else {
				  if (e.getPoint().y < 0) {
					 hasEntered = true;
					 startAutoScrolling(SCROLL_UP);
				  }
				}
			 }
		  }
		}

		/**
		 * Responds to mouse-pressed events on the combo box.
		 *
		 * @param e the mouse-press event to be handled
		 */
		public void mousePressed(MouseEvent e) {
		  if (!SwingUtilities.isLeftMouseButton(e) || !comboBox.isEnabled()) {
			 return;
		  }

		  if (comboBox.isEditable()) {
			 Component comp = comboBox.getEditor().getEditorComponent();
			 if ( (! (comp instanceof JComponent)) ||
				  ( (JComponent) comp).isRequestFocusEnabled()) {
				comp.requestFocus();
			 }
		  }
		  else if (comboBox.isRequestFocusEnabled()) {
			 //comboBox.requestFocus();
		  }

		  //togglePopup();

		  if (mcp.isVisible()) {
			 Vector v = ( (DJComboBox) comboBox).getDJComboBoxListeners();
			 for (int i = 0; i < v.size(); i++) {
				( (DJComboBoxListener) v.elementAt(i)).cerrado();
			 }
		  }
		  else {
			 Vector v = ( (DJComboBox) comboBox).getDJComboBoxListeners();
			 for (int i = 0; i < v.size(); i++) {
				( (DJComboBoxListener) v.elementAt(i)).abierto();
			 }
		  }

		  //System.out.println("DInvocationMouseHandler: mousePressed");
		}

		/**
		 * Responds to the user terminating
		 * a click or drag that began on the combo box.
		 *
		 * @param e the mouse-release event to be handled
		 */
		public void mouseReleased(MouseEvent e) {
		  //System.out.println("DInvocationMouseHandler: Mouse Released");
		  Component source = (Component) e.getSource();
		  Dimension size = source.getSize();
		  Rectangle bounds = new Rectangle(0, 0, size.width - 1, size.height - 1);
		  if (!bounds.contains(e.getPoint())) {
			 MouseEvent newEvent = convertMouseEvent(e);
			 Point location = newEvent.getPoint();
			 Rectangle r = new Rectangle();
			 list.computeVisibleRect(r);
			 if (r.contains(location)) {
				//comboBox.setSelectedIndex(list.getSelectedIndex());
				DJComboBoxEvent evento = new DJComboBoxEvent();
				evento.itemSeleccionado = new Integer(list.getSelectedIndex());
				Vector v = ( (DJComboBox) comboBox).getDJComboBoxListeners();
				for (int i = 0; i < v.size(); i++) {
				  ( (DJComboBoxListener) v.elementAt(i)).seleccion(evento);
				  /*System.out.println(
						"DListMouseMotionHandler: mouseReleased. Informado listner " +
						i);*/
				}
			 }
			 //comboBox.setPopupVisible(false);
		  }
		  hasEntered = false;
		  stopAutoScrolling();
		  //System.out.println("DInvocationMouseHandler: mouseReleased");
		}
	 }

	 /**
	  * This listener watches for dragging and updates the current selection in the
	  * list if it is dragging over the list.
	  */
	 protected class DInvocationMouseMotionHandler
		  extends MouseMotionAdapter {
		int i = 0;

		public DInvocationMouseMotionHandler() {
		  //System.out.println("Creada instancia DInvocationMouseMotionHandler");
		}

		public void mouseDragged(MouseEvent e) {
		  //System.out.println("DInvocationMouseMotionHandler: mouseDragged " + i++);
		  if (isVisible()) {
			 MouseEvent newEvent = convertMouseEvent(e);
			 Rectangle r = new Rectangle();
			 list.computeVisibleRect(r);

			 if (newEvent.getPoint().y >= r.y &&
				  newEvent.getPoint().y <= r.y + r.height - 1) {
				hasEntered = true;
				if (isAutoScrolling) {
				  stopAutoScrolling();
				}
				Point location = newEvent.getPoint();
				if (r.contains(location)) {
				  updateListBoxSelectionForEvent(newEvent, false);
				}
			 }
			 else {
				if (hasEntered) {
				  int directionToScroll = newEvent.getPoint().y < r.y ? SCROLL_UP :
						SCROLL_DOWN;
				  if (isAutoScrolling && scrollDirection != directionToScroll) {
					 stopAutoScrolling();
					 startAutoScrolling(directionToScroll);
				  }
				  else if (!isAutoScrolling) {
					 startAutoScrolling(directionToScroll);
				  }
				}
				else {
				  if (e.getPoint().y < 0) {
					 hasEntered = true;
					 startAutoScrolling(SCROLL_UP);
				  }
				}
			 }
		  }
		  //System.out.println("DInvocationMouseMotionHandler: mouseDragged");
		}
	 }

  }
}
