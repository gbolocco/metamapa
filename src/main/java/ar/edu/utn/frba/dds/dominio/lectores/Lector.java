package ar.edu.utn.frba.dds.dominio.lectores;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;

public interface Lector {
  List<Hecho> leer(String rutaArchivo);
}
