package ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

import java.util.List;
import java.util.Objects;

public class MayoriaSimple extends AlgoritmoConsenso{

  public Boolean estaConsensuado(Hecho hecho, List<Hecho> hechosCacheFiltrados) {
    return Objects.equals(this.cuantasVecesAparece(hecho, hechosCacheFiltrados ), Math.floor(this.servicioDeAgregacion.getCantFuentes()/2));
  }

}
