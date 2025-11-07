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
    if (tipoFuente == null || tipoFuente.isBlank()) {
      throw new IllegalArgumentException("tipoFuente es obligatorio");
    }

    final String tipo = tipoFuente.trim().toLowerCase(); // normalizamos

    // 1) Parsear componentes a List<Long>
    List<Long> idsFuentes = parsearComponentes(componentes);

    // 2) Resolver entidades componentes
    List<Fuente> fuentes = new ArrayList<>();
    for (Long id : idsFuentes) {
      Fuente comp = fuentesRepository.buscar(id);
      if (comp == null) {
        throw new IllegalArgumentException("Componente con id=" + id + " no existe");
      }
      fuentes.add(comp);
    }

    // 3) Construir la fuente según el tipo
    Fuente fuente;
    switch (tipo) {
      case "estatica" -> {
        if (url == null || url.isBlank()) {
          throw new IllegalArgumentException("URL es obligatoria para fuente estática");
        }
        fuente = new FuenteEstatica(url, new LectorCsv());
      }
      case "metamapa" -> {
        if (url == null || url.isBlank()) {
          throw new IllegalArgumentException("URL es obligatoria para fuente metamapa");
        }
        fuente = new FuenteMetaMapa(new FuenteMetaMapaAdapter(url));
      }
      case "fuentedemo", "demo" -> {
        fuente = new FuenteDemo();
      }
      case "fuentedinamica", "dinamica", "fuente_dinamica" -> {
        // sin URL
        fuente = new FuenteDinamica();
      }
      case "fuenteagregadora", "agregadora", "fuente_agregadora" -> {
        if (fuentes.isEmpty()) {
          throw new IllegalArgumentException("La fuente agregadora requiere al menos un componente");
        }
        fuente = new FuenteAgregadora(fuentes);
      }
      default -> throw new IllegalArgumentException("tipoFuente inválido: " + tipoFuente);
    }

    // 4) Persistir (solo si NO es null)
    fuentesRepository.agregarFuente(fuente);
    return fuente;
  }

  private static List<Long> parsearComponentes(String componentes) {
    if (componentes == null || componentes.isBlank()) return List.of();
    // elimina corchetes y espacios: "[1, 2]" -> "1,2"
    String limpio = componentes.replaceAll("[\\[\\]\\s]", "");
    if (limpio.isEmpty()) return List.of();

    String[] tokens = limpio.split(",");
    List<Long> ids = new ArrayList<>(tokens.length);
    for (String t : tokens) {
      if (t.isEmpty()) continue;
      try { ids.add(Long.parseLong(t)); }
      catch (NumberFormatException e) {
        throw new IllegalArgumentException("ID de componente inválido: " + t);
      }
    }
    return ids;
  }


}
