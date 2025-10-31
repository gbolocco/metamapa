package ar.edu.utn.frba.dds.servicios;

import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepositoryMemory;
import java.util.List;

public class ServicioFuentes {
  private final FuentesRepository fuentesRepositoryMemory;


  public ServicioFuentes(FuentesRepository fuentesRepositoryMemory) {
    this.fuentesRepositoryMemory = fuentesRepositoryMemory;
  }

  public List<Fuente> getFuentes() {
    return fuentesRepositoryMemory.getFuentes();
  }
}
