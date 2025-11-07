package ar.edu.utn.frba.dds.dominio.estadisticas;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;

public abstract class Estadistica {

  private boolean publica;

  public String respuesta = null;
  
  public Estadistica(boolean publica) {
    this.publica = publica;
  }

  public String getRespuesta() {
      return respuesta;
  }

  public abstract String calcular(List<Hecho> hechos);

}
