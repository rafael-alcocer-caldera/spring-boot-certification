package rafael.alcocer.caldera.spring.boot.conditionals.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    /**
     * 1. ✅ @ConditionalOnBean ✅
     *
     * This method is ignored because the @ConditionalOnBean annotation is used,
     *
     * and the DataSource bean is not present in the ApplicationContext.
     *
     * @param dataSource
     * @return
     */
    @Bean
    @ConditionalOnBean(DataSource.class)
    public JdbcTemplate jdbcTemplateConditionalOnBean(DataSource dataSource) {
        System.out.println("----------------------------------------");
        System.out.println("##### 1. ✅ @ConditionalOnBean ✅ #####");
        System.out.println("----------------------------------------");
        System.out.println("##### • It only creates the JdbcTemplate bean if—and only if—the DataSource bean exists in the ApplicationContext.");
        System.out.println("##### • If it does not exist, Spring simply ignores that method.");
        System.out.println("##### • Spring searches in the ApplicationContext.");
        return new JdbcTemplate(dataSource);
    }

    /**
     * 2. ✅ @ConditionalOnMissingBean ✅
     *
     * This method is executed because the @ConditionalOnMissingBean annotation is used,
     *
     * and the DataSource bean is not present in the ApplicationContext.
     *
     * @param dataSource
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    public JdbcTemplate jdbcTemplateConditionalOnMissingBean(DataSource dataSource) {
        System.out.println("-----------------------------------------------");
        System.out.println("##### 2. ✅ @ConditionalOnMissingBean ✅ #####");
        System.out.println("-----------------------------------------------");
        System.out.println("##### • If the DataSource bean is missing (doesn't exist) in the ApplicationContext, create it.");
        System.out.println("##### • This method is executed when the DataSource bean doesn't exist in the ApplicationContext.");
        return new JdbcTemplate(dataSource);
    }

    /**
     * 3. ✅ @ConditionalOnClass ✅
     *
     * This method is executed because the @ConditionalOnClass annotation is used,
     *
     * and the DataSource class exists in the classpath.
     *
     * @param dataSource
     * @return
     */
    @Bean
    @ConditionalOnClass(DataSource.class)
    public JdbcTemplate jdbcTemplateConditionalOnClass(DataSource dataSource) {
        System.out.println("-----------------------------------------");
        System.out.println("##### 3. ✅ @ConditionalOnClass ✅ #####");
        System.out.println("-----------------------------------------");
        System.out.println("##### • It only creates the JdbcTemplate bean if—and only if—the DataSource class exists in the classpath.");
        System.out.println("##### • If it does not exist, Spring simply ignores that method.");
        System.out.println("##### • Spring searches in the classpath.");
        return new JdbcTemplate(dataSource);
    }

    /**
     * 4. ✅ @ConditionalOnMissingClass ✅
     *
     * This method is ignored because the @ConditionalOnMissingClass annotation is used,
     *
     * and the DataSource class exists in the classpath.
     *
     * @param dataSource
     * @return
     */
    @Bean
    @ConditionalOnMissingClass("javax.sql.DataSource")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        System.out.println("------------------------------------------------");
        System.out.println("##### 4. ✅ @ConditionalOnMissingClass ✅ #####");
        System.out.println("------------------------------------------------");
        System.out.println("##### • It only creates the CacheManager bean if—and only if—the javax.sql.DataSource class doesn't exist in the classpath.");
        System.out.println("##### • If it exists, Spring simply ignores that method.");
        System.out.println("##### • Spring searches in the classpath.");
        return new JdbcTemplate(dataSource);
    }

    /**
     * 5. ✅ @ConditionalOnProperty ✅
     *
     * This method is executed because the @ConditionalOnProperty annotation is used,
     *
     * and the "app.jdbc.enabled" property with "true" value exists in the Spring Environment.
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "app.jdbc.enabled", havingValue = "true")
    public String appJdbcEnabled() {
        System.out.println("--------------------------------------------");
        System.out.println("##### 5. ✅ @ConditionalOnProperty ✅ #####");
        System.out.println("--------------------------------------------");
        System.out.println("##### • If \"app.jdbc.enabled\" property with \"true\" value exists in the Spring Environment, the JdbcTemplate bean will be created.");
        System.out.println("##### • If \"app.jdbc.enabled\" property exists without value or doesn't exist, the method is ignored.");
        System.out.println("##### • Spring searches in the Environment, which resolves properties in this exact priority order:");
        System.out.println("#####   1. Devtools settings");
        System.out.println("#####   2. @TestPropertySource properties");
        System.out.println("#####   3. @SpringBootTest properties");
        System.out.println("#####   4. Command-Line Arguments (Terminal)");
        System.out.println("#####   5. SPRING_APPLICATION_JSON");
        System.out.println("#####   6. JVM System Properties");
        System.out.println("#####   7. OS Environment Variables");
        System.out.println("#####   8. ServletConfig / ServletContext parameters");
        System.out.println("#####   9. JNDI attributes from java:comp/env");
        System.out.println("#####   10. External application.properties (Fuera del JAR)");
        System.out.println("#####   11. Internal application.properties (Dentro del JAR)");
        System.out.println("#####   12. @PropertySource files");
        System.out.println("#####   13. Hardcoded Defaults (incluyendo SpringApplication.setDefaultProperties)");
        return "app.jdbc.enabled=true";
    }
}
