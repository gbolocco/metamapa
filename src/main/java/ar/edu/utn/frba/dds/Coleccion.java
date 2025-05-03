package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.Lectores.Lector;
import ar.edu.utn.frba.dds.Lectores.LectorFactory;
import ar.edu.utn.frba.dds.filtros.Filtro;

import java.util.ArrayList;
import java.util.List;

public class Coleccion {
  private String titulo;
  private String descripcion;
  private List<Filtro> criteriosDePertenencia;
  private Fuente fuente;
  private List<Hecho> hechos;
  //constructor
  public Coleccion(String titulo, String descripcion, List<Filtro> criteriosDePertenencia, Fuente fuente) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.criteriosDePertenencia = criteriosDePertenencia;
    this.fuente = fuente;
    this.hechos = new ArrayList<>();
  }
  //geterss

  public String nombre() {
    return this.titulo;
  }
  public List<Hecho> getHechos() {
    return this.hechos;
  }

  //metodos relacionados a los hechos

  public void visualizarHechos(List<Filtro> filtros) {
    System.out.println("\n=== HECHOS CARGADOS ===");
    imprimirHechosFiltrados(filtros);
  }

  public void cargarHechosDesdeFuente() {
    String ruta = this.fuente.getPathArchivo();
    TipoArchivo tipo = this.fuente.getTipoArchivo();

    Lector lector = LectorFactory.crearLector(tipo); // Usa la fábrica que vimos antes

    lector.leer(ruta).forEach(hecho -> {
      if (this.aplicarFiltrosAUnHecho(criteriosDePertenencia, hecho)) {
        this.hechos.add(hecho);
      }
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
