package ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso;

import ar.edu.utn.frba.dds.compartido.servicios.agregador.ResultadoConsenso;
import ar.edu.utn.frba.dds.compartido.servicios.agregador.ServicioDeAgregacion;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

import java.util.List;

public class MayoriaSimple implements AlgoritmoConsenso{
  private static final ServicioDeAgregacion servicioDeAgregacion = new ServicioDeAgregacion();

  public Boolean estaConsensuado(Hecho hecho, List<Filtro>criterioDePertenencia){
    ResultadoConsenso resultado =this.servicioDeAgregacion.ConsensuarHechoSegunAlgoritmo(hecho,criterioDePertenencia);
    return resultado.getCantidadCoincidencias() >= resultado.getCantidadTotalFuentes()/2;
  }
}
