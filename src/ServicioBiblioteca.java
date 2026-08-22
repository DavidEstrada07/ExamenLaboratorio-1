

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicioBiblioteca {

    private final List<Material> materiales;
    private final List<Usuario> usuarios;
    private final Map<String, Deque<Usuario>> reservas;
    private final List<RegistroPrestamo> historial;
    private final Map<String, Integer> cantidadPrestamos;
    private final Map<String, Usuario> prestamosActuales;
    private final Map<String, Integer> penalizaciones;

    private static final int LIMITE_PRESTAMOS = 3;

    public ServicioBiblioteca() {
        materiales = new ArrayList<>();
        usuarios = new ArrayList<>();
        reservas = new HashMap<>();
        historial = new ArrayList<>();
        cantidadPrestamos = new HashMap<>();
        prestamosActuales = new HashMap<>();
        penalizaciones = new HashMap<>();
    }

    public void agregarMaterial(Material material) {
        if (material == null) {
            throw new BibliotecaException("El material no puede ser null.");
        }

        if (buscarPorCodigo(material.getCodigo()) != null) {
            throw new BibliotecaException(
                    "Ya existe un material con el codigo: "
                    + material.getCodigo()
            );
        }

        materiales.add(material);
        reservas.put(material.getCodigo(), new ArrayDeque<>());
        cantidadPrestamos.put(material.getCodigo(), 0);
    }

    public void agregarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new BibliotecaException("El usuario no puede ser null.");
        }

        if (buscarUsuario(usuario.getCodigo()) != null) {
            throw new BibliotecaException(
                    "Ya existe un usuario con el codigo: "
                    + usuario.getCodigo()
            );
        }

        usuarios.add(usuario);
        penalizaciones.put(usuario.getCodigo(), 0);
    }

    public Material buscarPorCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }

        for (Material material : materiales) {
            if (codigo.equalsIgnoreCase(material.getCodigo())) {
                return material;
            }
        }

        return null;
    }

    public Usuario buscarUsuario(String codigo) {
        if (codigo == null) {
            return null;
        }

        for (Usuario usuario : usuarios) {
            if (codigo.equalsIgnoreCase(usuario.getCodigo())) {
                return usuario;
            }
        }

        return null;
    }

    public Material buscarRecursivoPorCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }

        return buscarCodigoRecursivo(codigo, 0);
    }

    private Material buscarCodigoRecursivo(String codigo, int posicion) {
        if (posicion >= materiales.size()) {
            return null;
        }

        Material actual = materiales.get(posicion);

        if (codigo.equalsIgnoreCase(actual.getCodigo())) {
            return actual;
        }

        return buscarCodigoRecursivo(codigo, posicion + 1);
    }

    public Material buscarRecursivoPorTitulo(String titulo) {
        if (titulo == null) {
            return null;
        }

        return buscarTituloRecursivo(titulo, 0);
    }

    private Material buscarTituloRecursivo(String titulo, int posicion) {
        if (posicion >= materiales.size()) {
            return null;
        }

        Material actual = materiales.get(posicion);

        if (titulo.equalsIgnoreCase(actual.getTitulo())) {
            return actual;
        }

        return buscarTituloRecursivo(titulo, posicion + 1);
    }

    @FunctionalInterface
    public interface CriterioMaterial {
        boolean cumple(Material material);
    }

    public List<Material> buscarRecursivo(CriterioMaterial criterio) {
        List<Material> resultados = new ArrayList<>();

        if (criterio == null) {
            return resultados;
        }

        buscarRecursivo(criterio, 0, resultados);

        return resultados;
    }

    private void buscarRecursivo(
            CriterioMaterial criterio,
            int posicion,
            List<Material> resultados
    ) {
        if (posicion >= materiales.size()) {
            return;
        }

        Material actual = materiales.get(posicion);

        if (criterio.cumple(actual)) {
            resultados.add(actual);
        }

        buscarRecursivo(
                criterio,
                posicion + 1,
                resultados
        );
    }

    public List<Material> buscarPorComplejidad(
            NivelComplejidad nivel
    ) {
        return buscarRecursivo(
                material -> material.getNivelComplejidad() == nivel
        );
    }

    public List<Material> buscarDisponibles() {
        return buscarRecursivo(
                this::estaDisponible
        );
    }

    public List<Material> ordenarPorTitulo() {
        List<Material> copia = new ArrayList<>(materiales);
        copia.sort(Comparator.naturalOrder());
        return copia;
    }

    public List<Material> ordenarPorComplejidad() {
        List<Material> copia = new ArrayList<>(materiales);

        copia.sort(
                Comparator.comparing(
                        Material::getNivelComplejidad,
                        Comparator.nullsLast(
                                Comparator.comparing(Enum::ordinal)
                        )
                )
        );

        return copia;
    }

    public <T extends Material> List<T> filtrarPorTipo(
            Class<T> tipo
    ) {
        List<T> resultado = new ArrayList<>();

        if (tipo == null) {
            return resultado;
        }

        for (Material material : materiales) {
            if (tipo.isInstance(material)) {
                resultado.add(tipo.cast(material));
            }
        }

        return resultado;
    }

    public void prestarMaterial(
            String codigoMaterial,
            String codigoUsuario
    ) {
        Material material = buscarPorCodigo(codigoMaterial);
        Usuario usuario = buscarUsuario(codigoUsuario);

        if (material == null) {
            throw new BibliotecaException(
                    "Material no encontrado."
            );
        }

        if (usuario == null) {
            throw new BibliotecaException(
                    "Usuario no encontrado."
            );
        }

        if (!estaDisponible(material)) {
            throw new MaterialPrestadoException(
                    "El material esta prestado."
            );
        }

        int prestamosUsuario = contarPrestamosUsuario(usuario);

        if (prestamosUsuario >= LIMITE_PRESTAMOS) {
            throw new LimitePrestamosException(
                    "El usuario alcanzo el limite de "
                    + LIMITE_PRESTAMOS
                    + " prestamos."
            );
        }

        int penalizacion = penalizaciones.getOrDefault(
                usuario.getCodigo(),
                0
        );

        if (penalizacion > 0) {
            throw new BibliotecaException(
                    "El usuario tiene una penalizacion vigente."
            );
        }

        if (!usuarioPuedePrestar(usuario, material)) {
            throw new AutorizacionException(
                    "El usuario no esta autorizado para "
                    + "este nivel de complejidad."
            );
        }

        Deque<Usuario> cola = reservas.get(
                material.getCodigo()
        );

        if (cola != null && !cola.isEmpty()) {
            Usuario primero = cola.peek();

            if (!primero.getCodigo()
                    .equalsIgnoreCase(usuario.getCodigo())) {

                throw new BibliotecaException(
                        "El material esta reservado para "
                        + primero.getNombre()
                );
            }

            cola.poll();
        }

        prestamosActuales.put(
                material.getCodigo(),
                usuario
        );

        cantidadPrestamos.put(
                material.getCodigo(),
                cantidadPrestamos.getOrDefault(
                        material.getCodigo(),
                        0
                ) + 1
        );

        historial.add(
                new RegistroPrestamo(
                        material,
                        usuario
                )
        );

        cambiarEstadoPrestado(material);
    }

    public void reservarMaterial(
            String codigoMaterial,
            String codigoUsuario
    ) {
        Material material = buscarPorCodigo(codigoMaterial);
        Usuario usuario = buscarUsuario(codigoUsuario);

        if (material == null) {
            throw new BibliotecaException(
                    "Material no encontrado."
            );
        }

        if (usuario == null) {
            throw new BibliotecaException(
                    "Usuario no encontrado."
            );
        }

        if (estaDisponible(material)) {
            throw new BibliotecaException(
                    "El material esta disponible."
            );
        }

        Deque<Usuario> cola = reservas.computeIfAbsent(
                material.getCodigo(),
                k -> new ArrayDeque<>()
        );

        for (Usuario reservado : cola) {
            if (reservado.getCodigo()
                    .equalsIgnoreCase(usuario.getCodigo())) {

                throw new BibliotecaException(
                        "El usuario ya esta en la cola de reservas."
                );
            }
        }

        cola.offer(usuario);
    }

    public List<Usuario> obtenerReservas(
            String codigoMaterial
    ) {
        Deque<Usuario> cola = reservas.get(codigoMaterial);

        if (cola == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(cola);
    }

    public void devolverMaterial(
            String codigoMaterial
    ) {
        Material material = buscarPorCodigo(codigoMaterial);

        if (material == null) {
            throw new BibliotecaException(
                    "Material no encontrado."
            );
        }

        Usuario usuario = prestamosActuales.remove(
                material.getCodigo()
        );

        if (usuario == null) {
            throw new BibliotecaException(
                    "El material no se encuentra prestado."
            );
        }

        Deque<Usuario> cola = reservas.get(
                material.getCodigo()
        );

        if (cola != null && !cola.isEmpty()) {
            cambiarEstadoReservado(material);
        } else {
            cambiarEstadoDisponible(material);
        }
    }

    public void agregarPenalizacion(
            String codigoUsuario,
            int dias
    ) throws BibliotecaException {
        if (dias <= 0) {
            return;
        }

        Usuario usuario = buscarUsuario(codigoUsuario);

        if (usuario == null) {
            throw new BibliotecaException(
                    "Usuario no encontrado."
            );
        }

        int actual = penalizaciones.getOrDefault(
                codigoUsuario,
                0
        );

        penalizaciones.put(
                codigoUsuario,
                actual + dias
        );
    }

    public void reducirPenalizacion(
            String codigoUsuario,
            int dias
    ) {
        if (dias <= 0) {
            return;
        }

        int actual = penalizaciones.getOrDefault(
                codigoUsuario,
                0
        );

        penalizaciones.put(
                codigoUsuario,
                Math.max(0, actual - dias)
        );
    }

    public int obtenerPenalizacion(
            String codigoUsuario
    ) {
        return penalizaciones.getOrDefault(
                codigoUsuario,
                0
        );
    }

    public List<RegistroPrestamo> obtenerHistorial() {
        return new ArrayList<>(historial);
    }

    public List<Material> materialesMasSolicitados() {
        List<Material> resultado =
                new ArrayList<>(materiales);

        resultado.sort(
                Comparator.comparingInt(
                        (Material material) ->
                                cantidadPrestamos.getOrDefault(
                                        material.getCodigo(),
                                        0
                                )
                ).reversed()
        );

        return resultado;
    }

    public int obtenerCantidadPrestamos(
            String codigoMaterial
    ) {
        return cantidadPrestamos.getOrDefault(
                codigoMaterial,
                0
        );
    }

    private int contarPrestamosUsuario(
            Usuario usuario
    ) {
        int contador = 0;

        for (Usuario usuarioPrestamo :
                prestamosActuales.values()) {

            if (usuarioPrestamo.getCodigo()
                    .equalsIgnoreCase(usuario.getCodigo())) {

                contador++;
            }
        }

        return contador;
    }

    private boolean estaDisponible(
            Material material
    ) {
        return !prestamosActuales.containsKey(
                material.getCodigo()
        );
    }

    private boolean usuarioPuedePrestar(
            Usuario usuario,
            Material material
    ) {
        return true;
    }

    private void cambiarEstadoPrestado(
            Material material
    ) {
        material.setEstado(
                EstadoMaterial.PRESTADO
        );
    }

    private void cambiarEstadoDisponible(
            Material material
    ) {
        material.setEstado(
                EstadoMaterial.DISPONIBLE
        );
    }

    private void cambiarEstadoReservado(
            Material material
    ) {
        material.setEstado(
                EstadoMaterial.RESERVADO
        );
    }

    public static class RegistroPrestamo {

        private final Material material;
        private final Usuario usuario;

        public RegistroPrestamo(
                Material material,
                Usuario usuario
        ) {
            this.material = material;
            this.usuario = usuario;
        }

        public Material getMaterial() {
            return material;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        @Override
        public String toString() {
            return "Prestamo{"
                    + "material="
                    + material.getTitulo()
                    + ", usuario="
                    + usuario.getNombre()
                    + '}';
        }
    }

    public List<Material> getMateriales() {
        return new ArrayList<>(materiales);
    }

    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios);
    }
}
    public ArrayList<Prestamo> getPrestamos() {
        return prestamos;
    }
}
