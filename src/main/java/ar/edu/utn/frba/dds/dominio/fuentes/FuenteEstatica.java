package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.lectores.Lector;
import ar.edu.utn.frba.dds.dominio.lectores.LectorCsv;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepository;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import lombok.Getter;

@Entity
@DiscriminatorValue("estatica")
@Getter
public class FuenteEstatica extends Fuente {
  @Transient
  private Lector lector;
  private String rutaArchivo;
  
  public FuenteEstatica() {

  }

  public FuenteEstatica(String rutaArchivo, Lector lector) {
    this.rutaArchivo = rutaArchivo;
    FuentesRepository.getInstancia().agregarFuente(this);
  }

  public List<Hecho> obtenerHechos(List<Filtro> criteriosDePertenencia) {
    LectorCsv lectorCsv = new LectorCsv();
    this.hechos = lectorCsv.leer(rutaArchivo);
    return this.hechos.stream()
        .filter(hecho -> cumpleCriterio(hecho, criteriosDePertenencia))
        .collect(Collectors.toList());
  }

  public void  cargarFuente() {
    this.hechos = lector.leer(rutaArchivo);
  }

  @Override
  public TipoFuente getTipoFuente() {
    return TipoFuente.FUENTE_ESTATICA;
  }

  private boolean cumpleCriterio(Hecho hecho, List<Filtro> criterios) {
    return criterios.stream().allMatch(f -> f.cumpleFiltro(hecho));
  }
}
