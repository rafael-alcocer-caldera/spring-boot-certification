# 🛠 🚀 Spring Boot Certification 🚀 🛠
Exercises for Spring Certified Professional

## ✅ Autoconfiguration ✅
We are using Maven for these exercises, of course you can do it in Gradle.

Autoconfiguration classes are usually applied based on your classpath and what beans you have defined.

Autoconfiguration tries to be as intelligent as possible and will back-away as you define more of your own configuration. 

You can always manually `exclude` any configuration that you never want to apply.
If the class is public and imported, you can use `exclude` in the `@SpringBootApplication` annotation.
If the class is package-private or not imported, the code will not compile:
```java
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class Application {...}

```

Use `excludeName` for unaccessible classes (package-private or optional classes).
If the class is package-private or not imported, you can use `excludeName` in the `@SpringBootApplication` annotation:
```java
@SpringBootApplication(excludeName = {
    "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
public class Application {...}
```

You can also exclude them via the `spring.autoconfigure.exclude` property.

**`Autoconfiguration is always applied after user-defined beans have been registered.`**

Generally auto-configuration beans are @Conditional beans (most often using `@ConditionalOnClass` and `@ConditionalOnMissingBean` annotations).

### ✅ Examples of Autoconfiguration: DataSource, JdbcTemplate ✅
```text

                                      At Runtime, 
   What you include              Spring Boot checks if             What Spring Boot does
------------------------------->------------------------------>------------------------------
   
┌────────────────────┐         ┌───────────────────────┐         ┌───────────────────────┐
│    Embedded DB     │         │        HSQLDB         │         │      DataSource       │
│     dependency     │────────>│   (or H2 or Derby)    │────────>│      configured       │
│                    │         │     on classpath      │         │                       │
└────────────────────┘         └───────────────────────┘         └───────────────────────┘

                                          /\
        ┌─────────────────────────────────────────────────────────────────┐
          DataSourceAutoConfiguration handles the auto-config logic above


                               ┌───────────────────────┐         ┌───────────────────────┐
                       ┌──────>│     If DataSource     │────────>│     JdbcTemplate      │
┌────────────────────┐ │       │      configured       │         │      configured       │
│                    │─┘       └───────────────────────┘         └───────────────────────┘
│   Starter JDBC     │         ┌───────────────────────┐         ┌───────────────────────┐
│                    │────────>│     If DataSource     │────────>│    Runtime Error:     │
└────────────────────┘         │    not configured     │         │    JDBC found with    │
                               └───────────────────────┘         │     no DataSource     │
                                                                 └───────────────────────┘

                                          /\
        ┌───────────────────────────────────────────────────────────────────┐
          JdbcTemplateAutoConfiguration handles the auto-config logic above

```

#### DataSource
- When you include the embedded database dependency in your pom file, HSQLDB for example, HSQLDB will be pulled into the classpath.

- And just because you have this dependency in the classpath, Spring will configure a DataSource for you.

- It will configure a DataSource of an embedded database.

#### JdbcTemplae
- The same will happen when you define the Starter JDBC in the classpath or in the pom file.

- This will pull everything in the classpath.

- And if you have a DataSource configured, Spring Boot will auto-configure the JdbcTemplate for you.

- If you don't have a DataSource, this will throw an exception, Spring Boot will complain.

### ✅ Application main class ✅
```java
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
// @SpringBootApplication // This one includes the above three
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

- SpringApplication is actually a Spring Boot class.

### ✅ @SpringBootConfiguration ✅
Indicates that a class provides Spring Boot application `@Configuration`. 

Can be used as an alternative to the Spring's standard `@Configuration` annotation so that configuration can be found automatically (for example in tests).

Application should only ever include one `@SpringBootConfiguration` and most idiomatic Spring Boot applications will inherit it from `@SpringBootApplication`.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Indexed
public @interface SpringBootConfiguration {...}
```

### ✅ @EnableAutoConfiguration ✅
This is where all the magic happens.

This triggers the `@Configuration` of your application.

```java
@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {...}
```

### ✅ @ComponentScan ✅
Configures component scanning directives for use with `@Configuration` classes. 

Provides support parallel with Spring XML's `<context:component-scan>` element.

