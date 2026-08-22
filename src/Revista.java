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
                "Periocidad: "+periocidad+
                "nivel de Complejidad: "+nivelComplejidad+
                "Codigo: "+codigo+
                "Numero de Edicion: "+numeroEdicion;
    }

    @Override
    public int calcularDiasPrestados() {
        int dias = diasMaximo;
        if (periocidad.equals(Periocidad.MENSUAL)){
            dias++;
        }
        if (periocidad.equals(Periocidad.ANUAL)){
            dias+=2;
        }
        return dias+nivelComplejidad.getDiasAdicionales();
    }

    public int getNumeroEdicion() {
        return numeroEdicion;
    }

    public Periocidad getPeriocidad() {
        return periocidad;
    }
}
