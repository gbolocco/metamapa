package ar.edu.utn.frba.dds.compartido.servicios.agregador;

import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;

public class ServicioDeAgregacion {
  private List<Fuente> fuentes;

  public void agregarFuente(Fuente fuente){
    this.fuentes.add(fuente);
  }

  public ResultadoConsenso ConsensuarHechoSegunAlgoritmo(Hecho hecho){
    int total = fuentes.size();
    int coincidencias = 0;
    List<String> fuentesCoincidentes = new ArrayList<>();

    for (Fuente fuente : fuentes) {
      List<Hecho> hechos = fuente.obtenerHechos();
      for (Hecho otro : hechos) {
        if (sonEquivalentes(hecho, otro)) {
          coincidencias++;
          fuentesCoincidentes.add(fuente.getNombre());
          break;
        }
      }
    }

    return new ResultadoConsenso(total, coincidencias, fuentesCoincidentes);
  }

  private boolean sonEquivalentes(Hecho h1, Hecho h2) {
    return h1.getTitulo().equalsIgnoreCase(h2.getTitulo())
        && h1.getAtributosClave().equals(h2.getAtributosClave());
  }

  public List<Hecho> combinarHechosDesdeTodasLasFuentes() {

  }
}
