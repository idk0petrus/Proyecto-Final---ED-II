package modelo;

import java.util.*;

/**
 * Árbol General que modela la jerarquía territorial de Pinar del Río.
 * Permite insertar, buscar, eliminar y modificar ubicaciones.
 * Ofrece recorridos preorden y postorden.
 */
public class ArbolGeneral {
    private NodoGeneral raiz;

    public ArbolGeneral() {
        raiz = null;
    }

    /**
     * Inserta una nueva ubicación bajo un padre existente.
     * @param idPadre ID del nodo padre (debe existir)
     * @param nueva Ubicación a insertar
     * @return true si se insertó, false si el padre no existe
     */
    public boolean insertar(String idPadre, Ubicacion nueva) {
        if (raiz == null) {
            raiz = new NodoGeneral(nueva);
            return true;
        }
        NodoGeneral padre = buscar(idPadre);
        if (padre == null) return false;
        padre.agregarHijo(new NodoGeneral(nueva));
        return true;
    }

    /** Busca un nodo por su ID (recursivo). */
    public NodoGeneral buscar(String id) {
        return buscarRec(raiz, id);
    }

    private NodoGeneral buscarRec(NodoGeneral nodo, String id) {
        if (nodo == null) return null;
        if (nodo.getDato().getId().equals(id)) return nodo;
        for (NodoGeneral hijo : nodo.getHijos()) {
            NodoGeneral resultado = buscarRec(hijo, id);
            if (resultado != null) return resultado;
        }
        return null;
    }

    /**
     * Elimina un nodo (no raíz) y todos sus descendientes.
     * @param id ID del nodo a eliminar
     * @return true si se eliminó, false si es la raíz o no existe
     */
    public boolean eliminar(String id) {
        if (raiz == null) return false;
        if (raiz.getDato().getId().equals(id)) {
            System.out.println("No se puede eliminar la raiz.");
            return false;
        }
        NodoGeneral nodo = buscar(id);
        if (nodo == null) return false;
        NodoGeneral padre = nodo.getPadre();
        return padre.eliminarHijo(nodo);
    }

    /**
     * Modifica los atributos de una ubicación (nombre, tipo, capacidad, población).
     */
    public boolean modificar(String id, String nuevoNombre, String nuevoTipo,
                             int capacidad, int poblacion) {
        NodoGeneral nodo = buscar(id);
        if (nodo == null) return false;
        Ubicacion u = nodo.getDato();
        if (nuevoNombre != null && !nuevoNombre.isEmpty()) u.setNombre(nuevoNombre);
        if (nuevoTipo != null && !nuevoTipo.isEmpty()) u.setTipo(nuevoTipo);
        u.setCapacidad(capacidad);
        u.setPoblacion(poblacion);
        return true;
    }

    /** Recorrido preorden. */
    public List<Ubicacion> preorden() {
        List<Ubicacion> lista = new ArrayList<>();
        preordenRec(raiz, lista);
        return lista;
    }

    private void preordenRec(NodoGeneral nodo, List<Ubicacion> lista) {
        if (nodo == null) return;
        lista.add(nodo.getDato());
        for (NodoGeneral hijo : nodo.getHijos()) {
            preordenRec(hijo, lista);
        }
    }

    /** Recorrido postorden. */
    public List<Ubicacion> postorden() {
        List<Ubicacion> lista = new ArrayList<>();
        postordenRec(raiz, lista);
        return lista;
    }

    private void postordenRec(NodoGeneral nodo, List<Ubicacion> lista) {
        if (nodo == null) return;
        for (NodoGeneral hijo : nodo.getHijos()) {
            postordenRec(hijo, lista);
        }
        lista.add(nodo.getDato());
    }

    public NodoGeneral getRaiz() { return raiz; }
    public boolean isEmpty() { return raiz == null; }
}