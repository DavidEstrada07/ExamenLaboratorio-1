

import java.util.ArrayList;
import java.util.List;

public class UsuarioPremium extends Usuario {

    private static final int LIMITE_PRESTAMOS = 8;

    // Coleccion manejada por el tipo de la interfaz, no por el tipo concreto
    private List<Reservable> reservasActivas;

    public UsuarioPremium(String id, String nombre) {
        super(id, nombre);
        this.reservasActivas = new ArrayList<>();
    }

    @Override
    public int getLimitePrestamos() {
        return LIMITE_PRESTAMOS;
    }

    @Override
    public boolean tieneAutorizacionEspecial() {
        return true;
    }

    // Un premium puede reservar material que ya tiene prestado otro usuario
    public void reservarMaterial(Reservable material) {
        material.reservar(this);
        reservasActivas.add(material);
    }

    public void cancelarReservaMaterial(Reservable material) {
        material.cancelarReserva(this);
        reservasActivas.remove(material);
    }

    public List<Reservable> getReservasActivas() {
        return reservasActivas;
    }
}
