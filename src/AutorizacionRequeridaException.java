

public class AutorizacionRequeridaException extends BibliotecaException {

    public AutorizacionRequeridaException(String nombreUsuario, String tituloMaterial) {
        super("El usuario " + nombreUsuario + " no tiene autorizacion para prestar \"" + tituloMaterial
                + "\" porque es de nivel de complejidad alto.");
    }
}