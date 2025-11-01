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
    try{
      Long id = Long.parseLong(ctx.pathParam("id"));
      System.out.println(id);
      Usuario usuario = servicioUsuarios.buscarPorId(id);

      RolUpdateRequest requestBody = ctx.bodyAsClass(RolUpdateRequest.class);
      String rol = requestBody.getRol();

      System.out.println("ID: " + id + ", Nuevo Rol: " + rol);


      if (rol == null) {
        return;
      }else if (rol.equals("ADMIN")) {
        usuario.setRol(Rol.ADMIN);
      }else if (rol.equals("USER")) {
        usuario.setRol(Rol.USER);
      }
      servicioUsuarios.actualizarUsuario(usuario);
      ctx.status(200);
    }catch(Exception e){
      e.printStackTrace();
      System.out.println("Error al actualizar rol");
      ctx.status(500).result("Error interno del servidor: " + e.getMessage());
    }
  }

  public void mostrarMisSolicitudes(Context ctx) {
    try {
      Long userId = ctx.sessionAttribute("user_id");
      if (userId == null) {
        ctx.redirect("/login");
        return;
      }
      
      java.util.Map<String, Object> model = new java.util.HashMap<>();
      model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
      model.put("rol", ctx.sessionAttribute("rol"));
      model.put("user_name", ctx.sessionAttribute("user_name"));
      model.put("user_id", userId);
      
      ctx.render("mis-solicitudes.hbs", model);
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).result("Error interno del servidor: " + e.getMessage());
    }
  }

  public static class RolUpdateRequest {
    private String rol;

    public RolUpdateRequest() {}

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
  }
}
