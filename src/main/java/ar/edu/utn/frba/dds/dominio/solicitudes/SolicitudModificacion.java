package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("modificacion")
public class SolicitudModificacion extends Solicitud {

  private Long idHecho;

  public SolicitudModificacion(RepresentacionDeHecho representacionDeHecho, Long idHecho) {

    this.tipoSolicitud = TipoSolicitud.MODIFICACION_HECHO;
    this.representacionDeHecho = representacionDeHecho;
    this.idHecho = idHecho;
    SolicitudesRepository.getInstancia().agregar(this);
  }

  public Long getIdHecho() {
    return idHecho;
  }

  public void setIdHecho(Long idHecho) {
    this.idHecho = idHecho;
  }

  public SolicitudModificacion() {

  }

  public boolean sePuedeModificar() {
    Hecho hecho = HechosRepository.getInstancia().buscar(this.idHecho);
    return this.cumpleCondicionDias(hecho.getFechaDeCarga(), LocalDateTime.now());
  }

  public boolean cumpleCondicionDias(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
    long dias = ChronoUnit.DAYS.between(fechaInicial, fechaFinal);
    return dias >= 0 && dias <= 7;
  }

  @Override
  public void aceptar() {
    this.estadoSolicitud = EstadoSolicitud.ACEPTADA;
    // this.hechoModificado.marcarComoEditado();
    HechosRepository
        .getInstancia()
        .modificarHecho(HechosRepository
            .getInstancia()
            .buscar(idHecho), representacionDeHecho);
    SolicitudesRepository.getInstancia().actualizar(this);
  }

  @Override
  public void rechazar() {
    this.estadoSolicitud = EstadoSolicitud.RECHAZADA;
    SolicitudesRepository.getInstancia().actualizar(this);
  }

  public void aceptarConSugerenciaDeCambio(Hecho hechoSugerido) {
    // hechoSugerido.marcarComoEditado();
    this.idHecho = idHecho;
    this.aceptar();
  }

  @Override
  public TipoSolicitud getTipoSolicitud() {
    return TipoSolicitud.MODIFICACION_HECHO;
  }
}
