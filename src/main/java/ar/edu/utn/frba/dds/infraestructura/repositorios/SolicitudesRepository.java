package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudModificacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;

public class SolicitudesRepository implements
    ar.edu.utn.frba.dds.dominio.solicitudes.contratos.SolicitudesRepository, WithSimplePersistenceUnit {

  private static final SolicitudesRepository instance = new SolicitudesRepository();

  public static SolicitudesRepository getInstancia() {
    return instance;
  }

  public void agregar(Solicitud solicitud) {
    withTransaction(() -> {
      entityManager().persist(solicitud);
    });
  }

  public List<Solicitud> pendientes() {
    return entityManager()
        .createQuery("from Solicitud s where s.estadoSolicitud =:estadoSolicitud", Solicitud.class)
        .setParameter("estadoSolicitud", EstadoSolicitud.PENDIENTE)
        .getResultList();
  }

  public List<Solicitud> pendientesPorTipo(TipoSolicitud tipoSolicitud) {
    Class<? extends Solicitud> type = null;
    if (tipoSolicitud == TipoSolicitud.ELIMINACION_HECHO) {
      type = SolicitudEliminacion.class;
    } else if (tipoSolicitud == TipoSolicitud.MODIFICACION_HECHO) {
      type = SolicitudModificacion.class;
    }

    if (type == null) {
      return List.of();
    }

    return entityManager()
        .createQuery(
            "FROM Solicitud s WHERE s.estadoSolicitud =:estadoSolicitud AND TYPE(s) =:type ORDER BY s.fechaSolicitud DESC",
            Solicitud.class)
        .setParameter("estadoSolicitud", EstadoSolicitud.PENDIENTE)
        .setParameter("type", type)
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

  public List<Solicitud> todas() {
    return withTransaction(() -> {
      return entityManager()
          .createQuery("FROM Solicitud s ORDER BY s.fechaSolicitud DESC", Solicitud.class)
          .getResultList();
    });
  }

  public void actualizar(Solicitud solicitud) {
    withTransaction(() -> {
      entityManager().merge(solicitud);
    });
  }

  public List<Solicitud> buscarPorUsuario(Long usuarioId) {
    return entityManager()
        .createQuery("FROM Solicitud s WHERE s.usuario.id = :usuarioId ORDER BY s.fechaSolicitud DESC", Solicitud.class)
        .setParameter("usuarioId", usuarioId)
        .getResultList();
  }

}
