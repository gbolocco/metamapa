package ar.edu.utn.frba.dds.controladores;

import ar.edu.utn.frba.dds.dominio.estadisticas.*;
import ar.edu.utn.frba.dds.dominio.estadisticas.EstadisticaCantidadPorCategoria;
import ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia.CalculadorProvincia;
import ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia.ServicioCalculadorProvinciaNominatim;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.spam.DetectorDeSpam;
import ar.edu.utn.frba.dds.infraestructura.repositorios.EstadisticasRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import ar.edu.utn.frba.dds.servicios.ServicioSolicitudes;
import io.javalin.http.Context;

import java.util.*;
import java.util.stream.Collectors;

public class EstadisticaController {


    public EstadisticaController() {}

    public void crearEstadistica(Context ctx) {

        String tipo = ctx.formParam("tipo");
        if (tipo != null) tipo = tipo.trim();

        String categoria = ctx.formParam("categoria");
        if (categoria != null) categoria = categoria.trim();

        Estadistica estadistica;

        CalculadorProvincia calculadorProvincia = new CalculadorProvincia(new ServicioCalculadorProvinciaNominatim());

        switch (tipo) {
            case "mayorHechosProvincia":
                estadistica = new EstadisticaProvincia(calculadorProvincia, true);
                break;

            case "categoriaMayorHechos":
                estadistica = new EstadisticaCategoria(true);
                break;

            case "cantidadPorCategoria":
                estadistica = new EstadisticaCantidadPorCategoria(categoria,false);

            case "mayorHechosProvinciaCategoria":
                estadistica = new EstadisticaProvinciaPorCategoria(calculadorProvincia, categoria, true);
                break;

            case "horaMayorHechosCategoria":
                estadistica = new EstadisticaHoraPorCategoria(categoria, true);
                break;

            case "solicitudesSpam":
                // estadistica = new EstadisticaSpam(new DetectorDeSpam());
                // break;
                estadistica = new EstadisticaCategoria(true); // placeholder
                break;

            default:
                System.out.println("Tipo no reconocido: " + tipo);
                estadistica = new EstadisticaCategoria(true);
        }

        EstadisticasRepository.getInstancia().addEstadistica(estadistica);
        ctx.redirect("/admin/dashboard");
    }

    public void calcularEstadisticas(Context ctx) {

        List<Hecho> hechosSisitema = HechosRepository.getInstancia().mostrarHechos();
        EstadisticasRepository.getInstancia().calcular(hechosSisitema);

        ctx.redirect("/estadisticas");
    }


    public void mostrarEstadisticas(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        model.put("title", "Panel Admin");
        model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
        model.put("rol", ctx.sessionAttribute("rol"));
        model.put("user_name", ctx.sessionAttribute("user_name"));
        model.put("user_id", ctx.sessionAttribute("user_id"));

        List<String> resultadosEstadisticas = EstadisticasRepository.getInstancia().getRespuestas();

        model.put("estadisticas", resultadosEstadisticas);

        ctx.render("estadisticas.hbs", model);
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
