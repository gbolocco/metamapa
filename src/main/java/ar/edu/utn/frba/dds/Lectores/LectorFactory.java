package ar.edu.utn.frba.dds.Lectores;

public class LectorFactory {
  public static Lector crearLector(TipoArchivo tipo) {
    return switch (tipo) {
      case CSV -> new LectorCSV();
      // otros casos según lo que vayas agregando
    };
  }
}
