package componentes.base;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;

import Deventos.ColaEventos;
import Deventos.DEvent;
import Deventos.DJMenuItemEvent;
import Deventos.DMIEvent;
import Deventos.enlaceJS.DConector;

import componentes.listeners.DJMenuItemListener;
import componentes.listeners.LJMenuItemListener;
import componentes.util.DMenuSelectionManager;

/**
 * Implementacion de la clase captadora de eventos para el componente Elemento
 * de menu
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DJMenuItem extends JMenuItem implements DComponente
{
	private static final long serialVersionUID = 3018709029366109918L;

	private static final String uiClassID = "DMenuMetalMenuItemUI";

	@SuppressWarnings( "unchecked" )
	private Vector djmenuitemlisteners = new Vector(5);

	@SuppressWarnings( "unchecked" )
	private Vector ljmenuitemlisteners = new Vector(5);

	@SuppressWarnings( "unchecked" )
	private Vector lujmenuitemlisteners = new Vector(5);

	private Integer DID = new Integer(-1);

	private String nombre = null;

	private ColaEventos colaRecepcion = new ColaEventos();

	private ColaEventos colaEnvio = null;

	private Integer ultimoProcesado = new Integer(-1);

	private int nivelPermisos = 10;

	// *****************************************************************************************************************
	// CONTROLAR QUE SI DESPUES DE HACER UN CLICK SE MUEVE O PULSA OTRO ELEMENTO
	// NO SE PROCESE
	// POR EJEMPLO VIENDO SI EL MENU ESTA EN POSESION DE ALGUIEN
	public DJMenuItem( String nombre )
	{
		this.nombre = nombre;
		extrasConstructor();
	}

	public DJMenuItem( String p0, String nombre )
	{
		super(p0);
		this.nombre = nombre;
		extrasConstructor();
	}

	public DJMenuItem( String p0, int p1, String nombre )
	{
		super(p0, p1);
		this.nombre = nombre;
		extrasConstructor();
	}

	public DJMenuItem( Action p0, String nombre )
	{
		super(p0);
		this.nombre = nombre;
		extrasConstructor();
	}

	public DJMenuItem( Icon p0, String nombre )
	{
		super(p0);
		this.nombre = nombre;
		extrasConstructor();
	}

	public DJMenuItem( String p0, Icon p1, String nombre )
	{
		super(p0, p1);
		this.nombre = nombre;
		extrasConstructor();
	}

	private void extrasConstructor()
	{
		addDJMenuItemListener(new Listener());
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

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		// System.out.println("Pintado menuitem");
	}

	@Override
	public String getUIClassID()
	{
		return uiClassID;
	}

	@SuppressWarnings( "unchecked" )
	public void addDJMenuItemListener(DJMenuItemListener listener)
	{
		djmenuitemlisteners.add(listener);
	}

	@SuppressWarnings( "unchecked" )
	public void addLJMenuItemListener(LJMenuItemListener listener)
	{
		ljmenuitemlisteners.add(listener);
	}

	@SuppressWarnings( "unchecked" )
	public void addLUJMenuItemListener(LJMenuItemListener listener)
	{
		lujmenuitemlisteners.add(listener);
	}

	@SuppressWarnings( "unchecked" )
	public Vector getDJMenuItemListeners()
	{
		return djmenuitemlisteners;
	}

	@SuppressWarnings( "unchecked" )
	public Vector getLJMenuItemListeners()
	{
		return ljmenuitemlisteners;
	}

	@SuppressWarnings( "unchecked" )
	public Vector getLUJMenuItemListeners()
	{
		return lujmenuitemlisteners;
	}

	public void removeDJMenuItemListeners()
	{
		djmenuitemlisteners.removeAllElements();
	}

	public void removeLJMenuItemListeners()
	{
		ljmenuitemlisteners.removeAllElements();
	}

	public void removeLUJMenuItemListeners()
	{
		lujmenuitemlisteners.removeAllElements();
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

	public void setNivelPermisos(int nivel)
	{
		if (!isEnabled()) setEnabled(true);
		nivelPermisos = nivel;
		if (nivel >= 20)
			setForeground(Color.BLACK);
		else setForeground(Color.GRAY);
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
	private class Listener implements DJMenuItemListener
	{

		public void cambioEstado(DJMenuItemEvent evento)
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
					evento.tipo = new Integer(DJMenuItemEvent.CAMBIO_ESTADO
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

		DJMenuItem menuitem = null;

		HebraProcesadora( ColaEventos cola, DJMenuItem menuitem )
		{
			this.cola = cola;
			this.menuitem = menuitem;
		}

		@SuppressWarnings( "unchecked" )
		public void run()
		{
			DJMenuItemEvent evento = null;

			// setEnabled(true);

			while (true)
			{
				evento = (DJMenuItemEvent) cola.extraerEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());
				if (nivelPermisos >= 10)
					/*
					 * System.out.println("HebraProcesadora(" + DID + "):
					 * Procesado: Tipo=" + evento.tipo + " Ult. Proc.=" +
					 * evento.ultimoProcesado);
					 */
					if (evento.tipo.intValue() == DJMenuItemEvent.CAMBIO_ESTADO
							.intValue())
						if (( evento.path.size() > 0 )
								&& ( DJMenuBar.usuario.getText().length() > 0 ))
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
							// Repintamos los punteros remotos para que queden
							// por encima del menu
							DJFrame.frame.getGlassPane().repaint();
						}
						else if (evento.path.size() == 0)
						{
							DMenuSelectionManager.defaultManager()
									.clearSelectedPath();
							DJMenuBar.propietarioSeleccion = null;
							DJMenuBar.usuario.setText("");
							doClick(0);
							// Repintamos los punteros remotos para que queden
							// por encima del menu
							DJFrame.frame.getGlassPane().repaint();

							Vector v = getLJMenuItemListeners();
							for (int i = 0; i < v.size(); i++)
								( (LJMenuItemListener) v.elementAt(i) )
										.dispararAccion();

							if (evento.usuario.equals(DConector.Dusuario))
							{
								v = getLUJMenuItemListeners();
								for (int i = 0; i < v.size(); i++)
									( (LJMenuItemListener) v.elementAt(i) )
											.dispararAccion();
							}
						}
			}
		}
	}

}
