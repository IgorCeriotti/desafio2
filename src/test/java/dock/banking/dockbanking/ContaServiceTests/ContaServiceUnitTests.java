package dock.banking.dockbanking.ContaServiceTests;

import dock.banking.dockbanking.DockBankingMocks;
import dock.banking.dockbanking.RepositoryMocksConfiguration.ContaRepositoryConfiguration;
import dock.banking.dockbanking.RepositoryMocksConfiguration.PessoaRepositoryConfiguration;
import dock.banking.dockbanking.RepositoryMocksConfiguration.TransacaoRepositoryConfiguration;
import dock.banking.dockbanking.ServiceMocksConfiguration.PessoaServiceConfiguration;
import dock.banking.dockbanking.ServiceMocksConfiguration.TransacaoServiceConfiguration;
import dock.banking.dockbanking.exception.LimiteSaqueDiarioExcedido;
import dock.banking.dockbanking.model.Conta;
import dock.banking.dockbanking.model.Pessoa;
import dock.banking.dockbanking.model.Transacao;
import dock.banking.dockbanking.service.ContaService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.util.Optional;

@ActiveProfiles("test")
@SpringBootTest(classes = DockBankingMocks.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContaServiceUnitTests {

    @Autowired
    private TransacaoRepositoryConfiguration transacaoRepositoryConfiguration;

    @Autowired
    private TransacaoServiceConfiguration transacaoServiceConfiguration;

    @Autowired
    private PessoaServiceConfiguration pessoaServiceConfiguration;

    @Autowired
    private PessoaRepositoryConfiguration pessoaRepositoryConfiguration;

    @Autowired
    private ContaRepositoryConfiguration contaRepositoryConfiguration;

    Pessoa pessoaMock;
    Conta conta;
    Transacao deposito_saque;
    ContaService contaService;
    Long id = 1L;

    @BeforeAll
    public void init() {
        pessoaMock = new Pessoa("Maria", "12345678910", new java.sql.Date(10102010));

        deposito_saque = new Transacao();
        deposito_saque.setValor(BigDecimal.valueOf(50.00));

        conta = new Conta();
        conta.setFlagAtivo(true);
        conta.setSaldo(BigDecimal.valueOf(500));
        conta.setLimiteSaqueDiario(BigDecimal.valueOf(100));

        contaService = new ContaService();
        contaService.set_pessoaService(pessoaServiceConfiguration.pessoaServiceMock());
        contaService.set_transacaoService(transacaoServiceConfiguration.transacaoServiceMock());
        contaService.get_pessoaService().set_pessoaRepository(pessoaRepositoryConfiguration.pessoaRepositoryMock());
        contaService.set_contaRepository(contaRepositoryConfiguration.contaRepositoryMock());
        contaService.get_transacaoService().set_transacaoRepository(transacaoRepositoryConfiguration.transacaoRepositoryMock());
        contaService.set_transacaoService(transacaoServiceConfiguration.transacaoServiceMock());

        Mockito.when(pessoaRepositoryConfiguration
                        .pessoaRepositoryMock().findById(Mockito.any(Long.class)))
                .thenReturn(Optional.of(pessoaMock));

        Mockito.when(contaRepositoryConfiguration
                        .contaRepositoryMock().save(Mockito.any(Conta.class)))
                .thenReturn(Mockito.any());

        Mockito.when(contaRepositoryConfiguration
                        .contaRepositoryMock().findById(1L))
                .thenReturn(Optional.ofNullable(conta));

        Mockito.when(transacaoRepositoryConfiguration
                .transacaoRepositoryMock().save(Mockito.any(Transacao.class)))
                .thenReturn(Mockito.any());

        Mockito.when(transacaoServiceConfiguration.transacaoServiceMock()
                .retornaTotalSaqueDiario(conta))
                .thenReturn(BigDecimal.valueOf(30));

        Mockito.when(transacaoServiceConfiguration.transacaoServiceMock()
                        .get_transacaoRepository())
                .thenReturn(transacaoRepositoryConfiguration.transacaoRepositoryMock());

        Mockito.when(pessoaServiceConfiguration.pessoaServiceMock().get_pessoaRepository())
                .thenReturn(pessoaRepositoryConfiguration.pessoaRepositoryMock());

    }

    @AfterEach
    public void resetParams() {
        conta.setFlagAtivo(true);
        conta.setSaldo(BigDecimal.valueOf(500));
        conta.setLimiteSaqueDiario(BigDecimal.valueOf(100));
    }

    /**
     * Deve retornar a conta criada com os valores informados e vinculada à pessoa específica
     */
    @Test
    public void teste_criar_nova_conta() {

        Conta contaRetornada = contaService.criarNovaConta(id, conta);

        Assertions.assertTrue(contaRetornada.getSaldo() == conta.getSaldo());
        Assertions.assertTrue(contaRetornada.isFlagAtivo() && contaRetornada.isFlagAtivo());
        Assertions.assertTrue(contaRetornada.getLimiteSaqueDiario() == conta.getLimiteSaqueDiario());
        Assertions.assertTrue(contaRetornada.getPessoa().equals(pessoaMock));
    }

    /**
     * Deve realizar o depósito e atualizar o saldo da conta
     */
    @Test
    public void teste_realizar_deposito() {
        contaService.realizaDeposito(id, deposito_saque);
        Assertions.assertEquals(BigDecimal.valueOf(550.0), conta.getSaldo());
    }

    /**
     * Deve gerar mensagem contendo valor do saldo da conta formato para 2 casas decimais e vírgula
     */
    @Test
    public void teste_gerar_saldo() {
        Assertions.assertEquals("Saldo em conta: R$ 500,00", contaService.geraSaldo(id));
    }

    /**
     * Deve alterar flag para false
     */
    @Test
    public void teste_bloquear_conta() {
        Assertions.assertFalse(contaService.bloquearConta(id).isFlagAtivo());
    }

    /**
     * Deve debitar o valor do saldo da conta
     */
    @Test
    public void teste_realizar_saque_valor_limite_nao_excedido() {
        contaService.realizaSaque(id, deposito_saque);
        Assertions.assertEquals(BigDecimal.valueOf(450.0), conta.getSaldo());
    }

    /**
     * Deve retornar exceção ao tentar realizar saque com um valor maior que o limite diário
     */
    @Test
    public void teste_realizar_saque_valor_maior_que_limite() {
        Transacao transacaoValorLimiteExcedido = new Transacao();
        transacaoValorLimiteExcedido.setValor(BigDecimal.valueOf(500));
        Assertions.assertThrows(LimiteSaqueDiarioExcedido.class,
                () -> contaService.realizaSaque(id, transacaoValorLimiteExcedido));
    }

    /**
     * Deve gerar exceção quando o valor solicitado de saque ultrapassa a soma dos valores que já foram sacados no dia
     * É setado valor que já foi sacado em init(), em 30. Qualquer valor acima de 70 deve causa falha
     */
    @Test
    public void teste_realizar_somatorio_saque_maior_que_limite() {
        Transacao transacaoValorLimiteExcedido = new Transacao();
        transacaoValorLimiteExcedido.setValor(BigDecimal.valueOf(72));
        Assertions.assertThrows(LimiteSaqueDiarioExcedido.class,
                () -> contaService.realizaSaque(id, transacaoValorLimiteExcedido));
    }

    /**
     * Deve debitar o valor do saldo da conta, mesmo quando o valor acumulado é exatamente o limite diário
     */
    @Test
    public void teste_realizar_saque_valor_igual_ao_limite() {
        try {
            Transacao novaTransacao = new Transacao();
            novaTransacao.setValor(BigDecimal.valueOf(70));

            contaService.realizaSaque(id, novaTransacao);
        } catch (LimiteSaqueDiarioExcedido ex) {
            Assertions.fail("Houve exceção com valor limite de saque");
        }
    }
}
