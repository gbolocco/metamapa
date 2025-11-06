package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("modificacion")
public class SolicitudModificacion extends Solicitud {

  private Long idHecho;

  public SolicitudModificacion(RepresentacionDeHecho representacionDeHecho, Long idHecho) {

    this.tipoSolicitud = TipoSolicitud.MODIFICACION_HECHO;
    this.representacionDeHecho = representacionDeHecho;
    this.idHecho = idHecho;
    SolicitudesRepository.getInstancia().agregar(this);
  }

  public SolicitudModificacion() {

  }

  public boolean sePuedeModificar() {
    Hecho hecho = HechosRepository.getInstancia().buscar(this.idHecho);
    return this.cumpleCondicionDias(hecho.getFechaDeCarga(), LocalDateTime.now());
  }

  public boolean cumpleCondicionDias(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
    long dias = ChronoUnit.DAYS.between(fechaInicial, fechaFinal);
    return dias >= 0 && dias <= 7;
  }

  @Override
  public void aceptar() {
    this.estadoSolicitud = EstadoSolicitud.ACEPTADA;
    //this.hechoModificado.marcarComoEditado();
    HechosRepository
        .getInstancia()
        .modificarHecho(HechosRepository
            .getInstancia()
            .buscar(idHecho), representacionDeHecho);
    /*
    String hql = "UPDATE Hecho SET titulo = :titulo, descripcion = :descripcion " +
        "categoria =: categoria latidud:=
        latitud longitud:=longitud fechaAcontecimiento:= fechaAcontecimiento " +
        "WHERE id = :userId";

    Query query = entityManager().createQuery(hql);
    query.setParameter("titulo", this.representacionDeHecho.getTitulo());
    query.setParameter("descripcion", this.representacionDeHecho.getDescripcion());
    query.setParameter("categoria", this.representacionDeHecho.getCategoria());
    query.setParameter("latitud", this.representacionDeHecho.getUbicacion().getLatitud());
    query.setParameter("latitud", this.representacionDeHecho.getUbicacion().getLongitud());
    query.setParameter("latitud", this.representacionDeHecho.getFechaAcontecimiento());
    HechosRepositoryMemory
    .getInstancia().modificarHecho(this.hecho, this.hechoModificado);


    entityManager()
    .createQuery("FROM Solicitud s WHERE s.tipoSolicitud =:tipoSolicitud", Solicitud.class)
        .setParameter("tipoSolicitud", tipoSolicitud)
        .getResultList();*/
  }

  @Override
  public void rechazar() {
    this.estadoSolicitud = EstadoSolicitud.RECHAZADA;
  }

  public void aceptarConSugerenciaDeCambio(Hecho hechoSugerido) {
    //hechoSugerido.marcarComoEditado();
    this.idHecho = idHecho;
    this.aceptar();
  }
}
