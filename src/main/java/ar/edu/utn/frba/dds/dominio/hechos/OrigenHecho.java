package ar.edu.utn.frba.dds.dominio.hechos;

import ar.edu.utn.frba.dds.dominio.usuario.Contribuyente;
import javax.persistence.Entity;


public enum OrigenHecho {
  FUENTE_ESTATICA,
  FUENTE_PROXY,
  PROVISTO_POR_CONTRIBUYENTE;

}

/*
public class EjemploUso {
    public static void main(String[] args) {
        FuenteHecho f1 = FuenteHecho.SISTEMA;
        FuenteHecho f2 = FuenteHecho.USUARIO;

        // Asignar nombre al usuario solo si corresponde
        f2.setNombreUsuario("Juan Pérez");

        System.out.println(f2 + " - Usuario: " + f2.getNombreUsuario());

        // Esto lanzaría una excepción:
        // f1.setNombreUsuario("Error"); // UnsupportedOperationException
    }
}
}*/
