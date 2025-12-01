package ar.edu.utn.frba.dds.controladores;

import ar.edu.utn.frba.dds.compartido.DataFormatter;
import ar.edu.utn.frba.dds.dominio.estadisticas.*;
import ar.edu.utn.frba.dds.dominio.estadisticas.EstadisticaCantidadPorCategoria;
import ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia.CalculadorProvincia;
import ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia.ServicioCalculadorProvinciaNominatim;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.EstadisticasRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import io.javalin.http.Context;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class EstadisticaController {
    private DataFormatter formateador = new DataFormatter();

    public EstadisticaController() {}

    public void crearEstadistica(Context ctx) {

        String tipo = ctx.formParam("tipo");
        if (tipo != null) tipo = tipo.trim();

        String categoria = ctx.formParam("categoria");
        if (categoria != null) categoria = categoria.trim();

        Estadistica estadistica;

        CalculadorProvincia calculadorProvincia = new CalculadorProvincia(new ServicioCalculadorProvinciaNominatim());

        System.out.println(tipo);
        System.out.println(categoria);

        switch (tipo) {
            case "mayorHechosProvincia":
                estadistica = new EstadisticaProvincia(calculadorProvincia, true);
                break;

            case "categoriaMayorHechos":
                estadistica = new EstadisticaCategoria();
                break;

            case "cantidadPorCategoria":
                estadistica = new EstadisticaCantidadPorCategoria(categoria,false);
                break;

            case "mayorHechosProvinciaCategoria":
                estadistica = new EstadisticaProvinciaPorCategoria(calculadorProvincia, categoria, true);
                break;

            case "horaMayorHechosCategoria":
                estadistica = new EstadisticaHoraPorCategoria(categoria, true);
                break;

            case "solicitudesSpam":
                // estadistica = new EstadisticaSpam(new DetectorDeSpam());
                // break;
                estadistica = new EstadisticaCategoria(); // placeholder
                break;

            default:
                System.out.println("Tipo no reconocido: " + tipo);
                estadistica = new EstadisticaCategoria();
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
        model.put("bodyClass", "");  // o el que quieras
        model.put("isAdmin", "ADMIN".equals(ctx.sessionAttribute("rol")));
        model.put("title", "Panel Admin");
        model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
        model.put("rol", ctx.sessionAttribute("rol"));
        model.put("user_name", ctx.sessionAttribute("user_name"));
        model.put("user_id", ctx.sessionAttribute("user_id"));

        List<Estadistica> estadisticas = EstadisticasRepository.getInstancia().getEstadisticas();

        model.put("estadisticas", estadisticas);

        if (estadisticas.stream().map(Estadistica::getFechaDeCalculo) != null) {
            estadisticas.stream()
                .map(Estadistica::getFechaDeCalculo)
                .forEach(fecha -> model.put("fechaFormateada", formateador.formatearFecha(fecha)));
        }

        ctx.render("estadisticas.hbs", model);
    }

    public void descargarSeleccionadas(Context ctx) {
        List<String> seleccionadas = ctx.formParams("seleccionadas");

        if (seleccionadas == null || seleccionadas.isEmpty()) {
            ctx.result("No se seleccionaron estadísticas para descargar.");
            return;
        }

        List<Estadistica> todas = EstadisticasRepository.getInstancia().getEstadisticas();

        List<Estadistica> seleccionadasDatos = todas.stream()
                .filter(e -> seleccionadas.contains(String.valueOf(e.getId())))
                .toList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        String csv = seleccionadasDatos.stream()
                .map(e -> String.format("\"%s\",\"%s\"",
                        e.getRespuesta() != null ? e.getRespuesta().replace("\"", "\"\"") : "",
                        e.getFechaDeCalculo() != null ? e.getFechaDeCalculo().format(formatter) : ""))
                .collect(Collectors.joining("\n", "Respuesta,Fecha de Cálculo\n", ""));

        ctx.header("Content-Disposition", "attachment; filename=estadisticas.csv");
        ctx.header("Content-Type", "text/csv; charset=UTF-8");
        ctx.result("\uFEFF" + csv);
    }

}
