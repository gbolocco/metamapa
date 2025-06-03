package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;

public abstract class FuenteProxy {
  private String url;

  public FuenteProxy(String url) {
    this.url = url;
  }

  public abstract List<Hecho> cargarHechos();
}
