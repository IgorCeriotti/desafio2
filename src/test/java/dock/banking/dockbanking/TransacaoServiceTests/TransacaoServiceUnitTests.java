package dock.banking.dockbanking.TransacaoServiceTests;

import dock.banking.dockbanking.DockBankingMocks;
import dock.banking.dockbanking.RepositoryMocksConfiguration.TransacaoRepositoryConfiguration;
import dock.banking.dockbanking.exception.ParametroIncorretoException;
import dock.banking.dockbanking.model.Conta;
import dock.banking.dockbanking.model.Pessoa;
import dock.banking.dockbanking.model.Transacao;
import dock.banking.dockbanking.service.TransacaoService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest(classes = DockBankingMocks.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransacaoServiceUnitTests {

    @Autowired
    private TransacaoRepositoryConfiguration transacaoRepositoryConfiguration;

    TransacaoService transacaoService;
    Conta contaA;
    Conta contaB;
    Conta contaC;
    Conta contaD;
    Conta contaE;
    List<Transacao> transacaoList;

    @BeforeAll
    public void init() throws ParseException {
        transacaoService = new TransacaoService();
        transacaoService.set_transacaoRepository(transacaoRepositoryConfiguration.
                transacaoRepositoryMock());

        contaA = new Conta();
        contaA.setPessoa(new Pessoa());
        contaA.setId(1L);

        contaB = new Conta();
        contaB.setPessoa(new Pessoa());
        contaB.setId(2L);

        contaC = new Conta();
        contaC.setPessoa(new Pessoa());
        contaC.setId(3L);

        contaD = new Conta();
        contaD.setPessoa(new Pessoa());
        contaD.setId(4L);

        contaE = new Conta();
        contaE.setPessoa(new Pessoa());
        contaE.setId(5L);

        transacaoList = new LinkedList<>() {{

            Transacao transacaoSaque = new Transacao();
            transacaoSaque.setValor(BigDecimal.valueOf(-5.0));
            transacaoSaque.setDataTransacao(new Timestamp(System.currentTimeMillis()));

            Transacao transacaoDeposito = new Transacao();
            transacaoDeposito.setValor(BigDecimal.valueOf(20.50));
            transacaoDeposito.setDataTransacao(new Timestamp(System.currentTimeMillis()));

            Transacao transacaoDeposito2 = new Transacao();
            transacaoDeposito2.setValor(BigDecimal.valueOf(20.50));
            transacaoDeposito2.setDataTransacao(new Timestamp(System.currentTimeMillis()));

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

            Date date = formatter.parse("22-03-2022");
            java.sql.Timestamp timeStampDate22 = new Timestamp(date.getTime());

            Date date2 = formatter.parse("28-03-2022");
            java.sql.Timestamp timeStampDate28 = new Timestamp(date2.getTime());

            Transacao marco22 = new Transacao();
            marco22.setDataTransacao(timeStampDate22);

            Transacao marco28 = new Transacao();
            marco28.setDataTransacao(timeStampDate28);

            add(transacaoSaque);
            add(transacaoDeposito);
            add(transacaoDeposito2);
            add(marco22);
            add(marco28);
        }};

        Mockito.when(transacaoRepositoryConfiguration.
                transacaoRepositoryMock()
                .findByConta(contaA)).thenReturn(transacaoList);

        Mockito.when(transacaoRepositoryConfiguration.
                transacaoRepositoryMock()
                .findByConta(contaB)).thenReturn(transacaoList.subList(0, 1));

        Mockito.when(transacaoRepositoryConfiguration.
                transacaoRepositoryMock()
                .findByConta(contaC)).thenReturn(transacaoList.subList(1, 3));

        Mockito.when(transacaoRepositoryConfiguration.
                transacaoRepositoryMock()
                .findByConta(contaD)).thenReturn(transacaoList.subList(3, 5));
    }

    /**
     * Deve retornar o módulo do valor da transação de saque
     */
    @Test
    public void teste_com_um_saque() {
        Assertions.assertEquals(BigDecimal.valueOf(5.0), transacaoService.retornaTotalSaqueDiario(contaB));
    }

    /**
     * Deve retornar o módulo apenas da transação de saque
     */
    @Test
    public void teste_com_um_saque_e_depositos() {
        Assertions.assertEquals(BigDecimal.valueOf(5.0), transacaoService.retornaTotalSaqueDiario(contaA));
    }

    /**
     * Deve retornar zero, já que não há transação de saque
     */
    @Test
    public void teste_apenas_com_deposito() {
        Assertions.assertEquals(BigDecimal.ZERO, transacaoService.retornaTotalSaqueDiario(contaC));
    }

    /**
     * Deve retornar a mesma lista de transações por estarem dentro do período
     */
    @Test
    public void teste_transacoes_dentro_do_periodo() {
        Assertions.assertEquals(transacaoList.subList(3, 4), transacaoService
                .transacoesPorPeriodo("20-03-2022", "23-03-2022", contaD));
    }

    /**
     * Não deve retornar dados para o período com transações fora do range
     */
    @Test
    public void teste_transacoes_fora_do_periodo() {
        Assertions.assertTrue(transacaoService
                .transacoesPorPeriodo("15-03-2022", "17-03-2022", contaD).isEmpty());
    }

    /**
     * Deve retornar exceção se o formato de data para filtro estiver incorreto
     */
    @Test
    public void teste_transacoes_periodo_data_invalida() {
        Assertions.assertThrows(ParametroIncorretoException.class, () -> transacaoService
                .transacoesPorPeriodo("15-03-2022", "17-78932", contaD));
    }
}
