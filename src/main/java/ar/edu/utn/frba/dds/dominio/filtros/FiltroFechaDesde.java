package ar.edu.utn.frba.dds.dominio.filtros;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.time.LocalDate;

public class FiltroFechaDesde implements Filtro {
  private LocalDate fechaDesde;
  private CampoDeHecho campoDeHechoAplicado;
  
  public FiltroFechaDesde(
      LocalDate fechaDesde,
      CampoDeHecho campoDeHechoAplicado
  ) {
    this.fechaDesde = fechaDesde;
    this.campoDeHechoAplicado = campoDeHechoAplicado;
  }

  public LocalDate getFechaDesde() {
    return fechaDesde;
  }

  public CampoDeHecho getCampoDeHechoAplicado() {
    return campoDeHechoAplicado;
  }

  public boolean cumpleFiltro(Hecho hecho) {
    return this.fechaDesde.isAfter(this.campoDeHechoAplicado.obtenerValor(hecho));
  }

}
