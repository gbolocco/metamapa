package ar.edu.utn.frba.dds.algoritmos;

import static org.mockito.Mockito.mock;

public class AlgoritmosTest {
//
//    private ServicioDeAgregacion servicioDeAgregacion;
//    private List<Hecho> hechos = new ArrayList<>();
//    private List<Filtro> filtros;
//
//    public List<Hecho> listaDeHechos(OrigenHecho origen) {
//        Hecho hecho1 = new Hecho("incendio en la rioja",
//                "incendio forestal en la rioja", "incendios forestales",
//                new Ubicacion(-34.6591644, -58.4694862), LocalDate.of(2020, 4, 1),
//                LocalDateTime.of(2024, 5, 1, 9, 59, 00),
//                origen);
//        Hecho hecho2 = new Hecho("incendio en la rioja",
//                "incendio forestal en la pampa", "incendios forestales",
//                new Ubicacion(-34.5984145, -58.4222096), LocalDate.of(2020, 4, 2),
//                LocalDateTime.of(2024, 5, 1, 9, 59, 00),
//                origen);
//
//        Hecho hecho3 = new Hecho("incendio en la cordoba",
//                "incendio forestal en la cordoba", "incendios forestales",
//                new Ubicacion(-34.6591644, -58.4694862), LocalDate.of(2020, 4, 3),
//                LocalDateTime.of(2024, 5, 1, 13, 00, 00),
//                origen);
//        Hecho hecho4 = new Hecho("inundación en Rosario",
//                "el desborde del río provocó inundaciones en varios barrios",
//                "desastres naturales",
//                new Ubicacion(-32.9442, -60.6505),
//                LocalDate.of(2023, 11, 12),
//                LocalDateTime.of(2023, 11, 12, 14, 30, 0),
//                origen);
//
//        Hecho hecho5 = new Hecho("protesta docente en Mendoza",
//                "docentes marcharon por mejoras salariales en el centro de Mendoza",
//                "manifestaciones sociales",
//                new Ubicacion(-32.8908, -68.8272),
//                LocalDate.of(2024, 3, 7),
//                LocalDateTime.of(2024, 3, 7, 10, 0, 0),
//                origen);
//
//        Hecho hecho6 = new Hecho("accidente ferroviario en Buenos Aires",
//                "una formación del tren Mitre colisionó con un auto en un paso a nivel",
//                "accidentes de transporte",
//                new Ubicacion(-34.6037, -58.3816),
//                LocalDate.of(2024, 6, 20),
//                LocalDateTime.of(2024, 6, 20, 8, 15, 0),
//                origen);
//        List<Hecho> hechos = List.of(hecho1, hecho2, hecho3, hecho4, hecho5, hecho6);
//        return hechos;
//    }
//
//
//    public List<Filtro> crearListaFiltros() {
//        List<Filtro> lista;
//        FiltroContieneTexto filtroTexto1 = new FiltroContieneTexto("Geophysical", CampoDeHecho.CATEGORIA);
//        FiltroContieneTexto filtroTexto2 = new FiltroContieneTexto("Earthquake", CampoDeHecho.CATEGORIA);
//        FiltroFechaHasta filtroFechaHasta = new FiltroFechaHasta(
//                LocalDate.of(2024, 5, 1),
//                CampoDeHecho.FECHA_ACONTECIMIENTO
//        );
//
//        lista = List.of(filtroTexto1, filtroTexto2, filtroFechaHasta);
//
//        return lista;
//    }
//
//    public void configParaTestAlgAbsoluta() {
//        hechos = listaDeHechos(OrigenHecho.FUENTE_PROXY);
//        filtros = crearListaFiltros();
//
//        Fuente mockFuente = mock(Fuente.class);
//        when(mockFuente.obtenerHechos(anyList())).thenReturn(hechos);
//        ServicioDeAgregacion.getInstancia().agregarFuente(mockFuente);
//        System.out.println(ServicioDeAgregacion.getInstancia().getCantFuentes());
//    }
//
//    public void configParaTestAlgMayoriaSimple() {
//        this.hechos = listaDeHechos(OrigenHecho.FUENTE_PROXY);
//        this.filtros = crearListaFiltros();
//
//        // 3 fuentes que SÍ devuelven hechos.get(0)
//        for (int i = 0; i < 3; i++) {
//            Fuente fuenteConCoincidencia = mock(Fuente.class);
//            when(fuenteConCoincidencia.obtenerHechos(anyList())).thenReturn(List.of(hechos.get(0)));
//            ServicioDeAgregacion.getInstancia().agregarFuente(fuenteConCoincidencia);
//        }
//
//        // 2 fuentes que NO devuelven hechos.get(0)
//        for (int i = 0; i < 2; i++) {
//            Fuente fuenteSinCoincidencia = mock(Fuente.class);
//            when(fuenteSinCoincidencia.obtenerHechos(anyList())).thenReturn(List.of());
//            ServicioDeAgregacion.getInstancia().agregarFuente(fuenteSinCoincidencia);
//        }
//    }
//
//    public void configParaTestAlgMultMenciones() {
//        this.hechos = listaDeHechos(OrigenHecho.FUENTE_PROXY); // contiene hecho0, hecho1, etc.
//        this.filtros = crearListaFiltros();
//
//        // Hecho base con título compartido
//        Hecho hechoCoincidente = hechos.get(0);
//
//        // Fuentes que coinciden exactamente con hechoCoincidente
//        for (int i = 0; i < 3; i++) {
//            Fuente fuente = mock(Fuente.class);
//            when(fuente.obtenerHechos(anyList())).thenReturn(List.of(hechoCoincidente));
//            ServicioDeAgregacion.getInstancia().agregarFuente(fuente);
//        }
//
//        // Fuentes que no contienen el hecho ni otros con el mismo título
//        for (int i = 0; i < 2; i++) {
//            Fuente fuente = mock(Fuente.class);
//            when(fuente.obtenerHechos(anyList())).thenReturn(List.of()); // podrían devolver hechos distintos también
//            ServicioDeAgregacion.getInstancia().agregarFuente(fuente);
//        }
//
//    }
//
//
//    @Test
//    public void hechosDeMismoTituloYDistintosAtributos() {
//        this.hechos = listaDeHechos(OrigenHecho.FUENTE_PROXY);
//        MultiplesMenciones algoritmoMutiplesMenciones = new MultiplesMenciones();
//        assertTrue(algoritmoMutiplesMenciones.hechosDeMismoTituloYdistintosAtributos(this.hechos.get(0), this.hechos.get(1)));
//    }
//
//    @Test
//    public void hechosDeMismoTituloYMismosAtributos() {
//        this.hechos = listaDeHechos(OrigenHecho.FUENTE_PROXY);
//        MultiplesMenciones algoritmoMutiplesMenciones = new MultiplesMenciones();
//        assertFalse(algoritmoMutiplesMenciones.hechosDeMismoTituloYdistintosAtributos(this.hechos.get(0), this.hechos.get(0)));
//    }
//
////    @Test
////    public void testAlgoritmoAbsolutaConsensua() {
////        configParaTestAlgAbsoluta();
////        Absoluta algoritmoAbsoluta = new Absoluta();
////        List<Hecho> listHechosCache = algoritmoAbsoluta.hechosConsensuados(hechos,new ArrayList<>());
////        assertTrue(algoritmoAbsoluta.estaConsensuado(this.hechos.get(0), listHechosCache));
////    }
//
//    @Test
//    public void testAlgoritmoMayoriaSimpleConsensua() {
//        configParaTestAlgMayoriaSimple();
//        MayoriaSimple algoritmoMayoriaSimple = new MayoriaSimple();
//        List<Hecho> listHechosConsensuados = algoritmoMayoriaSimple.hechosConsensuados(hechos,new ArrayList<>());
//        assertTrue(listHechosConsensuados.contains(this.hechos.get(0)));
//    }
//
//    @Test
//    public void testAlgoritmoMultiplesMencionesConsensua() {
//        configParaTestAlgMultMenciones();
//        MultiplesMenciones algoritmoMultiplesMenciones = new MultiplesMenciones();
//        List<Hecho> listaHechosConsensuados = algoritmoMultiplesMenciones.hechosConsensuados(hechos,new ArrayList<>());
//        assertTrue(listaHechosConsensuados.contains(this.hechos.get(0)));
//    }

}
