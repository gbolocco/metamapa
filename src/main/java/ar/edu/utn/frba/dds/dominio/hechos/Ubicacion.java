package ar.edu.utn.frba.dds.dominio.hechos;

import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


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
/*
  public Double getLatitud() {
    return latitud;
  }

  public Double getLongitud() {
    return longitud;
  }*/
}
