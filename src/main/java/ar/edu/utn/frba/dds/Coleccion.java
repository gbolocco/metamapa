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

  public Coleccion(String titulo, String descripcion, List<Filtro> criteriosDePertenencia, Fuente fuente) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.criteriosDePertenencia = criteriosDePertenencia;
    this.fuente = fuente;
    this.hechos = new ArrayList<>();
  }

  public Coleccion visualizarHechos(List<Filtro> filtros) {
    System.out.println("\n=== HECHOS CARGADOS ===");
    imprimirHechosFiltrados(filtros);
    return null;
  }

  public void cargarHechosDesdeFuente() {//despues lector csv habria que instanciarlo y vendria dado en la fuente el tipo de lector que deberia usarse con una interfaz

    LectorCSV.leerHechosDesdeCSV(this.fuente.getPathArchivo()).forEach(hecho -> {if (this.aplicarFiltrosAUnHecho(criteriosDePertenencia, hecho)) {this.hechos.add(hecho);}});
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
  public String nombre() {
    return this.titulo;
  }
  public List<Hecho> getHechos() {
    return this.hechos;
  }
  public void imprimirHechosFiltrados(List<Filtro> filtros) {
    long total = this.hechos.stream()
        .filter(hecho -> filtros.isEmpty() || this.aplicarFiltrosAUnHecho(filtros, hecho))
        .peek(hecho -> {
          this.imprimirHecho(hecho);
          System.out.println();
        })
        .count();

    System.out.println("Total de hechos: " + total);
  }


  private boolean aplicarFiltrosAUnHecho(List<Filtro> filtros,Hecho hecho){
    return filtros.stream().allMatch(filtro->filtro.cumpleFiltro(hecho)) && ColectionManager.hechosEliminados.stream().noneMatch(eliminado->eliminado.getTitulo().equals(hecho.getTitulo()));
  }
}
