package dock.banking.dockbanking.ServiceMocksConfiguration;

import dock.banking.dockbanking.service.ContaService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class ContaServiceConfiguration {

    @Bean
    @Primary
    public ContaService contaServiceServiceMock() {
        return Mockito.mock(ContaService.class);
    }
}
