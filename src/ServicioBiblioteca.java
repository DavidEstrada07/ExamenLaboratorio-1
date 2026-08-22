package com.mycompany.examenlaboratorio_1_main.servicio;//Jhoveth Moncada

import com.mycompany.examenlaboratorio_1_main.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ServicioBiblioteca {

   

    private ArrayList<MaterialBiblioteca> materiales;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Prestamo> prestamos;

    

    public ServicioBiblioteca() {
        materiales = new ArrayList<>();
        usuarios = new ArrayList<>();
        prestamos = new ArrayList<>();
    }

    

    public void agregarMaterial(MaterialBiblioteca material) {
        if (material == null) {
            return;
        }

        materiales.add(material);
    }

    

    public void agregarUsuario(Usuario usuario) {
        if (usuario == null) {
            return;
        }

        usuarios.add(usuario);
    }

    

    public Prestamo prestarMaterial(
            Usuario usuario,
            MaterialBiblioteca material
    ) throws BibliotecaException {

        if (usuario == null) {
            throw new BibliotecaException(
                    "El usuario no puede ser nulo."
            );
        }

        if (material == null) {
            throw new BibliotecaException(
                    "El material no puede ser nulo."
            );
        }

      

        if (usuario.estaPenalizado()) {
            throw new BibliotecaException(
                    "El usuario " + usuario.getNombre()
                    + " está penalizado hasta "
                    + usuario.getFinPenalizacion() + "."
            );
        }

       

        if (usuario.getMaterialesPrestados().size()
                >= usuario.getLimitePrestamos()) {

            throw new LimitePrestamosExcedidoException(
                    usuario.getNombre(),
                    usuario.getLimitePrestamos()
            );
        }

       

        if (material.getNivelComplejidad().requiereAutorizacion()
                && !usuario.tieneAutorizacionEspecial()) {

            throw new AutorizacionRequeridaException(
                    usuario.getNombre(),
                    material.getTitulo()
            );
        }

       

        if (!(material instanceof Prestable)) {
            throw new BibliotecaException(
                    "El material \"" + material.getTitulo()
                    + "\" no puede ser prestado."
            );
        }

        Prestable prestable = (Prestable) material;

        

        if (!prestable.estaDisponible()) {
            throw new MaterialYaPrestadoException(
                    material.getTitulo()
            );
        }

        

        prestable.prestar(usuario);

        int diasPrestamo = material.calcularDiasPrestados();

        Prestamo prestamo = new Prestamo(
                material,
                usuario,
                diasPrestamo
        );

        // Guardar en historial general
        prestamos.add(prestamo);

        // Guardar en historial del usuario
        usuario.registrarPrestamo(prestamo);

        // Agregar material a préstamos actuales
        usuario.getMaterialesPrestados().add(material);

        return prestamo;
    }


    public void devolverMaterial(
            Usuario usuario,
            MaterialBiblioteca material
    ) throws BibliotecaException {

        if (usuario == null || material == null) {
            throw new BibliotecaException(
                    "El usuario y el material son obligatorios."
            );
        }

    

        Prestamo prestamoEncontrado = null;

        for (Prestamo prestamo : prestamos) {

            if (prestamo.getUsuario() == usuario
                    && prestamo.getMaterial() == material
                    && prestamo.getFechaDevolucionReal() == null) {

                prestamoEncontrado = prestamo;
                break;
            }
        }

        if (prestamoEncontrado == null) {
            throw new BibliotecaException(
                    "No existe un préstamo activo para este usuario."
            );
        }

 

        usuario.registrarDevolucion(prestamoEncontrado);

       

        if (material instanceof Prestable) {

            Prestable prestable = (Prestable) material;

            prestable.devolver();
        }

   

        if (material instanceof Reservable) {

            Reservable reservable =
                    (Reservable) material;

            if (reservable.tieneReservasPendientes()) {

                // Hay personas esperando.
                // El material NO queda disponible para cualquiera.
                material.cambiarEstado(Estado.RESERVADO);

            } else {

                // Nadie espera.
                material.cambiarEstado(Estado.DISPONIBLE);
            }
        }
    }

 

    public void reservarMaterial(
            Usuario usuario,
            MaterialBiblioteca material
    ) throws BibliotecaException {

        if (usuario == null || material == null) {
            throw new BibliotecaException(
                    "El usuario y el material son obligatorios."
            );
        }

      

        if (!(usuario instanceof UsuarioPremium)) {
            throw new BibliotecaException(
                    "Solo los usuarios premium pueden realizar reservas."
            );
        }

    

        if (!(material instanceof Reservable)) {
            throw new BibliotecaException(
                    "El material \"" + material.getTitulo()
                    + "\" no permite reservas."
            );
        }

       

        if (material.getEstado() == Estado.DISPONIBLE) {
            throw new BibliotecaException(
                    "El material \"" + material.getTitulo()
                    + "\" está disponible. No necesita reserva."
            );
        }

        Reservable reservable =
                (Reservable) material;

        UsuarioPremium premium =
                (UsuarioPremium) usuario;

   

        premium.reservarMaterial(reservable);

        // Si estaba prestado, continúa reservado.
        material.cambiarEstado(Estado.RESERVADO);
    }



    public void cancelarReserva(
            Usuario usuario,
            MaterialBiblioteca material
    ) throws BibliotecaException {

        if (usuario == null || material == null) {
            throw new BibliotecaException(
                    "El usuario y el material son obligatorios."
            );
        }

        if (!(usuario instanceof UsuarioPremium)) {
            throw new BibliotecaException(
                    "Solo los usuarios premium pueden cancelar reservas."
            );
        }

        if (!(material instanceof Reservable)) {
            throw new BibliotecaException(
                    "El material no permite reservas."
            );
        }

        UsuarioPremium premium =
                (UsuarioPremium) usuario;

        Reservable reservable =
                (Reservable) material;

        premium.cancelarReservaMaterial(reservable);

        // Si ya no quedan reservas y el material no está prestado,
        // puede volver a estar disponible.
        if (!reservable.tieneReservasPendientes()
                && material.getEstado() != Estado.PRESTADO) {

            material.cambiarEstado(Estado.DISPONIBLE);
        }
    }



    public Usuario obtenerSiguienteUsuarioEnReserva(
            MaterialBiblioteca material
    ) throws BibliotecaException {

        if (!(material instanceof Reservable)) {
            throw new BibliotecaException(
                    "El material no permite reservas."
            );
        }

        Reservable reservable =
                (Reservable) material;

        return reservable.obtenerSiguienteReserva();
    }



    public MaterialBiblioteca buscarPorTituloOCodigo(
            String dato
    ) {

        return buscarRecursivo(dato, 0);
    }

    private MaterialBiblioteca buscarRecursivo(
            String dato,
            int indice
    ) {

        // Caso base
        if (indice >= materiales.size()) {
            return null;
        }

        MaterialBiblioteca material =
                materiales.get(indice);

        // Buscar por título o código
        if (material.getTitulo().equalsIgnoreCase(dato)
                || material.getCodigo().equalsIgnoreCase(dato)) {

            return material;
        }

        // Recursividad
        return buscarRecursivo(
                dato,
                indice + 1
        );
    }

 

    public List<MaterialBiblioteca> buscarPorCriterio(
            NivelComplejidad nivel
    ) {

        ArrayList<MaterialBiblioteca> resultado =
                new ArrayList<>();

        buscarCriterioRecursivo(
                nivel,
                0,
                resultado
        );

        return resultado;
    }

    private void buscarCriterioRecursivo(
            NivelComplejidad nivel,
            int indice,
            List<MaterialBiblioteca> resultado
    ) {

        // Caso base
        if (indice >= materiales.size()) {
            return;
        }

        MaterialBiblioteca material =
                materiales.get(indice);

        if (material.getNivelComplejidad() == nivel) {
            resultado.add(material);
        }

        buscarCriterioRecursivo(
                nivel,
                indice + 1,
                resultado
        );
    }

  

    public void ordenarPorTitulo() {
        Collections.sort(materiales);
    }

    

    public void ordenarPorComplejidad() {

        materiales.sort(
                Comparator.comparingInt(
                        m -> m.getNivelComplejidad()
                                .getOrdenComplejidad()
                )
        );
    }

   

    public <T extends MaterialBiblioteca>
            List<T> obtenerPorTipo(Class<T> tipo) {

        ArrayList<T> resultado =
                new ArrayList<>();

        for (MaterialBiblioteca material : materiales) {

            if (tipo.isInstance(material)) {

                resultado.add(
                        tipo.cast(material)
                );
            }
        }

        return resultado;
    }

  

    public List<Prestamo> obtenerHistorialPrestamos() {

        return new ArrayList<>(prestamos);
    }

    

    public MaterialBiblioteca obtenerMaterialMasSolicitado() {

        if (prestamos.isEmpty()) {
            return null;
        }

        MaterialBiblioteca resultado = null;
        int mayorCantidad = 0;

        for (MaterialBiblioteca material : materiales) {

            int cantidad = 0;

            for (Prestamo prestamo : prestamos) {

                if (prestamo.getMaterial() == material) {
                    cantidad++;
                }
            }

            if (cantidad > mayorCantidad) {

                mayorCantidad = cantidad;
                resultado = material;
            }
        }

        return resultado;
    }



    public int calcularPenalizacionUsuario(
            Usuario usuario
    ) {

        if (usuario == null) {
            return 0;
        }

        return usuario.calcularDiasPenalizacionAcumulada();
    }



    public ArrayList<MaterialBiblioteca> getMateriales() {
        return materiales;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public ArrayList<Prestamo> getPrestamos() {
        return prestamos;
    }
}
