package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;
import ar.edu.utn.frba.dds.dominio.hechos.EstadoHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.spam.DetectorDeSpam;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryMemory;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;


@Entity
@DiscriminatorValue("eliminacion")
public class SolicitudEliminacion  extends Solicitud {

  Integer min = 500;
  @Transient
  DetectorDeSpam detectorDeSpam;

  public SolicitudEliminacion(Hecho hecho, String justificacion) {
    super(hecho);
    this.tipoSolicitud = TipoSolicitud.ELIMINACION_HECHO;
    Validacion.validarNoNulo(justificacion, "justificacion");
    Validacion.validarLongitudMinima(justificacion, min, "justificacion");
    this.justificacion = justificacion;
    this.agregarSolicitud();
  }

  public void agregarSolicitud(){
    SolicitudesRepositoryMemory.getInstancia().agregar(this);
  }

  public SolicitudEliminacion() {

  }

  public void setDetectorDeSpam(DetectorDeSpam detectorDeSpam) {
    this.detectorDeSpam = detectorDeSpam;
  }

  public void verificarSpam() {
    if (detectorDeSpam.esSpam(justificacion)) {
      rechazar();
    }
  }

  @Override
  public void aceptar() {
    estadoSolicitud = EstadoSolicitud.ACEPTADA;
    hecho.setEstadoHecho(EstadoHecho.ELIMINADO);
  }

  @Override
  public void rechazar() {
    estadoSolicitud = EstadoSolicitud.RECHAZADA;
  }

}
