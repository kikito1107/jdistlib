package componentes.gui.imagen;


import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import Deventos.DEvent;
import Deventos.DJLienzoEvent;
import aplicacion.fisica.documentos.Documento;

import componentes.base.DComponente;
import componentes.base.DComponenteBase;

public class DIPanelDibujo extends DComponenteBase
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DILienzo lienzo = null;
	private ControlesDibujo controles = null;
	private BarraEstado barra = null;
	

	private JFrame padre = null;
	private JScrollPane jsp = null;
	private String pathDocumento;
	
	
	public String getPathDocumento()
	{
		return pathDocumento;
	}

	public void setPathDocumento(String pathDocumento)
	{
		this.pathDocumento = pathDocumento;
	}

	public void setPadre(JFrame unaVentana) {
		padre = unaVentana;
	}
	
	/**
	 * Crea un nuevo objeto de la clase DIPanelDibujo
	 * @param nombre nombre del objeto en la BD
	 * @param conexionDC
	 * @param padre
	 */
	public DIPanelDibujo( String nombre, boolean conexionDC,
			DComponenteBase padre )
	{
		super(nombre, conexionDC, padre);
		
		lienzo = new DILienzo("lienzo", true, this);
		lienzo.setPadre(this.padre);
		
		barra = new BarraEstado(lienzo);
		controles = new ControlesDibujo(lienzo, barra);
		controles.setPadre(this.padre);
		
		init();
	}
	
	/**
	 * Inicializa el objeto
	 */
	public void init() {
		this.setLayout(new BorderLayout());
		
		/*JPanel aux = new JPanel( new BorderLayout() );
		aux.setBorder(new EtchedBorder(4));
		aux.add(lienzo, BorderLayout.CENTER);
		*/
		
		//JScrollPane jsp = new JScrollPane(lienzo,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		//jsp.setLayout(new BorderLayout());
		if (jsp==null) jsp = new JScrollPane(lienzo, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		this.add(jsp , 
				BorderLayout.CENTER);
		
		this.add(controles, BorderLayout.NORTH);
		this.add(barra, BorderLayout.SOUTH);
	}
	
	
	public DILienzo getLienzo(){
		return lienzo;
	}
	
	
	
	
	public void sincronizar()
	{
		if (conectadoDC()) {
			DJLienzoEvent peticion = new DJLienzoEvent();
			peticion.tipo = new Integer(DJLienzoEvent.SINCRONIZACION.intValue());

			
			enviarEvento(peticion);
		 }
	}
	
	
	
	/**
	 * Obtiene la informaci—n actual y la carga en un evento de lienzo
	 * @return el evento con los datos actuales
	 */
	public DJLienzoEvent obtenerInfoEstado()
	{
		DJLienzoEvent de = new DJLienzoEvent();
		
		
		return de;
	}

	public int obtenerNumComponentesHijos()
	{
		return 1;
	}
	
	public DComponente obtenerComponente(int i) {
		return lienzo;
		
	}
	
	synchronized public void procesarEventoHijo(DEvent evento) {
		 try {
			if (evento.tipo.intValue() == DJLienzoEvent.NUEVA_ANOTACION) {
			  enviarEvento(evento);
			}
		 }
		 catch (Exception e) {

		 }
	  }

	public void setDocumento(Documento d)
	{
		lienzo.setDocumento(d);
		
		lienzo.setSize(this.getSize().width-jsp.getHorizontalScrollBar().getWidth(), this.getSize().height-controles.getHeight()-barra.getHeight()-4);
	}
}
