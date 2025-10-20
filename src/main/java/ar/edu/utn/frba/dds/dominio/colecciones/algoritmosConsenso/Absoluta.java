package ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepository;
import java.util.List;
import java.util.Objects;

public class Absoluta extends AlgoritmoConsenso {

  public Boolean estaConsensuado(Hecho hecho, List<List<Hecho>> hechosCacheFiltrados) {
    Integer apariciones = this.cuantasVecesAparece(hecho, hechosCacheFiltrados);
    Integer totalFuentes = FuentesRepository.getInstancia().getCantidadFuentes();
    return Objects.equals(apariciones, totalFuentes);
  }
}
