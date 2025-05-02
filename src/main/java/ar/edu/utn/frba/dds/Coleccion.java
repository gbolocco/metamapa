package ar.edu.utn.frba.dds;

import java.util.ArrayList;
import java.util.List;

public class Coleccion {
  private String titulo;
  private String descripcion;
  private List<CriterioDePertenencia> criterios;
  private Fuente fuente;
  private List<Hecho> hechos;

  public Coleccion(String titulo, String descripcion, List<CriterioDePertenencia> criterios, Fuente fuente) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.criterios = criterios;
    this.fuente = fuente;
    this.hechos = new ArrayList<>();
  }

  public void visualizarHechos() {
    this.hechos.stream().forEach((hecho) -> System.out.println(hecho));//TODO hacer esta funcion

  }
  public void visualizarPorFiltros(List<Filtro> filtros) {
    this.hechos.stream().forEach((hecho) -> System.out.println(hecho));//TODO hacer esta funcion
  }

}
