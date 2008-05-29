/*

	Implementar el acceso a las estaciones existentes de forma at�mica



 */

package util;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.TextUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.Segment;

public class DCaret extends Rectangle implements Caret, FocusListener,
		MouseListener, MouseMotionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1242950692357469828L;

	Rectangle grupo = new Rectangle();

	/**
	 * Constructs a default caret.
	 */
	public DCaret()
	{
		async = true;
		setBlinkRate(400);

	}

	/**
	 * Get the flag that determines whether or not asynchronous updates will
	 * move the caret. Normally the caret is moved by events from the event
	 * thread such as mouse or keyboard events. Changes from another thread
	 * might be used to load a file, or show changes from another user. This
	 * flag determines whether those changes will move the caret.
	 */
	boolean getAsynchronousMovement()
	{
		return async;
	}

	/**
	 * Set the flag that determines whether or not asynchronous updates will
	 * move the caret. Normally the caret is moved by events from the event
	 * thread such as mouse or keyboard events. Changes from another thread
	 * might be used to load a file, or show changes from another user. This
	 * flag determines whether those changes will move the caret.
	 * 
	 * @param m
	 *            move the caret on asynchronous updates if true.
	 */
	void setAsynchronousMovement(boolean m)
	{
		async = m;
	}

	/**
	 * Gets the text editor component that this caret is is bound to.
	 * 
	 * @return the component
	 */
	protected final JTextComponent getComponent()
	{
		return component;
	}

	/**
	 * Cause the caret to be painted. The repaint area is the bounding box of
	 * the caret (i.e. the caret rectangle or <em>this</em>).
	 * <p>
	 * This method is thread safe, although most Swing methods are not. Please
	 * see <A
	 * HREF="http://java.sun.com/products/jfc/swingdoc-archive/threads.html">
	 * Threads and Swing</A> for more information.
	 */
	protected final synchronized void repaint()
	{
		if (component != null) component.repaint(x, y, width, height);
	}

	/**
	 * Damages the area surrounding the caret to cause it to be repainted in a
	 * new location. If paint() is reimplemented, this method should also be
	 * reimplemented. This method should update the caret bounds (x, y, width,
	 * and height).
	 * 
	 * @param r
	 *            the current location of the caret
	 * @see #paint
	 */
	protected synchronized void damage(Rectangle r)
	{
		if (r != null)
		{
			x = r.x - 4;
			y = r.y;
			width = 10;
			height = r.height;

			// +++++ Hacer monitor de estaciones;

			grupo.x = grupo.x - 4;
			grupo.y = grupo.y;
			grupo.width = 10;
			grupo.height = r.height;

			repaint();
		}
	}

	/**
	 * Scrolls the associated view (if necessary) to make the caret visible.
	 * Since how this should be done is somewhat of a policy, this method can be
	 * reimplemented to change the behavior. By default the scrollRectToVisible
	 * method is called on the associated component.
	 * 
	 * @param nloc
	 *            the new position to scroll to
	 */
	protected void adjustVisibility(Rectangle nloc)
	{
		SwingUtilities.invokeLater(new SafeScroller(nloc));
	}

	/**
	 * Gets the painter for the Highlighter.
	 * 
	 * @return the painter
	 */
	protected Highlighter.HighlightPainter getSelectionPainter()
	{
		return DefaultHighlighter.DefaultPainter;
	}

	/**
	 * Tries to set the position of the caret from the coordinates of a mouse
	 * event, using viewToModel().
	 * 
	 * @param e
	 *            the mouse event
	 */
	protected void positionCaret(MouseEvent e)
	{
		Point pt = new Point(e.getX(), e.getY());
		Position.Bias[] biasRet = new Position.Bias[1];
		int pos = component.getUI().viewToModel(component, pt, biasRet);
		if (biasRet[0] == null) biasRet[0] = Position.Bias.Forward;
		if (pos >= 0) setDot(pos, biasRet[0]);
	}

	/**
	 * Tries to move the position of the caret from the coordinates of a mouse
	 * event, using viewToModel(). This will cause a selection if the dot and
	 * mark are different.
	 * 
	 * @param e
	 *            the mouse event
	 */
	protected void moveCaret(MouseEvent e)
	{
		Point pt = new Point(e.getX(), e.getY());
		Position.Bias[] biasRet = new Position.Bias[1];
		int pos = component.getUI().viewToModel(component, pt, biasRet);
		if (biasRet[0] == null) biasRet[0] = Position.Bias.Forward;
		if (pos >= 0) moveDot(pos, biasRet[0]);
	}

	// --- FocusListener methods --------------------------

	/**
	 * Called when the component containing the caret gains focus. This is
	 * implemented to set the caret to visible if the component is editable.
	 * 
	 * @param e
	 *            the focus event
	 * @see FocusListener#focusGained
	 */
	public void focusGained(FocusEvent e)
	{
		if (component.isEnabled())
		{
			if (component.isEditable()) setVisible(true);
			setSelectionVisible(true);
		}
	}

	/**
	 * Called when the component containing the caret loses focus. This is
	 * implemented to set the caret to visibility to false.
	 * 
	 * @param e
	 *            the focus event
	 * @see FocusListener#focusLost
	 */
	public void focusLost(FocusEvent e)
	{
		setVisible(false);
		setSelectionVisible(e.isTemporary());
	}

	// --- MouseListener methods -----------------------------------

	/**
	 * Called when the mouse is clicked. If the click was generated from
	 * button1, a double click selects a word, and a triple click the current
	 * line.
	 * 
	 * @param e
	 *            the mouse event
	 * @see MouseListener#mouseClicked
	 */
	public void mouseClicked(MouseEvent e)
	{
		/*
		 * DJTextPane djtp = (DJTextPane)this.getComponent(); ActionMap act =
		 * djtp.getActionMap(); if ((e.getModifiers() & InputEvent.BUTTON1_MASK) !=
		 * 0) { if(e.getClickCount() == 2) { Action a =
		 * act.get(DJTextPane.selectWord); a.actionPerformed(null); } else
		 * if(e.getClickCount() == 3) { Action a =
		 * act.get(DJTextPane.selectLine); a.actionPerformed(null); } }
		 */
	}

	/**
	 * If button 1 is pressed, this is implemented to request focus on the
	 * associated text component, and to set the caret position. If the shift
	 * key is held down, the caret will be moved, potentially resulting in a
	 * selection, otherwise the caret position will be set to the new location.
	 * If the component is not enabled, there will be no request for focus.
	 * 
	 * @param e
	 *            the mouse event
	 * @see MouseListener#mousePressed
	 */
	public void mousePressed(MouseEvent e)
	{
		if (SwingUtilities.isLeftMouseButton(e))
		{
			if (( ( e.getModifiers() & ActionEvent.SHIFT_MASK ) != 0 )
					&& ( getDot() != -1 ))
				moveCaret(e);
			else positionCaret(e);
			if (( component != null ) && component.isEnabled())
				component.requestFocus();
		}
	}

	/**
	 * Called when the mouse is released.
	 * 
	 * @param e
	 *            the mouse event
	 * @see MouseListener#mouseReleased
	 */
	public void mouseReleased(MouseEvent e)
	{
	}

	/**
	 * Called when the mouse enters a region.
	 * 
	 * @param e
	 *            the mouse event
	 * @see MouseListener#mouseEntered
	 */
	public void mouseEntered(MouseEvent e)
	{
	}

	/**
	 * Called when the mouse exits a region.
	 * 
	 * @param e
	 *            the mouse event
	 * @see MouseListener#mouseExited
	 */
	public void mouseExited(MouseEvent e)
	{
	}

	// --- MouseMotionListener methods -------------------------

	/**
	 * Moves the caret position according to the mouse pointer's current
	 * location. This effectively extends the selection. By default, this is
	 * only done for mouse button 1.
	 * 
	 * @param e
	 *            the mouse event
	 * @see MouseMotionListener#mouseDragged
	 */
	public void mouseDragged(MouseEvent e)
	{
		if (SwingUtilities.isLeftMouseButton(e)) moveCaret(e);
	}

	/**
	 * Called when the mouse is moved.
	 * 
	 * @param e
	 *            the mouse event
	 * @see MouseMotionListener#mouseMoved
	 */
	public void mouseMoved(MouseEvent e)
	{
	}

	// ---- Caret methods ---------------------------------

	/**
	 * Renders the caret as a vertical line. If this is reimplemented the damage
	 * method should also be reimplemented as it assumes the shape of the caret
	 * is a vertical line. Sets the caret color to the value returned by
	 * getCaretColor().
	 * <p>
	 * If there are multiple text directions present in the associated document,
	 * a flag indicating the caret bias will be rendered. This will occur only
	 * if the associated document is a subclass of AbstractDocument and there
	 * are multiple bidi levels present in the bidi element structure (i.e. the
	 * text has multiple directions associated with it).
	 * 
	 * @param g
	 *            the graphics context
	 * @see #damage
	 */
	public void paint(Graphics g)
	{
		if (isVisible()) try
		{
			TextUI mapper = component.getUI();
			Rectangle r = mapper.modelToView(component, dot, dotBias);
			x = r.x - 4;
			y = r.y;
			width = 10;
			height = r.height;
			g.setColor(component.getCaretColor());
			g.drawLine(r.x, r.y, r.x, r.y + r.height - 1);

			/*
			 * DJTextPane dJTextPane = ((DJTextPane)component);
			 * //System.out.println(dJTextPane.grupoTrabajo.estaciones.size());
			 * for(int i=0; i<dJTextPane.grupoTrabajo.estaciones.size(); i++){
			 * DEstacion estacion =
			 * (DEstacion)dJTextPane.grupoTrabajo.estaciones.elementAt(i);
			 * estacion.r = mapper.modelToView(getComponent(),
			 * estacion.getPosicionCaret(), dotBias);
			 * g.setColor(estacion.getColor()); g.drawLine(estacion.r.x,
			 * estacion.r.y, estacion.r.x, estacion.r.y + estacion.r.height -
			 * 1); }
			 */

			// see if we should paint a flag to indicate the bias
			// of the caret.
			// PENDING(prinz) this should be done through
			// protected methods so that alternative LAF
			// will show bidi information.
			Document doc = component.getDocument();
			if (doc instanceof AbstractDocument)
			{
				Element bidi = ( (AbstractDocument) doc ).getBidiRootElement();
				if (( bidi != null ) && ( bidi.getElementCount() > 1 ))
				{
					// there are multiple directions present.
					flagXPoints[0] = r.x;
					flagYPoints[0] = r.y;
					flagXPoints[1] = r.x;
					flagYPoints[1] = r.y + 4;
					flagYPoints[2] = r.y;
					flagXPoints[2] = ( dotLTR ) ? r.x + 5 : r.x - 4;
					g.fillPolygon(flagXPoints, flagYPoints, 3);
				}
			}
		}
		catch (BadLocationException e)
		{
			// can't render I guess
			// System.err.println("Can't render cursor");
		}
	}

	/**
	 * Called when the UI is being installed into the interface of a
	 * JTextComponent. This can be used to gain access to the model that is
	 * being navigated by the implementation of this interface. Sets the dot and
	 * mark to 0, and establishes document, property change, focus, mouse, and
	 * mouse motion listeners.
	 * 
	 * @param c
	 *            the component
	 * @see Caret#install
	 */
	public void install(JTextComponent c)
	{
		component = c;
		Document doc = c.getDocument();
		dot = mark = 0;
		dotLTR = markLTR = true;
		dotBias = markBias = Position.Bias.Forward;
		if (doc != null) doc.addDocumentListener(updateHandler);
		c.addPropertyChangeListener(updateHandler);
		focusListener = new FocusHandler(this);
		c.addFocusListener(focusListener);
		c.addMouseListener(this);
		c.addMouseMotionListener(this);

		// if the component already has focus, it won't
		// be notified.
		if (component.hasFocus()) focusGained(null);
	}

	/**
	 * Called when the UI is being removed from the interface of a
	 * JTextComponent. This is used to unregister any listeners that were
	 * attached.
	 * 
	 * @param c
	 *            the component
	 * @see Caret#deinstall
	 */
	public void deinstall(JTextComponent c)
	{
		c.removeMouseListener(this);
		c.removeMouseMotionListener(this);
		if (focusListener != null)
		{
			c.removeFocusListener(focusListener);
			focusListener = null;
		}
		c.removePropertyChangeListener(updateHandler);
		Document doc = c.getDocument();
		if (doc != null) doc.removeDocumentListener(updateHandler);
		synchronized (this)
		{
			component = null;
		}
		if (flasher != null) flasher.stop();

	}

	/**
	 * Adds a listener to track whenever the caret position has been changed.
	 * 
	 * @param l
	 *            the listener
	 * @see Caret#addChangeListener
	 */
	public void addChangeListener(ChangeListener l)
	{
		listenerList.add(ChangeListener.class, l);
	}

	/**
	 * Removes a listener that was tracking caret position changes.
	 * 
	 * @param l
	 *            the listener
	 * @see Caret#removeChangeListener
	 */
	public void removeChangeListener(ChangeListener l)
	{
		listenerList.remove(ChangeListener.class, l);
	}

	/**
	 * Notifies all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method. The listener list is processed
	 * last to first.
	 * 
	 * @see EventListenerList
	 */
	protected void fireStateChanged()
	{
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == ChangeListener.class)
			{
				// Lazily create the event:
				if (changeEvent == null) changeEvent = new ChangeEvent(this);
				( (ChangeListener) listeners[i + 1] ).stateChanged(changeEvent);
			}
	}

	/**
	 * Return an array of all the listeners of the given type that were added to
	 * this model.
	 * 
	 * @returns all of the objects recieving <em>listenerType</em>
	 *          notifications from this model
	 * 
	 * @since 1.3
	 */

	public EventListener[] getListeners(Class<EventListener> listenerType)
	{
		return listenerList.getListeners(listenerType);
	}

	/**
	 * Changes the selection visibility.
	 * 
	 * @param vis
	 *            the new visibility
	 */
	public void setSelectionVisible(boolean vis)
	{
		if (vis != selectionVisible)
		{
			selectionVisible = vis;
			if (selectionVisible)
			{
				// show
				Highlighter h = component.getHighlighter();
				if (( dot != mark ) && ( h != null ) && ( selectionTag == null ))
				{
					int p0 = Math.min(dot, mark);
					int p1 = Math.max(dot, mark);
					Highlighter.HighlightPainter p = getSelectionPainter();
					try
					{
						selectionTag = h.addHighlight(p0, p1, p);
					}
					catch (BadLocationException bl)
					{
						selectionTag = null;
					}
				}
			}
			else // hide
			if (selectionTag != null)
			{
				Highlighter h = component.getHighlighter();
				h.removeHighlight(selectionTag);
				selectionTag = null;
			}
		}
	}

	/**
	 * Checks whether the current selection is visible.
	 * 
	 * @return true if the selection is visible
	 */
	public boolean isSelectionVisible()
	{
		return selectionVisible;
	}

	/**
	 * Determines if the caret is currently visible.
	 * 
	 * @return true if visible else false
	 * @see Caret#isVisible
	 */
	public boolean isVisible()
	{
		return visible;
	}

	/**
	 * Sets the caret visibility, and repaints the caret.
	 * 
	 * @param e
	 *            the visibility specifier
	 * @see Caret#setVisible
	 */
	public void setVisible(boolean e)
	{
		// focus lost notification can come in later after the
		// caret has been deinstalled, in which case the component
		// will be null.
		if (component != null)
		{
			if (visible != e) // repaint the caret
				try
				{
					Rectangle loc = component.modelToView(dot);
					damage(loc);
				}
				catch (BadLocationException badloc)
				{
					// hmm... not legally positioned
				}
			visible = e;
		}
		if (flasher != null) if (visible)
			flasher.start();
		else flasher.stop();
	}

	/**
	 * Sets the caret blink rate.
	 * 
	 * @param rate
	 *            the rate in milliseconds, 0 to stop blinking
	 * @see Caret#setBlinkRate
	 */
	public void setBlinkRate(int rate)
	{
		if (rate != 0)
		{
			if (flasher == null) flasher = new Timer(rate, updateHandler);
			flasher.setDelay(rate);
		}
		else if (flasher != null)
		{
			flasher.stop();
			flasher.removeActionListener(updateHandler);
			flasher = null;
		}
	}

	/**
	 * Gets the caret blink rate.
	 * 
	 * @returns the delay in milliseconds. If this is zero the caret will not
	 *          blink.
	 * @see Caret#getBlinkRate
	 */
	public int getBlinkRate()
	{
		return ( flasher == null ) ? 0 : flasher.getDelay();
	}

	/**
	 * Fetches the current position of the caret.
	 * 
	 * @return the position >= 0
	 * @see Caret#getDot
	 */
	public int getDot()
	{
		return dot;
	}

	/**
	 * Fetches the current position of the mark. If there is a selection, the
	 * dot and mark will not be the same.
	 * 
	 * @return the position >= 0
	 * @see Caret#getMark
	 */
	public int getMark()
	{
		return mark;
	}

	/**
	 * Sets the caret position and mark to some position. This implicitly sets
	 * the selection range to zero.
	 * 
	 * @param dot
	 *            the position >= 0
	 * @see Caret#setDot
	 */
	public void setDot(int dot)
	{
		setDot(dot, Position.Bias.Forward);
	}

	/**
	 * Moves the caret position to some other position.
	 * 
	 * @param dot
	 *            the position >= 0
	 * @see Caret#moveDot
	 */
	public void moveDot(int dot)
	{
		moveDot(dot, Position.Bias.Forward);
	}

	// ---- Bidi methods (we could put these in a subclass)

	void moveDot(int dot, Position.Bias dotBias)
	{
		if (!component.isEnabled())
		{
			// don't allow selection on disabled components.
			setDot(dot, dotBias);
			return;
		}
		if (dot != this.dot)
		{
			changeCaretPosition(dot, dotBias);

			if (selectionVisible)
			{
				Highlighter h = component.getHighlighter();
				if (h != null)
				{
					int p0 = Math.min(dot, mark);
					int p1 = Math.max(dot, mark);
					try
					{
						if (selectionTag != null)
							h.changeHighlight(selectionTag, p0, p1);
						else
						{
							Highlighter.HighlightPainter p = getSelectionPainter();
							selectionTag = h.addHighlight(p0, p1, p);
						}
					}
					catch (BadLocationException e)
					{
						// throw new StateInvariantError("Bad caret position");
						// throw new BadLocationException("Bad caret position",
						// e.offsetRequested());
					}
				}
			}
		}
	}

	void setDot(int dot, Position.Bias dotBias)
	{
		// move dot, if it changed
		Document doc = component.getDocument();
		if (doc != null) dot = Math.min(dot, doc.getLength());
		dot = Math.max(dot, 0);

		// The position (0,Backward) is out of range so disallow it.
		if (dot == 0) dotBias = Position.Bias.Forward;

		mark = dot;
		if (( this.dot != dot ) || ( this.dotBias != dotBias )
				|| ( selectionTag != null )) changeCaretPosition(dot, dotBias);
		this.markBias = this.dotBias;
		this.markLTR = dotLTR;
		Highlighter h = component.getHighlighter();
		if (( h != null ) && ( selectionTag != null ))
		{
			h.removeHighlight(selectionTag);
			selectionTag = null;
		}
	}

	Position.Bias getDotBias()
	{
		return dotBias;
	}

	Position.Bias getMarkBias()
	{
		return markBias;
	}

	boolean isDotLeftToRight()
	{
		return dotLTR;
	}

	boolean isMarkLeftToRight()
	{
		return markLTR;
	}

	boolean isPositionLTR(int position, Position.Bias bias)
	{
		Document doc = component.getDocument();
		if (doc instanceof AbstractDocument)
		{
			if (( bias == Position.Bias.Backward ) && ( --position < 0 ))
				position = 0;
			// return ((AbstractDocument)doc).isLeftToRight(position, position);
			return true;
		}
		return true;
	}

	Position.Bias guessBiasForOffset(int offset, Position.Bias lastBias,
			boolean lastLTR)
	{
		// There is an abiguous case here. That if your model looks like:
		// abAB with the cursor at abB]A (visual representation of
		// 3 forward) deleting could either become abB] or
		// ab[B. I'ld actually prefer abB]. But, if I implement that
		// a delete at abBA] would result in aBA] vs a[BA which I
		// think is totally wrong. To get this right we need to know what
		// was deleted. And we could get this from the bidi structure
		// in the change event. So:
		// PENDING: base this off what was deleted.
		if (lastLTR != isPositionLTR(offset, lastBias))
			lastBias = Position.Bias.Backward;
		else if (( lastBias != Position.Bias.Backward )
				&& ( lastLTR != isPositionLTR(offset, Position.Bias.Backward) ))
			lastBias = Position.Bias.Backward;
		if (( lastBias == Position.Bias.Backward ) && ( offset > 0 ))
			try
			{
				Segment s = new Segment();
				component.getDocument().getText(offset - 1, 1, s);
				if (( s.count > 0 ) && ( s.array[s.offset] == '\n' ))
					lastBias = Position.Bias.Forward;
			}
			catch (BadLocationException ble)
			{
			}
		return lastBias;
	}

	// ---- local methods --------------------------------------------

	/**
	 * Sets the caret position (dot) to a new location. This causes the old and
	 * new location to be repainted. It also makes sure that the caret is within
	 * the visible region of the view, if the view is scrollable.
	 */
	void changeCaretPosition(int dot, Position.Bias dotBias)
	{
		// repaint the old position and set the new value of
		// the dot.
		repaint();

		// Make sure the caret is visible if this window has the focus.
		if (( flasher != null ) && flasher.isRunning())
		{
			visible = true;
			flasher.restart();
		}

		// notify listeners at the caret moved
		this.dot = dot;
		this.dotBias = dotBias;
		dotLTR = isPositionLTR(dot, dotBias);
		fireStateChanged();

		setMagicCaretPosition(null);

		// We try to repaint the caret later, since things
		// may be unstable at the time this is called
		// (i.e. we don't want to depend upon notification
		// order or the fact that this might happen on
		// an unsafe thread).
		Runnable callRepaintNewCaret = new Runnable()
		{
			public void run()
			{
				repaintNewCaret();
			}
		};
		SwingUtilities.invokeLater(callRepaintNewCaret);
	}

	/**
	 * Repaints the new caret position, with the assumption that this is
	 * happening on the event thread so that calling <code>modelToView</code>
	 * is safe.
	 */
	void repaintNewCaret()
	{
		if (component != null)
		{
			TextUI mapper = component.getUI();
			Document doc = component.getDocument();
			if (( mapper != null ) && ( doc != null ))
			{
				// determine the new location and scroll if
				// not visible.
				Rectangle newLoc;
				try
				{
					newLoc = mapper.modelToView(component, this.dot,
							this.dotBias);
				}
				catch (BadLocationException e)
				{
					newLoc = null;
				}
				if (newLoc != null)
				{
					adjustVisibility(newLoc);
					// If there is no magic caret position, make one
					if (getMagicCaretPosition() == null)
						setMagicCaretPosition(new Point(newLoc.x, newLoc.y));
				}

				// repaint the new position
				damage(newLoc);
			}
		}
	}

	/**
	 * Saves the current caret position. This is used when caret up/down actions
	 * occur, moving between lines that have uneven end positions.
	 * 
	 * @param p
	 *            the position
	 * @see #getMagicCaretPosition
	 */
	public void setMagicCaretPosition(Point p)
	{
		magicCaretPosition = p;
	}

	/**
	 * Gets the saved caret position.
	 * 
	 * @return the position see #setMagicCaretPosition
	 */
	public Point getMagicCaretPosition()
	{
		return magicCaretPosition;
	}

	/**
	 * Compares this object to the specifed object. The superclass behavior of
	 * comparing rectangles is not desired, so this is changed to the Object
	 * behavior.
	 * 
	 * @param obj
	 *            the object to compare this font with.
	 * @return <code>true</code> if the objects are equal; <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object obj)
	{
		return super.equals(obj);
	}

	@Override
	public String toString()
	{
		String s = "Dot=(" + dot + ", " + dotBias + ")";
		s += " Mark=(" + mark + ", " + markBias + ")";
		return s;
	}

	// --- serialization ---------------------------------------------

	private void readObject(ObjectInputStream s) throws ClassNotFoundException,
			IOException
	{
		s.defaultReadObject();
		updateHandler = new UpdateHandler();
		if (!s.readBoolean())
			dotBias = Position.Bias.Forward;
		else dotBias = Position.Bias.Backward;
		if (!s.readBoolean())
			markBias = Position.Bias.Forward;
		else markBias = Position.Bias.Backward;
	}

	private void writeObject(ObjectOutputStream s) throws IOException
	{
		s.defaultWriteObject();
		s.writeBoolean(( dotBias == Position.Bias.Backward ));
		s.writeBoolean(( markBias == Position.Bias.Backward ));
	}

	// ---- member variables ------------------------------------------

	/**
	 * The event listener list.
	 */
	protected EventListenerList listenerList = new EventListenerList();

	/**
	 * The change event for the model. Only one ChangeEvent is needed per model
	 * instance since the event's only (read-only) state is the source property.
	 * The source of events generated here is always "this".
	 */
	protected transient ChangeEvent changeEvent = null;

	// package-private to avoid inner classes private member
	// access bug
	JTextComponent component;

	/**
	 * flag to indicate if async updates should move the caret.
	 */
	boolean async;

	boolean visible;

	int dot;

	int mark;

	Object selectionTag;

	boolean selectionVisible;

	Timer flasher;

	Point magicCaretPosition;

	transient Position.Bias dotBias;

	transient Position.Bias markBias;

	boolean dotLTR;

	boolean markLTR;

	transient UpdateHandler updateHandler = new UpdateHandler();

	transient private int[] flagXPoints = new int[3];

	transient private int[] flagYPoints = new int[3];

	transient private FocusListener focusListener;

	class SafeScroller implements Runnable
	{

		SafeScroller( Rectangle r )
		{
			this.r = r;
		}

		public void run()
		{
			if (component != null) component.scrollRectToVisible(r);
		}

		Rectangle r;
	}

	//
	// DefaultCaret extends Rectangle, which is Serializable. DefaultCaret
	// also implements FocusListener. Swing attempts to remove UI related
	// classes (such as this class) by installing a FocusListener. When
	// that FocusListener is messaged to writeObject it uninstalls the UI
	// (refer to JComponent.EnableSerializationFocusListener). The
	// problem with this is that any other FocusListeners will also get
	// serialized due to how AWT handles listeners. So, for the time being
	// this class is installed as the FocusListener on the JTextComponent,
	// and it will forward the FocusListener methods to the DefaultCaret.
	// Since FocusHandler is not Serializable DefaultCaret will not be
	// pulled in.
	//
	private static class FocusHandler implements FocusListener
	{
		private transient FocusListener fl;

		FocusHandler( FocusListener fl )
		{
			this.fl = fl;
		}

		/**
		 * Called when the component containing the caret gains focus. This is
		 * implemented to set the caret to visible if the component is editable.
		 * 
		 * @param e
		 *            the focus event
		 * @see FocusListener#focusGained
		 */
		public void focusGained(FocusEvent e)
		{
			fl.focusGained(e);
		}

		/**
		 * Called when the component containing the caret loses focus. This is
		 * implemented to set the caret to visibility to false.
		 * 
		 * @param e
		 *            the focus event
		 * @see FocusListener#focusLost
		 */
		public void focusLost(FocusEvent e)
		{
			fl.focusLost(e);
		}
	}

	class UpdateHandler implements PropertyChangeListener, DocumentListener,
			ActionListener
	{

		// --- ActionListener methods ----------------------------------

		/**
		 * Invoked when the blink timer fires. This is called asynchronously.
		 * The simply changes the visibility and repaints the rectangle that
		 * last bounded the caret.
		 * 
		 * @param e
		 *            the action event
		 */
		public void actionPerformed(ActionEvent e)
		{
			// DJTextPane dJTextPane = (DJTextPane)component;
			if (!visible && ( ( width == 0 ) || ( height == 0 ) ))
				setVisible(true);
			else
			{
				visible = !visible;
				try
				{
					

					/*
					 * TextUI mapper = component.getUI();
					 * for(int i=0; i<dJTextPane.grupoTrabajo.estaciones.size();
					 * i++){ DEstacion estacion =
					 * (DEstacion)dJTextPane.grupoTrabajo.estaciones.elementAt(i);
					 * estacion.r = mapper.modelToView(getComponent(),
					 * estacion.getPosicionCaret(), dotBias); estacion.r.x =
					 * estacion.r.x - 4; estacion.r.y = estacion.r.y;
					 * estacion.r.width = 10; estacion.r.height =
					 * estacion.r.height; }
					 */
				}
				catch (Exception ee)
				{
				}

				repaint();
			}
		}

		// --- DocumentListener methods --------------------------------

		/**
		 * Updates the dot and mark if they were changed by the insertion.
		 * 
		 * @param e
		 *            the document event
		 * @see DocumentListener#insertUpdate
		 */
		public void insertUpdate(DocumentEvent e)
		{
			if (async || SwingUtilities.isEventDispatchThread())
			{
				int adjust = 0;
				int offset = e.getOffset();
				int length = e.getLength();
				if (dot >= offset) adjust = length;
				if (mark >= offset) mark += length;

				if (adjust != 0)
					if (dot == offset)
					{
						Document doc = component.getDocument();
						int newDot = dot + adjust;
						boolean isNewline;
						try
						{
							Segment s = new Segment();
							doc.getText(newDot - 1, 1, s);
							isNewline = ( ( s.count > 0 ) && ( s.array[s.offset] == '\n' ) );
						}
						catch (BadLocationException ble)
						{
							isNewline = false;
						}
						if (isNewline)
							changeCaretPosition(newDot, Position.Bias.Forward);
						else changeCaretPosition(newDot, Position.Bias.Backward);
					}
					else changeCaretPosition(dot + adjust, dotBias);
			}
		}

		/**
		 * Updates the dot and mark if they were changed by the removal.
		 * 
		 * @param e
		 *            the document event
		 * @see DocumentListener#removeUpdate
		 */
		public void removeUpdate(DocumentEvent e)
		{
			if (async || SwingUtilities.isEventDispatchThread())
			{
				int adjust = 0;
				int offs0 = e.getOffset();
				int offs1 = offs0 + e.getLength();
				boolean adjustDotBias = false;
				if (dot >= offs1)
				{
					adjust = offs1 - offs0;
					if (dot == offs1) adjustDotBias = true;
				}
				else if (dot >= offs0)
				{
					adjust = dot - offs0;
					adjustDotBias = true;
				}
				boolean adjustMarkBias = false;
				if (mark >= offs1)
				{
					mark -= offs1 - offs0;
					if (mark == offs1) adjustMarkBias = true;
				}
				else if (mark >= offs0)
				{
					mark = offs0;
					adjustMarkBias = true;
				}
				if (mark == ( dot - adjust ))
					setDot(dot - adjust, guessBiasForOffset(dot - adjust,
							dotBias, dotLTR));
				else
				{
					if (adjustDotBias)
						dotBias = guessBiasForOffset(dot - adjust, dotBias,
								dotLTR);
					if (adjustMarkBias)
						markBias = guessBiasForOffset(mark, markBias, markLTR);
					changeCaretPosition(dot - adjust, dotBias);
					markLTR = isPositionLTR(mark, markBias);
				}
			}
		}

		/**
		 * Gives notification that an attribute or set of attributes changed.
		 * 
		 * @param e
		 *            the document event
		 * @see DocumentListener#changedUpdate
		 */
		public void changedUpdate(DocumentEvent e)
		{
		}

		// --- PropertyChangeListener methods -----------------------

		/**
		 * This method gets called when a bound property is changed. We are
		 * looking for document changes on the editor.
		 */
		public void propertyChange(PropertyChangeEvent evt)
		{
			Object oldValue = evt.getOldValue();
			Object newValue = evt.getNewValue();
			if (( oldValue instanceof Document )
					|| ( newValue instanceof Document ))
			{
				setDot(0);
				if (oldValue != null)
					( (Document) oldValue ).removeDocumentListener(this);
				if (newValue != null)
					( (Document) newValue ).addDocumentListener(this);
			}
		}

	}
}

