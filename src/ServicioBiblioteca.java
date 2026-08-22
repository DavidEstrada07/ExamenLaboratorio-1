
package com.mycompany.examenlaboratorio_1_main;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Prestamo {

    private final MaterialBiblioteca material;
    private Usuario usuario;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionPrevista;
    private LocalDate fechaDevolucionReal;

    public Prestamo(
            MaterialBiblioteca material,
            Usuario usuario,
            int diasPrestamo
    ) {
        this.material = material;
        this.usuario = usuario;
        this.fechaPrestamo = LocalDate.now();
        this.fechaDevolucionPrevista =
                fechaPrestamo.plusDays(diasPrestamo);
        this.fechaDevolucionReal = null;
    }

    public void registrarDevolucion() {
        this.fechaDevolucionReal = LocalDate.now();
    }

    public boolean estaVencido() {
        LocalDate referencia =
                (fechaDevolucionReal != null)
                        ? fechaDevolucionReal
                        : LocalDate.now();

        return referencia.isAfter(fechaDevolucionPrevista);
    }

    public boolean estaDevuelto() {
        return fechaDevolucionReal != null;
    }

    public long getDiasRetraso() {
        if (!estaVencido()) {
            return 0;
        }

        LocalDate referencia =
                (fechaDevolucionReal != null)
                        ? fechaDevolucionReal
                        : LocalDate.now();

        return ChronoUnit.DAYS.between(
                fechaDevolucionPrevista,
                referencia
        );
    }

    public MaterialBiblioteca getMaterial() {
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
}
        return prestamos;
    }
}
