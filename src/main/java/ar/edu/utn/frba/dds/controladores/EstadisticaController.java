package ar.edu.utn.frba.dds.controladores;

import ar.edu.utn.frba.dds.dominio.estadisticas.GestorDeEstadisticas;
import ar.edu.utn.frba.dds.infraestructura.repositorios.EstadisticasRepository;
import ar.edu.utn.frba.dds.servicios.ServicioSolicitudes;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstadisticaController {

    public EstadisticaController() {}

    public void crearEstadistica(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        model.put("title", "Panel Admin");
        model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
        model.put("rol", ctx.sessionAttribute("rol"));
        model.put("user_name", ctx.sessionAttribute("user_name"));
        model.put("user_id", ctx.sessionAttribute("user_id"));

        GestorDeEstadisticas gestor = new GestorDeEstadisticas(EstadisticasRepository.getInstancia().getEstadisticas());
        //gestor.calcular(FuentesRepository.getInstancia().getFuentes().forEach());

        //model.put("colecciones", colecciones.mostrarColecciones());
        //model.put("solicitudes", solicitudes.mostrarSolicitudes());

        ctx.render("crearEstadistica.hbs", model);
    }

    public void descargarSeleccionadas(Context ctx) {
        List<String> seleccionadas = ctx.formParams("seleccionadas");

        String csv = seleccionadas.stream()
                .map(s -> "\"" + s.replace("\"", "\"\"") + "\"") // escapado simple
                .collect(Collectors.joining("\n"));

        ctx.header("Content-Disposition", "attachment; filename=estadisticas.csv");
        ctx.result(csv);
    }
}
