package ar.edu.utn.frba.dds.servicios;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import java.util.List;

public class ServicioHechos {
  private HechosRepository hechosRepository;

  public ServicioHechos(HechosRepository hechosRepository) {
    this.hechosRepository = hechosRepository;
  }

  public List<Hecho> mostrarHechos() {
    return this.hechosRepository.mostrarHechos();
  }

  public Hecho buscar(long id) {
    return this.hechosRepository.buscar(id);
  }

  public void cargarHecho(Hecho hecho) {
    this.hechosRepository.cargarHecho(hecho);
  }


}
