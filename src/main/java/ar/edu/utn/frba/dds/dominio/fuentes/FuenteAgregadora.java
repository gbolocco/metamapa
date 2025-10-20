package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("fuenteAgregadora")
public class FuenteAgregadora extends Fuente {


  @OneToMany()
  private List<Fuente> fuentes;

  public FuenteAgregadora(List<Fuente> fuentes) {
    if (fuentes == null) {
      throw new NullPointerException("fuente agreagadora no puede estar vacia");
    }
    this.fuentes = fuentes;
  }

  public FuenteAgregadora() {

  }

  @Override
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
