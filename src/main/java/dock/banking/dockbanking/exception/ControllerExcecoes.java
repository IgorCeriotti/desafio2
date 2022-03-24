package dock.banking.dockbanking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * Classe responsável por tratar erros durante runtime e apresentar resposta com as informações desses erros
 */
@RestControllerAdvice
public class ControllerExcecoes {

    /**
     * Manipulador de exceções do tipo ResourceNotFoundException
     * @param ex RuntimeException
     * @param request request que gerou a exceção
     * @return Mensagem modelo de erro
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public MensagemErro resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        MensagemErro mensagem = new MensagemErro(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return mensagem;
    }

    /**
     * Manipulador de exceções globais do tipo Exception
     * @param ex RuntimeException
     * @param request request que gerou a exceção
     * @return Mensagem modelo de erro
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public MensagemErro globalExceptionHandler(Exception ex, WebRequest request) {
        MensagemErro mensagem = new MensagemErro(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return mensagem;
    }

    /**
     * Manipulador de exceções globais do tipo LimiteSaqueDiarioExcedido
     * @param ex RuntimeException
     * @param request request que gerou a exceção
     * @return Mensagem modelo de erro
     */
    @ExceptionHandler(LimiteSaqueDiarioExcedido.class)
    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    public MensagemErro limiteSaqueExcedidoHandler(LimiteSaqueDiarioExcedido ex, WebRequest request) {
        MensagemErro mensagem = new MensagemErro(
                HttpStatus.PRECONDITION_FAILED.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return mensagem;
    }

    /**
     * Manipulador de exceções globais do tipo GeneralException
     * @param ex RuntimeException
     * @param request request que gerou a exceção
     * @return Mensagem modelo de erro
     */
    @ExceptionHandler(GeneralException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public MensagemErro excecoesGeraisMapeadas(Exception ex, WebRequest request) {
        MensagemErro mensagem = new MensagemErro(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return mensagem;
    }

    /**
     * Manipulador de exceções globais do tipo ParametroIncorretoException
     * @param ex RuntimeException
     * @param request request que gerou a exceção
     * @return Mensagem modelo de erro
     */
    @ExceptionHandler(ParametroIncorretoException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public MensagemErro excecaoParametroIncorreto(Exception ex, WebRequest request) {
        MensagemErro mensagem = new MensagemErro(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return mensagem;
    }

}
