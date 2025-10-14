package ar.edu.utn.frba.dds.dominio.filtros;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("filtroFechaCargaDesde")
public class FiltroFechaDeCargaDesde extends Filtro {
  private LocalDateTime fechaCargaDesde;
  private CampoDeHecho campoDeHechoAplicado;
  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public FiltroFechaDeCargaDesde(
      LocalDateTime fechaCargaDesde,
      CampoDeHecho campoDeHechoAplicado
  ) {
    this.fechaCargaDesde = fechaCargaDesde;
    this.campoDeHechoAplicado = campoDeHechoAplicado;
  }

  public FiltroFechaDeCargaDesde() {

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


  @Override
  public Map<String, String> convertirfiltroAmap() {
    Map<String, String> map = new HashMap<>();
    map.put("fecha_de_carga_desde", this.fechaCargaDesde().format(DATE_FORMATTER));
    return map;
  }
}
