
package com.example.ej24;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LibroDao {

    // Obtener todos los libros ordenados por título
    @Query("SELECT * FROM libros ORDER BY titulo")
    List<Libro> obtenerTodos();

    // Contar los libros leídos
    @Query("SELECT COUNT(*) FROM libros WHERE leido = 1")
    int contarLeidos();

    // Insertar un libro
    @Insert
    long insertar(Libro libro);

    // Actualizar un libro
    @Update
    void actualizar(Libro libro);

    // Eliminar un libro
    @Delete
    void eliminar(Libro libro);
}
