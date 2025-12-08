package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConnectionTest implements SimplePersistenceTest {

    @Test
    void testConnectionOverride() {
        // Set a dummy URL that should fail with a specific error if respected
        System.setProperty("hibernate.connection.url", "jdbc:mysql://nonexistent.host:3306/db");

        // Attempt to open a transaction/query which triggers DB connection
        try {
            entityManager().getTransaction().begin();
        } catch (Exception e) {
            e.printStackTrace();
            // If the error mentions "nonexistent.host", then the override worked.
            if (e.getMessage().contains("nonexistent.host") || e.toString().contains("nonexistent.host")) {
                System.out.println("OVERRIDE_SUCCESS");
                return;
            }
            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause.getMessage() != null && cause.getMessage().contains("nonexistent.host")) {
                    System.out.println("OVERRIDE_SUCCESS");
                    return;
                }
                cause = cause.getCause();
            }
        }
        System.out.println("OVERRIDE_FAILED");
    }
}
