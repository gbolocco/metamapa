package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.contratos.SolicitudesRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.ArrayList;
import java.util.List;

public class SolicitudesRepositoryMemory implements
    SolicitudesRepository, WithSimplePersistenceUnit {

  private static final SolicitudesRepositoryMemory instance =
      new SolicitudesRepositoryMemory();

  public static SolicitudesRepositoryMemory getInstancia() {
    return instance;
  }

  public void agregar(Solicitud solicitud) {
    entityManager().persist(solicitud);
  }

  public List<Solicitud> pendientes() {
    return entityManager()
        .createQuery("from Solicitud s where s.estadoSolicitud =:estadoSolicitud", Solicitud.class)
        .setParameter("estadoSolicitud", EstadoSolicitud.PENDIENTE)
        .getResultList();
  }

  public void eliminarSolicitud(Solicitud solicitud) {
    entityManager().remove(solicitud);
  }

  public List<Solicitud> mostrarSolicitudes(TipoSolicitud tipoSolicitud) {
    return entityManager()
        .createQuery("FROM Solicitud s WHERE s.tipoSolicitud =:tipoSolicitud", Solicitud.class)
        .setParameter("tipoSolicitud", tipoSolicitud)
        .getResultList();
  }

  public Solicitud buscarSolicitudPorId(Long id) {
    return entityManager().find(Solicitud.class, id);
  }

}


