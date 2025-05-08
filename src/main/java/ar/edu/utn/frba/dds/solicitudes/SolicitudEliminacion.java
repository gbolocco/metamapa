package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.colecciones.ColectionManager;
import ar.edu.utn.frba.dds.hecho.Hecho;
import java.util.Date;
import ar.edu.utn.frba.dds.Validaciones.Validacion;

public class SolicitudEliminacion {
  Hecho hecho;
  String justificacion;
  EstadoSolicitud estadoSolicitud;
  Date fechaSolicitud;
  Integer max = 500;

  public SolicitudEliminacion(Hecho hecho, String justificacion) {

    Validacion.validarNoNulo(hecho, "hecho");
    Validacion.validarNoNulo(justificacion, "justificacion");
    Validacion.validarLongitudMaxima(justificacion, max, "justificacion");

    this.hecho = hecho;
    this.justificacion = justificacion;
    this.estadoSolicitud = EstadoSolicitud.PENDIENTE;
    this.fechaSolicitud = new Date();
  }

  public void aceptar() {
    this.estadoSolicitud = estadoSolicitud.ACEPTADA;
    ColectionManager.fueAceptada(this);
  }

  public void rechazar() {
    this.estadoSolicitud = estadoSolicitud.RECHAZADA;
    ColectionManager.fueRechazada(this);
  }

  public Hecho getHecho() {
    return this.hecho;
  }
}
