package ar.edu.utn.frba.dds.dominio.estadisticas;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import jdk.jfr.Enabled;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_estadistica", discriminatorType = DiscriminatorType.STRING)
public abstract class Estadistica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "idEstadistica")
    Long id;

    @Column (name = "publica")
    public boolean publica;

    @Column (name = "categoria")
    public String categoria;

    @Column (name="respuesta")
    public String respuesta = null;

    @Column (name = "fechaCalculo")
    public LocalDateTime fechaDeCalculo;

    public Estadistica() {
    }

    public Estadistica(boolean publica) {
        this.publica = publica;
    }

    public abstract String calcular(List<Hecho> hechos);

    public boolean fueCalculada() {
      return  this.respuesta != null;
    }

    public boolean getPublica() {
      return this.publica;
    }

}
