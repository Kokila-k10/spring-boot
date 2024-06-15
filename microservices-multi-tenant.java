import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class MultiTenantEcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiTenantEcommerceApplication.class, args);
    }

    // Example of a REST Controller for managing orders
    @RestController
    class OrderController {

        @GetMapping("/orders")
        public String getOrders() {
            // Example: Retrieve orders based on tenant context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String tenantId = authentication.getName(); // Assuming tenant ID is stored in username
            return "Fetching orders for Tenant ID: " + tenantId;
        }
    }

    // Example of a simple authentication configuration
    @EnableWebSecurity
    class SecurityConfig {

        @Bean
        public void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                .antMatchers("/orders").authenticated() // Secure endpoint
                .anyRequest().permitAll() // Allow other requests
                .and()
                .httpBasic(); // Use basic authentication
        }
    }

    // Example of logging and monitoring (simplified with console output)
    @Bean
    public LoggingService loggingService() {
        return new LoggingService();
    }

    class LoggingService {
        public void log(String message) {
            System.out.println("[LOG] " + message);
        }
    }

    // Example of a multi-tenant database configuration (in-memory for simplicity)
    @Bean
    public TenantDatabaseService tenantDatabaseService() {
        return new TenantDatabaseService();
    }

    class TenantDatabaseService {
        public void connect(String tenantId) {
            System.out.println("[DB] Connecting to database for Tenant ID: " + tenantId);
        }
    }
}
