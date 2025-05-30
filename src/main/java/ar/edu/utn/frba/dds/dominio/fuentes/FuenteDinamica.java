package ar.edu.utn.frba.dds.dominio.fuentes;


import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosYSolicitudesRepository;
import java.util.List;

public class FuenteDinamica implements Fuente {
  public List<Hecho> cargarHechos() {
    return HechosYSolicitudesRepository.getInstancia().mostrarHechos();
  }
}
