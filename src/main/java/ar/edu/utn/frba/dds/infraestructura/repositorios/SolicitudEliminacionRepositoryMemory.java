package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.contratos.SolicitudEliminacionRepository;
import java.util.ArrayList;
import java.util.List;

public class SolicitudEliminacionRepositoryMemory implements SolicitudEliminacionRepository {
  private final List<SolicitudEliminacion> solicitudesEliminacion = new ArrayList<>();

  public void agregar(SolicitudEliminacion solicitud) {
    solicitudesEliminacion.add(solicitud);
  }

  public List<SolicitudEliminacion> pendientes() {
    return solicitudesEliminacion.stream()
        .filter(SolicitudEliminacion::estaPendiente)
        .toList();
  }

  public void eliminar(SolicitudEliminacion solicitud) {
    solicitudesEliminacion.remove(solicitud);
  }

  public List<SolicitudEliminacion> mostrarSolicitudes() {
    return new ArrayList<>(solicitudesEliminacion);
  }
}
