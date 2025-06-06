package ar.edu.utn.frba.dds.dominio.filtros;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.time.LocalDate;

public class FiltroFechaHasta implements Filtro {
  private LocalDate fechaHasta;
  private CampoDeHecho campoDeHechoAplicado;

  public FiltroFechaHasta(
      LocalDate fechaHasta,
      CampoDeHecho campoDeHechoAplicado
  ) {
    this.fechaHasta = fechaHasta;
    this.campoDeHechoAplicado = campoDeHechoAplicado;
  }

  public LocalDate getFechaHasta() {
    return fechaHasta;
  }


  public CampoDeHecho getCampoDeHechoAplicado() {
    return campoDeHechoAplicado;
  }

  public boolean cumpleFiltro(Hecho hecho) {
    return this.fechaHasta.isBefore(this.campoDeHechoAplicado.obtenerValor(hecho));
  }

}
