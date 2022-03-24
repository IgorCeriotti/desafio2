package dock.banking.dockbanking.RepositoryMocksConfiguration;

import dock.banking.dockbanking.repository.ContaRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class ContaRepositoryConfiguration {

    @Bean
    @Primary
    public ContaRepository contaRepositoryMock() {
        return Mockito.mock(ContaRepository.class);
    }
}
