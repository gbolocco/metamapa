package ar.edu.utn.frba.dds.dominio.colecciones.algoritmosconsenso;

import ar.edu.utn.frba.dds.compartido.servicios.agregador.ServicioDeAgregacion;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;
import java.util.Objects;

public class Absoluta extends AlgoritmoConsenso {

  public Boolean estaConsensuado(Hecho hecho, List<Hecho> hechosCacheFiltrados) {
    Integer apariciones = this.cuantasVecesAparece(hecho, hechosCacheFiltrados);
    Integer totalFuentes = ServicioDeAgregacion.getInstancia().getCantFuentes();
    return Objects.equals(apariciones, totalFuentes);
  }
}
