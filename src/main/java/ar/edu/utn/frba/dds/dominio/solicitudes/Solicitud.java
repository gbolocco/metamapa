package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "solicitud") // Mapped to a single table named 'fuentes'
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_solicitud", discriminatorType = DiscriminatorType.STRING)
public abstract class Solicitud {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false, name = "solicitud_id")
  private Long id;
  @Enumerated(EnumType.STRING)
  protected EstadoSolicitud estadoSolicitud;
  @ManyToOne
  @Transient
  protected Hecho hecho;
  protected Date fechaSolicitud;
  @Enumerated(EnumType.STRING)
  protected TipoSolicitud tipoSolicitud;

  public Solicitud(Hecho hecho) {
    this.estadoSolicitud = EstadoSolicitud.PENDIENTE;
    this.hecho = hecho;
    this.fechaSolicitud = new Date();
  }

  public Solicitud() {

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

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
