package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;

public class HechosRepositoryMemory {

  private HechosRepositoryMemory() {
  }

  private static final HechosRepositoryMemory instance = new HechosRepositoryMemory();

  private final List<Hecho> hechos = new ArrayList<>();

  public static HechosRepositoryMemory getInstancia() {
    return instance;
  }

  public void cargarHecho(Hecho hecho) {
    this.hechos.add(hecho);
  }

  public List<Hecho> mostrarHechos() {
    return new ArrayList<>(this.hechos);
  }

  public void modificarHecho (Hecho hechoAModificar,Hecho hechoModificado) {
    if(!hechos.contains(hechoAModificar)) {
      throw new IllegalArgumentException("El hecho no existe en la fuenta dinamica");
    }
    this.hechos.set(hechos.indexOf(hechoAModificar), hechoModificado);
  }

}
