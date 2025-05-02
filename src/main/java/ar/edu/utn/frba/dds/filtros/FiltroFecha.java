package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.Hecho;

import java.time.LocalDate;

public class FiltroFecha extends Filtro{
  LocalDate fechaDesde;
  LocalDate fechaHasta;
  CampoDeHecho campoDeHechoAplicado=CampoDeHecho.FECHA_ACONTECIMIENTO;

  public boolean cumpleFiltro(Hecho hecho){

    return this.fechaDesde.isAfter(campoDeHechoAplicado.obtenerValor(hecho)) && this.fechaHasta.isBefore(campoDeHechoAplicado.obtenerValor(hecho));
  }

}
