package vista;

import modelo.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Ventana principal del Sistema de Evacuación.
 * Interfaz gráfica con JTree, JTable, JTextArea y botones.
 */
public class VentanaPrincipal extends JFrame {

    // Modelos de datos
    private ArbolGeneral arbol;
    private Grafo grafo;

    // Componentes GUI
    private JTree arbolJTree;
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode rootNode;

    private JTable tablaUbicaciones;
    private DefaultTableModel tableModel;

    private JTextArea areaResultados;

    // Campos de texto para entrada de datos
    private JTextField txtId, txtNombre, txtTipo, txtPoblacion, txtCapacidad;
    private JTextField txtIdPadre, txtIdOrigen, txtIdDestino, txtPeso;
    private JTextField txtBuscarId, txtEliminarId, txtModificarId;
    private JTextField txtDijkstraOrigen, txtDijkstraDestino;
    private JTextField txtBfsOrigen, txtDfsOrigen, txtPrimOrigen;

    // Constructor
    public VentanaPrincipal() {
        setTitle("Sistema de Evacuación ante Huracanes - Pinar del Río");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Inicializar modelos
        arbol = new ArbolGeneral();
        grafo = new Grafo();
        cargarDatosIniciales();

        // Construir la interfaz
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 3)); // 3 columnas: Árbol, Tabla, Resultados

        // Panel izquierdo: JTree (jerarquía)
        panelPrincipal.add(crearPanelArbol());

        // Panel central: Tabla de ubicaciones + botones CRUD
        panelPrincipal.add(crearPanelCentral());

        // Panel derecho: Resultados + algoritmos
        panelPrincipal.add(crearPanelDerecho());

        add(panelPrincipal, BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);

        // Actualizar la vista inicial
        actualizarArbol();
        actualizarTabla();
    }

    // Inicializar datos reales
    private void cargarDatosIniciales() {
        // Raíz
        Ubicacion provincia = new Ubicacion("PIN", "Pinar del Río", "Provincia");
        arbol.insertar(null, provincia);
        grafo.agregarVertice(arbol.getRaiz());

        // Municipios
        String[][] municipios = {
                {"PIN-VIN", "Viñales", "Municipio"},
                {"PIN-SJ", "San Juan y Martínez", "Municipio"},
                {"PIN-CON", "Consolación del Sur", "Municipio"},
                {"PIN-CAP", "Pinar del Río (Capital)", "Municipio"},
                {"PIN-GUA", "Guane", "Municipio"}
        };
        for (String[] m : municipios) {
            Ubicacion u = new Ubicacion(m[0], m[1], m[2]);
            arbol.insertar("PIN", u);
            grafo.agregarVertice(arbol.buscar(m[0]));
        }

        // Comunidades y Refugios
        insertarUbicacionAux("PIN-VIN", "PIN-VIN-PE", "Puerto Esperanza", "Comunidad", 850, 0);
        insertarUbicacionAux("PIN-VIN", "PIN-VIN-MO", "El Moncada", "Comunidad", 400, 0);
        insertarUbicacionAux("PIN-VIN", "PIN-VIN-SA", "La Sabana", "Comunidad", 300, 0);
        insertarUbicacionAux("PIN-VIN", "PIN-VIN-R1", "Escuela Primaria René Ramos", "Refugio", 0, 120);
        insertarUbicacionAux("PIN-VIN", "PIN-VIN-R2", "Casa de Cultura de Viñales", "Refugio", 0, 80);

        insertarUbicacionAux("PIN-SJ", "PIN-SJ-LC", "La Coloma", "Comunidad", 1200, 0);
        insertarUbicacionAux("PIN-SJ", "PIN-SJ-BA", "Báez", "Comunidad", 600, 0);
        insertarUbicacionAux("PIN-SJ", "PIN-SJ-R1", "Politécnico San Juan", "Refugio", 0, 200);

        insertarUbicacionAux("PIN-CON", "PIN-CON-PI", "Pilotos", "Comunidad", 500, 0);
        insertarUbicacionAux("PIN-CON", "PIN-CON-ST", "Santa Teresa", "Comunidad", 700, 0);
        insertarUbicacionAux("PIN-CON", "PIN-CON-R1", "Escuela Secundaria Consolación", "Refugio", 0, 150);

        insertarUbicacionAux("PIN-CAP", "PIN-CAP-HC", "Hermanos Cruz", "Comunidad", 2000, 0);
        insertarUbicacionAux("PIN-CAP", "PIN-CAP-LC", "La Conchita", "Comunidad", 1500, 0);
        insertarUbicacionAux("PIN-CAP", "PIN-CAP-R1", "Universidad de Pinar del Río (UPR)", "Refugio", 0, 500);
        insertarUbicacionAux("PIN-CAP", "PIN-CAP-R2", "Estadio Capitán San Luis", "Refugio", 0, 300);

        insertarUbicacionAux("PIN-GUA", "PIN-GUA-EC", "El Calvo", "Comunidad", 400, 0);
        insertarUbicacionAux("PIN-GUA", "PIN-GUA-R1", "Secundaria Básica Guane", "Refugio", 0, 100);

        // Rutas
        agregarRuta("PIN-CAP", "PIN-VIN", 25);
        agregarRuta("PIN-CAP", "PIN-SJ", 15);
        agregarRuta("PIN-CAP", "PIN-CON", 20);
        agregarRuta("PIN-CAP", "PIN-GUA", 45);
        agregarRuta("PIN-VIN", "PIN-SJ", 40);
        agregarRuta("PIN-SJ", "PIN-CON", 35);

        agregarRuta("PIN-VIN", "PIN-VIN-PE", 15);
        agregarRuta("PIN-VIN", "PIN-VIN-MO", 10);
        agregarRuta("PIN-VIN", "PIN-VIN-SA", 20);
        agregarRuta("PIN-VIN-PE", "PIN-VIN-R1", 5);
        agregarRuta("PIN-VIN-MO", "PIN-VIN-R2", 7);
        agregarRuta("PIN-VIN-SA", "PIN-VIN-R1", 12);
        agregarRuta("PIN-VIN-PE", "PIN-VIN-R2", 10);

        agregarRuta("PIN-SJ", "PIN-SJ-LC", 12);
        agregarRuta("PIN-SJ", "PIN-SJ-BA", 18);
        agregarRuta("PIN-SJ-LC", "PIN-SJ-R1", 6);
        agregarRuta("PIN-SJ-BA", "PIN-SJ-R1", 10);

        agregarRuta("PIN-CON", "PIN-CON-PI", 8);
        agregarRuta("PIN-CON", "PIN-CON-ST", 12);
        agregarRuta("PIN-CON-PI", "PIN-CON-R1", 5);
        agregarRuta("PIN-CON-ST", "PIN-CON-R1", 7);

        agregarRuta("PIN-CAP", "PIN-CAP-HC", 5);
        agregarRuta("PIN-CAP", "PIN-CAP-LC", 8);
        agregarRuta("PIN-CAP-HC", "PIN-CAP-R1", 10);
        agregarRuta("PIN-CAP-HC", "PIN-CAP-R2", 6);
        agregarRuta("PIN-CAP-LC", "PIN-CAP-R1", 12);

        agregarRuta("PIN-GUA", "PIN-GUA-EC", 15);
        agregarRuta("PIN-GUA-EC", "PIN-GUA-R1", 7);
    }

    private void insertarUbicacionAux(String idPadre, String id, String nombre, String tipo, int poblacion, int capacidad) {
        Ubicacion u = new Ubicacion(id, nombre, tipo);
        u.setPoblacion(poblacion);
        u.setCapacidad(capacidad);
        if (arbol.insertar(idPadre, u)) {
            NodoGeneral nodo = arbol.buscar(id);
            if (nodo != null) grafo.agregarVertice(nodo);
        }
    }

    private void agregarRuta(String id1, String id2, int peso) {
        grafo.agregarArista(id1, id2, peso);
    }

    // Panel izquierdo: Árbol
    private JPanel crearPanelArbol() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Jerarquía Territorial"));

        rootNode = new DefaultMutableTreeNode("Pinar del Río");
        treeModel = new DefaultTreeModel(rootNode);
        arbolJTree = new JTree(treeModel);
        arbolJTree.setShowsRootHandles(true);

        JScrollPane scroll = new JScrollPane(arbolJTree);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // Panel central: Tabla + CRUD
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Ubicaciones"));

        // Tabla
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Tipo", "Población", "Capacidad"}, 0);
        tablaUbicaciones = new JTable(tableModel);
        JScrollPane scrollTabla = new JScrollPane(tablaUbicaciones);
        panel.add(scrollTabla, BorderLayout.CENTER);

        // Panel de botones CRUD
        JPanel panelCRUD = new JPanel(new GridLayout(3, 4, 5, 5));
        panelCRUD.setBorder(BorderFactory.createTitledBorder("CRUD"));

        // Campos de entrada
        txtId = new JTextField(10);
        txtNombre = new JTextField(10);
        txtTipo = new JTextField(10);
        txtPoblacion = new JTextField(10);
        txtCapacidad = new JTextField(10);
        txtIdPadre = new JTextField(10);
        txtBuscarId = new JTextField(10);
        txtEliminarId = new JTextField(10);
        txtModificarId = new JTextField(10);

        panelCRUD.add(new JLabel("ID Padre:"));
        panelCRUD.add(txtIdPadre);
        panelCRUD.add(new JLabel("ID:"));
        panelCRUD.add(txtId);
        panelCRUD.add(new JLabel("Nombre:"));
        panelCRUD.add(txtNombre);
        panelCRUD.add(new JLabel("Tipo:"));
        panelCRUD.add(txtTipo);
        panelCRUD.add(new JLabel("Población:"));
        panelCRUD.add(txtPoblacion);
        panelCRUD.add(new JLabel("Capacidad:"));
        panelCRUD.add(txtCapacidad);

        JButton btnInsertar = new JButton("Insertar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnModificar = new JButton("Modificar");

        btnInsertar.addActionListener(e -> insertarUbicacion());
        btnBuscar.addActionListener(e -> buscarUbicacion());
        btnEliminar.addActionListener(e -> eliminarUbicacion());
        btnModificar.addActionListener(e -> modificarUbicacion());

        panelCRUD.add(btnInsertar);
        panelCRUD.add(new JLabel("Buscar ID:"));
        panelCRUD.add(txtBuscarId);
        panelCRUD.add(btnBuscar);
        panelCRUD.add(new JLabel("Eliminar ID:"));
        panelCRUD.add(txtEliminarId);
        panelCRUD.add(btnEliminar);
        panelCRUD.add(new JLabel("Modificar ID:"));
        panelCRUD.add(txtModificarId);
        panelCRUD.add(btnModificar);

        panel.add(panelCRUD, BorderLayout.SOUTH);

        return panel;
    }

    // Panel derecho: Algoritmos + Resultados
    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Algoritmos y Resultados"));

        // Área de resultados
        areaResultados = new JTextArea(15, 30);
        areaResultados.setEditable(false);
        JScrollPane scrollResultados = new JScrollPane(areaResultados);
        panel.add(scrollResultados, BorderLayout.CENTER);

        // Panel de algoritmos
        JPanel panelAlgoritmos = new JPanel(new GridLayout(6, 2, 5, 5));
        panelAlgoritmos.setBorder(BorderFactory.createTitledBorder("Algoritmos"));

        txtDijkstraOrigen = new JTextField(8);
        txtDijkstraDestino = new JTextField(8);
        txtBfsOrigen = new JTextField(8);
        txtDfsOrigen = new JTextField(8);
        txtPrimOrigen = new JTextField(8);
        txtIdOrigen = new JTextField(8);
        txtIdDestino = new JTextField(8);
        txtPeso = new JTextField(8);

        JButton btnDijkstra = new JButton("Dijkstra");
        JButton btnBFS = new JButton("BFS");
        JButton btnDFS = new JButton("DFS");
        JButton btnPrim = new JButton("Prim");
        JButton btnAgregarRuta = new JButton("Agregar Ruta");
        JButton btnEliminarRuta = new JButton("Eliminar Ruta");
        JButton btnMostrarRed = new JButton("Mostrar Red");

        btnDijkstra.addActionListener(e -> ejecutarDijkstra());
        btnBFS.addActionListener(e -> ejecutarBFS());
        btnDFS.addActionListener(e -> ejecutarDFS());
        btnPrim.addActionListener(e -> ejecutarPrim());
        btnAgregarRuta.addActionListener(e -> agregarRutaGUI());
        btnEliminarRuta.addActionListener(e -> eliminarRutaGUI());
        btnMostrarRed.addActionListener(e -> mostrarRed());

        panelAlgoritmos.add(new JLabel("Origen Dijkstra:"));
        panelAlgoritmos.add(txtDijkstraOrigen);
        panelAlgoritmos.add(new JLabel("Destino Dijkstra:"));
        panelAlgoritmos.add(txtDijkstraDestino);
        panelAlgoritmos.add(btnDijkstra);
        panelAlgoritmos.add(btnBFS);
        panelAlgoritmos.add(new JLabel("Origen BFS:"));
        panelAlgoritmos.add(txtBfsOrigen);
        panelAlgoritmos.add(new JLabel("Origen DFS:"));
        panelAlgoritmos.add(txtDfsOrigen);
        panelAlgoritmos.add(btnDFS);
        panelAlgoritmos.add(btnPrim);
        panelAlgoritmos.add(new JLabel("Origen Prim:"));
        panelAlgoritmos.add(txtPrimOrigen);
        panelAlgoritmos.add(new JLabel("Origen Ruta:"));
        panelAlgoritmos.add(txtIdOrigen);
        panelAlgoritmos.add(new JLabel("Destino Ruta:"));
        panelAlgoritmos.add(txtIdDestino);
        panelAlgoritmos.add(new JLabel("Peso:"));
        panelAlgoritmos.add(txtPeso);
        panelAlgoritmos.add(btnAgregarRuta);
        panelAlgoritmos.add(btnEliminarRuta);
        panelAlgoritmos.add(btnMostrarRed);

        panel.add(panelAlgoritmos, BorderLayout.SOUTH);

        return panel;
    }

    // Panel inferior
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblInfo = new JLabel("Seleccione una ubicación en la tabla o ingrese IDs para operaciones.");
        panel.add(lblInfo);
        return panel;
    }

    // Métodos de actualización
    private void actualizarArbol() {
        rootNode.removeAllChildren();
        if (arbol.getRaiz() != null) {
            construirNodoArbol(rootNode, arbol.getRaiz());
        }
        treeModel.reload();
    }

    private void construirNodoArbol(DefaultMutableTreeNode nodoGUI, NodoGeneral nodoArbol) {
        for (NodoGeneral hijo : nodoArbol.getHijos()) {
            DefaultMutableTreeNode hijoGUI = new DefaultMutableTreeNode(hijo.getDato().toString());
            nodoGUI.add(hijoGUI);
            construirNodoArbol(hijoGUI, hijo);
        }
    }

    private void actualizarTabla() {
        tableModel.setRowCount(0);
        List<Ubicacion> lista = arbol.preorden();
        for (Ubicacion u : lista) {
            tableModel.addRow(new Object[]{u.getId(), u.getNombre(), u.getTipo(),
                    u.getPoblacion() > 0 ? u.getPoblacion() : "-",
                    u.getCapacidad() > 0 ? u.getCapacidad() : "-"});
        }
    }

    // Acciones
    private void insertarUbicacion() {
        String idPadre = txtIdPadre.getText().trim();
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String tipo = txtTipo.getText().trim();
        int poblacion = 0, capacidad = 0;
        try {
            if (!txtPoblacion.getText().trim().isEmpty())
                poblacion = Integer.parseInt(txtPoblacion.getText().trim());
            if (!txtCapacidad.getText().trim().isEmpty())
                capacidad = Integer.parseInt(txtCapacidad.getText().trim());
        } catch (NumberFormatException ex) {
            areaResultados.append("Error: Población o Capacidad inválidos.\n");
            return;
        }

        Ubicacion u = new Ubicacion(id, nombre, tipo);
        u.setPoblacion(poblacion);
        u.setCapacidad(capacidad);
        if (arbol.insertar(idPadre, u)) {
            NodoGeneral nodo = arbol.buscar(id);
            if (nodo != null) grafo.agregarVertice(nodo);
            areaResultados.append("Ubicación insertada: " + id + "\n");
            actualizarArbol();
            actualizarTabla();
        } else {
            areaResultados.append("Error: No se pudo insertar. Verifique ID padre.\n");
        }
    }

    private void buscarUbicacion() {
        String id = txtBuscarId.getText().trim();
        NodoGeneral nodo = arbol.buscar(id);
        if (nodo != null) {
            areaResultados.append("Encontrado: " + nodo.getDato() + "\n");
        } else {
            areaResultados.append("No encontrado: " + id + "\n");
        }
    }

    private void eliminarUbicacion() {
        String id = txtEliminarId.getText().trim();
        grafo.eliminarVertice(id);
        if (arbol.eliminar(id)) {
            areaResultados.append("Eliminado: " + id + "\n");
            actualizarArbol();
            actualizarTabla();
        } else {
            areaResultados.append("Error: No se pudo eliminar " + id + " (raíz o no existe).\n");
        }
    }

    private void modificarUbicacion() {
        String id = txtModificarId.getText().trim();
        NodoGeneral nodo = arbol.buscar(id);
        if (nodo == null) {
            areaResultados.append("No encontrado: " + id + "\n");
            return;
        }

        String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre:", nodo.getDato().getNombre());
        String nuevoTipo = JOptionPane.showInputDialog("Nuevo tipo:", nodo.getDato().getTipo());
        if (nuevoNombre != null && !nuevoNombre.isEmpty()) {
            arbol.modificar(id, nuevoNombre, nuevoTipo, nodo.getDato().getCapacidad(), nodo.getDato().getPoblacion());
            areaResultados.append("Modificado: " + id + "\n");
            actualizarArbol();
            actualizarTabla();
        }
    }

    // Acciones de rutas
    private void agregarRutaGUI() {
        String origen = txtIdOrigen.getText().trim();
        String destino = txtIdDestino.getText().trim();
        int peso;
        try {
            peso = Integer.parseInt(txtPeso.getText().trim());
        } catch (NumberFormatException e) {
            areaResultados.append("Error: Peso inválido.\n");
            return;
        }
        if (grafo.agregarArista(origen, destino, peso)) {
            areaResultados.append("Ruta agregada: " + origen + " -> " + destino + " (" + peso + " min)\n");
        } else {
            areaResultados.append("Error: No se pudo agregar la ruta.\n");
        }
    }

    private void eliminarRutaGUI() {
        String origen = txtIdOrigen.getText().trim();
        String destino = txtIdDestino.getText().trim();
        if (grafo.eliminarArista(origen, destino)) {
            areaResultados.append("Ruta eliminada: " + origen + " -> " + destino + "\n");
        } else {
            areaResultados.append("Error: No se pudo eliminar la ruta.\n");
        }
    }

    private void mostrarRed() {
        areaResultados.append("\n--- Red de Carreteras ---\n");
        for (VerticeGrafo v : grafo.getVertices()) {
            String salida = v.getNodoArbol().getDato().getId() + " -> ";
            for (AristaGrafo a : v.getAristas()) {
                salida += a.toString() + " ";
            }
            areaResultados.append(salida + "\n");
        }
    }

    // Algoritmos
    private void ejecutarDijkstra() {
        String origen = txtDijkstraOrigen.getText().trim();
        String destino = txtDijkstraDestino.getText().trim();
        List<Ubicacion> camino = grafo.dijkstra(origen, destino);
        areaResultados.append("\n--- Dijkstra (" + origen + " -> " + destino + ") ---\n");
        if (camino == null) {
            areaResultados.append("No existe ruta.\n");
        } else {
            for (Ubicacion u : camino) {
                areaResultados.append("  " + u + "\n");
            }
            int total = calcularTiempoTotal(camino);
            areaResultados.append("Tiempo total: " + total + " minutos.\n");
        }
    }

    private void ejecutarBFS() {
        String id = txtBfsOrigen.getText().trim();
        List<Ubicacion> resultado = grafo.bfs(id);
        areaResultados.append("\n--- BFS desde " + id + " ---\n");
        if (resultado.isEmpty()) {
            areaResultados.append("No hay conexiones.\n");
        } else {
            for (Ubicacion u : resultado) {
                areaResultados.append("  " + u + "\n");
            }
        }
    }

    private void ejecutarDFS() {
        String id = txtDfsOrigen.getText().trim();
        List<Ubicacion> resultado = grafo.dfs(id);
        areaResultados.append("\n--- DFS desde " + id + " ---\n");
        if (resultado.isEmpty()) {
            areaResultados.append("No hay conexiones.\n");
        } else {
            for (Ubicacion u : resultado) {
                areaResultados.append("  " + u + "\n");
            }
        }
    }

    private void ejecutarPrim() {
        String inicio = txtPrimOrigen.getText().trim();
        List<AristaGrafo> mst = grafo.prim(inicio);
        areaResultados.append("\n--- Prim (MST desde " + inicio + ") ---\n");
        if (mst.isEmpty()) {
            areaResultados.append("No se puede construir la red.\n");
        } else {
            int total = 0;
            for (AristaGrafo a : mst) {
                areaResultados.append("  " + a.toString() + "\n");
                total += a.getPeso();
            }
            areaResultados.append("Costo total: " + total + " minutos.\n");
        }
    }

    private int calcularTiempoTotal(List<Ubicacion> camino) {
        if (camino.size() < 2) return 0;
        int total = 0;
        for (int i = 0; i < camino.size() - 1; i++) {
            String id1 = camino.get(i).getId();
            String id2 = camino.get(i + 1).getId();
            VerticeGrafo v1 = grafo.obtenerVertice(id1);
            if (v1 == null) continue;
            for (AristaGrafo a : v1.getAristas()) {
                if (a.getDestino().getNodoArbol().getDato().getId().equals(id2)) {
                    total += a.getPeso();
                    break;
                }
            }
        }
        return total;
    }
}