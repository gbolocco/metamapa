package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudEliminacionRepositoryMemory;
import java.util.Date;

public class SolicitudEliminacion  extends Solicitud{

  String justificacion;
  Integer min = 500;

  public SolicitudEliminacion(Hecho hecho, String justificacion) {
    super(hecho);

    Validacion.validarNoNulo(justificacion, "justificacion");
    Validacion.validarLongitudMinima(justificacion, min, "justificacion");
    this.justificacion = justificacion;
  }

  @Override
  public void aceptar() {
    estadoSolicitud = EstadoSolicitud.ACEPTADA;
    hecho.marcarComoEliminado();
    SolicitudEliminacionRepositoryMemory.getInstancia().eliminarSolicitud(this);
  }

  @Override
  public void rechazar() {
    estadoSolicitud = EstadoSolicitud.RECHAZADA;
  }

  public String getJustificacion() {
    return justificacion;
  }

}
