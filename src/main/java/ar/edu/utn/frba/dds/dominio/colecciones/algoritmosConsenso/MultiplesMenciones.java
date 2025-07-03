  package ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso;

  import ar.edu.utn.frba.dds.compartido.servicios.agregador.ResultadoConsenso;
  import ar.edu.utn.frba.dds.compartido.servicios.agregador.ServicioDeAgregacion;
  import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
  import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
  import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

  import java.util.ArrayList;
  import java.util.Comparator;
  import java.util.List;
  import java.util.stream.Collectors;

  public class MultiplesMenciones implements AlgoritmoConsenso{
    private ServicioDeAgregacion servicioDeAgregacion = new ServicioDeAgregacion();

    public void setServicioDeAgregacion(ServicioDeAgregacion servicioDeAgregacion) {
      this.servicioDeAgregacion = servicioDeAgregacion;
    }

    public List<Fuente> getFuentesNoCoincidentes(List<Fuente> lista1, List<Fuente> lista2){
      List<Fuente> resultado = new ArrayList<>();

      for (Fuente elemento : lista1) {
        if (!lista2.contains(elemento)) {
          resultado.add(elemento);
        }
      }

      for (Fuente elemento : lista2) {
        if (!lista1.contains(elemento)) {
          resultado.add(elemento);
        }
      }

      return resultado;
    }

    public Boolean estaConsensuado(Hecho hecho, List<Filtro> criterioDePertenencia) {
      ResultadoConsenso resultado = this.servicioDeAgregacion
              .ConsensuarHechoSegunAlgoritmo(hecho, criterioDePertenencia);
      return resultado.getCantidadCoincidencias() >= 2
              && this.getFuentesNoCoincidentes(this.servicioDeAgregacion.getFuentes(), resultado.getFuentesCoincidentes())
              .stream()
              .flatMap(fuente -> fuente.obtenerHechos(criterioDePertenencia).stream())
              .noneMatch(hechoActual -> hechosDeMismoTituloYDistintosAtributos(hechoActual, hecho));
    }

  public boolean hechosDeMismoTituloYDistintosAtributos(Hecho h1, Hecho h2) {
    return h1.getTitulo().equalsIgnoreCase(h2.getTitulo())
            && !h1.getAtributosClave().equals(h2.getAtributosClave());
  }
}
