package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;
import ar.edu.utn.frba.dds.dominio.hechos.EstadoRepresentacionHecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.dominio.spam.DetectorDeSpam;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepository;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;


@Entity
@DiscriminatorValue("eliminacion")
@Getter
@Setter
public class SolicitudEliminacion  extends Solicitud {

  @Column(length = 1000)
  private String justificacion;
  private Integer min = 500;
  
  @Transient
  DetectorDeSpam detectorDeSpam;

  public SolicitudEliminacion(RepresentacionDeHecho representacionDeHecho, String justificacion) {
    super(representacionDeHecho);
    this.tipoSolicitud = TipoSolicitud.ELIMINACION_HECHO;
    Validacion.validarNoNulo(justificacion, "justificacion");
    Validacion.validarLongitudMinima(justificacion, min, "justificacion");
    this.justificacion = justificacion;
    this.agregarSolicitud();
  }


  public void agregarSolicitud() {
    SolicitudesRepository.getInstancia().agregar(this);
  }

  public SolicitudEliminacion() {

  }

  public void verificarSpam() {
    if (detectorDeSpam.esSpam(justificacion)) {
      rechazar();
    }
  }

  @Override
  public void aceptar() {
    estadoSolicitud = EstadoSolicitud.ACEPTADA;
    this.representacionDeHecho.setEstadoRepresentacionHecho(EstadoRepresentacionHecho.ELIMINADO);
    FuentesRepository.getInstancia().actualizarListasFuentes();
    //HechosRepositoryMemory
    // .getInstancia().buscar(this.idHechoAEliminar).setEstadoHecho(EstadoHecho.ELIMINADO);

  }

  @Override
  public void rechazar() {
    estadoSolicitud = EstadoSolicitud.RECHAZADA;
    representacionDeHecho.setEstadoRepresentacionHecho(EstadoRepresentacionHecho.RECHAZADO);

  }
  
}
