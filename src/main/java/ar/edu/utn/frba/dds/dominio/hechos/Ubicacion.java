package ar.edu.utn.frba.dds.dominio.hechos;

import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Ubicacion {

  private Double latitud;
  private Double longitud;

  public Ubicacion(Double latitud, Double longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }

  public Ubicacion() {
  }

  public Double getLatitud() {
    return latitud;
  }

  public Double getLongitud() {
    return longitud;
  }
}
