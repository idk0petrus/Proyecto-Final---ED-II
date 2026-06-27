package modelo;

/** Arista ponderada que conecta 2 vértices en el grafo. */
public class AristaGrafo {
    private VerticeGrafo destino;  // Vértice al que se llega
    private int peso;              // Tiempo en minutos

    public AristaGrafo(VerticeGrafo destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public VerticeGrafo getDestino() { return destino; }
    public int getPeso() { return peso; }
    public void setPeso(int peso) { this.peso = peso; }

    @Override
    public String toString() {
        return destino.getNodoArbol().getDato().getId() + " (" + peso + " min)";
    }
}