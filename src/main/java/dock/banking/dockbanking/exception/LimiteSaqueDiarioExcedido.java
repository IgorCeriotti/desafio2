package dock.banking.dockbanking.exception;

/**
 * Classe de exceção para quando um saque é requisitado, mas o limite diário já foi excedido
 */
public class LimiteSaqueDiarioExcedido extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LimiteSaqueDiarioExcedido(String msg) {
        super(msg);
    }

}
