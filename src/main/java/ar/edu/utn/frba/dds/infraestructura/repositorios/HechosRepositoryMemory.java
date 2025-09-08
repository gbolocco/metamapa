package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.contratos.HechosRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.ArrayList;
import java.util.List;

public class HechosRepositoryMemory implements WithSimplePersistenceUnit {

  private HechosRepositoryMemory() {
  }

  private static final HechosRepositoryMemory instance = new HechosRepositoryMemory();

  //private final List<Hecho> hechos = new ArrayList<>();

  public static HechosRepositoryMemory getInstancia() {
    return instance;
  }

  public void cargarHecho(Hecho hecho) {
    entityManager().persist(hecho);
  }


  public List<Hecho> mostrarHechos() {
    return entityManager()
        .createQuery("FROM Hecho h", Hecho.class)
        .getResultList();
  }

//HACER QUERY
  public List<Hecho> filtrarHechos(List<Filtro> filtros, OrigenHecho origenHecho) {
    List<Hecho> hechos = this.mostrarHechos();
    return hechos.stream().filter(
        hecho -> filtros
            .stream()
            .allMatch(
                filtro -> filtro.cumpleFiltro(hecho)
                   && hecho.getOrigenHecho().equals(origenHecho)))
        .toList();
  }

  public void modificarHecho(Hecho hechoaModificar, Hecho hechoModificado) {
    List<Hecho> hechos = this.mostrarHechos();
    if (!hechos.contains(hechoaModificar)) {
      throw new IllegalArgumentException("El hecho no existe en la fuenta dinamica");
    }


  }


  public void agregarHechoAColeccion(Long coleccionId, Hecho hecho) {

    Coleccion coleccion = entityManager().find(Coleccion.class, coleccionId);


    if (hecho.getId() == null) {
      entityManager().persist(hecho);
    } else {

      hecho = entityManager().merge(hecho);
    }


    coleccion.anadirHecho(hecho);


  }
}
