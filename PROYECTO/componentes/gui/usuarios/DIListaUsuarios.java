package componentes.gui.usuarios;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import metainformacion.ClienteMetaInformacion;
import util.ListaElementos;
import Deventos.DMIEvent;

import componentes.base.DComponenteBase;

/**
 * Componente para poder ver todos los usuarios que hay conectados 
 * en cada momento.
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. 
 * 		   Ana Belen Pelegrina Ortiz
 */
public class DIListaUsuarios extends DComponenteBase
{
	private static final long serialVersionUID = 6491526090019528974L;

	private BorderLayout borderLayout1 = new BorderLayout();

	private JScrollPane jScrollPane1 = new JScrollPane();

	private ListaElementos lista = new ListaElementos();

	private TitledBorder borde;

	/**
	 * Constructor
	 * @param nombre
	 *            Nombre del componente.
	 * @param conexionDC
	 *            TRUE si esta en contacto directo con el DConector (no
	 *            es hijo de ningun otro componente) y FALSE en otro caso
	 * @param padre
	 *            Componente padre de este componente. Si no
	 *            tiene padre establecer a null
	 */
	public DIListaUsuarios( String nombre, boolean conexionDC,
			DComponenteBase padre )
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

	/**
	 * Procesa los eventos de Metainformacion que le llegan
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	@Override
	public void procesarMetaInformacion(DMIEvent evento)
	{
		super.procesarMetaInformacion(evento);
		/*
		 * if (evento.tipo.intValue() == DMIEvent.INFO_COMPLETA.intValue()) {
		 * Vector v = (Vector) evento.infoCompleta.usuariosConectados
		 * .elementAt(0); lista.eliminarElementos(); for (int i = 0; i <
		 * v.size(); i++) lista.aniadirElemento((String) v.elementAt(i)); } if
		 * (evento.tipo.intValue() == DMIEvent.NOTIFICACION_CONEXION_USUARIO
		 * .intValue()) lista.aniadirElemento(evento.usuario); if
		 * (evento.tipo.intValue() == DMIEvent.NOTIFICACION_DESCONEXION_USUARIO
		 * .intValue()) lista.eliminarElemento(evento.usuario);
		 */
	}

	/**
	 * Inicializacion de los componentes graficos
	 * @throws Exception
	 */
	@SuppressWarnings( "unchecked" )
	private void jbInit() throws Exception
	{
		borde = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,
				new Color(165, 163, 151)), "Usuarios conectados");
		this.setLayout(borderLayout1);

		Vector v = ClienteMetaInformacion.obtenerCMI().obtenerUsuarios();

		if (v != null) for (int i = 0; i < v.size(); ++i)
		{
			lista.aniadirElemento(v.get(i).toString());
			System.err.println("Agregando usuario " + v.get(i).toString());
		}

		this.obtenerPanelContenido().setBorder(borde);
		this.add(jScrollPane1, BorderLayout.CENTER);
		jScrollPane1.getViewport().add(lista, null);
	}

	/**
	 * Obtiene el numero de componentes hijos de este componente. SIEMPRE
	 * devuelve 0
	 * 
	 * @return Numero de componentes hijos. SIEMPRE devuelve 0.
	 */
	@Override
	public int obtenerNumComponentesHijos()
	{
		return 0;
	}

}
