package testtask.useraccounts.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
        basePackages = {
                "testtask.useraccounts.config",
        }
)
public class WebdriverTestSpringConfig {
}