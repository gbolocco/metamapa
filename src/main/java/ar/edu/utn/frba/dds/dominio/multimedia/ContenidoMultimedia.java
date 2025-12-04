package ar.edu.utn.frba.dds.dominio.multimedia;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ContenidoMultimedia {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Lob
  private byte[] datosArchivo;

  private String urlArchivo; // El String de tu lista

  // Campo para saber si es imagen o video
  @Enumerated(EnumType.STRING)
  private TipoContenido tipoContenido;

  private String tipoMime;

  public ContenidoMultimedia(String url, TipoContenido tipo) {
    this.urlArchivo = url;
    this.tipoContenido = tipo;
  }

  public ContenidoMultimedia() {

  }

  @ManyToOne
  @JsonIgnore // <-- ¡Añade esto!
  private Hecho hecho; // Referencia a la entidad Hecho a la que pertenece
}