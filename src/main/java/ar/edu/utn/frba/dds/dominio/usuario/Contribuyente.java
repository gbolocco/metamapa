package ar.edu.utn.frba.dds.dominio.usuario;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Contribuyente {

  private String nombre;
  private Integer edad;

  public Contribuyente(String nombre, Integer edad) {
    if (edad <= 18) {
      throw new IllegalArgumentException("El usuario debe ser mayor de edad");
    }
    this.nombre = nombre;
    this.edad = edad;
  }

  public Hecho crearHecho(
      String titulo,
      String descripcion,
      String categoria,
      Ubicacion ubicacion,
      LocalDate fechaAcontecimiento,
      LocalDateTime fechaDeCarga) {

    OrigenHecho origenContribuyente = OrigenHecho.PROVISTO_POR_CONTRIBUYENTE;
    origenContribuyente.setContribuyenteHecho(this);
    return new Hecho(
        titulo,
        descripcion,
        categoria,
        ubicacion,
        fechaAcontecimiento,
        fechaDeCarga,
        origenContribuyente
    );

  }
}

