package modelo;

import java.util.Objects;

/** Representa una ubicación geografica en Pinar del Rio, ya sea provincia, municipio, comunidad o refugio.
 */
public class Ubicacion {
    private String id;
    private String nombre;
    private String tipo;
    private int capacidad;
    private int poblacion;

    public Ubicacion(String id, String nombre, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidad = 0;
        this.poblacion = 0;
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public int getPoblacion() { return poblacion; }
    public void setPoblacion(int poblacion) { this.poblacion = poblacion; }

    @Override
    public String toString() {
        String info = id + " - " + nombre + " (" + tipo + ")";
        if (tipo.equals("refugio")) info += " | Cap: " + capacidad + " pers.";
        if (tipo.equals("comunidad")) info += " | Pob: " + poblacion + " hab.";
        return info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ubicacion that = (Ubicacion) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}