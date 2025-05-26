package ar.edu.utn.frba.dds.dominio.filtros;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

public interface Filtro {
  boolean cumpleFiltro(Hecho hecho);
}
