package ar.edu.utn.frba.dds.dominio.colecciones;

import ar.edu.utn.frba.dds.compartido.AppLogger;
import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;
import ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso.AlgoritmoConsenso;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.filtros.TipoCombinacion;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepositoryMemory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;


public class Coleccion {

  private String titulo;
  private String descripcion;
  private List<Filtro> criteriosDePertenencia;
  private Fuente fuente;
  private List<Hecho> hechos;
  private List<Hecho> hechosConsensuados;
  private AlgoritmoConsenso algoritmoConsenso;
  String handle;


  private static final Logger logger = AppLogger.getLogger(Coleccion.class);


  public Coleccion(
      String titulo,
      String descripcion,
      List<Filtro> criteriosDePertenencia,
      Fuente fuente
  ) {
    Validacion.validarStringNoVacio(titulo, "título");
    Validacion.validarNoNulo(descripcion, "descripción"); //descripcion puede ser nula?
    Validacion.validarListaNoNula(
        criteriosDePertenencia,
        "criteriosDePertenencia"
    );
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.criteriosDePertenencia = new ArrayList<>(criteriosDePertenencia);
    this.fuente = fuente;
    this.hechos = new ArrayList<>();
    this.cargarHechos();
    this.cargarColeccion();
  }

  //getters
  public String getTitulo() {
    return this.titulo;
  }

  public String getDescripcion() {
    return this.descripcion;
  }

  public List<Hecho> mostrarHechos() {
    return new ArrayList<>(this.hechos.stream().filter(hecho -> !hecho.estaEliminado()).toList());
  }

  public void cargarColeccion() {
    ColeccionRepositoryMemory.getInstancia().agregarColeccion(this);
  }

  //metodos relacionados a los hechos

  public void cargarHechos() {
    hechos = fuente.obtenerHechos(criteriosDePertenencia);

  }

  public List<Filtro> getCriteriosDePertenencia() {
    return new ArrayList<>(criteriosDePertenencia);
  }

  public List<Hecho> getHechos() {
    return new ArrayList<>(hechos);
  }

  public void imprimirColeccion(List<Filtro> filtros, TipoCombinacion tipoCombinacion) {
    logger.info("Coleccion: {}", this.titulo);
    logger.info("Descripcion: {}", this.descripcion);
    logger.info("Criterios de pertenencia: ");
    this.criteriosDePertenencia.forEach(filtro -> {
      logger.info(filtro.toString());
    });

    logger.info("Hechos: ");
    imprimirHechosDeColeccion(filtros, tipoCombinacion);
  }

  public void imprimirHechosDeColeccion(List<Filtro> filtros, TipoCombinacion tipoCombinacion) {
    if (filtros != null) {
      List<Hecho> hechosFiltrados = this.filtrarHechos(filtros, tipoCombinacion);
      logger.info("Cantidad de hechos filtrados: {}", hechosFiltrados.size());
      hechosFiltrados.forEach(Hecho::imprimirHecho);

    } else {
      logger.info("Cantidad de hechos: {}", this.mostrarHechos().size());
      this.mostrarHechos().forEach(Hecho::imprimirHecho);
    }
  }

  public boolean cumpleFiltros(Hecho hecho, List<Filtro> filtros, TipoCombinacion tipoCombinacion) {
    if (tipoCombinacion == TipoCombinacion.AND) {
      return filtros.stream().allMatch(filtro -> filtro.cumpleFiltro(hecho));
    } else {
      return filtros.stream().anyMatch(filtro -> filtro.cumpleFiltro(hecho));
    }
  }

  public boolean contieneHecho(Hecho hecho) {
    return this.hechos.contains(hecho);
  }


  public List<Hecho> filtrarHechos(List<Filtro> filtros, TipoCombinacion tipoCombinacion) {
    return this.mostrarHechos()
        .stream()
        .filter(h -> cumpleFiltros(h, filtros, tipoCombinacion))
        .collect(Collectors.toList());
  }

  public void curarHechos() {

    this.hechosConsensuados = this.hechos.stream()
        .filter(h-> algoritmoConsenso.estaConsensuado(h))
        .collect(Collectors.toList());
  }
}
