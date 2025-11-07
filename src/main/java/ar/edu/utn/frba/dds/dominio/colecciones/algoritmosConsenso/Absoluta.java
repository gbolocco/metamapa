package ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepository;
import java.util.List;
import java.util.Objects;
import javax.persistence.DiscriminatorValue;
import org.hibernate.annotations.Entity;


public class Absoluta extends AlgoritmoConsenso {

  public Boolean estaConsensuado(Hecho hecho, List<List<Hecho>> hechosCacheFiltrados) {
    Integer apariciones = this.cuantasVecesAparece(hecho, hechosCacheFiltrados);
    Integer totalFuentes = FuentesRepository.getInstancia().getCantidadFuentes();
    System.out.println(totalFuentes);
    System.out.println(apariciones);
    System.out.println("HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    return Objects.equals(apariciones, totalFuentes);
  }
}
