package ar.edu.utn.frba.dds.servicios;

import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepository;

import java.util.List;

public class ServicioFuentes {
  private final FuentesRepository fuentesRepository;


  public ServicioFuentes(FuentesRepository fuentesRepositoryMemory) {
    this.fuentesRepository = fuentesRepositoryMemory;
  }

  public List<Fuente> getFuentes() {
    return fuentesRepository.getFuentes();
  }

  public Fuente buscar(Long fuenteId) {
    return fuentesRepository.buscar(fuenteId);
  }

  public void guardarFuente(Fuente fuente) {
    fuentesRepository.agregarFuente(fuente);
  }
}
