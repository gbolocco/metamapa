package ar.edu.utn.frba.dds.servicios;

import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteAgregadora;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDemo;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteMetaMapa;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteMetaMapaAdapter;
import ar.edu.utn.frba.dds.dominio.lectores.LectorCsv;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepositoryMemory;
import java.util.ArrayList;
import java.util.Arrays;
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

  public void eliminar(Long id) {
    try {
      fuentesRepository.eliminar(id);
    }catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Fuente crearFuente(String url, String tipoFuente, String componentes) {
    Fuente fuente = null;
    List<Long> idsFuentes = new ArrayList<>();
    if (componentes != null && !componentes.isEmpty()) {
      idsFuentes = Arrays.stream(componentes.split(","))
          .map(String::trim)
          .filter(s -> !s.isEmpty())
          .map(Long::parseLong)
          .toList();
    }

    List<Fuente> fuentes = new ArrayList<>();
    for (Long id : idsFuentes) {
      fuentes.add( fuentesRepository.buscar(id));
    }

    if (tipoFuente.equals("estatica")) {
          fuente = new FuenteEstatica(url,new LectorCsv());
    }else if (tipoFuente.equals("metamapa")) {
          fuente = new FuenteMetaMapa(new FuenteMetaMapaAdapter(url));
    }else if (tipoFuente.equals("fuenteDemo")) {
          fuente = new FuenteDemo();
    }else if (tipoFuente.equals("fuenteDinamica")) {
          fuente = new FuenteDinamica();
    }else if (tipoFuente.equals("fuenteAgregadora") && !fuentes.isEmpty()) {
          fuente = new FuenteAgregadora(fuentes);
    }

    fuentesRepository.agregarFuente(fuente);
    return fuente;
  }
}