Either `basePackageClasses` or `basePackages` (or its alias `value`) may be specified to define specific packages to scan. 

If specific packages are not defined, scanning will occur from the package of the class that declares this annotation.

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(ComponentScans.class)
public @interface ComponentScan {...}
```

### ✅ @SpringBootApplication ✅
`@SpringBootApplication(scanBasePackages="example.config")` allows you to define the base package to the component scanning.

If you omit that package, what it will do is to scan from where you are.

Basically the base package where the `@SpringBootApplication` is configured will be where we start component scanning.

This package plus the sub-packages will scan the components.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
public @interface SpringBootApplication {...}
```

You'll see all these three annotations used in combination usually together:
----------------------------------------------------------------------------
1. @SpringBootConfiguration
2. @EnableAutoConfiguration
3. @ComponentScan("example.config")

So, instead of defining all these three annotations together, what we usually do is to combine them into one annotation called `@SpringBootApplication`.

This:
```java
@SpringBootApplication(scanBasePackages = "example.config")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

Is equivalent to this:
```java
@SpringBootApplication
@ComponentScan(basePackages = "example.config")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### ✅ Console output ✅

####  ✅ When DataSource is on classpath ✅
```text

                                      At Runtime, 
   What you include              Spring Boot checks if             What Spring Boot does
------------------------------->------------------------------>------------------------------
   
┌────────────────────┐         ┌───────────────────────┐         ┌───────────────────────┐
│    Embedded DB     │         │        HSQLDB         │         │      DataSource       │
│     dependency     │────────>│   (or H2 or Derby)    │────────>│      configured       │
│                    │         │     on classpath      │         │                       │
└────────────────────┘         └───────────────────────┘         └───────────────────────┘

                                          /\
        ┌─────────────────────────────────────────────────────────────────┐
          DataSourceAutoConfiguration handles the auto-config logic above

```

The above means that we have the dependency in pom.xml, and Spring Boot will configure a DataSource for us.
```
    <dependencies>
        <dependency>
            <groupId>org.hsqldb</groupId>
            <artifactId>hsqldb</artifactId>
        </dependency>
    </dependencies>
```

#### ✅ When DataSource is not on classpath and only JdbcTemplate is configured automatically by Spring Boot ✅
```text

                                      At Runtime, 
   What you include              Spring Boot checks if             What Spring Boot does
------------------------------->------------------------------>------------------------------
                       
┌────────────────────┐         ┌───────────────────────┐         ┌───────────────────────┐
│                    │         │     If DataSource     │         │    Runtime Error:     │
│    Starter JDBC    │────────>│    not configured     │────────>│    JDBC found with    │
│                    │         │                       │         │     no DataSource     │
└────────────────────┘         └───────────────────────┘         └───────────────────────┘

```

The above means that we only have the Starter JDBC in the classpath, and Spring Boot will throw an exception because it cannot configure a DataSource for us.
```
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
    </dependencies>
```

```java
@Configuration
public class AppConfig {
    
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
```

