

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Usuario {

    protected String id;
    protected String nombre;
    protected List<Material> materialesPrestados;
    protected List<Prestamo> historialPrestamos;
    protected LocalDate finPenalizacion;

    public Usuario(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.materialesPrestados = new ArrayList<>();
        this.historialPrestamos = new ArrayList<>();
        this.finPenalizacion = null;
    }

    // Cada perfil define su propio limite (polimorfismo)
    public abstract int getLimitePrestamos();

    // Solo el perfil premium devuelve true
    public abstract boolean tieneAutorizacionEspecial();

    public boolean estaPenalizado() {
        return finPenalizacion != null && LocalDate.now().isBefore(finPenalizacion);
    }

    public void aplicarPenalizacion(int diasPenalizacion) {
        LocalDate base = LocalDate.now();
        if (finPenalizacion != null && finPenalizacion.isAfter(base)) {
            base = finPenalizacion;
        }
        finPenalizacion = base.plusDays(diasPenalizacion);
    }

    public boolean puedeSolicitarPrestamo() {
        return !estaPenalizado() && materialesPrestados.size() < getLimitePrestamos();
    }

    public void registrarPrestamo(Prestamo prestamo) {
        historialPrestamos.add(prestamo);
    }

    public void registrarDevolucion(Prestamo prestamo) {
        prestamo.registrarDevolucion();
        if (prestamo.estaVencido()) {
            aplicarPenalizacion((int) prestamo.getDiasRetraso());
        }
        materialesPrestados.remove(prestamo.getMaterial());
    }

    public int calcularDiasPenalizacionAcumulada() {
        return calcularDiasPenalizacionAcumulada(historialPrestamos, 0);
    }

    private int calcularDiasPenalizacionAcumulada(List<Prestamo> lista, int indice) {
        if (indice >= lista.size()) {
            return 0;
        }
        Prestamo actual = lista.get(indice);
        int dias = actual.estaVencido() ? (int) actual.getDiasRetraso() : 0;
        return dias + calcularDiasPenalizacionAcumulada(lista, indice + 1);
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Material> getMaterialesPrestados() {
        return materialesPrestados;
    }

    public List<Prestamo> getHistorialPrestamos() {
        return historialPrestamos;
    }

    public LocalDate getFinPenalizacion() {
        return finPenalizacion;
    }

    private static class Material {

        public Material() {
        }
    }
}