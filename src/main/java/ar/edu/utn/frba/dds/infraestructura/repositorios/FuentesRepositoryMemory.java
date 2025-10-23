package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.ArrayList;
import java.util.List;

// todas las fuentes del nodo
public class FuentesRepositoryMemory implements WithSimplePersistenceUnit {

  private static final FuentesRepositoryMemory instance = new FuentesRepositoryMemory();


  private FuentesRepositoryMemory() {}

  public static FuentesRepositoryMemory getInstancia() {
    return instance;
  }

  public List<Fuente> getFuentes() {
    return entityManager()
        .createQuery("from Fuente", Fuente.class).getResultList();
  }

  public void agregarFuente(Fuente fuente) {
    entityManager().persist(fuente);
  }

  // metodos para algoritmos de consenso

  //TODO
  public List<List<Hecho>> obtenerHechosDeFuentes(List<Filtro> criterios) {
    return getFuentes().stream()
        .map(fuente -> fuente.obtenerHechos(criterios))
        .toList();
  }

  public int getCantidadFuentes() {
    return  getFuentes().size();
  }

  public void actualizarListasFuentes() {
    List<Fuente> fuentes = getFuentes().stream().filter(fuente -> fuente.getTipoFuente()== TipoFuente.FUENTE_ESTATICA).toList();

    List<RepresentacionDeHecho> representacionDeHechos =
        RepresentacionHechosRepositoryMemory
            .getInstancia().getRepHechosEliminados();
    
    

    fuentes.forEach(fuente -> fuente.actualizarLista(representacionDeHechos));
  }
}