If we try to run the application with only the Starter JDBC in the classpath with the above AppConfig, Spring Boot will throw an exception because it cannot configure a DataSource for us.
```log
/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/bin/java -XX:TieredStopAtLevel=1 -Dspring.output.ansi.enabled=always -javaagent:/Applications/IntelliJ IDEA CE.app/Contents/plugins/java/lib/idea_rt.jar=64403 -Dfile.encoding=UTF-8 -classpath /Users/rac/RAC/GitHub/spring-boot-certification/spring-boot-autoconfiguration/target/classes:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-starter-jdbc/2.7.5/spring-boot-starter-jdbc-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-starter/2.7.5/spring-boot-starter-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot/2.7.5/spring-boot-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/spring-context/5.3.23/spring-context-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-aop/5.3.23/spring-aop-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-expression/5.3.23/spring-expression-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/2.7.5/spring-boot-autoconfigure-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-starter-logging/2.7.5/spring-boot-starter-logging-2.7.5.jar:/Users/rac/.m2/repository/ch/qos/logback/logback-classic/1.2.11/logback-classic-1.2.11.jar:/Users/rac/.m2/repository/ch/qos/logback/logback-core/1.2.11/logback-core-1.2.11.jar:/Users/rac/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.17.2/log4j-to-slf4j-2.17.2.jar:/Users/rac/.m2/repository/org/apache/logging/log4j/log4j-api/2.17.2/log4j-api-2.17.2.jar:/Users/rac/.m2/repository/org/slf4j/jul-to-slf4j/1.7.36/jul-to-slf4j-1.7.36.jar:/Users/rac/.m2/repository/jakarta/annotation/jakarta.annotation-api/1.3.5/jakarta.annotation-api-1.3.5.jar:/Users/rac/.m2/repository/org/springframework/spring-core/5.3.23/spring-core-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-jcl/5.3.23/spring-jcl-5.3.23.jar:/Users/rac/.m2/repository/org/yaml/snakeyaml/1.30/snakeyaml-1.30.jar:/Users/rac/.m2/repository/com/zaxxer/HikariCP/4.0.3/HikariCP-4.0.3.jar:/Users/rac/.m2/repository/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar:/Users/rac/.m2/repository/org/springframework/spring-jdbc/5.3.23/spring-jdbc-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-beans/5.3.23/spring-beans-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-tx/5.3.23/spring-tx-5.3.23.jar rafael.alcocer.caldera.spring.boot.autoconfiguration.Application

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.5)

2026-11-20 08:55:18.052  INFO 65931 --- [           main] r.a.c.s.b.autoconfiguration.Application  : Starting Application using Java 17.0.7 on MacBook-Pro-de-Rafael.local with PID 65931 (/Users/rac/RAC/GitHub/spring-boot-certification/spring-boot-autoconfiguration/target/classes started by rac in /Users/rac/RAC/GitHub/spring-boot-certification)
2026-11-20 08:55:18.053  INFO 65931 --- [           main] r.a.c.s.b.autoconfiguration.Application  : No active profile set, falling back to 1 default profile: "default"
2026-11-20 08:55:18.267  WARN 65931 --- [           main] s.c.a.AnnotationConfigApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'dataSourceScriptDatabaseInitializer' defined in class path resource [org/springframework/boot/autoconfigure/sql/init/DataSourceInitializationConfiguration.class]: Unsatisfied dependency expressed through method 'dataSourceScriptDatabaseInitializer' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'dataSource' defined in class path resource [org/springframework/boot/autoconfigure/jdbc/DataSourceConfiguration$Hikari.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.zaxxer.hikari.HikariDataSource]: Factory method 'dataSource' threw exception; nested exception is org.springframework.boot.autoconfigure.jdbc.DataSourceProperties$DataSourceBeanCreationException: Failed to determine a suitable driver class
2026-11-20 08:55:18.271  INFO 65931 --- [           main] ConditionEvaluationReportLoggingListener : 

Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
2026-11-20 08:55:18.281 ERROR 65931 --- [           main] o.s.b.d.LoggingFailureAnalysisReporter   : 

***************************
APPLICATION FAILED TO START
***************************

Description:

Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

Reason: Failed to determine a suitable driver class


Action:

Consider the following:
	If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
	If you have database settings to be loaded from a particular profile you may need to activate it (no profiles are currently active).


Process finished with exit code 1

```

Now, lets suppose that we add the DataSource bean to the AppConfig class, without DataSource on classpath and only the Starter JDBC in the classpath:
```java
@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                // .addScript("classpath:schema.sql")
                // .addScript("classpath:data.sql")
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
```

