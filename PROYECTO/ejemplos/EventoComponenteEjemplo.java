package ejemplos;

import Deventos.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

/*public class EventoComponenteEjemplo extends DEvent {
  public static final Integer CAMBIO_ELEMENTO = new Integer(1);

  String elemento = null;
  String elmento = null;
  Integer abc = null;
 }*/

public class EventoComponenteEjemplo
	 extends DEvent {
  public static final Integer CAMBIO_ELEMENTO = new Integer(2);
  public static final Integer EVENTO_LISTA = new Integer(3);
  public static final Integer EVENTO_LISTA_IZDA = new Integer(4);
  public static final Integer EVENTO_LISTA_DCHA = new Integer(5);
  public static final Integer EVENTO_BOTON = new Integer(6);
  public static final Integer ELIMINAR_ELEMENTO_LISTA_DCHA = new Integer(7);
  public static final Integer SINCRONIZACION = new Integer(8);
  public static final Integer RESPUESTA_SINCRONIZACION = new Integer(9);
  public static final Integer EVENTO_ARBOL = new Integer(10);

  public String elemento = null;
  public DJListEvent infoEstadoLista = null;
  public DJButtonEvent infoEstadoBoton = null;
  public DJListEvent infoEstadoListaIzda = null;
  public DJListEvent infoEstadoListaDcha = null;
}
