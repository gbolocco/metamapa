package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.hechos.EstadoRepresentacionHecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class RepresentacionHechosRepository implements WithSimplePersistenceUnit {

  private static final RepresentacionHechosRepository instance =
      new RepresentacionHechosRepository();

  public static RepresentacionHechosRepository getInstancia() {
    return instance;
  }

  public List<RepresentacionDeHecho> getRepHechos() {
    return entityManager()
        .createQuery("FROM RepresentacionDeHecho r", RepresentacionDeHecho.class).getResultList();
  }

  public List<RepresentacionDeHecho> getRepHechosEliminados() {
    return entityManager()
        .createQuery("FROM RepresentacionDeHecho r where r.estadoRepresentacionHecho=: eliminado",
        RepresentacionDeHecho.class)
        .setParameter("eliminado", EstadoRepresentacionHecho.ELIMINADO)
        .getResultList();
  }


  public void cargarRepresentacionDeHecho(RepresentacionDeHecho representacionDeHecho) {
    entityManager().persist(representacionDeHecho);
  }

}
