# 🌀 Sistema de Evacuación ante Huracanes – Pinar del Río

## 📌 Descripción del Proyecto

Este proyecto es una aplicación Java que integra un **Árbol General** y un **Grafo No Dirigido Ponderado** para gestionar la evacuación de la población ante huracanes en la provincia de Pinar del Río, Cuba.

### Funcionalidades principales:

- Administrar la jerarquía territorial (Provincia → Municipios → Comunidades → Refugios).
- Gestionar la red de carreteras con tiempos de viaje estimados.
- Calcular rutas de evacuación óptimas usando **Dijkstra**.
- Diseñar una red de carreteras de mínimo costo usando **Prim**.
- Explorar conexiones con **BFS** y **DFS**.
- Operaciones **CRUD** completas sobre ambas estructuras.

> **Asignatura:** Estructura de Datos II  
> **Profesor:** M.Sc. Marcos Miguel Llamazares Blanco  
> **Curso:** 2025-2026  
> **Autor:** Pedro Antonio Palacio González

## 🗂️ Estructura del Proyecto
src/
├── Main.java 
├── modelo/
│ ├── Ubicacion.java
│ ├── NodoGeneral.java
│ ├── ArbolGeneral.java
│ ├── AristaGrafo.java
│ ├── VerticeGrafo.java
│ └── Grafo.java
└── vista/
└── VentanaPrincipal.java 

## ⚙️ Requisitos

- **Java JDK 8 o superior** (probado con JDK 17).
- **IntelliJ IDEA** (recomendado) o cualquier otro IDE.

## 🚀 Cómo ejecutar

1. Clona el repositorio:
   ```bash
   git clone https://github.com/idk0petrus/sistema de evacuacion ante huracanes – pinar del rio.git
