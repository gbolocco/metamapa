package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroContieneTexto;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroFechaDesde;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroFechaHasta;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FiltroUtils {

  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public static Map<String, String> convertirfiltrosaMap(List<Filtro> filtros) {
    Map<String, String> mapFiltros = new HashMap<>();

    for (Filtro filtro : filtros) {
      if (filtro instanceof FiltroFechaDesde) {
        FiltroFechaDesde f = (FiltroFechaDesde) filtro;
        String fechaStr = f.getFechaDesde().format(DATE_FORMATTER);
        mapFiltros.put("fecha_desde", fechaStr);
        //mapFiltros.put("campo_fecha_desde", f.getCampoDeHechoAplicado().name());

      } else if (filtro instanceof FiltroFechaHasta) {
        FiltroFechaHasta f = (FiltroFechaHasta) filtro;
        String fechaStr = f.getFechaHasta().format(DATE_FORMATTER);
        mapFiltros.put("fecha_hasta", fechaStr);
        //mapFiltros.put("campo_fecha_hasta", f.getCampoDeHechoAplicado().name());

      } else if (filtro instanceof FiltroContieneTexto) {
        FiltroContieneTexto f = (FiltroContieneTexto) filtro;
        mapFiltros.put(f.getCampoDeHechoAplicado()
            .name().toLowerCase(), f.getTextoClave().toLowerCase());

      }
    }

    return mapFiltros;
  }
}
