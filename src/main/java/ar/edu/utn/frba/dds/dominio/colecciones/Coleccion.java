package ar.edu.utn.frba.dds.dominio.colecciones;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.filtros.TipoCombinacion;
import ar.edu.utn.frba.dds.dominio.lectores.Lector;
import ar.edu.utn.frba.dds.dominio.lectores.LectorCsv;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepositoryMemory;
import java.util.ArrayList;
import java.util.List;

public class Coleccion {
  private final Lector lector;
  private String titulo;
  private String descripcion;
  private List<Filtro> criteriosDePertenencia;
  private String rutaArchivo;
  private List<Hecho> hechos;
  private TipoCombinacion tipoCombinacion;


  public Coleccion(
      String titulo,
      String descripcion,
      List<Filtro> criteriosDePertenencia,
      String rutaArchivo,
      TipoCombinacion tipoCombinacion,
      Lector lector
  ) {
    Validacion.validarStringNoVacio(titulo, "título");
    Validacion.validarNoNulo(descripcion, "descripción"); //descripcion puede ser nula?
    Validacion.validarListaNoNula(
        criteriosDePertenencia,
        "criteriosDePertenencia"
    );

    this.titulo = titulo;
    this.descripcion = descripcion;
    this.criteriosDePertenencia = criteriosDePertenencia;
    this.rutaArchivo = rutaArchivo;
    this.hechos = new ArrayList<>();
    this.tipoCombinacion = tipoCombinacion;
    this.lector = lector;
    this.cargarHechosDesdeFuente();
    this.cargarColeccion();
  }
  //getters

  public String getTitulo() {
    return this.titulo;
  }
  public String getDescripcion() {return this.descripcion;}
  public List<Hecho> mostrarHechos() {
    return new ArrayList<>(this.hechos.stream().filter(hecho -> !hecho.estaEliminado()).toList());
  }

  public void cargarColeccion(){
    ColeccionRepositoryMemory.getInstancia().agregarColeccion(this);
  }

  //metodos relacionados a los hechos

  public void cargarHechosDesdeFuente() {
    List<Hecho> hechosLeidos = lector.leer(this.rutaArchivo);
    hechosLeidos.stream()
        .filter(this::cumpleCriterio)
        .forEach(hechos::add);
  }

  private boolean cumpleCriterio(Hecho hecho) {
    if (this.tipoCombinacion == TipoCombinacion.AND) {
      return criteriosDePertenencia.stream().allMatch(f -> f.cumpleFiltro(hecho));
    } else {
      return criteriosDePertenencia.stream().anyMatch(f -> f.cumpleFiltro(hecho));
    }
  }


  //public void visualizarHechos(List<Filtro> filtros, TipoCombinacion tipo) {
//
//    System.out.println("\n=== HECHOS CARGADOS ===");
//    if (filtros != null) {
//      this.imprimirHechosSegunFiltros(filtros, tipo );
//    } else {
//      List<Hecho> hechosSinEliminar = this.hechos.stream()
//          .filter(hecho -> !hecho.fueEliminado())
//          .toList();
//
//      hechosSinEliminar.forEach(hecho -> {
//        hecho.imprimirHecho();
//        System.out.println();
//      });
//
//      System.out.println("Total de hechos: " + hechosSinEliminar.size());
//    }
//  }
//
//  public void imprimirHechosSegunFiltros(List<Filtro> filtros, TipoCombinacion tipo) {
//
//    List<Hecho> hechosFiltrados = this.hechos.stream()
//        .filter(hecho -> this.cumpleCriterio( hecho) && !hecho.fueEliminado())
//        .toList();
//
//    hechosFiltrados.forEach(hecho -> {
//      hecho.imprimirHecho();
//      System.out.println();
//    });
//
//    System.out.println("Total de hechos: " + hechosFiltrados.size());
//  }
}
