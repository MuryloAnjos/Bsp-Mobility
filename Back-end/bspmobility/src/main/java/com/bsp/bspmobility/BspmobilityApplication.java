package com.bsp.bspmobility;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BspmobilityApplication {
    public static void main(String[] args) {
        // Carrega as variáveis de ambiente diretamente
        setPropertyOrThrow("DATABASE_URL");
        setPropertyOrThrow("SPTRANS_API_TOKEN"); // Adicionada porque está no application.properties
        setPropertyOrThrow("PORT", "8080"); // Mantém o padrão 8080 se não estiver definido

        // Opcional: Log para depuração
        System.out.println("DATABASE_URL: " + System.getenv("DATABASE_URL"));
        System.out.println("SPTRANS_API_TOKEN: " + System.getenv("SPTRANS_API_TOKEN"));
        System.out.println("PORT: " + System.getProperty("PORT"));

        SpringApplication.run(BspmobilityApplication.class, args);
    }

    private static void setPropertyOrThrow(String key) {
        String value = System.getenv(key);
        if (value == null) {
            throw new IllegalStateException("Variável de ambiente " + key + " não está definida");
        }
        System.setProperty(key, value);
    }

    private static void setPropertyOrThrow(String key, String defaultValue) {
        String value = System.getenv(key);
        System.setProperty(key, value != null ? value : defaultValue);
    }
}