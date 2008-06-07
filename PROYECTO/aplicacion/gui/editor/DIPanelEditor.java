package aplicacion.gui.editor;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import aplicacion.fisica.documentos.Documento;

import componentes.base.DComponente;
import componentes.base.DComponenteBase;
import Deventos.DEvent;
import Deventos.DJLienzoEvent;

/**
 * Componente que une el lienzo de dibujo @see DILienzo, la barra de herramientas @see ControlesDibujo
 * y la barra de seleccion de paginas @see BarraEstado
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class DIPanelEditor extends DComponenteBase
{
	private static final long serialVersionUID = 1L;

	private DILienzo lienzo = null;

	private ControlesDibujo controles = null;

	private BarraEstado barra = null;

	private JScrollPane jsp = null;

	/**
	 * Constructor
	 * 
	 * @param nombre
	 *            Nombre del componente en el servidor de metainformacion
	 * @param conexionDC Indica si deseamos realizar una conexion directa
	 *                   con el @see DConector 
	 * @param padre Ventana padre de este componente
	 */
	public DIPanelEditor( String nombre, boolean conexionDC,
			DComponenteBase padre, FramePanelDibujo papi )
	{
		super(nombre, conexionDC, padre);

		lienzo = new DILienzo("lienzo", true, this);
		lienzo.setPadre(papi);

		barra = new BarraEstado(lienzo);
		controles = new ControlesDibujo(lienzo, barra);

		init();
	}

	/**
	 * Inicializa los componentes graficos
	 */
	public void init()
	{
		this.setLayout(new BorderLayout());

		if (jsp == null)
			jsp = new JScrollPane(lienzo,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		this.add(jsp, BorderLayout.CENTER);

		this.add(controles, BorderLayout.NORTH);
		this.add(barra, BorderLayout.SOUTH);
	}

	/**
	 * Obtiene el path del documento con el que estamos trabajando
	 * @return Path del documento con el que estamos trabajando
	 */
	public String getPathDocumento()
	{
		return lienzo.getDocumento().getPath();
	}

	/**
	 * Asigna el path del documento con el que se esta trabajando
	 * @param pathDocumento Nuevo path para el documento
	 */
	public void setPathDocumento(String pathDocumento)
	{
		lienzo.getDocumento().setPath(pathDocumento);
	}
	
	/**
	 * Obtiene el lienzo en el que pintamos
	 * @return Lienzo en el que pintamos
	 */
	public DILienzo getLienzo()
	{
		return lienzo;
	}

	/**
	 * Obtiene la informaci—n actual y la carga en un evento de lienzo
	 * 
	 * @return Evento con los datos actuales
	 */
	public DJLienzoEvent obtenerInfoEstado()
	{
		DJLienzoEvent de = new DJLienzoEvent();

		return de;
	}

	/**
	 * Asigna el documento de trabajo
	 * @param d Documento de trabajo
	 */
	public void setDocumento(Documento d)
	{
		lienzo.setDocumento(d);

		lienzo.setSize(this.getSize().width
				- jsp.getHorizontalScrollBar().getWidth(),
				this.getSize().height - controles.getHeight()
						- barra.getHeight() - 4);
	}

	@Override
	public int obtenerNumComponentesHijos()
	{
		return 1;
	}

	@Override
	public DComponente obtenerComponente(int i)
	{
		return lienzo;

	}

	@Override
	public synchronized void procesarEventoHijo(DEvent evento)
	{
		try
		{
			if (evento.tipo.intValue() == DJLienzoEvent.NUEVA_ANOTACION)
				enviarEvento(evento);
		}
		catch (Exception e)
		{

		}
	}
	
	@Override
	public void sincronizar()
	{
		if (conectadoDC())
		{
			DJLienzoEvent peticion = new DJLienzoEvent();
			peticion.tipo = new Integer(DJLienzoEvent.SINCRONIZACION.intValue());

			enviarEvento(peticion);
		}
	}
}
