package ar.edu.utn.frba.dds.colecciones;

import ar.edu.utn.frba.dds.Lectores.Fuente;
import ar.edu.utn.frba.dds.colecciones.excepciones.ColectionException;
import ar.edu.utn.frba.dds.hecho.Hecho;
import ar.edu.utn.frba.dds.Lectores.Lector;
import ar.edu.utn.frba.dds.Lectores.LectorFactory;
import ar.edu.utn.frba.dds.Lectores.TipoArchivo;
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
    //this.validarTitulo(titulo)
    //this.validarDescripcion(descripcion)
    //this.validarCriteriosDePertenencia(criteriosDePertenencia)
    //this.validarFuente(fuente)

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
    if(filtros != null){
      imprimirHechosSegunFiltros(filtros);
    }else{
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
        .filter(hecho -> this.aplicarFiltrosAUnHecho(filtros, hecho) || !hecho.fueEliminado())
        .toList();

    hechosFiltrados.forEach(hecho -> {
      hecho.imprimirHecho();
      System.out.println();
    });

    System.out.println("Total de hechos: " + hechosFiltrados.size());

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


  private boolean aplicarFiltrosAUnHecho(List<Filtro> filtros,Hecho hecho){
    return filtros.stream().allMatch(filtro->filtro.cumpleFiltro(hecho));
  }
}