/*
 * package keymap;
 * 
 * import javax.swing.text.DefaultCaret; import java.awt.*; import
 * java.awt.geom.*; import javax.swing.plaf.*; import javax.swing.text.*; import
 * java.awt.event.*; import java.beans.*; import java.awt.event.ActionEvent;
 * import java.awt.event.ActionListener; import java.io.*; import javax.swing.*;
 * import javax.swing.event.*; import javax.swing.plaf.*; import
 * java.util.EventListener; /** <p>Title: </p> <p>Description: </p> <p>Copyright:
 * Copyright (c) 2003</p> <p>Company: </p> @author unascribed
 * 
 * @version 1.0
 */

/*
 * public class DCaret extends DefaultCaret { transient private int[]
 * flagXPoints = new int[3]; transient private int[] flagYPoints = new int[3];
 * transient Position.Bias dotBias; JTextComponent component; int num = 0;
 * 
 * public DCaret() { super(); component = super.getComponent();
 * super.setBlinkRate(400); }
 * 
 * 
 * 
 * public void paint(Graphics g) { if(isVisible()) { try { TextUI mapper =
 * super.getComponent().getUI(); Rectangle r =
 * mapper.modelToView(super.getComponent(), super.getDot(), dotBias);
 * System.out.println(dotBias); x = r.x - 4; y = r.y; width = 10; height =
 * r.height; g.setColor(super.getComponent().getCaretColor()); g.drawLine(r.x,
 * r.y, r.x, r.y + r.height - 1); //g.drawLine(r.x, r.y, r.x+20, r.y + r.height -
 * 1);
 * 
 * /*DJTextPane dJTextPane = ((DJTextPane)super.getComponent()); DEstacion
 * estacion = (DEstacion)dJTextPane.grupoTrabajo.estaciones.elementAt(0); r =
 * mapper.modelToView(super.getComponent(), estacion.getPosicionCaret(),
 * dotBias); g.setColor(Color.red); g.drawLine(r.x, r.y, r.x, r.y + r.height -
 * 1);
 * 
 * 
 * /*Rectangle auxr = mapper.modelToView(super.getComponent(), super.getDot()+1,
 * dotBias); g.setColor(super.getComponent().getCaretColor());
 */

