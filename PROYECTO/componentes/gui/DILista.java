package componentes.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

import util.ElementoLista;
import Deventos.DEvent;
import Deventos.DJListEvent;
import Deventos.enlaceJS.DConector;

import componentes.base.DComponente;
import componentes.base.DComponenteBase;
import componentes.base.DJList;
import componentes.base.HebraProcesadoraBase;
import componentes.listeners.DJListListener;
import componentes.listeners.LJListListener;

/**
 * Lista de elementos distribuida. Consultar documentacion del proyecto para ver
 * su funcionamiento.
 */

public class DILista extends DComponenteBase implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3347591559680463259L;

	private BorderLayout borderLayout1 = new BorderLayout();

	private JScrollPane jScrollPane1 = new JScrollPane();

	private DJList lista = new DJList();

	private boolean estatica = false;

	/**
	 * @param nombre
	 *            String Nombre del componente.
	 * @param conexionDC
	 *            boolean TRUE si esta en contacto directo con el DConector (no
	 *            es hijo de ningun otro componente) y FALSE en otro caso
	 * @param padre
	 *            DComponenteBase Componente padre de este componente. Si no
	 *            tiene padre establecer a null
	 */
	public DILista( String nombre, boolean conexionDC, DComponenteBase padre )
	{
		super(nombre, conexionDC, padre);

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public DILista( String nombre, boolean estatica, boolean conexionDC,
			DComponenteBase padre )
	{
		super(nombre, conexionDC, padre);
		this.estatica = estatica;
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public DILista( String[] elementos, String nombre, boolean conexionDC,
			DComponenteBase padre )
	{
		this(nombre, conexionDC, padre);
		for (String element : elementos)
			lista.aniadirElemento(element);
	}

	public DILista( String[] elementos, boolean estatica, String nombre,
			boolean conexionDC, DComponenteBase padre )
	{
		this(nombre, conexionDC, padre);
		this.estatica = estatica;
		for (String element : elementos)
			lista.aniadirElemento(element);
	}

	private void jbInit() throws Exception
	{
		this.setLayout(borderLayout1);
		this.add(jScrollPane1, BorderLayout.CENTER);
		jScrollPane1.getViewport().add(lista, null);
		lista.addDJListListener(crearDJListener());
		// lista.aniadirElemento("elemento1");
		// lista.aniadirElemento("elemento2");
		// desactivar();//*******************************************************************************
	}

	/**
	 * Devuelve la instancia de la clase captadora que está usando.
	 * 
	 * @return DJList Clase captadora
	 */
	public DJList obtenerJComponente()
	{
		return lista;
	}

	// ******************** METODOS DE TRABAJO CON LA LISTA
	// *************************
	public void aniadirElemento(String texto)
	{
		if (!estatica)
		{
			DJListEvent evento = new DJListEvent();
			evento.tipo = new Integer(DJListEvent.ANIADIR_ELEMENTO.intValue());
			evento.elemento = new String(texto);
			enviarEvento(evento);
		}
		else lista.aniadirElemento(texto);
	}

	public void aniadirElemento(String texto, int posicion)
	{
		if (!estatica)
		{
			DJListEvent evento = new DJListEvent();
			evento.tipo = new Integer(DJListEvent.ANIADIR_ELEMENTO.intValue());
			evento.elemento = new String(texto);
			evento.posicion = new Integer(posicion);
			enviarEvento(evento);
		}
		else lista.aniadirElemento(texto, posicion);
	}

	public void aniadirElemento(ImageIcon imagen, String texto)
	{
		if (!estatica)
		{
			DJListEvent evento = new DJListEvent();
			evento.tipo = new Integer(DJListEvent.ANIADIR_ELEMENTO.intValue());
			evento.elemento = new String(texto); // kitar
			evento.imagen = imagen; // kitar

			evento.ellista = new ElementoLista(imagen, texto);
			enviarEvento(evento);
		}
		else lista.aniadirElemento(imagen, texto);
	}

	public void aniadirElemento(ImageIcon imagen, String texto, int posicion)
	{
		if (!estatica)
		{
			DJListEvent evento = new DJListEvent();
			evento.tipo = new Integer(DJListEvent.ANIADIR_ELEMENTO.intValue());
			evento.elemento = new String(texto);
			evento.imagen = imagen;
			evento.posicion = new Integer(posicion);
			enviarEvento(evento);
		}
		else lista.aniadirElemento(imagen, texto, posicion);
	}

	@SuppressWarnings( "unchecked" )
	public void aniadirElementos(String[] elementos)
	{
		if (!estatica)
		{
			DJListEvent evento = new DJListEvent();
			evento.tipo = new Integer(DJListEvent.ANIADIR_ELEMENTOS.intValue());
			Vector v = new Vector();
			for (String element : elementos)
				v.add(element);
			evento.elementos = v;

			enviarEvento(evento);
		}
		else for (String element : elementos)
			lista.aniadirElemento(element);
	}

	public int obtenerNumElementos()
	{
		return lista.obtenerNumElementos();
	}

	public int obtenerPosicionElemento(String elemento)
	{
		return lista.obtenerPosicionElemento(elemento);
	}

	public int obtenerIndiceSeleccionado()
	{
		return lista.getSelectedIndex();
	}

	public String obtenerElementoSeleccionado()
	{
		return lista.obtenerElementoSeleccionado();
	}

	public void eliminarElemento(String elemento)
	{
		if (!estatica)
		{
			DJListEvent evento = new DJListEvent();
			evento.tipo = new Integer(DJListEvent.ELIMINAR_ELEMENTO.intValue());
			evento.elemento = new String(elemento);
			enviarEvento(evento);
		}
		else lista.eliminarElemento(elemento);
	}

	public void eliminarElemento(int posicion)
	{
		if (!estatica)
		{
			DJListEvent evento = new DJListEvent();
			evento.tipo = new Integer(DJListEvent.ELIMINAR_ELEMENTO_POSICION
					.intValue());
			evento.posicion = new Integer(posicion);
			enviarEvento(evento);
		}
		else lista.eliminarElemento(posicion);
	}

	// *****************************************************************************

	/**
	 * Establece el nivel de permisos de este componente. No se recomienda
	 * llamar a este método desde el programa. Será llamado de forma automática
	 * cuando sea necesario
	 * 
	 * @param nivel
	 *            int Nivel que queremos establecer
	 */
	@Override
	public void setNivelPermisos(int nivel)
	{
		super.setNivelPermisos(nivel);
		if (nivel < 20)
			lista.setForeground(Color.GRAY);
		else lista.setForeground(Color.BLACK);
	}

	public DJListEvent obtenerInfoEstado()
	{
		return lista.obtenerInfoEstado();
	}

	public void procesarInfoEstado(DJListEvent evento)
	{
		ElementoLista el = null;
		lista.eliminarElementos();
		for (int i = 0; i < evento.elementos.size(); i++)
		{
			el = (ElementoLista) evento.elementos.elementAt(i);
			lista.aniadirElemento(el.imagen, el.texto);
		}
		lista.setSelectedIndex(evento.posicion.intValue());
		lista.ensureIndexIsVisible(evento.posicion.intValue());
	}

	/**
	 * Obtiene el numero de componentes hijos de este componente. SIEMPRE
	 * devuelve 0
	 * 
	 * @return int Número de componentes hijos. SIEMPRE devuelve 0.
	 */
	@Override
	public int obtenerNumComponentesHijos()
	{
		return 0;
	}

	/**
	 * Añade un DJListener a la clase captadora para recibir los eventos
	 * generados por la interaccion del usuario con el componente
	 * 
	 * @param listener
	 *            DJListListener Listener a añadir
	 */
	public void addDJListListener(DJListListener listener)
	{
		lista.addDJListListener(listener);
	}

	/**
	 * Añade un LListener al componente para ser notificado cuando cambie el
	 * estado del componente
	 * 
	 * @param listener
	 *            LJListListener Listener a añadir
	 */
	public void addLJListListener(LJListListener listener)
	{
		lista.addLJListListener(listener);
	}

	/**
	 * Añade un LUListener al componente para ser notificado cuando cambie el
	 * estado del componente. Solo sera notificado cuando el cambio de estado se
	 * deba a una accion realizada por el usuario de la aplicacion.
	 * 
	 * @param listener
	 *            LJListListener Listener a añadir
	 */
	public void addLUJListListener(LJListListener listener)
	{
		lista.addLUJListListener(listener);
	}

	/**
	 * Obtiene los DJListener que tiene asociado el componente
	 * 
	 * @return Vector Vector de listeners DJListListener
	 */
	public Vector getDJListListeners()
	{
		return lista.getDJListListeners();
	}

	/**
	 * Obtiene los LJListener que tiene asociado el componente
	 * 
	 * @return Vector Vector de listeners LJListListener
	 */
	public Vector getLJListListeners()
	{
		return lista.getLJListListeners();
	}

	/**
	 * Obtiene los LUJListener que tiene asociado el componente
	 * 
	 * @return Vector Vector de listeners LJListListener
	 */
	public Vector getLUJListListeners()
	{
		return lista.getLUJListListeners();
	}

	/**
	 * Elimina todos los DJListeners que tiene asociado el componente
	 */
	public void removeDJListListeners()
	{
		lista.removeDJListListeners();
	}

	/**
	 * Elimina todos los LJListeners que tiene asociado el componente
	 */
	public void removeLJListListeners()
	{
		lista.removeLJListListeners();
	}

	/**
	 * Elimina todos los LUJListeners que tiene asociado el componente
	 */
	public void removeLUJListListeners()
	{
		lista.removeLUJListListeners();
	}

	/**
	 * Activa el componente. No se recomienda llamar a este metodo desde el
	 * programa. Sera llamado de forma automatica cuando sea necesario
	 */
	@Override
	public void activar()
	{
		lista.activar();
	}

	/**
	 * Desctiva el componente. No se recomienda llamar a este metodo desde el
	 * programa. Sera llamado de forma automatica cuando sea necesario
	 */
	@Override
	public void desactivar()
	{
		lista.desactivar();
	}

	/**
	 * Mediante una llamada a este método se envía un mensaje de peticion de
	 * sincronizacion. No se debe llamar a este método de forma directa. Será
	 * llamado de forma automatica cuando sea necesario realizar la
	 * sincronizacion
	 */
	@Override
	public void sincronizar()
	{
		if (conectadoDC())
		{
			DJListEvent peticion = new DJListEvent();
			peticion.tipo = new Integer(DJListEvent.SINCRONIZACION.intValue());
			enviarEvento(peticion);
		}
	}

	/**
	 * Devuelve una instancia de un listener que se encargara de tratar los
	 * eventos que se reciben desde la clase captadora. Normalmente este
	 * tratamiento se reduce a enviar el evento.
	 * 
	 * @return DJListListener Listener creado
	 */
	public DJListListener crearDJListener()
	{
		return new Listener();
	}

	/**
	 * Devuelve una nueva instancia de la hebra que se encargara de procesar los
	 * eventos que se reciban. Este metodo no debe llamarse de forma directa.
	 * Sera llamado de forma automatica cuando sea necesario.
	 * 
	 * @return HebraProcesadoraBase Nueva hebra procesadora
	 */
	@Override
	public HebraProcesadoraBase crearHebraProcesadora()
	{
		return new HebraProcesadora(this);
	}

	/**
	 * Listener encargado de gestionar los eventos procedentes de la clase
	 * captadora
	 */
	private class Listener implements DJListListener
	{
		public void cambioPosicion(DJListEvent evento)
		{
			enviarEvento(evento);
		}
	}

	/**
	 * Hebra procesadora de eventos. Se encarga de realizar las acciones que
	 * correspondan cuando recibe un evento. Tambén se encarga en su inicio de
	 * sincronizar el componente.
	 */
	class HebraProcesadora extends HebraProcesadoraBase
	{

		HebraProcesadora( DComponente dc )
		{
			super(dc);
		}

		@SuppressWarnings( "unchecked" )
		@Override
		public void run()
		{
			DJListEvent evento = null;
			DJListEvent saux = null;
			DJListEvent respSincr = null;
			Vector vaux = new Vector();

			DEvent[] eventos = obtenerEventosColaRecepcion();
			int numEventos = eventos.length;
			int i = 0;

			// Buscamos si se ha recibido una respuesta de sincronizacion
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DJListEvent) eventos[j];
				if (( respSincr == null )
						&& ( saux.tipo.intValue() == DJListEvent.RESPUESTA_SINCRONIZACION
								.intValue() ))
					respSincr = saux;
				else vaux.add(saux);
			}

			activar();

			if (respSincr != null)
			{ // Se ha recibido respuesta de sincronizacion
				// System.out.println("++++++ Recibida respuesta");
				ultimoProcesado = new Integer(respSincr.ultimoProcesado
						.intValue());
				lista.eliminarElementos();
				ElementoLista el = null;
				for (i = 0; i < respSincr.elementos.size(); i++)
				{
					el = (ElementoLista) respSincr.elementos.elementAt(i);
					lista.aniadirElemento(el.imagen, el.texto);
				}
				lista.setSelectedIndex(respSincr.posicion.intValue());
				lista.ensureIndexIsVisible(respSincr.posicion.intValue());
			}
			else
			{
				// System.out.println("++++++ Sin respuesta");
			}

			// Colocamos en la cola de recepcion los eventos que deben ser
			// procesados (recibidos mientras se realizaba la sincronizacion )
			numEventos = vaux.size();
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DJListEvent) vaux.elementAt(j);
				if (saux.ultimoProcesado.intValue() > ultimoProcesado
						.intValue()) procesarEvento(saux);
			}

			while (true)
			{
				evento = (DJListEvent) leerSiguienteEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());
				if (( evento.tipo.intValue() == DJListEvent.SINCRONIZACION
						.intValue() )
						&& !evento.usuario.equals(DConector.Dusuario))
				{
					DJListEvent infoEstado = obtenerInfoEstado();
					infoEstado.tipo = new Integer(
							DJListEvent.RESPUESTA_SINCRONIZACION.intValue());
					enviarEvento(infoEstado);
				}
				else if (evento.tipo.intValue() == DJListEvent.ELIMINAR_ELEMENTO
						.intValue())
					lista.eliminarElemento(evento.elemento);
				else if (evento.tipo.intValue() == DJListEvent.ELIMINAR_ELEMENTO_POSICION
						.intValue())
					lista.eliminarElemento(evento.posicion.intValue());
				else if (evento.tipo.intValue() == DJListEvent.ANIADIR_ELEMENTO
						.intValue())
				{
					if (evento.imagen == null)
					{ // No hay imagen
						if (evento.posicion == null)
							lista.aniadirElemento(evento.ellista.texto);
						else lista.aniadirElemento(evento.ellista.texto,
								evento.posicion.intValue());
					}
					else if (evento.posicion == null)
						lista.aniadirElemento(evento.ellista.imagen,
								evento.ellista.texto);
					else lista.aniadirElemento(evento.ellista.imagen,
							evento.ellista.texto, evento.posicion.intValue());
				}
				else if (evento.tipo.intValue() == DJListEvent.ANIADIR_ELEMENTOS
						.intValue())
					for (i = 0; i < evento.elementos.size(); i++)
						lista.aniadirElemento((String) evento.elementos
								.elementAt(i));
				else lista.procesarEvento(evento);
			}

		}
	}

}
