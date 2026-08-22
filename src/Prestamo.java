

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Prestamo {

    private final Material material;
    private Usuario usuario;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionPrevista;
    private LocalDate fechaDevolucionReal;

    // diasPrestamo viene de material.calcularDiasPrestamo(), que ya
    // incluye el ajuste por nivel de complejidad (punto 3)
    public Prestamo(Material material, Usuario usuario, int diasPrestamo) {
        this.material = material;
        this.usuario = usuario;
        this.fechaPrestamo = LocalDate.now();
        this.fechaDevolucionPrevista = fechaPrestamo.plusDays(diasPrestamo);
        this.fechaDevolucionReal = null;
    }

    public void registrarDevolucion() {
        this.fechaDevolucionReal = LocalDate.now();
    }

    public boolean estaVencido() {
        LocalDate referencia = (fechaDevolucionReal != null) ? fechaDevolucionReal : LocalDate.now();
        return referencia.isAfter(fechaDevolucionPrevista);
    }

    public long getDiasRetraso() {
        if (!estaVencido()) {
            return 0;
        }
        LocalDate referencia = (fechaDevolucionReal != null) ? fechaDevolucionReal : LocalDate.now();
        return ChronoUnit.DAYS.between(fechaDevolucionPrevista, referencia);
    }

    public Material getMaterial() {
        return material;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaDevolucionPrevista() {
        return fechaDevolucionPrevista;
    }

    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    private static class Material {

        public Material() {
        }
    }
}
