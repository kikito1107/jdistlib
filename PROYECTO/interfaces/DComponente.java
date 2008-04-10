package interfaces;

import Deventos.*;
import componentes.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface DComponente {
  ColaEventos colaRecepcion = null;
  ColaEventos colaEnvio = null;

  public void activar();

  public void desactivar();

  public void iniciarHebraProcesadora();

  public void procesarEvento(DEvent evento);

  public void sincronizar();

  public void setNivelPermisos(int nivel);

  public int getNivelPermisos();

  public Integer getID();

  public String getNombre();

  public ColaEventos obtenerColaRecepcion();

  public ColaEventos obtenerColaEnvio();

  public HebraProcesadoraBase crearHebraProcesadora();

  public int obtenerNumComponentesHijos();

  public DComponente obtenerComponente(int num);

  public DComponente obtenerComponente(String nombre);

  public void procesarEventoHijo(DEvent evento);

  public void procesarMetaInformacion(DMIEvent evento);

  public boolean oculto();

  public DComponente obtenerPadre();

  public void padreOcultado();

  public void padreMostrado();

}
