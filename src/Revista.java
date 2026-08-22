public class Revista extends MaterialBiblioteca{
    private int numeroEdicion;
    private Periocidad  periocidad;

    public Revista(String titulo, String codigo, Estado estado, int diasMaximo, NivelComplejidad nivelComplejidad, String referenciaImagen, int numeroEdicion, Periocidad periocidad) {
        super(titulo, codigo, estado, diasMaximo, nivelComplejidad, referenciaImagen);
        this.numeroEdicion = numeroEdicion;
        this.periocidad = periocidad;
    }

    @Override
    public String descripcion() {
        return "Libro: "+titulo+
                "Estado: "+estado+
                "nivel de Complejidad: "+nivelComplejidad+
                "Codigo: "+codigo;
    }

    @Override
    public int calcularDiasPrestados() {
        return diasMaximo+nivelComplejidad.getDiasAdicionales();
    }

    public int getNumeroEdicion() {
        return numeroEdicion;
    }

    public Periocidad getPeriocidad() {
        return periocidad;
    }
}
