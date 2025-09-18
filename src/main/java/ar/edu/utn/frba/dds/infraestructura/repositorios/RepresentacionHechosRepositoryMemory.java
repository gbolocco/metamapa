package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.hechos.EstadoRepresentacionHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class RepresentacionHechosRepositoryMemory implements WithSimplePersistenceUnit {

  private static final RepresentacionHechosRepositoryMemory instance = new RepresentacionHechosRepositoryMemory();

  public static RepresentacionHechosRepositoryMemory getInstancia() {
    return instance;
  }

  public List<RepresentacionDeHecho> getRepHechos (){
    return entityManager().createQuery("FROM RepresentacionDeHecho r",RepresentacionDeHecho.class).getResultList();
  }

  public List<RepresentacionDeHecho> getRepHechosEliminados (){
    return entityManager().createQuery("FROM RepresentacionDeHecho r where r.estadoRepresentacionHecho=: eliminado",
        RepresentacionDeHecho.class)
        .setParameter("eliminado", EstadoRepresentacionHecho.ELIMINADO)
        .getResultList();
  }


  public void cargarRepresentacionDeHecho(RepresentacionDeHecho representacionDeHecho) {
    entityManager().persist(representacionDeHecho);
  }

}
