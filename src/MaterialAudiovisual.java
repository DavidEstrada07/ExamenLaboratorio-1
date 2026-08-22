public class MaterialAudiovisual extends MaterialBiblioteca{
    private int duracionMinutos;
    private Formato formato;

    public MaterialAudiovisual(String titulo, String codigo, Estado estado, int diasMaximo, NivelComplejidad nivelComplejidad, String referenciaImagen, int duracionMinutos, Formato formato) {
        super(titulo, codigo, estado, diasMaximo, nivelComplejidad, referenciaImagen);
        this.duracionMinutos = duracionMinutos;
        this.formato = formato;
    }

    @Override
    public String descripcion() {
        return "Libro: "+titulo+
                "Estado: "+estado+
                "Formato: "+formato+
                "Duracion: "+duracionMinutos+
                "nivel de Complejidad: "+nivelComplejidad+
                "Codigo: "+codigo;
    }

    @Override
    public int calcularDiasPrestados() {
        int dias = diasMaximo;
        if (formato.equals(Formato.DVD)){
            dias++;
        }
        return dias+nivelComplejidad.getDiasAdicionales();
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public Formato getFormato() {
        return formato;
    }
}
