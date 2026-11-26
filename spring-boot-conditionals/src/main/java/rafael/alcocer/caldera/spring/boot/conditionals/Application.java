package rafael.alcocer.caldera.spring.boot.conditionals;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /*
    @Bean
    public CommandLineRunner run(JdbcTemplate jdbcTemplate) {
        return args -> {
            jdbcTemplate.execute("CREATE TABLE customer (id INT PRIMARY KEY, name VARCHAR(50), email VARCHAR(50))");

            jdbcTemplate.update("INSERT INTO customer (id, name, email) VALUES (?, ?, ?)", 1, "Rafael", "rafael@example.com");
            jdbcTemplate.update("INSERT INTO customer (id, name, email) VALUES (?, ?, ?)", 2, "Ana", "ana@example.com");

            System.out.println(jdbcTemplate.queryForList("SELECT * FROM customer"));
        };
    }
     */
}
