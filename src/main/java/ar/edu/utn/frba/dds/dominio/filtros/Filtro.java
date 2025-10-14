package ar.edu.utn.frba.dds.dominio.filtros;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;


@Entity
@Table(name = "filtro")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_filtro", discriminatorType = DiscriminatorType.STRING)
public abstract class Filtro {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "filtro_Id")
  private Long id;

  public abstract boolean cumpleFiltro(Hecho hecho);

  public abstract Map<String, String>  convertirfiltroAmap();

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
