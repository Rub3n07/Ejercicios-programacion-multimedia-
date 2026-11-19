
package com.example.ej24;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ej24.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity
        implements LibroAdapter.OnLibroListener {

    private ActivityMainBinding binding;
    private LibroDao dao;
    private LibroAdapter adapter;

    // Hilo secundario para trabajar con la base de datos
    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtener la base de datos y el DAO
        dao = AppDatabase.getInstance(this).libroDao();

        // Preparar el RecyclerView
        adapter = new LibroAdapter(this);

        binding.rvLibros.setLayoutManager(
                new LinearLayoutManager(this)
        );

        binding.rvLibros.setAdapter(adapter);

        // Botón para añadir libros
        binding.btnAnadir.setOnClickListener(v -> anadirLibro());

        // Cargar los libros guardados
        cargarLibros();
    }

    // ==========================================
    // LEER LIBROS
    // ==========================================

    private void cargarLibros() {

        executor.execute(() -> {

            // HILO SECUNDARIO
            List<Libro> lista = dao.obtenerTodos();
            int leidos = dao.contarLeidos();

            // Volver al hilo principal
            runOnUiThread(() -> mostrar(lista, leidos));
        });
    }

    private void mostrar(List<Libro> lista, int leidos) {

        adapter.setLibros(lista);

        binding.tvVacio.setVisibility(
                lista.isEmpty() ? View.VISIBLE : View.GONE
        );

        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(
                    getString(
                            R.string.resumen_libros,
                            lista.size(),
                            leidos
                    )
            );
        }
    }

    // ==========================================
    // AÑADIR LIBROS
    // ==========================================

    private void anadirLibro() {

        String titulo = texto(binding.etTitulo.getText());
        String autor = texto(binding.etAutor.getText());
        String paginasTexto = texto(binding.etPaginas.getText());

        binding.tilTitulo.setError(null);
        binding.tilAutor.setError(null);
        binding.tilPaginas.setError(null);

        boolean correcto = true;

        if (titulo.isEmpty()) {
            binding.tilTitulo.setError(
                    getString(R.string.error_obligatorio)
            );
            correcto = false;
        }

        if (autor.isEmpty()) {
            binding.tilAutor.setError(
                    getString(R.string.error_obligatorio)
            );
            correcto = false;
        }

        int paginas = 0;

        try {
            paginas = Integer.parseInt(paginasTexto);
        } catch (NumberFormatException ignored) {
        }

        if (paginas <= 0) {
            binding.tilPaginas.setError(
                    getString(R.string.error_paginas)
            );
            correcto = false;
        }

        if (!correcto) {
            return;
        }

        Libro nuevo = new Libro(titulo, autor, paginas);

        executor.execute(() -> {

            // Insertar en la base de datos
            dao.insertar(nuevo);

            runOnUiThread(() -> {

                // Limpiar formulario
                binding.etTitulo.setText("");
                binding.etAutor.setText("");
                binding.etPaginas.setText("");

                binding.etTitulo.requestFocus();

                Toast.makeText(
                        this,
                        R.string.libro_guardado,
                        Toast.LENGTH_SHORT
                ).show();
            });

            // Actualizar lista
            cargarLibros();
        });
    }

    // ==========================================
    // ACTUALIZAR LIBRO
    // ==========================================

    @Override
    public void onCambiarLeido(Libro libro, boolean leido) {

        libro.leido = leido;

        executor.execute(() -> {

            dao.actualizar(libro);

            cargarLibros();
        });
    }

    // ==========================================
    // ELIMINAR LIBRO
    // ==========================================

    @Override
    public void onEliminar(Libro libro) {

        executor.execute(() -> {

            dao.eliminar(libro);

            cargarLibros();

            runOnUiThread(() -> {

                Snackbar.make(
                        binding.getRoot(),
                        R.string.libro_eliminado,
                        Snackbar.LENGTH_LONG
                ).setAction(
                        R.string.deshacer,
                        v -> executor.execute(() -> {

                            // Recuperar el libro eliminado
                            dao.insertar(libro);

                            cargarLibros();
                        })
                ).show();
            });
        });
    }

    // ==========================================
    // CERRAR APLICACIÓN
    // ==========================================

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Liberar el hilo
        executor.shutdown();
    }

    // ==========================================
    // CONVERTIR TEXTO
    // ==========================================

    private String texto(CharSequence cs) {
        return cs == null ? "" : cs.toString().trim();
    }
}
