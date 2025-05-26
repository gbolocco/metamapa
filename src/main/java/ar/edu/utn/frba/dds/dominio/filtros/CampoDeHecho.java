package ar.edu.utn.frba.dds.dominio.filtros;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import java.time.LocalDate;
import java.util.function.Function;

public enum CampoDeHecho {
  // Cada valor del enum especifica su tipo de retorno
  TITULO(Hecho::getTitulo, String.class),
  DESCRIPCION(Hecho::getDescripcion, String.class),
  CATEGORIA(Hecho::getCategoria, String.class),
  FECHA_ACONTECIMIENTO(Hecho::getFechaAcontecimiento, LocalDate.class),
  UBICACION(Hecho::getUbicacion, Ubicacion.class);

  private final Function<Hecho, ?> extractor;
  private final Class<?> tipoRetorno;

  <T> CampoDeHecho(Function<Hecho, T> extractor, Class<T> tipoRetorno) {
    this.extractor = extractor;
    this.tipoRetorno = tipoRetorno;
  }


  //@SuppressWarnings("unchecked")
  public <T> T obtenerValor(Hecho hecho) {
    return (T) extractor.apply(hecho);
  }

}
