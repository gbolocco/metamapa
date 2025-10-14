package ar.edu.utn.frba.dds.dominio.filtros;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("filtroTexto")
public class FiltroContieneTexto extends Filtro {
  private String textoClave;
  private CampoDeHecho campoDeHechoAplicado;

  public FiltroContieneTexto(String textoClave, CampoDeHecho campoDeHechoAplicado) {
    this.textoClave = textoClave.toUpperCase();
    this.campoDeHechoAplicado = campoDeHechoAplicado;
  }

  public FiltroContieneTexto() {

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

  @Override
  public Map<String, String> convertirfiltroAmap() {
    Map<String, String> map = new HashMap<>();
    map.put(this.getCampoDeHechoAplicado()
        .name().toLowerCase(), this.getTextoClave().toLowerCase());
    return map;
  }
}