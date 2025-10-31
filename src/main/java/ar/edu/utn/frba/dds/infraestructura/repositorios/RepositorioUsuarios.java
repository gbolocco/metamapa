package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.modelo.Usuario;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.util.List;
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


  public List<Usuario> getUsuarios() {
    return entityManager().createQuery("from Usuario").getResultList();
  }

  public Usuario buscarPorId(Long id) {
    return entityManager().createQuery("from Usuario u where id = u.id",Usuario.class).getSingleResult();
  }

  public void actualizarUsuario(Usuario usuario) {
    entityManager().merge(usuario);
  }
}
