package ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

public enum Provincia {
    BUENOS_AIRES("Buenos Aires"),
    CABA("Ciudad Autónoma de Buenos Aires"),
    CATAMARCA("Catamarca"),
    CHACO("Chaco"),
    CHUBUT("Chubut"),
    CORDOBA("Córdoba"),
    CORRIENTES("Corrientes"),
    ENTRE_RIOS("Entre Ríos"),
    FORMOSA("Formosa"),
    JUJUY("Jujuy"),
    LA_PAMPA("La Pampa"),
    LA_RIOJA("La Rioja"),
    MENDOZA("Mendoza"),
    MISIONES("Misiones"),
    NEUQUEN("Neuquén"),
    RIO_NEGRO("Río Negro"),
    SALTA("Salta"),
    SAN_JUAN("San Juan"),
    SAN_LUIS("San Luis"),
    SANTA_CRUZ("Santa Cruz"),
    SANTA_FE("Santa Fe"),
    SANTIAGO_DEL_ESTERO("Santiago del Estero"),
    TIERRA_DEL_FUEGO("Tierra del Fuego, Antártida e Islas del Atlántico Sur"),
    TUCUMAN("Tucumán");

    private final String nombre;
    private static final Map<String, Provincia> MAPA = new HashMap<>();

    static {
        for (Provincia p : Provincia.values()) {
            MAPA.put(normalizar(p.name()), p);
            MAPA.put(normalizar(p.getNombre()), p);
        }

        // Sinónimos adicionales (opcional)
        MAPA.put(normalizar("Capital Federal"), CABA);
        MAPA.put(normalizar("Buenos Aires Ciudad"), CABA);
        MAPA.put(normalizar("Bs As"), BUENOS_AIRES);
        MAPA.put(normalizar("Santa Fé"), SANTA_FE); // con tilde incorrecta
    }

    Provincia(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public static Provincia fromString(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("El valor no puede ser null");
        }

        String clave = normalizar(valor);
        Provincia provincia = MAPA.get(clave);

        if (provincia == null) {
            throw new IllegalArgumentException("Provincia desconocida: " + valor);
        }

        return provincia;
    }

    private static String normalizar(String texto) {
        return Normalizer
                .normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "") // elimina tildes
                .toUpperCase()
                .trim();
    }
}
