package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.contratos.HechosRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import java.util.ArrayList;
import java.util.List;

public class HechosRepositoryMemory implements WithSimplePersistenceUnit {

  private HechosRepositoryMemory() {
  }

  private static final HechosRepositoryMemory instance = new HechosRepositoryMemory();

  private final List<Hecho> hechos = new ArrayList<>();

  public static HechosRepositoryMemory getInstancia() {
    return instance;
  }

  public void cargarHecho(Hecho hecho) {
    entityManager().persist(hecho);
  }

public List<Hecho> buscarHechos(String texto) {

    return entityManager().createQuery("FROM Hecho WHERE MATCH(descripcion,titulo) AGAINST (:texto)", Hecho.class)
        .setParameter("texto", "%" + texto + "%")
        .getResultList();

}

  public List<Hecho> buscarPorTexto(String texto) {
    SearchSession searchSession = Search.session(entityManager());

    return searchSession.search(Hecho.class)
        .where(f -> f.match()
            .fields("titulo", "descripcion")
            .matching(texto)
            .analyzer("standard"))
        .fetchAllHits();
  }

  public List<Hecho> mostrarHechos() {
    return entityManager()
        .createQuery("FROM Hecho h", Hecho.class)
        .getResultList();
  }

//HACER QUERY
  public List<Hecho> filtrarHechos(List<Filtro> filtros, OrigenHecho origenHecho) {
    return hechos.stream().filter(
        hecho -> filtros
            .stream()
            .allMatch(
                filtro -> filtro.cumpleFiltro(hecho)
                   && hecho.getOrigenHecho().equals(origenHecho)))
        .toList();
  }

  public void modificarHecho(Hecho hechoaModificar, Hecho hechoModificado) {
    if (!hechos.contains(hechoaModificar)) {
      throw new IllegalArgumentException("El hecho no existe en la fuenta dinamica");
    }
    this.hechos.set(hechos.indexOf(hechoaModificar), hechoModificado);
  }

}
