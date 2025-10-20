package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ColeccionRepository implements WithSimplePersistenceUnit {

  private static final ColeccionRepository instance = new ColeccionRepository();

  private List<Coleccion> colecciones = new ArrayList<>();

  private ColeccionRepository() {
  }

  public static ColeccionRepository getInstancia() {
    return instance;
  }

  public void agregarColeccion(Coleccion coleccion) {
    entityManager().persist(coleccion.getFuente());
    entityManager().persist(coleccion);
  }

  public Coleccion buscarColeccionPorId(Long id) {
    return (entityManager().find(Coleccion.class, id));
  }

  public Optional<Hecho> buscarHechoPor(String tituloHecho) {
    return this.mostrarColecciones().stream()
        .flatMap(c -> c.mostrarHechos().stream())
        .filter(h -> Objects.equals(h.getTitulo(), tituloHecho))
        .findFirst();
  }

  public List<Coleccion> mostrarColecciones() {
    return entityManager()
        .createQuery("select c from Coleccion c", Coleccion.class).getResultList();
  }

  public void vaciar() {
    this.colecciones.clear();
  }

  public List<String> getHandleList() {
    return colecciones
            .stream()
            .map(Coleccion::getHandle)
            .toList();
  }

}


