package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;
import ar.edu.utn.frba.dds.dominio.hechos.EstadoHecho;
import ar.edu.utn.frba.dds.dominio.hechos.EstadoRepresentacionHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryMemory;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

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
    this.representacionDeHecho.setEstadoRepresentacionHecho(EstadoRepresentacionHecho.ACEPTADO);

    Hecho hecho = new Hecho(this.representacionDeHecho.getTitulo(),
        this.representacionDeHecho.getDescripcion(),
        this.representacionDeHecho.getCategoria(),
        this.representacionDeHecho.getUbicacion(),
        this.representacionDeHecho.getFechaAcontecimiento(),
        LocalDateTime.now(),
        OrigenHecho.PROVISTO_POR_CONTRIBUYENTE
        );

    HechosRepositoryMemory.getInstancia().cargarHecho(hecho);

  }

  @Override
  public void rechazar() {
    estadoSolicitud = EstadoSolicitud.RECHAZADA;
    representacionDeHecho.setEstadoRepresentacionHecho(EstadoRepresentacionHecho.RECHAZADO);
  }

  /*
  public void aceptarConSugerenciaDeCambio(Hecho hechoSugerido) {
    this.hecho = hechoSugerido;
    this.aceptar();
  }*/

}
