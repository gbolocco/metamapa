package ar.edu.utn.frba.dds.dominio.hechos;

import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Ubicacion {

  @Id
  @GeneratedValue
  private Double latitud;

  private Double longitud;

  public Ubicacion(Double latitud, Double longitud) {
    Validacion.validarCoordenadas(latitud, longitud);
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
