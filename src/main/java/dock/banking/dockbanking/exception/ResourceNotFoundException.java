package dock.banking.dockbanking.exception;

/**
 * Classe para exceções em que o recurso a ser realizada uma operação não é encontrado
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String msg) {
        super(msg);
    }

}
