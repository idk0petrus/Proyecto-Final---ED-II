package modelo;

import java.util.ArrayList;
import java.util.List;

/** Vértice del grafo: enlaza un nodo del árbol con sus conexiones. */
public class VerticeGrafo {
    private NodoGeneral nodoArbol;
    private List<AristaGrafo> aristas;

    public VerticeGrafo(NodoGeneral nodoArbol) {
        this.nodoArbol = nodoArbol;
        this.aristas = new ArrayList<>();
    }

    public NodoGeneral getNodoArbol() { return nodoArbol; }
    public List<AristaGrafo> getAristas() { return aristas; }

    /** Agrega una arista al destino, evitando duplicados. */
    public void agregarArista(VerticeGrafo destino, int peso) {
        for (AristaGrafo a : aristas) {
            if (a.getDestino().equals(destino)) return;
        }
        aristas.add(new AristaGrafo(destino, peso));
    }

    /** Elimina la arista hacia un destino específico. */
    public boolean eliminarArista(VerticeGrafo destino) {
        return aristas.removeIf(a -> a.getDestino().equals(destino));
    }

    /** Modifica el peso de una arista existente. */
    public void modificarPeso(VerticeGrafo destino, int nuevoPeso) {
        for (AristaGrafo a : aristas) {
            if (a.getDestino().equals(destino)) {
                a.setPeso(nuevoPeso);
                return;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VerticeGrafo that = (VerticeGrafo) o;
        return nodoArbol.getDato().getId().equals(that.nodoArbol.getDato().getId());
    }

    @Override
    public int hashCode() {
        return nodoArbol.getDato().getId().hashCode();
    }
}