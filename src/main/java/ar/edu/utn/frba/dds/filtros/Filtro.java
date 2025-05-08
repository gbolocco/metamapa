package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hecho.Hecho;

public abstract class Filtro {

  public abstract boolean cumpleFiltro(Hecho hecho);
}
