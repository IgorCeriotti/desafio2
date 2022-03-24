package dock.banking.dockbanking.service;

import dock.banking.dockbanking.exception.GeneralException;
import dock.banking.dockbanking.exception.ParametroIncorretoException;
import dock.banking.dockbanking.model.Conta;
import dock.banking.dockbanking.model.Transacao;
import dock.banking.dockbanking.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe responsável por operações referentes à lógica de negócio envolvendo transações
 */
@Service
public class TransacaoService {

    /**
     * Bean injetado do tipo TransacaoRepository
     */
    @Autowired
    private TransacaoRepository _transacaoRepository;

    Date diaAtual;

    /**
     * Método para verificar o somatório de valores de saque que aconteceram no dia
     * @param conta conta para verificar transações
     * @return retorna somatório de todos os valores de saque do dia
     */
    public BigDecimal retornaTotalSaqueDiario(Conta conta) {
        try {
            /*
            Filtra transacoes pelo dia atual
            Obtem os valores dessas transacoes
            Filtra por transacoes com valores negativos (saques)
            Soma todos esses valores e retorna modulo do valor
             */
            BigDecimal totalTransacoesSaque = _transacaoRepository.findByConta(conta).stream().filter(transacao -> {
                        Timestamp dataTransacaoBD = transacao.getDataTransacao();
                        diaAtual = new Date();

                        SimpleDateFormat dataFormatter = new SimpleDateFormat("ddMMyyyy");
                        return dataFormatter.format(dataTransacaoBD).equals(dataFormatter.format(diaAtual));
                    }).map(Transacao::getValor).toList()
                    .stream().filter(valor -> valor.signum() < 0).toList()
                    .stream().reduce(BigDecimal.ZERO, BigDecimal::add).abs();

            return totalTransacoesSaque;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new GeneralException("Erro ao verificar valor total dos saques que aconteceram no dia");
        }
    }

    /**
     * Método para filtrar transações de uma conta por um período de datas
     * @param dataInicial data inicial do período
     * @param dataFinal data final do período
     * @param conta conta a verificar extrato
     * @return retorna lista de transações da conta no período (incluindo data inicial e data final nos limites)
     */
    public List<Transacao> transacoesPorPeriodo(String dataInicial, String dataFinal, Conta conta) {
        String regex = "^([1-2][0-9]|[3][0-1])(-)(0[1-9]|1[1-2])(-)(\\d{4})$";

        if(!(dataFinal.matches(regex) && dataInicial.matches(regex))) {
            throw new ParametroIncorretoException("Parâmetros para a pesquisa incorretos!");
        }

        try{
            return _transacaoRepository.findByConta(conta).stream().filter(transacao -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                LocalDate localDataInicial = LocalDate.parse(dataInicial, formatter);
                LocalDate localDataFinal = LocalDate.parse(dataFinal, formatter);
                LocalDate dataDB = transacao.getDataTransacao().toLocalDateTime().toLocalDate();
                return (dataDB.isEqual(localDataInicial) || dataDB.isEqual(localDataFinal))
                        || (dataDB.isBefore(localDataFinal) && dataDB.isAfter(localDataInicial));
            }).collect(Collectors.toList());

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new GeneralException("Erro ao filtrar transacoes por periodo");
        }
    }

    public TransacaoRepository get_transacaoRepository() {
        return _transacaoRepository;
    }

    public void set_transacaoRepository(TransacaoRepository _transacaoRepository) {
        this._transacaoRepository = _transacaoRepository;
    }
}
