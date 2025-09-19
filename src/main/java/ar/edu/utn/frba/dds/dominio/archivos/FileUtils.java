package ar.edu.utn.frba.dds.dominio.archivos;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static void borrarArchivosCarpeta(String rutaCarpeta) {
      Path carpeta = Paths.get(rutaCarpeta);

      if (!Files.exists(carpeta) || !Files.isDirectory(carpeta)) {
        System.out.println("La ruta no existe o no es una carpeta: " + rutaCarpeta);
        return;
      }

      try (DirectoryStream<Path> stream = Files.newDirectoryStream(carpeta)) {
        for (Path archivo : stream) {
          if (Files.isRegularFile(archivo)) {
            Files.delete(archivo);
            System.out.println("Archivo borrado: " + archivo.getFileName());
          }
        }
      } catch (IOException e) {
        System.err.println("Error al borrar archivos: " + e.getMessage());
      }
    }
}
