package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("fuenteDemo")
public class FuenteDemo extends Fuente {
  @Transient
  private Conexion conexion;
  private String url = "URL";

  public FuenteDemo(Conexion conexion) {
    this.conexion = conexion;
  }

  public FuenteDemo() {

  }

  public void incorporarNuevosHechosSiLosHay(LocalDateTime fecha) {
    Map<String, Object> datosHecho;
    while ((datosHecho = conexion.siguienteHecho(this.url, fecha)) != null) {
      Hecho hecho = this.crearHechoDesdeMap(datosHecho);
      HechosRepositoryMemory.getInstancia().cargarHecho(hecho);
    }
    /*if (conexion.siguienteHecho(this.url, fecha) == null) {
      throw new RuntimeException("No hay nuevos hechos");
    }*/
  }

  public Hecho crearHechoDesdeMap(Map<String, Object> datosHecho) {
    String titulo = (String) datosHecho.getOrDefault("titulo", "");
    String descripcion = (String) datosHecho.getOrDefault("descripcion", "");
    String categoria = (String) datosHecho.getOrDefault("categoria", "");
    Double latitud = (Double) datosHecho.get("latitud");
    Double longitud = (Double) datosHecho.get("longitud");
    LocalDateTime fechaAcontecimiento = (LocalDateTime) datosHecho.get("fechaAcontecimiento");
    LocalDateTime fechaDeCarga = (LocalDateTime) datosHecho.get("fechaDeCarga");



    Ubicacion ubicacion = new Ubicacion(latitud, longitud);
    return new Hecho(
        titulo,
        descripcion,
        categoria,
        ubicacion,
        fechaAcontecimiento,
        fechaDeCarga,
        OrigenHecho.FUENTE_PROXY
    );
  }

  private boolean cumpleCriterio(Hecho hecho, List<Filtro> criterios) {
    return criterios.stream().allMatch(f -> f.cumpleFiltro(hecho));
  }

  @Override
  public List<Hecho> obtenerHechos(List<Filtro> criterios) {
    List<Hecho> hechos = HechosRepositoryMemory.getInstancia().mostrarHechos()
        .stream()
        .filter(h -> h.getOrigenHecho() == OrigenHecho.FUENTE_PROXY).toList();

    return hechos.stream()
        .filter(hecho -> cumpleCriterio(hecho, criterios))
        .collect(Collectors.toList());
  }

  @Override
  public TipoFuente getTipoFuente() {
    return TipoFuente.FUENTE_PROXY;
  }
}



