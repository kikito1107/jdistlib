package componentes.gui.usuarios;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import util.TablaElementos;
import Deventos.DMIEvent;

import componentes.base.DComponenteBase;

/**
 * Con este componente podemos ver todos los usuarios conectados en cada momento
 * asi como el rol que estan desempeñando.
 */

public class DIListaUsuariosConectadosInfoRol
	 extends DComponenteBase {
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  TitledBorder borde;
  TablaElementos tabla = new TablaElementos(new String[] {"Usuario", "Rol"});

  /**
	* @param nombre String Nombre del componente.
	* @param conexionDC boolean TRUE si esta en contacto directo con el DConector
	* (no es hijo de ningun otro componente) y FALSE en otro caso
	* @param padre DComponenteBase Componente padre de este componente. Si no
	* tiene padre establecer a null
	*/
  public DIListaUsuariosConectadosInfoRol(String nombre, boolean conexionDC,
													  DComponenteBase padre) {
	 super(nombre, conexionDC, padre);

	 try {
		jbInit();
	 }
	 catch (Exception e) {
		e.printStackTrace();
	 }
  }

  private void jbInit() throws Exception {
	 borde = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,
		  new Color(165, 163, 151)), "Usuarios conectados");
	 this.setLayout(borderLayout1);
	 this.obtenerPanelContenido().setBorder(borde);
	 this.add(jScrollPane1, BorderLayout.CENTER);
	 jScrollPane1.getViewport().add(tabla, null);
  }

  /**
	* Procesa los eventos de Metainformacion que le llegan
	* @param evento DMIEvent Evento recibido
	*/
  public void procesarMetaInformacion(DMIEvent evento) {
	 super.procesarMetaInformacion(evento);
	 if (evento.tipo.intValue() == DMIEvent.INFO_COMPLETA.intValue()) {
		Vector v1 = (Vector) evento.infoCompleta.usuariosConectados.elementAt(0);
		Vector v2 = (Vector) evento.infoCompleta.usuariosConectados.elementAt(1);
		int tam = v1.size();
		tabla.vaciar();
		String usuario = null;
		String rol = null;
		for (int i = 0; i < tam; i++) {
		  usuario = (String) v1.elementAt(i);
		  rol = (String) v2.elementAt(i);
		  tabla.aniadir(new String[] {usuario, rol});
		}
	 }
	 if (evento.tipo.intValue() ==
		  DMIEvent.NOTIFICACION_CAMBIO_ROL_USUARIO.intValue()) {
		int pos = buscarUsuario(evento.usuario);
		if (pos != -1) {
		  tabla.eliminarFila(pos);
		  tabla.aniadir(new String[] {evento.usuario, evento.infoCompleta.rol});
		}
	 }
	 if (evento.tipo.intValue() ==
		  DMIEvent.NOTIFICACION_CONEXION_USUARIO.intValue()) {
		tabla.aniadir(new String[] {evento.usuario, evento.rol});
	 }
	 if (evento.tipo.intValue() ==
		  DMIEvent.NOTIFICACION_DESCONEXION_USUARIO.intValue()) {
		int pos = buscarUsuario(evento.usuario);
		if (pos != -1) {
		  tabla.eliminarFila(pos);
		}
	 }
  }

  /**
	* Obtiene el numero de componentes hijos de este componente. SIEMPRE devuelve 0
	* @return int Número de componentes hijos. SIEMPRE devuelve 0.
	*/
  public int obtenerNumComponentesHijos() {
	 return 0;
  }

  private int buscarUsuario(String usuario) {
	 int pos = -1;
	 String[] fila = null;
	 for (int i = 0; i < tabla.numFilas(); i++) {
		fila = tabla.getFila(i);
		if (fila[0].equals(usuario)) {
		  pos = i;
		}
	 }
	 return pos;
  }
}
