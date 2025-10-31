package ar.edu.utn.frba.dds.controladores;

import ar.edu.utn.frba.dds.modelo.Rol;
import ar.edu.utn.frba.dds.modelo.Usuario;
import ar.edu.utn.frba.dds.servicios.ServicioUsuarios;
import io.javalin.http.Context;

public class UserController {
  private ServicioUsuarios servicioUsuarios;

  public UserController(ServicioUsuarios servicioUsuarios) {
    this.servicioUsuarios = servicioUsuarios;
  }

  public void actualizarRolUsuario(Context ctx) {
    String rol = ctx.pathParam("rol");
    Long id = Long.parseLong(ctx.pathParam("id"));
    Usuario usuario = servicioUsuarios.buscarPorId(id);
    if (rol == null) {
      return;
    }else if (rol.equals("admin")) {
      usuario.setRol(Rol.ADMIN);
    }else if (rol.equals("user")) {
      usuario.setRol(Rol.USER);
    }
    servicioUsuarios.actualizarUsuario(usuario);
    ctx.status(200);
  }
}
