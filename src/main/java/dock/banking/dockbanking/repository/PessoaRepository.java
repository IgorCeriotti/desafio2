package dock.banking.dockbanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dock.banking.dockbanking.model.Pessoa;

/**
 * Interface responsável pela consulta e manipulação em camada de dados de Pessoa.
 */
public interface PessoaRepository extends JpaRepository<Pessoa,Long> {
    
}
