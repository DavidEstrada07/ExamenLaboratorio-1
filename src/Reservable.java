
public interface Reservable {

    void reservar(Usuario usuario);

    void cancelarReserva(Usuario usuario);

    boolean tieneReservasPendientes();
}
