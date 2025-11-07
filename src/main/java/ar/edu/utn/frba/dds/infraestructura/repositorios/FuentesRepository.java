package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;

// todas las fuentes del nodo
public class FuentesRepository implements WithSimplePersistenceUnit {

  private static final FuentesRepository instance = new FuentesRepository();


  private FuentesRepository() {}

  public static FuentesRepository getInstancia() {
    return instance;
  }

  public List<Fuente> getFuentes() {
    return entityManager()
        .createQuery("from Fuente", Fuente.class).getResultList();
  }

  public void agregarFuente(Fuente fuente) {
    //entityManager().getTransaction().begin();
    entityManager().persist(fuente);
    //entityManager().getTransaction().commit();
  }

  // metodos para algoritmos de consenso

  //TODO
  public List<List<Hecho>> obtenerHechosDeFuentes(List<Filtro> criterios) {
    return getFuentes().stream()
        .map(fuente -> fuente.obtenerHechos(criterios))
        .toList();
  }

  public Fuente buscar(Long id) {
    return entityManager().find(Fuente.class, id);
  }

  public int getCantidadFuentes() {
    return  getFuentes().size();
  }

  public void actualizarListasFuentes() {
    List<Fuente> fuentes = getFuentes();
    List<RepresentacionDeHecho> representacionDeHechos =
        RepresentacionHechosRepository
            .getInstancia().getRepHechosEliminados();

    fuentes.forEach(fuente -> fuente.actualizarLista(representacionDeHechos));
  }
}
