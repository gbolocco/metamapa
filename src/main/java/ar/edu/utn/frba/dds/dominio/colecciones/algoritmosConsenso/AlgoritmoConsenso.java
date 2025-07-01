package ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

import java.util.List;

public interface AlgoritmoConsenso {
  public Boolean estaConsensuado(Hecho hecho, List<Filtro> criterioDePertenencia);
}
