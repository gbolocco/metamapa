package ar.edu.utn.frba.dds.controladores;


import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.servicios.ServicioColecciones;
import io.javalin.http.Context;
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

    // Obtener todas las colecciones
    List<Coleccion> colecciones = servicioColecciones.obtenerColecciones();

    // Calcular la cantidad total de páginas
    int totalPaginas = (int) Math.ceil((double) colecciones.size() / coleccionesPorPagina);

    // Obtener el número de página desde la URL (por defecto 1)
    int paginaActual = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);

    // Calcular el índice inicial y final
    int desde = (paginaActual - 1) * coleccionesPorPagina;
    int hasta = Math.min(desde + coleccionesPorPagina, colecciones.size());

    // Obtener solo las colecciones de esa página
    List<Coleccion> coleccionesPagina = colecciones.subList(desde, hasta);

    // Generar lista de números de página [1, 2, ..., totalPaginas]
    List<Integer> paginas = IntStream.rangeClosed(1, totalPaginas)
        .boxed()
        .collect(Collectors.toList());

    // Preparar modelo para la vista
    Map<String, Object> model = new HashMap<>();
    model.put("colecciones", coleccionesPagina);
    model.put("paginas", paginas);
    model.put("paginaActual", paginaActual);

    ctx.render("colecciones.hbs", model);
  }

}
