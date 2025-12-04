package ar.edu.utn.frba.dds.servicios;

import ar.edu.utn.frba.dds.dominio.multimedia.ContenidoMultimedia;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.NoResultException;

public class ServicioMultimedia implements WithSimplePersistenceUnit {
  public ContenidoMultimedia buscarPorId(Long id) {
    try {
      return entityManager().find(ContenidoMultimedia.class, id);
    } catch (NoResultException e) {
      return null;
    }
  }
}
