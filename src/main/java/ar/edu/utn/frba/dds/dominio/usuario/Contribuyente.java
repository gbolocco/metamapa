package ar.edu.utn.frba.dds.dominio.usuario;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Contribuyente {

  private  String nombre;
  private  Integer edad;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false)
  private Long id;

  public Contribuyente(String nombre, Integer edad) {
    if (edad <= 18) {
      throw new IllegalArgumentException("El usuario debe ser mayor de edad");
    }
    this.nombre = nombre;
    this.edad = edad;
  }

  public Contribuyente() {

  }

  public RepresentacionDeHecho crearHecho(
      String titulo,
      String descripcion,
      String categoria,
      Ubicacion ubicacion,
      LocalDateTime fechaAcontecimiento,
      LocalDateTime fechaDeCarga) {


    return new RepresentacionDeHecho(
        titulo,
        descripcion,
        categoria,
        ubicacion,
        fechaAcontecimiento,
        fechaDeCarga,
        OrigenHecho.PROVISTO_POR_CONTRIBUYENTE,
        this
    );

  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}

