package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.modelo.Usuario;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import javax.persistence.NoResultException;
import java.util.Optional;

public class RepositorioUsuarios implements SimplePersistenceTest {
  public static RepositorioUsuarios INSTANCE = new RepositorioUsuarios();

  public void registrar(Usuario usuario) {
    entityManager().persist(usuario);
  }

  public long contar() {
    return entityManager().createQuery("from Usuario").getResultStream().count();
  }

  public Optional<Usuario> buscarPorNombre(String nombre) {
    try {
      Usuario usuario = entityManager()
          .createQuery("FROM Usuario WHERE LOWER(nombre) = :nombre", Usuario.class)
          .setParameter("nombre", nombre.toLowerCase())
          .getSingleResult();
      return Optional.of(usuario);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }
}
