package ar.edu.utn.frba.dds.controladores;

import io.javalin.http.Context;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
public class ColeccionController {

  public void mostrarColecciones(Context ctx) {


    List<Map<String, String>> colecciones = List.of(
        Map.of("titulo", "Historia Argentina", "descripcion", "Colección de hechos históricos del país"),
        Map.of("titulo", "Arte Contemporáneo", "descripcion", "Muestras de arte moderno"),
        Map.of("titulo", "Ciencia y Tecnología", "descripcion", "Avances científicos importantes"),
        Map.of("titulo", "Deportes", "descripcion", "Eventos y logros deportivos")
    );

    List<Integer> paginas = List.of(1, 2, 3, 4, 5);

    Map<String, Object> model = new HashMap<>();
    model.put("colecciones", colecciones);
    model.put("paginas", paginas);

    ctx.render("colecciones.hbs", model);
  }
}
