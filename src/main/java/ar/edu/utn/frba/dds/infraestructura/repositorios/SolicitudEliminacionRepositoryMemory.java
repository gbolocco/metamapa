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

  private static final SolicitudEliminacionRepositoryMemory instance =
      new SolicitudEliminacionRepositoryMemory();

  public static SolicitudEliminacionRepositoryMemory getInstancia() {
    return instance;
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

  public void vaciar() {
    this.solicitudesEliminacion.clear();
  }
}
