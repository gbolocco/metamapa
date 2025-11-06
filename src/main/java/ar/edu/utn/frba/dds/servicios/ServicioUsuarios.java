package ar.edu.utn.frba.dds.servicios;

import ar.edu.utn.frba.dds.infraestructura.repositorios.UsuariosTableRepositoryDB;
import ar.edu.utn.frba.dds.modelo.Rol;
import ar.edu.utn.frba.dds.modelo.Usuario;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.Optional;

public class ServicioUsuarios {
  private final UsuariosTableRepositoryDB usuariosTableRepositoryDB;

  public ServicioUsuarios(UsuariosTableRepositoryDB usuariosTableRepositoryDB) {
    this.usuariosTableRepositoryDB = usuariosTableRepositoryDB;
  }

  public Usuario autenticar(String nombre, String password) {
    Usuario usuario = buscarPorNombre(nombre);
    if (usuario == null) {
      return null;
    }

    String hashIngresado = DigestUtils.sha256Hex(password);

    if (usuario.getHashPassword().equals(hashIngresado)) {
      return usuario;
    }

    return null;
  }

  public Usuario buscarPorNombre(String nombre) {
    if (nombre == null || nombre.isBlank()) {
      return null;
    }
    Optional<Usuario> usuario = usuariosTableRepositoryDB.buscarPorNombre(nombre.trim());
    return usuario.orElse(null);
  }

  public void registrarUsuario(String nombre, String password, Rol rol) {
    String hash = DigestUtils.sha256Hex(password);
    new Usuario(nombre, hash, rol);
  }

  public List<Usuario> getUsuarios() {
    return usuariosTableRepositoryDB.getUsuarios();
  }

  public Usuario buscarPorId(Long id) {
    return usuariosTableRepositoryDB.buscarPorId(id);
  }

  public void actualizarUsuario(Usuario usuario) {
    usuariosTableRepositoryDB.actualizarUsuario(usuario);
  }
}