If we try to run the application with only the Starter JDBC in the classpath with the above AppConfig, Spring Boot will throw an exception because it cannot configure a DataSource for us.
```log
/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/bin/java -XX:TieredStopAtLevel=1 -Dspring.output.ansi.enabled=always -javaagent:/Applications/IntelliJ IDEA CE.app/Contents/plugins/java/lib/idea_rt.jar=64094 -Dfile.encoding=UTF-8 -classpath /Users/rac/RAC/GitHub/spring-boot-certification/spring-boot-autoconfiguration/target/classes:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-starter-jdbc/2.7.5/spring-boot-starter-jdbc-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-starter/2.7.5/spring-boot-starter-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot/2.7.5/spring-boot-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/spring-context/5.3.23/spring-context-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-aop/5.3.23/spring-aop-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-expression/5.3.23/spring-expression-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/2.7.5/spring-boot-autoconfigure-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-starter-logging/2.7.5/spring-boot-starter-logging-2.7.5.jar:/Users/rac/.m2/repository/ch/qos/logback/logback-classic/1.2.11/logback-classic-1.2.11.jar:/Users/rac/.m2/repository/ch/qos/logback/logback-core/1.2.11/logback-core-1.2.11.jar:/Users/rac/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.17.2/log4j-to-slf4j-2.17.2.jar:/Users/rac/.m2/repository/org/apache/logging/log4j/log4j-api/2.17.2/log4j-api-2.17.2.jar:/Users/rac/.m2/repository/org/slf4j/jul-to-slf4j/1.7.36/jul-to-slf4j-1.7.36.jar:/Users/rac/.m2/repository/jakarta/annotation/jakarta.annotation-api/1.3.5/jakarta.annotation-api-1.3.5.jar:/Users/rac/.m2/repository/org/springframework/spring-core/5.3.23/spring-core-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-jcl/5.3.23/spring-jcl-5.3.23.jar:/Users/rac/.m2/repository/org/yaml/snakeyaml/1.30/snakeyaml-1.30.jar:/Users/rac/.m2/repository/com/zaxxer/HikariCP/4.0.3/HikariCP-4.0.3.jar:/Users/rac/.m2/repository/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar:/Users/rac/.m2/repository/org/springframework/spring-jdbc/5.3.23/spring-jdbc-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-beans/5.3.23/spring-beans-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-tx/5.3.23/spring-tx-5.3.23.jar rafael.alcocer.caldera.spring.boot.autoconfiguration.Application

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.5)

2026-11-20 08:22:33.910  INFO 63271 --- [           main] r.a.c.s.b.autoconfiguration.Application  : Starting Application using Java 17.0.7 on MacBook-Pro-de-Rafael.local with PID 63271 (/Users/rac/RAC/GitHub/spring-boot-certification/spring-boot-autoconfiguration/target/classes started by rac in /Users/rac/RAC/GitHub/spring-boot-certification)
2026-11-20 08:22:33.911  INFO 63271 --- [           main] r.a.c.s.b.autoconfiguration.Application  : No active profile set, falling back to 1 default profile: "default"
2026-11-20 08:22:34.114  WARN 63271 --- [           main] s.c.a.AnnotationConfigApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'dataSource' defined in class path resource [rafael/alcocer/caldera/spring/boot/autoconfiguration/config/AppConfig.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [javax.sql.DataSource]: Factory method 'dataSource' threw exception; nested exception is java.lang.IllegalStateException: Driver for test database type [HSQL] is not available
2026-11-20 08:22:34.118  INFO 63271 --- [           main] ConditionEvaluationReportLoggingListener : 

Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
2026-11-20 08:22:34.133 ERROR 63271 --- [           main] o.s.boot.SpringApplication               : Application run failed

org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'dataSource' defined in class path resource [rafael/alcocer/caldera/spring/boot/autoconfiguration/config/AppConfig.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [javax.sql.DataSource]: Factory method 'dataSource' threw exception; nested exception is java.lang.IllegalStateException: Driver for test database type [HSQL] is not available
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:658) ~[spring-beans-5.3.23.jar:5.3.23]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:486) ~[spring-beans-5.3.23.jar:5.3.23]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1352) ~[spring-beans-5.3.23.jar:5.3.23]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1195) ~[spring-beans-5.3.23.jar:5.3.23]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:582) ~[spring-beans-5.3.23.jar:5.3.23]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:542) ~[spring-beans-5.3.23.jar:5.3.23]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:335) ~[spring-beans-5.3.23.jar:5.3.23]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-5.3.23.jar:5.3.23]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:333) ~[spring-beans-5.3.23.jar:5.3.23]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:208) ~[spring-beans-5.3.23.jar:5.3.23]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:955) ~[spring-beans-5.3.23.jar:5.3.23]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:918) ~[spring-context-5.3.23.jar:5.3.23]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:583) ~[spring-context-5.3.23.jar:5.3.23]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:734) ~[spring-boot-2.7.5.jar:2.7.5]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:408) ~[spring-boot-2.7.5.jar:2.7.5]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:308) ~[spring-boot-2.7.5.jar:2.7.5]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1306) ~[spring-boot-2.7.5.jar:2.7.5]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1295) ~[spring-boot-2.7.5.jar:2.7.5]
	at rafael.alcocer.caldera.spring.boot.autoconfiguration.Application.main(Application.java:16) ~[classes/:na]
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [javax.sql.DataSource]: Factory method 'dataSource' threw exception; nested exception is java.lang.IllegalStateException: Driver for test database type [HSQL] is not available
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:185) ~[spring-beans-5.3.23.jar:5.3.23]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:653) ~[spring-beans-5.3.23.jar:5.3.23]
	... 18 common frames omitted
Caused by: java.lang.IllegalStateException: Driver for test database type [HSQL] is not available
	at org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurerFactory.getConfigurer(EmbeddedDatabaseConfigurerFactory.java:57) ~[spring-jdbc-5.3.23.jar:5.3.23]
	at org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory.setDatabaseType(EmbeddedDatabaseFactory.java:132) ~[spring-jdbc-5.3.23.jar:5.3.23]
	at org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder.setType(EmbeddedDatabaseBuilder.java:121) ~[spring-jdbc-5.3.23.jar:5.3.23]
	at rafael.alcocer.caldera.spring.boot.autoconfiguration.config.AppConfig.dataSource(AppConfig.java:17) ~[classes/:na]
	at rafael.alcocer.caldera.spring.boot.autoconfiguration.config.AppConfig$$EnhancerBySpringCGLIB$$55a9e746.CGLIB$dataSource$0(<generated>) ~[classes/:na]
	at rafael.alcocer.caldera.spring.boot.autoconfiguration.config.AppConfig$$EnhancerBySpringCGLIB$$55a9e746$$FastClassBySpringCGLIB$$3372974a.invoke(<generated>) ~[classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invokeSuper(MethodProxy.java:244) ~[spring-core-5.3.23.jar:5.3.23]
	at org.springframework.context.annotation.ConfigurationClassEnhancer$BeanMethodInterceptor.intercept(ConfigurationClassEnhancer.java:331) ~[spring-context-5.3.23.jar:5.3.23]
	at rafael.alcocer.caldera.spring.boot.autoconfiguration.config.AppConfig$$EnhancerBySpringCGLIB$$55a9e746.dataSource(<generated>) ~[classes/:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:568) ~[na:na]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:154) ~[spring-beans-5.3.23.jar:5.3.23]
	... 19 common frames omitted
Caused by: java.lang.ClassNotFoundException: org.hsqldb.jdbcDriver
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641) ~[na:na]
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188) ~[na:na]
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:520) ~[na:na]
	at java.base/java.lang.Class.forName0(Native Method) ~[na:na]
	at java.base/java.lang.Class.forName(Class.java:467) ~[na:na]
	at org.springframework.util.ClassUtils.forName(ClassUtils.java:284) ~[spring-core-5.3.23.jar:5.3.23]
	at org.springframework.jdbc.datasource.embedded.HsqlEmbeddedDatabaseConfigurer.getInstance(HsqlEmbeddedDatabaseConfigurer.java:50) ~[spring-jdbc-5.3.23.jar:5.3.23]
	at org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurerFactory.getConfigurer(EmbeddedDatabaseConfigurerFactory.java:47) ~[spring-jdbc-5.3.23.jar:5.3.23]
	... 32 common frames omitted


Process finished with exit code 1
```

