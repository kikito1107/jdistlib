/*
 * @(#)MetalComboBoxEditor.java	1.19 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package lookandfeel.Dmetal;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicComboBoxEditor;

/**
 * The default editor for Metal editable combo boxes
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 * 
 * @version 1.19 01/23/03
 * @author Steve Wilson
 */
public class MetalComboBoxEditor extends BasicComboBoxEditor
{

	public MetalComboBoxEditor()
	{
		super();
		// editor.removeFocusListener(this);
		editor = new JTextField("", 9)
		{
			private static final long serialVersionUID = -3489178113450599206L;

			// workaround for 4530952
			@Override
			public void setText(String s)
			{
				if (getText().equals(s)) return;
				super.setText(s);
			}
		};

		editor.setBorder(new EditorBorder());
		// editor.addFocusListener(this);
	}

	protected static Insets editorBorderInsets = new Insets(4, 2, 4, 0);

	class EditorBorder extends AbstractBorder
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -5327374797481752414L;

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int w,
				int h)
		{
			g.translate(x, y);

			g.setColor(MetalLookAndFeel.getControlDarkShadow());
			g.drawLine(0, 0, w - 1, 0);
			g.drawLine(0, 0, 0, h - 2);
			g.drawLine(0, h - 2, w - 1, h - 2);
			g.setColor(MetalLookAndFeel.getControlHighlight());
			g.drawLine(1, 1, w - 1, 1);
			g.drawLine(1, 1, 1, h - 1);
			g.drawLine(1, h - 1, w - 1, h - 1);
			g.setColor(MetalLookAndFeel.getControl());
			g.drawLine(1, h - 2, 1, h - 2);

			g.translate(-x, -y);
		}

		@Override
		public Insets getBorderInsets(Component c)
		{
			return editorBorderInsets;
		}
	}

	/**
	 * A subclass of BasicComboBoxEditor that implements UIResource.
	 * BasicComboBoxEditor doesn't implement UIResource directly so that
	 * applications can safely override the cellRenderer property with
	 * BasicListCellRenderer subclasses.
	 * <p>
	 * <strong>Warning:</strong> Serialized objects of this class will not be
	 * compatible with future Swing releases. The current serialization support
	 * is appropriate for short term storage or RMI between applications running
	 * the same version of Swing. As of 1.4, support for long term storage of
	 * all JavaBeans<sup><font size="-2">TM</font></sup> has been added to
	 * the <code>java.beans</code> package. Please see
	 * {@link java.beans.XMLEncoder}.
	 */
	public static class UIResource extends MetalComboBoxEditor implements
			javax.swing.plaf.UIResource
	{
	}
}
