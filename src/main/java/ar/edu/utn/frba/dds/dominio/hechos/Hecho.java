package ar.edu.utn.frba.dds.dominio.hechos;

import ar.edu.utn.frba.dds.compartido.AppLogger;
import ar.edu.utn.frba.dds.dominio.multimedia.ContenidoMultimedia;
import ar.edu.utn.frba.dds.dominio.multimedia.TipoContenido;
import ar.edu.utn.frba.dds.dominio.usuario.Contribuyente;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.slf4j.Logger;

@Getter
@Setter
@Entity
@Indexed
public class Hecho {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false, name = "hecho_id")
  private Long id;

  @FullTextField(analyzer = "standard")
  @Column(name = "titulo")
  private String titulo;

  @FullTextField(analyzer = "standard")
  @Column(name = "descripcion")
  private String descripcion;

  private String categoria;

  @Embedded
  private Ubicacion ubicacion;


  @Column(columnDefinition = "DATE")
  private LocalDateTime fechaAcontecimiento;

  @Column(columnDefinition = "DATE")
  private LocalDateTime fechaDeCarga;

  @Enumerated(EnumType.STRING)
  private OrigenHecho origenHecho;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @OneToMany(mappedBy = "hecho", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ContenidoMultimedia> contenidoMultimedia = new ArrayList<>();


  @Setter
  @Enumerated(EnumType.STRING)
  private EstadoHecho estadoHecho;
  private static final Logger logger = AppLogger.getLogger(Hecho.class);

  @ManyToOne
  private Contribuyente contribuyente;

  public Hecho() {

  }


  public Hecho(
      String titulo,
      String descripcion,
      String categoria,
      Ubicacion ubicacion,
      LocalDateTime fechaAcontecimiento,
      LocalDateTime fechaDeCarga,
      OrigenHecho origenHecho
  ) {
    /*
    Validacion.validarStringNoVacio(titulo, "título");
    Validacion.validarNoNulo(ubicacion, "ubicacion");
    Validacion.validarNoNulo(fechaAcontecimiento, "fechaAcontecimiento");
    Validacion.validarNoNulo(fechaDeCarga, "fechaDeCarga");
    Validacion.validarNoNulo(origenHecho, "origenHecho");*/
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.ubicacion = ubicacion;
    this.fechaAcontecimiento = fechaAcontecimiento;
    this.fechaDeCarga = fechaDeCarga;
    this.origenHecho = Objects.requireNonNull(origenHecho, "origenHecho no puede ser nulo");
    this.estadoHecho = EstadoHecho.VISUALIZABLE;
    this.contribuyente = null;
  }

  public OrigenHecho getOrigenHecho() {
    return OrigenHecho.valueOf(this.origenHecho.name());
  }

  public Map<String, Object> getAtributosClave() {
    Map<String, Object> resumen = new HashMap<>();
    resumen.put("descripcion", this.descripcion);
    resumen.put("categoria", this.categoria);
    resumen.put("ubicacion", this.ubicacion);
    resumen.put("fechaAcontecimiento", this.fechaAcontecimiento);
    resumen.put("fechaDeCarga", this.fechaDeCarga);
    resumen.put("origenHecho", this.origenHecho);
    return resumen;
  }


  public void imprimirHecho() {
    logger.info("Título: {}", this.titulo);
    logger.info("Descripción: {}", this.descripcion);
    logger.info("Categoría: {}", this.categoria);
    logger.info("Ubicación: {}", this.ubicacion);
    logger.info("Fecha del hecho: {}", this.fechaAcontecimiento);
    logger.info("Fecha de carga: {}", this.fechaDeCarga);
    logger.info("Origen: {}", this.origenHecho);
    logger.info("-------------------------------------------");

  }


  public void addContenidoMultimedia(String url, TipoContenido tipo) {
    ContenidoMultimedia nuevoContenido = new ContenidoMultimedia(url,tipo);
    nuevoContenido.setHecho(this);
    this.contenidoMultimedia.add(nuevoContenido);
  }



  public List<String> getUrlsMultimedia() {
    List<String> urls = this.getContenidoMultimedia().stream()
        .map(ContenidoMultimedia::getUrlArchivo)
        .collect(Collectors.toList());
    return new ArrayList<>(urls);
  }

}


