package componenteEstadistica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ar.edu.utn.frba.dds.dominio.estadisticas.EstadisticaCategoria;
import ar.edu.utn.frba.dds.dominio.estadisticas.EstadisticaHoraPorCategoria;
import ar.edu.utn.frba.dds.dominio.estadisticas.EstadisticaProvincia;
import ar.edu.utn.frba.dds.dominio.estadisticas.EstadisticaProvinciaPorCategoria;
import ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia.CalculadorProvincia;
import ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia.Provincia;
import ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia.ServicioCalculadorProvincia;
import ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia.ServicioCalculadorProvinciaNominatim;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EstadisticaTest {

  ServicioCalculadorProvincia servicioMock;
  Ubicacion buenosAires;
  Ubicacion cordoba;
  CalculadorProvincia calculador;
  List<Hecho> hechos = new ArrayList<>();

  @BeforeEach
  public void setUp(){
    servicioMock = mock(ServicioCalculadorProvincia.class);
    calculador = new CalculadorProvincia(servicioMock);

    buenosAires = mock(Ubicacion.class);
    cordoba = mock(Ubicacion.class);

    when(servicioMock.calcularProvincia(buenosAires)).thenReturn("CABA");
    when(servicioMock.calcularProvincia(cordoba)).thenReturn("CORDOBA");

    Hecho h1 = new Hecho("","","incendio",cordoba,LocalDateTime.of(2024, 5, 1, 10, 0),mock(LocalDateTime.class),mock(OrigenHecho.class));
    Hecho h2 = new Hecho("","","robo",buenosAires,LocalDateTime.of(2024, 5, 1, 10, 0),mock(LocalDateTime.class),mock(OrigenHecho.class));
    Hecho h3 = new Hecho("","","robo",cordoba,LocalDateTime.of(2024, 5, 1, 10, 0),mock(LocalDateTime.class),mock(OrigenHecho.class));
    Hecho h4 = new Hecho("","","incendio",cordoba,LocalDateTime.of(2024, 5, 1, 10, 0),mock(LocalDateTime.class),mock(OrigenHecho.class));
    Hecho h5 = new Hecho("","","incendio",buenosAires,LocalDateTime.of(2024, 5, 1, 10, 0),mock(LocalDateTime.class),mock(OrigenHecho.class));

    hechos = List.of(h1,h2,h3,h4,h5);

  }

  @Test
  public void testProvinciaConMasHechos(){
    EstadisticaProvincia estadistica = new EstadisticaProvincia(calculador, true);
    String resultado = estadistica.calcular(hechos);
    assertEquals("Córdoba (3 hechos)", resultado);
  }

  @Test
  public void testEstadisticaCategoria() {
    EstadisticaCategoria estadistica = new EstadisticaCategoria(true);
    String resultado = estadistica.calcular(hechos);
    assertEquals("incendio (3 hechos)", resultado); // 3 incendios vs 2 robos
  }

  @Test
  public void testEstadisticaHoraPorCategoria() {
    EstadisticaHoraPorCategoria estadistica = new EstadisticaHoraPorCategoria("robo", true);
    String resultado = estadistica.calcular(hechos);
    assertEquals("Hora con más hechos de categoría robo: 10 hs (2 hechos)", resultado);
  }

  @Test
  public void testEstadisticaProvinciaPorCategoria() {
    EstadisticaProvinciaPorCategoria estadistica =
        new EstadisticaProvinciaPorCategoria(calculador, "incendio", true);
    String resultado = estadistica.calcular(hechos);
      System.out.println(resultado);
    assertEquals("Córdoba (2 hechos)", resultado); // Córdoba tiene 2 incendios, CABA 1
  }


  @Test
  public void testEstadisticaHoraPorCategoriaSinDatos() {
    EstadisticaHoraPorCategoria estadistica = new EstadisticaHoraPorCategoria("asalto", true);
    String resultado = estadistica.calcular(hechos);
    System.out.println(resultado);
    assertEquals("Sin hechos para la categoría: asalto", resultado);
  }

  @Test
  public void testEstadisticaProvinciaPorCategoriaSinDatos() {
    EstadisticaProvinciaPorCategoria estadistica =
        new EstadisticaProvinciaPorCategoria(calculador, "fraude", true);
    String resultado = estadistica.calcular(hechos);
    System.out.println(resultado);
    assertEquals("Sin hechos para la categoría: fraude", resultado);
  }

  @Test
    public void testApi(){
      Ubicacion ubicacion = new Ubicacion(-34.6037, -58.3816); // Buenos Aires
      ServicioCalculadorProvincia servicio = new ServicioCalculadorProvinciaNominatim();
      CalculadorProvincia calculador = new CalculadorProvincia(servicio);

      Provincia provincia = calculador.calcularProvincia(ubicacion);
      System.out.println("Provincia: " + provincia.getNombre());
  }
}
