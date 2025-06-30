package ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

public interface AlgoritmoConsenso {
  public Boolean estaConsensuado(Hecho hecho);
}
