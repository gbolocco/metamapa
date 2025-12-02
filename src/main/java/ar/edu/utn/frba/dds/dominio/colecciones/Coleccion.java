package ar.edu.utn.frba.dds.dominio.colecciones;

import ar.edu.utn.frba.dds.compartido.AppLogger;
import ar.edu.utn.frba.dds.compartido.validaciones.Validacion;
import ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso.Absoluta;
import ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso.AlgoritmoConsenso;
import ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso.MayoriaSimple;
import ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso.MultiplesMenciones;
import ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso.TipoConsenso;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.filtros.TipoCombinacion;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.hechos.EstadoHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepository;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.slf4j.Logger;

@Getter
@Setter
@Entity
@DynamicUpdate
public class Coleccion  {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false, name = "id_coleccion")
  private Long id;


  private String titulo;

  private String descripcion;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Filtro> criteriosDePertenencia;


  @ManyToOne(targetEntity = Fuente.class, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "fuente_id")
  private Fuente fuente;



  @Transient
  private List<Hecho> hechosConsensuados;


  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_consenso", nullable = true)
  private TipoConsenso tipoConsenso;

  private String handle;

  @Column(columnDefinition = "DATE")
  private LocalDateTime fechaDeCreacion;

  private static final Logger logger = AppLogger.getLogger(Coleccion.class);


  public Coleccion(
        String titulo,
        String descripcion,
        List<Filtro> criteriosDePertenencia,
        Fuente fuente,
        String handle,
        TipoConsenso tipoConsenso
  ) {
    Validacion.validarStringNoVacio(titulo, "título");
    Validacion.validarNoNulo(descripcion, "descripción"); //descripcion puede ser nula?
    Validacion.validarListaNoNula(
            criteriosDePertenencia,
            "criteriosDePertenencia"
    );
    Validacion.validarStringAlfanumericoSinEspacios(handle);
    Validacion.validarHandleValorUnico(handle);
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.criteriosDePertenencia = new ArrayList<>(criteriosDePertenencia);
    this.fuente = fuente;
    this.fechaDeCreacion = LocalDateTime.now();
    this.handle = handle;
    this.tipoConsenso = tipoConsenso;
  }

  public Coleccion() {
  }


  // MODOS DE VISUALIZACION

  public List<Hecho> navegarHechos(boolean navegacionCurada) {
    if (navegacionCurada) {
      return this.mostrarHechosConsensuados();
    }
    return this.mostrarHechos();
  }

  public List<Hecho> mostrarHechos() {
    return new ArrayList<>(fuente.obtenerHechos(criteriosDePertenencia));
  }

  public List<Hecho> mostrarHechosConsensuados() {
    if (this.hechosConsensuados == null) {
      this.hechosConsensuados = this.consensuarHechos();
    }
    return this.hechosConsensuados;
  }

  public List<Hecho> consensuarHechos() {
    return this.getAlgoritmo().hechosConsensuados(
        this.mostrarHechos(), this.criteriosDePertenencia).stream()
        .filter(hecho -> hecho.getEstadoHecho().equals(EstadoHecho.VISUALIZABLE))
        .toList();
  }
    
  //metodos relacionados a los hechos

  public boolean cumpleFiltros(Hecho hecho, List<Filtro> filtros, TipoCombinacion tipoCombinacion) {
    if (tipoCombinacion == TipoCombinacion.AND) {
      return filtros.stream().allMatch(filtro -> filtro.cumpleFiltro(hecho));
    } else {
      return filtros.stream().anyMatch(filtro -> filtro.cumpleFiltro(hecho));
    }
  }

  public List<Hecho> filtrarHechos(List<Filtro> filtros, TipoCombinacion tipoCombinacion, boolean navegacionCurada) {
    return this.navegarHechos(navegacionCurada)
        .stream()
        .filter(h -> cumpleFiltros(h, filtros, tipoCombinacion))
        .collect(Collectors.toList());
  }


  public AlgoritmoConsenso getAlgoritmo() {
    return switch (tipoConsenso) {
      case ABSOLUTA -> new Absoluta();
      case MAYORIA_SIMPLE -> new MayoriaSimple();
      case MULTIPLES_MENCIONES -> new MultiplesMenciones();
    };
  }
  /*
  public void imprimirColeccion(List<Filtro> filtros, TipoCombinacion tipoCombinacion) {
    logger.info("Coleccion: {}", this.titulo);
    logger.info("Descripcion: {}", this.descripcion);
    logger.info("Criterios de pertenencia: ");
    this.criteriosDePertenencia.forEach(filtro -> {
      logger.info(filtro.toString());
    });

    logger.info("Hechos: ");
    imprimirHechosDeColeccion(filtros, tipoCombinacion);
  }


  public void imprimirHechosDeColeccion(List<Filtro> filtros, TipoCombinacion tipoCombinacion) {
    if (filtros != null) {
      List<Hecho> hechosFiltrados = this.filtrarHechos(filtros, tipoCombinacion);
      logger.info("Cantidad de hechos filtrados: {}", hechosFiltrados.size());
      hechosFiltrados.forEach(Hecho::imprimirHecho);

    } else {
      logger.info("Cantidad de hechos: {}", this.mostrarHechos().size());
      this.mostrarHechos().forEach(Hecho::imprimirHecho);
    }
  }




  public Fuente getFuente() {
    return fuente;
  }

  public AlgoritmoConsenso getAlgoritmoConsenso() {
    return algoritmoConsenso;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

*/


}
