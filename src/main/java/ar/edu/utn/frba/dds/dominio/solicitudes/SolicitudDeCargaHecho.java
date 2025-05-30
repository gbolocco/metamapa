package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosYSolicitudesRepository;
import java.util.Date;

public class SolicitudDeCargaHecho extends Solicitud {

  public SolicitudDeCargaHecho(Hecho hecho) {
    super(hecho);
    Validacion.validarNoNulo(hecho, "hecho");
    HechosYSolicitudesRepository.getInstancia().cargarSolicitud(this);
  }

  @Override
  public void aceptar() {
    estadoSolicitud = EstadoSolicitud.ACEPTADA;
    HechosYSolicitudesRepository.getInstancia().seAceptoUnaSolicitud(this);
  }

  @Override
  public void rechazar() {
    estadoSolicitud = EstadoSolicitud.RECHAZADA;
    HechosYSolicitudesRepository.getInstancia().eliminarSolicitud(this);
  }

  public void aceptarConSugerenciaDeCambios() {
    estadoSolicitud = EstadoSolicitud.ACEPTADA_CON_SUGERENCIA;
    HechosYSolicitudesRepository.getInstancia().cargarHecho(hecho);
  }

}
