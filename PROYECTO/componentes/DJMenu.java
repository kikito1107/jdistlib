package componentes;

import javax.swing.JMenu;
import javax.swing.Action;
import java.awt.*;
import javax.swing.*;
import java.util.*;

import javax.accessibility.*;

import componentes.listeners.*;

import interfaces.*;
import Deventos.*;
import Deventos.enlaceJS.*;

import util.*;

public class DJMenu
	 extends JMenu
	 implements DComponente {
  private static final String uiClassID = "DMenuMetalMenuUI";
  private DJMenuPopup popup = null;
  protected WinListener dpopupListener;
  private Point dcustomMenuLocation = null;

  private Vector djmenulisteners = new Vector(5);
  private Integer DID = new Integer( -1);
  private String nombre = null;
  private ColaEventos colaRecepcion = new ColaEventos();
  private ColaEventos colaEnvio = null;
  private Integer ultimoProcesado = new Integer( -1);
  private int nivelPermisos = 10;

  public DJMenu(String nombre) {
	 this.nombre = nombre;
	 extrasConstructor();
  }

  public DJMenu(String p0, String nombre) {
	 super(p0);
	 this.nombre = nombre;
	 extrasConstructor();
  }

  public DJMenu(String p0, boolean p1, String nombre) {
	 super(p0, p1);
	 this.nombre = nombre;
	 extrasConstructor();
  }

  public DJMenu(Action p0, String nombre) {
	 super(p0);
	 this.nombre = nombre;
	 extrasConstructor();
  }

  public String getUIClassID() {
	 return uiClassID;
  }

  private void extrasConstructor() {
	 addDJMenuListener(new Listener());
	 DID = new Integer(DConector.alta(this));
	 colaEnvio = DConector.getColaEventos();
  }

  public boolean oculto() {
	 return false;
  }

  public DComponente obtenerComponente(int num) {
	 return null;
  }

  public DComponente obtenerComponente(String nombre) {
	 return null;
  }

  public void procesarEventoHijo(DEvent evento) {
  }

  public void padreOcultado() {
  }

  public void padreMostrado() {
  }

  public DComponente obtenerPadre() {
	 return null;
  }

  public int obtenerNumComponentesHijos() {
	 return 0;
  }

  public void addDJMenuListener(DJMenuListener listener) {
	 djmenulisteners.add(listener);
  }

  public Vector getDJMenuListeners() {
	 return djmenulisteners;
  }

  public void activar() {
	 setEnabled(true);
  }

  public void desactivar() {
	 setEnabled(false);
  }

  public void iniciarHebraProcesadora() {
	 Thread t = new Thread(new HebraProcesadora(colaRecepcion, this));
	 t.start();
  }

  public void procesarEvento(DEvent evento) {
	 colaRecepcion.nuevoEvento(evento);
  }

  public void procesarMetaInformacion(DMIEvent evento) {
	 if (evento.tipo.intValue() == DMIEvent.INFO_COMPLETA.intValue()) {
		int permiso = evento.obtenerPermisoComponente(nombre);
		setNivelPermisos(permiso);
	 }
  }

  public void sincronizar() {
	 // No se realiza sincronizacion
  }

  public int getNivelPermisos() {
	 return nivelPermisos;
  }

  public void setNivelPermisos(int nivel) {
	 if (!isEnabled()) {
		setEnabled(true);
	 }
	 if (nivel >= 20) {
		setForeground(Color.BLACK);
	 }
	 else {
		setForeground(Color.GRAY);
		if (isTopLevelMenu()) {
		  Vector v = getDJMenuListeners();
		  DJMenuEvent evento = new DJMenuEvent();
		  evento.path = new Vector();
		  for (int i = 0; i < v.size(); i++) {
			 ( (DJMenuListener) v.elementAt(i)).cambioEstado(evento);
		  }
		}
	 }
	 nivelPermisos = nivel;
  }

  public Integer getID() {
	 return DID;
  }

  public String getNombre() {
	 return nombre;
  }

  public ColaEventos obtenerColaRecepcion() {
	 return colaRecepcion;
  }

  public ColaEventos obtenerColaEnvio() {
	 return colaEnvio;
  }

  public HebraProcesadoraBase crearHebraProcesadora() {
	 return null;
  }

  public boolean isPopupMenuVisible() {
	 asegurarPopupCreado();
	 return popup.isVisible();
  }

  public JPopupMenu getPopupMenu() {
	 asegurarPopupCreado();
	 return popup;
  }

  public void setMenuLocation(int x, int y) {
	 dcustomMenuLocation = new Point(x, y);
	 if (popup != null)
		popup.setLocation(x, y);
  }

  public void setPopupMenuVisible(boolean b) {

	 boolean isVisible = isPopupMenuVisible();
	 if (b != isVisible && (isEnabled() || !b)) {
		asegurarPopupCreado();
		if ( (b == true) && isShowing()) {
		  // Set location of popupMenu (pulldown or pullright)
		  Point p = obtenerCustomMenuLocation();
		  if (p == null) {
			 p = getPopupMenuOrigin();
		  }
		  getPopupMenu().show(this, p.x, p.y);
		}
		else {
		  getPopupMenu().setVisible(false);
		}
	 }

  }

  public JMenuItem add(JMenuItem menuItem) {
	 AccessibleContext ac = menuItem.getAccessibleContext();
	 ac.setAccessibleParent(this);
	 asegurarPopupCreado();
	 return popup.add(menuItem);
  }

  public Component add(Component c) {
	 if (c instanceof JComponent) {
		AccessibleContext ac = ( (JComponent) c).getAccessibleContext();
		if (ac != null) {
		  ac.setAccessibleParent(this);
		}
	 }
	 asegurarPopupCreado();
	 popup.add(c);
	 return c;
  }

  public Component add(Component c, int index) {
	 if (c instanceof JComponent) {
		AccessibleContext ac = ( (JComponent) c).getAccessibleContext();
		if (ac != null) {
		  ac.setAccessibleParent(this);
		}
	 }
	 asegurarPopupCreado();
	 popup.add(c, index);
	 return c;
  }

  public void addSeparator() {
	 asegurarPopupCreado();
	 popup.addSeparator();
  }

  public void insert(String s, int pos) {
	 if (pos < 0) {
		throw new IllegalArgumentException("index less than zero.");
	 }

	 asegurarPopupCreado();
	 popup.insert(new JMenuItem(s), pos);
  }

  public JMenuItem insert(JMenuItem mi, int pos) {
	 if (pos < 0) {
		throw new IllegalArgumentException("index less than zero.");
	 }
	 AccessibleContext ac = mi.getAccessibleContext();
	 ac.setAccessibleParent(this);
	 asegurarPopupCreado();
	 popup.insert(mi, pos);
	 return mi;
  }

  public JMenuItem insert(Action a, int pos) {
	 if (pos < 0) {
		throw new IllegalArgumentException("index less than zero.");
	 }

	 asegurarPopupCreado();
	 JMenuItem mi = new JMenuItem( (String) a.getValue(Action.NAME),
											(Icon) a.getValue(Action.SMALL_ICON));
	 mi.setHorizontalTextPosition(JButton.TRAILING);
	 mi.setVerticalTextPosition(JButton.CENTER);
	 mi.setEnabled(a.isEnabled());
	 mi.setAction(a);
	 popup.insert(mi, pos);
	 return mi;
  }

  public void insertSeparator(int index) {
	 if (index < 0) {
		throw new IllegalArgumentException("index less than zero.");
	 }

	 asegurarPopupCreado();
	 popup.insert(new JPopupMenu.Separator(), index);
  }

  public void remove(JMenuItem item) {
	 if (popup != null)
		popup.remove(item);
  }

  public void remove(int pos) {
	 if (pos < 0) {
		throw new IllegalArgumentException("index less than zero.");
	 }
	 if (pos > getItemCount()) {
		throw new IllegalArgumentException(
			 "index greater than the number of items.");
	 }
	 if (popup != null)
		popup.remove(pos);
  }

  public void remove(Component c) {
	 if (popup != null)
		popup.remove(c);
  }

  public void removeAll() {
	 if (popup != null)
		popup.removeAll();
  }

  public int getMenuComponentCount() {
	 int componentCount = 0;
	 if (popup != null)
		componentCount = popup.getComponentCount();
	 return componentCount;
  }

  public Component getMenuComponent(int n) {
	 if (popup != null)
		return popup.getComponent(n);

	 return null;
  }

  public Component[] getMenuComponents() {
	 if (popup != null)
		return popup.getComponents();

	 return new Component[0];
  }

  public MenuElement[] getSubElements() {
	 if (popup == null)
		return new MenuElement[0];
	 else {
		MenuElement result[] = new MenuElement[1];
		result[0] = popup;
		return result;
	 }
  }

  public void applyComponentOrientation(ComponentOrientation o) {
	 super.applyComponentOrientation(o);

	 if (popup != null) {
		int ncomponents = getMenuComponentCount();
		for (int i = 0; i < ncomponents; ++i) {
		  getMenuComponent(i).applyComponentOrientation(o);
		}
		popup.setComponentOrientation(o);
	 }
  }

  public void setComponentOrientation(ComponentOrientation o) {
	 super.setComponentOrientation(o);
	 if (popup != null) {
		popup.setComponentOrientation(o);
	 }
  }

  private Point obtenerCustomMenuLocation() {
	 return dcustomMenuLocation;
  }

  private void asegurarPopupCreado() {
	 if (popup == null) {
		final JMenu thisMenu = this;
		this.popup = new DJMenuPopup();
		popup.setInvoker(this);
		dpopupListener = createWinListener(popup);
	 }
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

  private MenuElement[] vectorToPath(Vector v) {
	 MenuElement barraMenu = ( (DMenuSelectionManager) DMenuSelectionManager.
									  defaultManager()).getBarraMenu();
	 MenuElement nodoActual = this;

	 MenuElement[] me = new MenuElement[v.size()];
	 MenuElement elementoActual = null;
	 int i, j;
	 if (me.length > 0) {
		me[0] = barraMenu;
		elementoActual = barraMenu;
		for (i = 1; i < v.size(); i++) {
		  if ( ( (Integer) v.elementAt(i)).intValue() == -5) { // es un popup
			 me[i] = ( (JMenu) elementoActual).getPopupMenu();
			 elementoActual = me[i];
		  }
		  else {
			 int indice = ( (Integer) v.elementAt(i)).intValue();
			 me[i] = (MenuElement) ( (Container) elementoActual).getComponent(
indice);
			 elementoActual = me[i];
		  }
		}
	 }
	 return me;
  }

//*****************************************************************************
	private class Listener
		 implements DJMenuListener {

	  public void cambioEstado(DJMenuEvent evento) {
		 boolean permiso = false;
		 if (nivelPermisos >= 20) {
			if (DJMenuBar.propietarioSeleccion == null) {
			  permiso = true;
			}
else {
			  if (DJMenuBar.propietarioSeleccion.equals(DConector.Dusuario))
				 permiso = true;
			}

			if (permiso) {
			  evento.componente = new Integer(DID.intValue());
			  evento.origen = new Integer(1); // Aplicacion
			  evento.destino = new Integer(0); // Coordinador
			  evento.componente = new Integer(DID.intValue());
			  evento.tipo = new Integer(DJMenuEvent.CAMBIO_ESTADO.intValue());
			  evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

			  colaEnvio.nuevoEvento(evento);
			}
		 }
	  }
	}

  class HebraProcesadora
		implements Runnable {

	 ColaEventos cola = null;
	 DJMenu menu = null;

	 HebraProcesadora(ColaEventos cola, DJMenu menu) {
		this.cola = cola;
		this.menu = menu;
	 }

	 public void run() {
		DJMenuEvent evento = null;

		//setEnabled(true);

		while (true) {
		  evento = (DJMenuEvent) cola.extraerEvento();
		  ultimoProcesado = new Integer(evento.contador.intValue());

		  boolean permiso = true;

		  if (DJMenuBar.propietarioSeleccion != null) {
			 if (!DJMenuBar.propietarioSeleccion.equals(evento.usuario)) {
				permiso = false;
			 }
		  }

		  if (nivelPermisos >= 10 && permiso) {
			 /*System.out.println("HebraProcesadora(" + DID +
									  "): Procesado: Tipo=" + evento.tipo +
									  " Ult. Proc.=" + evento.ultimoProcesado);*/
			 if (evento.tipo.intValue() == DJMenuEvent.CAMBIO_ESTADO.intValue()) {
				if (evento.path.size() > 0) {
				  if (DJMenuBar.propietarioSeleccion == null) {
					 DJMenuBar.propietarioSeleccion = new String(evento.usuario);
					 DJMenuBar.usuario.setText("    [" + evento.usuario + "]");
				  }
				  MenuElement[] path = vectorToPath(evento.path);
				  DMenuSelectionManager.defaultManager().setSelectedPath(path);
				  for (int j = 0; j < path.length; j++) {
					 if (path[j] instanceof JPopupMenu) {
						Toolkit.getDefaultToolkit().getSystemEventQueue().invokeLater(new RepintadorPopupMenu((JPopupMenu) path[j]));
						( (JPopupMenu) path[j]).repaint();
					 }
				  }
				  // Repintamos los punteros remotos para que queden por encima del menu
				  Toolkit.getDefaultToolkit().getSystemEventQueue().invokeLater(new RepintadorGlassPane(DJFrame.frame));
				}
				else {
				  DMenuSelectionManager.defaultManager().clearSelectedPath();
				  DJMenuBar.propietarioSeleccion = null;
				  DJMenuBar.usuario.setText("");
				  // Repintamos los punteros remotos para que queden por encima del menu
				  Toolkit.getDefaultToolkit().getSystemEventQueue().invokeLater(new RepintadorGlassPane(DJFrame.frame));
				}
			 }
		  }
		}
	 }
  }

  private class RepintadorPopupMenu implements Runnable{
	 JPopupMenu popup = null;
	 public RepintadorPopupMenu(JPopupMenu popup){
		this.popup = popup;
	 }
	 public void run(){
		popup.repaint();
}
  }
  private class RepintadorGlassPane implements Runnable{
	 JFrame frame = null;
	 public RepintadorGlassPane(JFrame frame){
		this.frame = frame;
	 }
	 public void run(){
		frame.getGlassPane().repaint();
}
  }

}
