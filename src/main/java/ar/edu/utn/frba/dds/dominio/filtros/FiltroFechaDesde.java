package ar.edu.utn.frba.dds.dominio.filtros;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class FiltroFechaDesde implements Filtro {
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
  public Map<String, String> convertirfiltroAMap() {

      Map<String, String> map = new HashMap<>();
      map.put("fecha_desde", this.fechaDesde.format(DATE_FORMATTER));
      return map;
    }
  }


