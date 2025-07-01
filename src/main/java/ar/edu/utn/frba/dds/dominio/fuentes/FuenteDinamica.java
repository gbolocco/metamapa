package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import java.util.List;


public class FuenteDinamica implements Fuente {
  public List<Hecho> obtenerHechos(List<Filtro> criterios) {
    return HechosRepositoryMemory
        .getInstancia()
        .filtrarHechos(criterios, OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);
  }

  @Override
  public TipoFuente getTipoFuente() {
    return TipoFuente.FUENTE_DINAMICA;
  }
}
