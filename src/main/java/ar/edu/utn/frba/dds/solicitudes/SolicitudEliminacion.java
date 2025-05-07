package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.colecciones.ColectionManager;
import ar.edu.utn.frba.dds.hecho.Hecho;
import java.util.Date;

public class SolicitudEliminacion {
  Hecho hecho;
  String justificacion;
  EstadoSolicitud estadoSolicitud;
  Date fechaSolicitud;

  public SolicitudEliminacion(Hecho hecho, String justificacion) {

    if (justificacion == null) {
      throw new IllegalArgumentException("La justificación no puede ser nula");
    }
    if (justificacion.length() > 500) {
      throw new IllegalArgumentException("La justificación no puede superar los 500 caracteres");
    }

    this.hecho=hecho;
    this.justificacion=justificacion;
    this.estadoSolicitud=EstadoSolicitud.PENDIENTE;
    this.fechaSolicitud=new Date();
  }
  public void aceptar(){
    this.estadoSolicitud=estadoSolicitud.ACEPTADA;
    ColectionManager.fueAceptada(this);
  }
  public void rechazar(){
    this.estadoSolicitud=estadoSolicitud.RECHAZADA;
    ColectionManager.fueRechazada(this);
  }
  public Hecho getHecho() {
    return this.hecho;
  }
}
