package ar.edu.utn.frba.dds.dominio.filtros;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

public class FiltroContieneTexto implements Filtro {
  private String textoClave;
  private CampoDeHecho campoDeHechoAplicado;

  public FiltroContieneTexto(String textoClave, CampoDeHecho campoDeHechoAplicado) {
    this.textoClave = textoClave.toUpperCase();
    this.campoDeHechoAplicado = campoDeHechoAplicado;
  }

  public CampoDeHecho getCampoDeHechoAplicado() {
    return campoDeHechoAplicado;
  }

  public String getTextoClave() {
    return textoClave;
  }

  @Override
  public boolean cumpleFiltro(Hecho hecho) {

    return this.campoDeHechoAplicado
        .obtenerValor(hecho)
        .toString()
        .toUpperCase()
        .contains(this.textoClave);
  }

}