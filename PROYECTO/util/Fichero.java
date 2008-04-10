package util;

import java.io.*;

/**
 * Clase para leer y escribir cadenas (String) en un fichero
 * @author Juan Antonio Ibañez Santorum
 */
public class Fichero {
  private File fichero;
  private RandomAccessFile ficheroRandom;

  /**
   * El modo puede ser "r" o "rw". El primero de ellos es para trabajar
   * en modo solo lectura y el siguiente para trabajar tanto en modo lectura
   * como escritura.
   * @param fichero Fichero sobre el que queremos trabajar
   * @param modo Modo de trabajo. Leer descripcion del constructor.
   * @throws IOException
   */
  public Fichero(String nombre, String modo) throws IOException{
    this.fichero = new File(nombre);
    try{
      if(!fichero.exists()) // Si no existe el fichero lo creamos
        fichero.createNewFile();
    }catch(IOException e){
      throw(e);
    }
    ficheroRandom = new RandomAccessFile(fichero,modo);
  }

  /**
   * Leer una linea del fichero
   * @return La linea o <i>null</i> si se ha llegado al final del fichero.
   * @throws IOException En caso de haber algun error en la lectura
   */
  public String leerLinea() throws IOException{
    String linea = null;
    try{
      linea = ficheroRandom.readLine();
    }catch(IOException e){
      throw(e);
    }
    return linea;
  }

  /**
   * Escribir una linea <b>al final</b> del fichero.
   * @param linea Linea que deseamos escribir
   * @throws IOException En caso de haber algun error en la escritura
   */
  public void escribirLinea(String linea) throws IOException{
    ficheroRandom.seek(ficheroRandom.length());
    try{
      byte[] aux = linea.getBytes();
      //Escribimos linea
      ficheroRandom.write(aux);
      //Escribimos fin de linea
      ficheroRandom.write(13);
      ficheroRandom.write(10);
    }catch(IOException e){
      throw(e);
    }
  }

  public void setPosicion(int pos){
	 try{
		ficheroRandom.seek(0);
	 }catch(Exception e){
		e.printStackTrace();
}
  }

  public void setLongitud(int longitud){
	 try{
		ficheroRandom.setLength(longitud);
	 }catch(Exception e){
		e.printStackTrace();
}
  }

  public void cerrar(){
    try{
      ficheroRandom.close();
    }catch(Exception e){
      System.out.println(e);
    }
  }
}
