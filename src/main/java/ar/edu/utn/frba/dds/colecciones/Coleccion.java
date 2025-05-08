package ar.edu.utn.frba.dds.colecciones;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hecho.Hecho;
import ar.edu.utn.frba.dds.lectores.Fuente;
import ar.edu.utn.frba.dds.lectores.Lector;
import ar.edu.utn.frba.dds.lectores.LectorFactory;
import ar.edu.utn.frba.dds.lectores.TipoArchivo;
import ar.edu.utn.frba.dds.validaciones.Validacion;
import java.util.ArrayList;
import java.util.List;

public class Coleccion {
  private String titulo;
  private String descripcion;
  private List<Filtro> criteriosDePertenencia;
  private Fuente fuente;
  private List<Hecho> hechos;

  public Coleccion(
      String titulo,
      String descripcion,
      List<Filtro> criteriosDePertenencia,
      Fuente fuente
  ) {
    Validacion.validarStringNoVacio(titulo, "título");
    Validacion.validarNoNulo(descripcion, "descripción");
    Validacion.validarListaNoNulaNiConElementosNulos(
        criteriosDePertenencia,
        "criteriosDePertenencia"
    );
    Validacion.validarNoNulo(fuente, "fuente");

    this.titulo = titulo;
    this.descripcion = descripcion;
    this.criteriosDePertenencia = new ArrayList<>(criteriosDePertenencia);
    this.fuente = fuente;
    this.hechos = new ArrayList<>();
  }
  //geterss

  public String nombre() {
    return this.titulo;
  }

  public String descripcion() {
    return this.descripcion;
  }

  public List<Hecho> getHechos() {
    return new ArrayList<>(this.hechos);
  }

  //metodos relacionados a los hechos

  public void visualizarHechos(List<Filtro> filtros) {

    System.out.println("\n=== HECHOS CARGADOS ===");
    if (filtros != null) {
      this.imprimirHechosSegunFiltros(filtros);
    } else {
      List<Hecho> hechosSinEliminar = this.hechos.stream()
          .filter(hecho -> !hecho.fueEliminado())
          .toList();

      hechosSinEliminar.forEach(hecho -> {
        hecho.imprimirHecho();
        System.out.println();
      });

      System.out.println("Total de hechos: " + hechosSinEliminar.size());
    }
  }

  public void imprimirHechosSegunFiltros(List<Filtro> filtros) {

    List<Hecho> hechosFiltrados = this.hechos.stream()
        .filter(hecho -> this.aplicarFiltrosHecho(filtros, hecho) && !hecho.fueEliminado())
        .toList();

    hechosFiltrados.forEach(hecho -> {
      hecho.imprimirHecho();
      System.out.println();
    });

    System.out.println("Total de hechos: " + hechosFiltrados.size());
  }

  private boolean aplicarFiltrosHecho(List<Filtro> filtros, Hecho hecho) {
    return filtros.stream().allMatch(filtro -> filtro.cumpleFiltro(hecho));
  }

  public void cargarHechosDesdeFuente() {
    String ruta = this.fuente.getPathArchivo();
    TipoArchivo tipo = this.fuente.getTipoArchivo();

    Lector lector = LectorFactory.crearLector(tipo); // Usa la fábrica que vimos antes

    lector.leer(ruta).forEach(hecho -> {
      if (this.aplicarFiltrosHecho(criteriosDePertenencia, hecho)) {
        this.hechos.add(hecho);
      }
    });
  }
}
