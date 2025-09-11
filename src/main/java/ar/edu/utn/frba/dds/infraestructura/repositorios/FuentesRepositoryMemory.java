package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.ArrayList;
import java.util.List;

// todas las fuentes del nodo
public class FuentesRepositoryMemory implements WithSimplePersistenceUnit {

  private static final FuentesRepositoryMemory instance = new FuentesRepositoryMemory();



  private FuentesRepositoryMemory() {}

  public static FuentesRepositoryMemory getInstancia() { return instance; }

  public List<Fuente> getFuentes() {
    return entityManager().createQuery("from Fuente", Fuente.class).getResultList();
  }

  public void agregarFuente(Fuente fuente) {
    entityManager().persist(fuente);
  }

  // metodos para algoritmos de consenso


  public List<List<Hecho>> obtenerHechosDeFuentes(List<Filtro> criterios) {
    /*return this.fuentes.stream()
        .map(fuente -> fuente.obtenerHechos(criterios))
        .toList();*/
    return new ArrayList<>();
  }

  public int getCantidadFuentes() {
    return  getFuentes().size();
  }


}
