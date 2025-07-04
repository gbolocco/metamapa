package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FiltroUtils {

  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public static Map<String, String> convertirfiltrosaMap(List<Filtro> filtros) {
    Map<String, String> mapFiltros = new HashMap<>();

    filtros.forEach(filtro -> mapFiltros.putAll(filtro.convertirfiltroAmap()));
    return mapFiltros;
  }
}
