package ar.edu.utn.frba.dds.dominio.filtros;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.time.LocalDateTime;

public class FiltroFechaDeCargaDesde implements Filtro {
  private LocalDateTime fechaCargaDesde;
  private CampoDeHecho campoDeHechoAplicado;

  public FiltroFechaDeCargaDesde(
      LocalDateTime fechaCargaDesde,
      CampoDeHecho campoDeHechoAplicado
  ) {
    this.fechaCargaDesde = fechaCargaDesde;
    this.campoDeHechoAplicado = campoDeHechoAplicado;
  }

  public LocalDateTime fechaCargaDesde() {
    return fechaCargaDesde;
  }

  public CampoDeHecho getCampoDeHechoAplicado() {
    return campoDeHechoAplicado;
  }

  public boolean cumpleFiltro(Hecho hecho) {
    return this.fechaCargaDesde.isBefore(this.campoDeHechoAplicado.obtenerValor(hecho));
  }
}
