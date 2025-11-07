package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.modelo.Usuario;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;


public class UsuariosRepository implements SimplePersistenceTest {
  public static UsuariosRepository INSTANCE = new UsuariosRepository();

  public void registrar(Usuario usuario) {
    entityManager().persist(usuario);
  }

  public long contar() {
    return entityManager().createQuery("from Usuario").getResultStream().count();
  }

  public Optional<Usuario> buscarPorNombre(String nombre) {
    try {
      System.out.println("DEBUG REPO: Buscando con nombre: '" + nombre.toLowerCase() + "'");
      Usuario usuario = entityManager()
          .createQuery("FROM Usuario WHERE LOWER(nombre) = :nombre", Usuario.class)
          .setParameter("nombre", nombre.toLowerCase())
          .getSingleResult();
      System.out.println("DEBUG REPO: Usuario encontrado: " + usuario.getNombre());
      return Optional.of(usuario);
    } catch (NoResultException e) {
      System.out.println("DEBUG REPO: NoResultException - usuario no encontrado");
      return Optional.empty();
    }
  }


  public List<Usuario> getUsuarios() {
    return entityManager().createQuery("from Usuario").getResultList();
  }

  public Usuario buscarPorId(Long id) {
    return entityManager().find(Usuario.class, id);
  }

  public void actualizarUsuario(Usuario usuario) {
    entityManager().getTransaction().begin();
    entityManager().merge(usuario);
    entityManager().getTransaction().commit();
  }
  
  public void agregarUsuario(Usuario usuario) {
    entityManager().getTransaction().begin();
    entityManager().persist(usuario);
    entityManager().getTransaction().commit();
  }

}
