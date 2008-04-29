package util;

import javax.swing.JList;
import java.util.Vector;
import javax.swing.*;

import Deventos.enlaceJS.DialogoSincronizacion;

import java.awt.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ListaElementos
	 extends JList {
	
  private static final long serialVersionUID = 1L;
  ImageIcon img = new ImageIcon(DialogoSincronizacion.class.getResource("../../Resources/openFile.png"));
  private Modelo model = null;

  public ListaElementos() {
	 super();
	 extrasConstructor();
  }

  public void extrasConstructor() {
	 this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	 this.setCellRenderer(new CellRenderer());
	 this.model = new Modelo();
	 this.setModel(model);
  }

  public void aniadirElemento(String texto) {
	 model.addElement(new ElementoLista(null, texto));
  }

  public void aniadirElemento(String texto,int posicion) {
	 model.add(posicion,new ElementoLista(null, texto));
  }

  public void aniadirElemento(ImageIcon imagen, String texto) {
	 model.addElement(new ElementoLista(imagen, texto));
	 //model.addElement(new ElementoLista(this.img, texto));
  }

  public void aniadirElemento(ImageIcon imagen, String texto, int posicion) {
	 model.add(posicion,new ElementoLista(imagen, texto));
  }


  public void aniadirElementos(String[] elementos){
	 for(int i=0; i<elementos.length; i++){
		model.addElement(new ElementoLista(null, elementos[i]));
	 }
}

  public int obtenerNumElementos(){
	 return model.getSize();
  }

  public Vector obtenerElementos(){
	 Vector v = new Vector();
	 ElementoLista el = null;
	 for(int i=0; i<obtenerNumElementos(); i++){
		el = (ElementoLista)model.elementAt(i);
		v.add(new ElementoLista(el));
	 }
	 return v;
  }

  public String obtenerElementoSeleccionado(){
	 String elemento = null;
	 ElementoLista el = (ElementoLista)getSelectedValue();
	 if(el != null){
		elemento = el.texto;
	 }
	 return elemento;
}

  public String[] obtenerElementosSeleccionados(){
	 ElementoLista[] el = (ElementoLista[])getSelectedValues();
	 String[] els = new String[el.length];
	 for(int i=0; i<els.length; i++){
		els[i] = el[i].texto;
	 }

	 return els;
}

  public int obtenerPosicionElemento(String elemento) {
	 int pos = -1;
	 try {
		int n = getModel().getSize();
		for (int i = 0; i < n; i++) {
		  ElementoLista el = (ElementoLista) getModel().getElementAt(i);
		  if (el != null) {
			 if (el.texto.equals(elemento)) {
				pos = i;
			 }
		  }
		}
	 }
	 catch (Exception e) {}
	 return pos;
  }

  public void eliminarElementos() {
	 model.removeAllElements();
  }

  public void eliminarElemento(String elemento) {
	 model.removeElement(elemento);
  }

  public void eliminarElemento(int posicion){
	 if(posicion < obtenerNumElementos()){
		model.remove(posicion);
}
}
  private class CellRenderer
		extends JLabel
		implements ListCellRenderer {

	 public CellRenderer() {
		setOpaque(true);
		setHorizontalAlignment(LEFT);
		setVerticalAlignment(CENTER);
	 }

/*
	  * This method finds the image and text corresponding
	  * to the selected value and returns the label, set up
	  * to display the text and image.
*/
	 public Component getListCellRendererComponent(
		  JList list,
		  Object value,
		  int index,
		  boolean isSelected,
		  boolean cellHasFocus) {
		//Get the selected index. (The index param isn't
		//always valid, so just use the value.)
		int selectedIndex = index;

		if (isSelected) {
		  setBackground(list.getSelectionBackground());
		  setForeground(list.getSelectionForeground());
		}
		else {
		  setBackground(list.getBackground());
		  setForeground(list.getForeground());
		}

		//Set the icon and text.  If icon was null, say so.
		ImageIcon icon = ((ElementoLista) value).imagen;


		String pet = ((ElementoLista) value).texto;
		setFont(list.getFont());
		setText(pet);
		  setIcon(icon);

		return this;
	 }
  }

  private class Modelo
		extends DefaultListModel {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean removeElement(Object obj) {
		boolean eliminado = false;
		if (obj instanceof String) {
		  ElementoLista el = new ElementoLista(null, (String) obj);
		  eliminado = super.removeElement(el);
		}
		return eliminado;
	 }

  }


}


