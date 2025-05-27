package ar.edu.utn.frba.dds.dominio.hechos;

import java.util.Arrays;
import java.util.List;

public enum CampoEsperado {
  TITULO("titulo"),
  DESCRIPCION("descripcion"),
  CATEGORIA("categoria" ),
  LATITUD("latitud"),
  LONGITUD("longitud"),
  FECHA_ACONTECIMIENTO("fecha_acontecimiento");

  private final String alias;

  CampoEsperado(String alias) {
    this.alias = alias;
  }

  public String getAlias() {
    return alias;
  }

  public static List<CampoEsperado> listado() {
    return Arrays.asList(values());
  }


  public static CampoEsperado buscarPorEncabezado(String encabezado) {
    String normalizado = encabezado.toLowerCase().trim();
    for (CampoEsperado campo : values()) {
      if (campo.alias.contains(normalizado)) {
        return campo;
      }
    }
    return null;
  }
}
