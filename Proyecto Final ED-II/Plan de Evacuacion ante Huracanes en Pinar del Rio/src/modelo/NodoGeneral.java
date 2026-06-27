package modelo;

import java.util.ArrayList;
import java.util.List;

/** Nodo del Árbol General. Almacena una ubicacion y referencias a su padre e hijos.
 */
public class NodoGeneral {
    private Ubicacion dato;
    private NodoGeneral padre;
    private List<NodoGeneral> hijos;

    public NodoGeneral(Ubicacion dato) {
        this.dato = dato;
        this.padre = null;
        this.hijos = new ArrayList<>();
    }

    // Getters y Setters
    public Ubicacion getDato() { return dato; }
    public void setDato(Ubicacion dato) { this.dato = dato; }
    public NodoGeneral getPadre() { return padre; }
    public void setPadre(NodoGeneral padre) { this.padre = padre; }
    public List<NodoGeneral> getHijos() { return hijos; }

    /** Agrega un hijo y establece su padre. */
    public void agregarHijo(NodoGeneral hijo) {
        hijo.setPadre(this);
        hijos.add(hijo);
    }

    /** Elimina un hijo de la lista. */
    public boolean eliminarHijo(NodoGeneral hijo) {
        return hijos.remove(hijo);
    }

    /** Verifica si es hoja. */
    public boolean esHoja() {
        return hijos.isEmpty();
    }

    @Override
    public String toString() {
        return dato.toString();
    }
}