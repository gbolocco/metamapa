package ar.edu.utn.frba.dds.dominio.spam;

import org.apache.commons.math3.util.MathArrays;
import java.util.*;
import java.util.stream.Collectors;


public class DetectorDeSpam {

    // Vocabulario predefinido (palabras clave para spam)
    private static final Set<String> VOCABULARIO = Set.of(
            "oferta", "gratis", "ganador", "millón", "clic", "urgente", "dinero", "promoción"
    );

    // Documentos de ejemplo para entrenamiento (simplificado)
    private static final List<String> DOCUMENTOS_SPAM = Arrays.asList(
            "¡Gana un millón de dólares ahora! Clic aquí",
            "Oferta exclusiva gratis para ti",
            "Promoción urgente: dinero fácil"
    );

    private static final List<String> DOCUMENTOS_NO_SPAM = Arrays.asList(
            "Reunión de trabajo mañana",
            "Hola, ¿cómo estás?",
            "Recordatorio: pago de factura"
    );

    private Map<String, Double> idfMap; // Almacena los valores IDF de cada palabra

    public DetectorDeSpam() {
        calcularIDF(); // Precalcula IDF al inicializar
    }

    // Paso 1: Tokenizar y limpiar texto
    private List<String> tokenizar(String texto) {
        return Arrays.stream(texto.toLowerCase().split("\\W+"))
                .filter(palabra -> !palabra.isEmpty() && VOCABULARIO.contains(palabra))
                .collect(Collectors.toList());
    }

    // Paso 2: Calcular TF (Frecuencia de Término)
    private Map<String, Double> calcularTF(List<String> palabras) {
        Map<String, Double> tf = new HashMap<>();
        int totalPalabras = palabras.size();
        for (String palabra : palabras) {
            tf.put(palabra, tf.getOrDefault(palabra, 0.0) + 1.0);
        }
        // Normalizar
        tf.replaceAll((k, v) -> v / totalPalabras);
        return tf;
    }

    // Paso 3: Calcular IDF (Frecuencia Inversa de Documento)
    private void calcularIDF() {
        List<List<String>> todosDocumentos = new ArrayList<>();
        todosDocumentos.addAll(DOCUMENTOS_SPAM.stream().map(this::tokenizar).collect(Collectors.toList()));
        todosDocumentos.addAll(DOCUMENTOS_NO_SPAM.stream().map(this::tokenizar).collect(Collectors.toList()));

        int totalDocumentos = todosDocumentos.size();
        idfMap = new HashMap<>();

        for (String palabra : VOCABULARIO) {
            long documentosConPalabra = todosDocumentos.stream()
                    .filter(doc -> doc.contains(palabra))
                    .count();
            idfMap.put(palabra, Math.log((double) totalDocumentos / (documentosConPalabra + 1)));
        }
    }

    // Paso 4: Calcular TF-IDF para un texto
    private double calcularTFIDF(String texto) {
        List<String> palabras = tokenizar(texto);
        Map<String, Double> tf = calcularTF(palabras);
        double tfidfTotal = 0.0;

        for (String palabra : palabras) {
            double tfidf = tf.get(palabra) * idfMap.getOrDefault(palabra, 0.0);
            tfidfTotal += tfidf;
        }

        return tfidfTotal;
    }

    // Paso 5: Clasificar como spam o no spam (umbral simple)
    public boolean esSpam(String texto) {
        double tfidf = calcularTFIDF(texto);
        double umbral = 0.5; // Ajusta según tus datos
        return tfidf > umbral;
    }

}

