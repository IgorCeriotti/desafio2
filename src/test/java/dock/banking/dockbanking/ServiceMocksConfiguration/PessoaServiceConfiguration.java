package dock.banking.dockbanking.ServiceMocksConfiguration;

import dock.banking.dockbanking.service.PessoaService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class PessoaServiceConfiguration {

    @Bean
    @Primary
    public PessoaService pessoaServiceMock() {
        return Mockito.mock(PessoaService.class);
    }
}
