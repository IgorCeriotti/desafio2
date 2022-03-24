package dock.banking.dockbanking.controller;

import dock.banking.dockbanking.model.Conta;
import dock.banking.dockbanking.model.Transacao;
import dock.banking.dockbanking.service.ContaService;
import dock.banking.dockbanking.service.TransacaoService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe responsável por processar REST API requests da conta, retornando view que será renderizada em resposta
 */
@RestController
@RequestMapping("/banking/conta")
public class ContaController {

    /**
     * Bean injetado do tipo ContaService
     */
    @Autowired
    ContaService _contaService;

    /**
     * Bean injetado do tipo TransacaoService
     */
    @Autowired
    TransacaoService _transacaoService;

    /**
     * Path com verbo POST que realiza a criação de uma conta
     * @param pessoaId parâmetro de path que representa id da pessoa no BD que para vincular conta
     * @param contaRequest body que representa os dados da conta a ser criada
     * @return resposta com a conta criada e status CREATED
     */
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Conta criada com sucesso"),
            @ApiResponse(code = 404, message = "Não existe pessoa cadastrada com o id informado"),
            @ApiResponse(code = 500, message = "Ocorreu uma exceção ao criar uma nova conta")
    })
    @PostMapping("/new/{pessoaId}")
    public ResponseEntity<Conta> criarConta(@PathVariable(value = "pessoaId") @NotBlank @NotNull
                                                   @NumberFormat(style = NumberFormat.Style.NUMBER) @Min(1) Long pessoaId,
                                                 @RequestBody @NotBlank @NotNull Conta contaRequest) {

        Conta conta = _contaService.criarNovaConta(pessoaId, contaRequest);

        return new ResponseEntity<>(conta, HttpStatus.CREATED);
    }

    /**
     * Path com verbo GET que realiza operação de consulta de saldo em determinada conta
     * @param contaId parâmetro de path que representa id da conta no BD para realizar a consulta
     * @return mensagem com saldo da conta e status OK
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Saldo obtido com sucesso"),
            @ApiResponse(code = 404, message = "Conta não encontrada"),
            @ApiResponse(code = 500, message = "Ocorreu uma exceção ao obter saldo de uma conta")
    })
    @GetMapping("/{contaId}/saldo")
    public ResponseEntity<String> consultarSaldo(@PathVariable(value = "contaId") @NotBlank @NotNull
                                                    @NumberFormat(style = NumberFormat.Style.NUMBER) @Min(1) Long contaId) {

        String saldo = _contaService.geraSaldo(contaId);

        return new ResponseEntity<>(saldo, HttpStatus.OK);
    }

    /**
     * Path com verbo PUT que realiza operação de depósito em uma conta
     * @param contaId parâmetro de path que representa id da conta no BD para realizar o depósito
     * @param transacaoRequest body que representa os dados da transação de depósito
     * @return mensagem de sucesso da operação e dados da transação realizada e status OK
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Depósito realizado com sucesso"),
            @ApiResponse(code = 404, message = "Conta não encontrada"),
            @ApiResponse(code = 500, message = "Ocorreu uma exceção ao realizar depósito em uma conta")
    })
    @PutMapping("/{contaId}/deposito")
    public ResponseEntity<Map<String,Object>> realizarDeposito(@PathVariable(value = "contaId") @NotBlank @NotNull
                                                                  @NumberFormat(style = NumberFormat.Style.NUMBER) @Min(1)Long contaId,
                                                                 @RequestBody @NotBlank @NotNull Transacao transacaoRequest) {

        _contaService.realizaDeposito(contaId, transacaoRequest);

        return new ResponseEntity<>(new LinkedHashMap<>(){{
            put("retorno", "Deposito realizado com sucesso!");
            put("transacao", transacaoRequest);
        }}, HttpStatus.OK);
    }

    /**
     * Path com verbo PUT que realiza operação de saque em uma conta
     * @param contaId parâmetro de path que representa id da conta no BD para realizar o saque
     * @param transacaoRequest body que representa os dados da transação de saque
     * @return mensagem de sucesso da operação e dados da transação realizada e status OK
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Saque realizado com sucesso"),
            @ApiResponse(code = 404, message = "Conta não encontrada"),
            @ApiResponse(code = 500, message = "Ocorreu uma exceção ao realizar saque em uma conta")
    })
    @PutMapping("/{contaId}/saque")
    public ResponseEntity<Map<String,Object>> realizarSaque(@PathVariable(value = "contaId") @NotBlank @NotNull
                                                                @NumberFormat(style = NumberFormat.Style.NUMBER) @Min(1) Long contaId,
                                                @RequestBody @NotBlank @NotNull Transacao transacaoRequest) {

        _contaService.realizaSaque(contaId, transacaoRequest);

        return new ResponseEntity<>(new LinkedHashMap<>(){{
            put("retorno", "Saque realizado com sucesso! Consulte seu saldo.");
            put("transacao", transacaoRequest);
        }}, HttpStatus.OK);
    }

    /**
     * Path com verbo PATCH que realiza o bloqueio de uma conta
     * @param contaId parâmetro de path que representa id da conta no BD a ser bloqueada
     * @return dados da conta bloqueada e status OK
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Bloqueio de conta realizado com sucesso"),
            @ApiResponse(code = 404, message = "Conta não encontrada"),
            @ApiResponse(code = 500, message = "Ocorreu uma exceção ao bloquear uma conta")
    })
    @PatchMapping(value = "/{contaId}/bloquear")
    public ResponseEntity<Conta> bloquearConta(@PathVariable @NotBlank @NotNull
                                                   @NumberFormat(style = NumberFormat.Style.NUMBER) @Min(1) Long contaId) {

        Conta conta = _contaService.bloquearConta(contaId);

        return new ResponseEntity<>(conta,HttpStatus.OK);
    }


    /**
     * Path com verbo GET que recupera o extrato de transações de uma conta
     * @param contaId parâmetro de path que representa id da conta a consultar extrato
     * @return lista de transações realizadas para aquela conta e status OK
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Extrato de transações de conta obtido com sucesso"),
            @ApiResponse(code = 404, message = "Conta não encontrada"),
            @ApiResponse(code = 500, message = "Ocorreu uma exceção ao verificar extrato de transações de uma conta")
    })
    @GetMapping("/{contaId}/extrato")
    public ResponseEntity<List<Transacao>> consultarExtrato
                    (@PathVariable(value = "contaId") @NotBlank @NotNull
                     @NumberFormat(style = NumberFormat.Style.NUMBER) @Min(1) Long contaId) {

        List<Transacao> extrato = _contaService.geraExtrato(contaId);

        return new ResponseEntity<>(extrato, HttpStatus.OK);
    }

    /**
     * Path com verbo GET que recupera extrato por período
     * @param contaId parâmetro de path que representa id da conta a consultar extrato
     * @param dataInicial parâmetro de path que representa a data inicial do período, no formato dd-MM-yyyy
     * @param dataFinal parâmetro de path que representa data final do período, no formato dd-MM-yyyy
     * @return lista de transações para a conta no período informado
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Extrato de transações de conta por período obtido com sucesso"),
            @ApiResponse(code = 404, message = "Conta não encontrada"),
            @ApiResponse(code = 422, message = "Algum parâmetro do path está incorreto"),
            @ApiResponse(code = 500, message = "Ocorreu uma exceção ao verificar extrato de transações por período de uma conta")
    })
    @GetMapping("/{contaId}/extrato/{dataInicial}/{dataFinal}")
    public ResponseEntity<List<Transacao>> consultarExtratoPeriodo
            (@PathVariable(value = "contaId")  @NotBlank @NotNull @NumberFormat(style = NumberFormat.Style.NUMBER) @Min(1) Long contaId,
             @PathVariable(value = "dataInicial") @NotBlank @NotNull String dataInicial,
             @PathVariable(value = "dataFinal") @NotBlank @NotNull String dataFinal) {

        Conta conta = _contaService.obterContaPorId(contaId);

        List<Transacao> extratoPeriodo = _transacaoService.transacoesPorPeriodo(dataInicial, dataFinal, conta);

        return new ResponseEntity<>(extratoPeriodo, HttpStatus.OK);
    }
}
