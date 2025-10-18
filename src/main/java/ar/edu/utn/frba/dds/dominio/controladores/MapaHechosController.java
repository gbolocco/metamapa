package ar.edu.utn.frba.dds.dominio.controladores;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MapaHechosController implements Handler {

  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    Collection<Hecho> hechos = HechosRepositoryMemory.getInstancia().mostrarHechos();

    Map<String, Object> model = new HashMap<>();
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String hechosJson = mapper.writeValueAsString(hechos);

    model.put("hechosJson", hechosJson);
    ctx.render("mapa.hbs", model);
  }

}

/*
      List<Hecho> hechos = List.of(
          new Hecho(-34.6037, -58.3816, "Hecho 1"),
          new Hecho(-34.6090, -58.3820, "Hecho 2")
      );

      Map<String, Object> model = new HashMap<>();

      ObjectMapper mapper = new ObjectMapper();
      String hechosJson = mapper.writeValueAsString(hechos);

      model.put("hechosJson", hechosJson);

      ctx.render("mapa.hbs", model);
*/