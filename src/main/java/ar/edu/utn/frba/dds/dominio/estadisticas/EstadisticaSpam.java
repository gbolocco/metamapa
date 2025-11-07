package ar.edu.utn.frba.dds.dominio.estadisticas;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import ar.edu.utn.frba.dds.dominio.spam.DetectorDeSpam;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryDB;

import java.util.List;

public class EstadisticaSpam extends Estadistica {

  private DetectorDeSpam detectorDeSpam;

  public EstadisticaSpam(DetectorDeSpam detectorDeSpam, boolean publica) {
    super(publica);
    this.detectorDeSpam = detectorDeSpam;
  }

  public String calcular(List<Hecho> hechos) {
    List<Solicitud> solicitudesDeEliminacionSpan =
        SolicitudesRepositoryDB
            .getInstancia()
            .mostrarSolicitudes(TipoSolicitud.ELIMINACION_HECHO)
            .stream()
            .filter(s -> detectorDeSpam.esSpam(s.getJustificacion()))
            .toList();
    int cantidadSolicitudesSpan = solicitudesDeEliminacionSpan.size();

      this.respuesta = cantidadSolicitudesSpan + " solicitudes de eliminacion son Spam";
      return respuesta;
  }
}
