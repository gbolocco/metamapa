package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.modelo.Usuario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

public class HechosRepository implements WithSimplePersistenceUnit {

  private HechosRepository() {
  }

  private static final HechosRepository instance = new HechosRepository();

  public static HechosRepository getInstancia() {
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

  public boolean existe(Long hechoId) {
    return buscar(hechoId) != null;
  }

  // NO HAY QUE USAR
  public List<Hecho> mostrarHechos() {
    return entityManager()
        .createQuery("FROM Hecho h", Hecho.class)
        .getResultList();
  }

  public void borrarHecho(Long hechoId) {
    if (existe(hechoId)) {
      entityManager().remove(entityManager().find(Hecho.class, hechoId));
    }
  }

  // TODO HACER QUERY
  // TODO HACER QUERY
  public List<Hecho> filtrarHechos(List<Filtro> filtros, OrigenHecho origenHecho) {
    List<RepresentacionDeHecho> hechosEliminados = RepresentacionHechosRepository.getInstancia()
        .getRepHechosEliminados();

    return this.mostrarHechos().stream()
        .filter(hecho -> hechosEliminados.stream().noneMatch(eliminado -> sonEquivalentes(hecho, eliminado)))
        .filter(hecho -> hecho.getOrigenHecho().equals(origenHecho))
        .filter(hecho -> filtros.stream().allMatch(filtro -> filtro.cumpleFiltro(hecho)))
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

  public List<Hecho> buscarPorUsuario(Usuario usuario) {
    return entityManager()
        .createQuery("FROM Hecho h WHERE h.usuario = :usuario", Hecho.class)
        .setParameter("usuario", usuario)
        .getResultList();
  }

}
