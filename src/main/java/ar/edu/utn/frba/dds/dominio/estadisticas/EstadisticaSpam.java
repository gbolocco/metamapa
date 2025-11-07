package ar.edu.utn.frba.dds.dominio.estadisticas;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import ar.edu.utn.frba.dds.dominio.spam.DetectorDeSpam;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepository;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@DiscriminatorValue("spam")
public class EstadisticaSpam extends Estadistica {
  @Transient
  private DetectorDeSpam detectorDeSpam;

  public  EstadisticaSpam() {}

  public EstadisticaSpam(DetectorDeSpam detectorDeSpam, boolean publica) {
    this.publica = publica;
    this.detectorDeSpam = detectorDeSpam;
  }
  @Override
  public String calcular(List<Hecho> hechos) {
    List<Solicitud> solicitudesDeEliminacionSpan =
        SolicitudesRepository
            .getInstancia()
            .mostrarSolicitudes(TipoSolicitud.ELIMINACION_HECHO)
            .stream()
            .filter(s -> detectorDeSpam.esSpam(s.getJustificacion()))
            .toList();
    int cantidadSolicitudesSpan = solicitudesDeEliminacionSpan.size();

    this.fechaDeCalculo = LocalDateTime.now();

    this.respuesta = cantidadSolicitudesSpan + " solicitudes de eliminacion son Spam";
    return respuesta;
  }
}
