package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.contratos.SolicitudesRepository;
import java.util.ArrayList;
import java.util.List;

public class SolicitudesRepositoryMemory implements SolicitudesRepository {

  private final List<Solicitud> solicitudes = new ArrayList<>();

  public void agregar(Solicitud solicitud) {
    solicitudes.add(solicitud);
  }

  private static final SolicitudesRepositoryMemory instance =
      new SolicitudesRepositoryMemory();

  public static SolicitudesRepositoryMemory getInstancia() {
    return instance;
  }


  public List<Solicitud> pendientes() {
    return solicitudes.stream()
        .filter(Solicitud::estaPendiente)
        .toList();
  }

  public void eliminarSolicitud(Solicitud solicitud) {
    solicitudes.remove(solicitud);
  }

  public List<Solicitud> mostrarSolicitudes(TipoSolicitud tipoSolicitud) {
    return this.solicitudes.stream().filter(s -> s.getTipoSolicitud()
        == tipoSolicitud).toList();
  }

  public void vaciar() {
    this.solicitudes.clear();
  }

}


