package ar.edu.utn.frba.dds.dominio.controladores;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class UILoginController implements Handler {

  @Override
  public void handle(@NotNull Context ctx) throws Exception {
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
