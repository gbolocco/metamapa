package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hecho.Hecho;
import java.time.LocalDate;

public class FiltroFecha extends Filtro {
  private LocalDate fechaDesde;
  private LocalDate fechaHasta;
  private CampoDeHecho campoDeHechoAplicado;
  
  public FiltroFecha(LocalDate fechaDesde, LocalDate fechaHasta, CampoDeHecho campoDeHechoAplicado) {
    this.fechaDesde = fechaDesde;
    this.fechaHasta = fechaHasta;
    this.campoDeHechoAplicado = campoDeHechoAplicado;
  }

  public boolean cumpleFiltro(Hecho hecho) {
    return this.fechaDesde
        .isBefore(this.campoDeHechoAplicado.obtenerValor(hecho))
        &&
        this.fechaHasta.isAfter(this.campoDeHechoAplicado.obtenerValor(hecho));
  }

}
