package ar.edu.utn.frba.dds.controladores;


import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import ar.edu.utn.frba.dds.servicios.ServicioColecciones;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.http.Context;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ColeccionController {
  private ServicioColecciones servicioColecciones;

  public ColeccionController(ServicioColecciones servicioColecciones) {
    this.servicioColecciones = servicioColecciones;
  }


  public void mostrarColecciones(Context ctx) {
    int coleccionesPorPagina = 12;


    List<Coleccion> colecciones = servicioColecciones.obtenerColecciones();


    int totalPaginas = (int) Math.ceil((double) colecciones.size() / coleccionesPorPagina);

    int paginaActual = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);


    int desde = (paginaActual - 1) * coleccionesPorPagina;
    int hasta = Math.min(desde + coleccionesPorPagina, colecciones.size());


    List<Coleccion> coleccionesPagina = colecciones.subList(desde, hasta);


    List<Integer> paginas = IntStream.rangeClosed(1, totalPaginas)
        .boxed()
        .collect(Collectors.toList());

    // Preparar modelo para la vista
    Map<String, Object> model = new HashMap<>();
    model.put("colecciones", coleccionesPagina);
    model.put("paginas", paginas);
    model.put("paginaActual", paginaActual);
    model.put("totalPaginas", totalPaginas);

    model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
    model.put("rol", ctx.sessionAttribute("rol"));
    model.put("user_id", ctx.sessionAttribute("user_id"));

    ctx.render("colecciones.hbs", model);
  }


  public void mostrarColeccion(Context ctx) throws JsonProcessingException {
    // Obtener el id desde la URL
    String idParam = ctx.pathParam("id");
    Long id = Long.parseLong(idParam);

    Coleccion coleccion = servicioColecciones.findById(id);
    if (coleccion == null) {
      ctx.status(404).result("Colección no encontrada");
      return;
    }

    Collection<Hecho> hechos = coleccion.getFuente().obtenerHechos(new ArrayList<>());



    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String hechosJson = mapper.writeValueAsString(hechos);

    Map<String, Object> model = new HashMap<>();
    model.put("coleccion", coleccion);
    model.put("hechosJson", hechosJson);
    model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
    model.put("rol", ctx.sessionAttribute("rol"));
    model.put("user_id", ctx.sessionAttribute("user_id"));

    ctx.render("coleccion.hbs", model);
  }


}
