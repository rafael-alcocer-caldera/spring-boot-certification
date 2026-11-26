# 🛠 🚀 Spring Boot Certification 🚀 🛠
Exercises for Spring Certified Professional

## ✅ Conditionals ✅

### ✅ @Conditional annotations are used to specify conditions ✅
• Allow conditional bean creation


#### 1. ✅ @ConditionalOnBean ✅
Meaning: 
- "Create it only when another specific bean already exists in the context."

Explanation:
- It only creates the JdbcTemplate bean if—and only if—the DataSource bean exists in the ApplicationContext.
- If it does not exist, Spring simply ignores that method.
- Spring searches in the ApplicationContext.

```java
@Bean
@ConditionalOnBean(DataSource.class)
public JdbcTemplate jdbcTemplateConditionalOnBean(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
}
```

#### 2. ✅ @ConditionalOnMissingBean ✅
Meaning: 
- "Create it only when a bean of this type does NOT already exist." (Great for default configurations).

Explanation:
- If the DataSource bean is missing (doesn't exist) in the ApplicationContext, create it.
- This method is executed when the DataSource bean doesn't exist in the ApplicationContext.

```java
@Bean
@ConditionalOnMissingBean(DataSource.class)
public JdbcTemplate jdbcTemplateConditionalOnMissingBean(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
}
```

#### 3. ✅ @ConditionalOnClass ✅
Meaning: 
- "Create it only when this library/class is present in the project (classpath)."

Explanation:
- It only creates the JdbcTemplate bean if—and only if—the DataSource class exists in the classpath.
- If it does not exist, Spring simply ignores that method.
- Spring searches in the classpath.

```java
@Bean
@ConditionalOnClass(DataSource.class)
public JdbcTemplate jdbcTemplateConditionalOnClass(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
}
```

#### 4. ✅ @ConditionalOnMissingClass ✅
Meaning: 
- "Create it only when this library/class is NOT present in the project."

Explanation:
- It only creates the JdbcTemplate bean if—and only if—the javax.sql.DataSource class doesn't exist in the classpath.
- If it exists, Spring simply ignores that method.
- Spring searches in the classpath.

```java
@Bean
@ConditionalOnMissingClass("javax.sql.DataSource")
public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
}
```

(Note: The class name goes in quotes because if the class is missing, using .class wouldn't compile).

#### 5. ✅ @ConditionalOnProperty ✅
Meaning: 
- "Create it only when this property has a specific value in Spring's Environment."

Explanation:
- If "app.jdbc.enabled" property with "true" value exists in the Spring Environment, the JdbcTemplate bean will be created.
- If "app.jdbc.enabled" property exists without value or doesn't exist, the method is ignored.
- Spring searches in the Environment, which resolves properties in this exact priority order:
  1. Devtools settings
  2. @TestPropertySource properties
  3. @SpringBootTest properties
  4. Command-Line Arguments (Terminal)
  5. SPRING_APPLICATION_JSON
  6. JVM System Properties
  7. OS Environment Variables
  8. ServletConfig / ServletContext parameters
  9. JNDI attributes from java:comp/env
  10. External application.properties (Fuera del JAR)
  11. Internal application.properties (Dentro del JAR)
  12. @PropertySource files
  13. Hardcoded Defaults (incluyendo SpringApplication.setDefaultProperties)

```java
@Bean
@ConditionalOnProperty(name = "app.jdbc.enabled", havingValue = "true")
public String appJdbcEnabled() {
    return "app.jdbc.enabled=true";
}
```

#### 6. ✅ @Profile (Bonus from your notes) ✅
Meaning: 
- "Create it only when this specific environment profile is active."

Explanation:
- If the "dev" profile is active in Spring's Environment (e.g., spring.profiles.active=dev), the fakeDataSource bean will be created.
- If another profile is active (like "prod") or no profile is active, Spring ignores this method.
- Golden Rule: When "dev" is active, Spring loads all beans marked with @Profile("dev") PLUS all default beans that do NOT have any @Profile annotation.
- Spring searches in the Environment (active profiles).

```java
// Create a fake database ONLY WHEN running in the "dev" environment
@Bean
@Profile("dev")
public DataSource fakeDataSource() {
    return new H2DataSource();
}
```

ℹ️ @Conditional annotations are added in Spring 4.0.

ℹ️ @Profile is an example of conditional configuration

### ✅ Console output ✅
```log
/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/bin/java -XX:TieredStopAtLevel=1 -Dspring.output.ansi.enabled=always -javaagent:/Applications/IntelliJ IDEA CE.app/Contents/plugins/java/lib/idea_rt.jar=61442 -Dfile.encoding=UTF-8 -classpath /Users/rac/RAC/GitHub/spring-boot-certification/spring-boot-conditionals/target/classes:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-starter-jdbc/2.7.5/spring-boot-starter-jdbc-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-starter/2.7.5/spring-boot-starter-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot/2.7.5/spring-boot-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/spring-context/5.3.23/spring-context-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-aop/5.3.23/spring-aop-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-expression/5.3.23/spring-expression-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/2.7.5/spring-boot-autoconfigure-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-starter-logging/2.7.5/spring-boot-starter-logging-2.7.5.jar:/Users/rac/.m2/repository/ch/qos/logback/logback-classic/1.2.11/logback-classic-1.2.11.jar:/Users/rac/.m2/repository/ch/qos/logback/logback-core/1.2.11/logback-core-1.2.11.jar:/Users/rac/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.17.2/log4j-to-slf4j-2.17.2.jar:/Users/rac/.m2/repository/org/apache/logging/log4j/log4j-api/2.17.2/log4j-api-2.17.2.jar:/Users/rac/.m2/repository/org/slf4j/jul-to-slf4j/1.7.36/jul-to-slf4j-1.7.36.jar:/Users/rac/.m2/repository/jakarta/annotation/jakarta.annotation-api/1.3.5/jakarta.annotation-api-1.3.5.jar:/Users/rac/.m2/repository/org/springframework/spring-core/5.3.23/spring-core-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-jcl/5.3.23/spring-jcl-5.3.23.jar:/Users/rac/.m2/repository/org/yaml/snakeyaml/1.30/snakeyaml-1.30.jar:/Users/rac/.m2/repository/com/zaxxer/HikariCP/4.0.3/HikariCP-4.0.3.jar:/Users/rac/.m2/repository/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar:/Users/rac/.m2/repository/org/springframework/spring-jdbc/5.3.23/spring-jdbc-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-beans/5.3.23/spring-beans-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-tx/5.3.23/spring-tx-5.3.23.jar:/Users/rac/.m2/repository/org/hsqldb/hsqldb/2.5.2/hsqldb-2.5.2.jar rafael.alcocer.caldera.spring.boot.conditionals.Application

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.5)

2026-11-26 00:59:27.268  INFO 34137 --- [           main] r.a.c.s.boot.conditionals.Application    : Starting Application using Java 17.0.7 on MacBook-Pro-de-Rafael.local with PID 34137 (/Users/rac/RAC/GitHub/spring-boot-certification/spring-boot-conditionals/target/classes started by rac in /Users/rac/RAC/GitHub/spring-boot-certification)
2026-11-26 00:59:27.269  INFO 34137 --- [           main] r.a.c.s.boot.conditionals.Application    : No active profile set, falling back to 1 default profile: "default"
-----------------------------------------------
##### 2. ✅ @ConditionalOnMissingBean ✅ #####
-----------------------------------------------
##### • If the DataSource bean is missing (doesn't exist) in the ApplicationContext, create it.
##### • This method is executed when the DataSource bean doesn't exist in the ApplicationContext.
-----------------------------------------
##### 3. ✅ @ConditionalOnClass ✅ #####
-----------------------------------------
##### • It only creates the JdbcTemplate bean if—and only if—the DataSource class exists in the classpath.
##### • If it does not exist, Spring simply ignores that method.
##### • Spring searches in the classpath.
--------------------------------------------
##### 5. ✅ @ConditionalOnProperty ✅ #####
--------------------------------------------
##### • If "app.jdbc.enabled" property with "true" value exists in the Spring Environment, the JdbcTemplate bean will be created.
##### • If "app.jdbc.enabled" property exists without value or doesn't exist, the method is ignored.
##### • Spring searches in the Environment, which resolves properties in this exact priority order:
#####   1. Devtools settings
#####   2. @TestPropertySource properties
#####   3. @SpringBootTest properties
#####   4. Command-Line Arguments (Terminal)
#####   5. SPRING_APPLICATION_JSON
#####   6. JVM System Properties
#####   7. OS Environment Variables
#####   8. ServletConfig / ServletContext parameters
#####   9. JNDI attributes from java:comp/env
#####   10. External application.properties (Fuera del JAR)
#####   11. Internal application.properties (Dentro del JAR)
#####   12. @PropertySource files
#####   13. Hardcoded Defaults (incluyendo SpringApplication.setDefaultProperties)
2026-11-26 00:59:27.519  INFO 34137 --- [           main] r.a.c.s.boot.conditionals.Application    : Started Application in 5.444 seconds (JVM running for 5.585)

Process finished with exit code 0

```
