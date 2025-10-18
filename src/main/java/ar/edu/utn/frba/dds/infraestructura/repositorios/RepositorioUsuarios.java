package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.modelo.Usuario;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.apache.commons.codec.digest.DigestUtils;

public class RepositorioUsuarios implements SimplePersistenceTest {
  public static RepositorioUsuarios INSTANCE = new RepositorioUsuarios();

  public void registrar(Usuario usuarie) {
    entityManager().persist(usuarie);
  }

  public long contar() {
    return entityManager().createQuery("from Usuario").getResultStream().count();
  }
  public Usuario buscar(String nombre, String contrasenia) {
    return entityManager()
        .createQuery("from Usuario where nombre = :nombre and hashPassorwd = :hashPassorwd"
            , Usuario.class)
        .setParameter("nombre", nombre)
        .setParameter("hashPassorwd", DigestUtils.sha256Hex(contrasenia))
        .getResultList()
        .get(0);
  }
}
