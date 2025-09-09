package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.lectores.Lector;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import lombok.Getter;

@Entity
@DiscriminatorValue("estatica")
@Getter
public class FuenteEstatica extends Fuente {
  @Transient
  private Lector lector;
  private String rutaArchivo;

  public FuenteEstatica() {

  }

  public FuenteEstatica(String rutaArchivo, Lector lector) {
    this.rutaArchivo = rutaArchivo;
    this.lector = lector;
  }

  public List<Hecho> obtenerHechos(List<Filtro> criteriosDePertenencia) {
    List<Hecho> hechosLeidos = lector.leer(rutaArchivo);

    return hechosLeidos.stream()
        .filter(hecho -> cumpleCriterio(hecho, criteriosDePertenencia))
        .collect(Collectors.toList());
  }

  @Override
  public TipoFuente getTipoFuente() {
    return TipoFuente.FUENTE_ESTATICA;
  }

  private boolean cumpleCriterio(Hecho hecho, List<Filtro> criterios) {
    return criterios.stream().allMatch(f -> f.cumpleFiltro(hecho));
  }
}
