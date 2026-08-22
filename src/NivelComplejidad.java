public enum NivelComplejidad {
    BAJO(1,0,"Nivel de complejidad Bajo"),
    MEDIO(2,3,"Nivel de complejidad Medio"),
    ALTO(3,6,"Nivel de complejidad Alto");

    private final int ordenComplejidad;
    private final int diasAdicionales;
    private final String nivelComplejidad;
    NivelComplejidad(int ordenComplejidad, int diasAdicionales, String nivelcomplejidad){
        this.ordenComplejidad=ordenComplejidad;
        this.diasAdicionales=diasAdicionales;
        this.nivelComplejidad=nivelcomplejidad;
    }

    public boolean requiereAutorizacionEspecial(){
        if (this == ALTO){
            return true;
        } return false;
    }

    public int getOrdenComplejidad() {
        return ordenComplejidad;
    }

    public int getDiasAdicionales() {
        return diasAdicionales;
    }

    public String getNivelComplejidad() {
        return nivelComplejidad;
    }
}
