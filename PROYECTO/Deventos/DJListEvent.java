package Deventos;

import java.util.Vector;

import javax.swing.ImageIcon;

import util.ElementoLista;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DJListEvent
	 extends DEvent {
  public static final Integer SINCRONIZACION = new Integer(0);
  public static final Integer RESPUESTA_SINCRONIZACION = new Integer(1);
  public static final Integer CAMBIO_POSICION = new Integer(2);
  public static final Integer ANIADIR_ELEMENTO = new Integer(3);
  public static final Integer ANIADIR_ELEMENTOS = new Integer(4);
  public static final Integer ELIMINAR_ELEMENTO = new Integer(5);
  public static final Integer ELIMINAR_ELEMENTO_POSICION = new Integer(6);

  public Integer posicion = null;
  public String elemento = null;
  public ImageIcon imagen = null;
  public Vector elementos = null;
  public ElementoLista ellista = null;

  public ImageIcon[] imagenes = null;
  public String[] textos = null;

  public DJListEvent() {
  }

  public DJListEvent(int nuevaPosicion) {
	 posicion = new Integer(nuevaPosicion);
  }
}
