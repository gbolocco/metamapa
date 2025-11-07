package ar.edu.utn.frba.dds.controladores;

import io.javalin.http.Context;
import java.util.HashMap;
import java.util.Map;

public class HomeController {

  public void mostrarHome(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
    model.put("rol", ctx.sessionAttribute("rol"));
    model.put("user_name", ctx.sessionAttribute("user_name"));
    model.put("user_id", ctx.sessionAttribute("user_id"));
    
    String success = ctx.queryParam("success");
    if ("solicitud_creada".equals(success)) {
      model.put("successMessage", "Solicitud generada exitosamente");
    }
    
    ctx.render("home.hbs", model);
  }
}