package ar.edu.utn.frba.dds.servicioDeAgregacion;

import ar.edu.utn.frba.dds.compartido.servicios.agregador.ServicioDeAgregacion;
import ar.edu.utn.frba.dds.dominio.colecciones.algoritmosconsenso.Absoluta;
import ar.edu.utn.frba.dds.dominio.colecciones.algoritmosconsenso.MayoriaSimple;
import ar.edu.utn.frba.dds.dominio.colecciones.algoritmosconsenso.MultiplesMenciones;
import ar.edu.utn.frba.dds.dominio.filtros.*;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class ServicioDeAgregacionTest {

    private List<Hecho> hechos;
    private List<Filtro> filtros;

    @BeforeEach
    public void setUp() {
        ServicioDeAgregacion.getInstancia().limpiarCache();
        this.hechos = listaDeHechos(OrigenHecho.FUENTE_PROXY);
        this.filtros = crearListaFiltros();
    }

    private void agregarFuenteQueDevuelve(List<Hecho> hechosDeFuente) {
        Fuente fuente = mock(Fuente.class);
        when(fuente.obtenerHechos(anyList())).thenReturn(hechosDeFuente);
        ServicioDeAgregacion.getInstancia().agregarFuente(fuente);
    }

    private List<Hecho> listaDeHechos(OrigenHecho origen) {
        return List.of(
            new Hecho("incendio en la rioja", "incendio forestal en la rioja", "incendios forestales",
                new Ubicacion(-34.6591644, -58.4694862), LocalDate.of(2020, 4, 1),
                LocalDateTime.of(2024, 5, 1, 9, 59, 0), origen),
            new Hecho("incendio en la rioja", "incendio forestal en la pampa", "incendios forestales",
                new Ubicacion(-34.5984145, -58.4222096), LocalDate.of(2020, 4, 2),
                LocalDateTime.of(2024, 5, 1, 9, 59, 0), origen),
            new Hecho("incendio en la cordoba", "incendio forestal en la cordoba", "incendios forestales",
                new Ubicacion(-34.6591644, -58.4694862), LocalDate.of(2020, 4, 3),
                LocalDateTime.of(2024, 5, 1, 13, 0, 0), origen),
            new Hecho("inundación en Rosario", "el desborde del río provocó inundaciones", "desastres naturales",
                new Ubicacion(-32.9442, -60.6505), LocalDate.of(2023, 11, 12),
                LocalDateTime.of(2023, 11, 12, 14, 30, 0), origen),
            new Hecho("protesta docente en Mendoza", "docentes marcharon por mejoras", "manifestaciones sociales",
                new Ubicacion(-32.8908, -68.8272), LocalDate.of(2024, 3, 7),
                LocalDateTime.of(2024, 3, 7, 10, 0, 0), origen),
            new Hecho("accidente ferroviario en Buenos Aires", "tren Mitre colisionó con un auto", "accidentes de transporte",
                new Ubicacion(-34.6037, -58.3816), LocalDate.of(2024, 6, 20),
                LocalDateTime.of(2024, 6, 20, 8, 15, 0), origen)
        );
    }

    private List<Filtro> crearListaFiltros() {
        return List.of(
            new FiltroContieneTexto("Geophysical", CampoDeHecho.CATEGORIA),
            new FiltroContieneTexto("Earthquake", CampoDeHecho.CATEGORIA),
            new FiltroFechaHasta(LocalDate.of(2024, 5, 1), CampoDeHecho.FECHA_ACONTECIMIENTO)
        );
    }


    @Test
    public void algoritmoAbsoluta_consensuaCuandoElHechoEstaEnTodasLasFuentes() {
        for (int i = 0; i < 3; i++) {
            agregarFuenteQueDevuelve(List.of(hechos.get(0)));
        }

        Absoluta algoritmo = new Absoluta();
        ServicioDeAgregacion.getInstancia().cargarHechosDesdeFuentesCache();
        List<Hecho> consensuados = algoritmo.hechosConsensuados(hechos, new ArrayList<>());
        System.out.println(consensuados);
        assertTrue(consensuados.contains(hechos.get(0)));
        assertFalse(consensuados.contains(hechos.get(2))); // caso negativo
    }

    @Test
    public void algoritmoMayoriaSimple_consensuaCuandoElHechoApareceEnMasDeLaMitad() {
        // 3 fuentes lo tienen
        for (int i = 0; i < 3; i++) {
            agregarFuenteQueDevuelve(List.of(hechos.get(0)));
        }
        // 2 fuentes no lo tienen
        for (int i = 0; i < 2; i++) {
            agregarFuenteQueDevuelve(List.of());
        }

        MayoriaSimple algoritmo = new MayoriaSimple();
        ServicioDeAgregacion.getInstancia().cargarHechosDesdeFuentesCache();
        List<Hecho> consensuados = algoritmo.hechosConsensuados(hechos, new ArrayList<>());
        System.out.println(consensuados);
        assertTrue(consensuados.contains(hechos.get(0)));
        assertFalse(consensuados.contains(hechos.get(2))); // caso negativo
    }

    @Test
    public void algoritmoMultiplesMenciones_consensuaCuandoHayDistintosHechosConMismoTitulo() {
        for (int i = 0; i < 3; i++) {
            agregarFuenteQueDevuelve(List.of(hechos.get(0)));
        }
        for (int i = 0; i < 2; i++) {
            agregarFuenteQueDevuelve(List.of());
        }

        MultiplesMenciones algoritmo = new MultiplesMenciones();
        ServicioDeAgregacion.getInstancia().cargarHechosDesdeFuentesCache();
        List<Hecho> consensuados = algoritmo.hechosConsensuados(hechos, new ArrayList<>());
        System.out.println(consensuados);
        assertTrue(consensuados.contains(hechos.get(0)));
    }
}
