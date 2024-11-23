package iit.y3.oopcw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;

@SpringBootApplication(scanBasePackages = {"iit.y3.oopcw"})
public class OopcwApplication extends SpringBootServletInitializer {
    private static final Logger logger = LoggerFactory.getLogger(OopcwApplication.class);

    public static void main(String[] args) {
        Runtime.getRuntime()
                .addShutdownHook(new Thread(() -> logger.warn("system shutdown [{}]", LocalDateTime.now())));
        SpringApplication.run(OopcwApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(OopcwApplication.class);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}