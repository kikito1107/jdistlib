package interfaces.listeners;

import Deventos.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface DJComboBoxListener {
  public void abierto();

  public void cerrado();

  public void cambioSeleccionLista(DJComboBoxEvent evento);

  public void seleccion(DJComboBoxEvent evento);
}
