
package com.example.mistareas;

public class Tarea {

    // Constantes de prioridad
    public static final int PRIORIDAD_BAJA = 0;
    public static final int PRIORIDAD_MEDIA = 1;
    public static final int PRIORIDAD_ALTA = 2;

    // Atributos de la tarea
    private String titulo;
    private String descripcion;
    private int prioridad;
    private boolean completada;

    // Constructor
    public Tarea(String titulo, String descripcion, int prioridad) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.completada = false;
    }

    // Obtener titulo
    public String getTitulo() {
        return titulo;
    }

    // Modificar titulo
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // Obtener descripcion
    public String getDescripcion() {
        return descripcion;
    }

    // Modificar descripcion
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Obtener prioridad
    public int getPrioridad() {
        return prioridad;
    }

    // Modificar prioridad
    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    // Comprobar si esta completada
    public boolean isCompletada() {
        return completada;
    }

    // Cambiar estado de la tarea
    public void setCompletada(boolean completada) {
        this.completada = completada;
    }
}
