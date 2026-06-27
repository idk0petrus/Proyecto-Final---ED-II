package modelo;

import java.util.*;

/**
 * Grafo no dirigido y ponderado que representa la red de carreteras entre ubicaciones de Pinar del Río.
 */
public class Grafo {
    private List<VerticeGrafo> vertices;

    public Grafo() {
        vertices = new ArrayList<>();
    }

    /** Agrega un vertice al grafo asociado a un nodo del árbol. */
    public void agregarVertice(NodoGeneral nodo) {
        if (obtenerVertice(nodo.getDato().getId()) == null) {
            vertices.add(new VerticeGrafo(nodo));
        }
    }

    /** Elimina un vertice y todas sus aristas incidentes. */
    public void eliminarVertice(String id) {
        VerticeGrafo v = obtenerVertice(id);
        if (v == null) return;
        // Eliminar aristas que apuntan a este vértice
        for (VerticeGrafo otro : vertices) {
            otro.eliminarArista(v);
        }
        vertices.remove(v);
    }

    /** Obtiene un vértice por ID. */
    public VerticeGrafo obtenerVertice(String id) {
        for (VerticeGrafo v : vertices) {
            if (v.getNodoArbol().getDato().getId().equals(id)) {
                return v;
            }
        }
        return null;
    }

    public List<VerticeGrafo> getVertices() { return vertices; }

    /** Agrega una arista no dirigida entre dos vértices. */
    public boolean agregarArista(String idOrigen, String idDestino, int peso) {
        VerticeGrafo origen = obtenerVertice(idOrigen);
        VerticeGrafo destino = obtenerVertice(idDestino);
        if (origen == null || destino == null) return false;
        origen.agregarArista(destino, peso);
        destino.agregarArista(origen, peso);  // Grafo no dirigido
        return true;
    }

    /** Elimina la arista entre dos vértices. */
    public boolean eliminarArista(String idOrigen, String idDestino) {
        VerticeGrafo origen = obtenerVertice(idOrigen);
        VerticeGrafo destino = obtenerVertice(idDestino);
        if (origen == null || destino == null) return false;
        boolean ok1 = origen.eliminarArista(destino);
        boolean ok2 = destino.eliminarArista(origen);
        return ok1 || ok2;
    }

    /** Modifica el peso de una arista existente. */
    public boolean modificarArista(String idOrigen, String idDestino, int nuevoPeso) {
        VerticeGrafo origen = obtenerVertice(idOrigen);
        VerticeGrafo destino = obtenerVertice(idDestino);
        if (origen == null || destino == null) return false;
        origen.modificarPeso(destino, nuevoPeso);
        destino.modificarPeso(origen, nuevoPeso);
        return true;
    }

    /** Muestra todas las aristas del grafo. */
    public void mostrarAristas() {
        for (VerticeGrafo v : vertices) {
            System.out.print(v.getNodoArbol().getDato().getId() + " -> ");
            for (AristaGrafo a : v.getAristas()) {
                System.out.print(a.toString() + " ");
            }
            System.out.println();
        }
    }

    /** Recorrido BFS. */
    public List<Ubicacion> bfs(String inicioId) {
        VerticeGrafo inicio = obtenerVertice(inicioId);
        if (inicio == null) return new ArrayList<>();
        List<Ubicacion> visitados = new ArrayList<>();
        Queue<VerticeGrafo> cola = new LinkedList<>();
        Set<VerticeGrafo> marcados = new HashSet<>();

        cola.add(inicio);
        marcados.add(inicio);

        while (!cola.isEmpty()) {
            VerticeGrafo v = cola.poll();
            visitados.add(v.getNodoArbol().getDato());
            for (AristaGrafo a : v.getAristas()) {
                if (!marcados.contains(a.getDestino())) {
                    marcados.add(a.getDestino());
                    cola.add(a.getDestino());
                }
            }
        }
        return visitados;
    }

