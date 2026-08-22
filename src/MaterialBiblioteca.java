public abstract class MaterialBiblioteca {
    protected String titulo;
    protected String codigo;
    protected Estado estado;
    protected int diasMaximo;
    protected NivelComplejidad nivelComplejidad;
    protected String referenciaImagen;

    public MaterialBiblioteca(String titulo, String codigo, Estado estado, int diasMaximo, NivelComplejidad nivelComplejidad, String referenciaImagen) {
        this.titulo = titulo;
        this.codigo = codigo;
        this.estado = estado;
        this.diasMaximo = diasMaximo;
        this.nivelComplejidad = nivelComplejidad;
        this.referenciaImagen = referenciaImagen;
    }

    public abstract String descripcion();
    public abstract int calcularDiasPrestados();

    public String getTitulo() {
        return titulo;
    }

    public String getCodigo() {
        return codigo;
    }

    public Estado getEstado() {
        return estado;
    }

    public int getDiasMaximo() {
        return diasMaximo;
    }

    public NivelComplejidad getNivelComplejidad() {
        return nivelComplejidad;
    }

    public String getReferenciaImagen() {
        return referenciaImagen;
    }

    
}
