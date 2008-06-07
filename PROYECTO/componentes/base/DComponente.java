package componentes.base;

import Deventos.ColaEventos;
import Deventos.DEvent;
import Deventos.DMIEvent;

/**
 * Interfaz que debe implementar cualquier componente de la plataforma.
 * 
 * @author Juan Antonio Iba–ez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public interface DComponente
{
	ColaEventos colaRecepcion = null;

	ColaEventos colaEnvio = null;

	/**
	 * Habilitacion del componente
	 */
	public void activar();

	/**
	 * Deshabilitacion del componente
	 */
	public void desactivar();

	/**
	 * Inicio de la hebra de procesamiento de eventos
	 */
	public void iniciarHebraProcesadora();

	/**
	 * Procesamiento de los eventos
	 * 
	 * @param evento
	 *            Evento a procesar
	 */
	public void procesarEvento(DEvent evento);

	/**
	 * Sincronizacion con otros componentes de la plataforma
	 */
	public void sincronizar();

	/**
	 * Indica el nivel de permisos de un componente y hace los cambios
	 * necesarios en la visualizacion del mismo.
	 * 
	 * @param nivel
	 *            Nivel de permisos
	 */
	public void setNivelPermisos(int nivel);

	/**
	 * Obtiene el nivel del permisos de un componente
	 * 
	 * @return Nivel de permisos
	 */
	public int getNivelPermisos();

	/**
	 * Obtiene el identificador del componente
	 * 
	 * @return Identificador del componente
	 */
	public Integer getID();

	/**
	 * Obtiene el nombre del componente
	 * 
	 * @return Nombre del componente
	 */
	public String getNombre();

	/**
	 * Obtiene la cola de recepcion de eventos del componente implementado
	 * 
	 * @return Cola de eventos
	 */
	public ColaEventos obtenerColaRecepcion();

	/**
	 * Obtiene la cola de envio de eventos al componente implementado
	 * 
	 * @return Cola de eventos
	 */
	public ColaEventos obtenerColaEnvio();

	/**
	 * Crea la hebra de procesamiento de eventos
	 * 
	 * @return Hebra ya creada e inicializada
	 */
	public HebraProcesadoraBase crearHebraProcesadora();

	/**
	 * Obtiene el numero de componentes hijos del componente implementado
	 * 
	 * @return Numero de componentes hijo
	 */
	public int obtenerNumComponentesHijos();

	/**
	 * Obtiene el componente hijo i-esimo del componente implementado
	 * 
	 * @param num
	 *            Numero de componente a obtener
	 * @return Componente hijo correspodiente al numero especificado o null si
	 *         no es un componente valido
	 */
	public DComponente obtenerComponente(int num);

	/**
	 * Obtiene el componente hijo con el nombre especificado del componente
	 * implementado
	 * 
	 * @param nombre
	 *            Nombre dle componente a obtener
	 * @return Componente hijo correspodiente al nombre especificado o null si
	 *         no es un componente valido
	 */
	public DComponente obtenerComponente(String nombre);

	/**
	 * Procesa el evento para un componente hijo
	 * 
	 * @param evento
	 *            Evento a procesar
	 */
	public void procesarEventoHijo(DEvent evento);

	/**
	 * Procesa un evento de metainformacion
	 * 
	 * @param evento
	 *            Evento a procesar
	 */
	public void procesarMetaInformacion(DMIEvent evento);

	/**
	 * Indica si el componente esta oculto o no
	 * 
	 * @return True si el componente esta oculto. False en otro casos
	 */
	public boolean oculto();

	/**
	 * Obtiene el padre del componente implementado
	 * 
	 * @return Padre del componente
	 */
	public DComponente obtenerPadre();

	/**
	 * Hace que el padre del componente se oculte
	 */
	public void padreOcultado();

	/**
	 * Hace que el padre del componente se muestre
	 */
	public void padreMostrado();

}
