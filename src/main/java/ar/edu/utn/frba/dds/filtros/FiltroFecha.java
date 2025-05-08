package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hecho.Hecho;
import java.time.LocalDate;

public class FiltroFecha extends Filtro {
  LocalDate fechaDesde;
  LocalDate fechaHasta;
  CampoDeHecho campoDeHechoAplicado = CampoDeHecho.FECHA_ACONTECIMIENTO;
  
  public FiltroFecha(LocalDate fechaDesde, LocalDate fechaHasta) {
    this.fechaDesde = fechaDesde;
    this.fechaHasta = fechaHasta;
  }

  public boolean cumpleFiltro(Hecho hecho) {
    return this.fechaDesde
        .isBefore(campoDeHechoAplicado.obtenerValor(hecho))
        &&
        this.fechaHasta.isAfter(campoDeHechoAplicado.obtenerValor(hecho));
  }

}
