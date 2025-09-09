package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.colecciones.contratos.ColeccionRepository;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class ColeccionRepositoryMemory implements WithSimplePersistenceUnit{

  private static final ColeccionRepositoryMemory instance = new ColeccionRepositoryMemory();

  private List<Coleccion> colecciones = new ArrayList<>();

  private ColeccionRepositoryMemory() {
  }

  public static ColeccionRepositoryMemory getInstancia() {
    return instance;
  }

  public void agregarColeccion(Coleccion coleccion) {
    entityManager().persist(coleccion.getFuente());
    entityManager().persist(coleccion);
  }

  public Coleccion buscarColeccionPorId(Long id) {
    return(entityManager().find(Coleccion.class,id));
  }

  public Optional<Hecho> buscarHechoPor(String tituloHecho) {
    return this.mostrarColecciones().stream()
        .flatMap(c -> c.mostrarHechos().stream())
        .filter(h -> Objects.equals(h.getTitulo(), tituloHecho))
        .findFirst();
  }

  public List<Coleccion> mostrarColecciones() {
    return entityManager().createQuery("select c from Coleccion c", Coleccion.class).getResultList();
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

  public void consensuarHechosDeColecciones() {
    this.colecciones.forEach(Coleccion::consensuarHechos);
  }

}


