package com.mycompany.examenlaboratorio_1_main.servicio;

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
        materiales.add(material);
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    

    public MaterialBiblioteca buscarPorTituloOCodigo(String dato) {
        return buscarRecursivo(dato, 0);
    }

    private MaterialBiblioteca buscarRecursivo(String dato, int indice) {

    
        if (indice >= materiales.size()) {
            return null;
        }

        MaterialBiblioteca material = materiales.get(indice);

     
        if (material.getTitulo().equalsIgnoreCase(dato)
                || material.getCodigo().equalsIgnoreCase(dato)) {

            return material;
        }

      
        return buscarRecursivo(dato, indice + 1);
    }

   

    public List<MaterialBiblioteca> buscarPorCriterio(
            NivelComplejidad nivel) {

        ArrayList<MaterialBiblioteca> resultado = new ArrayList<>();

        buscarCriterioRecursivo(nivel, 0, resultado);

        return resultado;
    }

    private void buscarCriterioRecursivo(
            NivelComplejidad nivel,
            int indice,
            List<MaterialBiblioteca> resultado) {

       
        if (indice >= materiales.size()) {
            return;
        }

        MaterialBiblioteca material = materiales.get(indice);

        // Agregar material si coincide el nivel
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

   

    public <T extends MaterialBiblioteca> List<T> obtenerPorTipo(
            Class<T> tipo) {

        ArrayList<T> lista = new ArrayList<>();

        for (MaterialBiblioteca material : materiales) {

            if (tipo.isInstance(material)) {
                lista.add(tipo.cast(material));
            }
        }

        return lista;
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