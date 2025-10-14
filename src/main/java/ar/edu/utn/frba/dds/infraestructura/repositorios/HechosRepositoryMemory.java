package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

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

  public List<Hecho> buscarPorTexto(String texto) {
    SearchSession searchSession = Search.session(entityManager());

    return searchSession.search(Hecho.class)
        .where(f -> f.match()
            .fields("titulo", "descripcion")
            .matching(texto)
            .analyzer("standard"))
        .fetchAllHits();
  }

  //NO HAY QUE USAR
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

  public void modificarHecho(Hecho hechoaModificar, RepresentacionDeHecho representacionDeHecho) {
    hechoaModificar.setTitulo(representacionDeHecho.getTitulo());
    hechoaModificar.setDescripcion(representacionDeHecho.getDescripcion());
    hechoaModificar.setCategoria(representacionDeHecho.getCategoria());
    hechoaModificar.setUbicacion(representacionDeHecho.getUbicacion());
    hechoaModificar.setFechaDeCarga(LocalDateTime.now());
    hechoaModificar.setFechaAcontecimiento(representacionDeHecho.getFechaAcontecimiento());
  }


  public static boolean sonEquivalentes(Hecho h1, RepresentacionDeHecho h2) {
    return h1.getTitulo().equalsIgnoreCase(h2.getTitulo())
        && h1.getDescripcion().equals(h2.getDescripcion())
        && h1.getCategoria().equals(h2.getCategoria())
        && h1.getUbicacion().getLatitud().equals(h2.getUbicacion().getLatitud())
        && h1.getUbicacion().getLongitud().equals(h2.getUbicacion().getLongitud())
        && h1.getFechaAcontecimiento().equals(h2.getFechaAcontecimiento());
  }


  public Hecho buscar(Long id) {
    return entityManager().find(Hecho.class, id);
  }


}
