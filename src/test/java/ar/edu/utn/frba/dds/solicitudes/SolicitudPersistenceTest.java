package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudModificacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.RepresentacionHechosRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepository;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class SolicitudPersistenceTest implements SimplePersistenceTest {

    @Test
    public void testPersistirSolicitudModificacion() {
        // Crear un hecho base
        Hecho hecho = new Hecho("Hecho Original", "Desc", "Cat", new Ubicacion(0.0, 0.0), LocalDateTime.now(),
                LocalDateTime.now(), OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);
        withTransaction(() -> {
            HechosRepository.getInstancia().cargarHecho(hecho);
        });

        // Crear representacion
        RepresentacionDeHecho rep = new RepresentacionDeHecho("Titulo Nuevo", "Desc Nueva", "Cat",
                new Ubicacion(0.0, 0.0),
                LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);

        // Esto deberia fallar si no hay transaccion adentro del metodo
        RepresentacionHechosRepository.getInstancia().cargarRepresentacionDeHecho(rep);

        // Crear solicitud
        // Esto tambien deberia fallar si no hay transaccion adentro del metodo o
        // constructor
        SolicitudModificacion solicitud = new SolicitudModificacion(rep, hecho.getId());

        // Verificar
        withTransaction(() -> {
            Assertions.assertNotNull(solicitud.getId(), "La solicitud deberia tener ID si se persistio");
            Assertions.assertNotNull(rep.getId(), "La representacion deberia tener ID si se persistio");

            SolicitudModificacion found = (SolicitudModificacion) SolicitudesRepository.getInstancia()
                    .buscarSolicitudPorId(solicitud.getId());
            Assertions.assertNotNull(found);
        });
    }
}
