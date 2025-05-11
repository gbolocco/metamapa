package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hecho.Hecho;

public interface Filtro {
  boolean cumpleFiltro(Hecho hecho);
}
