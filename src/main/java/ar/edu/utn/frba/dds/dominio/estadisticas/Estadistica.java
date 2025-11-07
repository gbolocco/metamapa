package ar.edu.utn.frba.dds.dominio.estadisticas;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public abstract class Estadistica {

  public boolean publica;
  @Getter
  public String categoria;
  @Getter
  public String respuesta = null;
  @Getter
  public LocalDateTime fechaDeCalculo;
  
  public Estadistica(boolean publica) {
    this.publica = publica;
  }

    public abstract String calcular(List<Hecho> hechos);

  public boolean fueCalculada() {
      return  this.respuesta != null;
  }

  public boolean getPublica() {
      return this.publica;
  }

}
