package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

public class SolicitudModificacion extends Solicitud {

  public SolicitudModificacion(Hecho hecho,Hecho hechoModificado) {
    super(hecho);
    this.tipoSolicitud = TipoSolicitud.MODIFICACION_HECHO;
  }

  public void aceptar() {}

  public void rechazar() {}

}
