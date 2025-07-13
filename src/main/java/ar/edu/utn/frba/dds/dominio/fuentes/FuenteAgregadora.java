package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import java.util.List;

public class FuenteAgregadora implements Fuente {

  private List<Fuente> fuentes;

  public FuenteAgregadora(List<Fuente> fuentes) {
    if (fuentes == null) throw new NullPointerException("fuente agreagadora no puede estar vacia");
    this.fuentes = fuentes;
  }

  public List<Hecho> obtenerHechos(List<Filtro> criterios) {
    return this.fuentes.stream()
        .flatMap(fuente -> fuente.obtenerHechos(criterios).stream())
        .toList();
  }

  @Override
  public TipoFuente getTipoFuente() {
    return TipoFuente.FUENTE_AGREGADORA;
  }
}
