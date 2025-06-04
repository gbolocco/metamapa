package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryMemory;

public class SolicitudEliminacion  extends Solicitud{

  String justificacion;
  Integer min = 500;

  public SolicitudEliminacion(Hecho hecho, String justificacion) {
    super(hecho);
    this.tipoSolicitud = TipoSolicitud.ELIMINACION_HECHO;
    Validacion.validarNoNulo(justificacion, "justificacion");
    Validacion.validarLongitudMinima(justificacion, min, "justificacion");
    this.justificacion = justificacion;
    SolicitudesRepositoryMemory.getInstancia().agregar(this);
  }

  @Override
  public void aceptar() {
    estadoSolicitud = EstadoSolicitud.ACEPTADA;
    hecho.marcarComoEliminado();
  }

  @Override
  public void rechazar() {
    estadoSolicitud = EstadoSolicitud.RECHAZADA;
  }

  public String getJustificacion() {
    return justificacion;
  }

}
