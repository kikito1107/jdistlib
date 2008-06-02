package aplicacion.gui.editor;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import aplicacion.fisica.documentos.Documento;

import componentes.base.DComponente;
import componentes.base.DComponenteBase;
import Deventos.DEvent;
import Deventos.DJLienzoEvent;

public class DIPanelEditor extends DComponenteBase
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DILienzo lienzo = null;

	private ControlesDibujo controles = null;

	private BarraEstado barra = null;

	private JScrollPane jsp = null;


	public String getPathDocumento()
	{
		return lienzo.getDocumento().getPath();
	}

	public void setPathDocumento(String pathDocumento)
	{
		lienzo.getDocumento().setPath(  pathDocumento);
	}


	/**
	 * Crea un nuevo objeto de la clase DIPanelDibujo
	 * 
	 * @param nombre
	 *            nombre del objeto en la BD
	 * @param conexionDC
	 * @param padre
	 */
	public DIPanelEditor( String nombre, boolean conexionDC,
			DComponenteBase padre, FramePanelDibujo papi)
	{
		super(nombre, conexionDC, padre);

		lienzo = new DILienzo("lienzo", true, this);
		lienzo.setPadre(papi);

		barra = new BarraEstado(lienzo);
		controles = new ControlesDibujo(lienzo, barra);
		

		init();
	}

	/**
	 * Inicializa el objeto
	 */
	public void init()
	{
		this.setLayout(new BorderLayout());

		/*
		 * JPanel aux = new JPanel( new BorderLayout() ); aux.setBorder(new
		 * EtchedBorder(4)); aux.add(lienzo, BorderLayout.CENTER);
		 */

		if (jsp == null)
			jsp = new JScrollPane(lienzo,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		this.add(jsp, BorderLayout.CENTER);

		this.add(controles, BorderLayout.NORTH);
		this.add(barra, BorderLayout.SOUTH);
	}

	public DILienzo getLienzo()
	{
		return lienzo;
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

	/**
	 * Obtiene la informaci—n actual y la carga en un evento de lienzo
	 * 
	 * @return el evento con los datos actuales
	 */
	public DJLienzoEvent obtenerInfoEstado()
	{
		DJLienzoEvent de = new DJLienzoEvent();

		return de;
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
	synchronized public void procesarEventoHijo(DEvent evento)
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

	public void setDocumento(Documento d)
	{
		lienzo.setDocumento(d);

		lienzo.setSize(this.getSize().width
				- jsp.getHorizontalScrollBar().getWidth(),
				this.getSize().height - controles.getHeight()
						- barra.getHeight() - 4);
	}
}
