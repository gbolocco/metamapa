package ar.edu.utn.frba.dds.dominio.hechos;

import ar.edu.utn.frba.dds.compartido.AppLogger;
import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.slf4j.Logger;

public class Hecho {
  private String titulo;
  private String descripcion;
  private String categoria;
  private Ubicacion ubicacion;
  private LocalDate fechaAcontecimiento;
  private LocalDateTime fechaDeCarga;
  private OrigenHecho origenHecho;
  private Boolean eliminado = false;

  private static final Logger logger = AppLogger.getLogger(Hecho.class);

  public Hecho(
      String titulo,
      String descripcion,
      String categoria,
      Ubicacion ubicacion,
      LocalDate fechaAcontecimiento,
      LocalDateTime fechaDeCarga,
      OrigenHecho origenHecho
  ) {
    Validacion.validarStringNoVacio(titulo, "título");
    Validacion.validarNoNulo(ubicacion, "ubicacion");
    Validacion.validarNoNulo(fechaAcontecimiento, "fechaAcontecimiento");
    Validacion.validarNoNulo(fechaDeCarga, "fechaDeCarga");
    Validacion.validarNoNulo(origenHecho, "origenHecho");
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.ubicacion = ubicacion;
    this.fechaAcontecimiento = fechaAcontecimiento;
    this.fechaDeCarga = fechaDeCarga;
    this.origenHecho = origenHecho;
  }

  public void marcarComoEliminado() {
    this.eliminado = true;
  }

  public boolean estaEliminado() {
    return this.eliminado;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public String getCategoria() {
    return categoria;
  }

  public Ubicacion getUbicacion() {
    return ubicacion;
  }

  public LocalDate getFechaAcontecimiento() {
    return fechaAcontecimiento;
  }

  public LocalDateTime getFechaDeCarga() {
    return fechaDeCarga;
  }

  public OrigenHecho getOrigenHecho() {
    return this.origenHecho;
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
}