#### ✅ When both DataSource and JdbcTemplate are on classpath configured automatically by Spring Boot ✅
```text

                                      At Runtime, 
   What you include              Spring Boot checks if             What Spring Boot does
------------------------------->------------------------------>------------------------------

┌────────────────────┐         ┌───────────────────────┐         ┌───────────────────────┐
│    Embedded DB     │         │        HSQLDB         │         │      DataSource       │
│     dependency     │────────>│   (or H2 or Derby)    │────────>│      configured       │
│                    │         │     on classpath      │         │                       │
└────────────────────┘         └───────────────────────┘         └───────────────────────┘

                                          /\
        ┌─────────────────────────────────────────────────────────────────┐
          DataSourceAutoConfiguration handles the auto-config logic above




┌────────────────────┐         ┌───────────────────────┐         ┌───────────────────────┐
│                    │         │          If           │         │      JdbcTemplate     │
│    Starter JDBC    │────────>│       DataSource      │────────>│      configured       │
│                    │         │       configured      │         │                       │
└────────────────────┘         └───────────────────────┘         └───────────────────────┘

                                          /\
        ┌───────────────────────────────────────────────────────────────────┐
          JdbcTemplateAutoConfiguration handles the auto-config logic above

```

The above means that when both `DataSource` and `JdbcTemplate` are present on the classpath, Spring Boot will automatically configure them for you.
```
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.hsqldb</groupId>
            <artifactId>hsqldb</artifactId>
        </dependency>
    </dependencies>
```

