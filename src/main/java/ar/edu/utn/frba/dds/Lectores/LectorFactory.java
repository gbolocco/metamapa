package ar.edu.utn.frba.dds.Lectores;

import ar.edu.utn.frba.dds.TipoArchivo;

public class LectorFactory {
  public static Lector crearLector(TipoArchivo tipo) {
    return switch (tipo) {
      case CSV -> new LectorCSV();
      // otros casos según lo que vayas agregando
    };
  }
}
