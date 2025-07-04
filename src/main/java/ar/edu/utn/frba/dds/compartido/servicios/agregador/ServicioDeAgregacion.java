package ar.edu.utn.frba.dds.compartido.servicios.agregador;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

import java.util.ArrayList;
import java.util.List;

public class ServicioDeAgregacion {
    private List<Fuente> fuentes = new ArrayList<>();

    public void agregarFuente(Fuente fuente) {
        this.fuentes.add(fuente);
    }

    public List<Fuente> getFuentes() {
        return fuentes;
    }

    public ResultadoConsenso ConsensuarHechoSegunAlgoritmo(Hecho hecho, List<Filtro> criteriosDePertenencia) {
        int total = fuentes.size();
        int coincidencias = 0;
        List<Fuente> fuentesCoincidentes = new ArrayList<>();
        List<TipoFuente> tipoFuentesCoincidentes = new ArrayList<>();

        for (Fuente fuente : fuentes) {
            List<Hecho> hechos = fuente.obtenerHechos(criteriosDePertenencia);/*Le paso los criterios de pertenencia
      de la fuente para q solo me traiga los hechos que pueden llegar a coinicidir con mi hecho*/
            for (Hecho otro : hechos) {
                if (sonEquivalentes(hecho, otro)) {
                    coincidencias++;

                    fuentesCoincidentes.add(fuente);
                    tipoFuentesCoincidentes.add(fuente.getTipoFuente());
                    break;
                }
            }
        }

        return new ResultadoConsenso(total, coincidencias, fuentesCoincidentes, tipoFuentesCoincidentes);
    }

    private boolean sonEquivalentes(Hecho h1, Hecho h2) {
        return h1.getTitulo().equalsIgnoreCase(h2.getTitulo())
                && h1.getAtributosClave().equals(h2.getAtributosClave());
    }

    public List<Hecho> combinarHechosDesdeTodasLasFuentes(List<Filtro> criteriosDePertenencia) {
        return this.fuentes
                .stream()
                .flatMap(fuente -> fuente.obtenerHechos(criteriosDePertenencia).stream())
                .toList();
    }
}
