package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "fuente") // Mapped to a single table named 'fuentes'
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_fuente", discriminatorType = DiscriminatorType.STRING)
public abstract class  Fuente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false, name = "id_Fuente")
  private Long id;



  public abstract List<Hecho> obtenerHechos(List<Filtro> criterios);


  public abstract TipoFuente getTipoFuente();

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
