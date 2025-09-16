package ar.edu.utn.frba.dds.dominio.estadisticas;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;

public abstract class Estadistica {

  private boolean publica;

  
  public Estadistica(boolean publica) {
    this.publica = publica;
  }
  

  public abstract String calcular(List<Hecho> hechos);

}
