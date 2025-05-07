package ar.edu.utn.frba.dds.hecho;

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
  public boolean fueEliminado(){
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

  public void imprimirHecho(){
    System.out.println("[-]  Título: " + this.titulo);
    System.out.println("[-]  Descripción: " + this.descripcion);
    System.out.println("[-]  Categoría: " + this.categoria);
    System.out.println("[-]  Ubicación: " +
        "Lat " + this.ubicacion.getLatitud() +
        ", Lon " + this.ubicacion.getLongitud());
    System.out.println("[-]  Fecha del hecho: " + this.fechaAcontecimiento);
    System.out.println("[-]  Fecha de carga: " + this.fechaDeCarga);
    System.out.println("[-]  Origen: " + this.originHecho);
  }
}


