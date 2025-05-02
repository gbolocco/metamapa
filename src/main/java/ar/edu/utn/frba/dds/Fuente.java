package ar.edu.utn.frba.dds;

public class Fuente {
  private TipoArchivo tipoArchivo;
  private String pathArchivo;
  public Fuente(TipoArchivo tipoArchivo,String pathArchivo) {
    this.tipoArchivo = tipoArchivo;
    this.pathArchivo = pathArchivo;

  }
  public String getPathArchivo() {
    return pathArchivo;
  }
}


