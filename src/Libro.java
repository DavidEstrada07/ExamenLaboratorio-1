public class Libro extends MaterialBiblioteca{
    private String autor;
    private int numeroPaginas;
    private String isbn;

    public Libro(String titulo, String codigo, Estado estado, int diasMaximo, NivelComplejidad nivelComplejidad, String referenciaImagen, String autor, int numeroPaginas, String ISBN) {
        super(titulo, codigo, estado, diasMaximo, nivelComplejidad, referenciaImagen);
        this.autor = autor;
        this.numeroPaginas = numeroPaginas;
        this.isbn = isbn;
    }

    @Override
    public String descripcion() {
        return "Libro: "+titulo+
                "Autor: "+autor+
                "Estado: "+estado+
                "nivel de Complejidad: "+nivelComplejidad+
                "Codigo: "+codigo+
                "ISBN: "+isbn;
    }

    @Override
    public int calcularDiasPrestados() {
        int dias=diasMaximo;
        return dias+nivelComplejidad.getDiasAdicionales();
    }

    public String getAutor() {
        return autor;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public String getIsbn() {
        return isbn;
    }
}