And we can also define our own configuration for the DataSource and JdbcTemplate, and Spring Boot will back away from its autoconfiguration.
```java
@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                // .addScript("classpath:schema.sql")
                // .addScript("classpath:data.sql")
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
```

```log
/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/bin/java -XX:TieredStopAtLevel=1 -Dspring.output.ansi.enabled=always -javaagent:/Applications/IntelliJ IDEA CE.app/Contents/plugins/java/lib/idea_rt.jar=63528 -Dfile.encoding=UTF-8 -classpath /Users/rac/RAC/GitHub/spring-boot-certification/spring-boot-autoconfiguration/target/classes:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-starter-jdbc/2.7.5/spring-boot-starter-jdbc-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-starter/2.7.5/spring-boot-starter-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot/2.7.5/spring-boot-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/spring-context/5.3.23/spring-context-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-aop/5.3.23/spring-aop-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-expression/5.3.23/spring-expression-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/2.7.5/spring-boot-autoconfigure-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-starter-logging/2.7.5/spring-boot-starter-logging-2.7.5.jar:/Users/rac/.m2/repository/ch/qos/logback/logback-classic/1.2.11/logback-classic-1.2.11.jar:/Users/rac/.m2/repository/ch/qos/logback/logback-core/1.2.11/logback-core-1.2.11.jar:/Users/rac/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.17.2/log4j-to-slf4j-2.17.2.jar:/Users/rac/.m2/repository/org/apache/logging/log4j/log4j-api/2.17.2/log4j-api-2.17.2.jar:/Users/rac/.m2/repository/org/slf4j/jul-to-slf4j/1.7.36/jul-to-slf4j-1.7.36.jar:/Users/rac/.m2/repository/jakarta/annotation/jakarta.annotation-api/1.3.5/jakarta.annotation-api-1.3.5.jar:/Users/rac/.m2/repository/org/springframework/spring-core/5.3.23/spring-core-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-jcl/5.3.23/spring-jcl-5.3.23.jar:/Users/rac/.m2/repository/org/yaml/snakeyaml/1.30/snakeyaml-1.30.jar:/Users/rac/.m2/repository/com/zaxxer/HikariCP/4.0.3/HikariCP-4.0.3.jar:/Users/rac/.m2/repository/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar:/Users/rac/.m2/repository/org/springframework/spring-jdbc/5.3.23/spring-jdbc-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-beans/5.3.23/spring-beans-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-tx/5.3.23/spring-tx-5.3.23.jar:/Users/rac/.m2/repository/org/hsqldb/hsqldb/2.5.2/hsqldb-2.5.2.jar rafael.alcocer.caldera.spring.boot.autoconfiguration.Application

.   ____          _            __ _ _
/\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
\\/  ___)| |_)| | | | | || (_| |  ) ) ) )
'  |____| .__|_| |_|_| |_\__, | / / / /
=========|_|==============|___/=/_/_/_/
:: Spring Boot ::                (v2.7.5)

2026-11-20 05:57:06.212  INFO 61286 --- [           main] r.a.c.s.b.autoconfiguration.Application  : Starting Application using Java 17.0.7 on MacBook-Pro-de-Rafael.local with PID 61286 (/Users/rac/RAC/GitHub/spring-boot-certification/spring-boot-autoconfiguration/target/classes started by rac in /Users/rac/RAC/GitHub/spring-boot-certification)
2026-11-20 05:57:06.213  INFO 61286 --- [           main] r.a.c.s.b.autoconfiguration.Application  : No active profile set, falling back to 1 default profile: "default"
2026-11-20 05:57:06.424  INFO 61286 --- [           main] o.s.j.d.e.EmbeddedDatabaseFactory        : Starting embedded database: url='jdbc:hsqldb:mem:testdb', username='sa'
2026-11-20 05:57:06.584  INFO 61286 --- [           main] r.a.c.s.b.autoconfiguration.Application  : Started Application in 5.547 seconds (JVM running for 5.728)
2026-11-20 05:57:06.586  INFO 61286 --- [ionShutdownHook] o.s.j.d.e.EmbeddedDatabaseFactory        : Shutting down embedded database: url='jdbc:hsqldb:mem:testdb'

Process finished with exit code 0
```

