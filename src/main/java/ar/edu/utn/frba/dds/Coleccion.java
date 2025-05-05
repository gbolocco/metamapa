package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.filtros.Filtro;

import java.util.ArrayList;
import java.util.List;

public class Coleccion {
  private String titulo;
  private String descripcion;
  private List<Filtro> criteriosDePertenencia;
  private Fuente fuente;
  private List<Hecho> hechos;

  public Coleccion(String titulo, String descripcion, List<Filtro> criteriosDePertenencia, Fuente fuente,List<Hecho> hechos) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.criteriosDePertenencia = criteriosDePertenencia;
    this.fuente = fuente;
    this.hechos = hechos;
  }

  public void visualizarHechos() {
    System.out.println("\n=== HECHOS CARGADOS ===");
    System.out.println("Total: " + hechos.size() + " hechos\n");
    this.hechos.forEach(hecho-> {
      this.imprimirHecho(hecho);
      System.out.println();
    });
  }

  private void imprimirHecho(Hecho hecho){
  System.out.println("  Título: " + hecho.getTitulo());
    System.out.println("  Descripción: " + hecho.getDescripcion());
    System.out.println("  Categoría: " + hecho.getCategoria());
    System.out.println("  Ubicación: " +
                           "Lat " + hecho.getUbicacion().getLatitud() +
    ", Lon " + hecho.getUbicacion().getLongitud());
    System.out.println("  Fecha del hecho: " + hecho.getFechaAcontecimiento());
    System.out.println("  Fecha de carga: " + hecho.getFechaDeCarga());
    System.out.println("  Origen: " + hecho.getOriginHecho());
    }


  public void imprimirHechosFiltrados(List<Filtro> filtros){
    this.hechos.stream().forEach(hecho -> {
      if(this.aplicarFiltrosAUnHecho(filtros,hecho) ){
        this.imprimirHecho(hecho);
      }});
  }

  private boolean aplicarFiltrosAUnHecho(List<Filtro> filtros,Hecho hecho){
    return filtros.stream().allMatch(filtro->filtro.cumpleFiltro(hecho));
  }
}
