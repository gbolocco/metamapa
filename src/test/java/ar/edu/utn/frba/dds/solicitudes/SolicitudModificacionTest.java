package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudModificacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class SolicitudModificacionTest {

    @Test
    public void testSolicitudModificacionInitialization() {
        RepresentacionDeHecho rep = mock(RepresentacionDeHecho.class);
        Long idHecho = 1L;

        // This constructor previously failed to initialize parent fields
        SolicitudModificacion solicitud = new SolicitudModificacion(rep, idHecho);

        Assertions.assertNotNull(solicitud.getFechaSolicitud(),
                "FechaSolicitud should be initialized by parent constructor");
        Assertions.assertEquals(EstadoSolicitud.PENDIENTE, solicitud.getEstadoSolicitud(),
                "EstadoSolicitud should be PENDIENTE");
        Assertions.assertEquals(rep, solicitud.getRepresentacionDeHecho(), "RepresentacionDeHecho should be set");
        Assertions.assertEquals(idHecho, solicitud.getIdHecho(), "IdHecho should be set");
    }
}
