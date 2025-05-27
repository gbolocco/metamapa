package ar.edu.utn.frba.dds.dominio.hechos;

import ar.edu.utn.frba.dds.AppLogger;
import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;
import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import java.time.LocalDate;
import org.slf4j.Logger;

public class Hecho {
  private String titulo;
  private String descripcion;
  private String categoria;
  private Ubicacion ubicacion;
  private LocalDate fechaAcontecimiento;
  private LocalDate fechaDeCarga;
  private OriginHecho originHecho;
  private Boolean eliminado = false;

  private static final Logger logger = AppLogger.getLogger(Hecho.class);

  public Hecho(
      String titulo,
      String descripcion,
      String categoria,
      Ubicacion ubicacion,
      LocalDate fechaAcontecimiento,
      LocalDate fechaDeCarga,
      OriginHecho originHecho
  ) {
    Validacion.validarStringNoVacio(titulo, "título");
    Validacion.validarNoNulo(ubicacion, "ubicacion");
    Validacion.validarNoNulo(fechaAcontecimiento, "fechaAcontecimiento");
    Validacion.validarNoNulo(fechaDeCarga, "fechaDeCarga");
    Validacion.validarNoNulo(originHecho, "originHecho");
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.ubicacion = ubicacion;
    this.fechaAcontecimiento = fechaAcontecimiento;
    this.fechaDeCarga = fechaDeCarga;
    this.originHecho = originHecho;
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

  public LocalDate getFechaDeCarga() {
    return fechaDeCarga;
  }

  public OriginHecho getOriginHecho() {
    return originHecho;
  }

<<<<<<< Updated upstream
  public void imprimirHecho(){
    logger.info("Título: {}", this.titulo);
    logger.info("Descripción: {}", this.descripcion);
    logger.info("Categoría: {}", this.categoria);
    logger.info("Ubicación: {}", this.ubicacion);
    logger.info("Fecha del hecho: {}", this.fechaAcontecimiento);
    logger.info("Fecha de carga: {}", this.fechaDeCarga);
    logger.info("Origen: {}", this.originHecho);
    logger.info("-------------------------------------------");

  }

  /*public void imprimirHecho() {
    System.out.println("[-]  Título: " + this.titulo);
    System.out.println("[-]  Descripción: " + this.descripcion);
    System.out.println("[-]  Categoría: " + this.categoria);
    System.out.println(
        "[-]  Ubicación: " + "Lat "
            + this.ubicacion.getLatitud()
            + ", Lon " + this.ubicacion.getLongitud()
    );
    System.out.println("[-]  Fecha del hecho: " + this.fechaAcontecimiento);
    System.out.println("[-]  Fecha de carga: " + this.fechaDeCarga);
    System.out.println("[-]  Origen: " + this.originHecho);
  }*/
=======
>>>>>>> Stashed changes
}