// *****************************************************************************
// ****** Pintamos los caret de las demas estaciones ***************************
// *****************************************************************************
/*
 * DJTextPane dJTextPane = ((DJTextPane)super.getComponent()); int numEstaciones =
 * dJTextPane.grupoTrabajo.estaciones.size(); for(int i = 0; i < numEstaciones;
 * i++){ if( i != dJTextPane.numEstacion ){ DEstacion estacion =
 * (DEstacion)dJTextPane.grupoTrabajo.estaciones.elementAt(i); r =
 * mapper.modelToView(super.getComponent(), estacion.getPosicionCaret(),
 * dotBias); x = r.x - 4; y = r.y; width = 10; height = r.height;
 * g.setColor(estacion.getColor()); g.drawLine(r.x, r.y, r.x, r.y + r.height -
 * 1); //System.out.println("Repintado caret estacion "+ i+" en posicion
 * "+estacion.getPosicionCaret()); } }
 */
// *****************************************************************************

// see if we should paint a flag to indicate the bias
// of the caret.
// PENDING(prinz) this should be done through
// protected methods so that alternative LAF
// will show bidi information.
/*
 * Document doc = super.getComponent().getDocument(); if (doc instanceof
 * AbstractDocument) { Element bidi =
 * ((AbstractDocument)doc).getBidiRootElement(); if ((bidi != null) &&
 * (bidi.getElementCount() > 1)) { // there are multiple directions present.
 * flagXPoints[0] = r.x; flagYPoints[0] = r.y; flagXPoints[1] = r.x;
 * flagYPoints[1] = r.y + 4; flagYPoints[2] = r.y; flagXPoints[2] = (true) ? r.x +
 * 5 : r.x - 4; g.fillPolygon(flagXPoints, flagYPoints, 3); } } } catch
 * (BadLocationException e) { // can't render I guess
 * //System.err.println("Can't render cursor"); } } }
 * 
 * protected synchronized void damage(Rectangle r) { if (r != null) { x = r.x -
 * 4; y = r.y; width = 10; height = r.height; repaint(); } TextUI mapper =
 * super.getComponent().getUI(); DEstacion estacion =
 * (DEstacion)dJTextPane.grupoTrabajo.estaciones.elementAt(0); r =
 * mapper.modelToView(super.getComponent(), estacion.getPosicionCaret(),
 * dotBias); g.setColor(Color.red); g.drawLine(r.x, r.y, r.x, r.y + r.height -
 * 1);
 *  }
 * 
 * 
 * public PathIterator getPathIterator(AffineTransform at, double flatness) {
 * /**@todo Implement this java.awt.Shape abstract method
 */
