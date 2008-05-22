package util;

import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Lista extends JList {

private static final long serialVersionUID=1L;
private DefaultListModel modelo = new DefaultListModel();

  public Lista() {
extrasConstructor();
  }

  public Lista(Object[] p0) {
    super(p0);
	 extrasConstructor();
  }

  public Lista(Vector p0) {
    super(p0);
	 extrasConstructor();
  }

  public Lista(ListModel p0) {
    super(p0);
	 extrasConstructor();
  }

  private void extrasConstructor(){
	 this.setModel(modelo);
	 this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
}

  public void aniadir(String elemento){
	 modelo.addElement(elemento);
}
  public void eliminar(String elemento){
	 modelo.removeElement(elemento);
}
}
