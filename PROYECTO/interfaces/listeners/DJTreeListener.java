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

public interface DJTreeListener {
  public void apertura_cierre(DJTreeEvent evento);

  public void seleccion(DJTreeEvent evento);
}
