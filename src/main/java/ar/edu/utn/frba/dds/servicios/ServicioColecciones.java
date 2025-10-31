package ar.edu.utn.frba.dds.servicios;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepository;
import java.util.List;

public class ServicioColecciones {
  private ColeccionRepository coleccionRepository;

  public ServicioColecciones(ColeccionRepository coleccionRepository) {
    this.coleccionRepository = coleccionRepository;
  }

  public void guardarColeccion(Coleccion coleccion){
    this.coleccionRepository.agregarColeccion(coleccion);
  }

  public List<Coleccion> obtenerColecciones(){
    return coleccionRepository.mostrarColecciones();
  }

  public Coleccion findById(Long id){
    return coleccionRepository.buscarColeccionPorId(id);
  }

}
