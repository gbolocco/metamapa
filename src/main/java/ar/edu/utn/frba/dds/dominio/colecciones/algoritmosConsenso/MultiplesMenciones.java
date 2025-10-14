package ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;

public class MultiplesMenciones extends AlgoritmoConsenso {

  public Boolean estaConsensuado(Hecho hecho, List<List<Hecho>> hechosCacheFiltrados) {
    int coincidencias = 0;

    for (List<Hecho> hechosFuente : hechosCacheFiltrados) {
      for (Hecho h : hechosFuente) {
        if (h.equals(hecho)) {
          coincidencias++;
          break; // ya contamos esta fuente
        } else if (hechosDeMismoTituloYdistintosAtributos(h, hecho)) {
          // hay otro hecho con el mismo título pero diferente contenido
          return false;
        }
      }
    }

    return coincidencias >= 2;
  }

  public boolean hechosDeMismoTituloYdistintosAtributos(Hecho h1, Hecho h2) {
    return h1.getTitulo().equalsIgnoreCase(h2.getTitulo())
            && !h1.getAtributosClave().equals(h2.getAtributosClave());
  }
}


/*
múltiples menciones:
Si al menos dos fuentes del nodo contienen un mismo hecho y ninguna otra fuente del nodo
contiene otro de igual título pero diferentes atributos, se lo considera consensuado;
*/