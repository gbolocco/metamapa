package ar.edu.utn.frba.dds.dominio.controladores;

import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class UIListaHechosController implements Handler {
    HechosRepositoryMemory repo = HechosRepositoryMemory.getInstancia();

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        Map<String, Object> model = new HashMap<>();

        model.put("hechos", repo.mostrarHechos());
        ctx.render("hechos.hbs", model);
    }
}
