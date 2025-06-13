package ar.edu.utn.frba.dds.dominio.solicitudes.contratos;

import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import java.util.List;

public interface SolicitudesRepository {

  void agregar(Solicitud solicitud);

  List<Solicitud> pendientes();

  void eliminarSolicitud(Solicitud solicitud);

  public void vaciar();

  List<Solicitud> mostrarSolicitudes(TipoSolicitud tipoSolicitud);
}
