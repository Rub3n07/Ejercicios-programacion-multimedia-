
package com.example.mistareas;

import android.content.Context;
import android.graphics.Paint;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mistareas.databinding.ItemTareaBinding;

import java.util.List;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    // Interfaz para comunicar el Adapter con MainActivity
    public interface OnTareaListener {
        void onTareaPulsada(int posicion);
        void onMenuTarea(View ancla, int posicion);
        void onCrearMenuContextual(ContextMenu menu, int posicion);
    }

    private final List<Tarea> tareas;
    private final OnTareaListener listener;

    // Constructor
    public TareaAdapter(List<Tarea> tareas, OnTareaListener listener) {
        this.tareas = tareas;
        this.listener = listener;
    }

    // Crear una tarjeta
    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        ItemTareaBinding binding = ItemTareaBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new TareaViewHolder(binding);
    }

    // Introducir los datos en la tarjeta
    @Override
    public void onBindViewHolder(
            @NonNull TareaViewHolder holder, int position) {

        holder.mostrar(tareas.get(position));
    }

    // Devolver el numero de tareas
    @Override
    public int getItemCount() {
        return tareas.size();
    }

    // ViewHolder: guarda las referencias de cada tarjeta
    class TareaViewHolder extends RecyclerView.ViewHolder {

        private final ItemTareaBinding binding;

        TareaViewHolder(ItemTareaBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

            // Detectar clic en la tarjeta
            binding.getRoot().setOnClickListener(v -> {

                int pos = getBindingAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    listener.onTareaPulsada(pos);
                }
            });

            // Detectar clic en los tres puntos
            binding.btnMenuTarea.setOnClickListener(v -> {

                int pos = getBindingAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    listener.onMenuTarea(v, pos);
                }
            });

            // Detectar pulsacion larga
            binding.getRoot().setOnCreateContextMenuListener(
                    (menu, v, menuInfo) -> {

                        int pos = getBindingAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            listener.onCrearMenuContextual(menu, pos);
                        }
                    }
            );
        }

        // Mostrar los datos de la tarea
        void mostrar(Tarea tarea) {

            Context ctx = binding.getRoot().getContext();

            binding.tvTitulo.setText(tarea.getTitulo());
            binding.tvDescripcion.setText(tarea.getDescripcion());
            binding.cbCompletada.setChecked(tarea.isCompletada());

            // Tachado cuando la tarea esta completada
            int flags = binding.tvTitulo.getPaintFlags();

            if (tarea.isCompletada()) {

                binding.tvTitulo.setPaintFlags(
                        flags | Paint.STRIKE_THRU_TEXT_FLAG
                );

                binding.getRoot().setAlpha(0.6f);

            } else {

                binding.tvTitulo.setPaintFlags(
                        flags & ~Paint.STRIKE_THRU_TEXT_FLAG
                );

                binding.getRoot().setAlpha(1f);
            }

            // Cambiar texto y color segun la prioridad
            int texto;
            int color;

            switch (tarea.getPrioridad()) {

                case Tarea.PRIORIDAD_ALTA:
                    texto = R.string.prioridad_alta;
                    color = R.color.prioridad_alta;
                    break;

                case Tarea.PRIORIDAD_BAJA:
                    texto = R.string.prioridad_baja;
                    color = R.color.prioridad_baja;
                    break;

                default:
                    texto = R.string.prioridad_media;
                    color = R.color.prioridad_media;
            }

            binding.tvPrioridad.setText(texto);

            binding.tvPrioridad.setTextColor(
                    ContextCompat.getColor(ctx, color)
            );
        }
    }
}
