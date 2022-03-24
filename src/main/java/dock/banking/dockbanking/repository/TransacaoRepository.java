package dock.banking.dockbanking.repository;

import dock.banking.dockbanking.model.Conta;
import dock.banking.dockbanking.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface responsável pela consulta e manipulação em camada de dados de Transacoes.
 */
public interface TransacaoRepository extends JpaRepository<Transacao,Long> {

    /**
     * Retorna lista de transações vinculadas à uma conta
     * @param conta conta para pesquisa de transações
     * @return lista de transções
     */
    List<Transacao> findByConta(Conta conta);
}
