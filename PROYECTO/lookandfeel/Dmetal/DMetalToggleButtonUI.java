/*
 * @(#)MetalToggleButtonUI.java	1.21 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package lookandfeel.Dmetal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.ButtonModel;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentInputMapUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicToggleButtonUI;


import componentes.base.DJToggleButton;
import componentes.listeners.DJToggleButtonListener;
import Deventos.DJToggleButtonEvent;

/**
 * MetalToggleButton implementation
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 * 
 * @version 1.21 01/23/03
 * @author Tom Santos
 */
@SuppressWarnings("unchecked")
public class DMetalToggleButtonUI extends BasicToggleButtonUI
{

	private static final DMetalToggleButtonUI metalToggleButtonUI = new DMetalToggleButtonUI();

	protected Color focusColor;

	protected Color selectColor;

	protected Color disabledTextColor;

	private boolean defaults_initialized = false;

	// ********************************
	// Create PLAF
	// ********************************
	public static ComponentUI createUI(JComponent b)
	{
		return metalToggleButtonUI;
	}

	// ********************************
	// Install Defaults
	// ********************************
	public void installDefaults(AbstractButton b)
	{
		super.installDefaults(b);
		if (!defaults_initialized)
		{
			focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
			selectColor = UIManager.getColor(getPropertyPrefix() + "select");
			disabledTextColor = UIManager.getColor(getPropertyPrefix()
					+ "disabledText");
			defaults_initialized = true;
		}
	}

	protected void uninstallDefaults(AbstractButton b)
	{
		super.uninstallDefaults(b);
		defaults_initialized = false;
	}

	protected void installKeyboardActions(AbstractButton b)
	{
	}

	protected void installListeners(AbstractButton b)
	{
		b.addMouseListener(new ButtonListener());
	}

	// ********************************
	// Default Accessors
	// ********************************
	protected Color getSelectColor()
	{
		return selectColor;
	}

	protected Color getDisabledTextColor()
	{
		return disabledTextColor;
	}

	protected Color getFocusColor()
	{
		return focusColor;
	}

	// ********************************
	// Paint Methods
	// ********************************
	protected void paintButtonPressed(Graphics g, AbstractButton b)
	{
		if (b.isContentAreaFilled())
		{
			Dimension size = b.getSize();
			g.setColor(getSelectColor());
			g.fillRect(0, 0, size.width, size.height);
		}
	}

