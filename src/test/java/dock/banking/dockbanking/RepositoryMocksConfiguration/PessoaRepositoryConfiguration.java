package dock.banking.dockbanking.RepositoryMocksConfiguration;

import dock.banking.dockbanking.repository.PessoaRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class PessoaRepositoryConfiguration {

    @Bean
    @Primary
    public PessoaRepository pessoaRepositoryMock() {
        return Mockito.mock(PessoaRepository.class);
    }
}
