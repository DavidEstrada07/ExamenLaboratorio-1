
public interface Prestable {

    void prestar(Usuario usuario) throws BibliotecaException;

    void devolver();

    boolean estaDisponible();
} 