package ar.edu.utn.frba.dds.dominio.hechos;

import ar.edu.utn.frba.dds.dominio.usuario.Contribuyente;
import java.time.LocalDateTime;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
    this.categoria = categoria;
    this.ubicacion = ubicacion;
    this.fechaAcontecimiento = fechaAcontecimiento;
    this.estadoRepresentacionHecho = EstadoRepresentacionHecho.PENDIENTE;
    this.hecho = null;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String titulo;
  private String categoria;
  private String descripcion;
  @Embedded
  private Ubicacion ubicacion;
  private LocalDateTime fechaAcontecimiento;
  @Enumerated(EnumType.STRING)
  private EstadoRepresentacionHecho estadoRepresentacionHecho;

  @OneToOne
  private Hecho hecho;
}
