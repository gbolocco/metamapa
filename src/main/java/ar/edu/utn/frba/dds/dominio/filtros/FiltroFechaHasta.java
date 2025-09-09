package ar.edu.utn.frba.dds.dominio.filtros;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("filtroFechaHasta")
public class FiltroFechaHasta extends Filtro {
  private LocalDateTime fechaHasta;
  private CampoDeHecho campoDeHechoAplicado;
  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public FiltroFechaHasta(
      LocalDateTime fechaHasta,
      CampoDeHecho campoDeHechoAplicado
  ) {
    this.fechaHasta = fechaHasta;
    this.campoDeHechoAplicado = campoDeHechoAplicado;
  }

  public FiltroFechaHasta() {

  }

  public LocalDateTime getFechaHasta() {
    return fechaHasta;
  }


  public CampoDeHecho getCampoDeHechoAplicado() {
    return campoDeHechoAplicado;
  }

  public boolean cumpleFiltro(Hecho hecho) {
    return this.fechaHasta.isAfter(this.campoDeHechoAplicado.obtenerValor(hecho));
  }

  @Override
  public Map<String, String> convertirfiltroAmap() {
    Map<String, String> map = new HashMap<>();
    map.put("fecha_hasta", this.fechaHasta.format(DATE_FORMATTER));
    return map;
  }

}
