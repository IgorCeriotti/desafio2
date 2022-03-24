package dock.banking.dockbanking.RepositoryMocksConfiguration;

import dock.banking.dockbanking.repository.TransacaoRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TransacaoRepositoryConfiguration {

    @Bean
    @Primary
    public TransacaoRepository transacaoRepositoryMock() {
        return Mockito.mock(TransacaoRepository.class);
    }
}