#### ✅ When both DataSource and JdbcTemplate are on classpath configured automatically by Spring Boot and we add JdbTemplate ✅
```java
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
// @SpringBootApplication // This one includes the above three
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner run(JdbcTemplate jdbcTemplate) {
        return args -> {
            jdbcTemplate.execute("CREATE TABLE customer (id INT PRIMARY KEY, name VARCHAR(50), email VARCHAR(50))");

            jdbcTemplate.update("INSERT INTO customer (id, name, email) VALUES (?, ?, ?)", 1, "Rafael", "rafael@example.com");
            jdbcTemplate.update("INSERT INTO customer (id, name, email) VALUES (?, ?, ?)", 2, "Ana", "ana@example.com");

            System.out.println(jdbcTemplate.queryForList("SELECT * FROM customer"));
        };
    }
}
```

```log
/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/bin/java -XX:TieredStopAtLevel=1 -Dspring.output.ansi.enabled=always -javaagent:/Applications/IntelliJ IDEA CE.app/Contents/plugins/java/lib/idea_rt.jar=61118 -Dfile.encoding=UTF-8 -classpath /Users/rac/RAC/GitHub/spring-boot-certification/spring-boot-autoconfiguration/target/classes:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-starter-jdbc/2.7.5/spring-boot-starter-jdbc-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-starter/2.7.5/spring-boot-starter-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot/2.7.5/spring-boot-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/spring-context/5.3.23/spring-context-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-aop/5.3.23/spring-aop-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-expression/5.3.23/spring-expression-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/2.7.5/spring-boot-autoconfigure-2.7.5.jar:/Users/rac/.m2/repository/org/springframework/boot/spring-boot-starter-logging/2.7.5/spring-boot-starter-logging-2.7.5.jar:/Users/rac/.m2/repository/ch/qos/logback/logback-classic/1.2.11/logback-classic-1.2.11.jar:/Users/rac/.m2/repository/ch/qos/logback/logback-core/1.2.11/logback-core-1.2.11.jar:/Users/rac/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.17.2/log4j-to-slf4j-2.17.2.jar:/Users/rac/.m2/repository/org/apache/logging/log4j/log4j-api/2.17.2/log4j-api-2.17.2.jar:/Users/rac/.m2/repository/org/slf4j/jul-to-slf4j/1.7.36/jul-to-slf4j-1.7.36.jar:/Users/rac/.m2/repository/jakarta/annotation/jakarta.annotation-api/1.3.5/jakarta.annotation-api-1.3.5.jar:/Users/rac/.m2/repository/org/springframework/spring-core/5.3.23/spring-core-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-jcl/5.3.23/spring-jcl-5.3.23.jar:/Users/rac/.m2/repository/org/yaml/snakeyaml/1.30/snakeyaml-1.30.jar:/Users/rac/.m2/repository/com/zaxxer/HikariCP/4.0.3/HikariCP-4.0.3.jar:/Users/rac/.m2/repository/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar:/Users/rac/.m2/repository/org/springframework/spring-jdbc/5.3.23/spring-jdbc-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-beans/5.3.23/spring-beans-5.3.23.jar:/Users/rac/.m2/repository/org/springframework/spring-tx/5.3.23/spring-tx-5.3.23.jar:/Users/rac/.m2/repository/org/hsqldb/hsqldb/2.5.2/hsqldb-2.5.2.jar rafael.alcocer.caldera.spring.boot.autoconfiguration.Application

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.5)

2026-11-22 13:26:05.384  INFO 52144 --- [           main] r.a.c.s.b.autoconfiguration.Application  : Starting Application using Java 17.0.7 on MacBook-Pro-de-Rafael.local with PID 52144 (/Users/rac/RAC/GitHub/spring-boot-certification/spring-boot-autoconfiguration/target/classes started by rac in /Users/rac/RAC/GitHub/spring-boot-certification)
2026-11-22 13:26:05.385  INFO 52144 --- [           main] r.a.c.s.b.autoconfiguration.Application  : No active profile set, falling back to 1 default profile: "default"
2026-11-22 13:26:05.578  INFO 52144 --- [           main] o.s.j.d.e.EmbeddedDatabaseFactory        : Starting embedded database: url='jdbc:hsqldb:mem:testdb', username='sa'
2026-11-22 13:26:05.722  INFO 52144 --- [           main] r.a.c.s.b.autoconfiguration.Application  : Started Application in 5.499 seconds (JVM running for 5.634)
[{ID=1, NAME=Rafael, EMAIL=rafael@example.com}, {ID=2, NAME=Ana, EMAIL=ana@example.com}]
2026-11-22 13:26:05.730  INFO 52144 --- [ionShutdownHook] o.s.j.d.e.EmbeddedDatabaseFactory        : Shutting down embedded database: url='jdbc:hsqldb:mem:testdb'

Process finished with exit code 0
```

