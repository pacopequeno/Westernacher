package testtask.useraccounts.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TestEnvironmentConfig {

    @Value("${selenium.debug:false}")
    private boolean seleniumDebug;

    public boolean isDebug() {
        return seleniumDebug;
    }
}