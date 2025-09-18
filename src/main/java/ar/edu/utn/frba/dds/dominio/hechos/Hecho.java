package ar.edu.utn.frba.dds.dominio.hechos;

import ar.edu.utn.frba.dds.compartido.AppLogger;
import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;
import ar.edu.utn.frba.dds.dominio.usuario.Contribuyente;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Getter
@Setter
@Entity
public class Hecho {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false, name = "hecho_id")
  private Long id;

  private String titulo;
  private String descripcion;
  private String categoria;

  @Embedded
  private Ubicacion ubicacion;


  @Column(columnDefinition = "DATE")
  private LocalDateTime fechaAcontecimiento;

  @Column(columnDefinition = "DATE")
  private LocalDateTime fechaDeCarga;

  @Enumerated(EnumType.STRING)
  private OrigenHecho origenHecho;


  @Setter
  @Enumerated(EnumType.STRING)
  private EstadoHecho estadoHecho;
  private static final Logger logger = AppLogger.getLogger(Hecho.class);

  @ManyToOne
  private Contribuyente contribuyente;

  public Hecho() {

  }


  public Hecho(
      String titulo,
      String descripcion,
      String categoria,
      Ubicacion ubicacion,
      LocalDateTime fechaAcontecimiento,
      LocalDateTime fechaDeCarga,
      OrigenHecho origenHecho
  ) {
    /*
    Validacion.validarStringNoVacio(titulo, "título");
    Validacion.validarNoNulo(ubicacion, "ubicacion");
    Validacion.validarNoNulo(fechaAcontecimiento, "fechaAcontecimiento");
    Validacion.validarNoNulo(fechaDeCarga, "fechaDeCarga");
    Validacion.validarNoNulo(origenHecho, "origenHecho");*/
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.ubicacion = ubicacion;
    this.fechaAcontecimiento = fechaAcontecimiento;
    this.fechaDeCarga = fechaDeCarga;
    this.origenHecho = Objects.requireNonNull(origenHecho, "origenHecho no puede ser nulo");
    this.estadoHecho = EstadoHecho.VISUALIZABLE;
    this.contribuyente = null;
  }

  public OrigenHecho getOrigenHecho() {
    return OrigenHecho.valueOf(this.origenHecho.name());
  }

  public Map<String, Object> getAtributosClave() {
    Map<String, Object> resumen = new HashMap<>();
    resumen.put("descripcion", this.descripcion);
    resumen.put("categoria", this.categoria);
    resumen.put("ubicacion", this.ubicacion);
    resumen.put("fechaAcontecimiento", this.fechaAcontecimiento);
    resumen.put("fechaDeCarga", this.fechaDeCarga);
    resumen.put("origenHecho", this.origenHecho);
    return resumen;
  }

  public void imprimirHecho() {
    logger.info("Título: {}", this.titulo);
    logger.info("Descripción: {}", this.descripcion);
    logger.info("Categoría: {}", this.categoria);
    logger.info("Ubicación: {}", this.ubicacion);
    logger.info("Fecha del hecho: {}", this.fechaAcontecimiento);
    logger.info("Fecha de carga: {}", this.fechaDeCarga);
    logger.info("Origen: {}", this.origenHecho);
    logger.info("-------------------------------------------");

  }

  /*
  public Long getId() {
    return id;
  }
*/
}


