package componentes.gui;

import java.util.*;

import java.awt.*;
import javax.swing.*;

import Deventos.*;
import Deventos.enlaceJS.*;
import componentes.*;
import interfaces.*;
import interfaces.listeners.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DITextField
	 extends DComponenteBase {
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  DJTextField campotexto = new DJTextField();
  BorderLayout borderLayout2 = new BorderLayout();

  public DITextField(String nombre, boolean conexionDC, DComponenteBase padre) {
	 super(nombre, conexionDC, padre);

	 try {
		jbInit();
	 }
	 catch (Exception e) {
		e.printStackTrace();
	 }
  }

  private void jbInit() throws Exception {
	 campotexto.setMinimumSize(new Dimension(100, 20));
	 campotexto.setPreferredSize(new Dimension(200, 20));
	 this.setLayout(borderLayout2);
	 // boton.addActionListener(new PanelLista_boton_actionAdapter(this));
	 this.add(campotexto, BorderLayout.NORTH);

	 campotexto.addDJTextFieldListener(crearDJListener());
	 //desactivar();//*******************************************************************************
  }

  /**
	* Devuelve la instancia de la clase captadora que est� usando.
	* @return DJTextField Clase captadora
	*/
  public DJTextField obtenerJComponente() {
	 return campotexto;
  }

  public void setNivelPermisos(int nivel) {
	 super.setNivelPermisos(nivel);
	 if (nivel < 20) {
		campotexto.setForeground(Color.GRAY);
		campotexto.setEditable(false);
	 }
	 else {
		campotexto.setForeground(Color.BLACK);
		campotexto.setEditable(true);
	 }
  }

  public DJTextFieldEvent obtenerInfoEstado() {
	 return campotexto.obtenerInfoEstado();
  }

  public int obtenerNumComponentesHijos() {
	 return 0;
  }

  public void addDJTextFieldListener(DJTextFieldListener listener) {
	 campotexto.addDJTextFieldListener(listener);
  }

  /*public void addLJTextFieldListener(LJTextFieldListener listener) {
	  campotexto.addLJTextFieldListener(listener);
	  }*/

  public void removeDJTextFieldListeners() {
	 campotexto.removeDJTextFieldListeners();
  }

  /*public void removeLJButtonListeners() {
	  campotexto.removeLJButtonListeners();
	  }*/

  public Vector getDJTextFieldListeners() {
	 return campotexto.getDJTextFieldListeners();
  }

  /*public Vector getLJTextFieldListeners() {
	  return campotexto.getLJTextFieldListeners();
	  }*/

  public void activar() {
	 campotexto.activar();
  }

  public void desactivar() {
	 campotexto.desactivar();
  }

  public void sincronizar() {
	 if (conectadoDC()) {
		DJTextFieldEvent peticion = new DJTextFieldEvent();
		peticion.tipo = new Integer(DJTextFieldEvent.SINCRONIZACION.intValue());
		enviarEvento(peticion);
	 }
  }

  public DJTextFieldListener crearDJListener() {
	 return new Listener();
  }

  public HebraProcesadoraBase crearHebraProcesadora() {
	 return new HebraProcesadora(this);
  }

  private class Listener
		implements DJTextFieldListener {
	 public void replace(DJTextFieldEvent evento) {
		enviarEvento(evento);
	 }

	 public void remove(DJTextFieldEvent evento) {
		enviarEvento(evento);
	 }

	 public void insert(DJTextFieldEvent evento) {
		enviarEvento(evento);
	 }
  }

  class HebraProcesadora
		extends HebraProcesadoraBase {

	 HebraProcesadora(DComponente dc) {
		super(dc);
	 }

	 public void run() {
		DJTextFieldEvent evento = null;
		DJTextFieldEvent saux = null;
		DJTextFieldEvent respSincr = null;
		Vector vaux = new Vector();

		DEvent[] eventos = obtenerEventosColaRecepcion();
		int numEventos = eventos.length;
		int i = 0;

		// Buscamos si se ha recibido una respuesta de sincronizacion
		for (int j = 0; j < numEventos; j++) {
		  saux = (DJTextFieldEvent) eventos[j];
		  if ( (respSincr == null) &&
				(saux.tipo.intValue() ==
				 DJTextFieldEvent.RESPUESTA_SINCRONIZACION.intValue())) {
			 respSincr = saux;
		  }
		  else {
			 vaux.add(saux);
		  }
		}

		activar();

		if (respSincr != null) { // Se ha recibido respuesta de sincronizacion
		  ultimoProcesado = new Integer(respSincr.ultimoProcesado.intValue());
		  campotexto.setText(respSincr.contenido);
		}

		// Colocamos en la cola de recepcion los eventos que deben ser
		// procesados (recibidos mientras se realizaba la sincronizacion )
		numEventos = vaux.size();
		for (int j = 0; j < numEventos; j++) {
		  saux = (DJTextFieldEvent) vaux.elementAt(j);
		  if (saux.ultimoProcesado.intValue() > ultimoProcesado.intValue()) {
			 campotexto.procesarEvento(saux);
		  }
		}

		while (true) {
		  evento = (DJTextFieldEvent) leerSiguienteEvento();
		  ultimoProcesado = new Integer(evento.contador.intValue());
		  if (evento.tipo.intValue() == DJTextFieldEvent.SINCRONIZACION.intValue() &&
				!evento.usuario.equals(DConector.Dusuario)) {
			 DJTextFieldEvent infoEstado = obtenerInfoEstado();
			 infoEstado.tipo = new Integer(DJTextFieldEvent.
													 RESPUESTA_SINCRONIZACION.intValue());
			 enviarEvento(infoEstado);
		  }
		  else {
			 campotexto.procesarEvento(evento);
		  }
		}

	 }
  }

}
