package ar.edu.utn.frba.dds.controladores;

import ar.edu.utn.frba.dds.infraestructura.repositorios.RepositorioUsuarios;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class LoginController {

  public void login(Context ctx) {
    try {
      var usuario = RepositorioUsuarios.INSTANCE.buscar(
          ctx.formParam("nombre"),
          ctx.formParam("password")
      );
      ctx.sessionAttribute("user_id", usuario.getId());
      ctx.redirect("/");
    } catch (Exception e) {
      ctx.redirect("/login?error=true");
    }
  }

  public void mostrarLogin(Context ctx) {
    if (ctx.sessionAttribute("user_id") != null) {
      ctx.redirect("/");
    }

    Map<String, Object> model = new HashMap<>();
    if ("true".equals(ctx.queryParam("error"))) {
      model.put("error", "usuario o contraseña invalidas");
    }
    ctx.render("login.hbs", model);
  }

}
