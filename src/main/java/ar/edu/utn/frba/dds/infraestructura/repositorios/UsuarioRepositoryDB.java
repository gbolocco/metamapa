package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.usuario.Contribuyente;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class UsuarioRepositoryDB implements WithSimplePersistenceUnit {

  private static final UsuarioRepositoryDB instance = new UsuarioRepositoryDB();

  public static UsuarioRepositoryDB getInstance() {
    return instance;
  }

  public void agregar(Contribuyente contribuyente) {
    entityManager().persist(contribuyente);
  }

  public Contribuyente buscarSolicitudPorId(Long id) {
    return entityManager().find(Contribuyente.class, id);
  }

  public List<Contribuyente> buscarTodos() {
    return entityManager()
        .createQuery("from Contribuyente", Contribuyente.class).getResultList();
  }

}
