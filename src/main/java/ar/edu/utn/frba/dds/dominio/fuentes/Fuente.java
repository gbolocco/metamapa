package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;

public interface Fuente {
  public List<Hecho> cargarHechos();
}