	protected void paintText(Graphics g, JComponent c, Rectangle textRect,
			String text)
	{
		AbstractButton b = (AbstractButton) c;
		ButtonModel model = b.getModel();
		FontMetrics fm = g.getFontMetrics();
		int mnemIndex = b.getDisplayedMnemonicIndex();

		/* Draw the Text */
		if (model.isEnabled())
		{
			/** * paint the text normally */
			g.setColor(b.getForeground());
			BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemIndex,
					textRect.x, textRect.y + fm.getAscent());
		}
		else
		{
			/** * paint the text disabled ** */
			if (model.isSelected())
			{
				g.setColor(c.getBackground());
			}
			else
			{
				g.setColor(getDisabledTextColor());
			}
			BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemIndex,
					textRect.x, textRect.y + fm.getAscent());

		}
	}

	protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect,
			Rectangle textRect, Rectangle iconRect)
	{

		Rectangle focusRect = new Rectangle();
		String text = b.getText();
		boolean isIcon = b.getIcon() != null;

		// If there is text
		if (text != null && !text.equals(""))
		{
			if (!isIcon)
			{
				focusRect.setBounds(textRect);
			}
			else
			{
				focusRect.setBounds(iconRect.union(textRect));
			}
		}
		// If there is an icon and no text
		else if (isIcon)
		{
			focusRect.setBounds(iconRect);
		}

		g.setColor(getFocusColor());
		g.drawRect(( focusRect.x - 1 ), ( focusRect.y - 1 ),
				focusRect.width + 1, focusRect.height + 1);

	}

	// *************

	private class ButtonListener implements MouseListener, MouseMotionListener,
			FocusListener, ChangeListener, PropertyChangeListener
	{
		/** Set to true when the WindowInputMap is installed. */
		private boolean createdWindowInputMap;

		transient long lastPressedTimestamp = -1;

		transient boolean shouldDiscardRelease = false;

		public ButtonListener()
		{
		}

		public ButtonListener( AbstractButton b )
		{
		}

		public void propertyChange(PropertyChangeEvent e)
		{
			String prop = e.getPropertyName();
			if (prop.equals(AbstractButton.MNEMONIC_CHANGED_PROPERTY))
			{
				updateMnemonicBinding((AbstractButton) e.getSource());
			}

			if (prop
					.equals(AbstractButton.CONTENT_AREA_FILLED_CHANGED_PROPERTY))
			{
				checkOpacity((AbstractButton) e.getSource());
			}

			if (prop.equals(AbstractButton.TEXT_CHANGED_PROPERTY)
					|| "font".equals(prop) || "foreground".equals(prop))
			{
				AbstractButton b = (AbstractButton) e.getSource();
				// BasicHTML.updateRenderer(b, b.getText());
			}
		}

		protected void checkOpacity(AbstractButton b)
		{
			b.setOpaque(b.isContentAreaFilled());
		}

		/**
		 * Register default key actions: pressing space to "click" a button and
		 * registring the keyboard mnemonic (if any).
		 */
		public void installKeyboardActions(JComponent c)
		{
			/*
			 * AbstractButton b = (AbstractButton) c; // Update the mnemonic
			 * binding. updateMnemonicBinding(b); // Reset the ActionMap.
			 * ActionMap map = getActionMap(b);
			 * 
			 * SwingUtilities.replaceUIActionMap(c, map);
			 * 
			 * InputMap km = getInputMap(JComponent.WHEN_FOCUSED, c);
			 * 
			 * SwingUtilities.replaceUIInputMap(c, JComponent.WHEN_FOCUSED, km);
			 */
		}

		/**
		 * Unregister's default key actions
		 */
		public void uninstallKeyboardActions(JComponent c)
		{
			/*
			 * if (createdWindowInputMap) { SwingUtilities.replaceUIInputMap(c,
			 * JComponent. WHEN_IN_FOCUSED_WINDOW, null); createdWindowInputMap =
			 * false; } SwingUtilities.replaceUIInputMap(c,
			 * JComponent.WHEN_FOCUSED, null);
			 * SwingUtilities.replaceUIActionMap(c, null);
			 */
		}

		/**
		 * Returns the ActionMap to use for <code>b</code>. Called as part of
		 * <code>installKeyboardActions</code>.
		 */
		ActionMap getActionMap(AbstractButton b)
		{
			return createActionMap(b);
		}

		/**
		 * Returns the InputMap for condition <code>condition</code>. Called
		 * as part of <code>installKeyboardActions</code>.
		 */
		InputMap getInputMap(int condition, JComponent c)
		{
			/*
			 * if (condition == JComponent.WHEN_FOCUSED) { ButtonUI ui = (
			 * (AbstractButton) c).getUI(); if (ui != null && (ui instanceof
			 * BasicButtonUI)) { return (InputMap) UIManager.get( (
			 * (BasicButtonUI) ui). getPropertyPrefix() + "focusInputMap"); } }
			 */
			return null;
		}

		/**
		 * Creates and returns the ActionMap to use for the button.
		 */
		ActionMap createActionMap(AbstractButton c)
		{
			ActionMap retValue = new javax.swing.plaf.ActionMapUIResource();

			/*
			 * retValue.put("pressed", new PressedAction( (AbstractButton) c));
			 * retValue.put("released", new ReleasedAction( (AbstractButton)
			 * c));
			 */
			return retValue;
		}

		/**
		 * Resets the binding for the mnemonic in the WHEN_IN_FOCUSED_WINDOW UI
		 * InputMap.
		 */
		void updateMnemonicBinding(AbstractButton b)
		{
			int m = b.getMnemonic();
			if (m != 0)
			{
				InputMap map;
				if (!createdWindowInputMap)
				{
					map = new ComponentInputMapUIResource(b);
					SwingUtilities.replaceUIInputMap(b,
							JComponent.WHEN_IN_FOCUSED_WINDOW, map);
					createdWindowInputMap = true;
				}
				else
				{
					map = SwingUtilities.getUIInputMap(b,
							JComponent.WHEN_IN_FOCUSED_WINDOW);
				}
				if (map != null)
				{
					map.clear();
					map.put(KeyStroke.getKeyStroke(m, ActionEvent.ALT_MASK,
							false), "pressed");
					map.put(KeyStroke.getKeyStroke(m, ActionEvent.ALT_MASK,
							true), "released");
					map.put(KeyStroke.getKeyStroke(m, 0, true), "released");
				}
			}
			else if (createdWindowInputMap)
			{
				InputMap map = SwingUtilities.getUIInputMap(b,
						JComponent.WHEN_IN_FOCUSED_WINDOW);
				if (map != null)
				{
					map.clear();
				}
			}
		}

		public void stateChanged(ChangeEvent e)
		{
			AbstractButton b = (AbstractButton) e.getSource();
			b.repaint();
		}

		public void focusGained(FocusEvent e)
		{
			AbstractButton b = (AbstractButton) e.getSource();
			if (b instanceof JButton && ( (JButton) b ).isDefaultCapable())
			{
				JRootPane root = b.getRootPane();
				if (root != null)
				{
					root.putClientProperty("temporaryDefaultButton", b);
					root.setDefaultButton((JButton) b);
					root.putClientProperty("temporaryDefaultButton", null);
				}
			}
			b.repaint();
		}

		public void focusLost(FocusEvent e)
		{
			AbstractButton b = (AbstractButton) e.getSource();

			JRootPane root = b.getRootPane();
			if (root != null)
			{
				JButton initialDefault = (JButton) root
						.getClientProperty("initialDefaultButton");
				if (b != initialDefault)
				{
					root.setDefaultButton(initialDefault);
				}
			}

			b.getModel().setArmed(false);

			b.repaint();
		}

		public void mouseMoved(MouseEvent e)
		{
		};

		public void mouseDragged(MouseEvent e)
		{
		};

		public void mouseClicked(MouseEvent e)
		{
		};

		public void mousePressed(MouseEvent e)
		{
			// System.out.println("MetalCheckBoxUI: mousePressed()");
			if (SwingUtilities.isLeftMouseButton(e))
			{
				AbstractButton b = (AbstractButton) e.getSource();

				if (b.contains(e.getX(), e.getY()))
				{
					long multiClickThreshhold = b.getMultiClickThreshhold();
					long lastTime = lastPressedTimestamp;
					long currentTime = lastPressedTimestamp = e.getWhen();
					if (lastTime != -1
							&& currentTime - lastTime < multiClickThreshhold)
					{
						shouldDiscardRelease = true;
						return;
					}

					ButtonModel model = b.getModel();
					if (!model.isEnabled())
					{
						// Disabled buttons ignore all input...
						return;
					}
					if (!model.isArmed())
					{
						// button not armed, should be
						// model.setArmed(true);
					}
					/*
					 * if (!b.hasFocus() && b.isRequestFocusEnabled()) {
					 * b.requestFocus(); }
					 */
					// Notificamos la pulsacion del CheckBox
					// model.setPressed(true);
					// Se comenta para que solo se tome en consideracion el
					// mouse released
					Vector v = ( (DJToggleButton) b )
							.getDJToggleButtonListeners();
					DJToggleButtonEvent evento = new DJToggleButtonEvent();
					evento.tipo = new Integer(DJToggleButtonEvent.PRESIONADO
							.intValue());
					for (int i = 0; i < v.size(); i++)
					{
						( (DJToggleButtonListener) v.elementAt(i) )
								.presionado(evento);
					}
				}
			}
		};

		public void mouseReleased(MouseEvent e)
		{
			if (SwingUtilities.isLeftMouseButton(e))
			{
				// Support for multiClickThreshhold
				if (shouldDiscardRelease)
				{
					shouldDiscardRelease = false;
					return;
				}
				AbstractButton b = (AbstractButton) e.getSource();
				ButtonModel model = b.getModel();

				// Al haber deshabilitado los eventos de entrada y salida del
				// puntero siempre
				// que se produzca un evento mouseReleased cambiará la seleccion
				// del togglebutton

				// Cualquier mouseReleased() producira cambio de estado
				// if(model.isPressed() && model.isArmed()){
				boolean seleccionado = model.isSelected();
				Vector v = ( (DJToggleButton) b ).getDJToggleButtonListeners();
				DJToggleButtonEvent evento = new DJToggleButtonEvent();
				evento.tipo = new Integer(DJToggleButtonEvent.SOLTADO
						.intValue());
				for (int i = 0; i < v.size(); i++)
				{
					( (DJToggleButtonListener) v.elementAt(i) ).soltado(evento);
				}
				// }

				/*
				 * model.setPressed(false); model.setArmed(false);
				 */

			}
		};

		public void mouseEntered(MouseEvent e)
		{
			/*
			 * AbstractButton b = (AbstractButton) e.getSource(); ButtonModel
			 * model = b.getModel(); if (b.isRolloverEnabled()) {
			 * model.setRollover(true); } if (model.isPressed())
			 * model.setArmed(true);
			 */
		};

		public void mouseExited(MouseEvent e)
		{
			/*
			 * AbstractButton b = (AbstractButton) e.getSource(); ButtonModel
			 * model = b.getModel(); if (b.isRolloverEnabled()) {
			 * model.setRollover(false); } model.setArmed(false);
			 */
		};

		/*
		 * static class PressedAction extends AbstractAction { AbstractButton b =
		 * null; PressedAction(AbstractButton b) { this.b = b; }
		 * 
		 * public void actionPerformed(ActionEvent e) { ButtonModel model =
		 * b.getModel(); model.setArmed(true); model.setPressed(true); if
		 * (!b.hasFocus()) { b.requestFocus(); } }
		 * 
		 * public boolean isEnabled() { if (!b.getModel().isEnabled()) { return
		 * false; } else { return true; } } }
		 * 
		 * static class ReleasedAction extends AbstractAction { AbstractButton b =
		 * null; ReleasedAction(AbstractButton b) { this.b = b; }
		 * 
		 * public void actionPerformed(ActionEvent e) { ButtonModel model =
		 * b.getModel(); model.setPressed(false); model.setArmed(false); }
		 * 
		 * public boolean isEnabled() { if (!b.getModel().isEnabled()) { return
		 * false; } else { return true; } } }
		 */

	}

}
