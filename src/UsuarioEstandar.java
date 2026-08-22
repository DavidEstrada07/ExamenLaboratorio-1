

public class UsuarioEstandar extends Usuario {

    private static final int LIMITE_PRESTAMOS = 3;

    public UsuarioEstandar(String id, String nombre) {
        super(id, nombre);
    }

    @Override
    public int getLimitePrestamos() {
        return LIMITE_PRESTAMOS;
    }

    @Override
    public boolean tieneAutorizacionEspecial() {
        return false;
    }
}