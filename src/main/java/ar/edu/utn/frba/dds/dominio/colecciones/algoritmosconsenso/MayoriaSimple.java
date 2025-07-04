package ar.edu.utn.frba.dds.dominio.colecciones.algoritmosconsenso;

import ar.edu.utn.frba.dds.compartido.servicios.agregador.ServicioDeAgregacion;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;

public class MayoriaSimple extends AlgoritmoConsenso {

  public Boolean estaConsensuado(Hecho hecho, List<Hecho> hechosCacheFiltrados) {
    Integer apariciones = this.cuantasVecesAparece(hecho, hechosCacheFiltrados);
    Integer cantFuentes = ServicioDeAgregacion.getInstancia().getCantFuentes();
    return apariciones > (cantFuentes / 2);
  }
}
