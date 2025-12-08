package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.hechos.EstadoRepresentacionHecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.config.EntityManagerProvider;
import javax.persistence.EntityManager;
import java.util.List;

public class RepresentacionHechosRepository {

  private static final RepresentacionHechosRepository instance = new RepresentacionHechosRepository();

  public static RepresentacionHechosRepository getInstancia() {
    return instance;
  }

  private EntityManager entityManager() {
    return EntityManagerProvider.getEntityManager();
  }

  public List<RepresentacionDeHecho> getRepHechos() {
    return entityManager()
        .createQuery("FROM RepresentacionDeHecho r", RepresentacionDeHecho.class).getResultList();
  }

  public List<RepresentacionDeHecho> getRepHechosEliminados() {
    return entityManager()
        .createQuery("FROM RepresentacionDeHecho r where r.estadoRepresentacionHecho=:eliminado",
            RepresentacionDeHecho.class)
        .setParameter("eliminado", EstadoRepresentacionHecho.ELIMINADO)
        .getResultList();
  }

  public void cargarRepresentacionDeHecho(RepresentacionDeHecho representacionDeHecho) {
    EntityManagerProvider.withTransaction(() -> {
      entityManager().persist(representacionDeHecho);
    });
  }

}
