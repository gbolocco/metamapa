package ar.edu.utn.frba.dds.lectores;

import ar.edu.utn.frba.dds.validaciones.Validacion;

public class Fuente {
  private TipoArchivo tipoArchivo;
  private String pathArchivo;

  public Fuente(TipoArchivo tipoArchivo, String pathArchivo) {
    Validacion.validarNoNulo(tipoArchivo, "tipoArchivo");
    Validacion.validarStringNoVacio(pathArchivo, "pathArchivo");
    this.tipoArchivo = tipoArchivo;
    this.pathArchivo = pathArchivo;

  }

  public String getPathArchivo() {
    return pathArchivo;
  }

  public TipoArchivo getTipoArchivo() {
    return tipoArchivo;
  }
}


