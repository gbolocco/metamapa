package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.lectores.Lector;
import java.util.List;

public class FuenteEstatica implements Fuente {
  private final Lector lector;
  private String rutaArchivo;

  public String getRutaArchivo() {
    return rutaArchivo;
  }

  public FuenteEstatica(String rutaArchivo, Lector lector) {
    this.rutaArchivo = rutaArchivo;
    this.lector = lector;
  }

  public List<Hecho> cargarHechos() {

    return lector.leer(rutaArchivo);
  }
}
