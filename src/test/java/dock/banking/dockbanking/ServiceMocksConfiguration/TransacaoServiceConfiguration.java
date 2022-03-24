package dock.banking.dockbanking.ServiceMocksConfiguration;

import dock.banking.dockbanking.repository.TransacaoRepository;
import dock.banking.dockbanking.service.TransacaoService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TransacaoServiceConfiguration {

    @Bean
    @Primary
    public TransacaoService transacaoServiceMock() {
        return Mockito.mock(TransacaoService.class);
    }
}
