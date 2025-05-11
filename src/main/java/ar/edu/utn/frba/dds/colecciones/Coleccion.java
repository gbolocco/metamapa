package ar.edu.utn.frba.dds.colecciones;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.TipoCombinacion;
import ar.edu.utn.frba.dds.lectores.LectorCsv;
import ar.edu.utn.frba.dds.hecho.Hecho;
import ar.edu.utn.frba.dds.validaciones.Validacion;
import java.util.ArrayList;
import java.util.List;

public class Coleccion {
  private String titulo;
  private String descripcion;
  private List<Filtro> criteriosDePertenencia;
  private String rutaArchivo;
  private List<Hecho> hechos;

  public Coleccion(
      String titulo,
      String descripcion,
      List<Filtro> criteriosDePertenencia,
      String rutaArchivo
  ) {
    Validacion.validarStringNoVacio(titulo, "título");
    Validacion.validarNoNulo(descripcion, "descripción");
    Validacion.validarListaNoNulaNiConElementosNulos(
        criteriosDePertenencia,
        "criteriosDePertenencia"
    );

    this.titulo = titulo;
    this.descripcion = descripcion;
    this.criteriosDePertenencia = new ArrayList<>(criteriosDePertenencia);
    this.rutaArchivo = rutaArchivo;
    this.hechos = new ArrayList<>();
    this.cargarHechosDesdeFuente();
    this.cargarColeccion();
  }
  //getters

  public String nombre() {
    return this.titulo;
  }

  public String descripcion() {
    return this.descripcion;
  }

  public List<Hecho> getHechos() {
    return new ArrayList<>(this.hechos);
  }
  public void cargarColeccion(){
    ColeccionesRepository.getInstancia().agregarColeccion(this);
  }
  //metodos relacionados a los hechos
  public void cargarHechosDesdeFuente() {
    LectorCsv lector = LectorCsv.getInstancia();
    lector.leer(this.rutaArchivo).forEach(hecho -> {
      if (this.aplicarFiltrosHecho(criteriosDePertenencia, hecho, TipoCombinacion.AND)) {
        this.hechos.add(hecho);
      }
    });
  }

  public void visualizarHechos(List<Filtro> filtros, TipoCombinacion tipo) {

    System.out.println("\n=== HECHOS CARGADOS ===");
    if (filtros != null) {
      this.imprimirHechosSegunFiltros(filtros, tipo );
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

  public void imprimirHechosSegunFiltros(List<Filtro> filtros, TipoCombinacion tipo) {

    List<Hecho> hechosFiltrados = this.hechos.stream()
        .filter(hecho -> this.aplicarFiltrosHecho(filtros, hecho, tipo) && !hecho.fueEliminado())
        .toList();

    hechosFiltrados.forEach(hecho -> {
      hecho.imprimirHecho();
      System.out.println();
    });

    System.out.println("Total de hechos: " + hechosFiltrados.size());
  }

  private boolean aplicarFiltrosHecho(List<Filtro> filtros, Hecho hecho, TipoCombinacion tipo) {
    if (tipo == TipoCombinacion.AND) {
      return filtros.stream().allMatch(f -> f.cumpleFiltro(hecho));
    } else {
      return filtros.stream().anyMatch(f -> f.cumpleFiltro(hecho));
    }
  }
}