/*
 * throw new java.lang.UnsupportedOperationException("Method getPathIterator()
 * not yet implemented."); } public boolean contains(Rectangle2D r) { /**@todo
 * Implement this java.awt.Shape abstract method
 */
/*
 * throw new java.lang.UnsupportedOperationException("Method contains() not yet
 * implemented."); } public boolean intersects(Rectangle2D r) { /**@todo
 * Implement this java.awt.Shape abstract method
 */
/*
 * throw new java.lang.UnsupportedOperationException("Method intersects() not
 * yet implemented."); } public boolean contains(double x, double y, double w,
 * double h) { /**@todo Implement this java.awt.Shape abstract method
 */
/*
 * throw new java.lang.UnsupportedOperationException("Method contains() not yet
 * implemented."); } public boolean contains(double x, double y) { /**@todo
 * Implement this java.awt.Shape abstract method
 */
/*
 * throw new java.lang.UnsupportedOperationException("Method contains() not yet
 * implemented."); } public boolean contains(Point2D p) { /**@todo Implement
 * this java.awt.Shape abstract method
 */
/*
 * throw new java.lang.UnsupportedOperationException("Method contains() not yet
 * implemented."); } public boolean intersects(double x, double y, double w,
 * double h) { /**@todo Implement this java.awt.Shape abstract method
 */
/*
 * throw new java.lang.UnsupportedOperationException("Method intersects() not
 * yet implemented."); } public PathIterator getPathIterator(AffineTransform at) {
 * /**@todo Implement this java.awt.Shape abstract method
 */
/*
 * throw new java.lang.UnsupportedOperationException("Method getPathIterator()
 * not yet implemented."); }
 * 
 *  }
 */