    /** Recorrido DFS. */
    public List<Ubicacion> dfs(String inicioId) {
        VerticeGrafo inicio = obtenerVertice(inicioId);
        if (inicio == null) return new ArrayList<>();
        List<Ubicacion> visitados = new ArrayList<>();
        Set<VerticeGrafo> marcados = new HashSet<>();
        dfsRec(inicio, marcados, visitados);
        return visitados;
    }

    private void dfsRec(VerticeGrafo v, Set<VerticeGrafo> marcados, List<Ubicacion> visitados) {
        marcados.add(v);
        visitados.add(v.getNodoArbol().getDato());
        for (AristaGrafo a : v.getAristas()) {
            if (!marcados.contains(a.getDestino())) {
                dfsRec(a.getDestino(), marcados, visitados);
            }
        }
    }

    /**
     * Algoritmo de Dijkstra para encontrar la ruta + rápida entre 2 ubicaciones.
     * @param origenId ID del nodo de origen
     * @param destinoId ID del nodo de destino
     * @return Lista de ubicaciones que forman el camino mínimo, o null si no existe.
     */
    public List<Ubicacion> dijkstra(String origenId, String destinoId) {
        VerticeGrafo origen = obtenerVertice(origenId);
        VerticeGrafo destino = obtenerVertice(destinoId);
        if (origen == null || destino == null) return null;

        // Inicializar distancias y predecesores
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(
                Comparator.comparingInt(Map.Entry::getValue)
        );

        for (VerticeGrafo v : vertices) {
            String id = v.getNodoArbol().getDato().getId();
            dist.put(id, Integer.MAX_VALUE);
            prev.put(id, null);
        }
        dist.put(origenId, 0);
        pq.add(new AbstractMap.SimpleEntry<>(origenId, 0));

        while (!pq.isEmpty()) {
            Map.Entry<String, Integer> entry = pq.poll();
            String uId = entry.getKey();
            int d = entry.getValue();
            if (d > dist.get(uId)) continue;

            VerticeGrafo u = obtenerVertice(uId);
            if (u == null) continue;
            for (AristaGrafo a : u.getAristas()) {
                String vId = a.getDestino().getNodoArbol().getDato().getId();
                int nuevo = d + a.getPeso();
                if (nuevo < dist.get(vId)) {
                    dist.put(vId, nuevo);
                    prev.put(vId, uId);
                    pq.add(new AbstractMap.SimpleEntry<>(vId, nuevo));
                }
            }
        }

        // Si no hay camino al destino
        if (dist.get(destinoId) == Integer.MAX_VALUE) return null;

        // Reconstruir el camino
        List<Ubicacion> camino = new ArrayList<>();
        String actual = destinoId;
        while (actual != null) {
            VerticeGrafo v = obtenerVertice(actual);
            if (v == null) break;
            camino.add(0, v.getNodoArbol().getDato());
            actual = prev.get(actual);
        }
        return camino;
    }

    /**
     * Algoritmo de Prim para construir el Árbol de Expansión Mínima (MST) de la red de carreteras, conectando todas las ubicaciones con el menor costo total.
     * @param inicioId ID del vértice desde donde comenzar
     * @return Lista de aristas que forman el MST
     */
    public List<AristaGrafo> prim(String inicioId) {
        VerticeGrafo inicio = obtenerVertice(inicioId);
        if (inicio == null) return new ArrayList<>();

        Set<VerticeGrafo> enMST = new HashSet<>();
        PriorityQueue<AristaGrafo> pq = new PriorityQueue<>(Comparator.comparingInt(AristaGrafo::getPeso));
        List<AristaGrafo> mst = new ArrayList<>();

        enMST.add(inicio);
        pq.addAll(inicio.getAristas());

        while (!pq.isEmpty() && mst.size() < vertices.size() - 1) {
            AristaGrafo arista = pq.poll();
            VerticeGrafo destino = arista.getDestino();
            if (enMST.contains(destino)) continue;

            enMST.add(destino);
            mst.add(arista);
            for (AristaGrafo a : destino.getAristas()) {
                if (!enMST.contains(a.getDestino())) {
                    pq.add(a);
                }
            }
        }
        return mst;
    }
}