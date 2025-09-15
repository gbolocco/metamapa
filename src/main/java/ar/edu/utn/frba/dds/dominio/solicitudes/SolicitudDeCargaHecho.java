package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;
import ar.edu.utn.frba.dds.dominio.hechos.EstadoHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryMemory;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("cargaHecho")
public class SolicitudDeCargaHecho extends Solicitud {


  public SolicitudDeCargaHecho(RepresentacionDeHecho representacionDeHecho) {
    super(representacionDeHecho);
    this.tipoSolicitud = TipoSolicitud.CARGA_HECHO;
    //Validacion.validarNoNulo(hecho, "hecho");
    //hecho.setEstadoHecho(EstadoHecho.PENDIENTE_DE_APROBACION);
    SolicitudesRepositoryMemory.getInstancia().agregar(this);
  }

  public SolicitudDeCargaHecho() {

  }

  @Override
  public void aceptar() {
    estadoSolicitud = EstadoSolicitud.ACEPTADA;
    
    this.hecho.setEstadoHecho(EstadoHecho.VISUALIZABLE);
  }

  @Override
  public void rechazar() {
    estadoSolicitud = EstadoSolicitud.RECHAZADA;
    hecho.setEstadoHecho(EstadoHecho.ELIMINADO);
  }

  public void aceptarConSugerenciaDeCambio(Hecho hechoSugerido) {
    this.hecho = hechoSugerido;
    this.aceptar();
  }

}
