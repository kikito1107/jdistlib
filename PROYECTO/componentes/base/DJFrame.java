package componentes.base;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Deventos.ColaEventos;
import Deventos.DEvent;
import Deventos.DMIEvent;
import Deventos.DMouseEvent;
import Deventos.enlaceJS.DConector;
import Deventos.enlaceJS.DialogoSincronizacion;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DJFrame extends JFrame {
  /**
	 * 
	 */
	private static final long serialVersionUID = -1361890889404193956L;
BorderLayout borderLayout1 = new BorderLayout();
  private GestorMousesRemotos gestorMR = null;

  //private DConector dconector = null;
  private Point posMouse = new Point();
  private boolean sincronizado = false;
  public static DJFrame frame = null;

  public DJFrame(boolean mostrarPunterosRemotos,
					  String nombreGestorMousesRemotos) {
	 DJFrame.frame = this;
	 
	 String nombre = nombreGestorMousesRemotos;
	 if (nombre == null) {
		nombre = new String("GMR");
	 }
	 if (mostrarPunterosRemotos) {
		gestorMR = new GestorMousesRemotos(this, nombre);
	 }

	 try {
		jbInit();
	 }
	 catch (Exception ex) {
		ex.printStackTrace();
	 }
  }

  public DJFrame(String nombreAplicacion, String nombreJS,
					  boolean mostrarPunterosRemotos,
					  String nombreGestorMousesRemotos) {
	 if (nombreJS == null) {
		nombreJS = new String("JavaSpace");
	 }
	 this.frame = this;
	 String nombre = nombreGestorMousesRemotos;
	 if (nombre == null) {
		nombre = new String("GMR");
	 }
	 if (mostrarPunterosRemotos) {
		gestorMR = new GestorMousesRemotos(this, nombre);
	 }

	 try {
		jbInit();
	 }
	 catch (Exception ex) {
		ex.printStackTrace();
	 }
  }

  public void sincronizar() {
	 //dconector.sincronizarComponentes(this);
  }

  public void setVisible(boolean b) {
	 super.setVisible(b);
	 if (b && !sincronizado) {
		sincronizar();
	 }
  }

  void jbInit() throws Exception {
	 this.setGlassPane(new PanelGlass());
	 super.setResizable(false);
	 this.getGlassPane().setVisible(true);

// ****** CAMBIAMOS LA COLA DE EVENTOS DE LA APLICACION PARA    ***********
	 // ****** PARA COGER TODOS LOS EVENTOS DE MOVIMIENTO DEL RATON  ***********
	 Toolkit.getDefaultToolkit().getSystemEventQueue().push(new DEventQueue(this));
	 // ************************************************************************

	 this.getContentPane().setLayout(borderLayout1);
  }

  public void setResizable(boolean b) {

  }

  private class PanelContenido
		extends JPanel {
	 int i = 0;
	 public void paint(Graphics g) {
		super.paint(g);
	 }
  }

  private class PanelGlass
		extends JComponent {

	 public void paintComponent(Graphics g) {
		if (gestorMR != null) {
		  Color auxColor = g.getColor();
		  Font auxFont = g.getFont();

		  Image img = new ImageIcon(DialogoSincronizacion.class.getResource("../../Resources/puntero.gif")).
				getImage();
		  Point posTexto = new Point();
		  g.setColor(Color.red);
		  Font newFont = new Font(auxFont.getName(), Font.BOLD, auxFont.getSize());
		  g.setFont(newFont);
		  Vector mouses = gestorMR.obtenerMousesRemotos();
		  MouseRemoto mr = null;
		  //System.out.print("Pintado: ");
		  for (int i = 0; i < mouses.size(); i++) {
			 mr = (MouseRemoto) mouses.elementAt(i);
			 g.drawImage(img, mr.posicion.x, mr.posicion.y, null);
			 posTexto.x = mr.posicion.x + 15;
			 posTexto.y = mr.posicion.y + 20;
			 g.drawString(mr.usuario, posTexto.x, posTexto.y);
			 //System.out.print(mr.usuario + " ");
		  }
		  //System.out.println("");

		  g.setColor(auxColor);
		  g.setFont(auxFont);
		}
	 }
  }

  private class DEventQueue
		extends EventQueue {
	 JFrame frame = null;
	 public DEventQueue(JFrame frame) {
		this.frame = frame;
	 }

	 protected void dispatchEvent(AWTEvent e) {
		if (e instanceof MouseEvent) {
		  if (e.getID() == MouseEvent.MOUSE_MOVED ||
				e.getID() == MouseEvent.MOUSE_DRAGGED) {
			 MouseEvent evento = (MouseEvent) e;
			 Point p = SwingUtilities.convertPoint( (Component) e.getSource(),
																evento.getPoint(),
																frame.getGlassPane());
			 if (p.x >= 0 && p.y >= 0) {
				posMouse.x = p.x;
				posMouse.y = p.y;
			 }
		  }
		}
		super.dispatchEvent(e);
	 }
  }

// ****************************************************************************
// **************** CLASES DE APOYO *******************************************
// ****************************************************************************

  private class MouseRemoto {
	 public Point posicion = new Point();
	 int contador = -1;
	 int timeout = -1;
	 String usuario = null;

	 MouseRemoto(String usuario, int contador, Point posicion) {
		this.usuario = new String(usuario);
		this.contador = contador;
		this.timeout = contador;
		this.posicion.x = posicion.x;
		this.posicion.y = posicion.y;
	 }

	 void decrementarContador() {
		contador--;
	 }

	 void reiniciarContador() {
		contador = timeout;
	 }

	 boolean contadorAgotado() {
		return (contador <= 0);
	 }

  }

  private class ConjuntoMousesRemotos {
	 private Vector v = new Vector();

	 public synchronized void nuevoMouse(String usuario, Point posicion) {
		MouseRemoto mr = new MouseRemoto(usuario, 2, posicion);
		v.add(mr);
	 }

	 public synchronized void eliminarUsuario(String usuario) {
		MouseRemoto mr = null;
		int pos = buscar(usuario);
		if (pos >= 0) { // El usuario existe
		  v.removeElementAt(pos);
		}
	 }

	 public synchronized void decrementarContadores() {
		MouseRemoto mr = null;
		for (int i = (v.size() - 1); i >= 0; i--) {
		  mr = (MouseRemoto) v.elementAt(i);
		  mr.decrementarContador();
		  if (mr.contadorAgotado()) {
			 v.removeElementAt(i);
		  }
		}
	 }

	 public synchronized void actualizarMouse(String usuario, Point posicion) {
		MouseRemoto mr = null;
		int pos = buscar(usuario);
		if (pos >= 0) { // El usuario existe
		  mr = (MouseRemoto) v.elementAt(pos);
		  mr.posicion.x = posicion.x;
		  mr.posicion.y = posicion.y;
		  mr.reiniciarContador();
		}
	 }

	 public synchronized boolean existe(String usuario) {
		MouseRemoto mr = null;
		boolean existe = false;
		int posicion = buscar(usuario);

		if (posicion >= 0)
		  existe = true;

		return existe;
	 }

	 public synchronized Vector obtenerMousesRemotos() {
		Vector vector = new Vector();
		for (int i = 0; i < v.size(); i++) {
		  vector.add(v.elementAt(i));
		}
		return vector;
	 }

	 private int buscar(String usuario) {
		MouseRemoto mr = null;
		int posicion = -1;
		for (int i = 0; i < v.size(); i++) {
		  mr = (MouseRemoto) v.elementAt(i);
		  if (mr.usuario.equals(usuario)) {
			 posicion = i;
		  }
		}
		return posicion;
	 }
  }

  private class GestorMousesRemotos
		implements DComponente {
	 private Integer DID = null;
	 private String nombre = null;
	 private ColaEventos colaRecepcion = new ColaEventos();
	 private ColaEventos colaEnvio = null;
	 private boolean activo = false;
	 protected DJFrame frame = null;

	 private ConjuntoMousesRemotos conjuntoMouses = new ConjuntoMousesRemotos();
	 private int nivelPermisos = 10;

	 int x = 20;

	 public GestorMousesRemotos(DJFrame frame, String nombre) {
		this.nombre = nombre;
		this.frame = frame;
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

	 public void activar() {}

	 public void desactivar() {}

	 public void iniciarHebraProcesadora() {
		Thread t = new Thread(new HebraProcesadora(this));
		t.start();
	 }

	 public void procesarEvento(DEvent evento) {
		colaRecepcion.nuevoEvento(evento);
	 }

	 public void procesarMetaInformacion(DMIEvent evento) {
		if(evento.tipo.intValue() == DMIEvent.INFO_COMPLETA.intValue()){
		  int permiso = evento.obtenerPermisoComponente(nombre);
		  setNivelPermisos(permiso);
		}
		if(evento.tipo.intValue() == DMIEvent.NOTIFICACION_DESCONEXION_USUARIO.intValue()){
		  conjuntoMouses.eliminarUsuario(evento.usuario);
		  EventQueue.invokeLater(new RepintadorGlassPane(frame));
		}
	 }

	 public void sincronizar() {
		if (nivelPermisos >= 20) {
		  DMouseEvent evento = new DMouseEvent();

		  evento.origen = new Integer(1); // Aplicacion
		  evento.destino = new Integer(0); // Coordinador
		  evento.componente = new Integer(DID.intValue());
		  evento.tipo = new Integer(DMouseEvent.PETICION_INFORMACION.intValue());
		  colaEnvio.nuevoEvento(evento);
		}
	 }

	 public int getNivelPermisos() {
		return nivelPermisos;
	 }

	 public void setNivelPermisos(int nivel) {
		nivelPermisos = nivel;
		if (nivel < 20) {
		  DMouseEvent evento = new DMouseEvent();

		  evento.origen = new Integer(1); // Aplicacion
		  evento.destino = new Integer(0); // Coordinador
		  evento.componente = new Integer(DID.intValue());
		  evento.tipo = new Integer(DMouseEvent.OCULTACION.intValue());
		  colaEnvio.nuevoEvento(evento);
		}
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

	 public Vector obtenerMousesRemotos() {
		Vector v = null;
		if (nivelPermisos >= 10) {
		  v = conjuntoMouses.obtenerMousesRemotos();
		}
		else {
		  v = new Vector();
		}
		return v;
	 }

	 public void nuevoMouse(String usuario, Point posicion) {
		conjuntoMouses.nuevoMouse(usuario, posicion);
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

	 class HebraProcesadora
		  implements Runnable {

		GestorMousesRemotos gestor = null;

		HebraProcesadora(GestorMousesRemotos gestor) {
		  this.gestor = gestor;
		}

		public void run() {
		  DMouseEvent evento = null;
		  Thread t = new Thread(new HebraGestora(gestor));
		  t.start();

		  while (true) {
			 evento = (DMouseEvent) colaRecepcion.extraerEvento();
			 //System.out.println("GestorMousesRemotos: nuevo evento");
			 if (nivelPermisos >= 10) {
				if (evento.tipo.intValue() ==
					 DMouseEvent.PETICION_INFORMACION.intValue()) {

				  DMouseEvent aux = new DMouseEvent();
				  aux.origen = new Integer(1); // Aplicacion
				  aux.destino = new Integer(0); // Coordinador
				  aux.componente = new Integer(DID.intValue());
				  aux.tipo = new Integer(DMouseEvent.CAMBIO_POSICION.intValue());
				  aux.px = new Integer(posMouse.x);
				  aux.py = new Integer(posMouse.y);

				  colaEnvio.nuevoEvento(aux);
				}
				if (evento.tipo.intValue() == DMouseEvent.CAMBIO_POSICION.intValue()) {
				  if (!evento.usuario.equals(DConector.Dusuario)) {
					 /*System.out.println("Actualizado mouse del usuario " +
											  evento.usuario);*/
					 conjuntoMouses.actualizarMouse(evento.usuario,
															  new Point(evento.px.intValue(),
						  evento.py.intValue()));

					 if (!conjuntoMouses.existe(evento.usuario)) {
						//System.out.println("Añadido usuario " + evento.usuario);
						conjuntoMouses.nuevoMouse(evento.usuario,
														  new Point(evento.px.intValue(),
							 evento.py.intValue()));
					 }

					 EventQueue.invokeLater(new Runnable(){
					 public void run(){
						//System.out.println("Evento");
					 }
					 });

					 EventQueue.invokeLater(new RepintadorGlassPane(frame));


				  }
				}
				if (evento.tipo.intValue() == DMouseEvent.OCULTACION.intValue()) {
				  if (!evento.usuario.equals(DConector.Dusuario)) {
					 /*System.out.println("Eliminado usuario " +
											  evento.usuario);*/
					 conjuntoMouses.eliminarUsuario(evento.usuario);

					 EventQueue.invokeLater(new RepintadorGlassPane(frame));

				  }
				}

			 }
		  }
		}
	 }

	 /**
	  * Se encarga de comunicar cada X tiempo nuestra posicion del mouse y eliminar
	  * mouses de usuarios que ya no estan presentes
*/
	 class HebraGestora
		  implements Runnable {
		int tiempoActualizacion = 500; // Milisegundos
		int tiempoPeticion = 30000; // Milisegundos
		int tiempoRestante = 30000; // Milisegundos
		GestorMousesRemotos gestor = null;
		Point ultimaEnviada = new Point();
		int ii = 0;

		HebraGestora(GestorMousesRemotos gestor) {
		  this.gestor = gestor;
		}

		public void run() {
		  while (true) {
			 try {
				Thread.currentThread().sleep(tiempoActualizacion);
				System.out.flush();
				tiempoRestante -= tiempoActualizacion;
				DMouseEvent evento = new DMouseEvent();

				if (nivelPermisos >= 20) {
				  // Solo enviamos la posicion actual si ha cambiado desde el ultimo
// envio
				  if (posMouse.x != ultimaEnviada.x ||
						posMouse.y != ultimaEnviada.y) {
					 evento.origen = new Integer(1); // Aplicacion
					 evento.destino = new Integer(0); // Coordinador
					 evento.componente = new Integer(DID.intValue());
					 evento.tipo = new Integer(DMouseEvent.CAMBIO_POSICION.intValue());
					 evento.px = new Integer(posMouse.x);
					 evento.py = new Integer(posMouse.y);
					 colaEnvio.nuevoEvento(evento);

					 ultimaEnviada.x = posMouse.x;
					 ultimaEnviada.y = posMouse.y;
				  }
				  else {
				  }

				  if (tiempoRestante <= 0) {
					 evento = new DMouseEvent();
					 evento.origen = new Integer(1); // Aplicacion
					 evento.destino = new Integer(0); // Coordinador
					 evento.componente = new Integer(DID.intValue());
					 evento.tipo = new Integer(DMouseEvent.CAMBIO_POSICION.
														intValue());
					 evento.px = new Integer(posMouse.x);
					 evento.py = new Integer(posMouse.y);
					 colaEnvio.nuevoEvento(evento);

					 conjuntoMouses.decrementarContadores();
					 EventQueue.invokeLater(new RepintadorGlassPane(frame));
					 tiempoRestante = tiempoPeticion;
				  }
				}

			 }
			 catch (Exception e) {
				e.printStackTrace();
			 }
		  }
		}
	 }

  }

}
