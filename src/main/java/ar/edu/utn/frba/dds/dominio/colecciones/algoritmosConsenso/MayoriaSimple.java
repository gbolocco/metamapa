package ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepositoryMemory;
import java.util.List;

public class MayoriaSimple extends AlgoritmoConsenso {

  public Boolean estaConsensuado(Hecho hecho, List<List<Hecho>> hechosCacheFiltrados) {
    int apariciones = this.cuantasVecesAparece(hecho, hechosCacheFiltrados);
    int cantFuentes = FuentesRepositoryMemory.getInstancia().getCantidadFuentes();
    return apariciones > (cantFuentes / 2);
  }
}
