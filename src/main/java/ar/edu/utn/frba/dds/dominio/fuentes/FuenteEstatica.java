package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.lectores.Lector;
import java.util.List;
import java.util.stream.Collectors;

public class FuenteEstatica implements Fuente {
  private final Lector lector;
  private String rutaArchivo;

  public String getRutaArchivo() {
    return rutaArchivo;
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
