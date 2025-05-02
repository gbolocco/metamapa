package ar.edu.utn.frba.dds;

import java.time.LocalDate;

public class Hecho {
  private String titulo;
  private String descripcion;
  private String categoria;
  private Ubicacion ubicacion;
  private LocalDate fechaAcontecimiento;
  private LocalDate fechaDeCarga;
  private OriginHecho originHecho;
  private Boolean fueEliminado=false;

  public Hecho(String titulo,String descripcion,String categoria,Ubicacion ubicacion,LocalDate fechaAcontecimiento,LocalDate fechaDeCarga,OriginHecho originHecho) {
    this.titulo=titulo;
    this.descripcion=descripcion;
    this.categoria=categoria;
    this.ubicacion=ubicacion;
    this.fechaAcontecimiento=fechaAcontecimiento;
    this.fechaDeCarga=fechaDeCarga;
    this.originHecho=originHecho;
  }

  public void eliminarHecho (){
    this.fueEliminado=true;
  }
  public boolean getFueEliminado(){
    return this.fueEliminado;
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
}