### ✅ What are Auto-Configuration Classes? ✅
- Pre-written Spring configurations
  – org.springframework.boot.autoconfigure package
  – See spring-boot-autoconfigure JAR file
- Best place to check what they exactly do

```java
@AutoConfiguration
...
public class DataSourceAutoConfiguration {
    ...
                        ┌────────────────────┐
                        │ Auto-configure     │
                        │ DataSource bean    │
                        │ only if it is not  │
                        │ already configured │
                        └────────────────────┘
    @Conditional(...)           \/ 
    @ConditionalOnMissingBean(DataSource.class, ..)
    @Import({EmbeddedDataSourceConfiguration.class})
    protected static class EmbeddedDatabaseConfiguration { ... }
    ...
}
```

ℹ️ Spring Boot defines many of these configurations. They 
   activate in response to dependencies on the classpath

### ✅ Where are these Auto-Configuration classes specified? ✅
- @EnableAutoConfiguration processes autoconfiguration:
  – @AutoConfiguration annotation specifies a class explicitly for autoconfiguration
  – spring-boot-autoconfigure/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
  
- AutoConfiguration classes used by Spring Boot.
  – May be overridden 
  
- Auto-configuration classes processed after explicitly created beans are defined
  – Beans you define always take precedence over AutoConfiguration

#### 💡 Nota técnica clave para tu examen (Spring Boot 2.7.5):
Este slide muestra un cambio histórico importantísimo en el framework. 
En versiones anteriores (como Spring Boot 2.6 hacia atrás), las autoconfiguraciones se registraban en un archivo llamado spring.factories. 
A partir de Spring Boot 2.7, se utiliza el nuevo archivo empaquetado dentro del JAR que se menciona en el slide: org.springframework.boot.autoconfigure.AutoConfiguration.imports. Es una pregunta fija de examen sobre la arquitectura del framework.

### ✅ Override Default Configuration ✅
Controlling Spring Boot Auto-Configuration

- Spring Boot is designed to make overriding easy

- There are several options
  1. Set some of Spring Boot's properties
  2. Explicitly define beans yourself so Spring Boot won't
  3. Explicitly disable some auto-configuration
  4. Change dependencies or their versions

### ✅ Summary ✅
- Properties can be set in application.properties / yml

- @ConfigurationProperties simplifies handling of large # of properties

- Spring Boot auto-configuration is enabled by @EnableAutoConfiguration

- Spring Boot auto-configuration is managed by a set of auto-configuration classes, which uses @Conditional's

- AutoConfiguration can be controlled by
  1. Properties
  2. Explicit bean definition
  3. Disabling of auto configuration
  4. Altering dependency versions
