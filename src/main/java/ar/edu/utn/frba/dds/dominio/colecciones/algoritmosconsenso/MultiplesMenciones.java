package ar.edu.utn.frba.dds.dominio.colecciones.algoritmosconsenso;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;

public class MultiplesMenciones extends AlgoritmoConsenso {

  public Boolean estaConsensuado(Hecho hecho, List<Hecho> hechosCacheFiltrados) {
    return this.cuantasVecesAparece(hecho, hechosCacheFiltrados) >= 2;
  }

  public boolean hechosDeMismoTituloYdistintosAtributos(Hecho h1, Hecho h2) {
    return h1.getTitulo().equalsIgnoreCase(h2.getTitulo())
            && !h1.getAtributosClave().equals(h2.getAtributosClave());
  }
}
