

public class LimitePrestamosExcedidoException extends BibliotecaException {

    public LimitePrestamosExcedidoException(String nombreUsuario, int limite) {
        super("El usuario " + nombreUsuario + " ya alcanzo su limite de " + limite + " prestamos simultaneos.");
    }
}
