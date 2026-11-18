
package com.example.ej24;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ej24.databinding.ItemLibroBinding;

import java.util.ArrayList;
import java.util.List;

public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.LibroViewHolder> {

    public interface OnLibroListener {
        void onCambiarLeido(Libro libro, boolean leido);
        void onEliminar(Libro libro);
    }

    private final List<Libro> libros = new ArrayList<>();
    private final OnLibroListener listener;

    public LibroAdapter(OnLibroListener listener) {
        this.listener = listener;
    }

    // Sustituir los libros de la lista
    public void setLibros(List<Libro> nuevos) {
        libros.clear();
        libros.addAll(nuevos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        ItemLibroBinding binding = ItemLibroBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new LibroViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull LibroViewHolder holder, int position) {

        holder.mostrar(libros.get(position));
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    class LibroViewHolder extends RecyclerView.ViewHolder {

        private final ItemLibroBinding binding;

        LibroViewHolder(ItemLibroBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

            // Marcar como leído
            binding.cbLeido.setOnClickListener(v -> {

                int pos = getBindingAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    listener.onCambiarLeido(
                            libros.get(pos),
                            binding.cbLeido.isChecked()
                    );
                }
            });

            // Eliminar libro
            binding.btnEliminar.setOnClickListener(v -> {

                int pos = getBindingAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    listener.onEliminar(libros.get(pos));
                }
            });
        }

        // Mostrar los datos
        void mostrar(Libro libro) {

            binding.tvTitulo.setText(libro.titulo);
            binding.tvAutor.setText(libro.autor);

            binding.tvPaginas.setText(
                    binding.getRoot().getContext().getString(
                            R.string.paginas_formato,
                            libro.paginas
                    )
            );

            binding.cbLeido.setChecked(libro.leido);
        }
    }
}
