package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;
import java.util.Date;

public class SolicitudEliminacion {
  Hecho hecho;
  String justificacion;
  EstadoSolicitud estadoSolicitud;
  Date fechaSolicitud;
  Integer min = 500;

  public SolicitudEliminacion(Hecho hecho, String justificacion) {

    Validacion.validarNoNulo(hecho, "hecho");
    Validacion.validarNoNulo(justificacion, "justificacion");
    Validacion.validarLongitudMinima(justificacion, min, "justificacion");

    this.hecho = hecho;
    this.justificacion = justificacion;
    this.estadoSolicitud = EstadoSolicitud.PENDIENTE;
    this.fechaSolicitud = new Date();
  }

  public void aceptar() {
    estadoSolicitud = EstadoSolicitud.ACEPTADA;
    hecho.marcarComoEliminado();
  }

  public void rechazar() {
    estadoSolicitud = EstadoSolicitud.RECHAZADA;
  }

  public boolean estaPendiente() {
    return estadoSolicitud == EstadoSolicitud.PENDIENTE;
  }
  public Hecho getHecho() {
    return this.hecho;
  }
}
