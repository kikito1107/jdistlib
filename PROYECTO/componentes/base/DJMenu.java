package componentes.base;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Point;
import java.util.Vector;

import javax.accessibility.AccessibleContext;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.SwingConstants;

import Deventos.ColaEventos;
import Deventos.DEvent;
import Deventos.DJMenuEvent;
import Deventos.DMIEvent;
import Deventos.enlaceJS.DConector;

import componentes.listeners.DJMenuListener;
import componentes.util.DMenuSelectionManager;

/**
 * Implementacion de la clase captadora de eventos para el componente Menu
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DJMenu extends JMenu implements DComponente
{
	private static final long serialVersionUID = 7773903204628794169L;

	private static final String uiClassID = "DMenuMetalMenuUI";

	private DJMenuPopup popup = null;

	protected WinListener dpopupListener;

	private Point dcustomMenuLocation = null;

	@SuppressWarnings( "unchecked" )
	private Vector djmenulisteners = new Vector(5);

	private Integer DID = new Integer(-1);

	private String nombre = null;

	private ColaEventos colaRecepcion = new ColaEventos();

	private ColaEventos colaEnvio = null;

	private Integer ultimoProcesado = new Integer(-1);

	private int nivelPermisos = 10;

	public DJMenu( String nombre )
	{
		this.nombre = nombre;
		extrasConstructor();
	}

	public DJMenu( String p0, String nombre )
	{
		super(p0);
		this.nombre = nombre;
		extrasConstructor();
	}

	public DJMenu( String p0, boolean p1, String nombre )
	{
		super(p0, p1);
		this.nombre = nombre;
		extrasConstructor();
	}

	public DJMenu( Action p0, String nombre )
	{
		super(p0);
		this.nombre = nombre;
		extrasConstructor();
	}

	@Override
	public String getUIClassID()
	{
		return uiClassID;
	}

	private void extrasConstructor()
	{
		addDJMenuListener(new Listener());
		DID = new Integer(DConector.alta(this));
		colaEnvio = DConector.getColaEventos();
	}

	public boolean oculto()
	{
		return false;
	}

	public DComponente obtenerComponente(int num)
	{
		return null;
	}

	public DComponente obtenerComponente(String nombre)
	{
		return null;
	}

	public void procesarEventoHijo(DEvent evento)
	{
	}

	public void padreOcultado()
	{
	}

	public void padreMostrado()
	{
	}

	public DComponente obtenerPadre()
	{
		return null;
	}

	public int obtenerNumComponentesHijos()
	{
		return 0;
	}

	@SuppressWarnings( "unchecked" )
	public void addDJMenuListener(DJMenuListener listener)
	{
		djmenulisteners.add(listener);
	}

	@SuppressWarnings( "unchecked" )
	public Vector getDJMenuListeners()
	{
		return djmenulisteners;
	}

	public void activar()
	{
		setEnabled(true);
	}

	public void desactivar()
	{
		setEnabled(false);
	}

	public void iniciarHebraProcesadora()
	{
		Thread t = new Thread(new HebraProcesadora(colaRecepcion, this));
		t.start();
	}

	public void procesarEvento(DEvent evento)
	{
		colaRecepcion.nuevoEvento(evento);
	}

	public void procesarMetaInformacion(DMIEvent evento)
	{
		if (evento.tipo.intValue() == DMIEvent.INFO_COMPLETA.intValue())
		{
			int permiso = evento.obtenerPermisoComponente(nombre);
			setNivelPermisos(permiso);
		}
	}

	public void sincronizar()
	{
		// No se realiza sincronizacion
	}

	public int getNivelPermisos()
	{
		return nivelPermisos;
	}

	@SuppressWarnings( "unchecked" )
	public void setNivelPermisos(int nivel)
	{
		if (!isEnabled()) setEnabled(true);
		if (nivel >= 20)
			setForeground(Color.BLACK);
		else
		{
			setForeground(Color.GRAY);
			if (isTopLevelMenu())
			{
				Vector v = getDJMenuListeners();
				DJMenuEvent evento = new DJMenuEvent();
				evento.path = new Vector();
				for (int i = 0; i < v.size(); i++)
					( (DJMenuListener) v.elementAt(i) ).cambioEstado(evento);
			}
		}
		nivelPermisos = nivel;
	}

	public Integer getID()
	{
		return DID;
	}

	public String getNombre()
	{
		return nombre;
	}

	public ColaEventos obtenerColaRecepcion()
	{
		return colaRecepcion;
	}

	public ColaEventos obtenerColaEnvio()
	{
		return colaEnvio;
	}

	public HebraProcesadoraBase crearHebraProcesadora()
	{
		return null;
	}

	@Override
	public boolean isPopupMenuVisible()
	{
		asegurarPopupCreado();
		return popup.isVisible();
	}

	@Override
	public JPopupMenu getPopupMenu()
	{
		asegurarPopupCreado();
		return popup;
	}

	@Override
	public void setMenuLocation(int x, int y)
	{
		dcustomMenuLocation = new Point(x, y);
		if (popup != null) popup.setLocation(x, y);
	}

	@Override
	public void setPopupMenuVisible(boolean b)
	{

		boolean isVisible = isPopupMenuVisible();
		if (( b != isVisible ) && ( isEnabled() || !b ))
		{
			asegurarPopupCreado();
			if (( b == true ) && isShowing())
			{
				// Set location of popupMenu (pulldown or pullright)
				Point p = obtenerCustomMenuLocation();
				if (p == null) p = getPopupMenuOrigin();
				getPopupMenu().show(this, p.x, p.y);
			}
			else getPopupMenu().setVisible(false);
		}

	}

	@Override
	public JMenuItem add(JMenuItem menuItem)
	{
		AccessibleContext ac = menuItem.getAccessibleContext();
		ac.setAccessibleParent(this);
		asegurarPopupCreado();
		return popup.add(menuItem);
	}

	@Override
	public Component add(Component c)
	{
		if (c instanceof JComponent)
		{
			AccessibleContext ac = ( (JComponent) c ).getAccessibleContext();
			if (ac != null) ac.setAccessibleParent(this);
		}
		asegurarPopupCreado();
		popup.add(c);
		return c;
	}

	@Override
	public Component add(Component c, int index)
	{
		if (c instanceof JComponent)
		{
			AccessibleContext ac = ( (JComponent) c ).getAccessibleContext();
			if (ac != null) ac.setAccessibleParent(this);
		}
		asegurarPopupCreado();
		popup.add(c, index);
		return c;
	}

	@Override
	public void addSeparator()
	{
		asegurarPopupCreado();
		popup.addSeparator();
	}

	@Override
	public void insert(String s, int pos)
	{
		if (pos < 0)
			throw new IllegalArgumentException("index less than zero.");

		asegurarPopupCreado();
		popup.insert(new JMenuItem(s), pos);
	}

	@Override
	public JMenuItem insert(JMenuItem mi, int pos)
	{
		if (pos < 0)
			throw new IllegalArgumentException("index less than zero.");
		AccessibleContext ac = mi.getAccessibleContext();
		ac.setAccessibleParent(this);
		asegurarPopupCreado();
		popup.insert(mi, pos);
		return mi;
	}

	@Override
	public JMenuItem insert(Action a, int pos)
	{
		if (pos < 0)
			throw new IllegalArgumentException("index less than zero.");

		asegurarPopupCreado();
		JMenuItem mi = new JMenuItem((String) a.getValue(Action.NAME), (Icon) a
				.getValue(Action.SMALL_ICON));
		mi.setHorizontalTextPosition(SwingConstants.TRAILING);
		mi.setVerticalTextPosition(SwingConstants.CENTER);
		mi.setEnabled(a.isEnabled());
		mi.setAction(a);
		popup.insert(mi, pos);
		return mi;
	}

	@Override
	public void insertSeparator(int index)
	{
		if (index < 0)
			throw new IllegalArgumentException("index less than zero.");

		asegurarPopupCreado();
		popup.insert(new JPopupMenu.Separator(), index);
	}

	@Override
	public void remove(JMenuItem item)
	{
		if (popup != null) popup.remove(item);
	}

	@Override
	public void remove(int pos)
	{
		if (pos < 0)
			throw new IllegalArgumentException("index less than zero.");
		if (pos > getItemCount())
			throw new IllegalArgumentException(
					"index greater than the number of items.");
		if (popup != null) popup.remove(pos);
	}

	@Override
	public void remove(Component c)
	{
		if (popup != null) popup.remove(c);
	}

	@Override
	public void removeAll()
	{
		if (popup != null) popup.removeAll();
	}

	@Override
	public int getMenuComponentCount()
	{
		int componentCount = 0;
		if (popup != null) componentCount = popup.getComponentCount();
		return componentCount;
	}

	@Override
	public Component getMenuComponent(int n)
	{
		if (popup != null) return popup.getComponent(n);

		return null;
	}

	@Override
	public Component[] getMenuComponents()
	{
		if (popup != null) return popup.getComponents();

		return new Component[0];
	}

	@Override
	public MenuElement[] getSubElements()
	{
		if (popup == null)
			return new MenuElement[0];
		else
		{
			MenuElement result[] = new MenuElement[1];
			result[0] = popup;
			return result;
		}
	}

	@Override
	public void applyComponentOrientation(ComponentOrientation o)
	{
		super.applyComponentOrientation(o);

		if (popup != null)
		{
			int ncomponents = getMenuComponentCount();
			for (int i = 0; i < ncomponents; ++i)
				getMenuComponent(i).applyComponentOrientation(o);
			popup.setComponentOrientation(o);
		}
	}

	@Override
	public void setComponentOrientation(ComponentOrientation o)
	{
		super.setComponentOrientation(o);
		if (popup != null) popup.setComponentOrientation(o);
	}

	private Point obtenerCustomMenuLocation()
	{
		return dcustomMenuLocation;
	}

	private void asegurarPopupCreado()
	{
		if (popup == null)
		{
			this.popup = new DJMenuPopup();
			popup.setInvoker(this);
			dpopupListener = createWinListener(popup);
		}
	}

	@SuppressWarnings( "unchecked" )
	private MenuElement[] vectorToPath(Vector v)
	{
		MenuElement barraMenu = ( (DMenuSelectionManager) DMenuSelectionManager
				.defaultManager() ).getBarraMenu();

		MenuElement[] me = new MenuElement[v.size()];
		MenuElement elementoActual = null;
		if (me.length > 0)
		{
			me[0] = barraMenu;
			elementoActual = barraMenu;
			for (int i = 1; i < v.size(); i++)
				if (( (Integer) v.elementAt(i) ).intValue() == -5)
				{ // es un popup
					me[i] = ( (JMenu) elementoActual ).getPopupMenu();
					elementoActual = me[i];
				}
				else
				{
					int indice = ( (Integer) v.elementAt(i) ).intValue();
					me[i] = (MenuElement) ( (Container) elementoActual )
							.getComponent(indice);
					elementoActual = me[i];
				}
		}
		return me;
	}

	// *****************************************************************************
	private class Listener implements DJMenuListener
	{

		public void cambioEstado(DJMenuEvent evento)
		{
			boolean permiso = false;
			if (nivelPermisos >= 20)
			{
				if (DJMenuBar.propietarioSeleccion == null)
					permiso = true;
				else if (DJMenuBar.propietarioSeleccion
						.equals(DConector.Dusuario)) permiso = true;

				if (permiso)
				{
					evento.componente = new Integer(DID.intValue());
					evento.origen = new Integer(1); // Aplicacion
					evento.destino = new Integer(0); // Coordinador
					evento.componente = new Integer(DID.intValue());
					evento.tipo = new Integer(DJMenuEvent.CAMBIO_ESTADO
							.intValue());
					evento.ultimoProcesado = new Integer(ultimoProcesado
							.intValue());

					colaEnvio.nuevoEvento(evento);
				}
			}
		}
	}

	class HebraProcesadora implements Runnable
	{

		ColaEventos cola = null;

		DJMenu menu = null;

		HebraProcesadora( ColaEventos cola, DJMenu menu )
		{
			this.cola = cola;
			this.menu = menu;
		}

		public void run()
		{
			DJMenuEvent evento = null;

			// setEnabled(true);

			while (true)
			{
				evento = (DJMenuEvent) cola.extraerEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());

				boolean permiso = true;

				if (DJMenuBar.propietarioSeleccion != null)
					if (!DJMenuBar.propietarioSeleccion.equals(evento.usuario))
						permiso = false;

				if (( nivelPermisos >= 10 ) && permiso)
					/*
					 * System.out.println("HebraProcesadora(" + DID + "):
					 * Procesado: Tipo=" + evento.tipo + " Ult. Proc.=" +
					 * evento.ultimoProcesado);
					 */
					if (evento.tipo.intValue() == DJMenuEvent.CAMBIO_ESTADO
							.intValue())
						if (evento.path.size() > 0)
						{
							if (DJMenuBar.propietarioSeleccion == null)
							{
								DJMenuBar.propietarioSeleccion = new String(
										evento.usuario);
								DJMenuBar.usuario.setText("    ["
										+ evento.usuario + "]");
							}
							MenuElement[] path = vectorToPath(evento.path);
							DMenuSelectionManager.defaultManager()
									.setSelectedPath(path);
							for (MenuElement element : path)
								if (element instanceof JPopupMenu)
								{
									EventQueue
											.invokeLater(new RepintadorPopupMenu(
													(JPopupMenu) element));
									( (JPopupMenu) element ).repaint();
								}
							// Repintamos los punteros remotos para que queden
							// por encima del menu
							EventQueue.invokeLater(new RepintadorGlassPane(
									DJFrame.frame));
						}
						else
						{
							DMenuSelectionManager.defaultManager()
									.clearSelectedPath();
							DJMenuBar.propietarioSeleccion = null;
							DJMenuBar.usuario.setText("");
							// Repintamos los punteros remotos para que queden
							// por encima del menu
							EventQueue.invokeLater(new RepintadorGlassPane(
									DJFrame.frame));
						}
			}
		}
	}

	private class RepintadorPopupMenu implements Runnable
	{
		JPopupMenu popup = null;

		public RepintadorPopupMenu( JPopupMenu popup )
		{
			this.popup = popup;
		}

		public void run()
		{
			popup.repaint();
		}
	}

	private class RepintadorGlassPane implements Runnable
	{
		JFrame frame = null;

		public RepintadorGlassPane( JFrame frame )
		{
			this.frame = frame;
		}

		public void run()
		{
			frame.getGlassPane().repaint();
		}
	}

}
