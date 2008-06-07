package componentes.base;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JMenuBar;

import util.DMenuSelectionManager;
import Deventos.ColaEventos;
import Deventos.DEvent;
import Deventos.DMIEvent;
import Deventos.enlaceJS.DConector;

/**
 * Implementacion de la clase captadora de eventos para el componente Barra de
 * Menu
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DJMenuBar extends JMenuBar implements DComponente
{
	private static final long serialVersionUID = 1L;

	public static JLabel usuario = null;

	public static String propietarioSeleccion = null;

	private Integer DID = new Integer(-1);

	private String nombre = null;

	public DJMenuBar( String nombre )
	{
		this.nombre = nombre;
		( (DMenuSelectionManager) DMenuSelectionManager.defaultManager() )
				.inicializar(this);
		DID = new Integer(DConector.alta(this));
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

	public void activar()
	{
		//
	}

	public void desactivar()
	{
		//
	}

	public void iniciarHebraProcesadora()
	{
		//
	}

	public void procesarEvento(DEvent evento)
	{
		//
	}

	public void procesarMetaInformacion(DMIEvent evento)
	{
		if (evento.tipo.intValue() == DMIEvent.NOTIFICACION_DESCONEXION_USUARIO
				.intValue())
			if (DJMenuBar.propietarioSeleccion != null)
				if (DJMenuBar.propietarioSeleccion.equals(evento.usuario))
				{
					DMenuSelectionManager.defaultManager().clearSelectedPath();
					DJMenuBar.propietarioSeleccion = null;
					DJMenuBar.usuario.setText("");
				}
	}

	public void sincronizar()
	{
		usuario = new JLabel("");
		usuario.setForeground(Color.red);
		this.add(usuario);
	}

	public int getNivelPermisos()
	{
		return -1;
	}

	public void setNivelPermisos(int nivel)
	{
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

}
