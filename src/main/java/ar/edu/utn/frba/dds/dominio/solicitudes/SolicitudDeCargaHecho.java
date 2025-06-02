package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryMemory;

public class SolicitudDeCargaHecho extends Solicitud {

  public SolicitudDeCargaHecho(Hecho hecho) {
    super(hecho);
    this.tipoSolicitud = TipoSolicitud.CARGA_HECHO;
    Validacion.validarNoNulo(hecho, "hecho");
    SolicitudesRepositoryMemory.getInstancia().agregar(this);
  }

  @Override
  public void aceptar() {
    estadoSolicitud = EstadoSolicitud.ACEPTADA;
    HechosRepository.getInstancia().cargarHecho(this.hecho);
    SolicitudesRepositoryMemory.getInstancia().eliminarSolicitud(this);
  }

  @Override
  public void rechazar() {
    estadoSolicitud = EstadoSolicitud.RECHAZADA;
    SolicitudesRepositoryMemory.getInstancia().eliminarSolicitud(this);
  }



}
