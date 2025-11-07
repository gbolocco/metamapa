package componenteEstadistica;

import ar.edu.utn.frba.dds.dominio.estadisticas.EstadisticaCategoria;
import ar.edu.utn.frba.dds.dominio.estadisticas.EstadisticaProvinciaPorCategoria;
import ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia.CalculadorProvincia;
import ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia.ServicioCalculadorProvincia;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.EstadisticasRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EstadisticaPersistenceTest implements WithSimplePersistenceUnit {

    ServicioCalculadorProvincia servicioMock;
    Ubicacion buenosAires;
    Ubicacion cordoba;
    CalculadorProvincia calculador;
    EstadisticaProvinciaPorCategoria estadistica1;
    EstadisticaCategoria estadistica2;

    @BeforeEach
    void setup() {

        servicioMock = mock(ServicioCalculadorProvincia .class);
        calculador = new CalculadorProvincia(servicioMock);

        buenosAires = mock(Ubicacion .class);
        cordoba = mock(Ubicacion.class);

        when(servicioMock.calcularProvincia(buenosAires)).thenReturn("CABA");
        when(servicioMock.calcularProvincia(cordoba)).thenReturn("CORDOBA");
        estadistica1 = new EstadisticaProvinciaPorCategoria(calculador, "fraude", true);
        estadistica2 = new EstadisticaCategoria();
    }

    @Test
    public void persistirFuentes() {
        entityManager().getTransaction().begin();
        EstadisticasRepository.getInstancia().persistirEstadistica(estadistica1);
        EstadisticasRepository.getInstancia().persistirEstadistica(estadistica2);
        //entityManager().getTransaction().commit();
        //entityManager().flush();
    }

}
