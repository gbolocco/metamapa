package ar.edu.utn.frba.dds.dominio.solicitudes.contratos;

import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import java.util.List;

public interface SolicitudEliminacionRepository {
  void agregar(SolicitudEliminacion solicitud);
  List<SolicitudEliminacion> pendientes();
  void eliminar(SolicitudEliminacion solicitud);
  List<SolicitudEliminacion> mostrarSolicitudes();
}
