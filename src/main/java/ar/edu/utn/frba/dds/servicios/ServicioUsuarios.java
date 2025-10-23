package ar.edu.utn.frba.dds.servicios;

import ar.edu.utn.frba.dds.infraestructura.repositorios.RepositorioUsuarios;
import ar.edu.utn.frba.dds.modelo.Rol;
import ar.edu.utn.frba.dds.modelo.Usuario;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.Optional;

public class ServicioUsuarios {
  private final RepositorioUsuarios repositorioUsuarios;

  public ServicioUsuarios(RepositorioUsuarios repositorioUsuarios) {
    this.repositorioUsuarios = repositorioUsuarios;
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
    Optional<Usuario> usuario = repositorioUsuarios.buscarPorNombre(nombre.trim().toLowerCase());
    return usuario.orElse(null);
  }

  public void registrarUsuario(String nombre, String password, Rol rol) {
    String hash = DigestUtils.sha256Hex(password);
    Usuario usuario = new Usuario(nombre, password, rol); // tu constructor ya hace el hash
    //repositorioUsuarios.guardar(usuario);
  }

}
