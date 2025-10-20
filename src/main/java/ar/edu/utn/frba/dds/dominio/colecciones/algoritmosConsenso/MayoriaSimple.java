package ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepository;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("mayoriaSimple")
public class MayoriaSimple extends AlgoritmoConsenso {

  public Boolean estaConsensuado(Hecho hecho, List<List<Hecho>> hechosCacheFiltrados) {
    int apariciones =  this.cuantasVecesAparece(hecho, hechosCacheFiltrados);
    int cantFuentes = FuentesRepository.getInstancia().getCantidadFuentes();
    return apariciones >= Math.ceil((double) cantFuentes / 2);
  }
}
