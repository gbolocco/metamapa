package ar.edu.utn.frba.dds.dominio.filtros;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

import java.util.List;
import java.util.Map;

public interface Filtro {
  boolean cumpleFiltro(Hecho hecho);
  Map<String, String> convertirfiltroAMap();


}
