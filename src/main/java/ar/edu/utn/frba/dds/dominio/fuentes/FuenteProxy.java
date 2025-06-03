package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;

public abstract class FuenteProxy {

  public abstract List<Hecho> cargarHechos();
}
