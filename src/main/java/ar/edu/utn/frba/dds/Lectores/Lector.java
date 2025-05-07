package ar.edu.utn.frba.dds.Lectores;

import ar.edu.utn.frba.dds.hecho.Hecho;
import java.util.List;

public interface Lector {
  List<Hecho> leer(String rutaArchivo);
}
