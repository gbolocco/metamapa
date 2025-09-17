package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.dominio.usuario.Contribuyente;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryMemory;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;


@Entity
@DiscriminatorValue("modificacion")
public class SolicitudModificacion extends Solicitud {

  private Long IdHecho;

  public SolicitudModificacion(RepresentacionDeHecho representacionDeHecho, Long IdHecho) {

    if (HechosRepositoryMemory.getInstancia().buscar(IdHecho).getContribuyente() == null || !sePuedeModificar()) {
      throw new UnsupportedOperationException("No se puede modificar un hecho sin contribuyente o no cumple condicion dias");
    }
    this.tipoSolicitud = TipoSolicitud.MODIFICACION_HECHO;
    this.representacionDeHecho = representacionDeHecho;
    this.IdHecho = IdHecho;
    this.representacionDeHecho.setHecho(HechosRepositoryMemory.getInstancia().buscar(IdHecho));
    SolicitudesRepositoryMemory.getInstancia().agregar(this);
  }

  public SolicitudModificacion() {

  }

  public boolean sePuedeModificar() {
    Hecho hecho= HechosRepositoryMemory.getInstancia().buscar(this.IdHecho);
    return this.cumpleCondicionDias(hecho.getFechaDeCarga(), LocalDateTime.now());
  }

  public boolean cumpleCondicionDias(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
    long dias = ChronoUnit.DAYS.between(fechaInicial, fechaFinal);
    return dias >= 0 && dias <= 7;
  }

  /*public LocalDateTime getFechaDeCarga() {
    return hecho.getFechaDeCarga();
  }*/

  @Override
  public void aceptar() {
    this.estadoSolicitud = EstadoSolicitud.ACEPTADA;
    //this.hechoModificado.marcarComoEditado();
    HechosRepositoryMemory.getInstancia().modificarHecho(HechosRepositoryMemory.getInstancia().buscar(IdHecho), representacionDeHecho);
/*
    String hql = "UPDATE Hecho SET titulo = :titulo, descripcion = :descripcion " +
        "categoria =: categoria latidud:=latitud longitud:=longitud fechaAcontecimiento:= fechaAcontecimiento " +
        "WHERE id = :userId";

    Query query = entityManager().createQuery(hql);
    query.setParameter("titulo", this.representacionDeHecho.getTitulo());
    query.setParameter("descripcion", this.representacionDeHecho.getDescripcion());
    query.setParameter("categoria", this.representacionDeHecho.getCategoria());
    query.setParameter("latitud", this.representacionDeHecho.getUbicacion().getLatitud());
    query.setParameter("latitud", this.representacionDeHecho.getUbicacion().getLongitud());
    query.setParameter("latitud", this.representacionDeHecho.getFechaAcontecimiento());
    HechosRepositoryMemory.getInstancia().modificarHecho(this.hecho, this.hechoModificado);


    entityManager().createQuery("FROM Solicitud s WHERE s.tipoSolicitud =:tipoSolicitud", Solicitud.class)
        .setParameter("tipoSolicitud", tipoSolicitud)
        .getResultList();*/
  }

  @Override
  public void rechazar() {
    this.estadoSolicitud = EstadoSolicitud.RECHAZADA;
  }

  public void aceptarConSugerenciaDeCambio(Hecho hechoSugerido) {
    //hechoSugerido.marcarComoEditado();
    this.IdHecho = IdHecho;
    this.aceptar();
  }
}
