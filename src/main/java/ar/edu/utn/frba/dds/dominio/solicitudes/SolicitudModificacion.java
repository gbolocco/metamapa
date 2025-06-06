package ar.edu.utn.frba.dds.dominio.solicitudes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.usuario.Contribuyente;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepositoryMemory;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryMemory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SolicitudModificacion extends Solicitud {

  protected Hecho hechoModificado;
  protected Contribuyente contribuyente;

  public SolicitudModificacion(Hecho hecho, Hecho hechoModificado, Contribuyente contribuyente) {
    super(hecho);
    this.contribuyente = contribuyente;
    if (!this.sePuedeModificar() || contribuyente == null) {
      throw new UnsupportedOperationException("No se puede modificar el hecho");
    }
    this.tipoSolicitud = TipoSolicitud.MODIFICACION_HECHO;
    this.hechoModificado = hechoModificado;
    SolicitudesRepositoryMemory.getInstancia().agregar(this);
  }

  public boolean sePuedeModificar() {
    return (hecho.getOrigenHecho().getContribuyenteHecho() == this.contribuyente
        && this.cumpleCondicionDias(hecho.getFechaDeCarga(), LocalDateTime.now()));
  }

  public boolean cumpleCondicionDias(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
    long dias = ChronoUnit.DAYS.between(fechaInicial, fechaFinal);
    return dias >= 0 && dias <= 7;
  }

  public LocalDateTime getFechaDeCarga() {
    return hecho.getFechaDeCarga();
  }

  @Override
  public void aceptar() {
    this.estadoSolicitud = EstadoSolicitud.ACEPTADA;
    ColeccionRepositoryMemory.getInstancia().modificarHecho(this.hecho, this.hechoModificado);
    HechosRepositoryMemory.getInstancia().modificarHecho(this.hecho, this.hechoModificado);
  }

  @Override
  public void rechazar() {
    this.estadoSolicitud = EstadoSolicitud.RECHAZADA;
  }

}
