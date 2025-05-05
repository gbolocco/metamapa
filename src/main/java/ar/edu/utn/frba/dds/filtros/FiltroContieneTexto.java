package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.Hecho;

public class FiltroContieneTexto extends Filtro{
  private String textoClave;
  private CampoDeHecho campoDeHechoAplicado;
  public FiltroContieneTexto(String textoClave, CampoDeHecho campoDeHechoAplicado){
    this.textoClave = textoClave.toUpperCase();
    this.campoDeHechoAplicado = campoDeHechoAplicado;
  }
  @Override
  public boolean cumpleFiltro(Hecho hecho){

    return this.campoDeHechoAplicado.obtenerValor(hecho).toString().toUpperCase().contains(this.textoClave);
  }

}