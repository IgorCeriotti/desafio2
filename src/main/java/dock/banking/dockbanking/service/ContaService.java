package dock.banking.dockbanking.service;

import dock.banking.dockbanking.exception.GeneralException;
import dock.banking.dockbanking.exception.LimiteSaqueDiarioExcedido;
import dock.banking.dockbanking.exception.ResourceNotFoundException;
import dock.banking.dockbanking.model.Conta;
import dock.banking.dockbanking.model.Transacao;
import dock.banking.dockbanking.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

/**
 * Classe responsável por operações referentes à lógica de negócio envolvendo contas
 */
@Service
public class ContaService {
    public ContaService() {
    }

    /**
     * Bean injetado do tipo ContaRepository
     */
    @Autowired
    private ContaRepository _contaRepository;

    /**
     * Bean injetado do tipo PessoaRepository
     */
    @Autowired
    private PessoaService _pessoaService;

    /**
     * Bean injetado do tipo TransacaoService
     */
    @Autowired
    TransacaoService _transacaoService;

    /**
     * Cria uma nova conta
     * @param pessoaId id da pessoa no BD a ser vinculada à conta
     * @param contaRequest objeto conta com os dados enviados pelo request
     * @return objeto conta criado
     */
    public Conta criarNovaConta(Long pessoaId, Conta contaRequest) {
        /*
        Procura pessoa pelo id informado
        Seta pessoa a conta
        Salva nova conta
         */
        try {
            return _pessoaService.get_pessoaRepository().findById(pessoaId).map(pessoa -> {
                contaRequest.setPessoa(pessoa);

                _contaRepository.save(contaRequest);

                return contaRequest;
            }).orElseThrow(() -> new ResourceNotFoundException("Nao foi encontrada pessoa com id = " + pessoaId));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new GeneralException("Falha ao criar nova conta!");
        }
    }

    /**
     * Obtém conta por id
     * @param id id da conta no BD a ser obtida
     * @return conta
     */
    public Conta obterContaPorId(Long id) {
            return _contaRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException("Nao foi encontrada conta com id = " + id));
    }

    /**
     * Realiza operação de saque
     * @param contaId id da conta no BD para realizar o saque
     * @param transacao transacao com valor a ser sacado
     */
    public void realizaSaque(Long contaId, Transacao transacao) {
        Conta conta = obterContaPorId(contaId);
        BigDecimal valorSaque = transacao.getValor().abs();
        BigDecimal valorJaSacadoDia = _transacaoService.retornaTotalSaqueDiario(conta);
        BigDecimal limiteSaque = conta.getLimiteSaqueDiario();
        BigDecimal cotaSaque = valorJaSacadoDia.add(valorSaque);

        /*
        Verifica se o valor solicitado de saque eh menor que o limite diario
        Verifica se o valor de saque solicitado nao ultrapassa limite diario ja sacado
         */
        if(cotaSaque.compareTo(limiteSaque) > 0 ||
                valorSaque.compareTo(limiteSaque) > 0) {
            throw new LimiteSaqueDiarioExcedido("Valor do saque ultrapassa o limite máximo diário! " +
                    "Não é possível realizar a operação.");
        }

        /*
        Subtrai do saldo o valor do saque
        Salva a conta com o novo saldo
        Salva a transacao de saque
         */
        try {
            BigDecimal saldoConta = conta.getSaldo();
            conta.setSaldo(saldoConta.subtract(valorSaque));
            _contaRepository.save(conta);

            transacao.setConta(conta);
            transacao.setValor(transacao.getValor().negate());
            _transacaoService.get_transacaoRepository().save(transacao);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new GeneralException("Falha ao realizar operacoes de saque");
        }
    }

    /**
     * Método responsável por realizar o depósito em conta
     * @param contaId id da conta no BD para realizar o depósito
     * @param transacao transacao com valor a ser depositado
     */
    public void realizaDeposito(Long contaId, Transacao transacao) {
        Conta conta = obterContaPorId(contaId);

        /*
        Adiciona valor do deposito ao saldo
        Salva conta com saldo atualizado
        Salva transacao de deposito
         */
        try {
            BigDecimal valorDeposito = transacao.getValor();
            BigDecimal saldoConta = conta.getSaldo().abs();
            conta.setSaldo(saldoConta.add(valorDeposito));
            _contaRepository.save(conta);

            transacao.setConta(conta);
            _transacaoService.get_transacaoRepository().save(transacao);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new GeneralException("Falha ao realizar operacoes de deposito!");
        }
    }

    /**
     * Método que retorna o saldo de uma conta
     * @param contaId id da conta no BD para consultar saldo
     * @return mensagem apresentando saldo da conta
     */
    public String geraSaldo(Long contaId) {
        Conta conta = obterContaPorId(contaId);

        return "Saldo em conta: R$ " +
                String.format(Locale.FRANCE, "%.2f", conta.getSaldo());
    }

    /**
     * Método que altera a flag de conta ativa para falso (bloqueia a conta)
     * @param contaId id da conta no BD a ser bloqueada
     * @return conta bloqueada
     */
    public Conta bloquearConta(Long contaId) {
        Conta conta = obterContaPorId(contaId);

        try {
            new LinkedHashMap<String,Boolean>(){{
                put("flagAtivo", false);
            }}.forEach((k, v) -> {
                Field field = ReflectionUtils.findField(Conta.class, k);
                field.setAccessible(true);
                ReflectionUtils.setField(field, conta, v);
            });

            _contaRepository.save(conta);

            return conta;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new GeneralException("Falha ao redefinir tag de bloqueio de conta!");
        }
    }

    /**
     * Método que gera extrato de transações de uma conta
     * @param contaId id da conta no BD para gerar extrato
     * @return lista de transações da conta
     */
    public List<Transacao> geraExtrato(Long contaId) {
        Conta conta = obterContaPorId(contaId);

        return _transacaoService.get_transacaoRepository().findByConta(conta);
    }

    public ContaRepository get_contaRepository() {
        return _contaRepository;
    }

    public void set_contaRepository(ContaRepository _contaRepository) {
        this._contaRepository = _contaRepository;
    }

    public TransacaoService get_transacaoService() {
        return _transacaoService;
    }

    public void set_transacaoService(TransacaoService _transacaoService) {
        this._transacaoService = _transacaoService;
    }

    public PessoaService get_pessoaService() {
        return _pessoaService;
    }

    public void set_pessoaService(PessoaService _pessoaService) {
        this._pessoaService = _pessoaService;
    }
}
