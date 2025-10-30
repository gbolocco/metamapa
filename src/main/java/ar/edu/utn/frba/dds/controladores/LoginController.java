package ar.edu.utn.frba.dds.controladores;

import ar.edu.utn.frba.dds.infraestructura.repositorios.RepositorioUsuarios;
import ar.edu.utn.frba.dds.modelo.Rol;
import ar.edu.utn.frba.dds.modelo.Usuario;
import io.javalin.http.Context;
import ar.edu.utn.frba.dds.servicios.ServicioUsuarios;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

  private final ServicioUsuarios servicio;

  public LoginController(ServicioUsuarios servicio) {
    this.servicio = servicio;
  }

  public void login(Context ctx) {
    String nombre = ctx.formParam("nombre");
    String password = ctx.formParam("password");

    if (nombre == null || password == null) {
      ctx.redirect("/login?error=Completa usuario y contraseña");
      return;
    }
    Usuario usuario = servicio.autenticar(nombre, password);
    if (usuario == null) {
      String mensaje = "Usuario o contraseña inválidos";
      ctx.redirect("/login?error=" + URLEncoder.encode(mensaje, StandardCharsets.UTF_8));
      return;
    }
    ctx.sessionAttribute("user_id", usuario.getId());
    ctx.sessionAttribute("user_name", nombre);
    ctx.sessionAttribute("loggedIn", true);
    ctx.sessionAttribute("rol", usuario.getRol());

    if (usuario.getRol() == Rol.ADMIN) {
      ctx.redirect("/admin/dashboard");
    }if (usuario.getRol() == Rol.USER) {
      ctx.redirect("/colecciones");
    }
    //ctx.redirect("/hechos");
  }

  public void logout(Context ctx) {
    var session = ctx.req().getSession(false);
    if (session != null) {
      System.out.println("Session before invalidate - user_id: " + ctx.sessionAttribute("user_id") +
          ", loggedIn: " + ctx.sessionAttribute("loggedIn") +
          ", rol: " + ctx.sessionAttribute("rol"));
      session.invalidate();
      System.out.println("Session invalidated");
    }
    ctx.redirect("/login");
  }

  public void mostrarLogin(Context ctx) {
    if (ctx.sessionAttribute("user_id") != null) {
      ctx.redirect("/");
    }

    Map<String, Object> model = new HashMap<>();

    String errorMsg = ctx.queryParam("error");
    if (errorMsg != null && !errorMsg.isEmpty()) {
      model.put("error", errorMsg);
    }

    ctx.render("login.hbs", model);
  }

}
