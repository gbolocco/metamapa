package ar.edu.utn.frba.dds.lectores;

public class LectorFactory {
  public static Lector crearLector(TipoArchivo tipo) {
    return switch (tipo) {
      case CSV -> new LectorCsv();
      // otros casos según lo que vayas agregando
    };
  }
}
