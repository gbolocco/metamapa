package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.contratos.SolicitudesRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class SolicitudesRepositoryDB implements
    SolicitudesRepository, WithSimplePersistenceUnit {

  private static final SolicitudesRepositoryDB instance =
      new SolicitudesRepositoryDB();

  public static SolicitudesRepositoryDB getInstancia() {
    return instance;
  }

  public void agregar(Solicitud solicitud) {
    entityManager().persist(solicitud);
  }

  public List<Solicitud> pendientes() {
    return withTransaction(() -> {
      List<Solicitud> resultado = entityManager()
          .createQuery("from Solicitud s where s.estadoSolicitud =:estadoSolicitud", Solicitud.class)
          .setParameter("estadoSolicitud", EstadoSolicitud.PENDIENTE)
          .getResultList();
      System.out.println("DEBUG: Solicitudes pendientes encontradas: " + resultado.size());
      resultado.forEach(s -> System.out.println("DEBUG: Solicitud ID: " + s.getId() + ", Estado: " + s.getEstadoSolicitud() + ", Tipo: " + s.getTipoSolicitud()));
      return resultado;
    });
  }

  public void eliminarSolicitud(Solicitud solicitud) {
    entityManager().remove(solicitud);
  }

  public List<Solicitud> mostrarSolicitudes(TipoSolicitud tipoSolicitud) {
    return withTransaction(() -> {
      List<Solicitud> resultado = entityManager()
          .createQuery("FROM Solicitud s WHERE s.tipoSolicitud =:tipoSolicitud", Solicitud.class)
          .setParameter("tipoSolicitud", tipoSolicitud)
          .getResultList();
      System.out.println("DEBUG: mostrarSolicitudes por tipo " + tipoSolicitud + ": " + resultado.size());
      return resultado;
    });
  }

  public Solicitud buscarSolicitudPorId(Long id) {
    return entityManager().find(Solicitud.class, id);
  }

}


