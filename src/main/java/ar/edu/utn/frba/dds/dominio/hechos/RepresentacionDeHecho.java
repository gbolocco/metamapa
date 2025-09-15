package ar.edu.utn.frba.dds.dominio.hechos;

import java.time.LocalDateTime;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RepresentacionDeHecho {


  public RepresentacionDeHecho() {

  }

  public RepresentacionDeHecho(String titulo,
                               String descripcion,
                               String categoria,
                               Ubicacion ubicacion,
                               LocalDateTime fechaAcontecimiento,
                               LocalDateTime fechaDeCarga,
                               OrigenHecho origenHecho) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria= categoria;
    this.ubicacion = ubicacion;
    this.fechaAcontecimiento = fechaAcontecimiento;
    //this.estadoRepresentacionHecho= EstadoRepresentacionHecho.PENDIENTE;
  }

  @Id
  private Long id;
  public String titulo;
  public String categoria;
  public String descripcion;
  @Embedded
  public Ubicacion ubicacion;
  public LocalDateTime fechaAcontecimiento;
  //public EstadoRepresentacionHecho estadoRepresentacionHecho;


}
