package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.RepresentacionHechosRepositoryMemory;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

  protected RepresentacionDeHecho representacionDeHecho;
  protected Date fechaSolicitud;
  @Enumerated(EnumType.STRING)
  protected TipoSolicitud tipoSolicitud;
  @Column(length = 1000)
  String justificacion;

  public Solicitud(RepresentacionDeHecho representacionDeHecho) {
    this.estadoSolicitud = EstadoSolicitud.PENDIENTE;
    this.representacionDeHecho = representacionDeHecho;
    this.fechaSolicitud = new Date();
    RepresentacionHechosRepositoryMemory
        .getInstancia().cargarRepresentacionDeHecho(representacionDeHecho);
  }

  public Solicitud() {

  }

  public boolean estaPendiente() {
    return estadoSolicitud == EstadoSolicitud.PENDIENTE;
  }

  public abstract void aceptar();

  public abstract void rechazar();


  /*
  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public Hecho getHecho() {
    return hecho;
  }
  public EstadoSolicitud getEstadoSolicitud() {
    return estadoSolicitud;
  }
    public Date getFechaSolicitud() {
    return (fechaSolicitud == null) ? null : new Date(fechaSolicitud.getTime());
  }

    */
}
