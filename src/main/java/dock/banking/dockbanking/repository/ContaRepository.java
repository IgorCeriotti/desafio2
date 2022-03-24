package dock.banking.dockbanking.repository;

import dock.banking.dockbanking.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface responsável pela consulta e manipulação em camada de dados de Contas.
 */
public interface ContaRepository extends JpaRepository<Conta,Long> {

    /**
     * Retorna contas vinculadas a uma pessoa específica
     * @param pessoaId o id da pessoa no banco de dados
     * @return lista de contas
     */
    List<Conta> findByPessoaId(Long pessoaId);

}
