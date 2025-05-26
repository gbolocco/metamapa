package ar.edu.utn.frba.dds.dominio.lectores.excepciones;

public class ArchivoNoEncontradoException extends RuntimeException {
  public ArchivoNoEncontradoException(String mensaje) {
    super(mensaje);
  }
}
