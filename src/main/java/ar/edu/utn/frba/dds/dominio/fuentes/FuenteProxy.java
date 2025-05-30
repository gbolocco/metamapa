package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;

public class FuenteProxy implements Fuente {
  public List<Hecho> cargarHechos() {
    return new ArrayList<>();
  }
}
