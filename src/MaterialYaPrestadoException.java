

public class MaterialYaPrestadoException extends BibliotecaException {

    public MaterialYaPrestadoException(String tituloMaterial) {
        super("El material \"" + tituloMaterial + "\" ya esta prestado y no puede volver a prestarse.");
    }
}