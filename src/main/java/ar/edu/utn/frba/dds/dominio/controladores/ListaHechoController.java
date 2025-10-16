package ar.edu.utn.frba.dds.dominio.controladores;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Collection;

public class ListaHechoController implements Handler, SimplePersistenceTest{

    public void persistirHechos() {
        Hecho hecho = new Hecho("Prueba1", "Prueba1", "Prueba1", new Ubicacion(30.2,30.2), LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);
        Hecho hecho2 = new Hecho("Prueba2", "Prueba2", "Prueba2", new Ubicacion(20.2,10.2), LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.FUENTE_PROXY);

        var em = entityManager();
        System.out.println("EntityManager: " + em);
        System.out.println("Is open: " + em.isOpen());
        System.out.println("Transaction active: " + em.getTransaction().isActive());


        HechosRepositoryMemory repo = HechosRepositoryMemory.getInstancia();
        repo.cargarHecho(hecho);
        repo.cargarHecho(hecho2);
        entityManager().getTransaction().begin();
        entityManager().flush();
        entityManager().getTransaction().commit();

    }

    public ListaHechoController() {
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        persistirHechos();
        Collection<Hecho> coleccion = HechosRepositoryMemory.getInstancia().mostrarHechos();
        ctx.json(coleccion);
    }
}
