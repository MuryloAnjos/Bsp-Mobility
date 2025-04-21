package com.bsp.bspmobility;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BspmobilityApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        setPropertyOrThrow(dotenv, "DATABASE_URL");
        setPropertyOrThrow(dotenv, "DATABASE_USERNAME");
        setPropertyOrThrow(dotenv, "DATABASE_PASSWORD");
        setPropertyOrThrow(dotenv, "SUPABASE_URL");
        setPropertyOrThrow(dotenv, "SUPABASE_KEY");
        setPropertyOrThrow(dotenv, "PORT", "8080");
        SpringApplication.run(BspmobilityApplication.class, args);
    }

    private static void setPropertyOrThrow(Dotenv dotenv, String key) {
        String value = dotenv.get(key);
        if (value == null) {
            throw new IllegalStateException("Variável de ambiente" + key + "não está definido no arquivo .env");
        }
        System.setProperty(key, value);
    }

    private static void setPropertyOrThrow(Dotenv dotenv, String key, String defaultValue) {
        String value = dotenv.get(key);
        System.setProperty(key, value != null ? value : defaultValue);
    }
}