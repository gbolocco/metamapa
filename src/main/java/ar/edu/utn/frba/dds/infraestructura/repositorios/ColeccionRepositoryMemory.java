package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.colecciones.contratos.ColeccionRepository;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ColeccionRepositoryMemory implements ColeccionRepository {

  private static final ColeccionRepositoryMemory instance = new ColeccionRepositoryMemory();

  private List<Coleccion> colecciones = new ArrayList<>();

  private ColeccionRepositoryMemory() {}

  public static ColeccionRepositoryMemory getInstancia() {
    return instance;
  }

  public void modificarHecho(Hecho hechoAmodificar, Hecho hechoModificado) {

    List <Coleccion> coleccionesParaModificar = colecciones.stream().filter( coleccion -> coleccion.contieneHecho(hechoAmodificar)).toList();
    coleccionesParaModificar.forEach( coleccion -> { coleccion.modificarHecho(hechoAmodificar, hechoModificado); });
  }

  public void agregarColeccion(Coleccion coleccion) {
    this.colecciones.add(coleccion);
  }

  public Optional<Coleccion> buscarColeccionPor(String titulo) {
    return this.mostrarColecciones().stream()
        .filter(c -> Objects.equals(titulo, c.getTitulo()))
        .findFirst();
  }

  public Optional<Hecho> buscarHechoPor(String tituloHecho) {
    return this.mostrarColecciones().stream()
        .flatMap(c -> c.mostrarHechos().stream())
        .filter(h -> Objects.equals(h.getTitulo(), tituloHecho))
        .findFirst();
  }

  public List<Coleccion> mostrarColecciones() {
    return new ArrayList<>(this.colecciones);
  }

  public void vaciar() {
    this.colecciones.clear();
  }
}
