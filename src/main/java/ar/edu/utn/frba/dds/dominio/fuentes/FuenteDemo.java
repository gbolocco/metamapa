package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;

public class FuenteDemo extends FuenteProxy {
  private Conexion conexion;

  public FuenteDemo(String url) {
    super(url);
  }

  @Override
  public List<Hecho> cargarHechos() {
    return List.of();
  }
}
