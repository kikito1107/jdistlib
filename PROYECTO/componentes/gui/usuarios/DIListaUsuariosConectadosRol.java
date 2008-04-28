package componentes.gui.usuarios;

import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import Deventos.*;
import Deventos.enlaceJS.*;
import componentes.*;
import componentes.base.DComponenteBase;
import metainformacion.*;
import util.*;

/**
 * Con este componente podemos ver todos los usuarios que hay conectados que
 * est�n desempe�ando el mismo rol que nosotros
 */

public class DIListaUsuariosConectadosRol
	 extends DComponenteBase {
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  ListaElementos lista = new ListaElementos();
  TitledBorder borde;

  /**
	* @param nombre String Nombre del componente.
	* @param conexionDC boolean TRUE si esta en contacto directo con el DConector
	* (no es hijo de ningun otro componente) y FALSE en otro caso
	* @param padre DComponenteBase Componente padre de este componente. Si no
	* tiene padre establecer a null
	*/
  public DIListaUsuariosConectadosRol(String nombre, boolean conexionDC,
												 DComponenteBase padre) {
	 super(nombre, conexionDC, padre);

	 try {
		jbInit();
	 }
	 catch (Exception e) {
		e.printStackTrace();
	 }
  }

  /**
	* Procesa los eventos de Metainformacion que le llegan
	* @param evento DMIEvent Evento recibido
	*/
  public void procesarMetaInformacion(DMIEvent evento) {
	 super.procesarMetaInformacion(evento);
	 if (evento.tipo.intValue() == DMIEvent.INFO_COMPLETA.intValue()) {
		String rol = evento.infoCompleta.rol;
		borde.setTitle("Usuarios bajo rol " + rol);
		this.obtenerPanelContenido().repaint();
		Vector v = ClienteMetaInformacion.obtenerCMI().obtenerUsuariosBajoRol(rol);
		lista.eliminarElementos();
		
		if (v != null) {
			for (int i = 0; i < v.size(); i++) {
			  lista.aniadirElemento( (String) v.elementAt(i));
			}
		}
	 }
	 if (evento.tipo.intValue() ==
		  DMIEvent.NOTIFICACION_DESCONEXION_USUARIO.intValue()) {
		lista.eliminarElemento(evento.usuario);
	 }
	 if (evento.tipo.intValue() ==
		  DMIEvent.NOTIFICACION_CAMBIO_ROL_USUARIO.intValue()) {
		if (evento.usuario.equals(DConector.Dusuario)) {
		  String rol = evento.infoCompleta.rol;
		  borde.setTitle("Usuarios bajo rol " + rol);
		  this.obtenerPanelContenido().repaint();
		  Vector v = ClienteMetaInformacion.obtenerCMI().obtenerUsuariosBajoRol(
				rol);
		  lista.eliminarElementos();
		  for (int i = 0; i < v.size(); i++) {
			 lista.aniadirElemento( (String) v.elementAt(i));
		  }
		}
		else {
		  if (evento.infoCompleta.rol.equals(DConector.Drol)) {
			 lista.aniadirElemento(evento.usuario);
		  }
		  else if (evento.rolAntiguo.equals(DConector.Drol)) {
			 lista.eliminarElemento(evento.usuario);
		  }

		}
	 }
  }

  private void jbInit() throws Exception {
	 borde = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,
		  new Color(165, 163, 151)), "Usuarios bajo rol:");
	 this.setLayout(borderLayout1);
	 this.obtenerPanelContenido().setBorder(borde);
	 this.add(jScrollPane1, BorderLayout.CENTER);
	 jScrollPane1.getViewport().add(lista, null);
  }

  /**
	* Obtiene el numero de componentes hijos de este componente. SIEMPRE devuelve 0
	* @return int N�mero de componentes hijos. SIEMPRE devuelve 0.
	*/
  public int obtenerNumComponentesHijos() {
	 return 0;
  }

}
