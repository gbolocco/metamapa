package ar.edu.utn.frba.dds.dominio.filtros;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("filtroFechaDesde")
public class FiltroFechaDesde extends Filtro {
  private LocalDate fechaDesde;
  private CampoDeHecho campoDeHechoAplicado;
  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public FiltroFechaDesde(
      LocalDate fechaDesde,
      CampoDeHecho campoDeHechoAplicado
  ) {
    this.fechaDesde = fechaDesde;
    this.campoDeHechoAplicado = campoDeHechoAplicado;
  }

  public FiltroFechaDesde() {

  }

  public LocalDate getFechaDesde() {
    return fechaDesde;
  }

  public CampoDeHecho getCampoDeHechoAplicado() {
    return campoDeHechoAplicado;
  }

  public boolean cumpleFiltro(Hecho hecho) {
    return this.fechaDesde.isBefore(this.campoDeHechoAplicado.obtenerValor(hecho));
  }

  @Override
  public Map<String, String> convertirfiltroAmap() {
    Map<String, String> map = new HashMap<>();
    map.put("fecha_desde", this.fechaDesde.format(DATE_FORMATTER));
    return map;
  }
}


