package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.compartido.AppLogger;
import ar.edu.utn.frba.dds.compartido.servicios.agregador.ServicioDeAgregacion;
import ar.edu.utn.frba.dds.dominio.preprocesadores.PreProcesadorCsv;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;


public class Main {
  private static AppLogger AppLogger;
  private static final Logger logger = AppLogger.getLogger(Main.class);

  public static void main(String[] args) {
    String rutaEntrada = "datos/desastres_naturales.csv";

//    PreProcesadorCsv procesador = new PreProcesadorCsv();
//    try {
//      File archivoProcesado = procesador.preprocesar(rutaEntrada);
//      logger.info("El archivo fue procesado"); //cambiar por logger
//      logger.info("Ruta de salida: {}", archivoProcesado.getAbsolutePath());
//    } catch (IOException | CsvException e) {
//      logger.error("Error al procesar el CSV:");
//      e.printStackTrace();
//    }



    // Lo que ejecuta el cron, todos los dias en un horario de baja carga
    ServicioDeAgregacion.getInstancia().cargarHechosDesdeFuentesCache();

    System.out.println("EJECUTANDO CRON");

  }
}