package ar.edu.utn.frba.dds.dominio.fuentes;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import java.util.List;


public abstract class FuenteDinamica implements Fuente {
  public List<Hecho> obtenerHechos(List<Filtro> criterios) {
      return HechosRepositoryMemory.getInstancia().mostrarHechos();

  }
}
