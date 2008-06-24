package fisica.audio;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * A Layout-manager that displays the added components in lines. The width of
 * this layout is determined by the widest component that was added. The height
 * of each stripe is the preferred height of the respective component.<BR>
 */
public class StripeLayout implements LayoutManager
{
	protected int leftEdge;

	protected int topEdge;

	protected int rightEdge;

	protected int bottomEdge;

	protected int spacer;

	public StripeLayout()
	{
		this(0, 0, 0, 0);
	}

	/**
	 * Constructs a StripeLayout with left, right, top, and bottom edge of
	 * <tt>edge</tt>
	 */
	public StripeLayout( int edge )
	{
		this(edge, edge, edge, edge);
	}

	/**
	 * Constructs a StripeLayout with the given edges.
	 */
	public StripeLayout( int leftEdge, int topEdge, int rightEdge,
			int bottomEdge )
	{
		this(leftEdge, topEdge, rightEdge, bottomEdge, 0);
	}

	/**
	 * Constructs a StripeLayout with the given edges and spacer
	 */
	public StripeLayout( int leftEdge, int topEdge, int rightEdge,
			int bottomEdge, int spacer )
	{
		this.leftEdge = leftEdge;
		this.topEdge = topEdge;
		this.rightEdge = rightEdge;
		this.bottomEdge = bottomEdge;
		this.spacer = spacer;
	}

	public void addLayoutComponent(String name, Component comp)
	{
	}

	public void removeLayoutComponent(Component comp)
	{
	}

	protected Insets getTotalInsets(Container parent)
	{
		Insets insets = parent.getInsets();
		insets.left += leftEdge;
		insets.right += rightEdge;
		insets.top += topEdge;
		insets.bottom += bottomEdge;
		return insets;
	}

	/**
	 * calculates the dimension of the layout.
	 * 
	 * @param boolean
	 *            preferred for the utilization of the preferred dimensions
	 * @return
	 */
	private Dimension calcSize(Container parent, boolean preferred)
	{
		Dimension dim = new Dimension(0, 0);
		int nmembers = parent.getComponentCount();
		Dimension d;
		for (int i = 0; i < nmembers; i++)
		{
			Component m = parent.getComponent(i);
			if (m.isVisible())
			{
				if (preferred)
					d = m.getPreferredSize();
				else d = m.getMinimumSize();

				if (dim.width < d.width) dim.width = d.width;
				dim.height += d.height;
				if (i < ( nmembers - 1 )) dim.height += spacer;
			}
		}
		Insets insets = getTotalInsets(parent);
		dim.width += insets.left + insets.right;
		dim.height += insets.top + insets.bottom;
		return dim;
	}

	public Dimension preferredLayoutSize(Container target)
	{
		return calcSize(target, true);
	}

	public Dimension minimumLayoutSize(Container target)
	{
		return calcSize(target, false);
	}

	public void layoutContainer(Container target)
	{
		// insanity check,
		Insets insets = getTotalInsets(target);

		int x = insets.left;
		int y = insets.top;

		int width = target.getWidth() - insets.left - insets.right;
		int maxY = target.getSize().height - insets.bottom - insets.top;

		int nmembers = target.getComponentCount();
		for (int i = 0; ( i < nmembers ) && ( y < maxY ); i++)
		{
			Component m = target.getComponent(i);
			if (m.isVisible())
			{
				m.setBounds(x, y, width, m.getPreferredSize().height);
				y += m.getHeight();
				if (i < ( nmembers - 1 )) y += spacer;
			}
		}
	}
}
