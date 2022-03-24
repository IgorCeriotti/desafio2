package dock.banking.dockbanking.exception;

/**
 * Classe para exceções em que algum parâmetro passado para a requisição não é o esperadoß
 */
public class ParametroIncorretoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ParametroIncorretoException(String msg) {
        super(msg);
    }

}
