package ar.edu.utn.frba.dds.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EntityManagerProvider {

    private static EntityManagerFactory emf;
    private static ThreadLocal<EntityManager> threadLocal;

    static {
        try {
            Map<String, String> env = System.getenv();
            Map<String, Object> configOverrides = new HashMap<>();

            String host = env.get("MYSQLHOST");
            String port = env.get("MYSQLPORT");
            String user = env.get("MYSQLUSER");
            String password = env.get("MYSQLPASSWORD");
            String database = env.get("MYSQLDATABASE");

            if (host != null && port != null && database != null) {
                // Updated URL with essential parameters for Cloud/Docker MySQL 8
                String dbUrl = String.format(
                        "jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", host,
                        port, database);

                configOverrides.put("hibernate.connection.url", dbUrl);
                configOverrides.put("hibernate.connection.username", user);
                configOverrides.put("hibernate.connection.password", password);
                configOverrides.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");

                System.out.println("EntityManagerProvider: Configured for Railway/Env URL: " + dbUrl);
            } else {
                System.out.println("EntityManagerProvider: Using persistence.xml defaults (Local/Dev).");
            }

            emf = Persistence.createEntityManagerFactory("simple-persistence-unit", configOverrides);
            threadLocal = new ThreadLocal<>();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not initialize EntityManagerFactory", e);
        }
    }

    public static EntityManager getEntityManager() {
        EntityManager manager = threadLocal.get();
        if (manager == null || !manager.isOpen()) {
            manager = emf.createEntityManager();
            threadLocal.set(manager);
        }
        return manager;
    }

    public static void closeEntityManager() {
        EntityManager manager = threadLocal.get();
        if (manager != null && manager.isOpen()) {
            manager.close();
            threadLocal.remove();
        }
    }

    public static void beginTransaction() {
        EntityManager em = getEntityManager();
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    public static void commitTransaction() {
        EntityManager em = getEntityManager();
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    public static void rollbackTransaction() {
        EntityManager em = getEntityManager();
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    public static void withTransaction(Runnable action) {
        beginTransaction();
        try {
            action.run();
            commitTransaction();
        } catch (Throwable e) {
            rollbackTransaction();
            throw e;
        }
    }

    public static <T> T withTransaction(Supplier<T> action) {
        beginTransaction();
        try {
            T result = action.get();
            commitTransaction();
            return result;
        } catch (Throwable e) {
            rollbackTransaction();
            throw e;
        }
    }
}
