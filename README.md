# Spring Boot Up - Embedded Keycloak

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.wnameless.spring.boot.up/spring-boot-up-embedded-keycloak.svg)](https://search.maven.org/artifact/com.github.wnameless.spring.boot.up/spring-boot-up-embedded-keycloak)

A Spring Boot library that provides an embedded Keycloak server for development and testing purposes. Run Keycloak directly within your Spring Boot application without external dependencies.

## Features

- 🚀 **Zero Configuration** - Works out of the box with sensible defaults
- 🔧 **Fully Configurable** - Customize all aspects via Spring Boot properties
- 🗄️ **Multiple Database Support** - H2 (default), PostgreSQL, MySQL, and more
- 📦 **Realm Import** - Automatically import realm configurations on startup
- 🔐 **Admin User Creation** - Auto-create admin user for master realm
- 🌐 **RESTEasy Integration** - Full JAX-RS 3.0 support with Jakarta EE

## Related Projects

### Spring Boot Up - Keycloak Plugin

If you need a **fully Spring-integrated authentication solution** with Keycloak, rather than just a standalone embedded Keycloak server, check out the [Spring Boot Up - Keycloak Plugin](https://github.com/wnameless/spring-boot-up-keycloak-plugin) project.

**Key differences:**
- **This project** (`spring-boot-up-embedded-keycloak`): Provides a standalone embedded Keycloak server within your Spring Boot application. Perfect for development/testing when you need a full Keycloak instance.
- **Keycloak Plugin** (`spring-boot-up-keycloak-plugin`): Offers complete Spring Security integration with the embedded Keycloak server, including authentication filters, security configurations, and seamless Spring Boot integration for production use.

Choose this project if you need just an embedded Keycloak server for development/testing. Choose the Keycloak Plugin if you need full Spring Security integration with the embedded Keycloak server.

## Version Numbering

This library uses a 4-digit versioning scheme: `KEYCLOAK.SPRINGBOOT.MAJOR.MINOR`

For example, version `24.3.0.0` means:
- **24** - Keycloak major version
- **3** - Spring Boot major version  
- **0.0** - Library version (major.minor)

This scheme makes it easy to identify compatibility at a glance, with Keycloak version first since it changes more frequently than Spring Boot.

## Requirements

- Java 17 or higher
- Spring Boot 3.5.5 or higher
- Keycloak 24.0.5 (included)

## Quick Start

### 1. Add Dependency

#### Maven
```xml
<dependency>
    <groupId>com.github.wnameless.spring.boot.up</groupId>
    <artifactId>spring-boot-up-embedded-keycloak</artifactId>
    <version>24.3.0.0</version>
</dependency>
```

#### Gradle
```gradle
implementation 'com.github.wnameless.spring.boot.up:spring-boot-up-embedded-keycloak:24.3.0.0'
```

### 2. Enable Embedded Keycloak

Add the `@EnableEmbeddedKeycloak` annotation to your Spring Boot application:

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.github.wnameless.spring.boot.up.embedded.keycloak.config.EnableEmbeddedKeycloak;

@EnableEmbeddedKeycloak
@SpringBootApplication
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
```

### 3. Run Your Application

Start your Spring Boot application and access Keycloak at: `http://localhost:8080/auth`

Default credentials:
- Username: `admin`
- Password: `admin`

## Configuration

Configure the embedded Keycloak server via `application.properties` or `application.yml`:

### Basic Configuration

```properties
# Server context path
keycloak.server.context-path=/auth

# Admin credentials
keycloak.server.admin-user.username=admin
keycloak.server.admin-user.password=admin

# Realm import file (must be in classpath)
keycloak.server.realm-import-file=keycloak-realm.json
```

### Database Configuration

By default, an in-memory H2 database is used. To configure a persistent database:

```properties
# H2 file database
keycloak.connections-jpa.url=jdbc:h2:file:./keycloak-db

# PostgreSQL
keycloak.connections-jpa.url=jdbc:postgresql://localhost:5432/keycloak
keycloak.connections-jpa.driver=org.postgresql.Driver
keycloak.connections-jpa.driverDialect=org.hibernate.dialect.PostgreSQLDialect
keycloak.connections-jpa.user=keycloak
keycloak.connections-jpa.password=keycloak

# MySQL
keycloak.connections-jpa.url=jdbc:mysql://localhost:3306/keycloak
keycloak.connections-jpa.driver=com.mysql.cj.jdbc.Driver
keycloak.connections-jpa.driverDialect=org.hibernate.dialect.MySQLDialect
keycloak.connections-jpa.user=keycloak
keycloak.connections-jpa.password=keycloak
```

### Advanced Configuration

```properties
# Show SQL statements
keycloak.connections-jpa.showSql=true

# Format SQL statements
keycloak.connections-jpa.formatSql=true

# Global statistics interval (seconds)
keycloak.connections-jpa.globalStatsInterval=3600
```

## Realm Configuration

To import a realm on startup, place your realm JSON file in the classpath (e.g., `src/main/resources/keycloak-realm.json`):

```json
{
  "realm": "my-realm",
  "enabled": true,
  "clients": [
    {
      "clientId": "my-app",
      "enabled": true,
      "publicClient": true,
      "redirectUris": ["http://localhost:8080/*"]
    }
  ],
  "users": [
    {
      "username": "testuser",
      "enabled": true,
      "credentials": [
        {
          "type": "password",
          "value": "password"
        }
      ]
    }
  ]
}
```

## Custom Keycloak Server Configuration

For advanced Keycloak configuration, create a `keycloak-server.json` file in your classpath. The library will automatically detect and use it instead of the default configuration template.

## Use Cases

### Development Environment

Perfect for local development where you need a fully functional Keycloak server without Docker or external services:

```properties
# Development configuration
keycloak.connections-jpa.url=jdbc:h2:file:./dev-keycloak
keycloak.server.realm-import-file=dev-realm.json
```

### Integration Testing

Ideal for integration tests with automatic setup and teardown:

```java
@SpringBootTest
@EnableEmbeddedKeycloak
class IntegrationTest {
    // Your tests with embedded Keycloak
}
```

### Demo Applications

Create self-contained demos with authentication/authorization:

```properties
# Demo configuration
keycloak.connections-jpa.url=jdbc:h2:mem:demo
keycloak.server.realm-import-file=demo-realm.json
```

## Architecture

The library integrates Keycloak into Spring Boot by:

1. **Service Provider Implementation** - Custom platform and RESTEasy providers for embedded operation
2. **Configuration Management** - Spring Boot properties mapped to Keycloak system properties
3. **JAX-RS Integration** - RESTEasy servlet registration with Spring Boot's embedded container
4. **Session Management** - Request filtering for Keycloak session handling
5. **Database Integration** - JPA configuration bridging Spring and Keycloak

## Troubleshooting

### Port Conflicts

If port 8080 is already in use, change the Spring Boot server port:

```properties
server.port=8081
```

### Memory Issues

For large realm imports or production-like testing, increase JVM heap:

```bash
java -Xmx2g -jar your-application.jar
```

### Database Connection Issues

Ensure the database driver is included in your dependencies:

```xml
<!-- PostgreSQL -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- MySQL -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

## Limitations

- **Not for Production** - This library is intended for development and testing only
- **Single Instance** - Only one Keycloak instance per JVM
- **No Clustering** - Clustering features are not supported in embedded mode
- **Limited Extensions** - Some Keycloak extensions may not work in embedded mode

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Author

**Wei-Ming Wu** - [wnameless@gmail.com](mailto:wnameless@gmail.com)

## Acknowledgments

- [Keycloak](https://www.keycloak.org/) - Open Source Identity and Access Management
- [Spring Boot](https://spring.io/projects/spring-boot) - Framework for building Java applications
- [RESTEasy](https://resteasy.dev/) - JAX-RS implementation

## Support

For issues, questions, or suggestions, please use the [GitHub Issues](https://github.com/wnameless/spring-boot-up-embedded-keycloak/issues) page.