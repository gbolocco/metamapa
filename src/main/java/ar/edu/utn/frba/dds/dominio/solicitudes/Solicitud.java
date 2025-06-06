package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.Date;

public abstract class Solicitud {

  protected EstadoSolicitud estadoSolicitud;
  protected Hecho hecho;
  protected Date fechaSolicitud;
  protected TipoSolicitud tipoSolicitud;

  public Solicitud(Hecho hecho) {
    this.estadoSolicitud = EstadoSolicitud.PENDIENTE;
    this.hecho = hecho;
    this.fechaSolicitud = new Date();
  }

  public EstadoSolicitud getEstadoSolicitud() {
    return estadoSolicitud;
  }

  public Hecho getHecho() {
    return hecho;
  }

  public Date getFechaSolicitud() {
    return (fechaSolicitud == null) ? null : new Date(fechaSolicitud.getTime());
  }

  public boolean estaPendiente() {
    return estadoSolicitud == EstadoSolicitud.PENDIENTE;
  }

  public TipoSolicitud getTipoSolicitud() {
    return tipoSolicitud;
  }

  public abstract void aceptar();

  public abstract void rechazar();

}
