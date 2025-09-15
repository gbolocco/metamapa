package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;
import ar.edu.utn.frba.dds.dominio.hechos.EstadoHecho;
import ar.edu.utn.frba.dds.dominio.hechos.EstadoRepresentacionHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.dominio.spam.DetectorDeSpam;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryMemory;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.time.LocalDateTime;


@Entity
@DiscriminatorValue("eliminacion")
public class SolicitudEliminacion  extends Solicitud {
  @Column(length = 1000)
  String justificacion;
  Integer min = 500;
  @Transient
  DetectorDeSpam detectorDeSpam;

  public SolicitudEliminacion(RepresentacionDeHecho representacionDeHecho, String justificacion) {
    super(representacionDeHecho);
    this.tipoSolicitud = TipoSolicitud.ELIMINACION_HECHO;
    Validacion.validarNoNulo(justificacion, "justificacion");
    Validacion.validarLongitudMinima(justificacion, min, "justificacion");
    this.justificacion = justificacion;
    this.agregarSolicitud();
    SolicitudesRepositoryMemory.getInstancia().agregar(this);
  }

  /*public void agregarSolicitud(){
    SolicitudesRepositoryMemory.getInstancia().agregar(this);
  }*/

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
    this.representacionDeHecho.setEstadoRepresentacionHecho(EstadoRepresentacionHecho.ACEPTADO);

    Hecho hecho = new Hecho(this.representacionDeHecho.getTitulo(),
        this.representacionDeHecho.getDescripcion(),
        this.representacionDeHecho.getCategoria(),
        this.representacionDeHecho.getUbicacion(),
        this.representacionDeHecho.getFechaAcontecimiento(),
        LocalDateTime.now(),
        OrigenHecho.PROVISTO_POR_CONTRIBUYENTE
    );

    //HechosRepositoryMemory.getInstancia().cargarHecho(hecho);
    //hecho.setEstadoHecho(EstadoHecho.ELIMINADO);
  }

  @Override
  public void rechazar() {
    estadoSolicitud = EstadoSolicitud.RECHAZADA;
    representacionDeHecho.setEstadoRepresentacionHecho(EstadoRepresentacionHecho.RECHAZADO);
  }

  public String getJustificacion() {
    return justificacion;
  }

  public void setJustificacion(String justificacion) {
    this.justificacion = justificacion;
  }
}
